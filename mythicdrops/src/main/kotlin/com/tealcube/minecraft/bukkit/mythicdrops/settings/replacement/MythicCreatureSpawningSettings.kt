package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.CreatureSpawningSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.spawning.SpawnPrevention
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.spawning.MythicSpawnPrevention
import com.tealcube.minecraft.bukkit.mythicdrops.toEntityType
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.EntityType

data class MythicCreatureSpawningSettings(
    override val version: String = "",
    override val spawnPrevention: SpawnPrevention = MythicSpawnPrevention(),
    override val dropMultipliers: Map<EntityType, Double> = emptyMap(),
    override val tierDrops: Map<EntityType, List<String>> = emptyMap()
): CreatureSpawningSettings {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection): MythicCreatureSpawningSettings {
            val version = configurationSection.getString("version") ?: ""
            val spawnPrevention = MythicSpawnPrevention.fromConfigurationSection(configurationSection.getOrCreateSection("spawnPrevention"))
            val dropMultipliers = configurationSection.getOrCreateSection("dropMultipliers").let { dropMultipliersCS ->
                dropMultipliersCS.getKeys(false)
                    .map { key -> key.toEntityType() to dropMultipliersCS.getDouble(key) }
                    .mapNotNull { (entityType, dropMultiplier) ->
                        entityType?.let { it to dropMultiplier }
                    }
                    .toMap()
            }
            val tierDrops = configurationSection.getOrCreateSection("tierDrops").let { tierDropsCS ->
                tierDropsCS.getKeys(false)
                    .map { key -> key.toEntityType() to tierDropsCS.getStringList(key) }
                    .mapNotNull { (entityType, dropMultiplier) ->
                        entityType?.let { it to dropMultiplier }
                    }
                    .toMap()
            }
            return MythicCreatureSpawningSettings(version, spawnPrevention, dropMultipliers, tierDrops)
        }
    }
}