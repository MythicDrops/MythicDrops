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
@file:Suppress("detekt.TooManyFunctions")
package io.pixeloutlaw.minecraft.spigot.mythicdrops

import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.CustomEnchantmentRegistry
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ChatColorUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.MinecraftVersionUtil
import io.pixeloutlaw.minecraft.spigot.hilt.getDisplayName
import io.pixeloutlaw.minecraft.spigot.hilt.getFromItemMeta
import io.pixeloutlaw.minecraft.spigot.hilt.getThenSetItemMeta
import org.bukkit.ChatColor
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

/**
 * Gets keys from the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
fun ItemStack.getPersistentDataKeys(namespace: String): List<NamespacedKey> {
    return if (MinecraftVersionUtil.isAtLeastMinecraft116()) {
        getFromItemMeta {
            persistentDataContainer.keys.filter { it.namespace.equals(namespace, ignoreCase = true) }
        } ?: emptyList()
    } else {
        emptyList()
    }
}

/**
 * Gets a nullable string from the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
fun ItemStack.getPersistentDataString(namespacedKey: NamespacedKey): String? {
    return if (MinecraftVersionUtil.isAtLeastMinecraft116()) {
        getFromItemMeta { persistentDataContainer.get(namespacedKey, org.bukkit.persistence.PersistentDataType.STRING) }
    } else {
        null
    }
}

/**
 * Gets a nullable boolean from the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
fun ItemStack.getPersistentDataBoolean(namespacedKey: NamespacedKey): Boolean? {
    return if (MinecraftVersionUtil.isAtLeastMinecraft116()) {
        getFromItemMeta {
            persistentDataContainer.get(
                namespacedKey,
                org.bukkit.persistence.PersistentDataType.STRING
            )
        }?.toBoolean()
    } else {
        null
    }
}

/**
 * Sets a nullable string in the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
fun ItemStack.setPersistentDataString(namespacedKey: NamespacedKey, value: String) {
    if (MinecraftVersionUtil.isAtLeastMinecraft116()) {
        getThenSetItemMeta {
            // we use the full class instead of the import in order to work better on < 1.16
            persistentDataContainer.set(namespacedKey, org.bukkit.persistence.PersistentDataType.STRING, value)
        }
    }
}

/**
 * Sets a nullable boolean in the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
fun ItemStack.setPersistentDataBoolean(namespacedKey: NamespacedKey, value: Boolean) {
    if (MinecraftVersionUtil.isAtLeastMinecraft116()) {
        getThenSetItemMeta {
            // we use the full class instead of the import in order to work better on < 1.16
            persistentDataContainer.set(namespacedKey, org.bukkit.persistence.PersistentDataType.STRING, "$value")
        }
    }
}

/**
 * Gets all of the [ItemFlag]s from the ItemMeta.
 */
fun ItemStack.getItemFlags(): Set<ItemFlag> = getFromItemMeta { itemFlags }?.toSet() ?: emptySet()

/**
 * Sets the [ItemFlag]s on the ItemMeta.
 *
 * @param itemFlags Flags to set
 */
fun ItemStack.setItemFlags(itemFlags: Set<ItemFlag>) {
    getThenSetItemMeta { itemFlags.forEach { addItemFlags(it) } }
}

/**
 * Attempts to get the highest enchantment off the ItemStack. Returns null if no enchantments are present.
 */
fun ItemStack.getHighestEnchantment(): Enchantment? {
    return enchantments.maxBy { it.value }?.key
}

/**
 * Attempts to get the custom item from this ItemStack that matches the custom items available in the CustomItemManager.
 */
fun ItemStack.getCustomItem(
    customItemManager: CustomItemManager,
    customEnchantmentRegistry: CustomEnchantmentRegistry
): CustomItem? = getCustomItem(customItemManager.get(), customEnchantmentRegistry)

/**
 * Attempts to get the custom item from this ItemStack that matches the given collection of custom items.
 */
fun ItemStack.getCustomItem(
    customItems: Collection<CustomItem>,
    customEnchantmentRegistry: CustomEnchantmentRegistry
): CustomItem? {
    return getCustomItemFromItemStackPersistentData(this, customItems) ?: getCustomItemFromItemStackSimilarity(
        this,
        customItems,
        customEnchantmentRegistry
    )
}

/**
 * Attempts to get the tier from this ItemStack that matches the tiers available in the TierManager.
 *
 * @param tierManager Tier Manager
 */
fun ItemStack.getTier(tierManager: TierManager): Tier? = getTier(tierManager.get())

/**
 * Attempts to get the tier from this ItemStack that matches the given collection of tiers.
 *
 * @param tiers tiers to choose from
 */
fun ItemStack.getTier(tiers: Collection<Tier>): Tier? {
    return getTierFromItemStackPersistentData(this, tiers) ?: getTierFromItemStackDisplayName(this, tiers)
}

private fun getCustomItemFromItemStackPersistentData(
    itemStack: ItemStack,
    customItems: Collection<CustomItem>
): CustomItem? {
    return itemStack.getPersistentDataString(mythicDropsCustomItem)
        ?.let { customItemName -> customItems.find { it.name == customItemName } }
}

private fun getTierFromItemStackPersistentData(itemStack: ItemStack, tiers: Collection<Tier>): Tier? {
    return itemStack.getPersistentDataString(mythicDropsTier)?.let { tierName -> tiers.find { it.name == tierName } }
}

private fun getTierFromItemStackDisplayName(itemStack: ItemStack, tiers: Collection<Tier>): Tier? {
    return itemStack.getDisplayName()?.let { displayName ->
        val firstChatColor = ChatColorUtil.getFirstColor(displayName)
        val colors = ChatColor.getLastColors(displayName)
        val lastChatColor = if (colors.contains(ChatColor.COLOR_CHAR)) {
            ChatColor.getByChar(colors.substring(1, 2))
        } else {
            null
        }
        if (firstChatColor == null || lastChatColor == null || firstChatColor == lastChatColor) {
            null
        } else {
            tiers.find { it.displayColor == firstChatColor && it.identifierColor == lastChatColor }
        }
    }
}

private fun getCustomItemFromItemStackSimilarity(
    itemStack: ItemStack,
    customItems: Collection<CustomItem>,
    customEnchantmentRegistry: CustomEnchantmentRegistry
): CustomItem? {
    return customItems.find { it.toItemStack(customEnchantmentRegistry).isSimilar(itemStack) }
}
