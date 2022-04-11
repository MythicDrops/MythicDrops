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
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.CustomEnchantmentRegistry
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
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.cache.SocketGemCacheManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerGuiFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.armor.ArmorListener
import com.tealcube.minecraft.bukkit.mythicdrops.crafting.CraftingListener
import com.tealcube.minecraft.bukkit.mythicdrops.debug.DebugListener
import com.tealcube.minecraft.bukkit.mythicdrops.debug.MythicDebugManager
import com.tealcube.minecraft.bukkit.mythicdrops.enchantments.MythicCustomEnchantmentRegistry
import com.tealcube.minecraft.bukkit.mythicdrops.errors.MythicLoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.hdb.HeadDatabaseAdapters
import com.tealcube.minecraft.bukkit.mythicdrops.identification.IdentificationInventoryDragListener
import com.tealcube.minecraft.bukkit.mythicdrops.inventories.AnvilListener
import com.tealcube.minecraft.bukkit.mythicdrops.inventories.EnchantmentTableListener
import com.tealcube.minecraft.bukkit.mythicdrops.inventories.GrindstoneListener
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicCustomItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicProductionLine
import com.tealcube.minecraft.bukkit.mythicdrops.items.factories.MythicCustomItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.items.factories.MythicIdentificationItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.items.factories.MythicSocketGemItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.items.factories.MythicTieredItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.items.strategies.MultipleDropStrategy
import com.tealcube.minecraft.bukkit.mythicdrops.items.strategies.MythicDropStrategyManager
import com.tealcube.minecraft.bukkit.mythicdrops.items.strategies.SingleDropStrategy
import com.tealcube.minecraft.bukkit.mythicdrops.relations.MythicRelationManager
import com.tealcube.minecraft.bukkit.mythicdrops.repair.MythicRepairItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.repair.RepairingListener
import com.tealcube.minecraft.bukkit.mythicdrops.settings.MythicSettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.smithing.SmithingListener
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.MythicSocketGemManager
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.SocketEffectListener
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.SocketExtenderInventoryDragListener
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.SocketGemCombinerListener
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.SocketInventoryDragListener
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.cache.MythicSocketGemCacheManager
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.cache.SocketGemCacheListener
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.combiners.MythicSocketGemCombinerGuiFactory
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.combiners.MythicSocketGemCombinerManager
import com.tealcube.minecraft.bukkit.mythicdrops.spawning.ItemDroppingListener
import com.tealcube.minecraft.bukkit.mythicdrops.spawning.ItemSpawningListener
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.MythicTierManager
import io.pixeloutlaw.minecraft.spigot.mythicdrops.scheduleSyncDelayedTask
import net.kyori.adventure.platform.AudienceProvider
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.onClose
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.binds
import org.koin.dsl.module

internal fun pluginModule(plugin: MythicDropsPlugin): Module {
    return module {
        single { plugin } binds arrayOf(MythicDrops::class, JavaPlugin::class, Plugin::class)
    }
}

internal val coreModule = module {
    singleOf(::MythicItemGroupManager) {
        bind<ItemGroupManager>()
    }
    singleOf(::MythicSocketGemManager) {
        bind<SocketGemManager>()
    }
    singleOf(::MythicSocketGemCombinerManager) {
        bind<SocketGemCombinerManager>()
    }
    singleOf(::MythicLoadingErrorManager) {
        bind<LoadingErrorManager>()
    }
    singleOf(::MythicTierManager) {
        bind<TierManager>()
    }
    singleOf(::MythicRepairItemManager) {
        bind<RepairItemManager>()
    }
    singleOf(::MythicCustomItemManager) {
        bind<CustomItemManager>()
    }
    singleOf(::MythicRelationManager) {
        bind<RelationManager>()
    }
    singleOf(::MythicSettingsManager) {
        bind<SettingsManager>()
    }
    singleOf(::MythicDropStrategyManager) {
        bind<DropStrategyManager>()
    }
    singleOf(::MythicCustomEnchantmentRegistry) {
        bind<CustomEnchantmentRegistry>()
    }
    singleOf(::MythicCustomItemFactory) {
        bind<CustomItemFactory>()
    }
    singleOf(::MythicProductionLine) {
        bind<ProductionLine>()
    }
    singleOf(::MythicSocketGemCombinerGuiFactory) {
        bind<SocketGemCombinerGuiFactory>()
    }
    singleOf(::MythicTieredItemFactory) {
        bind<TieredItemFactory>()
    }
    singleOf(::MythicIdentificationItemFactory) {
        bind<IdentificationItemFactory>()
    }
    singleOf(::MythicSocketGemItemFactory) {
        bind<SocketGemItemFactory>()
    }
    singleOf(::SingleDropStrategy)
    singleOf(::MultipleDropStrategy)
    singleOf(::DebugListener)
    singleOf(::AnvilListener)
    singleOf(::EnchantmentTableListener)
    singleOf(::CraftingListener)
    singleOf(::ArmorListener)
    singleOf(::GrindstoneListener)
    singleOf(::SmithingListener)
    single<SocketGemCacheManager> { MythicSocketGemCacheManager(get<MythicDropsPlugin>()::scheduleSyncDelayedTask) }
    single { HeadDatabaseAdapters.determineAdapter() }
    single { MythicDebugManager }
    single { BukkitAudiences.create(get()) }.withOptions {
        bind<AudienceProvider>()
        onClose {
            it?.close()
        }
    }
}

internal val creatureSpawningModule = module {
    singleOf(::ItemDroppingListener)
    singleOf(::ItemSpawningListener)
}

internal val repairingModule = module {
    singleOf(::RepairingListener)
}

internal val socketingModule = module {
    singleOf(::SocketInventoryDragListener)
    singleOf(::SocketEffectListener)
    singleOf(::SocketGemCacheListener)
    singleOf(::SocketGemCombinerListener)
    singleOf(::SocketExtenderInventoryDragListener)
}

internal val identifyingModule = module {
    singleOf(::IdentificationInventoryDragListener)
}

internal val featureModules: List<Module> = listOf(
    coreModule,
    creatureSpawningModule,
    repairingModule,
    socketingModule,
    identifyingModule
)
