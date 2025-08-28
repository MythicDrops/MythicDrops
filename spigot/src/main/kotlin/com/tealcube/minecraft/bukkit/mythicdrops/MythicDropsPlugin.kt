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
import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroup
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.builders.DropBuilder
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.strategies.DropStrategyManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketExtenderTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.worldguard.WorldGuardFlags
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
import com.tealcube.minecraft.bukkit.mythicdrops.config.Resources
import com.tealcube.minecraft.bukkit.mythicdrops.debug.MythicDebugManager
import com.tealcube.minecraft.bukkit.mythicdrops.hdb.HeadDatabaseAdapter
import com.tealcube.minecraft.bukkit.mythicdrops.koin.MythicKoinComponent
import com.tealcube.minecraft.bukkit.mythicdrops.koin.MythicKoinContext
import com.tealcube.minecraft.bukkit.mythicdrops.koin.inject
import com.tealcube.minecraft.bukkit.mythicdrops.koin.integrationsModule
import com.tealcube.minecraft.bukkit.mythicdrops.koin.pluginModule
import com.tealcube.minecraft.bukkit.mythicdrops.loading.FeatureFlagged
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory
import com.tealcube.minecraft.bukkit.mythicdrops.logging.MythicDropsLoggingFormatter
import com.tealcube.minecraft.bukkit.mythicdrops.utils.EnchantmentUtil
import com.tealcube.minecraft.bukkit.mythicdrops.worldguard.registerFlags
import dev.mythicdrops.MythicDropsDevModule
import io.pixeloutlaw.kindling.Log
import io.pixeloutlaw.minecraft.spigot.PixelOutlawModule
import io.pixeloutlaw.minecraft.spigot.config.ConfigMigratorSerialization
import io.pixeloutlaw.minecraft.spigot.config.migration.migrators.JarConfigMigrator
import io.pixeloutlaw.minecraft.spigot.mythicdrops.scheduleSyncDelayedTask
import org.bukkit.Bukkit
import org.bukkit.Registry
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication
import org.koin.ksp.generated.module
import java.util.Locale
import java.util.logging.FileHandler
import java.util.logging.Level

