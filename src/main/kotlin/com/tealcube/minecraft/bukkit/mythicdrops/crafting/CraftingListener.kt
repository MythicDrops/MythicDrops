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
package com.tealcube.minecraft.bukkit.mythicdrops.crafting

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.utils.GemUtil
import io.pixeloutlaw.minecraft.spigot.hilt.getDisplayName
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.ItemStack

class CraftingListener(private val settingsManager: SettingsManager) : Listener {
    @EventHandler
    fun onPrepareItemCraftEvent(event: PrepareItemCraftEvent) {
        if (settingsManager.socketingSettings.options.isPreventCraftingWithGems) {
            handleEarlySocketGemCheck(event)
            handleEarlySocketExtenderCheck(event)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onItemCraftEvent(event: CraftItemEvent) {
        if (settingsManager.socketingSettings.options.isPreventCraftingWithGems) {
            handleSocketGemCheck(event)
            handleSocketExtenderCheck(event)
        }
    }

    private fun handleSocketExtenderCheck(event: CraftItemEvent) {
        val anyAreSocketExtenders = event.inventory.matrix.filterNotNull()
            .any { it.getDisplayName() == settingsManager.socketingSettings.items.socketExtender.name.chatColorize() }
        if (anyAreSocketExtenders) {
            event.isCancelled = true
            (event.whoClicked as? Player)?.sendMessage(
                settingsManager.languageSettings.socketing.preventedCrafting.chatColorize()
            )
        }
    }

    private fun handleSocketGemCheck(event: CraftItemEvent) {
        val anySocketGems = event.inventory.matrix.filterNotNull().any {
            GemUtil.getSocketGemFromPotentialSocketItem(it) != null
        }
        if (anySocketGems) {
            event.isCancelled = true
            (event.whoClicked as? Player)?.sendMessage(
                settingsManager.languageSettings.socketing.preventedCrafting.chatColorize()
            )
        }
    }

    private fun handleEarlySocketExtenderCheck(event: PrepareItemCraftEvent) {
        val anyAreSocketExtenders = event.inventory.matrix.filterNotNull()
            .any { it.getDisplayName() == settingsManager.socketingSettings.items.socketExtender.name.chatColorize() }
        if (anyAreSocketExtenders) {
            event.inventory.result = ItemStack(Material.AIR)
        }
    }

    private fun handleEarlySocketGemCheck(event: PrepareItemCraftEvent) {
        val anySocketGems = event.inventory.matrix.filterNotNull().any {
            GemUtil.getSocketGemFromPotentialSocketItem(it) != null
        }
        if (anySocketGems) {
            event.inventory.result = ItemStack(Material.AIR)
        }
    }
}
