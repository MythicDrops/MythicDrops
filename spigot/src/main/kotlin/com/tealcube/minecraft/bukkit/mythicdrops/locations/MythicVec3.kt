package com.tealcube.minecraft.bukkit.mythicdrops.locations

import com.tealcube.minecraft.bukkit.mythicdrops.api.locations.Vec3
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.ConfigurationSection

internal data class MythicVec3(
    override val world: World,
    override val x: Int,
    override val y: Int,
    override val z: Int
) : Vec3 {
    companion object {
        @JvmStatic
        fun fromConfigurationSection(configurationSection: ConfigurationSection): Vec3 {
            val worldString = configurationSection.getString("world") ?: ""
            val world = Bukkit.getWorld(worldString) ?: throw IllegalArgumentException("world does not exist")
            val x = configurationSection.getInt("x")
            val y = configurationSection.getInt("y")
            val z = configurationSection.getInt("z")
            return MythicVec3(world, x, y, z)
        }

        @JvmStatic
        fun fromLocation(location: Location): Vec3 {
            return MythicVec3(
                location.world!!,
                location.blockX,
                location.blockY,
                location.blockZ
            )
        }
    }

    /**
     * Adds a [Vec3] to this [Vec3] and returns the combination.
     *
     * @param other Another [Vec3]
     * @return combined value
     * @throws IllegalArgumentException if [other]'s [world] does not match this' world
     */
    operator fun plus(other: Vec3): Vec3 {
        require(world == other.world) { "worlds must be the same" }
        return MythicVec3(
            world,
            x + other.x,
            y + other.y,
            z + other.z
        )
    }
}
