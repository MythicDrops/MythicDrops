package com.tealcube.minecraft.bukkit.mythicdrops.tiers

import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TierUtil
import org.apache.commons.lang3.RandomUtils
import org.bukkit.ChatColor
import java.util.concurrent.ConcurrentHashMap

object TierMap : ConcurrentHashMap<String, Tier>() {
    fun getRandomTierWithChance(): Tier? {
        if (values.isEmpty()) {
            return null
        }
        val totalWeight = values.sumByDouble { it.spawnChance }

        val chosenWeight = RandomUtils.nextDouble(0.0, 1.0) * totalWeight

        return TierUtil.getTierFromListWithWeight(values.shuffled(), chosenWeight)
    }

    fun getRandomTier(): Tier {
        return values.toTypedArray()[RandomUtils.nextInt(0, values.size)]
    }

    fun getRandomTierWithIdentifyChance(): Tier? {
        if (values.isEmpty()) {
            return null
        }
        val totalWeight = values.sumByDouble { it.identifyChance }

        val chosenWeight = RandomUtils.nextDouble(0.0, 1.0) * totalWeight

        return TierUtil.getTierFromListWithWeight(values.shuffled(), chosenWeight)
    }

    fun hasTierWithColors(displayColor: ChatColor, identifierColor: ChatColor): Boolean {
        return values.any { it.displayColor == displayColor && it.identificationColor == identifierColor }
    }
}