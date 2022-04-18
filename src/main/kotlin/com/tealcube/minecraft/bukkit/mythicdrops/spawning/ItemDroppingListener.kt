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
package com.tealcube.minecraft.bukkit.mythicdrops.spawning

import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.getThenSetItemMetaAsDamageable
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicDropTracker
import com.tealcube.minecraft.bukkit.mythicdrops.utils.BroadcastMessageUtil.broadcastItem
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getCustomItem
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getDurabilityInPercentageRange
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getSocketGem
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getTier
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

internal class ItemDroppingListener(private val mythicDrops: MythicDrops, private val audiences: BukkitAudiences) :
    Listener {
    @EventHandler
    fun onEntityDeathEvent(event: EntityDeathEvent) {
        if (shouldNotHandleDeathEvent(event)) return

        if (mythicDrops.settingsManager.configSettings.options.isDisplayMobEquipment) {
            handleEntityDeathEventWithGive(event)
        } else {
            handleEntityDeathEventWithoutGive(event)
        }
    }

    private fun handleEntityDeathEventWithGive(event: EntityDeathEvent) {
        val itemsToIterateThrough = event.drops
        itemsToIterateThrough.forEachIndexed { idx, item ->
            // check if custom item and announce
            item.getCustomItem(
                mythicDrops.customItemManager,
            )?.let {
                handleCustomItemDropAtIndex(event, idx, item, it)
            }
            // check if tier and announce
            item.getTier(mythicDrops.tierManager)?.let {
                handleTierDropAtIndex(event, idx, item, it)
            }
            // check if socket gem and announce
            item.getSocketGem(
                mythicDrops.socketGemManager,
                mythicDrops.settingsManager.socketingSettings,
            )?.let {
                handleSocketGemDropAtIndex(event, item, it)
            }
        }
    }

    private fun handleCustomItemDropAtIndex(
        event: EntityDeathEvent,
        idxOfItemInDrops: Int,
        item: ItemStack,
        it: CustomItem
    ) {
        event.drops[idxOfItemInDrops] = item.clone().apply {
            if (it.hasDurability) {
                getThenSetItemMetaAsDamageable { damage = it.durability }
            } else {
                getThenSetItemMetaAsDamageable { damage = 0 }
            }
        }
        val killer = event.entity.killer
        if (it.isBroadcastOnFind && killer != null) {
            broadcastItem(
                mythicDrops.settingsManager.languageSettings,
                killer,
                item,
                audiences,
                mythicDrops.settingsManager.configSettings.drops.broadcastTarget
            )
        }
    }

    private fun handleTierDropAtIndex(
        event: EntityDeathEvent,
        idxOfItemInDrops: Int,
        item: ItemStack,
        it: Tier
    ) {
        event.drops[idxOfItemInDrops] = item.clone().apply {
            val durabilityValue = type.getDurabilityInPercentageRange(
                it.minimumDurabilityPercentage,
                it.maximumDurabilityPercentage
            )
            getThenSetItemMetaAsDamageable { damage = durabilityValue }
        }
        val killer = event.entity.killer
        if (it.isBroadcastOnFind && killer != null) {
            broadcastItem(
                mythicDrops.settingsManager.languageSettings,
                killer,
                item,
                audiences,
                mythicDrops.settingsManager.configSettings.drops.broadcastTarget
            )
        }
    }

    private fun handleSocketGemDropAtIndex(
        event: EntityDeathEvent,
        item: ItemStack,
        it: SocketGem
    ) {
        val killer = event.entity.killer
        if (it.isBroadcastOnFind && killer != null) {
            broadcastItem(
                mythicDrops.settingsManager.languageSettings,
                killer,
                item,
                audiences,
                mythicDrops.settingsManager.configSettings.drops.broadcastTarget
            )
        }
    }

    private fun handleEntityDeathEventWithoutGive(event: EntityDeathEvent) {
        val dropStrategy =
            mythicDrops.dropStrategyManager.getById(mythicDrops.settingsManager.configSettings.drops.strategy)
                ?: return

        MythicDropTracker.spawn()
        val drops = dropStrategy.getDropsForEntityDeathEvent(event)

        drops.forEach {
            val itemStack = it.first
            val dropChance = it.second

            val tier = itemStack.getTier(mythicDrops.tierManager)
            val customItem =
                itemStack.getCustomItem(
                    mythicDrops.customItemManager
                )
            val socketGem = itemStack.getSocketGem(
                mythicDrops.socketGemManager,
                mythicDrops.settingsManager.socketingSettings
            )

            val broadcast =
                tier?.isBroadcastOnFind ?: customItem?.isBroadcastOnFind ?: socketGem?.isBroadcastOnFind ?: false
            val killer = event.entity.killer

            if (itemStack.amount > 0 && !itemStack.type.isAir && Random.nextDouble(0.0, 1.0) <= dropChance) {
                event.drops.add(itemStack)
                if (broadcast && killer != null) {
                    broadcastItem(
                        mythicDrops.settingsManager.languageSettings,
                        killer,
                        itemStack,
                        audiences,
                        mythicDrops.settingsManager.configSettings.drops.broadcastTarget
                    )
                }
            }
        }
    }

    private fun shouldNotHandleDeathEvent(event: EntityDeathEvent): Boolean {
        return when {
            event.entity is Player -> true
            event.entity.lastDamageCause == null -> true
            event.entity.lastDamageCause?.isCancelled == true -> true // :|
            !mythicDrops
                .settingsManager
                .configSettings
                .multiworld
                .enabledWorlds
                .contains(event.entity.world.name) -> true
            requirePlayerKillForDrops(event) -> true
            else -> false
        }
    }

    private fun requirePlayerKillForDrops(event: EntityDeathEvent): Boolean {
        return !mythicDrops.settingsManager.configSettings.options.isDisplayMobEquipment &&
            mythicDrops.settingsManager.configSettings.options.isRequirePlayerKillForDrops &&
            event.entity.killer == null
    }
}
