/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2019 Richard Harrah
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.api.locations

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.ConfigurationSection

/**
 * Represents a location with a [World], an x, a y, and a z.
 *
 * @property world World of location
 * @property x x of location
 * @property y y of location
 * @property z z of location
 */
data class Vec3(val world: World, val x: Int, val y: Int, val z: Int) {
    companion object {
        @JvmStatic
        fun fromConfigurationSection(configurationSection: ConfigurationSection): Vec3 {
            val worldString = configurationSection.getString("world") ?: ""
            val world = Bukkit.getWorld(worldString) ?: throw IllegalArgumentException("world does not exist")
            val x = configurationSection.getInt("x")
            val y = configurationSection.getInt("y")
            val z = configurationSection.getInt("z")
            return Vec3(world, x, y, z)
        }

        @JvmStatic
        fun fromLocation(location: Location): Vec3 {
            return Vec3(location.world!!, location.blockX, location.blockY, location.blockZ)
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
        return Vec3(world, x + other.x, y + other.y, z + other.z)
    }
}
