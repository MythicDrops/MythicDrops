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

import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SocketingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGemManager
import com.tealcube.minecraft.bukkit.mythicdrops.stripColors
import com.tealcube.minecraft.bukkit.mythicdrops.strippedIndexOf
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getSocketGem
import io.pixeloutlaw.minecraft.spigot.mythicdrops.lore
import net.md_5.bungee.api.ChatColor
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

/**
 * Utility methods for working with Socket Gems.
 */
internal object GemUtil {
    private val socketGemManager: SocketGemManager
        get() {
            return MythicDropsApi.mythicDrops.socketGemManager
        }
    private val socketingSettings: SocketingSettings
        get() {
            return MythicDropsApi.mythicDrops.settingsManager.socketingSettings
        }

    /**
     * Gets the gem associated with an [ItemStack].
     *
     * @param itemStack ItemStack to check
     */
    fun getSocketGemFromPotentialSocketItem(itemStack: ItemStack?): SocketGem? {
        return itemStack?.getSocketGem(socketGemManager, socketingSettings)
    }

    /**
     * Returns index of first open socket in [list], -1 if there are none.
     *
     * @param list List of Strings to check against
     *
     * @return index of first open socket
     */
    fun indexOfFirstOpenSocket(list: List<String>): Int {
        val socketString =
            socketingSettings.items.socketedItem.socket.replace('&', '\u00A7')
                .replace("\u00A7\u00A7", "&")
                .replace("%tiercolor%", "")
        return list.strippedIndexOf(ChatColor.stripColor(socketString), true)
    }

    fun getRandomSocketGemByWeightFromFamilyWithLevel(family: String, level: Int): SocketGem? =
        socketGemManager.randomByWeight { it.family.equals(family, ignoreCase = true) && it.level == level }

    fun getRandomSocketGemByWeightWithLevel(level: Int): SocketGem? =
        socketGemManager.randomByWeight { it.level == level }

    fun getRandomSocketGemMaterial(): Material? =
        if (socketingSettings.options.socketGemMaterialIds.isNotEmpty()) {
            socketingSettings.options.socketGemMaterialIds.random()
        } else {
            null
        }

    @JvmOverloads
    fun getRandomSocketGemByWeight(entityType: EntityType? = null): SocketGem? =
        socketGemManager.randomByWeight { entityType == null || it.canDropFrom(entityType) }

    fun getSocketGemsFromItemStackLore(itemStack: ItemStack?): List<SocketGem> =
        getSocketGemsFromStringList(itemStack?.lore ?: emptyList())

    fun doAllGemsHaveSameFamily(gems: List<SocketGem>): Boolean {
        if (gems.isEmpty()) {
            return true
        }
        val family = gems.first().family
        return gems.all { it.family.equals(family, ignoreCase = true) }
    }

    fun doAllGemsHaveSameLevel(gems: List<SocketGem>): Boolean {
        if (gems.isEmpty()) {
            return true
        }
        val level = gems.first().level
        return gems.all { it.level == level }
    }

    private fun getSocketGemsFromStringList(list: List<String>): List<SocketGem> =
        list.mapNotNull { getSocketGemFromName(it.stripColors()) }

    /**
     * Gets [SocketGem] from [SocketGemManager] with case-insensitive searching. Also checks for [name] with underscores
     * replaced by spaces.
     *
     * @param name Name to attempt to find
     * @return
     */
    private fun getSocketGemFromName(name: String): SocketGem? {
        for (sg in socketGemManager.get()) {
            if (sg.name.equals(name, ignoreCase = true) || sg.name.equals(name.replace("_", " "), ignoreCase = true)) {
                return sg
            }
        }
        return null
    }
}
