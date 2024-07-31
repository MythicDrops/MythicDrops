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
@file:Suppress("detekt.TooManyFunctions")

package io.pixeloutlaw.minecraft.spigot.mythicdrops

import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi.mythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SocketingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.replaceArgs
import com.tealcube.minecraft.bukkit.mythicdrops.stripColors
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ChatColorUtil
import org.bukkit.ChatColor
import org.bukkit.inventory.ItemStack

/**
 * Attempts to get the custom item from this ItemStack that matches the custom items available in the CustomItemManager.
 */
internal fun ItemStack.getCustomItem(
    customItemManager: CustomItemManager,
    disableLegacyItemCheck: Boolean
): CustomItem? = getCustomItem(customItemManager.get(), disableLegacyItemCheck)

/**
 * Attempts to get the custom item from this ItemStack that matches the given collection of custom items.
 */
internal fun ItemStack.getCustomItem(
    customItems: Collection<CustomItem>,
    disableLegacyItemCheck: Boolean
): CustomItem? {
    val fromPersistentData = getCustomItemFromItemStackPersistentData(this, customItems)
    // we only perform the ItemStack similarity check if disableLegacyItemCheck is false
    val canPerformLegacyItemCheck = !disableLegacyItemCheck
    return if (canPerformLegacyItemCheck && fromPersistentData == null) {
        getCustomItemFromItemStackSimilarity(
            this,
            customItems
        )
    } else {
        fromPersistentData
    }
}

/**
 * Attempts to get the tier from this ItemStack that matches the tiers available in the TierManager.
 *
 * @param tierManager Tier Manager
 */
internal fun ItemStack.getTier(
    tierManager: TierManager,
    disableLegacyItemCheck: Boolean
): Tier? = getTier(tierManager.get(), disableLegacyItemCheck)

/**
 * Attempts to get the tier from this ItemStack that matches the given collection of tiers.
 *
 * @param tiers tiers to choose from
 */
internal fun ItemStack.getTier(
    tiers: Collection<Tier>,
    disableLegacyItemCheck: Boolean
): Tier? {
    val fromPersistentData = getTierFromItemStackPersistentData(this, tiers)
    // we only perform the ItemStack similarity check if disableLegacyItemCheck is false
    val canPerformLegacyItemCheck = !disableLegacyItemCheck
    return if (canPerformLegacyItemCheck && fromPersistentData == null) {
        getTierFromItemStackDisplayName(this, tiers)
    } else {
        fromPersistentData
    }
}

internal fun ItemStack.getSocketGem(
    socketGemManager: SocketGemManager,
    socketTypeManager: SocketTypeManager,
    socketingSettings: SocketingSettings,
    disableLegacyItemCheck: Boolean
): SocketGem? = getSocketGem(socketGemManager.get(), socketingSettings, socketTypeManager, disableLegacyItemCheck)

internal fun ItemStack.getSocketGem(
    gems: Collection<SocketGem>,
    socketingSettings: SocketingSettings,
    socketTypeManager: SocketTypeManager,
    disableLegacyItemCheck: Boolean
): SocketGem? {
    val fromPersistentData = getSocketGemFromItemStackPersistentData(this, gems)
    // we only perform the ItemStack similarity check if disableLegacyItemCheck is false
    val canPerformLegacyItemCheck = !disableLegacyItemCheck
    return if (canPerformLegacyItemCheck && fromPersistentData == null) {
        getSocketGemFromItemStackDisplayName(this, socketingSettings, socketTypeManager, gems)
    } else {
        fromPersistentData
    }
}

private fun getCustomItemFromItemStackPersistentData(
    itemStack: ItemStack,
    customItems: Collection<CustomItem>
): CustomItem? =
    itemStack
        .getPersistentDataString(mythicDropsCustomItem)
        ?.let { customItemName -> customItems.find { it.name == customItemName } }

private fun getTierFromItemStackPersistentData(
    itemStack: ItemStack,
    tiers: Collection<Tier>
): Tier? = itemStack.getPersistentDataString(mythicDropsTier)?.let { tierName -> tiers.find { it.name == tierName } }

private fun getSocketGemFromItemStackPersistentData(
    itemStack: ItemStack,
    gems: Collection<SocketGem>
): SocketGem? =
    itemStack
        .getPersistentDataString(mythicDropsSocketGem)
        ?.let { socketGemName -> gems.find { it.name == socketGemName } }

private fun getCustomItemFromItemStackSimilarity(
    itemStack: ItemStack,
    customItems: Collection<CustomItem>
): CustomItem? =
    customItems.find {
        mythicDrops.productionLine.customItemFactory
            .toItemStack(it)
            .isSimilar(itemStack)
    }

private fun getTierFromItemStackDisplayName(
    itemStack: ItemStack,
    tiers: Collection<Tier>
): Tier? =
    itemStack.displayName?.let { displayName ->
        val firstChatColor = ChatColorUtil.getFirstColor(displayName)?.toString()
        val colors = ChatColor.getLastColors(displayName)
        val lastChatColor =
            if (colors.contains(ChatColor.COLOR_CHAR)) {
                ChatColor.getByChar(colors.substring(1, 2))
            } else {
                null
            }?.toString()
        if (firstChatColor == null || lastChatColor == null || firstChatColor == lastChatColor) {
            null
        } else {
            tiers.find {
                it.displayColor.toString() == firstChatColor && it.identifierColor.toString() == lastChatColor
            }
        }
    }

private fun getSocketGemFromItemStackDisplayName(
    itemStack: ItemStack,
    socketingSettings: SocketingSettings,
    socketTypeManager: SocketTypeManager,
    gems: Collection<SocketGem>
): SocketGem? {
    if (!socketingSettings.options.socketGemMaterialIds.contains(itemStack.type)) {
        return null
    }
    val displayName = itemStack.displayName
    if (displayName.isNullOrBlank()) {
        return null
    }

    val socketTypes = socketTypeManager.get()

    return socketTypes
        .map { it.socketGemStyleStripped.replaceArgs("%socketgem%" to "") }
        .map { displayName.stripColors().replace(it, "") }
        .map { typeFromDisplayName ->
            gems.find {
                it.name.equals(typeFromDisplayName, ignoreCase = true) ||
                    it.name.equals(typeFromDisplayName.replace("_", ""), ignoreCase = true)
            }
        }.firstOrNull { it != null }
}
