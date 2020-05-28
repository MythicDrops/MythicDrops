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
package com.tealcube.minecraft.bukkit.mythicdrops.api.items

import com.tealcube.minecraft.bukkit.mythicdrops.api.attributes.MythicAttribute
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.CustomEnchantmentRegistry
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment
import com.tealcube.minecraft.bukkit.mythicdrops.api.weight.Weighted
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * Represents a custom item from the customItems.yml. Names map almost one-to-one, with a couple of exceptions,
 * notably [hasDurability] and [hasCustomModelData]. Those should return true if the custom item should use the
 * respective values in [toItemStack].
 */
interface CustomItem : Weighted {
    val name: String
    val displayName: String
    val chanceToDropOnDeath: Double
    val enchantments: Set<MythicEnchantment>
    val lore: List<String>
    val material: Material
    val isBroadcastOnFind: Boolean
    val hasDurability: Boolean
    val durability: Int
    val hasCustomModelData: Boolean
    val customModelData: Int
    val isUnbreakable: Boolean
    val attributes: Set<MythicAttribute>
    val isGlow: Boolean

    @Deprecated("Requires customEnchantmentRegistry parameter.", ReplaceWith("toItemStack(customEnchantmentRegistry)"))
    fun toItemStack(): ItemStack
    fun toItemStack(customEnchantmentRegistry: CustomEnchantmentRegistry): ItemStack
}
