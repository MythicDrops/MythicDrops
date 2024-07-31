package com.tealcube.minecraft.bukkit.mythicdrops

import com.tealcube.minecraft.bukkit.mythicdrops.config.ConfigQualifiers
import io.pixeloutlaw.minecraft.spigot.config.VersionedFileAwareYamlConfiguration
import org.bukkit.plugin.Plugin
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@ComponentScan
@Module
internal class MythicDropsModule {
    @Single
    @Named(ConfigQualifiers.ARMOR)
    fun armorYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.ARMOR}.yml"))

    @Single
    @Named(ConfigQualifiers.CONFIG)
    fun configYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.CONFIG}.yml"))

    @Single
    @Named(ConfigQualifiers.CREATURE_SPAWNING)
    fun creatureSpawningYAML(plugin: Plugin) =
        VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.CREATURE_SPAWNING}.yml"))

    @Single
    @Named(ConfigQualifiers.CUSTOM_ITEMS)
    fun customItemsYAML(plugin: Plugin) =
        VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.CUSTOM_ITEMS}.yml"))

    @Single
    @Named(ConfigQualifiers.IDENTIFYING)
    fun identifyingYAML(plugin: Plugin) =
        VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.IDENTIFYING}.yml"))

    @Single
    @Named(ConfigQualifiers.ITEM_GROUPS)
    fun itemGroupsYAML(plugin: Plugin) =
        VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.ITEM_GROUPS}.yml"))

    @Single
    @Named(ConfigQualifiers.LANGUAGE)
    fun languageYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.LANGUAGE}.yml"))

    @Single
    @Named(ConfigQualifiers.RELATION)
    fun relationYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.RELATION}.yml"))

    @Single
    @Named(ConfigQualifiers.REPAIR_COSTS)
    fun repairCostsYAML(plugin: Plugin) =
        VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.REPAIR_COSTS}.yml"))

    @Single
    @Named(ConfigQualifiers.REPAIRING)
    fun repairingYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.REPAIRING}.yml"))

    @Single
    @Named(ConfigQualifiers.SOCKET_GEM_COMBINERS)
    fun socketGemCombinersYAML(plugin: Plugin) =
        VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.SOCKET_GEM_COMBINERS}.yml"))

    @Single
    @Named(ConfigQualifiers.SOCKET_GEMS)
    fun socketGemsYAML(plugin: Plugin) =
        VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.SOCKET_GEMS}.yml"))

    @Single
    @Named(ConfigQualifiers.SOCKET_TYPES)
    fun socketTypesYAML(plugin: Plugin) =
        VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.SOCKET_TYPES}.yml"))

    @Single
    @Named(ConfigQualifiers.SOCKETING)
    fun socketingYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.SOCKETING}.yml"))

    @Single
    @Named(ConfigQualifiers.STARTUP)
    fun startupYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.STARTUP}.yml"))
}
