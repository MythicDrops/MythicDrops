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

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.socketing.items.SocketGemOptions
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.replaceArgs
import com.tealcube.minecraft.bukkit.mythicdrops.replaceWithCollections
import com.tealcube.minecraft.bukkit.mythicdrops.setDisplayNameChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.setLoreChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.splitOnNewlines
import com.tealcube.minecraft.bukkit.mythicdrops.trimEmpty
import io.pixeloutlaw.minecraft.spigot.hilt.addItemFlags
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class SocketItem(
    material: Material,
    socketGem: SocketGem,
    socketGemOptions: SocketGemOptions
) : ItemStack(material, 1) {
    init {
        this.setDisplayNameChatColorized(
            socketGemOptions.name.replaceArgs(
                "%socketgem%" to socketGem.name
            )
        )
        val typeLore = socketGemOptions.socketTypeLore.replaceArgs(
            "%type%" to socketGem.getPresentableType(
                socketGemOptions.allOfSocketTypeLore,
                socketGemOptions.anyOfSocketTypeLore,
                socketGemOptions.noneOfSocketTypeLore
            ).joinToString(separator = "\n", prefix = "\n")
        ).splitOnNewlines()
        val familyLore = if (socketGem.family.isNotBlank()) {
            socketGemOptions.familyLore.replaceArgs(
                "%family%" to socketGem.family,
                "%level%" to socketGem.level.toString()
            )
        } else {
            emptyList()
        }
        val socketGemLore = socketGem.lore

        val lore = socketGemOptions.lore.replaceWithCollections(
            "%sockettypelore%" to typeLore,
            "%socketfamilylore%" to familyLore,
            "%socketgemlore%" to socketGemLore
        ).trimEmpty()
        this.setLoreChatColorized(lore)

        if (socketGemOptions.isGlow) {
            this.addUnsafeEnchantment(Enchantment.DURABILITY, 1)
            this.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
    }
}
