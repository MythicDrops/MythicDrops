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
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.getTargetItemAndCursorAndPlayer
import com.tealcube.minecraft.bukkit.mythicdrops.loading.FeatureFlagged
import com.tealcube.minecraft.bukkit.mythicdrops.updateCurrentItemAndSubtractFromCursor
import dev.mythicdrops.NamespacedKeys
import io.pixeloutlaw.kindling.Log
import io.pixeloutlaw.minecraft.spigot.mythicdrops.displayName
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getPersistentDataInt
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getTier
import io.pixeloutlaw.minecraft.spigot.mythicdrops.lore
import io.pixeloutlaw.minecraft.spigot.mythicdrops.setPersistentDataInt
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.koin.core.annotation.Single

@Single
internal class SocketExtenderInventoryDragListener(
    private val settingsManager: SettingsManager,
    private val socketExtenderTypeManager: SocketExtenderTypeManager,
    private val tierManager: TierManager,
    private val namespacedKeys: NamespacedKeys
) : FeatureFlagged,
    Listener {
    override fun isEnabled(): Boolean = settingsManager.configSettings.components.isSocketingEnabled

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

        if (!settingsManager.socketingSettings.options.socketExtenderMaterialIds
                .contains(cursor.type)
        ) {
            Log.debug("!socketingSettings.socketExtenderMaterialIds.contains(cursor.type)")
            return
        }

        val isCursorSocketExtender =
            socketExtenderTypeManager.get().any {
                it.socketExtenderStyleChatColorized.equals(cursorName, ignoreCase = true)
            }
        // Check if the cursor is a Socket Extender
        if (!isCursorSocketExtender) {
            Log.debug("!isCursorSocketExtender")
            return
        }

        // Check if the targetItem has an open socket extender slot
        val targetItemLore = targetItem.lore
        val indexOfFirstSocketExtenderSlot = indexOfFirstOpenSocketExtenderSlot(targetItemLore)
        val requireExtenderSlots = settingsManager.socketingSettings.options.isRequireExtenderSlotsToAddSockets
        if (
            requireExtenderSlots &&
            indexOfFirstSocketExtenderSlot < 0
        ) {
            Log.debug(
                "requireExtenderSlots && indexOfFirstSocketExtenderSlot < 0"
            )
            player.sendMessage(
                settingsManager.languageSettings.socketing.noSocketExtenderSlots
                    .chatColorize()
            )
            return
        }

        val socketType = socketExtenderTypeManager.randomByWeight()
        if (socketType == null) {
            Log.debug("socketType == null")
            return
        }

        // Check if the targetItem has more than the maximum number of allowed socket extenders
        if (targetItemHasMaximumSocketExtenderSlots(targetItem, player)) {
            return
        }

        val tier = targetItem.getTier(tierManager, settingsManager.configSettings.options.isDisableLegacyItemChecks)
        val tierColor =
            if (tier != null) {
                "${tier.displayColor}"
            } else {
                ""
            }

        val emptySocketString = socketType.appliedSocketType.socketStyleChatColorized.replace("%tiercolor%", tierColor)
        val socketExtendersUsed = targetItem.getPersistentDataInt(namespacedKeys.socketExtendersUsed) ?: 0

        targetItem.lore = getLoreWithAddedSocket(indexOfFirstSocketExtenderSlot, targetItemLore, emptySocketString)
        targetItem.setPersistentDataInt(namespacedKeys.socketExtendersUsed, socketExtendersUsed + 1)

        event.updateCurrentItemAndSubtractFromCursor(targetItem)
        player.sendMessage(
            settingsManager.languageSettings.socketing.addedSocket
                .chatColorize()
        )
    }

    private fun targetItemHasMaximumSocketExtenderSlots(
        targetItem: ItemStack,
        player: Player
    ): Boolean {
        val maximumAllowedSocketGemExtenders =
            settingsManager.socketingSettings.options.maximumNumberOfSocketsViaExtender
        if (maximumAllowedSocketGemExtenders < 0) {
            return false
        }

        val socketExtendersUsed = targetItem.getPersistentDataInt(namespacedKeys.socketExtendersUsed) ?: 0
        if (socketExtendersUsed >= maximumAllowedSocketGemExtenders) {
            Log.debug("socketExtendersUsed >= maximumAllowedSocketGemExtenders")
            player.sendMessage(
                settingsManager.languageSettings.socketing.maximumSocketExtenderSlots
                    .chatColorize()
            )
            return true
        }
        return false
    }

    private fun getLoreWithAddedSocket(
        indexOfFirstSocketExtenderSlot: Int,
        targetItemLore: List<String>,
        emptySocketString: String
    ): List<String> {
        val newTargetLore =
            if (indexOfFirstSocketExtenderSlot < 0) {
                targetItemLore + emptySocketString
            } else {
                targetItemLore
                    .toMutableList()
                    .apply {
                        set(
                            indexOfFirstSocketExtenderSlot,
                            emptySocketString
                        )
                    }.toList()
            }
        return newTargetLore
    }

    private fun indexOfFirstOpenSocketExtenderSlot(lore: List<String>): Int {
        val notIgnoringColorsIndex =
            socketExtenderTypeManager
                .getNotIgnoreColors()
                .map { socketExtenderType ->
                    lore.indexOfFirst {
                        it.equals(socketExtenderType.slotStyleChatColorized, true)
                    }
                }.firstOrNull { it >= 0 }
        if (notIgnoringColorsIndex != null) {
            return notIgnoringColorsIndex
        }
        return socketExtenderTypeManager
            .getIgnoreColors()
            .map { socketExtenderType ->
                lore.indexOfFirst {
                    it.equals(socketExtenderType.slotStyleChatColorized, true)
                }
            }.firstOrNull { it >= 0 } ?: -1
    }
}
