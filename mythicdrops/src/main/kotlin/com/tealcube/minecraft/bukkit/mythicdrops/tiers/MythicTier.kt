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
package com.tealcube.minecraft.bukkit.mythicdrops.tiers

import com.squareup.moshi.JsonClass
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment
import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroup
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.getChatColor
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection

@JsonClass(generateAdapter = true)
data class MythicTier(
    override val name: String = "",
    override val displayName: String = "",
    override val displayColor: ChatColor = ChatColor.RESET,
    override val identifierColor: ChatColor = ChatColor.RESET,
    override val baseLore: List<String> = emptyList(),
    override val bonusLore: List<String> = emptyList(),
    override val minimumBonusLore: Int = 0,
    override val maximumBonusLore: Int = 0,
    override val baseEnchantments: Set<MythicEnchantment> = emptySet(),
    override val bonusEnchantments: Set<MythicEnchantment> = emptySet(),
    override val minimumBonusEnchantments: Int = 0,
    override val maximumBonusEnchantments: Int = 0,
    override val isSafeBaseEnchantments: Boolean = false,
    override val isSafeBonusEnchantments: Boolean = false,
    override val isAllowHighBaseEnchantments: Boolean = false,
    override val isAllowHighBonusEnchantments: Boolean = false,
    override val minimumDurabilityPercentage: Double = 0.0,
    override val maximumDurabilityPercentage: Double = 0.0,
    override val chanceToDropOnMonsterDeath: Double = 0.0,
    override val allowedItemGroups: Set<ItemGroup> = emptySet(),
    override val disallowedItemGroups: Set<ItemGroup> = emptySet(),
    override val allowedMaterialIds: Set<Material> = emptySet(),
    override val disallowedMaterialIds: Set<Material> = emptySet(),
    override val minimumDistanceFromSpawn: Int = -1,
    override val maximumDistanceFromSpawn: Int = -1,
    override val isUnbreakable: Boolean = false,
    override val identityWeight: Double = 0.0,
    override val weight: Double = 0.0,
    override val chanceToHaveSockets: Double = 0.0,
    override val minimumSockets: Int = 0,
    override val maximumSockets: Int = 0,
    override val isBroadcastOnFind: Boolean = false
) : Tier {
    companion object {
        private val logger = JulLoggerFactory.getLogger(MythicTier::class)

        @JvmStatic
        fun fromConfigurationSection(
            configurationSection: ConfigurationSection,
            key: String,
            itemGroupManager: ItemGroupManager,
            loadingErrorManager: LoadingErrorManager
        ): MythicTier? {
            val displayColor = configurationSection.getChatColor("display-color")?.let {
                if (it == ChatColor.WHITE) {
                    logger.info("WHITE doesn't work due to a bug in Spigot, so we're replacing it with RESET instead")
                    ChatColor.RESET
                } else {
                    it
                }
            }
            if (displayColor == null) {
                logger.fine("displayColor == null, key=$key")
                loadingErrorManager.add("Not loading tier $key as it has an invalid display color")
                return null
            }
            val identifierColor = configurationSection.getChatColor("identifier-color")
            if (identifierColor == null) {
                logger.fine("identifierColor == null, key=$key")
                loadingErrorManager.add("Not loading tier $key as it has an invalid identifier color")
                return null
            }
            val mythicBaseEnchantments =
                if (configurationSection.isConfigurationSection("enchantments.base-enchantments")) {
                    val baseEnchantmentsCs = configurationSection.getOrCreateSection("enchantments.base-enchantments")
                    baseEnchantmentsCs.getKeys(false)
                        .mapNotNull { MythicEnchantment.fromConfigurationSection(baseEnchantmentsCs, it) }
                } else {
                    configurationSection.getStringList("enchantments.base-enchantments")
                        .mapNotNull { MythicEnchantment.fromString(it) }
                }
            val mythicBonusEnchantments =
                if (configurationSection.isConfigurationSection("enchantments.bonus-enchantments")) {
                    val bonusEnchantmentsCs = configurationSection.getOrCreateSection("enchantments.bonus-enchantments")
                    bonusEnchantmentsCs.getKeys(false)
                        .mapNotNull { MythicEnchantment.fromConfigurationSection(bonusEnchantmentsCs, it) }
                } else {
                    configurationSection.getStringList("enchantments.bonus-enchantments")
                        .mapNotNull { MythicEnchantment.fromString(it) }
                }
            val allowedItemGroups = configurationSection.getStringList("item-types.allowed-groups").mapNotNull {
                itemGroupManager.getById(it)
            }
            val disallowedItemGroups = configurationSection.getStringList("item-types.disallowed-groups").mapNotNull {
                itemGroupManager.getById(it)
            }
            val allowedMaterialIds = configurationSection.getStringList("item-types.allowed-material-ids").mapNotNull {
                Material.getMaterial(it)
            }
            val disallowedMaterialIds =
                configurationSection.getStringList("item-types.disallowed-material-ids").mapNotNull {
                    Material.getMaterial(it)
                }
            return MythicTier(
                key,
                configurationSection.getNonNullString("display-name", key),
                displayColor,
                identifierColor,
                configurationSection.getStringList("lore.base-lore"),
                configurationSection.getStringList("lore.bonus-lore"),
                configurationSection.getInt("lore.minimum-bonus-lore"),
                configurationSection.getInt("lore.maximum-bonus-lore"),
                mythicBaseEnchantments.toSet(),
                mythicBonusEnchantments.toSet(),
                configurationSection.getInt("enchantments.minimum-bonus-enchantments"),
                configurationSection.getInt("enchantments.maximum-bonus-enchantments"),
                configurationSection.getBoolean("enchantments.safe-base-enchantments"),
                configurationSection.getBoolean("enchantments.safe-bonus-enchantments"),
                configurationSection.getBoolean("enchantments.allow-high-base-enchantments"),
                configurationSection.getBoolean("enchantments.allow-high-bonus-enchantments"),
                configurationSection.getDouble("minimum-durability"),
                configurationSection.getDouble("maximum-durability"),
                configurationSection.getDouble("chance-to-drop-on-monster-death"),
                allowedItemGroups.toSet(),
                disallowedItemGroups.toSet(),
                allowedMaterialIds.toSet(),
                disallowedMaterialIds.toSet(),
                configurationSection.getInt("minimum-distance-from-spawn", -1),
                configurationSection.getInt("maximum-distance-from-spawn", -1),
                configurationSection.getBoolean("unbreakable"),
                configurationSection.getDouble("weight"),
                configurationSection.getDouble("identity-weight"),
                configurationSection.getDouble("chance-to-have-sockets"),
                configurationSection.getInt("minimum-sockets"),
                configurationSection.getInt("maximum-sockets"),
                configurationSection.getBoolean("broadcast-on-find")
            )
        }
    }

    override fun compareTo(other: Tier): Int {
        return if (this == other) {
            0
        } else {
            weight.compareTo(other.weight)
        }
    }
}
