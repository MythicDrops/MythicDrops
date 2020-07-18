package io.pixeloutlaw.minecraft.spigot.mythicdrops

import com.tealcube.minecraft.bukkit.mythicdrops.safeRandom
import org.bukkit.Material
import kotlin.math.max

/**
 * Determines a randomized durability from a durability percentage range.
 *
 * @param minimumDurabilityPercentage minimum percentage
 * @param maximumDurabilityPercentage maximum percentage
 */
fun Material.getDurabilityInPercentageRange(
    minimumDurabilityPercentage: Double,
    maximumDurabilityPercentage: Double
): Int {
    val coercedMinimumDurabilityPercentage = minimumDurabilityPercentage.coerceAtLeast(0.0).coerceAtMost(1.0)
    val coercedMaximumDurabilityPercentage = maximumDurabilityPercentage.coerceAtLeast(0.0).coerceAtMost(1.0)

    val maximumDurability = this.maxDurability - (this.maxDurability * max(
        coercedMinimumDurabilityPercentage,
        coercedMaximumDurabilityPercentage
    )).toInt()
    val minimumDurability = this.maxDurability - (this.maxDurability * max(
        coercedMinimumDurabilityPercentage,
        coercedMaximumDurabilityPercentage
    )).toInt()

    return (minimumDurability..maximumDurability).safeRandom()
}