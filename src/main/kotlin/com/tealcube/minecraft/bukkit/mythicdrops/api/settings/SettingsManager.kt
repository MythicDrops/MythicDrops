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
package com.tealcube.minecraft.bukkit.mythicdrops.api.settings

import org.bukkit.configuration.Configuration

/**
 * A manager for storing and retrieving various types of settings.
 */
interface SettingsManager {
    val armorSettings: ArmorSettings
    val configSettings: ConfigSettings
    val creatureSpawningSettings: CreatureSpawningSettings
    val identifyingSettings: IdentifyingSettings
    val languageSettings: LanguageSettings
    val repairingSettings: RepairingSettings
    val socketingSettings: SocketingSettings
    val startupSettings: StartupSettings

    fun loadArmorSettingsFromConfiguration(configuration: Configuration)

    fun loadConfigSettingsFromConfiguration(configuration: Configuration)

    fun loadCreatureSpawningSettingsFromConfiguration(configuration: Configuration)

    fun loadIdentifyingSettingsFromConfiguration(configuration: Configuration)

    fun loadLanguageSettingsFromConfiguration(configuration: Configuration)

    fun loadRepairingSettingsFromConfiguration(configuration: Configuration)

    fun loadSocketingSettingsFromConfiguration(configuration: Configuration)

    fun loadStartupSettingsFromConfiguration(configuration: Configuration)
}
