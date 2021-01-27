/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2020 Richard Harrah
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

import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierEnchantments
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import org.bukkit.configuration.ConfigurationSection

internal data class MythicTierEnchantments(
    override val baseEnchantments: Set<MythicEnchantment> = emptySet(),
    override val bonusEnchantments: Set<MythicEnchantment> = emptySet(),
    override val minimumBonusEnchantments: Int = 0,
    override val maximumBonusEnchantments: Int = 0,
    override val isSafeBaseEnchantments: Boolean = false,
    override val isSafeBonusEnchantments: Boolean = false,
    override val isSafeRelationEnchantments: Boolean = false,
    override val isAllowHighBaseEnchantments: Boolean = false,
    override val isAllowHighBonusEnchantments: Boolean = false,
    override val isAllowHighRelationEnchantments: Boolean = false
) : TierEnchantments {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection): MythicTierEnchantments {
            val mythicBaseEnchantments =
                if (configurationSection.isConfigurationSection("base-enchantments")) {
                    val baseEnchantmentsCs = configurationSection.getOrCreateSection("base-enchantments")
                    baseEnchantmentsCs.getKeys(false)
                        .mapNotNull { MythicEnchantment.fromConfigurationSection(baseEnchantmentsCs, it) }
                } else {
                    configurationSection.getStringList("base-enchantments")
                        .mapNotNull { MythicEnchantment.fromString(it) }
                }
            val mythicBonusEnchantments =
                if (configurationSection.isConfigurationSection("bonus-enchantments")) {
                    val bonusEnchantmentsCs = configurationSection.getOrCreateSection("bonus-enchantments")
                    bonusEnchantmentsCs.getKeys(false)
                        .mapNotNull { MythicEnchantment.fromConfigurationSection(bonusEnchantmentsCs, it) }
                } else {
                    configurationSection.getStringList("bonus-enchantments")
                        .mapNotNull { MythicEnchantment.fromString(it) }
                }
            val isAllowHighBaseEnchantments =
                configurationSection.getBoolean("allow-high-base-enchantments")
            val isAllowHighBonusEnchantments =
                configurationSection.getBoolean("allow-high-bonus-enchantments")
            val isAllowHighRelationEnchantments =
                configurationSection.getBoolean("allow-high-relation-enchantments")
            val isSafeRelationEnchantments =
                configurationSection.getBoolean("safe-relation-enchantments")
            return MythicTierEnchantments(
                baseEnchantments = mythicBaseEnchantments.toSet(),
                bonusEnchantments = mythicBonusEnchantments.toSet(),
                minimumBonusEnchantments = configurationSection.getInt("minimum-bonus-enchantments"),
                maximumBonusEnchantments = configurationSection.getInt("maximum-bonus-enchantments"),
                isSafeBaseEnchantments = configurationSection.getBoolean("safe-base-enchantments"),
                isSafeBonusEnchantments = configurationSection.getBoolean("safe-bonus-enchantments"),
                isAllowHighBaseEnchantments = isAllowHighBaseEnchantments,
                isAllowHighBonusEnchantments = isAllowHighBonusEnchantments,
                isSafeRelationEnchantments = isSafeRelationEnchantments,
                isAllowHighRelationEnchantments = isAllowHighRelationEnchantments
            )
        }
    }
}
