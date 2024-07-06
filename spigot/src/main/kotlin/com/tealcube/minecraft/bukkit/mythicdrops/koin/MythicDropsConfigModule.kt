package com.tealcube.minecraft.bukkit.mythicdrops.koin

import com.tealcube.minecraft.bukkit.mythicdrops.config.ConfigQualifiers
import io.pixeloutlaw.minecraft.spigot.config.VersionedFileAwareYamlConfiguration
import org.bukkit.plugin.Plugin
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named

@Module
internal class MythicDropsConfigModule {
    @Named(ConfigQualifiers.ARMOR)
    fun armorYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.ARMOR}.yml"))

    @Named(ConfigQualifiers.CONFIG)
    fun configYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.CONFIG}.yml"))

    @Named(ConfigQualifiers.CREATURE_SPAWNING)
    fun creatureSpawningYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.CREATURE_SPAWNING}.yml"))

    @Named(ConfigQualifiers.CUSTOM_ITEMS)
    fun customItemsYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.CUSTOM_ITEMS}.yml"))

    @Named(ConfigQualifiers.IDENTIFYING)
    fun identifyingYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.IDENTIFYING}.yml"))

    @Named(ConfigQualifiers.ITEM_GROUPS)
    fun itemGroupsYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.ITEM_GROUPS}.yml"))

    @Named(ConfigQualifiers.LANGUAGE)
    fun languageYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.LANGUAGE}.yml"))

    @Named(ConfigQualifiers.RELATION)
    fun relationYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.RELATION}.yml"))

    @Named(ConfigQualifiers.REPAIR_COSTS)
    fun repairCostsYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.REPAIR_COSTS}.yml"))

    @Named(ConfigQualifiers.REPAIRING)
    fun repairingYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.REPAIRING}.yml"))

    @Named(ConfigQualifiers.SOCKET_GEM_COMBINERS)
    fun socketGemCombinersYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.SOCKET_GEM_COMBINERS}.yml"))

    @Named(ConfigQualifiers.SOCKET_GEMS)
    fun socketGemsYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.SOCKET_GEMS}.yml"))

    @Named(ConfigQualifiers.SOCKET_TYPES)
    fun socketTypesYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.SOCKET_TYPES}.yml"))

    @Named(ConfigQualifiers.SOCKETING)
    fun socketingYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.SOCKETING}.yml"))

    @Named(ConfigQualifiers.STARTUP)
    fun startupYAML(plugin: Plugin) = VersionedFileAwareYamlConfiguration(plugin.dataFolder.resolve("${ConfigQualifiers.STARTUP}.yml"))
}
