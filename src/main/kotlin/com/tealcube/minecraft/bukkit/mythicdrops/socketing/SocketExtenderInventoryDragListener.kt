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
package com.tealcube.minecraft.bukkit.mythicdrops.socketing

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketExtenderTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.getTargetItemAndCursorAndPlayer
import com.tealcube.minecraft.bukkit.mythicdrops.stripColors
import com.tealcube.minecraft.bukkit.mythicdrops.updateCurrentItemAndSubtractFromCursor
import com.tealcube.minecraft.bukkit.mythicdrops.utils.GemUtil
import io.pixeloutlaw.kindling.Log
import io.pixeloutlaw.minecraft.spigot.mythicdrops.displayName
import io.pixeloutlaw.minecraft.spigot.mythicdrops.lore
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

internal class SocketExtenderInventoryDragListener(
    private val settingsManager: SettingsManager,
    private val socketExtenderTypeManager: SocketExtenderTypeManager
) : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    @Suppress("detekt.ReturnCount")
    fun onInventoryClickEvent(event: InventoryClickEvent) {
        val isDisableDefaultTieredItemAttributes =
            settingsManager.configSettings.options.isDisableDefaultTieredItemAttributes
        val clickTypeToSocket = settingsManager.socketingSettings.options.clickTypeToSocket
        val targetItemAndCursorAndPlayer =
            event.getTargetItemAndCursorAndPlayer(clickTypeToSocket, !isDisableDefaultTieredItemAttributes) ?: return
        val (targetItem, cursor, player) = targetItemAndCursorAndPlayer
        val cursorName = cursor.displayName ?: ""

        if (!settingsManager.socketingSettings.options.socketExtenderMaterialIds.contains(cursor.type)) {
            Log.debug("!sockettingSettings.socketExtenderMaterialIds.contains(cursor.type)")
            return
        }

        val isCursorSocketExtender = socketExtenderTypeManager.get().any {
            it.socketExtenderStyleChatColorized.equals(cursorName, ignoreCase = true)
        }
        // Check if the cursor is a Socket Extender
        if (!isCursorSocketExtender) {
            Log.debug("!isCursorSocketExtender")
            return
        }

        // Check if the targetItem has an open socket extender slot
        val targetItemLore = targetItem.lore
        val strippedTargetItemLore = targetItemLore.stripColors()
        val indexOfFirstSocketExtenderSlot = indexOfFirstOpenSocketExtenderSlot(strippedTargetItemLore)
        val requireExtenderSlots = settingsManager.socketingSettings.options.isRequireExtenderSlotsToAddSockets
        if (
            requireExtenderSlots &&
            indexOfFirstSocketExtenderSlot < 0
        ) {
            Log.debug(
                "requireExtenderSlots && indexOfFirstSocketExtenderSlot < 0"
            )
            player.sendMessage(settingsManager.languageSettings.socketing.noSocketExtenderSlots.chatColorize())
            return
        }

        // Check if the targetItem has more than the maximum number of allowed socket extenders
        if (targetItemHasMaximumSocketExtenderSlots(targetItem, player)) return

        val emptySocketString =
            socketExtenderTypeManager.randomByWeight()?.appliedSocketType?.socketStyleChatColorized ?: ""

        targetItem.lore = getLoreWithAddedSocket(indexOfFirstSocketExtenderSlot, targetItemLore, emptySocketString)

        event.updateCurrentItemAndSubtractFromCursor(targetItem)
        player.sendMessage(settingsManager.languageSettings.socketing.addedSocket.chatColorize())
    }

    private fun targetItemHasMaximumSocketExtenderSlots(
        targetItem: ItemStack,
        player: Player
    ): Boolean {
        val numberOfSocketGemsOnItem = numberOfSocketGemsOnItem(targetItem)
        val numberOfSocketExtendersOnItem = numberOfSocketGemExtendersOnItem(targetItem)
        val totalNumberOfSocketsAdded = numberOfSocketGemsOnItem + numberOfSocketExtendersOnItem
        val maximumAllowedSocketGemExtenders =
            settingsManager.socketingSettings.options.maximumNumberOfSocketsViaExtender
        if (maximumAllowedSocketGemExtenders in 1..totalNumberOfSocketsAdded) {
            Log.debug("maximumAllowedSocketGemExtenders in 1..totalNumberOfSocketsAdded")
            player.sendMessage(settingsManager.languageSettings.socketing.maximumSocketExtenderSlots.chatColorize())
            return true
        }
        return false
    }

    private fun getLoreWithAddedSocket(
        indexOfFirstSocketExtenderSlot: Int,
        targetItemLore: List<String>,
        emptySocketString: String
    ): List<String> {
        val newTargetLore = if (indexOfFirstSocketExtenderSlot < 0) {
            targetItemLore + emptySocketString
        } else {
            targetItemLore.toMutableList().apply {
                set(
                    indexOfFirstSocketExtenderSlot,
                    emptySocketString
                )
            }.toList()
        }
        return newTargetLore
    }

    private fun indexOfFirstOpenSocketExtenderSlot(lore: List<String>): Int {
        val notIgnoringColorsIndex = socketExtenderTypeManager.getNotIgnoreColors()
            .map { socketExtenderType ->
                lore.indexOfFirst {
                    it.equals(socketExtenderType.slotStyleChatColorized, true)
                }
            }
            .firstOrNull { it >= 0 }
        if (notIgnoringColorsIndex != null) {
            return notIgnoringColorsIndex
        }
        return socketExtenderTypeManager.getIgnoreColors()
            .map { socketExtenderType ->
                lore.indexOfFirst {
                    it.equals(socketExtenderType.slotStyleChatColorized, true)
                }
            }
            .firstOrNull { it >= 0 } ?: -1
    }

    private fun numberOfSocketGemsOnItem(itemStack: ItemStack): Int =
        GemUtil.getSocketGemsFromItemStackLore(itemStack).size

    private fun numberOfSocketGemExtendersOnItem(itemStack: ItemStack): Int {
        val socketExtenderSlot =
            ChatColor.stripColor(settingsManager.socketingSettings.items.socketExtender.slot.chatColorize())
        return itemStack.lore.filter { ChatColor.stripColor(it) == socketExtenderSlot }.size
    }
}
