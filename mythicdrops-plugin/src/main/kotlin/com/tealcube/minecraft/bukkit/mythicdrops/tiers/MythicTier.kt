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

import com.tealcube.minecraft.bukkit.mythicdrops.DEFAULT_REPAIR_COST
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierAttributes
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierEnchantments
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierItemTypes
import com.tealcube.minecraft.bukkit.mythicdrops.getChatColor
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import io.pixeloutlaw.minecraft.spigot.mythicdrops.enumValueOrNull
import org.bukkit.ChatColor
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemFlag

internal data class MythicTier(
    override val name: String = "",
    override val displayName: String = "",
    @Deprecated(
        "Not used anymore, replaced by itemDisplayNameFormat",
        replaceWith = ReplaceWith("itemDisplayNameFormat")
    )
    override val displayColor: ChatColor = ChatColor.RESET,
    @Deprecated(
        "Not used anymore, replaced by itemDisplayNameFormat",
        replaceWith = ReplaceWith("itemDisplayNameFormat")
    )
    override val identifierColor: ChatColor = ChatColor.RESET,
    override val baseLore: List<String> = emptyList(),
    override val bonusLore: List<String> = emptyList(),
    override val minimumBonusLore: Int = 0,
    override val maximumBonusLore: Int = 0,
    override val enchantments: TierEnchantments = MythicTierEnchantments(),
    override val minimumDurabilityPercentage: Double = 0.0,
    override val maximumDurabilityPercentage: Double = 0.0,
    override val chanceToDropOnMonsterDeath: Double = 0.0,
    override val itemTypes: TierItemTypes = MythicTierItemTypes(),
    override val minimumDistanceFromSpawn: Int = -1,
    override val maximumDistanceFromSpawn: Int = -1,
    override val isUnbreakable: Boolean = false,
    override val identityWeight: Double = 0.0,
    override val weight: Double = 0.0,
    override val chanceToHaveSockets: Double = 0.0,
    override val minimumSockets: Int = 0,
    override val maximumSockets: Int = 0,
    override val isBroadcastOnFind: Boolean = false,
    override val attributes: TierAttributes = MythicTierAttributes(),
    override val itemDisplayNameFormat: String = "",
    override val tooltipFormat: List<String>? = null,
    override val itemFlags: Set<ItemFlag> = emptySet(),
    override val chanceToHaveSocketExtenderSlots: Double = 0.0,
    override val minimumSocketExtenderSlots: Int = 0,
    override val maximumSocketExtenderSlots: Int = 0,
    override val repairCost: Int = DEFAULT_REPAIR_COST
) : Tier {
    companion object {
        @JvmStatic
        fun fromConfigurationSection(
            configurationSection: ConfigurationSection,
            key: String,
            itemGroupManager: ItemGroupManager
        ): MythicTier {
            val itemTypesSection = configurationSection.getOrCreateSection("item-types")
            val tierItemTypes = MythicTierItemTypes.fromConfigurationSection(itemTypesSection, itemGroupManager)
            val attributesSection = configurationSection.getOrCreateSection("attributes")
            val tierAttributes = MythicTierAttributes.fromConfigurationSection(attributesSection)
            val enchantmentsSection = configurationSection.getOrCreateSection("enchantments")
            val tierEnchantments = MythicTierEnchantments.fromConfigurationSection(enchantmentsSection)
            val itemDisplayNameFormat = configurationSection.getNonNullString("item-display-name-format")
            val tooltipFormat = if (configurationSection.isList("tooltip-format")) {
                configurationSection.getStringList("tooltip-format").filterNotNull().toList()
            } else {
                null
            }
            val itemFlags = configurationSection.getStringList("item-flags").mapNotNull {
                enumValueOrNull<ItemFlag>(it)
            }.toSet()
            return MythicTier(
                name = key,
                displayName = configurationSection.getNonNullString("display-name", key),
                displayColor = ChatColor.RESET,
                identifierColor = ChatColor.RESET,
                baseLore = configurationSection.getStringList("lore.base-lore"),
                bonusLore = configurationSection.getStringList("lore.bonus-lore"),
                minimumBonusLore = configurationSection.getInt("lore.minimum-bonus-lore"),
                maximumBonusLore = configurationSection.getInt("lore.maximum-bonus-lore"),
                minimumDurabilityPercentage = configurationSection.getDouble("minimum-durability"),
                maximumDurabilityPercentage = configurationSection.getDouble("maximum-durability"),
                chanceToDropOnMonsterDeath = configurationSection.getDouble("chance-to-drop-on-monster-death"),
                itemTypes = tierItemTypes,
                minimumDistanceFromSpawn = configurationSection.getInt("minimum-distance-from-spawn", -1),
                maximumDistanceFromSpawn = configurationSection.getInt("maximum-distance-from-spawn", -1),
                isUnbreakable = configurationSection.getBoolean("unbreakable"),
                weight = configurationSection.getDouble("weight"),
                identityWeight = configurationSection.getDouble("identity-weight"),
                chanceToHaveSockets = configurationSection.getDouble("chance-to-have-sockets"),
                minimumSockets = configurationSection.getInt("minimum-sockets"),
                maximumSockets = configurationSection.getInt("maximum-sockets"),
                isBroadcastOnFind = configurationSection.getBoolean("broadcast-on-find"),
                attributes = tierAttributes,
                enchantments = tierEnchantments,
                itemDisplayNameFormat = itemDisplayNameFormat,
                tooltipFormat = tooltipFormat,
                itemFlags = itemFlags,
                chanceToHaveSocketExtenderSlots = configurationSection.getDouble(
                    "chance-to-have-socket-extender-slots"
                ),
                minimumSocketExtenderSlots = configurationSection.getInt("minimum-socket-extender-slots"),
                maximumSocketExtenderSlots = configurationSection.getInt("maximum-socket-extender-slots"),
                repairCost = configurationSection.getInt("repair-cost", DEFAULT_REPAIR_COST)
            )
        }

        @JvmStatic
        fun fromConfigurationSection(
            configurationSection: ConfigurationSection
        ): MythicTier {
            val displayColor = configurationSection.getChatColor("display-color") ?: ChatColor.RESET
            val identifierColor = configurationSection.getChatColor("identifier-color") ?: ChatColor.RESET
            val tierItemTypes = MythicTierItemTypes()
            val attributesSection = configurationSection.getOrCreateSection("attributes")
            val tierAttributes = MythicTierAttributes.fromConfigurationSection(attributesSection)
            val enchantmentsSection = configurationSection.getOrCreateSection("enchantments")
            val tierEnchantments = MythicTierEnchantments.fromConfigurationSection(enchantmentsSection)
            val itemDisplayNameFormat = configurationSection.getNonNullString("item-display-name-format")
            val tooltipFormat = if (configurationSection.isList("tooltip-format")) {
                configurationSection.getStringList("tooltip-format").filterNotNull().toList()
            } else {
                null
            }
            val itemFlags = configurationSection.getStringList("item-flags").mapNotNull {
                enumValueOrNull<ItemFlag>(it)
            }.toSet()
            return MythicTier(
                name = "",
                displayName = configurationSection.getNonNullString("display-name"),
                displayColor = displayColor,
                identifierColor = identifierColor,
                baseLore = configurationSection.getStringList("lore.base-lore"),
                bonusLore = configurationSection.getStringList("lore.bonus-lore"),
                minimumBonusLore = configurationSection.getInt("lore.minimum-bonus-lore"),
                maximumBonusLore = configurationSection.getInt("lore.maximum-bonus-lore"),
                minimumDurabilityPercentage = configurationSection.getDouble("minimum-durability"),
                maximumDurabilityPercentage = configurationSection.getDouble("maximum-durability"),
                chanceToDropOnMonsterDeath = configurationSection.getDouble("chance-to-drop-on-monster-death"),
                itemTypes = tierItemTypes,
                minimumDistanceFromSpawn = configurationSection.getInt("minimum-distance-from-spawn", -1),
                maximumDistanceFromSpawn = configurationSection.getInt("maximum-distance-from-spawn", -1),
                isUnbreakable = configurationSection.getBoolean("unbreakable"),
                weight = configurationSection.getDouble("weight"),
                identityWeight = configurationSection.getDouble("identity-weight"),
                chanceToHaveSockets = configurationSection.getDouble("chance-to-have-sockets"),
                minimumSockets = configurationSection.getInt("minimum-sockets"),
                maximumSockets = configurationSection.getInt("maximum-sockets"),
                isBroadcastOnFind = configurationSection.getBoolean("broadcast-on-find"),
                attributes = tierAttributes,
                enchantments = tierEnchantments,
                itemDisplayNameFormat = itemDisplayNameFormat,
                tooltipFormat = tooltipFormat,
                itemFlags = itemFlags,
                chanceToHaveSocketExtenderSlots = configurationSection.getDouble(
                    "chance-to-have-socket-extender-slots"
                ),
                minimumSocketExtenderSlots = configurationSection.getInt("minimum-socket-extender-slots"),
                maximumSocketExtenderSlots = configurationSection.getInt("maximum-socket-extender-slots"),
                repairCost = configurationSection.getInt("repair-cost", DEFAULT_REPAIR_COST)
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
