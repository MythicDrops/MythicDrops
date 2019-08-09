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
package com.tealcube.minecraft.bukkit.mythicdrops.api

import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.ConfigSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.CreatureSpawningSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.IdentifyingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.RelationSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.RepairingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SocketingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.StartupSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.cache.SocketGemCacheManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerGuiFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerManager
import io.pixeloutlaw.minecraft.spigot.config.SmartYamlConfiguration
import io.pixeloutlaw.minecraft.spigot.config.VersionedSmartYamlConfiguration
import se.ranzdo.bukkit.methodcommand.CommandHandler
import java.util.Random

interface MythicDrops {
    @Deprecated("Use replacement config instead.", ReplaceWith("settingsManager.configSettings"))
    val configSettings: ConfigSettings

    @Deprecated("Use replacement config instead.", ReplaceWith("settingsManager.creatureSpawningSettings"))
    val creatureSpawningSettings: CreatureSpawningSettings

    @Deprecated("Use replacement config instead.", ReplaceWith("settingsManager.repairingSettings"))
    val repairingSettings: RepairingSettings

    @Deprecated("Use replacement config instead.", ReplaceWith("settingsManager.socketingSettings"))
    val socketingSettings: SocketingSettings

    @Deprecated("Use replacement config instead.", ReplaceWith("settingsManager.identifyingSettings"))
    val identifyingSettings: IdentifyingSettings

    @Deprecated("Use replacement config instead.", ReplaceWith("settingsManager.relationSettings"))
    val relationSettings: RelationSettings

    @Deprecated("Use replacement config instead.", ReplaceWith("settingsManager.startupSettings"))
    val startupSettings: StartupSettings

    val configYAML: VersionedSmartYamlConfiguration

    val creatureSpawningYAML: VersionedSmartYamlConfiguration

    val customItemYAML: VersionedSmartYamlConfiguration

    val itemGroupYAML: VersionedSmartYamlConfiguration

    val languageYAML: VersionedSmartYamlConfiguration

    val socketGemsYAML: VersionedSmartYamlConfiguration

    val sockettingYAML: VersionedSmartYamlConfiguration

    val repairingYAML: VersionedSmartYamlConfiguration

    val identifyingYAML: VersionedSmartYamlConfiguration

    val relationYAML: VersionedSmartYamlConfiguration

    val socketGemCombinersYAML: SmartYamlConfiguration

    val startupYAML: SmartYamlConfiguration

    val commandHandler: CommandHandler

    val random: Random

    val tierYAMLs: List<SmartYamlConfiguration>

    val itemGroupManager: ItemGroupManager

    val socketGemCacheManager: SocketGemCacheManager

    val socketGemManager: SocketGemManager

    val socketGemCombinerManager: SocketGemCombinerManager

    val socketGemCombinerGuiFactory: SocketGemCombinerGuiFactory

    val settingsManager: SettingsManager

    fun reloadStartupSettings()

    fun reloadSettings()

    fun reloadTiers()

    fun reloadCustomItems()

    fun reloadNames()

    fun reloadConfigurationFiles()

    fun reloadRepairCosts()

    fun reloadItemGroups()

    fun reloadSocketGemCombiners()

    fun saveSocketGemCombiners()
}
