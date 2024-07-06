package com.tealcube.minecraft.bukkit.mythicdrops.settings.spawning

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.spawning.NumberOfPasses
import com.tealcube.minecraft.bukkit.mythicdrops.safeRandom
import kotlin.math.max
import kotlin.math.min

data class MythicNumberOfPasses(
    private val pMinimum: Int,
    private val pMaximum: Int,
    override val minimum: Int = min(pMinimum, pMaximum),
    override val maximum: Int = max(pMinimum, pMaximum)
) : NumberOfPasses {
    override fun random(): Int = (minimum..maximum).safeRandom()
}
