package com.tealcube.minecraft.bukkit.mythicdrops

import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.debug.DebugBundleGenerator
import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ProductionLine
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.strategies.DropStrategyManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.loading.ConfigLoader
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
import org.koin.core.annotation.Single

@Single
internal class MythicDropsImpl(
    override val itemGroupManager: ItemGroupManager,
    override val socketGemCacheManager: SocketGemCacheManager,
    override val socketGemManager: SocketGemManager,
    override val socketTypeManager: SocketTypeManager,
    override val socketExtenderTypeManager: SocketExtenderTypeManager,
    override val socketGemCombinerManager: SocketGemCombinerManager,
    override val socketGemCombinerGuiFactory: SocketGemCombinerGuiFactory,
    override val settingsManager: SettingsManager,
    override val repairItemManager: RepairItemManager,
    override val customItemManager: CustomItemManager,
    override val relationManager: RelationManager,
    override val tierManager: TierManager,
    override val loadingErrorManager: LoadingErrorManager,
    override val dropStrategyManager: DropStrategyManager,
    override val productionLine: ProductionLine,
    override val configLoader: ConfigLoader,
    override val debugBundleGenerator: DebugBundleGenerator
) : MythicDrops
