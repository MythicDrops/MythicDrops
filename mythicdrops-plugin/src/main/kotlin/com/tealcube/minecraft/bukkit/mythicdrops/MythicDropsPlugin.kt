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

import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.arguments.standard.StringArgument
import cloud.commandframework.bukkit.BukkitCaptionRegistry
import cloud.commandframework.bukkit.CloudBukkitCapabilities
import cloud.commandframework.meta.CommandMeta
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler
import cloud.commandframework.minecraft.extras.MinecraftHelp
import cloud.commandframework.paper.PaperCommandManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.builders.DropBuilder
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.strategies.DropStrategy
import com.tealcube.minecraft.bukkit.mythicdrops.api.worldguard.WorldGuardFlags
import com.tealcube.minecraft.bukkit.mythicdrops.commands.cloud.arguments.MythicDropsParserRegistryEnhancer
import com.tealcube.minecraft.bukkit.mythicdrops.commands.cloud.captions.MythicDropsCaptionRegistryEnhancer
import com.tealcube.minecraft.bukkit.mythicdrops.commands.cloud.commands.CloudCommand
import com.tealcube.minecraft.bukkit.mythicdrops.hdb.HeadDatabaseAdapter
import com.tealcube.minecraft.bukkit.mythicdrops.io.ResourceWriter
import com.tealcube.minecraft.bukkit.mythicdrops.koin.MythicKoinComponent
import com.tealcube.minecraft.bukkit.mythicdrops.koin.MythicKoinContext
import com.tealcube.minecraft.bukkit.mythicdrops.koin.configFilesModule
import com.tealcube.minecraft.bukkit.mythicdrops.koin.inject
import com.tealcube.minecraft.bukkit.mythicdrops.koin.integrationModule
import com.tealcube.minecraft.bukkit.mythicdrops.koin.pluginModule
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory
import com.tealcube.minecraft.bukkit.mythicdrops.logging.MythicDropsLoggingFormatter
import com.tealcube.minecraft.bukkit.mythicdrops.worldguard.registerFlags
import dev.mythicdrops.spigot.FeatureToggledListener
import io.pixeloutlaw.kindling.Log
import io.pixeloutlaw.minecraft.spigot.config.ConfigMigratorSerialization
import io.pixeloutlaw.minecraft.spigot.config.migration.migrators.ClasspathConfigMigrator
import io.pixeloutlaw.minecraft.spigot.mythicdrops.scheduleSyncDelayedTask
import io.pixeloutlaw.minecraft.spigot.plumbing.api.MinecraftVersions
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.koin.dsl.koinApplication
import org.koin.ksp.generated.module
import java.util.Locale
import java.util.logging.FileHandler
import java.util.logging.Level

