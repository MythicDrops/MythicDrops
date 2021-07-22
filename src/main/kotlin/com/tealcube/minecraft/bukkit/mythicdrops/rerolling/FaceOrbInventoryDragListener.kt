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
package com.tealcube.minecraft.bukkit.mythicdrops.rerolling

import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGenerationReason
import com.tealcube.minecraft.bukkit.mythicdrops.api.reroll.PreRerollEvent
import com.tealcube.minecraft.bukkit.mythicdrops.api.reroll.RerollEvent
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.getFromItemMetaAsDamageable
import com.tealcube.minecraft.bukkit.mythicdrops.getTargetItemAndCursorAndPlayer
import com.tealcube.minecraft.bukkit.mythicdrops.getThenSetItemMetaAsDamageable
import com.tealcube.minecraft.bukkit.mythicdrops.sendMythicMessage
import com.tealcube.minecraft.bukkit.mythicdrops.updateCurrentItemAndSubtractFromCursor
import io.pixeloutlaw.kindling.Log
import io.pixeloutlaw.minecraft.spigot.mythicdrops.displayName
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getTier
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

internal class FaceOrbInventoryDragListener(
    private val settingsManager: SettingsManager,
    private val tierManager: TierManager
) : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onInventoryClickEvent(event: InventoryClickEvent) {
        val disableLegacyItemCheck = settingsManager.configSettings.options.isDisableLegacyItemChecks
        val clickTypeToSocket = settingsManager.identifyingSettings.options.clickTypeToIdentify
        val targetItemAndCursorAndPlayer = event.getTargetItemAndCursorAndPlayer(clickTypeToSocket) ?: return
        val (targetItem, cursor, player) = targetItemAndCursorAndPlayer
        val cursorName = cursor.displayName ?: ""

        // Check if the cursor is an Face Orb
        if (cursorName != settingsManager.socketingSettings.items.faceOrb.name.chatColorize()) {
            Log.debug("cursorName != socketingSettings.items.faceOrb.name.chatColorize()")
            return
        }

        // Check if the target item is an Tier Item
        val tier = targetItem.getTier(tierManager, disableLegacyItemCheck)
        if (tier == null) {
            Log.debug("tier == null")
            player.sendMythicMessage(
                settingsManager.languageSettings.reroll.failure,
                "%reason%" to "tier is null"
            )
            return
        }

        val preRerollEvent = PreRerollEvent(targetItem, tier, player)
        Bukkit.getPluginManager().callEvent(preRerollEvent)

        if (preRerollEvent.isCancelled) {
            Log.debug("rerollEvent.isCancelled")
            player.sendMythicMessage(
                settingsManager.languageSettings.reroll.failure,
                "%reason%" to "reroll event is cancelled"
            )
            return
        }

        val newTargetItem = MythicDropsApi.mythicDrops.productionLine.tieredItemFactory.getNewDropBuilder()
            .withItemGenerationReason(ItemGenerationReason.DEFAULT)
            .withMaterial(targetItem.type)
            .withTier(preRerollEvent.tier)
            .useDurability(true)
            .build()

        if (newTargetItem == null) {
            Log.debug("newTargetItem == null")
            player.sendMythicMessage(
                settingsManager.languageSettings.reroll.failure,
                "%reason%" to "newTargetItem is null"
            )
            return
        }

        newTargetItem.getThenSetItemMetaAsDamageable {
            damage = targetItem.getFromItemMetaAsDamageable { damage } ?: 0
        }

        val rerollEvent = RerollEvent(newTargetItem, player)
        Bukkit.getPluginManager().callEvent(rerollEvent)

        if (rerollEvent.isCancelled) {
            Log.debug("rerollEvent.isCancelled")
            player.sendMythicMessage(
                settingsManager.languageSettings.reroll.failure,
                "%reason%" to "reroll event is cancelled"
            )
            return
        }

        event.updateCurrentItemAndSubtractFromCursor(rerollEvent.result)
        player.sendMythicMessage(settingsManager.languageSettings.reroll.success)
    }
}
