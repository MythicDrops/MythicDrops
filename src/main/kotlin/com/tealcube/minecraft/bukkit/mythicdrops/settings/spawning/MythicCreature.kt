package com.tealcube.minecraft.bukkit.mythicdrops.settings.spawning

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.spawning.Creature
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.spawning.NumberOfPasses
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.EntityType

internal data class MythicCreature(
    override val entityType: EntityType,
    override val dropMultiplier: Double = 0.0,
    override val tierDrops: List<String> = emptyList(),
    override val numberOfLootPasses: NumberOfPasses = MythicNumberOfPasses(0, 0)
) : Creature {
    companion object {
        fun fromConfigurationSection(
            configurationSection: ConfigurationSection,
            entityType: EntityType
        ): MythicCreature {
            val dropMultiplier = configurationSection.getDouble("drop-multiplier", 0.0)
            val tierDrops = configurationSection.getStringList("tier-drops")
            val numberOfLootPasses =
                if (configurationSection.isConfigurationSection("number-of-loot-passes")) {
                    val minimum = configurationSection.getOrCreateSection("number-of-loot-passes").getInt("minimum")
                    val maximum = configurationSection.getOrCreateSection("number-of-loot-passes").getInt("maximum")
                    MythicNumberOfPasses(minimum, maximum)
                } else {
                    val num = configurationSection.getInt("number-of-loot-passes")
                    MythicNumberOfPasses(num, num)
                }
            return MythicCreature(
                entityType = entityType,
                dropMultiplier = dropMultiplier,
                tierDrops = tierDrops,
                numberOfLootPasses = numberOfLootPasses
            )
        }
    }
}
