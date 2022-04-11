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

import cloud.commandframework.CommandManager
import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.arguments.parser.StandardParameters
import cloud.commandframework.bukkit.BukkitCommandManager
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.meta.CommandMeta
import cloud.commandframework.paper.PaperCommandManager
import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin
import com.tealcube.minecraft.bukkit.mythicdrops.config.ConfigQualifiers
import com.tealcube.minecraft.bukkit.mythicdrops.hdb.HeadDatabaseAdapters
import io.pixeloutlaw.minecraft.spigot.config.VersionedFileAwareYamlConfiguration
import net.kyori.adventure.platform.AudienceProvider
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.onClose
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.binds
import org.koin.dsl.module
import java.io.File
import java.util.function.Function

internal fun pluginModule(plugin: MythicDropsPlugin): Module {
    return module {
        single { plugin } binds arrayOf(JavaPlugin::class, Plugin::class)
    }
}

internal val integrationModule = module {
    single { BukkitAudiences.create(get()) }.withOptions {
        bind<AudienceProvider>()
        onClose {
            it?.close()
        }
    }
    single { HeadDatabaseAdapters.determineAdapter() }
    single {
        PaperCommandManager(
            get(),
            CommandExecutionCoordinator.simpleCoordinator(),
            Function.identity(),
            Function.identity()
        )
    }.withOptions {
        bind<BukkitCommandManager<CommandSender>>()
        bind<CommandManager<CommandSender>>()
    }
    single {
        AnnotationParser(get(), CommandSender::class.java) {
            CommandMeta.simple().with(CommandMeta.DESCRIPTION, it.get(StandardParameters.DESCRIPTION, "No description"))
                .build()
        }
    }
}

internal val configFilesModule = module {
    single(ConfigQualifiers.armor) { VersionedFileAwareYamlConfiguration(File(get<Plugin>().dataFolder, "armor.yml")) }
    single(ConfigQualifiers.config) {
        VersionedFileAwareYamlConfiguration(
            File(
                get<Plugin>().dataFolder,
                "config.yml"
            )
        )
    }
    single(ConfigQualifiers.creatureSpawning) {
        VersionedFileAwareYamlConfiguration(
            File(
                get<Plugin>().dataFolder,
                "creatureSpawning.yml"
            )
        )
    }
    single(ConfigQualifiers.customItems) {
        VersionedFileAwareYamlConfiguration(
            File(
                get<Plugin>().dataFolder,
                "customItems.yml"
            )
        )
    }
    single(ConfigQualifiers.identifying) {
        VersionedFileAwareYamlConfiguration(
            File(
                get<Plugin>().dataFolder,
                "identifying.yml"
            )
        )
    }
    single(ConfigQualifiers.itemGroups) {
        VersionedFileAwareYamlConfiguration(
            File(
                get<Plugin>().dataFolder,
                "itemGroups.yml"
            )
        )
    }
    single(ConfigQualifiers.language) {
        VersionedFileAwareYamlConfiguration(
            File(
                get<Plugin>().dataFolder,
                "language.yml"
            )
        )
    }
    single(ConfigQualifiers.relation) {
        VersionedFileAwareYamlConfiguration(
            File(
                get<Plugin>().dataFolder,
                "relation.yml"
            )
        )
    }
    single(ConfigQualifiers.repairCosts) {
        VersionedFileAwareYamlConfiguration(
            File(
                get<Plugin>().dataFolder,
                "repairCosts.yml"
            )
        )
    }
    single(ConfigQualifiers.repairing) {
        VersionedFileAwareYamlConfiguration(
            File(
                get<Plugin>().dataFolder,
                "repairing.yml"
            )
        )
    }
    single(ConfigQualifiers.socketGemCombiners) {
        VersionedFileAwareYamlConfiguration(
            File(
                get<Plugin>().dataFolder,
                "socketGemCombiners.yml"
            )
        )
    }
    single(ConfigQualifiers.socketGems) {
        VersionedFileAwareYamlConfiguration(
            File(
                get<Plugin>().dataFolder,
                "socketGems.yml"
            )
        )
    }
    single(ConfigQualifiers.socketing) {
        VersionedFileAwareYamlConfiguration(
            File(
                get<Plugin>().dataFolder,
                "socketing.yml"
            )
        )
    }
    single(ConfigQualifiers.startup) {
        VersionedFileAwareYamlConfiguration(
            File(
                get<Plugin>().dataFolder,
                "startup.yml"
            )
        )
    }
    single(ConfigQualifiers.tiers) {
        VersionedFileAwareYamlConfiguration(
            File(
                get<Plugin>().dataFolder,
                "startup.yml"
            )
        )
    }
}
