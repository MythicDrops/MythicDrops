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
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.identification.items.UnidentifiedItemOptions
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.items.DEFAULT_REPAIR_COST
import com.tealcube.minecraft.bukkit.mythicdrops.items.getThenSetItemMetaAsDamageable
import com.tealcube.minecraft.bukkit.mythicdrops.items.setDisplayNameChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.items.setLoreChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.items.setRepairCost
import com.tealcube.minecraft.bukkit.mythicdrops.replaceArgs
import com.tealcube.minecraft.bukkit.mythicdrops.trimEmpty
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

class UnidentifiedItem @JvmOverloads constructor(
    material: Material,
    unidentifiedItemOptions: UnidentifiedItemOptions,
    allowableTiers: List<Tier> = emptyList(),
    droppedBy: EntityType? = null,
    tier: Tier? = null,
    amount: Int = 1,
    durability: Short = 0
) : ItemStack(material, amount) {
    init {
        getThenSetItemMetaAsDamageable { damage = durability.toInt() }
        setDisplayNameChatColorized(unidentifiedItemOptions.name)
        val allowableTiersJoined =
            Joiner.on(unidentifiedItemOptions.allowableTiersSeparator).join(allowableTiers.map { it.name })
        val allowableTiersLore = if (allowableTiers.isNotEmpty()) {
            "${unidentifiedItemOptions.allowableTiersPrefix}$allowableTiersJoined${unidentifiedItemOptions.allowableTiersSuffix}"
        } else {
            ""
        }
        val droppedByLore = droppedBy?.let {
            "${unidentifiedItemOptions.droppedByPrefix}${it.name}${unidentifiedItemOptions.droppedBySuffix}"
        } ?: ""
        val tierLore = tier?.let {
            "${unidentifiedItemOptions.tierPrefix}${it.name}${unidentifiedItemOptions.tierSuffix}"
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
