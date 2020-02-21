/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2020 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops

import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.choices.WeightedChoice
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TierUtil
import kotlin.math.pow
import org.bukkit.entity.LivingEntity

fun LivingEntity.getTierForEntity(mythicDrops: MythicDrops): Tier? {
    val allowableTiers =
        (mythicDrops.settingsManager.creatureSpawningSettings.tierDrops[this.type] ?: emptyList())
            .mapNotNull { TierUtil.getTier(it) }

    val distanceFromSpawnInBlocks = this.location.distanceSquared(this.world.spawnLocation).toInt()

    val selectableTiers = allowableTiers.filter {
        if (it.maximumDistanceFromSpawn < 0 || it.minimumDistanceFromSpawn < 0) {
            true
        } else {
            val minDistFromSpawnSquared =
                it.minimumDistanceFromSpawn.toDouble().pow(2.0)
            val maxDistFromSpawnSquared =
                it.maximumDistanceFromSpawn.toDouble().pow(2.0)
            !(distanceFromSpawnInBlocks > maxDistFromSpawnSquared ||
                distanceFromSpawnInBlocks < minDistFromSpawnSquared)
        }
    }

    return WeightedChoice.between(selectableTiers).choose()
}
