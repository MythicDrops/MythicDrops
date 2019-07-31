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
package com.tealcube.minecraft.bukkit.mythicdrops.identification

import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGenerationReason
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.ConfigSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.IdentifyingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.RelationSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SockettingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.getTargetItemAndCursorAndPlayer
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicDropBuilder
import com.tealcube.minecraft.bukkit.mythicdrops.items.getFromItemMetaAsDamageable
import com.tealcube.minecraft.bukkit.mythicdrops.items.getThenSetItemMetaAsDamageable
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory
import com.tealcube.minecraft.bukkit.mythicdrops.socketting.SocketInventoryDragListener
import com.tealcube.minecraft.bukkit.mythicdrops.updateCurrentItemAndSubtractFromCursor
import com.tealcube.minecraft.bukkit.mythicdrops.utils.BroadcastMessageUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TierUtil
import io.pixeloutlaw.minecraft.spigot.hilt.getDisplayName
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class IdentificationInventoryDragListener(
    private val configSettings: ConfigSettings,
    private val identifyingSettings: IdentifyingSettings,
    private val relationSettings: RelationSettings,
    private val sockettingSettings: SockettingSettings
) : Listener {
    private val logger = JulLoggerFactory.getLogger(SocketInventoryDragListener::class.java)

    @EventHandler(priority = EventPriority.LOWEST)
    fun onInventoryClickEvent(event: InventoryClickEvent) {
        val targetItemAndCursorAndPlayer = event.getTargetItemAndCursorAndPlayer(logger) ?: return
        val (targetItem, cursor, player) = targetItemAndCursorAndPlayer
        val cursorName = cursor.getDisplayName() ?: ""
        val targetItemName = targetItem.getDisplayName() ?: ""

        // Check if the cursor is an Identity Tome
        if (cursorName != identifyingSettings.identityTomeName.chatColorize()) {
            logger.fine("cursorName != identifyingSettings.identityTomeName.chatColorize()")
            return
        }

        // Check if the target item is an Unidentified Item
        if (targetItemName != "${UnidentifiedItem.displayNamePrefix}${identifyingSettings.unidentifiedItemName.chatColorize()}${UnidentifiedItem.displayNameSuffix}") {
            logger.fine("targetItemName != \"${UnidentifiedItem.displayNamePrefix}${identifyingSettings.unidentifiedItemName.chatColorize()}${UnidentifiedItem.displayNameSuffix}\"")
            player.sendMessage(configSettings.getFormattedLanguageString("identification.not-unidentified-item"))
            return
        }

        // Identify the item
        // TODO: support getting a tier from the last line of lore on the unid item
        val tier = TierUtil.randomTierWithIdentifyChance(ItemUtil.getTiersFromMaterial(targetItem.type))
        if (tier == null) {
            logger.fine("tier == null")
            player.sendMessage(configSettings.getFormattedLanguageString("identification.failure"))
            return
        }

        val newTargetItem = MythicDropBuilder(configSettings, sockettingSettings, relationSettings)
            .withItemGenerationReason(ItemGenerationReason.DEFAULT)
            .withMaterial(targetItem.type)
            .withTier(tier)
            .useDurability(false)
            .build()
        newTargetItem.getThenSetItemMetaAsDamageable { damage = targetItem.getFromItemMetaAsDamageable { damage } ?: 0 }

        val identificationEvent = IdentificationEvent(newTargetItem, player)
        Bukkit.getPluginManager().callEvent(identificationEvent)

        if (identificationEvent.isCancelled) {
            logger.fine("identificationEvent.isCancelled")
            player.sendMessage(configSettings.getFormattedLanguageString("identification.failure"))
            return
        }

        event.updateCurrentItemAndSubtractFromCursor(identificationEvent.result)
        player.sendMessage(configSettings.getFormattedLanguageString("identification.success"))
        if (tier.isBroadcastOnFind) {
            BroadcastMessageUtil.broadcastItem(configSettings, player, identificationEvent.result)
        }
    }
}
