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
package io.pixeloutlaw.minecraft.spigot.mythicdrops

import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.safeRandom
import org.bukkit.Material
import kotlin.math.max
import kotlin.math.min

/**
 * Determines a randomized durability from a durability percentage range.
 *
 * @param minimumDurabilityPercentage minimum percentage
 * @param maximumDurabilityPercentage maximum percentage
 */
internal fun Material.getDurabilityInPercentageRange(
    minimumDurabilityPercentage: Double,
    maximumDurabilityPercentage: Double
): Int {
    val coercedMinimumDurabilityPercentage = minimumDurabilityPercentage.coerceAtLeast(0.0).coerceAtMost(1.0)
    val coercedMaximumDurabilityPercentage = maximumDurabilityPercentage.coerceAtLeast(0.0).coerceAtMost(1.0)

    val maximumDurability = this.maxDurability - (
        this.maxDurability * max(
            coercedMinimumDurabilityPercentage,
            coercedMaximumDurabilityPercentage
        )
        ).toInt()
    val minimumDurability = this.maxDurability - (
        this.maxDurability * min(
            coercedMinimumDurabilityPercentage,
            coercedMaximumDurabilityPercentage
        )
        ).toInt()

    return (minimumDurability..maximumDurability).safeRandom()
}

/**
 * Gets the applicable tiers for a material.
 *
 * @param tierManager tier manager instance
 *
 * @return applicable Tiers using the item groups and material configs for a tier
 */
internal fun Material.getApplicableTiers(tierManager: TierManager): Collection<Tier> {
    return tierManager.get().filter { it.getMaterials().contains(this) }
}

/**
 * Gets the name of the material in a human presentable way.
 */
internal fun Material.getMinecraftName(): String =
    name.split("_").filter { it.isNotBlank() }.joinToString { it.toTitleCase() }
