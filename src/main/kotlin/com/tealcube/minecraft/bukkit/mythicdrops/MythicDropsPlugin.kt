/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2020 Richard Harrah
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

import co.aikar.commands.ConditionFailedException
import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.PaperCommandManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.CustomEnchantmentRegistry
import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroup
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ProductionLine
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.builders.DropBuilder
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.strategies.DropStrategyManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.names.NameType
import com.tealcube.minecraft.bukkit.mythicdrops.api.relations.RelationManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.GemTriggerType
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketExtenderTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.cache.SocketGemCacheManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerGuiFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.worldguard.WorldGuardFlags
import com.tealcube.minecraft.bukkit.mythicdrops.armor.ArmorListener
import com.tealcube.minecraft.bukkit.mythicdrops.aura.AuraRunnable
import com.tealcube.minecraft.bukkit.mythicdrops.commands.CombinerCommands
import com.tealcube.minecraft.bukkit.mythicdrops.commands.CustomCreateCommand
import com.tealcube.minecraft.bukkit.mythicdrops.commands.CustomItemsCommand
import com.tealcube.minecraft.bukkit.mythicdrops.commands.DebugCommand
import com.tealcube.minecraft.bukkit.mythicdrops.commands.DropCommands
import com.tealcube.minecraft.bukkit.mythicdrops.commands.DropRatesCommand
import com.tealcube.minecraft.bukkit.mythicdrops.commands.GiveCommands
import com.tealcube.minecraft.bukkit.mythicdrops.commands.HelpCommand
import com.tealcube.minecraft.bukkit.mythicdrops.commands.ItemGroupsCommand
import com.tealcube.minecraft.bukkit.mythicdrops.commands.ModifyCommands
import com.tealcube.minecraft.bukkit.mythicdrops.commands.ReloadCommand
import com.tealcube.minecraft.bukkit.mythicdrops.commands.SocketGemsCommand
import com.tealcube.minecraft.bukkit.mythicdrops.commands.SpawnCommands
import com.tealcube.minecraft.bukkit.mythicdrops.commands.TiersCommand
import com.tealcube.minecraft.bukkit.mythicdrops.crafting.CraftingListener
import com.tealcube.minecraft.bukkit.mythicdrops.debug.DebugListener
import com.tealcube.minecraft.bukkit.mythicdrops.debug.MythicDebugManager
import com.tealcube.minecraft.bukkit.mythicdrops.hdb.HeadDatabaseAdapter
import com.tealcube.minecraft.bukkit.mythicdrops.identification.IdentificationInventoryDragListener
import com.tealcube.minecraft.bukkit.mythicdrops.inventories.AlreadyBroadcastNbtStripperListener
import com.tealcube.minecraft.bukkit.mythicdrops.inventories.AnvilListener
import com.tealcube.minecraft.bukkit.mythicdrops.inventories.EnchantmentTableListener
import com.tealcube.minecraft.bukkit.mythicdrops.inventories.GrindstoneListener
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicCustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicItemGroup
import com.tealcube.minecraft.bukkit.mythicdrops.items.strategies.MultipleDropStrategy
import com.tealcube.minecraft.bukkit.mythicdrops.items.strategies.SingleDropStrategy
import com.tealcube.minecraft.bukkit.mythicdrops.koin.MythicKoinComponent
import com.tealcube.minecraft.bukkit.mythicdrops.koin.MythicKoinContext
import com.tealcube.minecraft.bukkit.mythicdrops.koin.inject
import com.tealcube.minecraft.bukkit.mythicdrops.koin.mythicDropsModule
import com.tealcube.minecraft.bukkit.mythicdrops.koin.mythicDropsPluginModule
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory
import com.tealcube.minecraft.bukkit.mythicdrops.logging.MythicDropsLogger
import com.tealcube.minecraft.bukkit.mythicdrops.logging.MythicDropsLoggingFormatter
import com.tealcube.minecraft.bukkit.mythicdrops.names.NameMap
import com.tealcube.minecraft.bukkit.mythicdrops.relations.MythicRelation
import com.tealcube.minecraft.bukkit.mythicdrops.repair.MythicRepairItem
import com.tealcube.minecraft.bukkit.mythicdrops.repair.RepairingListener
import com.tealcube.minecraft.bukkit.mythicdrops.smithing.SmithingListener
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.SocketEffectListener
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.SocketExtenderInventoryDragListener
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.SocketGemCombinerListener
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.SocketInventoryDragListener
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.cache.SocketGemCacheListener
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.combiners.MythicSocketGemCombiner
import com.tealcube.minecraft.bukkit.mythicdrops.spawning.ItemDroppingListener
import com.tealcube.minecraft.bukkit.mythicdrops.spawning.ItemSpawningListener
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.MythicTier
import com.tealcube.minecraft.bukkit.mythicdrops.utils.EnchantmentUtil
import com.tealcube.minecraft.bukkit.mythicdrops.worldguard.registerFlags
import dev.mythicdrops.prettyPrint
import io.pixeloutlaw.kindling.Log
import io.pixeloutlaw.minecraft.spigot.config.ConfigMigratorSerialization
import io.pixeloutlaw.minecraft.spigot.config.VersionedFileAwareYamlConfiguration
import io.pixeloutlaw.minecraft.spigot.config.migration.migrators.JarConfigMigrator
import io.pixeloutlaw.minecraft.spigot.klob.Glob
import io.pixeloutlaw.minecraft.spigot.mythicdrops.scheduleSyncDelayedTask
import io.pixeloutlaw.minecraft.spigot.plumbing.api.MinecraftVersions
import io.pixeloutlaw.minecraft.spigot.resettableLazy
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.Bukkit
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import org.koin.dsl.koinApplication
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.logging.FileHandler
import java.util.logging.Level

