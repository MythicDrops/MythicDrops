package com.tealcube.minecraft.bukkit.mythicdrops.io

import io.pixeloutlaw.kindling.Log
import io.pixeloutlaw.minecraft.spigot.config.migration.migrators.ClasspathConfigMigrator
import org.bukkit.plugin.Plugin
import org.koin.core.annotation.Single
import java.io.File

@Single
internal class ResourceWriter(private val plugin: Plugin, private val classpathConfigMigrator: ClasspathConfigMigrator) {
    fun writeResourceFiles() {
        val resources = listOf(
            "/resources/lore/general.txt",
            "/resources/lore/enchantments/damage_all.txt",
            "/resources/lore/materials/diamond_sword.txt",
            "/resources/lore/tiers/legendary.txt",
            "/resources/lore/itemtypes/sword.txt",
            "/resources/prefixes/general.txt",
            "/resources/prefixes/enchantments/damage_all.txt",
            "/resources/prefixes/materials/diamond_sword.txt",
            "/resources/prefixes/tiers/legendary.txt",
            "/resources/prefixes/itemtypes/sword.txt",
            "/resources/suffixes/general.txt",
            "/resources/suffixes/enchantments/damage_all.txt",
            "/resources/suffixes/materials/diamond_sword.txt",
            "/resources/suffixes/tiers/legendary.txt",
            "/resources/suffixes/itemtypes/sword.txt",
            "/resources/mobnames/general.txt"
        )
        resources.forEach { resource ->
            val actual = File(plugin.dataFolder, resource)
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

    fun writeConfigFiles() {
        // write all configuration files from the JAR if they don't exist in the data folder
        classpathConfigMigrator.writeYamlFromResourcesIfNotExists("armor.yml")
        classpathConfigMigrator.writeYamlFromResourcesIfNotExists("config.yml")
        classpathConfigMigrator.writeYamlFromResourcesIfNotExists("creatureSpawning.yml")
        classpathConfigMigrator.writeYamlFromResourcesIfNotExists("customItems.yml")
        classpathConfigMigrator.writeYamlFromResourcesIfNotExists("language.yml")
        classpathConfigMigrator.writeYamlFromResourcesIfNotExists("identifying.yml")
        classpathConfigMigrator.writeYamlFromResourcesIfNotExists("itemGroups.yml")
        classpathConfigMigrator.writeYamlFromResourcesIfNotExists("relation.yml")
        classpathConfigMigrator.writeYamlFromResourcesIfNotExists("repairing.yml")
        classpathConfigMigrator.writeYamlFromResourcesIfNotExists("repairCosts.yml")
        classpathConfigMigrator.writeYamlFromResourcesIfNotExists("socketing.yml")
        classpathConfigMigrator.writeYamlFromResourcesIfNotExists("socketGems.yml")
    }
}
