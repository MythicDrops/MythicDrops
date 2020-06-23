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
package com.tealcube.minecraft.bukkit.mythicdrops.utils

import com.tealcube.minecraft.bukkit.mythicdrops.api.choices.WeightedChoice
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.CreatureSpawningSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.identification.items.UnidentifiedItemOptions
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.enumValueOrNull
import com.tealcube.minecraft.bukkit.mythicdrops.stripColors
import org.bukkit.entity.EntityType

object IdentifyingUtil {
    /**
     * priority order is allowableTiers > droppedBy > potentialTierFromLastLoreLine > tiersFromMaterial
     */
    fun determineTierForIdentify(
        creatureSpawningSettings: CreatureSpawningSettings,
        tierManager: TierManager,
        allowableTiers: Collection<Tier>?,
        droppedBy: EntityType?,
        tiersFromMaterial: Collection<Tier>,
        potentialTierFromLastLoreLine: Tier?
    ): Tier? {
        return allowableTiers?.let {
            WeightedChoice.between(allowableTiers).choose()
        } ?: droppedBy?.let {
            WeightedChoice.between(
                (creatureSpawningSettings.tierDrops[it]
                    ?: emptyList()).mapNotNull { tierName -> tierManager.getByName(tierName) }).choose()
        } ?: potentialTierFromLastLoreLine ?: WeightedChoice.between(tiersFromMaterial).choose()
    }

    fun getAllowableTiers(
        lore: List<String>,
        unidentifiedItemOptions: UnidentifiedItemOptions,
        tierManager: TierManager
    ): List<Tier>? {
        val allowableTiersPrefixStripped =
            unidentifiedItemOptions.allowableTiersPrefix.chatColorize().stripColors()
        val allowableTiersSuffixStripped =
            unidentifiedItemOptions.allowableTiersSuffix.chatColorize().stripColors()
        val allowableTiersLoreLine =
            findLoreLineWithPrefixAndSuffix(lore, allowableTiersPrefixStripped, allowableTiersSuffixStripped)
                ?: return null

        val allowableTiersStrings = allowableTiersLoreLine.chatColorize()
            .stripColors()
            .replace(allowableTiersPrefixStripped, "")
            .replace(allowableTiersSuffixStripped, "")
            .split(unidentifiedItemOptions.allowableTiersSeparator.chatColorize().stripColors())
        return allowableTiersStrings.mapNotNull { tierManager.getByName(it) }
    }

    fun getUnidentifiedItemDroppedBy(
        lore: List<String>,
        unidentifiedItemOptions: UnidentifiedItemOptions,
        displayNames: Map<String, String>
    ): EntityType? {
        val droppedByPrefixStripped =
            unidentifiedItemOptions.droppedByPrefix.chatColorize().stripColors()
        val droppedBySuffixStripped =
            unidentifiedItemOptions.droppedBySuffix.chatColorize().stripColors()
        val entityTypeLoreLine =
            findLoreLineWithPrefixAndSuffix(lore, droppedByPrefixStripped, droppedBySuffixStripped) ?: return null

        val entityTypeNameFromLore = entityTypeLoreLine.chatColorize()
            .stripColors()
            .replace(droppedByPrefixStripped, "")
            .replace(droppedBySuffixStripped, "")
        val entityTypeName = entityTypeNameFromLore.replace(" ", "_")
            .toUpperCase()
        val entityTypeNameKey = displayNames.filterValues { it == entityTypeNameFromLore }.keys.firstOrNull()

        return enumValueOrNull<EntityType>(entityTypeNameKey) ?: enumValueOrNull<EntityType>(entityTypeName)
    }

    private fun findLoreLineWithPrefixAndSuffix(
        lore: List<String>,
        prefixStripped: String,
        suffixStripped: String
    ): String? {
        if (lore.isEmpty()) {
            return null
        }

        return lore.find { lineOfLore ->
            val lineOfLoreStripped = lineOfLore.chatColorize().stripColors()
            lineOfLoreStripped.startsWith(prefixStripped) &&
                lineOfLoreStripped.endsWith(suffixStripped)
        }
    }
}
