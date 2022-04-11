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
package com.tealcube.minecraft.bukkit.mythicdrops.inventories

import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.CustomEnchantmentRegistry
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.getFromItemMetaAsRepairable
import com.tealcube.minecraft.bukkit.mythicdrops.getThenSetItemMetaAsRepairable
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getCustomItem
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.GrindstoneInventory
import org.bukkit.inventory.Inventory
import kotlin.math.max

class GrindstoneListener(
    private val customEnchantmentRegistry: CustomEnchantmentRegistry,
    private val customItemManager: CustomItemManager,
    private val settingsManager: SettingsManager
) : Listener {
    companion object {
        private const val FIRST_ITEM_SLOT = 0
        private const val SECOND_ITEM_SLOT = 1
        private const val RESULT_SLOT = 2
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onInventoryClickEvent(event: InventoryClickEvent) {
        val whoClicked = event.whoClicked
        val inventory = event.inventory
        if (event.isCancelled || whoClicked !is Player || inventory !is GrindstoneInventory) {
            return
        }
        handleRepairCost(inventory)
        handleCustomItemsEnchantments(event, inventory)
    }

    private fun handleCustomItemsEnchantments(event: InventoryClickEvent, inventory: GrindstoneInventory) {
        val slot1CustomItem = inventory.getItem(FIRST_ITEM_SLOT)?.getCustomItem(
            customItemManager
        )
        val slot2CustomItem = inventory.getItem(SECOND_ITEM_SLOT)?.getCustomItem(
            customItemManager
        )
        val isSlot1Removable = slot1CustomItem?.isEnchantmentsRemovableByGrindstone ?: true
        val isSlot2Removable = slot2CustomItem?.isEnchantmentsRemovableByGrindstone ?: true
        if (isSlot1Removable && isSlot2Removable) {
            return
        }
        event.isCancelled = true
        event.result = Event.Result.DENY
    }

    private fun handleRepairCost(inventory: GrindstoneInventory) {
        if (settingsManager.configSettings.options.isAllowItemsToHaveRepairCostRemovedByGrindstone) {
            return
        }
        val higherRepairCost = getSlot2RepairCost(inventory)
        inventory.getItem(RESULT_SLOT)?.let {
            inventory.setItem(
                RESULT_SLOT,
                it.clone().apply {
                    getThenSetItemMetaAsRepairable {
                        repairCost = higherRepairCost
                    }
                }
            )
        }
    }

    private fun getSlot2RepairCost(inv: Inventory): Int {
        val slot0RepairCost = inv.getItem(FIRST_ITEM_SLOT)?.getFromItemMetaAsRepairable {
            repairCost
        } ?: 0
        val slot1RepairCost = inv.getItem(SECOND_ITEM_SLOT)?.getFromItemMetaAsRepairable { repairCost } ?: 0
        return max(slot0RepairCost, slot1RepairCost).coerceAtLeast(0)
    }
}
