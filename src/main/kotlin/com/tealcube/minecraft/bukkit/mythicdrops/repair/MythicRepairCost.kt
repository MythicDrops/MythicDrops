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
package com.tealcube.minecraft.bukkit.mythicdrops.repair

import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairCost
import com.tealcube.minecraft.bukkit.mythicdrops.getMaterial
import com.tealcube.minecraft.bukkit.mythicdrops.utils.AirUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.EnchantmentUtil.getByKeyOrName
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment

internal data class MythicRepairCost(
    override val itemLore: List<String>?,
    override val itemName: String?,
    override val material: Material,
    override val amount: Int,
    override val repairPercentagePerCost: Double,
    override val experienceCost: Int,
    override val priority: Int,
    override val name: String,
    override val enchantments: Map<Enchantment, Int>?
) : RepairCost {
    companion object {
        fun fromConfigurationSection(
            configurationSection: ConfigurationSection,
            key: String,
            loadingErrorManager: LoadingErrorManager
        ): MythicRepairCost? {
            val itemMaterial = configurationSection.getMaterial("material-name", Material.AIR)
            if (AirUtil.isAir(itemMaterial)) {
                loadingErrorManager.add("Not loading repair cost $key as it has an invalid material name")
                return null
            }

            val experienceCost = configurationSection.getInt("experience-cost", 0)
            val priority = configurationSection.getInt("priority", 0)
            val amount = configurationSection.getInt("amount", 1)
            val repairPerCost = configurationSection.getDouble("repair-per-cost", 0.1)
            val costName = configurationSection.getString("item-name")
            val costLore = if (configurationSection.isList("item-lore")) {
                configurationSection.getStringList("item-lore")
            } else {
                null
            }
            val enchantmentsSection = configurationSection.getConfigurationSection("enchantments")
            val enchantments = enchantmentsSection?.getKeys(false)?.mapNotNull { enchantmentKey ->
                val enchantment = getByKeyOrName(enchantmentKey)
                if (enchantment != null) {
                    enchantment to enchantmentsSection.getInt(enchantmentKey)
                } else {
                    null
                }
            }?.toMap()
            return MythicRepairCost(
                itemLore = costLore,
                itemName = costName,
                material = itemMaterial,
                amount = amount,
                repairPercentagePerCost = repairPerCost,
                experienceCost = experienceCost,
                priority = priority,
                name = key,
                enchantments = enchantments
            )
        }
    }
}
