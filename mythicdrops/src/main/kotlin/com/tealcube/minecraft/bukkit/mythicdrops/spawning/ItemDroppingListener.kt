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
import com.tealcube.minecraft.bukkit.mythicdrops.getThenSetItemMetaAsDamageable
import com.tealcube.minecraft.bukkit.mythicdrops.utils.AirUtil.isAir
import com.tealcube.minecraft.bukkit.mythicdrops.utils.BroadcastMessageUtil.broadcastItem
import com.tealcube.minecraft.bukkit.mythicdrops.utils.CustomItemUtil
import io.pixeloutlaw.minecraft.spigot.bandsaw.JulLoggerFactory
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getDurabilityInPercentageRange
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getTier
import org.apache.commons.lang3.RandomUtils
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

class ItemDroppingListener(private val mythicDrops: MythicDrops) : Listener {
    companion object {
        private val logger = JulLoggerFactory.getLogger(ItemDroppingListener::class)
    }

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
        val entityEquipment = event.entity.equipment ?: return
        val itemsToIterateThrough =
            entityEquipment.armorContents.toList() + entityEquipment.itemInMainHand + entityEquipment.itemInOffHand
        itemsToIterateThrough.forEachIndexed { idx, item ->
            val idxOfItemInDrops = event.drops.indexOf(item)

            if (idxOfItemInDrops == -1) {
                return@forEachIndexed
            }

            // check if custom item and announce
            CustomItemUtil.getCustomItemFromItemStack(item)?.let {
                // TODO: set durability
                logger.fine("entity equipment $idx customItem=$it")
                if (it.isBroadcastOnFind && event.entity.killer != null) {
                    broadcastItem(
                        mythicDrops.settingsManager.languageSettings,
                        event.entity.killer,
                        item
                    )
                }
            }
            // check if tier and announce
            item.getTier(mythicDrops.tierManager)?.let {
                event.drops[idxOfItemInDrops] = item.clone().apply {
                    val durabilityValue = type.getDurabilityInPercentageRange(
                        it.minimumDurabilityPercentage,
                        it.maximumDurabilityPercentage
                    )
                    getThenSetItemMetaAsDamageable { damage = durabilityValue }
                }
                if (it.isBroadcastOnFind && event.entity.killer != null) {
                    broadcastItem(
                        mythicDrops.settingsManager.languageSettings,
                        event.entity.killer,
                        item
                    )
                }
            }
        }
    }

    private fun handleEntityDeathEventWithoutGive(event: EntityDeathEvent) {
        val dropStrategy =
            mythicDrops.dropStrategyManager.getById(mythicDrops.settingsManager.configSettings.drops.strategy)
                ?: return

        val drops = dropStrategy.getDropsForEntityDeathEvent(event)

        drops.forEach {
            val itemStack = it.first
            val dropChance = it.second

            val tier = itemStack.getTier(mythicDrops.tierManager)
            val customItem = CustomItemUtil.getCustomItemFromItemStack(itemStack)

            val broadcast = tier?.isBroadcastOnFind ?: customItem?.isBroadcastOnFind ?: false

            if (itemStack.amount > 0 && !isAir(itemStack.type) && RandomUtils.nextDouble(0.0, 1.0) <= dropChance) {
                event.drops.add(itemStack)
                if (broadcast) {
                    broadcastItem(mythicDrops.settingsManager.languageSettings, event.entity.killer, itemStack)
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
