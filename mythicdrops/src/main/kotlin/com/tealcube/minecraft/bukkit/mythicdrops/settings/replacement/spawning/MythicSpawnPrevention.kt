package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.spawning

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.spawning.SpawnPrevention
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import org.bukkit.configuration.ConfigurationSection

data class MythicSpawnPrevention(
    override val isSpawnEgg: Boolean = true,
    override val isSpawner: Boolean = true,
    override val isCustom: Boolean = true,
    override val isReinforcements: Boolean = true,
    override val aboveY: Map<String, Int> = mapOf("world" to 255)
) : SpawnPrevention {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection): MythicSpawnPrevention {
            val spawnEgg = configurationSection.getBoolean("spawnEgg", true)
            val spawner = configurationSection.getBoolean("spawner", true)
            val custom = configurationSection.getBoolean("custom", true)
            val reinforcements = configurationSection.getBoolean("reinforcements", true)
            val aboveY = configurationSection.getOrCreateSection("aboveY").let { aboveYCS ->
                aboveYCS.getKeys(false).map { key -> key to aboveYCS.getInt(key, 255) }.toMap()
            }
            return MythicSpawnPrevention(spawnEgg, spawner, custom, reinforcements, aboveY)
        }
    }
}