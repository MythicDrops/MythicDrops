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
) : CreatureSpawningSettings {
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
