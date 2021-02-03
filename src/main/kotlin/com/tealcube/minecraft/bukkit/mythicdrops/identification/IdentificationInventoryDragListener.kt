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

import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi
import com.tealcube.minecraft.bukkit.mythicdrops.api.identification.IdentificationEvent
import com.tealcube.minecraft.bukkit.mythicdrops.api.identification.PreIdentificationEvent
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGenerationReason
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.relations.RelationManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.getFromItemMetaAsDamageable
import com.tealcube.minecraft.bukkit.mythicdrops.getTargetItemAndCursorAndPlayer
import com.tealcube.minecraft.bukkit.mythicdrops.getThenSetItemMetaAsDamageable
import com.tealcube.minecraft.bukkit.mythicdrops.sendMythicMessage
import com.tealcube.minecraft.bukkit.mythicdrops.unChatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.updateCurrentItemAndSubtractFromCursor
import com.tealcube.minecraft.bukkit.mythicdrops.utils.BroadcastMessageUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.IdentifyingUtil
import io.pixeloutlaw.minecraft.spigot.mythicdrops.displayName
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getApplicableTiers
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getPersistentDataString
import io.pixeloutlaw.minecraft.spigot.mythicdrops.lore
import io.pixeloutlaw.minecraft.spigot.mythicdrops.mythicDropsTier
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import saschpe.log4k.Log

internal class IdentificationInventoryDragListener(
    private val itemGroupManager: ItemGroupManager,
    private val relationManager: RelationManager,
    private val settingsManager: SettingsManager,
    private val tierManager: TierManager
) : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onInventoryClickEvent(event: InventoryClickEvent) {
        val clickTypeToSocket = settingsManager.identifyingSettings.options.clickTypeToIdentify
        val targetItemAndCursorAndPlayer = event.getTargetItemAndCursorAndPlayer(clickTypeToSocket) ?: return
        val (targetItem, cursor, player) = targetItemAndCursorAndPlayer
        val cursorName = cursor.displayName ?: ""
        val targetItemName = targetItem.displayName ?: ""

        // Check if the cursor is an Identity Tome
        if (cursorName != settingsManager.identifyingSettings.items.identityTome.name.chatColorize()) {
            Log.debug("cursorName != identifyingSettings.identityTomeName.chatColorize()")
            return
        }

        // Check if the target item is an Unidentified Item
        if (targetItemName != settingsManager.identifyingSettings.items.unidentifiedItem.name.chatColorize()) {
            Log.debug(
                "targetItemName != settingsManager.identifyingSettings.items.unidentifiedItem.name.chatColorize()"
            )
            player.sendMessage(settingsManager.languageSettings.identification.notUnidentifiedItem.chatColorize())
            return
        }

        // Get potential tier from last line of lore
        val targetItemLore = targetItem.lore

        val tier: Tier? = attemptToGetTierForIdentify(targetItemLore, targetItem)
        if (tier == null) {
            Log.debug("tier == null")
            player.sendMythicMessage(
                settingsManager.languageSettings.identification.failure,
                "%reason%" to "tier is null"
            )
            return
        }

        val preIdentificationEvent = PreIdentificationEvent(targetItem, tier, player)
        Bukkit.getPluginManager().callEvent(preIdentificationEvent)

        if (preIdentificationEvent.isCancelled) {
            Log.debug("identificationEvent.isCancelled")
            player.sendMythicMessage(
                settingsManager.languageSettings.identification.failure,
                "%reason%" to "identification event is cancelled"
            )
            return
        }

        val newTargetItem = MythicDropsApi.mythicDrops.productionLine.tieredItemFactory.getNewDropBuilder()
            .withItemGenerationReason(ItemGenerationReason.DEFAULT)
            .withMaterial(targetItem.type)
            .withTier(preIdentificationEvent.tier)
            .useDurability(false)
            .build()

        if (newTargetItem == null) {
            Log.debug("newTargetItem == null")
            player.sendMythicMessage(
                settingsManager.languageSettings.identification.failure,
                "%reason%" to "newTargetItem is null"
            )
            return
        }

        newTargetItem.getThenSetItemMetaAsDamageable {
            damage = targetItem.getFromItemMetaAsDamageable { damage } ?: 0
        }

        val identificationEvent = IdentificationEvent(newTargetItem, player)
        Bukkit.getPluginManager().callEvent(identificationEvent)

        if (identificationEvent.isCancelled) {
            Log.debug("identificationEvent.isCancelled")
            player.sendMythicMessage(
                settingsManager.languageSettings.identification.failure,
                "%reason%" to "identification event is cancelled"
            )
            return
        }

        event.updateCurrentItemAndSubtractFromCursor(identificationEvent.result)
        player.sendMythicMessage(settingsManager.languageSettings.identification.success)
        if (tier.isBroadcastOnFind) {
            BroadcastMessageUtil.broadcastItem(
                settingsManager.languageSettings,
                player,
                identificationEvent.result,
                settingsManager.configSettings.drops.broadcastTarget
            )
        }
    }

    private fun attemptToGetTierForIdentify(
        targetItemLore: List<String>,
        targetItem: ItemStack
    ): Tier? {
        val potentialTierFromLastLoreLineString = if (targetItemLore.isNotEmpty()) {
            targetItemLore.last()
        } else {
            ""
        }

        val definedTier = targetItem.getPersistentDataString(mythicDropsTier)
        if (!definedTier.isNullOrEmpty()) {
            return tierManager.getByName(definedTier)
        }

        val potentialTierFromLastLoreLine = tierManager.getByName(potentialTierFromLastLoreLineString.unChatColorize())

        val allowableTiers: List<Tier>? = IdentifyingUtil.getAllowableTiers(
            targetItemLore,
            settingsManager.identifyingSettings.items.unidentifiedItem,
            tierManager
        )

        val droppedBy: EntityType? = IdentifyingUtil.getUnidentifiedItemDroppedBy(
            targetItemLore,
            settingsManager.identifyingSettings.items.unidentifiedItem,
            settingsManager.languageSettings.displayNames
        )

        val tiersFromMaterial = targetItem.type.getApplicableTiers(tierManager)

        Log.debug("allowableTiers=[${allowableTiers?.joinToString { it.name }}]")
        Log.debug("droppedBy=$droppedBy")
        Log.debug("tiersFromMaterial=[${tiersFromMaterial.joinToString { it.name }}]")
        Log.debug("potentialTierFromLastLoreLine=${potentialTierFromLastLoreLine?.name}")
        // Identify the item
        // prio order is allowableTiers > droppedBy > potentialTierFromLastLoreLine > tiersFromMaterial
        // effectively grabs the first non-null value if it exists, null otherwise
        return IdentifyingUtil.determineTierForIdentify(
            targetItem.type,
            settingsManager.creatureSpawningSettings,
            tierManager,
            allowableTiers,
            droppedBy,
            tiersFromMaterial,
            potentialTierFromLastLoreLine
        )
    }
}
