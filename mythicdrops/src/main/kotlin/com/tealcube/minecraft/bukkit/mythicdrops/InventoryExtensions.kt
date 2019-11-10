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
package com.tealcube.minecraft.bukkit.mythicdrops

import io.pixeloutlaw.minecraft.spigot.hilt.getDisplayName
import io.pixeloutlaw.minecraft.spigot.hilt.getLore
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.Inventory

fun Inventory.isSlotEmpty(slot: Int): Boolean {
    val itemInSlot = getItem(slot)
    return itemInSlot == null || itemInSlot.type == Material.AIR
}

fun Inventory.containsAtLeast(
    itemName: String? = null,
    itemLore: List<String>? = null,
    itemEnchantments: Map<Enchantment, Int>? = null,
    amount: Int = 1
): Boolean {
    var amountToFind = amount
    if (amount <= 0) {
        return true
    }
    for (item in storageContents) {
        if (item == null || item.type == Material.AIR) {
            continue
        }
        val doesItemNameMatch = itemName == null || item.getDisplayName() == itemName.chatColorize()
        val doesItemLoreMatch = itemLore == null || item.getLore() == itemLore.chatColorize()
        val doesItemEnchantmentsMatch = itemEnchantments == null || itemEnchantments == item.enchantments
        if (doesItemNameMatch && doesItemLoreMatch && doesItemEnchantmentsMatch) {
            amountToFind -= item.amount
            if (amountToFind <= 0) {
                return true
            }
        }
    }
    return false
}

fun Inventory.removeItem(
    itemName: String? = null,
    itemLore: List<String>? = null,
    itemEnchantments: Map<Enchantment, Int>? = null,
    amount: Int = 1
) {
    var toDelete = amount

    while (true) {
        val firstSlot = firstSlot(itemName, itemLore, itemEnchantments)

        // Drat! we don't have this type in the inventory
        if (firstSlot == -1) {
            break
        }
        val itemAtFirstSlot = getItem(firstSlot) ?: break
        val amountAtFirstSlot = itemAtFirstSlot.amount
        if (amountAtFirstSlot <= toDelete) {
            toDelete -= amountAtFirstSlot
            clear(firstSlot)
        } else {
            itemAtFirstSlot.amount = (amountAtFirstSlot - toDelete)
            setItem(firstSlot, itemAtFirstSlot)
            toDelete = 0
        }

        // Bail when done
        if (toDelete <= 0) {
            break
        }
    }
}

fun Inventory.firstSlot(
    itemName: String? = null,
    itemLore: List<String>? = null,
    itemEnchantments: Map<Enchantment, Int>? = null
): Int {
    return storageContents.indexOfFirst {
        if (it == null || it.type == Material.AIR) {
            false
        } else {
            val doesItemNameMatch = itemName == null || it.getDisplayName() == itemName.chatColorize()
            val doesItemLoreMatch = itemLore == null || it.getLore() == itemLore.chatColorize()
            val doesItemEnchantmentsMatch = itemEnchantments == null || itemEnchantments == it.enchantments
            doesItemNameMatch && doesItemLoreMatch && doesItemEnchantmentsMatch
        }
    }
}
