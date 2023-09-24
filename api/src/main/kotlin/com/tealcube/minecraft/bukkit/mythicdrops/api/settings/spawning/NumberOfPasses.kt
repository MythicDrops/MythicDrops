package com.tealcube.minecraft.bukkit.mythicdrops.api.settings.spawning

/**
 * Represents a number of passes that could be done for a creature.
 * @property minimum Minimum number of passes
 * @property maximum Maximum number of passes
 */
interface NumberOfPasses {
    val minimum: Int
    val maximum: Int

    /**
     * Returns a random number between minimum and maximum.
     */
    fun random(): Int
}
