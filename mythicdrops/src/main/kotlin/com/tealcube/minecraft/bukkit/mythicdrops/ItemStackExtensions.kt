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

import io.pixeloutlaw.minecraft.spigot.hilt.setDisplayName
import io.pixeloutlaw.minecraft.spigot.hilt.setLore
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.Repairable

fun ItemStack.setUnsafeEnchantments(enchantments: Map<Enchantment, Int>) {
    this.enchantments.keys.toSet().forEach { removeEnchantment(it) }
    addUnsafeEnchantments(enchantments)
}

const val DEFAULT_REPAIR_COST = 1000

fun <R> ItemStack.getFromItemMetaAsDamageable(action: Damageable.() -> R, backup: (ItemStack.() -> R)? = null): R? {
    return try {
        (this.itemMeta as? Damageable)?.run(action)
    } catch (ignored: NoClassDefFoundError) {
        return backup?.let { this.run(it) }
    }
}

fun ItemStack.getThenSetItemMetaAsDamageable(action: Damageable.() -> Unit, backup: (ItemStack.() -> Unit)? = null) {
    try {
        (this.itemMeta as? Damageable)?.let {
            action(it)
            this.itemMeta = it as ItemMeta
        }
    } catch (ignored: NoClassDefFoundError) {
        backup?.let { this.run(it) }
    }
}

fun ItemStack.getThenSetItemMetaAsRepairable(action: Repairable.() -> Unit) {
    (this.itemMeta as? Repairable)?.let {
        action(it)
        this.itemMeta = it as ItemMeta
    }
}

@JvmOverloads
fun ItemStack.setRepairCost(cost: Int = DEFAULT_REPAIR_COST) = getThenSetItemMetaAsRepairable { this.repairCost = cost }

fun ItemStack.setDisplayNameChatColorized(string: String) = setDisplayName(string.chatColorize())
fun ItemStack.setLoreChatColorized(strings: List<String>) = setLore(strings.chatColorize())
