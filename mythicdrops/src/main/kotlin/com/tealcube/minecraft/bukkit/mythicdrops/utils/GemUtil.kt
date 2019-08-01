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
package com.tealcube.minecraft.bukkit.mythicdrops.utils

import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin
import com.tealcube.minecraft.bukkit.mythicdrops.api.choices.Choice
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SockettingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketGemManager
import com.tealcube.minecraft.bukkit.mythicdrops.replaceArgs
import com.tealcube.minecraft.bukkit.mythicdrops.stripColors
import com.tealcube.minecraft.bukkit.mythicdrops.strippedIndexOf
import io.pixeloutlaw.minecraft.spigot.hilt.getDisplayName
import io.pixeloutlaw.minecraft.spigot.hilt.getLore
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

/**
 * Utility methods for working with Socket Gems.
 */
object GemUtil {
    private val socketGemManager: SocketGemManager
        get() = MythicDropsPlugin.getInstance().socketGemManager
    private val sockettingSettings: SockettingSettings
        get() = MythicDropsPlugin.getInstance().sockettingSettings

    /**
     * Gets the gem associated with an [ItemStack] like
     * [com.tealcube.minecraft.bukkit.mythicdrops.socketting.SocketItem].
     *
     * @param itemStack ItemStack to check
     */
    fun getSocketGemFromPotentialSocketItem(itemStack: ItemStack?): SocketGem? {
        if (itemStack == null) {
            return null
        }
        if (!sockettingSettings.socketGemMaterials.contains(itemStack.type)) {
            return null
        }
        val displayName: String = itemStack.getDisplayName() ?: return null
        if (displayName.isBlank()) {
            return null
        }
        val formatFromSettings =
            sockettingSettings.socketGemName.replaceArgs("%socketgem%" to "")
                .replace('&', '\u00A7')
                .replace("\u00A7\u00A7", "&")
                .stripColors()
        val typeFromDisplayName = displayName.stripColors().replace(formatFromSettings, "")
        return getSocketGemFromName(typeFromDisplayName)
    }

    /**
     * Returns index of first open socket in [list], -1 if there are none.
     *
     * @param list List of Strings to check against
     *
     * @return index of first open socket
     */
    fun indexOfFirstOpenSocket(list: List<String>): Int {
        val socketString = sockettingSettings.sockettedItemString.replace('&', '\u00A7').replace("\u00A7\u00A7", "&")
            .replace("%tiercolor%", "")
        return list.strippedIndexOf(socketString, true)
    }

    /**
     * Returns index of first open socket in [list], -1 if there are none.
     *
     * @param itemStack ItemStack to check against
     *
     * @return index of first open socket
     */
    fun indexOfFirstOpenSocket(itemStack: ItemStack): Int =
        indexOfFirstOpenSocket(itemStack.getLore())

    /**
     * Gets [SocketGem] from [SocketGemManager] with case-insensitive searching. Also checks for [name] with underscores
     * replaced by spaces.
     *
     * @param name Name to attempt to find
     * @return
     */
    fun getSocketGemFromName(name: String): SocketGem? {
        for (sg in socketGemManager.getSocketGems()) {
            if (sg.name.equals(name, ignoreCase = true) || sg.name.equals(name.replace("_", " "), ignoreCase = true)) {
                return sg
            }
        }
        return null
    }

    fun getRandomSocketGemFromFamily(family: String): SocketGem? =
        Choice.between(socketGemManager.getSocketGems().filter { it.family.equals(family, ignoreCase = true) }).choose()

    fun getRandomSocketGemFromFamilyAboveLevel(family: String, level: Int): SocketGem? =
        Choice.between(socketGemManager.getSocketGems().filter {
            it.family.equals(
                family,
                ignoreCase = true
            ) && it.level == level + 1
        }).choose()

    fun getRandomSocketGemMaterial(): Material =
        sockettingSettings.socketGemMaterials.random()

    @JvmOverloads
    fun getRandomSocketGemByWeight(entityType: EntityType? = null): SocketGem? =
        socketGemManager.getRandomByWeight { entityType == null || it.canDropFrom(entityType) }

    fun getSocketGemsFromStringList(list: List<String>): List<SocketGem> = list.mapNotNull { getSocketGemFromName(it) }

    fun getSocketGemsFromItemStackLore(itemStack: ItemStack?): List<SocketGem> =
        getSocketGemsFromStringList(itemStack?.getLore() ?: emptyList())
}
