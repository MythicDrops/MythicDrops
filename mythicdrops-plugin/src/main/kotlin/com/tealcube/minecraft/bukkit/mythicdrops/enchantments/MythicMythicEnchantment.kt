package com.tealcube.minecraft.bukkit.mythicdrops.enchantments

import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment
import com.tealcube.minecraft.bukkit.mythicdrops.utils.EnchantmentUtil
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import kotlin.math.max
import kotlin.math.min

internal data class MythicMythicEnchantment(
    override val enchantment: Enchantment,
    override val minimumLevel: Int,
    override val maximumLevel: Int = minimumLevel
) : MythicEnchantment {
    companion object {
        private val colonRegex = """:""".toRegex()

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
                configurationSection.getInt("$key.minimumLevel", MythicEnchantment.MINIMUM_ENCHANTMENT_LEVEL),
                configurationSection.getInt("$key.minimum-level", MythicEnchantment.MINIMUM_ENCHANTMENT_LEVEL)
            )
            val maximumLevel = max(
                configurationSection.getInt("$key.maximumLevel", MythicEnchantment.MINIMUM_ENCHANTMENT_LEVEL),
                configurationSection.getInt("$key.maximum-level", MythicEnchantment.MINIMUM_ENCHANTMENT_LEVEL)
            )
            return of(enchantment, minimumLevel, maximumLevel)
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
            val split = string.split(colonRegex).dropLastWhile { it.isEmpty() }.toTypedArray()
            when (split.size) {
                0 -> {
                }
                1 -> {
                }
                2 -> {
                    enchantment = EnchantmentUtil.getByKeyOrName(split[0])
                    if (enchantment != null) {
                        value1 = split[1].toIntOrNull() ?: MythicEnchantment.MINIMUM_ENCHANTMENT_LEVEL
                        value2 = value1
                    }
                }
                else -> {
                    enchantment = EnchantmentUtil.getByKeyOrName(split[0])
                    if (enchantment != null) {
                        value1 = split[1].toIntOrNull() ?: MythicEnchantment.MINIMUM_ENCHANTMENT_LEVEL
                        value2 = split[2].toIntOrNull() ?: MythicEnchantment.MINIMUM_ENCHANTMENT_LEVEL
                    }
                }
            }
            return if (enchantment == null) {
                null
            } else {
                of(enchantment, value1, value2)
            }
        }

        fun of(enchantment: Enchantment, minimumLevel: Int, maximumLevel: Int = minimumLevel): MythicEnchantment {
            return MythicMythicEnchantment(
                enchantment,
                min(minimumLevel, maximumLevel).coerceAtLeast(MythicEnchantment.MINIMUM_ENCHANTMENT_LEVEL),
                max(minimumLevel, maximumLevel).coerceAtMost(MythicEnchantment.MAXIMUM_ENCHANTMENT_LEVEL)
            )
        }
    }

    /**
     * Gets a random level value between [minimumLevel] (inclusive) and [maximumLevel] (inclusive).
     *
     * @return random level
     */
    override fun getRandomLevel() = if (minimumLevel < maximumLevel) {
        (minimumLevel..maximumLevel).random()
    } else {
        minimumLevel
    }

    override fun toString(): String {
        return "$enchantment:$minimumLevel:$maximumLevel"
    }
}
