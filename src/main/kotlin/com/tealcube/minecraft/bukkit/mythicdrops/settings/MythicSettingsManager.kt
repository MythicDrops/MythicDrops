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

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.ArmorSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.ConfigSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.CreatureSpawningSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.IdentifyingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.LanguageSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.RepairingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SocketingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.StartupSettings
import org.bukkit.configuration.Configuration

internal class MythicSettingsManager : SettingsManager {
    override var armorSettings: ArmorSettings = MythicArmorSettings()
        private set
    override var configSettings: ConfigSettings = MythicConfigSettings()
        private set
    override var creatureSpawningSettings: CreatureSpawningSettings = MythicCreatureSpawningSettings()
        private set
    override var identifyingSettings: IdentifyingSettings = MythicIdentifyingSettings()
        private set
    override var languageSettings: LanguageSettings = MythicLanguageSettings()
        private set
    override var repairingSettings: RepairingSettings = MythicRepairingSettings()
        private set
    override var socketingSettings: SocketingSettings = MythicSocketingSettings()
        private set
    override var startupSettings: StartupSettings = MythicStartupSettings()
        private set

    override fun loadArmorSettingsFromConfiguration(configuration: Configuration) {
        armorSettings = MythicArmorSettings.fromConfigurationSection(configuration)
    }

    override fun loadConfigSettingsFromConfiguration(configuration: Configuration) {
        configSettings = MythicConfigSettings.fromConfigurationSection(configuration)
    }

    override fun loadCreatureSpawningSettingsFromConfiguration(configuration: Configuration) {
        creatureSpawningSettings = MythicCreatureSpawningSettings.fromConfigurationSection(configuration)
    }

    override fun loadIdentifyingSettingsFromConfiguration(configuration: Configuration) {
        identifyingSettings = MythicIdentifyingSettings.fromConfigurationSection(configuration)
    }

    override fun loadLanguageSettingsFromConfiguration(configuration: Configuration) {
        languageSettings = MythicLanguageSettings.fromConfigurationSection(configuration)
    }

    override fun loadRepairingSettingsFromConfiguration(configuration: Configuration) {
        repairingSettings = MythicRepairingSettings.fromConfigurationSection(configuration)
    }

    override fun loadSocketingSettingsFromConfiguration(configuration: Configuration) {
        socketingSettings = MythicSocketingSettings.fromConfigurationSection(configuration)
    }

    override fun loadStartupSettingsFromConfiguration(configuration: Configuration) {
        startupSettings = MythicStartupSettings.fromConfigurationSection(configuration)
    }
}
