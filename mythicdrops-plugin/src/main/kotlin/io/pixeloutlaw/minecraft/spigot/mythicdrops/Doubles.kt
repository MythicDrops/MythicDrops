package io.pixeloutlaw.minecraft.spigot.mythicdrops

/**
 * Checks if the current double is within a given positive and negative threshold.
 *
 * @param threshold threshold to check if within
 */
internal fun Double.isZero(threshold: Double = 0.000005): Boolean = this >= -threshold && this <= threshold
