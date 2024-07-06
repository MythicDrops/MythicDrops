package com.tealcube.minecraft.bukkit.mythicdrops.enchantments

import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment
import com.tealcube.minecraft.bukkit.mythicdrops.utils.EnchantmentUtil
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import kotlin.math.max
import kotlin.math.min

/**
 * Represents an enchantment with a minimum level and a maximum level.
 *
 * @property enchantment Minecraft enchantment to wrap
 * @property minimumLevel Minimum level of enchantment
 * @property maximumLevel Maximum level of enchantment
 */

internal data class MythicMythicEnchantment(
    override val enchantment: Enchantment,
    internal val pMinimumLevel: Int,
    internal val pMaximumLevel: Int = pMinimumLevel,
    override val minimumLevel: Int = min(pMinimumLevel, pMaximumLevel).coerceAtLeast(1),
    override val maximumLevel: Int = max(pMinimumLevel, pMaximumLevel).coerceAtMost(HIGHEST_ENCHANTMENT_LEVEL)
) : MythicEnchantment {
    companion object {
        const val HIGHEST_ENCHANTMENT_LEVEL = 127
        private const val DEFAULT_ENCHANTMENT_LEVEL = 1
        private val colonRegex = """:""".toRegex()

        /**
         * Constructs a [MythicEnchantment] from a [ConfigurationSection] and its associated [key].
         *
         * @param configurationSection ConfigurationSection of a YAML file
         * @param key Key for the given [configurationSection]
         *
         * @return Constructed MythicEnchantment, or null if unable to find matching [Enchantment]
         */
        fun fromConfigurationSection(
            configurationSection: ConfigurationSection,
            key: String
        ): MythicEnchantment? {
            val enchantment = EnchantmentUtil.getByKeyOrName(key) ?: return null
            val minimumLevel =
                max(
                    configurationSection.getInt("$key.minimumLevel", DEFAULT_ENCHANTMENT_LEVEL),
                    configurationSection.getInt("$key.minimum-level", DEFAULT_ENCHANTMENT_LEVEL)
                )
            val maximumLevel =
                max(
                    configurationSection.getInt("$key.maximumLevel", DEFAULT_ENCHANTMENT_LEVEL),
                    configurationSection.getInt("$key.maximum-level", DEFAULT_ENCHANTMENT_LEVEL)
                )
            return MythicMythicEnchantment(
                enchantment,
                minimumLevel,
                maximumLevel
            )
        }

        /**
         * Constructs a [MythicEnchantment] from a [String] in the format "enchantment:minimumLevel:maximumLevel".
         *
         * Also accepts "enchantment:level" format.
         *
         * @param string String in "enchantment:minimumLevel:maximumLevel" format.
         * @return Constructed MythicEnchantment, or null if unable to find matching [Enchantment]
         */
        fun fromString(string: String): MythicEnchantment? {
            var enchantment: Enchantment? = null
            var value1 = 0
            var value2 = 0
            val split = string.split(colonRegex).dropLastWhile { it.isEmpty() }.toTypedArray()
            when (split.size) {
                0 -> {
                }

                1 -> {
                }

                2 -> {
                    enchantment = EnchantmentUtil.getByKeyOrName(split[0])
                    if (enchantment != null) {
                        value1 = split[1].toIntOrNull() ?: DEFAULT_ENCHANTMENT_LEVEL
                        value2 = value1
                    }
                }

                else -> {
                    enchantment = EnchantmentUtil.getByKeyOrName(split[0])
                    if (enchantment != null) {
                        value1 = split[1].toIntOrNull() ?: DEFAULT_ENCHANTMENT_LEVEL
                        value2 = split[2].toIntOrNull() ?: DEFAULT_ENCHANTMENT_LEVEL
                    }
                }
            }
            return if (enchantment == null) {
                null
            } else {
                MythicMythicEnchantment(
                    enchantment,
                    value1,
                    value2
                )
            }
        }
    }

    /**
     * Gets a random level value between [minimumLevel] (inclusive) and [maximumLevel] (inclusive).
     *
     * @return random level
     */
    override fun getRandomLevel() =
        if (minimumLevel < maximumLevel) {
            (minimumLevel..maximumLevel).random()
        } else {
            minimumLevel
        }

    override fun toString(): String = "$enchantment:$minimumLevel:$maximumLevel"
}
