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

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.getFromItemMetaAsRepairable
import com.tealcube.minecraft.bukkit.mythicdrops.getThenSetItemMetaAsRepairable
import kotlin.math.max
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.GrindstoneInventory
import org.bukkit.inventory.Inventory

class GrindstoneListener(
    private val settingsManager: SettingsManager
) : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onInventoryClickEvent(e: InventoryClickEvent) {
        val ent = e.whoClicked
        val inv = e.inventory
        if (e.isCancelled || ent !is Player || inv !is GrindstoneInventory) {
            return
        }
        if (settingsManager.configSettings.options.isAllowItemsToHaveRepairCostRemovedByGrindstone) {
            return
        }
        val higherRepairCost = getSlot2RepairCost(inv)
        inv.getItem(2)?.let {
            inv.setItem(2, it.clone().apply {
                getThenSetItemMetaAsRepairable {
                    repairCost = higherRepairCost
                }
            })
        }
    }

    private fun getSlot2RepairCost(inv: Inventory): Int {
        val slot0RepairCost = inv.getItem(0)?.getFromItemMetaAsRepairable {
            repairCost
        } ?: 0
        val slot1RepairCost = inv.getItem(1)?.getFromItemMetaAsRepairable { repairCost } ?: 0
        return max(slot0RepairCost, slot1RepairCost)
    }
}
