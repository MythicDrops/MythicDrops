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

import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroup
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierItemTypes
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection

data class MythicTierItemTypes(
    override val allowedItemGroups: Set<ItemGroup> = emptySet(),
    override val disallowedItemGroups: Set<ItemGroup> = emptySet(),
    override val allowedMaterialIds: Set<Material> = emptySet(),
    override val disallowedMaterialIds: Set<Material> = emptySet()
) : TierItemTypes {
    companion object {
        fun fromConfigurationSection(
            configurationSection: ConfigurationSection,
            itemGroupManager: ItemGroupManager
        ): MythicTierItemTypes {
            val allowedItemGroups = configurationSection.getStringList("allowed-groups").mapNotNull {
                itemGroupManager.getById(it)
            }
            val disallowedItemGroups = configurationSection.getStringList("disallowed-groups").mapNotNull {
                itemGroupManager.getById(it)
            }
            val allowedMaterialIds = configurationSection.getStringList("allowed-material-ids").mapNotNull {
                Material.getMaterial(it)
            }
            val disallowedMaterialIds =
                configurationSection.getStringList("disallowed-material-ids").mapNotNull {
                    Material.getMaterial(it)
                }
            return MythicTierItemTypes(
                allowedItemGroups = allowedItemGroups.toSet(),
                disallowedItemGroups = disallowedItemGroups.toSet(),
                allowedMaterialIds = allowedMaterialIds.toSet(),
                disallowedMaterialIds = disallowedMaterialIds.toSet()
            )
        }
    }
}
