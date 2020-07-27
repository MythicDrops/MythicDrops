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

import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.CustomEnchantmentRegistry
import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.strategies.DropStrategyManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.relations.RelationManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.cache.SocketGemCacheManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerGuiFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import io.pixeloutlaw.minecraft.spigot.config.SmartYamlConfiguration
import java.util.Random

interface MythicDrops {
    // REMOVE IN 7.0.0
    @Deprecated("Should not be exposed to API consumers")
    val configYAML: SmartYamlConfiguration

    // REMOVE IN 7.0.0
    @Deprecated("Should not be exposed to API consumers")
    val creatureSpawningYAML: SmartYamlConfiguration

    // REMOVE IN 7.0.0
    @Deprecated("Should not be exposed to API consumers")
    val customItemYAML: SmartYamlConfiguration

    // REMOVE IN 7.0.0
    @Deprecated("Should not be exposed to API consumers")
    val itemGroupYAML: SmartYamlConfiguration

    // REMOVE IN 7.0.0
    @Deprecated("Should not be exposed to API consumers")
    val languageYAML: SmartYamlConfiguration

    // REMOVE IN 7.0.0
    @Deprecated("Should not be exposed to API consumers")
    val socketGemsYAML: SmartYamlConfiguration

    // REMOVE IN 7.0.0
    @Deprecated("Should not be exposed to API consumers")
    val socketingYAML: SmartYamlConfiguration

    // REMOVE IN 7.0.0
    @Deprecated("Should not be exposed to API consumers")
    val repairingYAML: SmartYamlConfiguration

    // REMOVE IN 7.0.0
    @Deprecated("Should not be exposed to API consumers")
    val repairCostsYAML: SmartYamlConfiguration

    // REMOVE IN 7.0.0
    @Deprecated("Should not be exposed to API consumers")
    val identifyingYAML: SmartYamlConfiguration

    // REMOVE IN 7.0.0
    @Deprecated("Should not be exposed to API consumers")
    val relationYAML: SmartYamlConfiguration

    // REMOVE IN 7.0.0
    @Deprecated("Should not be exposed to API consumers")
    val socketGemCombinersYAML: SmartYamlConfiguration

    // REMOVE IN 7.0.0
    @Deprecated("Should not be exposed to API consumers")
    val startupYAML: SmartYamlConfiguration

    // REMOVE IN 7.0.0
    @Deprecated("Should not be exposed to API consumers")
    val random: Random

    // REMOVE IN 7.0.0
    @Deprecated("Should not be exposed to API consumers")
    val tierYAMLs: List<SmartYamlConfiguration>

    val itemGroupManager: ItemGroupManager

    val socketGemCacheManager: SocketGemCacheManager

    val socketGemManager: SocketGemManager

    val socketGemCombinerManager: SocketGemCombinerManager

    val socketGemCombinerGuiFactory: SocketGemCombinerGuiFactory

    val settingsManager: SettingsManager

    val repairItemManager: RepairItemManager

    val customItemManager: CustomItemManager

    val relationManager: RelationManager

    val tierManager: TierManager

    val loadingErrorManager: LoadingErrorManager

    val customEnchantmentRegistry: CustomEnchantmentRegistry

    val dropStrategyManager: DropStrategyManager

    fun reloadSettings()

    fun reloadTiers()

    fun reloadCustomItems()

    fun reloadNames()

    // REMOVE IN 7.0.0
    @Deprecated("Other reload methods will handle loading the respective config files")
    fun reloadConfigurationFiles()

    fun reloadRepairCosts()

    fun reloadItemGroups()

    fun reloadSocketGemCombiners()

    fun saveSocketGemCombiners()

    fun reloadSocketGems()

    fun reloadRelations()
}