@Suppress("detekt.LargeClass", "detekt.TooManyFunctions")
class MythicDropsPlugin : JavaPlugin(), MythicKoinComponent {
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
            MythicDropsApi.mythicDrops.productionLine.tieredItemFactory.getNewDropBuilder()
    }

    private val mythicDrops: MythicDrops by inject()
    private val classpathConfigMigrator: ClasspathConfigMigrator by inject()
    private val headDatabaseAdapter: HeadDatabaseAdapter by inject()
    private val bukkitCommandManager: PaperCommandManager<CommandSender> by inject()
    private val annotationParser: AnnotationParser<CommandSender> by inject()
    private val resourceWriter: ResourceWriter by inject()

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
            Log.error(
                "MythicDrops only supports Minecraft 1.17+ due to the Java 17 changes - disabling MythicDrops!"
            )
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
                        "%s/%s.log".format(
                            Locale.ROOT,
                            dataFolder.absolutePath,
                            this@MythicDropsPlugin.name.lowercase(Locale.ROOT)
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
        val koinApp = koinApplication {
            modules(pluginModule(this@MythicDropsPlugin), integrationModule, configFilesModule, MythicDropsModule().module)
        }

        MythicKoinContext.koinApp = koinApp
        koinApp.createEagerInstances()
        mythicDrops.reloadStartupSettings()

        ConfigMigratorSerialization.registerAll()

        mythicDrops.customEnchantmentRegistry.registerEnchantments()

        Log.info("Writing configuration files if necessary...")
        resourceWriter.writeConfigFiles()

        Log.info("Running pre-load configuration migrations...")
        classpathConfigMigrator.preMigrate()

        Log.info("Writing resources files if necessary...")
        resourceWriter.writeResourceFiles()

        Log.info("Registering default drop strategies...")
        koinApp.koin.getAll<DropStrategy>().forEach { dropStrategy ->
            Log.debug("Registering drop strategy: ${dropStrategy.name}")
            mythicDrops.dropStrategyManager.add(dropStrategy)
        }

        Log.info("Loading all settings and everything else...")
        mythicDrops.reloadAll()

        Log.info("Performing post-load configuration migrations...")
        classpathConfigMigrator.postMigrate()

        Log.info("Reloading all settings and everything else...")
        mythicDrops.reloadAll()

        Log.info("Registering enabled feature-toggled event listeners...")
        koinApp.koin.getAll<Listener>().filter { it is FeatureToggledListener && it.isEnabled() }.forEach { listener ->
            Log.debug("Registering listener: ${listener.javaClass.name}")
            Bukkit.getPluginManager().registerEvents(listener, this)
        }

        Log.info("Registering non-feature-toggled event listeners...")
        koinApp.koin.getAll<Listener>().filter { it !is FeatureToggledListener }.forEach { listener ->
            Log.debug("Registering listener: ${listener.javaClass.name}")
            Bukkit.getPluginManager().registerEvents(listener, this)
        }

        Log.info("Setting up commands...")
        setupCommands()

        // setup HeadDatabase support a tick after the plugin is enabled to ensure we're not loaded before it
        // this is really only necessary because I don't have a copy of the plugin and can't test it myself
        scheduleSyncDelayedTask {
            headDatabaseAdapter.register(this)
        }

        // SocketGemCombiners need to be loaded after the worlds have been loaded, so run a delayed
        // task:
        scheduleSyncDelayedTask {
            mythicDrops.reloadSocketGemCombiners()
        }

        MythicDropsApi.mythicDrops = mythicDrops
        Log.info("v${description.version} enabled")
    }

    override fun onDisable() {
        HandlerList.unregisterAll(this)
        Bukkit.getScheduler().cancelTasks(this)
        ConfigMigratorSerialization.unregisterAll()

        mythicDrops.socketGemCacheManager.clear()
        mythicDrops.socketGemManager.clear()
        mythicDrops.itemGroupManager.clear()
        mythicDrops.socketGemCombinerManager.clear()
        mythicDrops.repairItemManager.clear()
        mythicDrops.customItemManager.clear()
        mythicDrops.relationManager.clear()
        mythicDrops.tierManager.clear()
        mythicDrops.loadingErrorManager.clear()

        MythicKoinContext.koinApp?.close()
        MythicKoinContext.koinApp = null
    }

    private fun setupCommands() {
        MythicDropsParserRegistryEnhancer.registerArguments(bukkitCommandManager.parserRegistry())
        MythicDropsCaptionRegistryEnhancer.enhanceCaptions(bukkitCommandManager.captionRegistry() as BukkitCaptionRegistry)
        if (bukkitCommandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            bukkitCommandManager.registerAsynchronousCompletions()
        }
        getKoin().getAll<CloudCommand>().forEach {
            annotationParser.parse(it)
        }

        val bukkitAudiences = getKoin().get<BukkitAudiences>()
        MinecraftExceptionHandler<CommandSender>()
            .withArgumentParsingHandler()
            .withInvalidSenderHandler()
            .withInvalidSyntaxHandler()
            .withNoPermissionHandler()
            .withCommandExecutionHandler()
            .apply(bukkitCommandManager, bukkitAudiences::sender)

        val builder = bukkitCommandManager.commandBuilder("mythicdrops", "md")
        val minecraftHelp =
            MinecraftHelp("/mythicdrops help", bukkitAudiences::sender, bukkitCommandManager)

        bukkitCommandManager.command(
            builder.meta(CommandMeta.DESCRIPTION, "The main command").handler { context ->
                minecraftHelp.queryCommands("", context.sender)
            }
        )
        bukkitCommandManager.command(
            builder.literal("help").argument(StringArgument.optional("query", StringArgument.StringMode.GREEDY))
                .handler { context ->
                    minecraftHelp.queryCommands(
                        context.getOptional<String>("query").map { "md $it" }.orElse("md"),
                        context.sender
                    )
                }
        )
    }
}
