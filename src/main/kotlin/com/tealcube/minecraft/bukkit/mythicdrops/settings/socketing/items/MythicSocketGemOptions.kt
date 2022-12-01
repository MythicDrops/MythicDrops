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
package com.tealcube.minecraft.bukkit.mythicdrops.settings.socketing.items

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.socketing.items.SocketGemOptions
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

internal data class MythicSocketGemOptions(
    @Deprecated("Replaced with SocketType#socketGemStyle.")
    override val name: String = "",
    override val lore: List<String> = emptyList(),
    override val familyLore: List<String> = emptyList(),
    @Deprecated("Use itemGroupLore instead.", replaceWith = ReplaceWith("itemGroupLore"))
    override val socketTypeLore: List<String> = emptyList(),
    override val itemGroupLore: List<String> = emptyList(),
    @Deprecated("Use anyOfItemGroupLore instead.", replaceWith = ReplaceWith("anyOfItemGroupLore"))
    override val anyOfSocketTypeLore: List<String> = emptyList(),
    override val anyOfItemGroupLore: List<String> = emptyList(),
    @Deprecated("Use allOfItemGroupLore instead.", replaceWith = ReplaceWith("allOfItemGroupLore"))
    override val allOfSocketTypeLore: List<String> = emptyList(),
    override val allOfItemGroupLore: List<String> = emptyList(),
    @Deprecated("Use noneOfItemGroupLore instead.", replaceWith = ReplaceWith("noneOfItemGroupLore"))
    override val noneOfSocketTypeLore: List<String> = emptyList(),
    override val noneOfItemGroupLore: List<String> = emptyList(),
    override val isGlow: Boolean = false
) : SocketGemOptions {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicSocketGemOptions(
            name = configurationSection.getNonNullString("name"),
            lore = configurationSection.getStringList("lore"),
            familyLore = configurationSection.getStringList("family-lore"),
            socketTypeLore = configurationSection.getStringList("item-group-lore"),
            itemGroupLore = configurationSection.getStringList("item-group-lore"),
            anyOfSocketTypeLore = configurationSection.getStringList("any-of-item-group-lore"),
            anyOfItemGroupLore = configurationSection.getStringList("any-of-item-group-lore"),
            allOfSocketTypeLore = configurationSection.getStringList("all-of-item-group-lore"),
            allOfItemGroupLore = configurationSection.getStringList("all-of-item-group-lore"),
            noneOfSocketTypeLore = configurationSection.getStringList("none-of-item-group-lore"),
            noneOfItemGroupLore = configurationSection.getStringList("none-of-item-group-lore"),
            isGlow = configurationSection.getBoolean("glow")
        )
    }
}
