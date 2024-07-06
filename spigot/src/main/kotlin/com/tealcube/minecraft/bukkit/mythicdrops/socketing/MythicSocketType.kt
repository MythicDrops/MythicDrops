/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2022 Richard Harrah
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

import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketType
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import com.tealcube.minecraft.bukkit.mythicdrops.stripColors
import org.bukkit.configuration.ConfigurationSection

internal data class MythicSocketType(
    override val name: String,
    override val socketGemStyle: String = "",
    override val socketGemStyleChatColorized: String = socketGemStyle.chatColorize(),
    override val socketGemStyleStripped: String = socketGemStyleChatColorized.stripColors(),
    override val socketStyle: String = "",
    override val socketStyleChatColorized: String = socketStyle.chatColorize(),
    override val socketStyleStripped: String = socketStyleChatColorized.stripColors(),
    override val isIgnoreColors: Boolean = false,
    override val weight: Double = 0.0,
    override val socketHelp: List<String> = emptyList()
) : SocketType {
    companion object {
        fun fromConfigurationSection(
            configurationSection: ConfigurationSection,
            key: String
        ): MythicSocketType {
            val weight = configurationSection.getDouble("weight")
            val socketGemStyle = configurationSection.getNonNullString("socket-gem-style")
            val socketStyle = configurationSection.getNonNullString("socket-style")
            val isIgnoreColors = configurationSection.getBoolean("ignore-colors", false)
            val help = configurationSection.getStringList("help")
            return MythicSocketType(
                name = key,
                weight = weight,
                socketGemStyle = socketGemStyle,
                socketStyle = socketStyle,
                isIgnoreColors = isIgnoreColors,
                socketHelp = help
            )
        }
    }
}