class MythicDropsPlugin :
    JavaPlugin(),
    MythicKoinComponent {
    companion object {
        private lateinit var instance: MythicDropsPlugin

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
        fun getNewDropBuilder(): DropBuilder =
            MythicDropsApi.mythicDrops.productionLine.tieredItemFactory
                .getNewDropBuilder()
    }

    private val jarConfigMigrator: JarConfigMigrator by inject()
    private val headDatabaseAdapter: HeadDatabaseAdapter by inject()

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

        instance = this
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
                modules(
                    pluginModule(this@MythicDropsPlugin),
                    integrationsModule,
                    PixelOutlawModule().module,
                    MythicDropsModule().module,
                    MythicDropsDevModule().module
                )
            }
        val mythicDrops = koinApp.koin.get<MythicDrops>()
        MythicKoinContext.koinApp = koinApp
        MythicDropsApi.mythicDrops = mythicDrops

        mythicDrops.configLoader.reloadStartupSettings()

        ConfigMigratorSerialization.registerAll()

        Log.info("Loading configuration files...")
        writeConfigFilesAndMigrate()

        Log.info("Writing resources files if necessary...")
        koinApp.koin.get<Resources>().writeResourceFiles()

        Log.info("Loading all settings and everything else...")
        mythicDrops.configLoader.reloadSettings()
        mythicDrops.configLoader.reloadItemGroups()
        mythicDrops.configLoader.reloadTiers()
        mythicDrops.configLoader.reloadNames()
        mythicDrops.configLoader.reloadCustomItems()
        mythicDrops.configLoader.reloadRepairCosts()
        mythicDrops.configLoader.reloadSocketGems()
        mythicDrops.configLoader.reloadRelations()

        // SocketGemCombiners need to be loaded after the worlds have been loaded, so run a delayed
        // task:
        server.scheduler.runTask(this, Runnable { mythicDrops.configLoader.reloadSocketGemCombiners() })

        Log.info("Registering event listeners...")
        val listeners = koinApp.koin.getAll<Listener>()
        Log.info("Total possible listeners: ${listeners.size}")
        listeners.filter { it !is FeatureFlagged || it.isEnabled() }.forEach {
            Log.debug("Registering event listener: ${it::class.simpleName}")
            Bukkit.getPluginManager().registerEvents(it, this)
        }

        Log.info("Setting up commands...")
        setupCommands(koinApp)

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

    private fun writeConfigFilesAndMigrate() {
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

        // use config-migrator to migrate all the existing configs using the migrations defined
        // in src/main/resources/config/migrations
        jarConfigMigrator.migrate()
    }

    private fun setupCommands(koinApp: KoinApplication) {
        val commandManager = PaperCommandManager(this)
        commandManager.enableUnstableAPI("help")
        commandManager.registerDependency(CustomItemManager::class.java, koinApp.koin.get())
        commandManager.registerDependency(
            DropStrategyManager::class.java,
            koinApp.koin.get()
        )
        commandManager.registerDependency(HeadDatabaseAdapter::class.java, koinApp.koin.get())
        commandManager.registerDependency(
            LoadingErrorManager::class.java,
            koinApp.koin.get()
        )
        commandManager.registerDependency(MythicDebugManager::class.java, koinApp.koin.get())
        commandManager.registerDependency(MythicDrops::class.java, koinApp.koin.get())
        commandManager.registerDependency(SettingsManager::class.java, koinApp.koin.get())
        commandManager.registerDependency(TierManager::class.java, koinApp.koin.get())
        commandManager.registerDependency(SocketTypeManager::class.java, koinApp.koin.get())
        commandManager.registerDependency(
            SocketExtenderTypeManager::class.java,
            koinApp.koin.get()
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
            val tier =
                MythicDropsApi.mythicDrops.tierManager.getByName(firstArg)
                    ?: MythicDropsApi.mythicDrops.tierManager.getByName(firstArg.replace("_", " "))
            if (tier == null && firstArg != "*") {
                throw InvalidCommandArgument("No tier found by that name!")
            }
            tier
        }
        commandManager.commandContexts.registerContext(ItemGroup::class.java) { c ->
            val firstArg = c.popFirstArg() ?: throw InvalidCommandArgument()
            val itemGroup =
                MythicDropsApi.mythicDrops.itemGroupManager.getById(firstArg)
                    ?: MythicDropsApi.mythicDrops.itemGroupManager.getById(firstArg.replace("_", " "))
            if (itemGroup == null && firstArg != "*") {
                throw InvalidCommandArgument("No item group found by that name!")
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
                Registry.ENCHANTMENT.map {
                    try {
                        it.keyOrThrow.toString()
                    } catch (_: Throwable) {
                        @Suppress("DEPRECATION")
                        it.name
                    }
                }
            }
        commandManager.commandCompletions.registerCompletion("customItems") { _ ->
            listOf("*") +
                MythicDropsApi.mythicDrops.customItemManager
                    .get()
                    .map { it.name.replace(" ", "_") }
        }
        commandManager.commandCompletions.registerCompletion("socketGems") { _ ->
            listOf("*") +
                MythicDropsApi.mythicDrops.socketGemManager
                    .get()
                    .map { it.name.replace(" ", "_") }
        }
        commandManager.commandCompletions.registerCompletion("tiers") { _ ->
            listOf("*") +
                MythicDropsApi.mythicDrops.tierManager
                    .get()
                    .map { it.name.replace(" ", "_") }
        }
        commandManager.commandCompletions.registerCompletion("itemGroups") { _ ->
            listOf("*") +
                MythicDropsApi.mythicDrops.itemGroupManager
                    .get()
                    .map { it.name.replace(" ", "_") }
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
}
