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
package io.pixeloutlaw.minecraft.spigot.mythicdrops

import org.bukkit.NamespacedKey
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.Locale

/**
 * Attempts to get the highest enchantment off the ItemStack. Returns null if no enchantments are present.
 */
internal fun ItemStack.getHighestEnchantment(): Enchantment? = enchantments.maxByOrNull { it.value }?.key

/**
 * Clones this ItemStack with default attribute modifiers. Only returns null if unable to retrieve/create ItemMeta.
 */
internal fun ItemStack.cloneWithDefaultAttributes(): ItemStack {
    val cloned = clone()
    val originalItemMeta = itemMeta ?: return cloned
    val clonedItemMeta = originalItemMeta.clone()
    if (type.isItem) {
        EquipmentSlot.entries.forEach { slot ->
            type
                .getDefaultAttributeModifiers(slot)
                .entries()
                .forEach {
                    val defaultModifier = it.value ?: return@forEach
                    val baseKey = defaultModifier.key
                    val modifiedModifierKey =
                        NamespacedKey.minecraft(
                            "${baseKey.key}.${it.key.toString().lowercase()}.${
                                slot.name.lowercase(
                                    Locale.ROOT
                                )
                            }"
                        )
                    val modifiedModifier =
                        AttributeModifier(modifiedModifierKey, it.value.amount, it.value.operation, it.value.slotGroup)
                    clonedItemMeta.addAttributeModifier(it.key, modifiedModifier)
                }
        }
    }
    cloned.itemMeta = clonedItemMeta
    return cloned
}

/**
 * Clones the current ItemStack with default attribute modifiers unless it already has attributes added.
 */
internal fun ItemStack.conditionallyCloneWithDefaultAttributes(): ItemStack {
    if (itemMeta?.hasAttributeModifiers() == true) {
        return clone()
    }
    return cloneWithDefaultAttributes()
}
