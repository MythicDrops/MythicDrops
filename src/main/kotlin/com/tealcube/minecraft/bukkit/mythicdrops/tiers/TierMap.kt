/*
 * The MIT License
 * Copyright © 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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