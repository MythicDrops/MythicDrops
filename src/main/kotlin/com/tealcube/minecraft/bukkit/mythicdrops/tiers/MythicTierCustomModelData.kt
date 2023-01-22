package com.tealcube.minecraft.bukkit.mythicdrops.tiers

import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierCustomModelData
import io.pixeloutlaw.minecraft.spigot.mythicdrops.enumValueOrNull
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getOrDefaultAsDefaultValueType
import org.bukkit.Material

internal data class MythicTierCustomModelData(
    override val modelData: Int = 0,
    override val weight: Double = 0.0,
    override val material: Material? = null
) : TierCustomModelData {
    companion object {
        fun fromMap(map: Map<String, Any>): MythicTierCustomModelData {
            return MythicTierCustomModelData(
                modelData = map.getOrDefaultAsDefaultValueType("model-data", 0),
                weight = map.getOrDefaultAsDefaultValueType("weight", 0.0),
                material = enumValueOrNull<Material>(map.getOrDefaultAsDefaultValueType("material", ""))
            )
        }
    }
}
