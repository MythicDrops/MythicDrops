/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2019 Richard Harrah
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments

import com.tealcube.minecraft.bukkit.mythicdrops.utils.EnchantmentUtil
import kotlin.math.max
import kotlin.math.min
import org.apache.commons.lang3.math.NumberUtils
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment

/**
 * Represents an enchantment with a minimum level and a maximum level.
 *
 * @property enchantment Minecraft enchantment to wrap
 * @property minimumLevel Minimum level of enchantment
 * @property maximumLevel Maximum level of enchantment
 */
class MythicEnchantment(val enchantment: Enchantment, pMinimumLevel: Int, pMaximumLevel: Int = pMinimumLevel) {
    companion object {
        const val HIGHEST_ENCHANTMENT_LEVEL = 127

        /**
         * Constructs a [MythicEnchantment] from a [ConfigurationSection] and its associated [key].
         *
         * @param configurationSection ConfigurationSection of a YAML file
         * @param key Key for the given [configurationSection]
         *
         * @return Constructed MythicEnchantment, or null if unable to find matching [Enchantment]
         */
        @JvmStatic
        fun fromConfigurationSection(configurationSection: ConfigurationSection, key: String): MythicEnchantment? {
            val enchantment = EnchantmentUtil.getByKeyOrName(key) ?: return null
            val minimumLevel = max(
                configurationSection.getInt("minimumLevel", 0), configurationSection.getInt("minimum-level", 0)
            )
            val maximumLevel = max(
                configurationSection.getInt("maximumLevel", 0), configurationSection.getInt("maximum-level", 0)
            )
            return MythicEnchantment(enchantment, minimumLevel, maximumLevel)
        }

        /**
         * Constructs a [MythicEnchantment] from a [String] in the format "enchantment:minimumLevel:maximumLevel".
         *
         * Also accepts "enchantment:level" format.
         *
         * @param string String in "enchantment:minimumLevel:maximumLevel" format.
         * @return Constructed MythicEnchantment, or null if unable to find matching [Enchantment]
         */
        @JvmStatic
        fun fromString(string: String): MythicEnchantment? {
            var enchantment: Enchantment? = null
            var value1 = 0
            var value2 = 0
            val split = string.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            when (split.size) {
                0 -> {
                }
                1 -> {
                }
                2 -> {
                    enchantment = EnchantmentUtil.getByKeyOrName(split[0])
                    if (enchantment != null) {
                        value2 = NumberUtils.toInt(split[1], 1)
                        value1 = value2
                    }
                }
                else -> {
                    enchantment = EnchantmentUtil.getByKeyOrName(split[0])
                    if (enchantment != null) {
                        value1 = NumberUtils.toInt(split[1], 1)
                        value2 = NumberUtils.toInt(split[2], 1)
                    }
                }
            }
            return if (enchantment == null) {
                null
            } else MythicEnchantment(enchantment, value1, value2)
        }
    }

    val minimumLevel = min(pMinimumLevel, pMaximumLevel).coerceAtLeast(1)
    val maximumLevel = max(pMinimumLevel, pMaximumLevel).coerceAtMost(HIGHEST_ENCHANTMENT_LEVEL)

    /**
     * Gets a random level value between [minimumLevel] (inclusive) and [maximumLevel] (inclusive).
     *
     * @return random level
     */
    fun getRandomLevel() = if (minimumLevel < maximumLevel) {
        (minimumLevel..maximumLevel).random()
    } else {
        minimumLevel
    }

    override fun toString(): String {
        return "$enchantment:$minimumLevel:$maximumLevel"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MythicEnchantment

        if (enchantment != other.enchantment) return false
        if (minimumLevel != other.minimumLevel) return false
        if (maximumLevel != other.maximumLevel) return false

        return true
    }

    override fun hashCode(): Int {
        var result = enchantment.hashCode()
        result = 31 * result + minimumLevel
        result = 31 * result + maximumLevel
        return result
    }
}
