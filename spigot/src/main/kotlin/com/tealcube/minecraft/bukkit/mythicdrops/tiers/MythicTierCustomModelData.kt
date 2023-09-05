package com.tealcube.minecraft.bukkit.mythicdrops.tiers

import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierCustomModelData
import dev.mythicdrops.Either
import dev.mythicdrops.Either.Companion.left
import dev.mythicdrops.Either.Companion.right
import io.pixeloutlaw.minecraft.spigot.mythicdrops.enumValueOrNull
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getOrDefaultAsDefaultValueType
import org.bukkit.Material

internal data class MythicTierCustomModelData(
    override val modelData: Int = 0,
    override val weight: Double = 0.0,
    override val material: Material? = null
) : TierCustomModelData {
    companion object {
        fun fromMap(
            map: Map<String, Any>
        ): Either<String, MythicTierCustomModelData> {
            val material = enumValueOrNull<Material>(map.getOrDefaultAsDefaultValueType("material", ""))
            if (map.containsKey("material") && material == null) {
                return left("material was present and invalid: ${map["material"]}")
            }
            return right(
                MythicTierCustomModelData(
                    modelData = map.getOrDefaultAsDefaultValueType("model-data", 0),
                    weight = map.getOrDefaultAsDefaultValueType("weight", 0.0 as Number).toDouble(),
                    material = enumValueOrNull<Material>(map.getOrDefaultAsDefaultValueType("material", ""))
                )
            )
        }
    }
}
