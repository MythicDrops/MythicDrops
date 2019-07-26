/*
 * The MIT License
 * Copyright © 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.utils

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SockettingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketGemMap
import com.tealcube.minecraft.bukkit.mythicdrops.replaceArgs
import com.tealcube.minecraft.bukkit.mythicdrops.stripColors
import io.pixeloutlaw.minecraft.spigot.hilt.getDisplayName
import io.pixeloutlaw.minecraft.spigot.hilt.getLore
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

/**
 * Utility methods for working with Socket Gems.
 */
object GemUtil {
    /**
     * Gets the gem associated with an [ItemStack] like
     * [com.tealcube.minecraft.bukkit.mythicdrops.socketting.SocketItem].
     *
     * @param sockettingSettings Socketting settings for plugin
     * @param itemStack ItemStack to check
     */
    fun getSocketGemFromSocketGemItemStack(sockettingSettings: SockettingSettings, itemStack: ItemStack): SocketGem? {
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
        return SocketGemMap[typeFromDisplayName] ?: getSocketGemFromName(typeFromDisplayName)
    }

    /**
     * Gets [SocketGem] from [SocketGemMap] with case-insensitive searching. Also checks for [name] with underscores
     * replaced by spaces.
     *
     * @param name Name to attempt to find
     * @return
     */
    fun getSocketGemFromName(name: String): SocketGem? {
        for (sg in SocketGemMap.values) {
            if (sg.name.equals(name, ignoreCase = true) || sg.name.equals(name.replace("_", " "), ignoreCase = true)) {
                return sg
            }
        }
        return null
    }

    fun getRandomSocketGemMaterial(sockettingSettings: SockettingSettings): Material =
        sockettingSettings.socketGemMaterials.random()

    @JvmOverloads
    fun getRandomSocketGemByWeight(entityType: EntityType? = null): SocketGem? =
        SocketGemMap.getRandomByWeight { entityType == null || it.canDropFrom(entityType) }

    fun getSocketGemsFromStringList(list: List<String>): List<SocketGem> = list.mapNotNull { getSocketGemFromName(it) }

    fun getSocketGemsFromItemStackLore(itemStack: ItemStack?): List<SocketGem> =
        getSocketGemsFromStringList(itemStack?.getLore() ?: emptyList())
}