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

import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.endsWithAny
import com.tealcube.minecraft.bukkit.mythicdrops.firstChatColors
import com.tealcube.minecraft.bukkit.mythicdrops.getTargetItemAndCursorAndPlayer
import com.tealcube.minecraft.bukkit.mythicdrops.setUnsafeEnchantments
import com.tealcube.minecraft.bukkit.mythicdrops.startsWithAny
import com.tealcube.minecraft.bukkit.mythicdrops.stripChatColors
import com.tealcube.minecraft.bukkit.mythicdrops.stripColors
import com.tealcube.minecraft.bukkit.mythicdrops.updateCurrentItemAndSubtractFromCursor
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ChatColorUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.GemUtil
import io.pixeloutlaw.kindling.Log
import io.pixeloutlaw.minecraft.spigot.mythicdrops.addAttributeModifier
import io.pixeloutlaw.minecraft.spigot.mythicdrops.displayName
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getMinecraftName
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getTier
import io.pixeloutlaw.minecraft.spigot.mythicdrops.lore
import org.bukkit.ChatColor
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

internal class SocketInventoryDragListener(
    private val itemGroupManager: ItemGroupManager,
    private val settingsManager: SettingsManager,
    private val socketGemManager: SocketGemManager,
    private val tierManager: TierManager
) : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onInventoryClickEvent(event: InventoryClickEvent) {
        val clickTypeToSocket = settingsManager.socketingSettings.options.clickTypeToSocket
        val targetItemAndCursorAndPlayer = event.getTargetItemAndCursorAndPlayer(clickTypeToSocket) ?: return
        val (targetItem, cursor, player) = targetItemAndCursorAndPlayer
        val targetItemName =
            targetItem.displayName ?: targetItem.type.getMinecraftName()

        if (!settingsManager.socketingSettings.options.socketGemMaterialIds.contains(cursor.type)) {
            Log.debug("!sockettingSettings.socketGemMaterials.contains(cursor.type)")
            return
        }

        // Check if the item is a socket gem
        val socketGem = GemUtil.getSocketGemFromPotentialSocketItem(cursor)
        if (socketGem == null) {
            Log.debug("socketGem == null")
            return
        }

        val matchingItemGroups = itemGroupManager.getMatchingItemGroups(targetItem.type)

        Log.debug(
            "matchingItemGroups=${matchingItemGroups.joinToString { it.name }}"
        )
        Log.debug("socketGem.anyOfItemGroups=${socketGem.anyOfItemGroups.joinToString { it.name }}")
        Log.debug("socketGem.allOfItemGroups=${socketGem.allOfItemGroups.joinToString { it.name }}")
        Log.debug("socketGem.noneOfItemGroups=${socketGem.noneOfItemGroups.joinToString { it.name }}")

        val allOfMatch =
            socketGem.allOfItemGroups.isEmpty() || matchingItemGroups.containsAll(socketGem.allOfItemGroups)
        val anyOfMatch =
            socketGem.anyOfItemGroups.isEmpty() || matchingItemGroups.any { socketGem.anyOfItemGroups.contains(it) }
        val noneOfMatch = matchingItemGroups.any { socketGem.noneOfItemGroups.contains(it) }

        if (!allOfMatch) {
            Log.debug("!allOfMatch")
            player.sendMessage(settingsManager.languageSettings.socketing.notInItemGroup.chatColorize())
            return
        }
        if (!anyOfMatch) {
            Log.debug("!anyOfMatch")
            player.sendMessage(settingsManager.languageSettings.socketing.notInItemGroup.chatColorize())
            return
        }
        if (noneOfMatch) {
            Log.debug("noneOfMatch")
            player.sendMessage(settingsManager.languageSettings.socketing.notInItemGroup.chatColorize())
            return
        }

        // Check if the targetItem has an open socket
        val targetItemLore = targetItem.lore
        val strippedTargetItemLore = targetItemLore.stripChatColors()
        val indexOfFirstSocket = GemUtil.indexOfFirstOpenSocket(strippedTargetItemLore)
        if (indexOfFirstSocket < 0) {
            Log.debug("indexOfFirstSocket < 0")
            player.sendMessage(settingsManager.languageSettings.socketing.noOpenSockets.chatColorize())
            return
        }

        // Attempt to find tier for the target item (used for coloring the socket gem name in the lore)
        val tier = targetItem.getTier(tierManager)

        // Add the socket gem lore to the item's lore
        val manipulatedTargetItemLore = applySocketGemLore(targetItemLore, indexOfFirstSocket, socketGem, tier)

        // Add the socket gem prefix and suffix to the item's display name
        val manipulatedTargetItemDisplayName = applySocketGemDisplayName(targetItemName, socketGem)

        val targetItemEnchantments = targetItem.enchantments
        val manipulatedTargetItemEnchantments = applySocketGemEnchantments(targetItemEnchantments, socketGem)

        targetItem.displayName = manipulatedTargetItemDisplayName
        targetItem.lore = manipulatedTargetItemLore
        targetItem.setUnsafeEnchantments(manipulatedTargetItemEnchantments)
        socketGem.attributes.forEach {
            val (attribute, attributeModifier) = it.toAttributeModifier()
            targetItem.addAttributeModifier(attribute, attributeModifier)
        }

        event.updateCurrentItemAndSubtractFromCursor(targetItem)
        player.sendMessage(settingsManager.languageSettings.socketing.success.chatColorize())
    }

    internal fun applySocketGemLore(
        previousLore: List<String>,
        indexOfFirstSocket: Int,
        socketGem: SocketGem,
        tier: Tier? = null
    ): List<String> {
        if (indexOfFirstSocket < 0) {
            return previousLore
        }
        val chatColorForSocketGemName =
            if (tier != null && settingsManager.socketingSettings.options.useTierColorForSocketName) {
                tier.itemDisplayNameFormat.chatColorize().firstChatColors()
            } else {
                settingsManager.socketingSettings.options.defaultSocketNameColorOnItems
            }
        // replace the open socket with the Socket Gem name followed by the socket gem lore
        return previousLore.toMutableList().apply {
            set(indexOfFirstSocket, "$chatColorForSocketGemName${socketGem.name.chatColorize()}")
            addAll(indexOfFirstSocket + 1, socketGem.lore.chatColorize())
        }.toList()
    }

    internal fun applySocketGemDisplayName(
        previousDisplayName: String,
        socketGem: SocketGem
    ): String {
        // apply the prefix and then the suffix
        val prefixedDisplayName = applySocketGemDisplayNamePrefix(previousDisplayName, socketGem)
        return applySocketGemDisplayNameSuffix(prefixedDisplayName, socketGem)
    }

    internal fun applySocketGemDisplayNamePrefix(
        previousDisplayName: String,
        socketGem: SocketGem
    ): String {
        // if socket gem doesn't have a prefix, don't do anything
        if (socketGem.prefix.isBlank()) {
            return previousDisplayName
        }

        // get the previous colors
        val prefixColorString = previousDisplayName.firstChatColors()

        // get all socket gem prefixes
        val socketGemPrefixes = socketGemManager.get().map { it.prefix }

        // if we're preventing multiple changes from sockets and the item already has a prefix,
        // don't do anything
        if (
            settingsManager.socketingSettings.options.isPreventMultipleNameChangesFromSockets &&
            previousDisplayName.stripColors().startsWithAny(socketGemPrefixes, true)
        ) {
            return previousDisplayName
        }

        return "$prefixColorString${socketGem.prefix.chatColorize()}${ChatColor.RESET} $previousDisplayName"
    }

    internal fun applySocketGemDisplayNameSuffix(
        previousDisplayName: String,
        socketGem: SocketGem
    ): String {
        // if socket gem doesn't have a prefix, don't do anything
        if (socketGem.suffix.isBlank()) {
            return previousDisplayName
        }

        // get the previous colors
        val prefixColorString = previousDisplayName.firstChatColors()
        // get the colors for the end of the name
        val suffixColorString = ChatColor.getLastColors(previousDisplayName)

        // get all socket gem prefixes
        val socketGemSuffixes = socketGemManager.get().map { it.suffix }

        // if we're preventing multiple changes from sockets and the item already has a prefix,
        // don't do anything
        if (
            settingsManager.socketingSettings.options.isPreventMultipleNameChangesFromSockets &&
            previousDisplayName.stripColors().endsWithAny(socketGemSuffixes, true)
        ) {
            return previousDisplayName
        }

        return "$previousDisplayName $prefixColorString${socketGem.suffix.chatColorize()}$suffixColorString"
    }

    internal fun applySocketGemEnchantments(
        targetItemEnchantments: Map<Enchantment, Int>,
        socketGem: SocketGem
    ): Map<Enchantment, Int> {
        val mutableEnchantmentsMap = targetItemEnchantments.toMutableMap()
        socketGem.enchantments.forEach {
            mutableEnchantmentsMap[it.enchantment] =
                mutableEnchantmentsMap.getOrPut(it.enchantment) { 0 } + it.getRandomLevel()
        }
        return mutableEnchantmentsMap.toMap()
    }
}
