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
package com.tealcube.minecraft.bukkit.mythicdrops.settings

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.StartupSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.ConfigSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.CreatureSpawningSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.IdentifyingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.LanguageSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.RepairingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.SocketingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.MythicConfigSettings
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.MythicCreatureSpawningSettings
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.MythicIdentifyingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.MythicLanguageSettings
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.MythicRepairingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.MythicSocketingSettings
import org.bukkit.configuration.Configuration

data class MythicSettingsManager(
    override var configSettings: ConfigSettings = MythicConfigSettings(),
    override var creatureSpawningSettings: CreatureSpawningSettings = MythicCreatureSpawningSettings(),
    override var identifyingSettings: IdentifyingSettings = MythicIdentifyingSettings(),
    override var languageSettings: LanguageSettings = MythicLanguageSettings(),
    override var repairingSettings: RepairingSettings = MythicRepairingSettings(),
    override var socketingSettings: SocketingSettings = MythicSocketingSettings(),
    override var startupSettings: StartupSettings = MythicStartupSettings()
) : SettingsManager {
    fun loadConfigSettingsFromConfiguration(configuration: Configuration) {
        configSettings = MythicConfigSettings.fromConfigurationSection(configuration)
    }

    fun loadCreatureSpawningSettingsFromConfiguration(configuration: Configuration) {
        creatureSpawningSettings = MythicCreatureSpawningSettings.fromConfigurationSection(configuration)
    }

    fun loadIdentifyingSettingsFromConfiguration(configuration: Configuration) {
        identifyingSettings = MythicIdentifyingSettings.fromConfigurationSection(configuration)
    }

    fun loadLanguageSettingsFromConfiguration(configuration: Configuration) {
        languageSettings = MythicLanguageSettings.fromConfigurationSection(configuration)
    }

    fun loadRepairingSettingsFromConfiguration(configuration: Configuration) {
        repairingSettings = MythicRepairingSettings.fromConfigurationSection(configuration)
    }

    fun loadSocketingSettingsFromConfiguration(configuration: Configuration) {
        socketingSettings = MythicSocketingSettings.fromConfigurationSection(configuration)
    }

    fun loadStartupSettingsFromConfiguration(configuration: Configuration) {
        startupSettings = MythicStartupSettings.fromConfigurationSection(configuration)
    }
}

