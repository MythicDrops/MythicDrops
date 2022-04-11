package io.pixeloutlaw.minecraft.spigot.mythicdrops

import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun ClosedFloatingPointRange<Double>.safeRandom(): Double {
    val minOfRange = min(start, endInclusive)
    val maxOfRange = max(start, endInclusive)
    val value = minOfRange + Random.Default.nextDouble() * (maxOfRange - minOfRange)
    return min(max(value, minOfRange), maxOfRange)
}
