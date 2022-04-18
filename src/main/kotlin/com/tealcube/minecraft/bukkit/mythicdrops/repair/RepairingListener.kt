/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2020 Richard Harrah
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.repair

import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairCost
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.containsAtLeast
import com.tealcube.minecraft.bukkit.mythicdrops.getFromItemMetaAsDamageable
import com.tealcube.minecraft.bukkit.mythicdrops.getThenSetItemMetaAsDamageable
import com.tealcube.minecraft.bukkit.mythicdrops.removeItem
import com.tealcube.minecraft.bukkit.mythicdrops.sendMythicMessage
import io.pixeloutlaw.minecraft.spigot.experience.PlayerExperience
import io.pixeloutlaw.minecraft.spigot.mythicdrops.displayName
import io.pixeloutlaw.minecraft.spigot.mythicdrops.lore
import io.pixeloutlaw.minecraft.spigot.orPrerequisite
import io.pixeloutlaw.minecraft.spigot.prerequisites
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

internal class RepairingListener(
    private val repairItemManager: RepairItemManager,
    private val settingsManager: SettingsManager
) : Listener {
    companion object {
        private const val REPAIRING_COOLDOWN_IN_SECONDS: Long = 3
    }

    private val repairingMap = mutableMapOf<UUID, Instant>()

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPlayerInteractEvent(event: PlayerInteractEvent) {
        // check our prerequisites for repairing
        val prereqs = prerequisites {
            prerequisite { event.useItemInHand() != Event.Result.DENY }
            prerequisite { event.useInteractedBlock() != Event.Result.DENY }
            prerequisite { event.action == Action.LEFT_CLICK_BLOCK }
            prerequisite { settingsManager.configSettings.components.isRepairingEnabled }
            prerequisite { event.clickedBlock?.type == Material.ANVIL }
            prerequisite { event.player.hasPermission("mythicdrops.repair") }
            prerequisite(
                orPrerequisite {
                    // if the player isn't sneaking
                    prerequisite { !event.player.isSneaking }
                    // if the player is sneaking AND we're allowing repairing while sneaking
                    prerequisite {
                        event.player.isSneaking && settingsManager.repairingSettings.isAllowRepairingWhileSneaking
                    }
                }
            )
            prerequisite(
                orPrerequisite {
                    // if the player hasn't repaired at all
                    prerequisite { !repairingMap.containsKey(event.player.uniqueId) }
                    // if the player has repaired recently, is it outside our cooldown?
                    prerequisite {
                        repairingMap.getOrDefault(event.player.uniqueId, Instant.now())
                            .plus(REPAIRING_COOLDOWN_IN_SECONDS, ChronoUnit.SECONDS)
                            .isBefore(Instant.now())
                    }
                }
            )
        }
        if (!prereqs) {
            return
        }

        val player = event.player
        val itemInMainHand = player.equipment?.itemInMainHand
        if (itemInMainHand == null) {
            return
        }

        handleRepairingItemInMainHand(player, itemInMainHand)
    }

    private fun handleRepairingItemInMainHand(player: Player, itemInMainHand: ItemStack) {
        val prereqs =
            prerequisites {
                prerequisite { itemInMainHand.getFromItemMetaAsDamageable { hasDamage() } ?: false }
                prerequisite { !itemInMainHand.type.isAir }
            }
        if (!prereqs) {
            return
        }
        val repairItem = repairItemManager.get().find {
            val matchesMaterial = it.material == itemInMainHand.type
            val matchesName =
                it.itemName?.let { name -> name.chatColorize() == itemInMainHand.displayName } ?: true
            val matchesLore = if (it.itemLore.isNotEmpty()) {
                it.itemLore.chatColorize() == itemInMainHand.lore
            } else {
                true
            }
            matchesMaterial && matchesName && matchesLore
        }
        val repairCost: RepairCost? = repairItem?.let { getRepairCost(it.repairCosts, player) }
        repairAndMessage(player, repairItem, repairCost, itemInMainHand)
    }

    private fun repairAndMessage(
        player: Player,
        repairItem: RepairItem?,
        repairCost: RepairCost?,
        itemInMainHand: ItemStack
    ) {
        val equipment = player.equipment
        if (repairItem == null || equipment == null) {
            player.sendMythicMessage(settingsManager.languageSettings.repairing.cannotUse)
            return
        }
        if (repairCost == null) {
            player.sendMythicMessage(settingsManager.languageSettings.repairing.doNotHave)
            return
        }
        equipment.setItemInMainHand(repairItemStack(itemInMainHand, repairCost))
        player.inventory.removeItem(
            repairCost.material,
            repairCost.itemName,
            repairCost.itemLore,
            repairCost.enchantments,
            repairCost.amount
        )
        player.inventory.viewers.forEach { (it as? Player)?.updateInventory() }
        player.sendMythicMessage(settingsManager.languageSettings.repairing.success)
    }

    private fun getRepairCost(repairCosts: Collection<RepairCost>, player: Player): RepairCost? {
        var repairCost: RepairCost? = null

        for (cost in repairCosts) {
            val inventoryContains = player.inventory.containsAtLeast(
                cost.material,
                cost.itemName,
                cost.itemLore,
                cost.enchantments,
                cost.amount
            )
            val hasExperience = PlayerExperience.hasExp(player, cost.experienceCost)
            if (inventoryContains && hasExperience) {
                if (repairCost == null) {
                    repairCost = cost
                    continue
                }
                // we want the one with the highest priority
                if (cost.priority > repairCost.priority) {
                    repairCost = cost
                }
            }
        }

        return repairCost
    }

    /**
     * Returns a repaired copy of the given ItemStack.
     */
    private fun repairItemStack(itemStack: ItemStack, repairCost: RepairCost): ItemStack {
        val repaired = itemStack.clone()
        val currentDamage = itemStack.getFromItemMetaAsDamageable { damage } ?: 0
        val newDamage =
            currentDamage - (itemStack.type.maxDurability * repairCost.repairPercentagePerCost).toInt().coerceAtLeast(0)
        repaired.getThenSetItemMetaAsDamageable { damage = newDamage }
        return repaired
    }
}
