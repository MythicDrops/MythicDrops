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
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairItem
import com.tealcube.minecraft.bukkit.mythicdrops.getMaterial
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.utils.AirUtil
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection

internal data class MythicRepairItem @JvmOverloads constructor(
    override val name: String,
    override val material: Material,
    override val itemName: String?,
    override val itemLore: List<String>,
    override val repairCosts: List<RepairCost> = emptyList()
) : RepairItem {
    companion object {
        fun fromConfigurationSection(
            configurationSection: ConfigurationSection,
            key: String,
            loadingErrorManager: LoadingErrorManager
        ): MythicRepairItem? {
            val itemMaterial = configurationSection.getMaterial("material-name", Material.AIR)
            if (AirUtil.isAir(itemMaterial)) {
                loadingErrorManager.add("Not loading repair item $key as it has an invalid material name")
                return null
            }

            val itemName = configurationSection.getString("item-name")
            val itemLore = configurationSection.getStringList("item-lore")
            val costsConfigurationSection = configurationSection.getOrCreateSection("costs")
            val repairCosts = costsConfigurationSection.getKeys(false).mapNotNull { repairCostKey ->
                if (!costsConfigurationSection.isConfigurationSection(repairCostKey)) {
                    return@mapNotNull null
                }
                val repairCostSection = costsConfigurationSection.getOrCreateSection(repairCostKey)
                MythicRepairCost.fromConfigurationSection(repairCostSection, repairCostKey, loadingErrorManager)
            }

            return MythicRepairItem(
                name = key,
                material = itemMaterial,
                itemName = itemName,
                itemLore = itemLore,
                repairCosts = repairCosts
            )
        }
    }

    override fun addRepairCosts(vararg repairCost: RepairCost): RepairItem =
        copy(repairCosts = repairCosts.plus(repairCost))

    override fun removeRepairCosts(vararg name: String): RepairItem =
        copy(repairCosts = repairCosts.filter { it.name !in name })
}
