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

import io.pixeloutlaw.minecraft.spigot.mythicdrops.displayName
import io.pixeloutlaw.minecraft.spigot.mythicdrops.lore
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.Repairable

internal fun ItemStack.setUnsafeEnchantments(enchantments: Map<Enchantment, Int>) {
    this.enchantments.keys.toSet().forEach { removeEnchantment(it) }
    addUnsafeEnchantments(enchantments)
}

internal const val DEFAULT_REPAIR_COST = 1000

internal fun <R> ItemStack.getFromItemMetaAsDamageable(action: Damageable.() -> R): R? {
    return (this.itemMeta as? Damageable)?.run(action)
}

internal fun ItemStack.getThenSetItemMetaAsDamageable(action: Damageable.() -> Unit) {
    (this.itemMeta as? Damageable)?.let {
        action(it)
        this.itemMeta = it
    }
}

internal fun <R> ItemStack.getFromItemMetaAsRepairable(action: Repairable.() -> R): R? {
    return (this.itemMeta as? Repairable)?.run(action)
}

internal fun ItemStack.getThenSetItemMetaAsRepairable(action: Repairable.() -> Unit) {
    (this.itemMeta as? Repairable)?.let {
        action(it)
        this.itemMeta = it
    }
}

@JvmOverloads
internal fun ItemStack.setRepairCost(cost: Int = DEFAULT_REPAIR_COST) =
    getThenSetItemMetaAsRepairable { this.repairCost = cost }

internal fun ItemStack.setDisplayNameChatColorized(string: String) {
    displayName = string.chatColorize()
}

internal fun ItemStack.setLoreChatColorized(strings: List<String>) {
    lore = strings.chatColorize()
}
