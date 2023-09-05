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
package com.tealcube.minecraft.bukkit.mythicdrops.settings.spawning

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.spawning.SpawnPrevention
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import org.bukkit.configuration.ConfigurationSection

internal data class MythicSpawnPrevention(
    override val isSpawnEgg: Boolean = true,
    override val isSpawner: Boolean = true,
    override val isCustom: Boolean = true,
    override val isReinforcements: Boolean = true,
    override val isDrowned: Boolean = true,
    override val aboveY: Map<String, Int> = mapOf("world" to 255)
) : SpawnPrevention {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection): MythicSpawnPrevention {
            val isSpawnEgg = configurationSection.getBoolean("spawnEgg", true)
            val isSpawner = configurationSection.getBoolean("spawner", true)
            val isCustom = configurationSection.getBoolean("custom", true)
            val isReinforcements = configurationSection.getBoolean("reinforcements", true)
            val isDrowned = configurationSection.getBoolean("drowned", true)
            val aboveY = configurationSection.getOrCreateSection("aboveY").let { aboveYCS ->
                aboveYCS.getKeys(false).map { key -> key to aboveYCS.getInt(key, 255) }.toMap()
            }
            return MythicSpawnPrevention(
                isSpawnEgg = isSpawnEgg,
                isSpawner = isSpawner,
                isCustom = isCustom,
                isReinforcements = isReinforcements,
                isDrowned = isDrowned,
                aboveY = aboveY
            )
        }
    }
}
