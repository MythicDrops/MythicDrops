package io.pixeloutlaw.minecraft.spigot.mythicdrops

import com.tealcube.minecraft.bukkit.mythicdrops.api.choices.WeightedChoice
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.CreatureSpawningSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import org.bukkit.entity.LivingEntity
import kotlin.math.pow

/**
 * Attempts to get a tier for this particular entity's type.
 *
 * @param creatureSpawningSettings
 * @param tierManager
 */
fun LivingEntity.getTier(
    creatureSpawningSettings: CreatureSpawningSettings,
    tierManager: TierManager
): Tier? {
    val allowableTiers =
        (creatureSpawningSettings.tierDrops[this.type] ?: emptyList())
            .mapNotNull { tierManager.getByName(it) }

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
