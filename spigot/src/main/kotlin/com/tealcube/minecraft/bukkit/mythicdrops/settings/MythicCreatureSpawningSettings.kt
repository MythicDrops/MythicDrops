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
package com.tealcube.minecraft.bukkit.mythicdrops.settings

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.CreatureSpawningSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.spawning.Creature
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.spawning.SpawnPrevention
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.settings.spawning.MythicCreature
import com.tealcube.minecraft.bukkit.mythicdrops.settings.spawning.MythicSpawnPrevention
import io.pixeloutlaw.minecraft.spigot.mythicdrops.enumValueOrNull
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.EntityType

internal data class MythicCreatureSpawningSettings(
    override val version: String = "",
    override val spawnPrevention: SpawnPrevention = MythicSpawnPrevention(),
    override val creatures: Map<EntityType, Creature> = emptyMap(),
    @Deprecated("Use creatures instead.")
    override val dropMultipliers: Map<EntityType, Double> = emptyMap(),
    @Deprecated("Use creatures instead.")
    override val tierDrops: Map<EntityType, List<String>> = emptyMap()
) : CreatureSpawningSettings {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection): MythicCreatureSpawningSettings {
            val version = configurationSection.getString("version") ?: ""
            val spawnPrevention =
                MythicSpawnPrevention.fromConfigurationSection(
                    configurationSection.getOrCreateSection("spawnPrevention")
                )
            val creaturesSection = configurationSection.getOrCreateSection("creatures")
            val creatures =
                creaturesSection.getKeys(false).mapNotNull { enumValueOrNull<EntityType>(it) }
                    .map { MythicCreature.fromConfigurationSection(creaturesSection.getOrCreateSection(it.name), it) }
                    .associateBy { it.entityType }
            val dropMultipliers = mutableMapOf<EntityType, Double>()
            val tierDrops = mutableMapOf<EntityType, List<String>>()
            return MythicCreatureSpawningSettings(
                version = version,
                spawnPrevention = spawnPrevention,
                creatures = creatures,
                dropMultipliers = dropMultipliers,
                tierDrops = tierDrops
            )
        }
    }
}
