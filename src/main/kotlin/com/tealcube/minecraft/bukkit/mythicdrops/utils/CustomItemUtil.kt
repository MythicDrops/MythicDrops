/*
 * The MIT License
 * Copyright © 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.utils

import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.items.CustomItemMap
import com.tealcube.minecraft.bukkit.mythicdrops.items.getThenSetItemMetaAsDamageable
import com.tealcube.minecraft.bukkit.mythicdrops.items.setDisplayNameChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.items.setLoreChatColorized
import io.pixeloutlaw.minecraft.spigot.hilt.setCustomModelData
import io.pixeloutlaw.minecraft.spigot.hilt.setUnbreakable
import org.bukkit.inventory.ItemStack

object CustomItemUtil {
    fun getCustomItemFromItemStack(itemStack: ItemStack): CustomItem? {
        return CustomItemMap.getInstance().values.find { it.toItemStack().isSimilar(itemStack) }
    }

    fun getItemStackFromCustomItem(customItem: CustomItem): ItemStack {
        val itemStack = ItemStack(customItem.material, 1)
        val enchantments = customItem.enchantments.map { it.enchantment to it.getRandomLevel() }.toMap()
        if (customItem.hasDurability()) {
            itemStack.getThenSetItemMetaAsDamageable {
                damage = customItem.durability.toInt()
            }
        }
        if (customItem.hasCustomModelData()) {
            itemStack.setCustomModelData(customItem.customModelData)
        }
        itemStack.setDisplayNameChatColorized(customItem.displayName)
        itemStack.setLoreChatColorized(customItem.lore)
        itemStack.addUnsafeEnchantments(enchantments)
        itemStack.setUnbreakable(customItem.isUnbreakable)
        return itemStack
    }
}
