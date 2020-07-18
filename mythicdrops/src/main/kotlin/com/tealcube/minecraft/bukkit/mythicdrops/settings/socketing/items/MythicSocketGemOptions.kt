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

import com.squareup.moshi.JsonClass
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.socketing.items.SocketGemOptions
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

@JsonClass(generateAdapter = true)
data class MythicSocketGemOptions internal constructor(
    override val name: String = "",
    override val lore: List<String> = emptyList(),
    override val familyLore: List<String> = emptyList(),
    override val socketTypeLore: List<String> = emptyList(),
    override val anyOfSocketTypeLore: List<String> = emptyList(),
    override val allOfSocketTypeLore: List<String> = emptyList(),
    override val noneOfSocketTypeLore: List<String> = emptyList(),
    override val isGlow: Boolean = false
) : SocketGemOptions {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicSocketGemOptions(
            name = configurationSection.getNonNullString("name"),
            lore = configurationSection.getStringList("lore"),
            familyLore = configurationSection.getStringList("family-lore"),
            socketTypeLore = configurationSection.getStringList("socket-type-lore"),
            anyOfSocketTypeLore = configurationSection.getStringList("any-of-socket-type-lore"),
            allOfSocketTypeLore = configurationSection.getStringList("all-of-socket-type-lore"),
            noneOfSocketTypeLore = configurationSection.getStringList("none-of-socket-type-lore"),
            isGlow = configurationSection.getBoolean("glow")
        )
    }
}
