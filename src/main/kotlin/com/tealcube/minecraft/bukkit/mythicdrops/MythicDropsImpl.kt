/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2021 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops

import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.CustomEnchantmentRegistry
import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ProductionLine
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.strategies.DropStrategyManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.relations.RelationManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.cache.SocketGemCacheManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerGuiFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import org.koin.core.Koin

/**
 * Implementation of [MythicDrops] to move it out of [MythicDropsPlugin].
 */
class MythicDropsImpl(koin: Koin) : MythicDrops {
    override val itemGroupManager: ItemGroupManager = koin.get()
    override val socketGemCacheManager: SocketGemCacheManager = koin.get()
    override val socketGemManager: SocketGemManager = koin.get()
    override val socketGemCombinerManager: SocketGemCombinerManager = koin.get()
    override val socketGemCombinerGuiFactory: SocketGemCombinerGuiFactory = koin.get()
    override val settingsManager: SettingsManager = koin.get()
    override val repairItemManager: RepairItemManager = koin.get()
    override val customItemManager: CustomItemManager = koin.get()
    override val relationManager: RelationManager = koin.get()
    override val tierManager: TierManager = koin.get()
    override val loadingErrorManager: LoadingErrorManager = koin.get()
    override val customEnchantmentRegistry: CustomEnchantmentRegistry = koin.get()
    override val dropStrategyManager: DropStrategyManager = koin.get()
    override val productionLine: ProductionLine = koin.get()

    override fun reloadSettings() {
        TODO("Not yet implemented")
    }

    override fun reloadTiers() {
        TODO("Not yet implemented")
    }

    override fun reloadCustomItems() {
        TODO("Not yet implemented")
    }

    override fun reloadNames() {
        TODO("Not yet implemented")
    }

    override fun reloadRepairCosts() {
        TODO("Not yet implemented")
    }

    override fun reloadItemGroups() {
        TODO("Not yet implemented")
    }

    override fun reloadSocketGemCombiners() {
        TODO("Not yet implemented")
    }

    override fun saveSocketGemCombiners() {
        TODO("Not yet implemented")
    }

    override fun reloadSocketGems() {
        TODO("Not yet implemented")
    }

    override fun reloadRelations() {
        TODO("Not yet implemented")
    }
}
