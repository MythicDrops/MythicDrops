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
package com.tealcube.minecraft.bukkit.mythicdrops.identification

import com.google.common.base.Joiner
import com.tealcube.minecraft.bukkit.mythicdrops.DEFAULT_REPAIR_COST
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.CreatureSpawningSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.identification.items.UnidentifiedItemOptions
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.getThenSetItemMetaAsDamageable
import com.tealcube.minecraft.bukkit.mythicdrops.replaceArgs
import com.tealcube.minecraft.bukkit.mythicdrops.setDisplayNameChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.setLoreChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.setRepairCost
import com.tealcube.minecraft.bukkit.mythicdrops.trimEmpty
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemUtil
import org.apache.commons.text.WordUtils
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

class UnidentifiedItem @JvmOverloads constructor(
    material: Material,
    unidentifiedItemOptions: UnidentifiedItemOptions,
    displayNames: Map<String, String>,
    allowableTiers: Collection<Tier> = emptyList(),
    droppedBy: EntityType? = null,
    tier: Tier? = null,
    amount: Int = 1,
    durability: Short = 0
) : ItemStack(material, amount) {
    companion object {
        @JvmOverloads
        @JvmStatic
        fun build(
            creatureSpawningSettings: CreatureSpawningSettings,
            displayNames: Map<String, String>,
            material: Material,
            tierManager: TierManager,
            unidentifiedItemOptions: UnidentifiedItemOptions,
            droppedBy: EntityType? = null,
            tier: Tier? = null
        ): UnidentifiedItem {
            val tiersForMaterial = ItemUtil.getTiersFromMaterial(material)
            val tiersForEntityType = droppedBy?.let { entityType ->
                creatureSpawningSettings.tierDrops[entityType]?.mapNotNull { tierManager.getById(it) }
            } ?: emptyList()
            val allowableTiers = if (droppedBy != null) {
                tiersForMaterial.union(tiersForEntityType)
            } else {
                tiersForMaterial
            }
            return UnidentifiedItem(material, unidentifiedItemOptions, displayNames, allowableTiers, droppedBy, tier)
        }
    }

    init {
        getThenSetItemMetaAsDamageable({ damage = durability.toInt() }, { this.durability = durability })
        setDisplayNameChatColorized(unidentifiedItemOptions.name)
        val allowableTiersJoined =
            Joiner.on(unidentifiedItemOptions.allowableTiersSeparator).join(allowableTiers.map { it.displayName })
        val allowableTiersLore = if (allowableTiers.isNotEmpty()) {
            "${unidentifiedItemOptions.allowableTiersPrefix}$allowableTiersJoined${unidentifiedItemOptions.allowableTiersSuffix}"
        } else {
            ""
        }
        val droppedByLore = droppedBy?.let {
            val fromLocalization = displayNames[it.name]
            val prettyEntityTypeName = Joiner.on(" ").join(it.name.split("_").map(WordUtils::capitalizeFully))
            "${unidentifiedItemOptions.droppedByPrefix}${fromLocalization
                ?: prettyEntityTypeName}${unidentifiedItemOptions.droppedBySuffix}"
        } ?: ""
        val tierLore = tier?.let {
            "${unidentifiedItemOptions.tierPrefix}${it.displayName}${unidentifiedItemOptions.tierSuffix}"
        } ?: ""
        val lore = unidentifiedItemOptions.lore.replaceArgs(
            "%droppedby%" to droppedByLore,
            "%allowabletiers%" to allowableTiersLore,
            "%tier%" to tierLore
        ).trimEmpty()
        setLoreChatColorized(lore)
        setRepairCost(DEFAULT_REPAIR_COST)
    }
}
