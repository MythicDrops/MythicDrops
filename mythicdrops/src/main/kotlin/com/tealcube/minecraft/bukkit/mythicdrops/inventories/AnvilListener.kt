/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2019 Richard Harrah
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
import com.tealcube.minecraft.bukkit.mythicdrops.utils.GemUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TierUtil
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.ItemStack

class AnvilListener(private val settingsManager: SettingsManager) : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onInventoryClickEvent(e: InventoryClickEvent) {
        val ent = e.whoClicked
        val inv = e.inventory
        if (e.isCancelled || !settingsManager.configSettings.components.isRepairingEnabled) {
            return
        }
        if (ent !is Player || inv !is AnvilInventory) {
            return
        }
        val fis = inv.getItem(0)
        val sis = inv.getItem(1)

        if (settingsManager.configSettings.options.isAllowItemsToBeRepairedByAnvil) {
            preventGems(fis, sis, e)
        } else {
            preventTiersAndGems(fis, sis, e)
        }
    }

    private fun preventTiersAndGems(fis: ItemStack?, sis: ItemStack?, e: InventoryClickEvent) {
        val ft = if (fis != null) TierUtil.getTierFromItemStack(fis) else null
        val st = if (sis != null) TierUtil.getTierFromItemStack(sis) else null
        val fsg = if (fis != null) GemUtil.getSocketGemFromPotentialSocketItem(fis) else null
        val stg = if (sis != null) GemUtil.getSocketGemFromPotentialSocketItem(sis) else null
        if ((ft != null || st != null || fsg != null || stg != null) && e.slot == 2) {
            e.isCancelled = true
            e.result = Event.Result.DENY
        }
    }

    private fun preventGems(fis: ItemStack?, sis: ItemStack?, e: InventoryClickEvent) {
        val fsg = if (fis != null) GemUtil.getSocketGemFromPotentialSocketItem(fis) else null
        val stg = if (sis != null) GemUtil.getSocketGemFromPotentialSocketItem(sis) else null
        if ((fsg != null || stg != null) && e.slot == 2) {
            e.isCancelled = true
            e.result = Event.Result.DENY
        }
    }
}