@Suppress("detekt.LargeClass", "detekt.TooManyFunctions")
class MythicDropsPlugin : JavaPlugin(), MythicDrops, MythicKoinComponent {
    companion object {
        private const val ALREADY_LOADED_TIER_MSG =
            "Not loading %s as there is already a tier with that display color and identifier color loaded: %s"
        private lateinit var instance: MythicDropsPlugin
        private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")

        @JvmStatic
        fun getInstance() = instance

        @Deprecated(
            "Use MythicDropsApi instead",
            ReplaceWith(
                "MythicDropsApi.mythicDrops.productionLine.tieredItemFactory.getNewDropBuilder()",
                "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
            )
        )
        @JvmStatic
        fun getNewDropBuilder(): DropBuilder = MythicDropsApi.mythicDrops.productionLine.tieredItemFactory.getNewDropBuilder()
    }

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.itemGroupManager",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override val itemGroupManager: ItemGroupManager by inject()

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.socketGemCacheManager",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override val socketGemCacheManager: SocketGemCacheManager by inject()

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.socketGemManager",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override val socketGemManager: SocketGemManager by inject()

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.socketTypeManager",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override val socketTypeManager: SocketTypeManager by inject()

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.socketExtenderTypeManager",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override val socketExtenderTypeManager: SocketExtenderTypeManager by inject()

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.socketGemCombinerManager",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override val socketGemCombinerManager: SocketGemCombinerManager by inject()

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.socketGemCombinerGuiFactory",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override val socketGemCombinerGuiFactory: SocketGemCombinerGuiFactory by inject()

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.settingsManager",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override val settingsManager: SettingsManager by inject()

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.repairItemManager",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override val repairItemManager: RepairItemManager by inject()

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.customItemManager",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override val customItemManager: CustomItemManager by inject()

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.relationManager",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override val relationManager: RelationManager by inject()

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.tierManager",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override val tierManager: TierManager by inject()

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.loadingErrorManager",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override val loadingErrorManager: LoadingErrorManager by inject()

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.customEnchantmentRegistry",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override val customEnchantmentRegistry: CustomEnchantmentRegistry by inject()

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.dropStrategyManager",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override val dropStrategyManager: DropStrategyManager by inject()

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.productionLine",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override val productionLine: ProductionLine by inject()

