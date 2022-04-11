package com.tealcube.minecraft.bukkit.mythicdrops.api.config

import dev.mythicdrops.spigot.configuration.VersionedFileAwareConfiguration

interface ConfigManager {
    val armorYAML: VersionedFileAwareConfiguration
    val configYAML: VersionedFileAwareConfiguration
    val creatureSpawningYAML: VersionedFileAwareConfiguration
    val customItemYAML: VersionedFileAwareConfiguration
    val itemGroupYAML: VersionedFileAwareConfiguration
    val languageYAML: VersionedFileAwareConfiguration
    val socketGemsYAML: VersionedFileAwareConfiguration
    val socketingYAML: VersionedFileAwareConfiguration
    val repairingYAML: VersionedFileAwareConfiguration
    val repairCostsYAML: VersionedFileAwareConfiguration
    val identifyingYAML: VersionedFileAwareConfiguration
    val relationYAML: VersionedFileAwareConfiguration
    val socketGemCombinersYAML: VersionedFileAwareConfiguration
    val startupYAML: VersionedFileAwareConfiguration
    val tierYAMLs: List<VersionedFileAwareConfiguration>
}
