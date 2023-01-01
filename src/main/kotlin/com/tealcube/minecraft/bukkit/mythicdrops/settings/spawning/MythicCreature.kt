package com.tealcube.minecraft.bukkit.mythicdrops.settings.spawning

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.spawning.Creature
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.EntityType

internal data class MythicCreature(
    override val entityType: EntityType,
    override val dropMultiplier: Double = 0.0,
    override val tierDrops: List<String> = emptyList()
) : Creature {
    companion object {
        fun fromConfigurationSection(
            configurationSection: ConfigurationSection,
            entityType: EntityType
        ): MythicCreature {
            val dropMultiplier = configurationSection.getDouble("drop-multiplier", 0.0)
            val tierDrops = configurationSection.getStringList("tier-drops")
            return MythicCreature(entityType = entityType, dropMultiplier = dropMultiplier, tierDrops = tierDrops)
        }
    }
}
