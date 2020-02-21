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

import com.squareup.moshi.JsonClass
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairCost
import com.tealcube.minecraft.bukkit.mythicdrops.setDisplayNameChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.setLoreChatColorized
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

@JsonClass(generateAdapter = true)
data class MythicRepairCost(
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
    override fun toItemStack(amount: Int): ItemStack = ItemStack(material, amount).let {
        if (itemName != null) {
            it.setDisplayNameChatColorized(itemName)
        }
        if (itemLore != null) {
            it.setLoreChatColorized(itemLore)
        }
        it.enchantments.forEach { (enchantment, _) -> it.removeEnchantment(enchantment) }
        if (enchantments != null) {
            it.addUnsafeEnchantments(enchantments)
        }
        it
    }
}
