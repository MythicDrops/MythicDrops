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
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketExtenderTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.utils.GemUtil
import io.pixeloutlaw.minecraft.spigot.mythicdrops.displayName
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getTier
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.ItemStack

internal class AnvilListener(
    private val settingsManager: SettingsManager,
    private val tierManager: TierManager,
    private val socketExtenderTypeManager: SocketExtenderTypeManager
) : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onPrepareAnvilEvent(event: PrepareAnvilEvent) {
        if (settingsManager.socketingSettings.options.isPreventCraftingWithGems) {
            handleEarlySocketGemCheck(event)
            handleEarlySocketExtenderCheck(event)
        }
        if (!settingsManager.configSettings.options.isAllowItemsToBeRepairedByAnvil) {
            handleEarlyTierCheck(event)
        }
        handleUnidentifiedItemCheck(event)
        handleIdentityTomeCheck(event)
    }

    private fun handleEarlySocketExtenderCheck(event: PrepareAnvilEvent) {
        val anyAreSocketExtenders =
            socketExtenderTypeManager.get().any {
                event.inventory.anyDisplayName(it.socketExtenderStyleChatColorized)
            }
        if (anyAreSocketExtenders) {
            event.result = ItemStack(Material.AIR)
        }
    }

    private fun handleEarlySocketGemCheck(event: PrepareAnvilEvent) {
        val anySocketGems =
            event.inventory.contents.filterNotNull().any {
                GemUtil.getSocketGemFromPotentialSocketItem(it) != null
            }
        if (anySocketGems) {
            event.result = ItemStack(Material.AIR)
        }
    }

    private fun handleEarlyTierCheck(event: PrepareAnvilEvent) {
        val disableLegacyItemCheck = settingsManager.configSettings.options.isDisableLegacyItemChecks
        val anyTieredItems =
            event.inventory.contents.filterNotNull().any { it.getTier(tierManager, disableLegacyItemCheck) != null }
        if (anyTieredItems) {
            event.result = ItemStack(Material.AIR)
        }
    }

    private fun handleUnidentifiedItemCheck(event: PrepareAnvilEvent) {
        val anyUnidentifiedItems =
            event.inventory.anyDisplayName(
                settingsManager.identifyingSettings.items.unidentifiedItem.name.chatColorize()
            )
        if (anyUnidentifiedItems) {
            event.result = ItemStack(Material.AIR)
        }
    }

    private fun handleIdentityTomeCheck(event: PrepareAnvilEvent) {
        val anyIdentityTomes = event.inventory.anyDisplayName(settingsManager.identifyingSettings.items.identityTome.name.chatColorize())
        if (anyIdentityTomes) {
            event.result = ItemStack(Material.AIR)
        }
    }

    /**
     * Returns true if any of the items in the inventory have a display name that matches the given [name].
     * <p>
     * This fixes issue GH-704, where the display names of non-renamed items can be a blank string, instead
     * of null. When paired with the default config, especially for the socketExtender, which has a blank
     * display name, this causes the anvil to be unusable for vanilla items. We can resolve this by checking
     * for a blank string, and ignoring it.
     *
     * @param name the name to check for
     * @return true if any of the items in the inventory have a display name that matches the given [name]
     */
    private fun AnvilInventory.anyDisplayName(name: String) =
        contents.filterNotNull().any { item ->
            item.displayName?.takeUnless { s -> s.isEmpty() } == name
        }
}
