package com.tealcube.minecraft.bukkit.mythicdrops.config

import com.github.shyiko.klob.Glob
import com.tealcube.minecraft.bukkit.mythicdrops.api.config.ConfigManager
import dev.mythicdrops.spigot.configuration.VersionedFileAwareConfiguration
import io.pixeloutlaw.minecraft.spigot.config.VersionedFileAwareYamlConfiguration
import org.bukkit.plugin.Plugin
import org.koin.core.annotation.Single
import java.io.File

@Single
internal class MythicConfigManager(private val plugin: Plugin) : ConfigManager {
    override val armorYAML: VersionedFileAwareConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(plugin.dataFolder, "armor.yml"))
    }
    override val configYAML: VersionedFileAwareConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(plugin.dataFolder, "config.yml"))
    }
    override val creatureSpawningYAML: VersionedFileAwareConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(plugin.dataFolder, "creatureSpawning.yml"))
    }
    override val customItemYAML: VersionedFileAwareConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(plugin.dataFolder, "customItems.yml"))
    }
    override val itemGroupYAML: VersionedFileAwareConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(plugin.dataFolder, "itemGroups.yml"))
    }
    override val languageYAML: VersionedFileAwareConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(plugin.dataFolder, "language.yml"))
    }
    override val socketGemsYAML: VersionedFileAwareConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(plugin.dataFolder, "socketGems.yml"))
    }
    override val socketingYAML: VersionedFileAwareConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(plugin.dataFolder, "socketing.yml"))
    }
    override val repairingYAML: VersionedFileAwareConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(plugin.dataFolder, "repairing.yml"))
    }
    override val repairCostsYAML: VersionedFileAwareConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(plugin.dataFolder, "repairCosts.yml"))
    }
    override val identifyingYAML: VersionedFileAwareConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(plugin.dataFolder, "identifying.yml"))
    }
    override val relationYAML: VersionedFileAwareConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(plugin.dataFolder, "relation.yml"))
    }
    override val socketGemCombinersYAML: VersionedFileAwareConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(plugin.dataFolder, "socketGemCombiners.yml"))
    }
    override val startupYAML: VersionedFileAwareConfiguration by lazy {
        VersionedFileAwareYamlConfiguration(File(plugin.dataFolder, "startup.yml"))
    }
    override val tierYAMLs: List<VersionedFileAwareConfiguration> by lazy {
        Glob.from("tiers/**/*.yml").iterate(plugin.dataFolder.toPath()).asSequence().toList()
            .map { VersionedFileAwareYamlConfiguration(it.toFile()) }
    }
}
