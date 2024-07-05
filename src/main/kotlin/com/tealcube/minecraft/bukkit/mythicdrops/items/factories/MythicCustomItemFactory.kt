/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2021 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.items.factories

import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories.CustomItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.getThenSetItemMetaAsDamageable
import com.tealcube.minecraft.bukkit.mythicdrops.hdb.HeadDatabaseAdapter
import com.tealcube.minecraft.bukkit.mythicdrops.setDisplayNameChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.setLoreChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.setRepairCost
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TemplatingUtil
import io.pixeloutlaw.minecraft.spigot.mythicdrops.addAttributeModifier
import io.pixeloutlaw.minecraft.spigot.mythicdrops.cloneWithDefaultAttributes
import io.pixeloutlaw.minecraft.spigot.mythicdrops.customModelData
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getThenSetItemMeta
import io.pixeloutlaw.minecraft.spigot.mythicdrops.isUnbreakable
import io.pixeloutlaw.minecraft.spigot.mythicdrops.itemFlags
import io.pixeloutlaw.minecraft.spigot.mythicdrops.mythicDropsCustomItem
import io.pixeloutlaw.minecraft.spigot.mythicdrops.setPersistentDataString
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta

/**
 * Implementation of [CustomItemFactory] that enables the implementation of [CustomItem] to change
 * without impacting the actual API design of [CustomItem]'s [CustomItem.toItemStack].
 */
internal class MythicCustomItemFactory(
    private val headDatabaseAdapter: HeadDatabaseAdapter
) : CustomItemFactory {
    override fun toItemStack(customItem: CustomItem): ItemStack {
        val originalItemStack =
            if (customItem.material == Material.PLAYER_HEAD && customItem.hdbId.isNotBlank()) {
                headDatabaseAdapter.getItemFromId(customItem.hdbId)
            } else {
                ItemStack(customItem.material)
            }
        val itemStack =
            if (customItem.isAddDefaultAttributes) {
                originalItemStack.cloneWithDefaultAttributes()
            } else {
                originalItemStack
            }
        if (customItem.hasDurability) {
            itemStack.getThenSetItemMetaAsDamageable {
                damage = customItem.durability
            }
        }
        if (customItem.hasCustomModelData) {
            itemStack.customModelData = customItem.customModelData
        }
        if (customItem.displayName.isNotBlank()) {
            itemStack.setDisplayNameChatColorized(customItem.displayName)
        }
        itemStack.setLoreChatColorized(customItem.lore.map(TemplatingUtil::template))
        itemStack.addUnsafeEnchantments(customItem.enchantments.associate { it.enchantment to it.getRandomLevel() })
        if (customItem.isGlow) {
            itemStack.getThenSetItemMeta {
                setEnchantmentGlintOverride(true)
            }
        }
        itemStack.isUnbreakable = customItem.isUnbreakable
        if (customItem.repairCost >= 0) {
            itemStack.setRepairCost(customItem.repairCost)
        }
        customItem.attributes.forEach {
            val (attribute, attributeModifier) = it.toAttributeModifier()
            itemStack.addAttributeModifier(attribute, attributeModifier)
        }
        itemStack.itemFlags = customItem.itemFlags
        itemStack.setPersistentDataString(mythicDropsCustomItem, customItem.name)

        if (!customItem.rgb.isEmpty() && itemStack.itemMeta is LeatherArmorMeta) {
            val meta = itemStack.itemMeta as LeatherArmorMeta
            meta.setColor(customItem.rgb.toColor())
            itemStack.itemMeta = meta
        }

        return itemStack
    }
}
