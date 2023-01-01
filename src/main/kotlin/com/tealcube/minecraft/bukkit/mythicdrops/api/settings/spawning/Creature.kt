package com.tealcube.minecraft.bukkit.mythicdrops.api.settings.spawning

import org.bukkit.entity.EntityType

/**
 * Represents a creature in the creatureSpawning.yml.
 */
interface Creature {
    val entityType: EntityType
    val dropMultiplier: Double
    val tierDrops: List<String>
}