    private val armorYAML: VersionedFileAwareYamlConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(dataFolder, "armor.yml"))
    }
    private val configYAML: VersionedFileAwareYamlConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(dataFolder, "config.yml"))
    }
    private val creatureSpawningYAML: VersionedFileAwareYamlConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(dataFolder, "creatureSpawning.yml"))
    }
    internal val customItemYAML: VersionedFileAwareYamlConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(dataFolder, "customItems.yml"))
    }
    private val itemGroupYAML: VersionedFileAwareYamlConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(dataFolder, "itemGroups.yml"))
    }
    private val languageYAML: VersionedFileAwareYamlConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(dataFolder, "language.yml"))
    }
    private val socketGemsYAML: VersionedFileAwareYamlConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(dataFolder, "socketGems.yml"))
    }
    private val socketTypesYAML: VersionedFileAwareYamlConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(dataFolder, "socketTypes.yml"))
    }
    private val socketingYAML: VersionedFileAwareYamlConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(dataFolder, "socketing.yml"))
    }
    private val repairingYAML: VersionedFileAwareYamlConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(dataFolder, "repairing.yml"))
    }
    private val repairCostsYAML: VersionedFileAwareYamlConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(dataFolder, "repairCosts.yml"))
    }
    private val identifyingYAML: VersionedFileAwareYamlConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(dataFolder, "identifying.yml"))
    }
    private val relationYAML: VersionedFileAwareYamlConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(dataFolder, "relation.yml"))
    }
    private val socketGemCombinersYAML: VersionedFileAwareYamlConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(dataFolder, "socketGemCombiners.yml"))
    }
    private val startupYAML: VersionedFileAwareYamlConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(dataFolder, "startup.yml"))
    }
    private val tierYAMLs =
        resettableLazy {
            Glob.from("tiers/**/*.yml").iterate(dataFolder.toPath()).asSequence().toList()
                .map { VersionedFileAwareYamlConfiguration(it.toFile()) }
        }
    private val jarConfigMigrator by lazy {
        JarConfigMigrator(
            jarFile = file,
            dataFolder = dataFolder,
            backupOnMigrate = MythicDropsApi.mythicDrops.settingsManager.startupSettings.isBackupOnConfigMigrate
        )
    }
    private val headDatabaseAdapter: HeadDatabaseAdapter by inject()
    private val audiences: BukkitAudiences by inject()
    private var auraTask: BukkitTask? = null

    override fun onLoad() {
        // register our flags with WorldGuard
        WorldGuardFlags.registerFlags()
    }

    override fun onEnable() {
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            logger.severe("Unable to create data folder - disabling MythicDrops!")
            Log.error("Unable to create data folder - disabling MythicDrops!")
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        if (!MinecraftVersions.isAtLeastMinecraft117) {
            logger.severe(
                "MythicDrops only supports Minecraft 1.17+ due to the Java 16 changes - disabling MythicDrops!"
            )
            Log.error(
                "MythicDrops only supports Minecraft 1.17+ due to the Java 16 changes - disabling MythicDrops!"
            )
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        instance = this
        MythicDropsApi.mythicDrops = this
        // setup logging
        JulLoggerFactory.clearCachedLoggers()
        JulLoggerFactory.clearCustomizers()
        JulLoggerFactory.registerLoggerCustomizer { logger ->
            logger.apply {
                level = Level.ALL
                addHandler(
                    FileHandler(
                        String.format(
                            "%s/%s.log",
                            dataFolder.absolutePath,
                            this@MythicDropsPlugin.name.lowercase(Locale.getDefault())
                        ),
                        true
                    ).apply {
                        level = Level.ALL
                        formatter = MythicDropsLoggingFormatter()
                    }
                )
                useParentHandlers = false
            }
        }

        // initialize koin
        // we have to do this early due to startup settings depending on it
        val koinApp =
            koinApplication {
                modules(mythicDropsPluginModule(this@MythicDropsPlugin), mythicDropsModule)
            }
        MythicKoinContext.koinApp = koinApp
        reloadStartupSettings()

        ConfigMigratorSerialization.registerAll()

        customEnchantmentRegistry.registerEnchantments()

        Log.info("Loading configuration files...")
        writeConfigFilesAndMigrate()

        Log.info("Writing resources files if necessary...")
        writeResourceFiles()

        Log.info("Registering default drop strategies...")
        dropStrategyManager.add(SingleDropStrategy(this))
        dropStrategyManager.add(MultipleDropStrategy(this))

        Log.info("Loading all settings and everything else...")
        reloadSettings()
        reloadItemGroups()
        reloadTiers()
        reloadNames()
        reloadCustomItems()
        reloadRepairCosts()
        reloadSocketGems()
        reloadRelations()

        // SocketGemCombiners need to be loaded after the worlds have been loaded, so run a delayed
        // task:
        server.scheduler.runTask(this, Runnable { MythicDropsApi.mythicDrops.reloadSocketGemCombiners() })

        Log.info("Registering general event listeners...")
        Bukkit.getPluginManager().registerEvents(DebugListener(MythicDebugManager), this)
        Bukkit.getPluginManager().registerEvents(AlreadyBroadcastNbtStripperListener(), this)
        Bukkit.getPluginManager()
            .registerEvents(
                AnvilListener(
                    MythicDropsApi.mythicDrops.settingsManager,
                    MythicDropsApi.mythicDrops.tierManager,
                    MythicDropsApi.mythicDrops.socketExtenderTypeManager
                ),
                this
            )
        Bukkit.getPluginManager()
            .registerEvents(EnchantmentTableListener(MythicDropsApi.mythicDrops.settingsManager), this)
        Bukkit.getPluginManager().registerEvents(
            CraftingListener(
                MythicDropsApi.mythicDrops.settingsManager,
                MythicDropsApi.mythicDrops.socketExtenderTypeManager
            ),
            this
        )
        Bukkit.getPluginManager().registerEvents(ArmorListener(MythicDropsApi.mythicDrops.settingsManager), this)
        Bukkit.getPluginManager().registerEvents(
            GrindstoneListener(
                MythicDropsApi.mythicDrops.customEnchantmentRegistry,
                MythicDropsApi.mythicDrops.customItemManager,
                MythicDropsApi.mythicDrops.settingsManager
            ),
            this
        )
        Bukkit.getPluginManager().registerEvents(
            SmithingListener(
                MythicDropsApi.mythicDrops.customEnchantmentRegistry,
                MythicDropsApi.mythicDrops.customItemManager,
                MythicDropsApi.mythicDrops.settingsManager,
                MythicDropsApi.mythicDrops.tierManager
            ),
            this
        )

        Log.info("Setting up commands...")
        setupCommands()

        if (MythicDropsApi.mythicDrops.settingsManager.configSettings.components.isCreatureSpawningEnabled) {
            Log.info("Mobs spawning with equipment enabled")
            Bukkit.getPluginManager().registerEvents(ItemDroppingListener(this, audiences), this)
            Bukkit.getPluginManager().registerEvents(ItemSpawningListener(this), this)
        }
        if (MythicDropsApi.mythicDrops.settingsManager.configSettings.components.isRepairingEnabled) {
            Log.info("Repairing enabled")
            Bukkit.getPluginManager()
                .registerEvents(
                    RepairingListener(
                        MythicDropsApi.mythicDrops.repairItemManager,
                        MythicDropsApi.mythicDrops.settingsManager
                    ),
                    this
                )
        }
        if (MythicDropsApi.mythicDrops.settingsManager.configSettings.components.isSocketingEnabled) {
            Log.info("Socketing enabled")
            Bukkit.getPluginManager().registerEvents(
                SocketInventoryDragListener(
                    MythicDropsApi.mythicDrops.itemGroupManager,
                    MythicDropsApi.mythicDrops.settingsManager,
                    MythicDropsApi.mythicDrops.socketGemManager,
                    MythicDropsApi.mythicDrops.socketTypeManager
                ),
                this
            )
            Bukkit.getPluginManager().registerEvents(
                SocketEffectListener(
                    this,
                    MythicDropsApi.mythicDrops.socketGemCacheManager,
                    MythicDropsApi.mythicDrops.settingsManager
                ),
                this
            )
            Bukkit.getPluginManager()
                .registerEvents(SocketGemCacheListener(this, MythicDropsApi.mythicDrops.socketGemCacheManager), this)
            Bukkit.getPluginManager().registerEvents(
                SocketGemCombinerListener(
                    MythicDropsApi.mythicDrops.socketGemCombinerManager,
                    MythicDropsApi.mythicDrops.socketGemCombinerGuiFactory
                ),
                this
            )
            Bukkit.getPluginManager().registerEvents(
                SocketExtenderInventoryDragListener(
                    MythicDropsApi.mythicDrops.settingsManager,
                    MythicDropsApi.mythicDrops.socketExtenderTypeManager,
                    MythicDropsApi.mythicDrops.tierManager
                ),
                this
            )
        }
        if (MythicDropsApi.mythicDrops.settingsManager.configSettings.components.isIdentifyingEnabled) {
            Log.info("Identifying enabled")
            Bukkit.getPluginManager().registerEvents(
                IdentificationInventoryDragListener(
                    audiences,
                    MythicDropsApi.mythicDrops.settingsManager,
                    MythicDropsApi.mythicDrops.tierManager
                ),
                this
            )
        }

        Log.info("Shamelessly shilling for Paper...")

        // setup HeadDatabase support a tick after the plugin is enabled to ensure we're not loaded before it
        // this is really only necessary because I don't have a copy of the plugin and can't test it myself
        scheduleSyncDelayedTask {
            headDatabaseAdapter.register(this)
        }

        Log.info("v${description.version} enabled")
    }

    override fun onDisable() {
        HandlerList.unregisterAll(this)
        Bukkit.getScheduler().cancelTasks(this)
        ConfigMigratorSerialization.unregisterAll()

        MythicKoinContext.koinApp?.close()
        MythicKoinContext.koinApp = null
    }

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.reloadSettings",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override fun reloadSettings() {
        reloadStartupSettings()

        Log.debug("Clearing loading errors...")
        loadingErrorManager.clear()

        Log.debug("Loading settings from armor.yml...")
        armorYAML.load()
        MythicDropsApi.mythicDrops.settingsManager.loadArmorSettingsFromConfiguration(armorYAML)

        Log.debug("Loading settings from config.yml...")
        configYAML.load()
        MythicDropsApi.mythicDrops.settingsManager.loadConfigSettingsFromConfiguration(configYAML)

        if (!MythicDropsApi.mythicDrops.settingsManager.configSettings.options.isDisableLegacyItemChecks) {
            val disableLegacyItemChecksWarning =
                """
                Legacy item checks (checking the lore of items) are not disabled! This feature is deprecated and will be removed in MythicDrops 9.x.
                """.trimIndent()
            Log.warn(disableLegacyItemChecksWarning)
            logger.warning(disableLegacyItemChecksWarning)
        }

        Log.debug("Loading settings from language.yml...")
        languageYAML.load()
        MythicDropsApi.mythicDrops.settingsManager.loadLanguageSettingsFromConfiguration(languageYAML)

        Log.debug("Loading settings from creatureSpawning.yml...")
        creatureSpawningYAML.load()
        MythicDropsApi.mythicDrops.settingsManager.loadCreatureSpawningSettingsFromConfiguration(creatureSpawningYAML)

        Log.debug("Loading settings from repairing.yml...")
        repairingYAML.load()
        MythicDropsApi.mythicDrops.settingsManager.loadRepairingSettingsFromConfiguration(repairingYAML)

        Log.debug("Loading settings from socketing.yml...")
        socketingYAML.load()
        MythicDropsApi.mythicDrops.settingsManager.loadSocketingSettingsFromConfiguration(socketingYAML)

        Log.debug("Loading settings from identifying.yml...")
        identifyingYAML.load()
        MythicDropsApi.mythicDrops.settingsManager.loadIdentifyingSettingsFromConfiguration(identifyingYAML)
    }

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.reloadTiers",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override fun reloadTiers() {
        Log.debug("Loading tiers...")
        tierManager.clear()

        tierYAMLs.reset()
        tierYAMLs.value.forEach { tierYaml ->
            tierYaml.load()
            Log.debug("Loading tier from ${tierYaml.fileName}...")
            val key = tierYaml.fileName.replace(".yml", "")

            // check if tier with same name already exists
            if (tierManager.contains(key)) {
                val message = "Not loading $key as there is already a tier with that name loaded"
                Log.info(message)
                loadingErrorManager.add(message)
                return@forEach
            }

            val tier =
                MythicTier.fromConfigurationSection(tierYaml, key, itemGroupManager, loadingErrorManager)
                    ?: return@forEach

            // check if tier already exists with same color combination
            val preExistingTierWithColors = tierManager.getWithColors(tier.displayColor, tier.identifierColor)
            val disableLegacyItemChecks =
                MythicDropsApi.mythicDrops.settingsManager.configSettings.options.isDisableLegacyItemChecks
            if (preExistingTierWithColors != null && !disableLegacyItemChecks) {
                val message =
                    ALREADY_LOADED_TIER_MSG.format(
                        key,
                        preExistingTierWithColors.name
                    )
                Log.info(message)
                loadingErrorManager.add(message)
                return@forEach
            }

            tierManager.add(tier)
        }

        Log.info("Loaded tiers: ${tierManager.get().joinToString(prefix = "[", postfix = "]") { it.name }}")
    }

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.reloadCustomItems",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override fun reloadCustomItems() {
        Log.debug("Loading custom items...")
        MythicDropsApi.mythicDrops.customItemManager.clear()
        customItemYAML.load()
        customItemYAML.getKeys(false).forEach {
            if (!customItemYAML.isConfigurationSection(it)) {
                return@forEach
            }
            val customItemCs = customItemYAML.getOrCreateSection(it)
            val customItem = MythicCustomItem.fromConfigurationSection(customItemCs, it)
            if (customItem.material.isAir) {
                val message =
                    "Error when loading custom item ($it): material is equivalent to AIR: ${customItem.material}"
                Log.debug(message)
                loadingErrorManager.add(message)
                return@forEach
            }
            MythicDropsApi.mythicDrops.customItemManager.add(customItem)
        }
        Log.info("Loaded custom items: ${MythicDropsApi.mythicDrops.customItemManager.get().size}")
    }

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.reloadNames",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override fun reloadNames() {
        NameMap.clear()

        Log.debug("Loading prefixes...")
        val prefixes = loadPrefixes()
        NameMap.putAll(prefixes)
        Log.info("Loaded prefixes: ${prefixes.values.flatten().size}")

        Log.debug("Loading suffixes...")
        val suffixes = loadSuffixes()
        NameMap.putAll(suffixes)
        Log.info("Loaded suffixes: ${suffixes.values.flatten().size}")

        Log.debug("Loading lore...")
        val lore = loadLore()
        NameMap.putAll(lore)
        Log.info("Loaded lore: ${lore.values.flatten().size}")

        Log.debug("Loading mob names...")
        val mobNames = loadMobNames()
        NameMap.putAll(mobNames)
        Log.info("Loaded mob names: ${mobNames.values.flatten().size}")
    }

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.reloadRepairCosts",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override fun reloadRepairCosts() {
        Log.debug("Loading repair costs...")
        repairItemManager.clear()
        repairCostsYAML.load()
        repairCostsYAML.getKeys(false).mapNotNull { key ->
            if (!repairCostsYAML.isConfigurationSection(key)) {
                return@mapNotNull null
            }

            val repairItemConfigurationSection = repairCostsYAML.getOrCreateSection(key)
            MythicRepairItem.fromConfigurationSection(repairItemConfigurationSection, key, loadingErrorManager)
        }.forEach { repairItemManager.add(it) }
        Log.info("Loaded repair items: ${repairItemManager.get().size}")
    }

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.reloadItemGroups",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override fun reloadItemGroups() {
        Log.debug("Loading item groups...")
        itemGroupManager.clear()
        itemGroupYAML.load()
        itemGroupYAML.getKeys(false).forEach { key ->
            if (!itemGroupYAML.isConfigurationSection(key)) {
                return@forEach
            }
            val itemGroupCs = itemGroupYAML.getOrCreateSection(key)
            itemGroupManager.add(MythicItemGroup.fromConfigurationSection(itemGroupCs, key))
        }
        Log.info("Loaded item groups: ${itemGroupManager.get().size}")
    }

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.reloadSocketGemCombiners",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override fun reloadSocketGemCombiners() {
        Log.debug("Loading socket gem combiners...")
        socketGemCombinerManager.clear()
        socketGemCombinersYAML.load()
        socketGemCombinersYAML.getKeys(false).forEach {
            if (!socketGemCombinersYAML.isConfigurationSection(it)) return@forEach
            try {
                socketGemCombinerManager.add(
                    MythicSocketGemCombiner.fromConfigurationSection(
                        socketGemCombinersYAML.getOrCreateSection(
                            it
                        ),
                        it
                    )
                )
            } catch (iae: IllegalArgumentException) {
                Log.error("Unable to load socket gem combiner with id=$it", iae)
                loadingErrorManager.add("Unable to load socket gem combiner with id=$it")
            }
        }
    }

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.saveSocketGemCombiners",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override fun saveSocketGemCombiners() {
        socketGemCombinersYAML
            .getKeys(false)
            .forEach { socketGemCombinersYAML[it] = null }
        socketGemCombinerManager.get().forEach {
            val key = it.uuid.toString()
            socketGemCombinersYAML["$key.world"] = it.location.world.name
            socketGemCombinersYAML["$key.x"] = it.location.x
            socketGemCombinersYAML["$key.y"] = it.location.y
            socketGemCombinersYAML["$key.z"] = it.location.z
        }
        socketGemCombinersYAML.save()
    }

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.reloadSocketGems",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override fun reloadSocketGems() {
        Log.debug("Loading socket types and socket extender types...")
        MythicDropsApi.mythicDrops.socketTypeManager.clear()
        MythicDropsApi.mythicDrops.socketExtenderTypeManager.clear()
        socketTypesYAML.load()
        MythicDropsApi.mythicDrops.socketTypeManager.addAll(
            MythicDropsApi.mythicDrops.socketTypeManager.loadFromConfiguration(
                socketTypesYAML
            )
        )
        MythicDropsApi.mythicDrops.socketExtenderTypeManager.addAll(
            MythicDropsApi.mythicDrops.socketExtenderTypeManager.loadFromConfiguration(
                socketTypesYAML
            )
        )

        Log.debug("Loading socket gems...")
        MythicDropsApi.mythicDrops.socketGemManager.clear()
        socketGemsYAML.load()
        MythicDropsApi.mythicDrops.socketGemManager.addAll(
            MythicDropsApi.mythicDrops.socketGemManager.loadFromConfiguration(socketGemsYAML)
        )
        auraTask?.cancel()
        val isStartAuraRunnable =
            MythicDropsApi.mythicDrops.socketGemManager.get().any { it.gemTriggerType == GemTriggerType.AURA }
        if (isStartAuraRunnable) {
            auraTask =
                AuraRunnable(MythicDebugManager, socketGemCacheManager).runTaskTimer(
                    this,
                    20,
                    20 * MythicDropsApi.mythicDrops.settingsManager.socketingSettings.options.auraRefreshInSeconds.toLong()
                )
            Log.info("Auras enabled")
        }
    }

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.reloadRelations",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override fun reloadRelations() {
        Log.debug("Loading relations...")
        relationManager.clear()
        relationYAML.load()
        relationYAML.getKeys(false).forEach {
            if (!relationYAML.isConfigurationSection(it)) return@forEach
            relationManager.add(MythicRelation.fromConfigurationSection(relationYAML.getOrCreateSection(it), it))
        }
        Log.info("Loaded relations: ${relationManager.get().size}")
    }

    // MOVE TO DIFFERENT CLASS IN 9.0.0
    @Deprecated(
        "Use MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.generateDebugBundle",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    override fun generateDebugBundle(): String {
        Log.info("Generating debug bundle...")
        val debugDirectory =
            dataFolder.resolve("debug").resolve(dateTimeFormatter.format(LocalDateTime.now(ZoneOffset.UTC)))
        if (!debugDirectory.mkdirs()) {
            Log.error("Unable to create debug directory")
            return debugDirectory.absolutePath
        }

        // Basic data first
        debugDirectory.resolve("basic.txt").writeText(
            """
            version: ${description.version}
            server package: ${Bukkit.getServer().javaClass.getPackage()}
            """.trimIndent()
        )
        debugDirectory.resolve("settings.txt")
            .writeText(MythicDropsApi.mythicDrops.settingsManager.prettyPrint())
        debugDirectory.resolve("customItems.txt")
            .writeText(MythicDropsApi.mythicDrops.customItemManager.prettyPrint())
        debugDirectory.resolve("socketGems.txt")
            .writeText(MythicDropsApi.mythicDrops.socketGemManager.prettyPrint())
        debugDirectory.resolve("tiers.txt")
            .writeText(MythicDropsApi.mythicDrops.tierManager.prettyPrint())
        debugDirectory.resolve("socketTypes.txt")
            .writeText(MythicDropsApi.mythicDrops.socketTypeManager.prettyPrint())
        debugDirectory.resolve("socketExtenderTypes.txt")
            .writeText(MythicDropsApi.mythicDrops.socketExtenderTypeManager.prettyPrint())

        Log.info("Wrote debug bundle to $debugDirectory")
        logger.info("Wrote debug bundle to $debugDirectory")
        return debugDirectory.absolutePath
    }

    private fun reloadStartupSettings() {
        startupYAML.load()
        MythicDropsApi.mythicDrops.settingsManager.loadStartupSettingsFromConfiguration(startupYAML)

        Log.clearLoggers()
        val logLevel =
            if (MythicDropsApi.mythicDrops.settingsManager.startupSettings.debug) {
                Log.Level.DEBUG
            } else {
                Log.Level.INFO
            }
        Log.addLogger(MythicDropsLogger(logLevel))
    }

    private fun writeResourceFiles() {
        val resources =
            listOf(
                "resources/lore/general.txt",
                "resources/lore/enchantments/damage_all.txt",
                "resources/lore/materials/diamond_sword.txt",
                "resources/lore/tiers/legendary.txt",
                "resources/lore/itemtypes/sword.txt",
                "resources/prefixes/general.txt",
                "resources/prefixes/enchantments/damage_all.txt",
                "resources/prefixes/materials/diamond_sword.txt",
                "resources/prefixes/tiers/legendary.txt",
                "resources/prefixes/itemtypes/sword.txt",
                "resources/suffixes/general.txt",
                "resources/suffixes/enchantments/damage_all.txt",
                "resources/suffixes/materials/diamond_sword.txt",
                "resources/suffixes/tiers/legendary.txt",
                "resources/suffixes/itemtypes/sword.txt",
                "resources/mobnames/general.txt"
            )
        resources.forEach { resource ->
            val actual = File(dataFolder, resource)
            val parentDirectory = actual.parentFile
            // we only write these resources if their parent folder doesn't exist, if we can make their parent
            // directory, and if the file doesn't already exist
            if (parentDirectory.exists() || !parentDirectory.exists() && !parentDirectory.mkdirs() || actual.exists()) {
                return@forEach
            }
            try {
                val contents = this.javaClass.classLoader?.getResource(resource)?.readText() ?: ""
                actual.writeText(contents)
            } catch (exception: Exception) {
                Log.error("Unable to write resource! resource=$resource", exception)
            }
        }
    }

    private fun writeConfigFilesAndMigrate() {
        // socketting.yml was renamed to socketing.yml
        File(dataFolder, "socketting.yml").renameTo(File(dataFolder, "socketting_RENAMED_TO_socketing.yml.backup"))

        // write all configuration files from the JAR if they don't exist in the data folder
        jarConfigMigrator.writeYamlFromResourcesIfNotExists("armor.yml")
        jarConfigMigrator.writeYamlFromResourcesIfNotExists("config.yml")
        jarConfigMigrator.writeYamlFromResourcesIfNotExists("creatureSpawning.yml")
        jarConfigMigrator.writeYamlFromResourcesIfNotExists("customItems.yml")
        jarConfigMigrator.writeYamlFromResourcesIfNotExists("language.yml")
        jarConfigMigrator.writeYamlFromResourcesIfNotExists("identifying.yml")
        jarConfigMigrator.writeYamlFromResourcesIfNotExists("itemGroups.yml")
        jarConfigMigrator.writeYamlFromResourcesIfNotExists("relation.yml")
        jarConfigMigrator.writeYamlFromResourcesIfNotExists("repairing.yml")
        jarConfigMigrator.writeYamlFromResourcesIfNotExists("repairCosts.yml")
        jarConfigMigrator.writeYamlFromResourcesIfNotExists("socketing.yml")
        jarConfigMigrator.writeYamlFromResourcesIfNotExists("socketGems.yml")
        jarConfigMigrator.writeYamlFromResourcesIfNotExists("socketTypes.yml")

        // use config-migrator to migrate all of the existing configs using the migrations defined
        // in src/main/resources/config/migrations
        jarConfigMigrator.migrate()
    }

    private fun setupCommands() {
        val commandManager = PaperCommandManager(this)
        @Suppress("DEPRECATION")
        commandManager.enableUnstableAPI("help")
        commandManager.registerDependency(CustomItemManager::class.java, MythicDropsApi.mythicDrops.customItemManager)
        commandManager.registerDependency(
            DropStrategyManager::class.java,
            MythicDropsApi.mythicDrops.dropStrategyManager
        )
        commandManager.registerDependency(HeadDatabaseAdapter::class.java, headDatabaseAdapter)
        commandManager.registerDependency(
            LoadingErrorManager::class.java,
            MythicDropsApi.mythicDrops.loadingErrorManager
        )
        commandManager.registerDependency(MythicDebugManager::class.java, MythicDebugManager)
        commandManager.registerDependency(MythicDrops::class.java, this)
        commandManager.registerDependency(SettingsManager::class.java, MythicDropsApi.mythicDrops.settingsManager)
        commandManager.registerDependency(TierManager::class.java, MythicDropsApi.mythicDrops.tierManager)
        commandManager.registerDependency(SocketTypeManager::class.java, MythicDropsApi.mythicDrops.socketTypeManager)
        commandManager.registerDependency(
            SocketExtenderTypeManager::class.java,
            MythicDropsApi.mythicDrops.socketExtenderTypeManager
        )
        registerContexts(commandManager)
        registerConditions(commandManager)
        registerCompletions(commandManager)
        registerCommands(commandManager)
    }

    private fun registerContexts(commandManager: PaperCommandManager) {
        commandManager.commandContexts.registerContext(CustomItem::class.java) { c ->
            val firstArg = c.popFirstArg() ?: throw InvalidCommandArgument()
            val customItem =
                MythicDropsApi.mythicDrops.customItemManager.getById(firstArg)
                    ?: MythicDropsApi.mythicDrops.customItemManager.getById(
                        firstArg.replace(
                            "_",
                            " "
                        )
                    )
            if (customItem == null && firstArg != "*") {
                throw InvalidCommandArgument("No custom item found by that name!")
            }
            customItem
        }
        commandManager
            .commandContexts
            .registerContext(
                Enchantment::class.java
            ) { c ->
                val firstArg = c.firstArg
                val enchantment = EnchantmentUtil.getByKeyOrName(firstArg) ?: throw InvalidCommandArgument()
                c.popFirstArg()
                enchantment
            }
        commandManager.commandContexts.registerContext(SocketGem::class.java) { c ->
            val firstArg = c.popFirstArg() ?: throw InvalidCommandArgument()
            val socketGem =
                MythicDropsApi.mythicDrops.socketGemManager.getById(firstArg)
                    ?: MythicDropsApi.mythicDrops.socketGemManager.getById(
                        firstArg.replace("_", " ")
                    )
            if (socketGem == null && firstArg != "*") {
                throw InvalidCommandArgument("No socket gem found by that name!")
            }
            socketGem
        }
        commandManager.commandContexts.registerContext(Tier::class.java) { c ->
            val firstArg = c.popFirstArg() ?: throw InvalidCommandArgument()
            val tier = tierManager.getByName(firstArg) ?: tierManager.getByName(firstArg.replace("_", " "))
            if (tier == null && firstArg != "*") {
                throw InvalidCommandArgument("No tier found by that name!")
            }
            tier
        }
        commandManager.commandContexts.registerContext(ItemGroup::class.java) { c ->
            val firstArg = c.popFirstArg() ?: throw InvalidCommandArgument()
            val itemGroup = itemGroupManager.getById(firstArg) ?: itemGroupManager.getById(firstArg.replace("_", " "))
            if (itemGroup == null && firstArg != "*") {
                throw InvalidCommandArgument("No tier found by that name!")
            }
            itemGroup
        }
    }

    private fun registerConditions(commandManager: PaperCommandManager) {
        commandManager
            .commandConditions
            .addCondition(
                Int::class.java,
                "limits"
            ) { c, _, value ->
                if (value != null) {
                    if (c.hasConfig("min") && c.getConfigValue("min", 0) > value) {
                        throw ConditionFailedException(
                            "Min value must be " + c.getConfigValue("min", 0)!!
                        )
                    }
                    if (c.hasConfig("max") && c.getConfigValue("max", 3) < value) {
                        throw ConditionFailedException(
                            "Max value must be " + c.getConfigValue("max", 3)!!
                        )
                    }
                }
            }
    }

    private fun registerCompletions(commandManager: PaperCommandManager) {
        commandManager
            .commandCompletions
            .registerCompletion(
                "enchantments"
            ) { _ ->
                Enchantment.values().map {
                    try {
                        it.key.toString()
                    } catch (ex: Throwable) {
                        @Suppress("DEPRECATION")
                        it.name
                    }
                }
            }
        commandManager.commandCompletions.registerCompletion("customItems") { _ ->
            listOf("*") + MythicDropsApi.mythicDrops.customItemManager.get().map { it.name.replace(" ", "_") }
        }
        commandManager.commandCompletions.registerCompletion("socketGems") { _ ->
            listOf("*") + MythicDropsApi.mythicDrops.socketGemManager.get().map { it.name.replace(" ", "_") }
        }
        commandManager.commandCompletions.registerCompletion("tiers") { _ ->
            listOf("*") + tierManager.get().map { it.name.replace(" ", "_") }
        }
        commandManager.commandCompletions.registerCompletion("itemGroups") { _ ->
            listOf("*") + itemGroupManager.get().map { it.name.replace(" ", "_") }
        }
    }

    private fun registerCommands(commandManager: PaperCommandManager) {
        commandManager.registerCommand(CombinerCommands())
        commandManager.registerCommand(CustomCreateCommand())
        commandManager.registerCommand(CustomItemsCommand())
        commandManager.registerCommand(DebugCommand())
        commandManager.registerCommand(DropCommands())
        commandManager.registerCommand(DropRatesCommand())
        commandManager.registerCommand(GiveCommands())
        commandManager.registerCommand(HelpCommand())
        commandManager.registerCommand(ItemGroupsCommand())
        commandManager.registerCommand(ModifyCommands())
        commandManager.registerCommand(ReloadCommand())
        commandManager.registerCommand(SocketGemsCommand())
        commandManager.registerCommand(SpawnCommands())
        commandManager.registerCommand(TiersCommand())
    }

    private fun loadPrefixes(): Map<out String, List<String>> {
        val prefixes = mutableMapOf<String, List<String>>()
        val dataFolderAsPath = dataFolder.toPath()

        Glob.from("resources/prefixes/general.txt").iterate(dataFolderAsPath).forEach {
            prefixes[NameType.GENERAL_PREFIX.format] = it.toFile().readLines()
        }

        Glob.from("resources/prefixes/tiers/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key = "${NameType.TIER_PREFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            prefixes[key] = file.readLines()
        }

        Glob.from("resources/prefixes/materials/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.MATERIAL_PREFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            prefixes[key] = file.readLines()
        }

        Glob.from("resources/prefixes/enchantments/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.ENCHANTMENT_PREFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            prefixes[key] = file.readLines()
        }

        Glob.from("resources/prefixes/itemtypes/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.ITEMTYPE_PREFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            prefixes[key] = file.readLines()
        }

        return prefixes
    }

    private fun loadSuffixes(): Map<out String, List<String>> {
        val suffixes = mutableMapOf<String, List<String>>()
        val dataFolderAsPath = dataFolder.toPath()

        Glob.from("resources/suffixes/general.txt").iterate(dataFolderAsPath).forEach {
            suffixes[NameType.GENERAL_SUFFIX.format] = it.toFile().readLines()
        }

        Glob.from("resources/suffixes/tiers/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key = "${NameType.TIER_SUFFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            suffixes[key] = file.readLines()
        }

        Glob.from("resources/suffixes/materials/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.MATERIAL_SUFFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            suffixes[key] = file.readLines()
        }

        Glob.from("resources/suffixes/enchantments/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.ENCHANTMENT_SUFFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            suffixes[key] = file.readLines()
        }

        Glob.from("resources/suffixes/itemtypes/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.ITEMTYPE_SUFFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            suffixes[key] = file.readLines()
        }

        return suffixes
    }

    private fun loadLore(): Map<out String, List<String>> {
        val lore = mutableMapOf<String, List<String>>()
        val dataFolderAsPath = dataFolder.toPath()

        Glob.from("resources/lore/general.txt").iterate(dataFolderAsPath).forEach {
            lore[NameType.GENERAL_LORE.format] = it.toFile().readLines()
        }

        Glob.from("resources/lore/tiers/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key = "${NameType.TIER_LORE.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            lore[key] = file.readLines()
        }

        Glob.from("resources/lore/materials/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key = "${NameType.MATERIAL_LORE.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            lore[key] = file.readLines()
        }

        Glob.from("resources/lore/enchantments/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.ENCHANTMENT_LORE.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            lore[key] = file.readLines()
        }

        Glob.from("resources/lore/itemtypes/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key = "${NameType.ITEMTYPE_LORE.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            lore[key] = file.readLines()
        }

        return lore
    }

    private fun loadMobNames(): Map<out String, List<String>> {
        val mobNames = mutableMapOf<String, List<String>>()
        val dataFolderAsPath = dataFolder.toPath()

        Glob.from("resources/mobnames/general.txt").iterate(dataFolderAsPath).forEach {
            mobNames[NameType.GENERAL_MOB_NAME.format] = it.toFile().readLines()
        }

        Glob.from("resources/mobnames/*.txt", "!resources/mobnames/general.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.SPECIFIC_MOB_NAME.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            mobNames[key] = file.readLines()
        }

        return mobNames
    }
}
