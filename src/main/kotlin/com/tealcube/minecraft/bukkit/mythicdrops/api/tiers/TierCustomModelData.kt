package com.tealcube.minecraft.bukkit.mythicdrops.api.tiers

import com.tealcube.minecraft.bukkit.mythicdrops.api.weight.Weighted
import org.bukkit.Material

/**
 * Represents a possible custom model data for a tier.
 */
interface TierCustomModelData: Weighted {
    val modelData: Int
    val material: Material?
}
