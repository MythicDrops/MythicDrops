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
package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.LanguageSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.CommandMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.GeneralMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.IdentificationMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.RepairingMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.SocketingMessages
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.MythicCommandMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.MythicGeneralMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.MythicIdentificationMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.MythicRepairingMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.MythicSocketingMessages
import org.bukkit.configuration.ConfigurationSection

data class MythicLanguageSettings(
    override val version: String = "",
    override val general: GeneralMessages = MythicGeneralMessages(),
    override val command: CommandMessages = MythicCommandMessages(),
    override val identification: IdentificationMessages = MythicIdentificationMessages(),
    override val repairing: RepairingMessages = MythicRepairingMessages(),
    override val socketing: SocketingMessages = MythicSocketingMessages(),
    override val displayNames: Map<String, String> = emptyMap()
) : LanguageSettings {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection): MythicLanguageSettings {
            val displayNames = configurationSection.getOrCreateSection("display-names")
                .let { it.getKeys(false).map { key -> key to it.getNonNullString(key) }.toMap() }
            return MythicLanguageSettings(
                configurationSection.getNonNullString("version"),
                MythicGeneralMessages.fromConfigurationSection(configurationSection.getOrCreateSection("general")),
                MythicCommandMessages.fromConfigurationSection(configurationSection.getOrCreateSection("command")),
                MythicIdentificationMessages.fromConfigurationSection(configurationSection.getOrCreateSection("identification")),
                MythicRepairingMessages.fromConfigurationSection(configurationSection.getOrCreateSection("repairing")),
                MythicSocketingMessages.fromConfigurationSection(configurationSection.getOrCreateSection("socketing")),
                displayNames
            )
        }
    }
}
