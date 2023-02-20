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
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemMigrator
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ProductionLine
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.strategies.DropStrategyManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.relations.RelationManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketExtenderTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.cache.SocketGemCacheManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerGuiFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager

/**
 * Allows access to plugin data and utilities.
 */
interface MythicDrops {
    val itemGroupManager: ItemGroupManager

    val socketGemCacheManager: SocketGemCacheManager

    val socketGemManager: SocketGemManager

    val socketTypeManager: SocketTypeManager

    val socketExtenderTypeManager: SocketExtenderTypeManager

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

    val productionLine: ProductionLine

    val itemMigrator: ItemMigrator

    fun reloadSettings()

    fun reloadTiers()

    fun reloadCustomItems()

    fun reloadNames()

    fun reloadRepairCosts()

    fun reloadItemGroups()

    fun reloadSocketGemCombiners()

    fun saveSocketGemCombiners()

    fun reloadSocketGems()

    fun reloadRelations()

    fun generateDebugBundle(): String
}
