/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2021 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.items.factories

import com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories.SocketGemItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.replaceArgs
import com.tealcube.minecraft.bukkit.mythicdrops.replaceWithCollections
import com.tealcube.minecraft.bukkit.mythicdrops.setDisplayNameChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.setLoreChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.splitOnNewlines
import com.tealcube.minecraft.bukkit.mythicdrops.trimEmpty
import io.pixeloutlaw.minecraft.spigot.mythicdrops.addItemFlags
import io.pixeloutlaw.minecraft.spigot.mythicdrops.customModelData
import io.pixeloutlaw.minecraft.spigot.mythicdrops.mythicDropsSocketExtender
import io.pixeloutlaw.minecraft.spigot.mythicdrops.mythicDropsSocketGem
import io.pixeloutlaw.minecraft.spigot.mythicdrops.setPersistentDataBoolean
import io.pixeloutlaw.minecraft.spigot.mythicdrops.setPersistentDataString
import io.pixeloutlaw.minecraft.spigot.plumbing.lib.ItemAttributes
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.koin.core.annotation.Single

@Single
internal class MythicSocketGemItemFactory(private val settingsManager: SettingsManager) : SocketGemItemFactory {
    override fun toItemStack(socketGem: SocketGem): ItemStack? {
        val material =
            settingsManager.socketingSettings.options.socketGemMaterialIds.randomOrNull() ?: return null
        val socketGemOptions = settingsManager.socketingSettings.items.socketGem
        return ItemAttributes.cloneWithDefaultAttributes(ItemStack(material)).apply {
            setDisplayNameChatColorized(
                socketGemOptions.name.replaceArgs(
                    "%socketgem%" to socketGem.name
                )
            )
            val combinedTypeLore = socketGem.getPresentableType(
                socketGemOptions.allOfSocketTypeLore,
                socketGemOptions.anyOfSocketTypeLore,
                socketGemOptions.noneOfSocketTypeLore
            ).joinToString(separator = "\n", prefix = "\n") // leaving prefix for backwards compatibility
            val allOfTypeLore =
                socketGem.getPresentableType(socketGemOptions.allOfSocketTypeLore, emptyList(), emptyList())
                    .filterNot { it.isBlank() }.joinToString(separator = "\n")
            val anyOfTypeLore =
                socketGem.getPresentableType(emptyList(), socketGemOptions.anyOfSocketTypeLore, emptyList())
                    .filterNot { it.isBlank() }.joinToString(separator = "\n")
            val noneOfTypeLore =
                socketGem.getPresentableType(emptyList(), emptyList(), socketGemOptions.noneOfSocketTypeLore)
                    .filterNot { it.isBlank() }.joinToString(separator = "\n")
            val typeLore = socketGemOptions.socketTypeLore.replaceArgs(
                "%type%" to combinedTypeLore,
                "%alloftype%" to allOfTypeLore,
                "%anyoftype%" to anyOfTypeLore,
                "%noneoftype%" to noneOfTypeLore
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
            setLoreChatColorized(lore)

            if (socketGemOptions.isGlow) {
                addUnsafeEnchantment(Enchantment.DURABILITY, 1)
                addItemFlags(ItemFlag.HIDE_ENCHANTS)
            }

            setPersistentDataString(mythicDropsSocketGem, socketGem.name)

            customModelData = socketGem.customModelData
        }
    }

    override fun buildSocketExtender(): ItemStack? {
        val material =
            settingsManager.socketingSettings.options.socketExtenderMaterialIds.randomOrNull() ?: return null
        return ItemStack(material).apply {
            setDisplayNameChatColorized(settingsManager.socketingSettings.items.socketExtender.name)
            setLoreChatColorized(settingsManager.socketingSettings.items.socketExtender.lore)
            setPersistentDataBoolean(mythicDropsSocketExtender, true)
        }
    }
}
