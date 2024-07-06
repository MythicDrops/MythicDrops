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
package com.tealcube.minecraft.bukkit.mythicdrops.koin

import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ProductionLine
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories.CustomItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories.IdentificationItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories.SocketGemItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories.TieredItemFactory
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
import com.tealcube.minecraft.bukkit.mythicdrops.errors.MythicLoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.hdb.HeadDatabaseAdapters
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicCustomItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicProductionLine
import com.tealcube.minecraft.bukkit.mythicdrops.items.factories.MythicCustomItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.items.factories.MythicIdentificationItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.items.factories.MythicSocketGemItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.items.factories.MythicTieredItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.items.strategies.MythicDropStrategyManager
import com.tealcube.minecraft.bukkit.mythicdrops.relations.MythicRelationManager
import com.tealcube.minecraft.bukkit.mythicdrops.repair.MythicRepairItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.settings.MythicSettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.MythicSocketExtenderTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.MythicSocketGemManager
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.MythicSocketTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.cache.MythicSocketGemCacheManager
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.combiners.MythicSocketGemCombinerGuiFactory
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.combiners.MythicSocketGemCombinerManager
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.MythicTierManager
import dev.mythicdrops.spigot.messaging.MessageBroadcaster
import io.pixeloutlaw.minecraft.spigot.mythicdrops.scheduleSyncDelayedTask
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.module.Module
import org.koin.core.module.dsl.onClose
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.binds
import org.koin.dsl.module

internal fun mythicDropsPluginModule(plugin: MythicDropsPlugin): Module =
    module {
        single { plugin } binds arrayOf(MythicDrops::class, JavaPlugin::class, Plugin::class)
    }

internal val mythicDropsModule =
    module {
        single<ItemGroupManager> { MythicItemGroupManager() }
        single<SocketGemCacheManager> { MythicSocketGemCacheManager(get<MythicDropsPlugin>()::scheduleSyncDelayedTask) }
        single<SocketGemManager> { MythicSocketGemManager(get(), get(), get()) }
        single<SocketGemCombinerManager> { MythicSocketGemCombinerManager() }
        single<LoadingErrorManager> { MythicLoadingErrorManager() }
        single<TierManager> { MythicTierManager() }
        single<RepairItemManager> { MythicRepairItemManager() }
        single<CustomItemManager> { MythicCustomItemManager() }
        single<RelationManager> { MythicRelationManager() }
        single<SettingsManager> { MythicSettingsManager() }
        single<DropStrategyManager> { MythicDropStrategyManager() }
        single { HeadDatabaseAdapters.determineAdapter() }
        single<CustomItemFactory> { MythicCustomItemFactory(get()) }
        single<ProductionLine> { MythicProductionLine(get(), get(), get(), get()) }
        single<SocketGemCombinerGuiFactory> { MythicSocketGemCombinerGuiFactory(get(), get()) }
        single<TieredItemFactory> { MythicTieredItemFactory(get(), get(), get(), get(), get(), get()) }
        single<IdentificationItemFactory> { MythicIdentificationItemFactory(get(), get()) }
        single<SocketGemItemFactory> { MythicSocketGemItemFactory(get(), get()) }
        single<SocketTypeManager> { MythicSocketTypeManager() }
        single<SocketExtenderTypeManager> { MythicSocketExtenderTypeManager(get(), get()) }
        single { BukkitAudiences.create(get()) } withOptions {
            onClose { it?.close() }
        }
        single { MessageBroadcaster(get(), get<SettingsManager>().languageSettings) }
    }
