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
@file:Suppress("detekt.TooManyFunctions")

package io.pixeloutlaw.minecraft.spigot.mythicdrops

import com.google.common.collect.ImmutableMultimap
import com.google.common.collect.Multimap
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * Acquires the ItemMeta and runs an action on it, returning the result.
 *
 * @param action Action to perform on the ItemMeta
 */
internal fun <R> ItemStack.getFromItemMeta(action: ItemMeta.() -> R): R? {
    return this.itemMeta?.run(action)
}

/**
 * Acquires the ItemMeta, runs an action on it, then sets the ItemMeta.
 *
 * @param action Action to perform on the ItemMeta
 */
internal fun ItemStack.getThenSetItemMeta(action: ItemMeta.() -> Unit) {
    this.itemMeta = this.itemMeta?.apply(action)
}

/**
 * Acquires the ItemMeta, runs an action on it, sets the ItemMeta, and returns the result of the action.
 *
 * @param R Return type of the Action
 * @param action Action to perform on the ItemMeta
 * @return result of the action, null if there is no ItemMeta
 */
internal fun <R> ItemStack.getThenSetItemMeta(action: ItemMeta.() -> R): R? {
    return this.itemMeta?.let {
        val retValue = action(it)
        this.itemMeta = it
        return retValue
    }
}

/**
 * Checks if the ItemMeta of the current ItemStack is of type `IM`.
 *
 * @param IM Subclass of ItemMeta
 * @return if ItemMeta is of expected type
 */
internal inline fun <reified IM : ItemMeta> ItemStack.hasItemMetaOf(): Boolean {
    return this.itemMeta is IM
}

/**
 * Acquires the ItemMeta and attempts to cast it to the given type of [ItemMeta].
 */
internal inline fun <reified IM : ItemMeta> ItemStack.getItemMetaAs(): IM? {
    return this.itemMeta as? IM
}

/**
 * Acquires the ItemMeta of type `IM`, runs an action on it, and returns the result.
 */
internal inline fun <reified IM : ItemMeta, R> ItemStack.getFromItemMetaAs(action: IM.() -> R): R? {
    return getItemMetaAs<IM>()?.run(action)
}

/**
 * Acquires the ItemMeta of type `IM`, runs an action on it, then sets the ItemMeta.
 *
 * @param IM Subclass of ItemMeta
 * @param action Action to perform on the ItemMeta
 */
internal inline fun <reified IM : ItemMeta> ItemStack.getThenSetItemMetaAs(action: IM.() -> Unit) {
    getItemMetaAs<IM>()?.let {
        action(it)
        this.itemMeta = it
    }
}

/**
 * Acquires the ItemMeta of type `IM`, runs an action on it, sets the ItemMeta,
 * and returns the result of the action.
 *
 * @param IM Subclass of ItemMeta
 * @param R Return type of action
 * @param action Action to perform on the ItemMeta
 * @return result of the action, null if no ItemMeta found
 */
internal inline fun <reified IM : ItemMeta, R> ItemStack.getThenSetItemMetaAs(action: IM.() -> R): R? {
    return getItemMetaAs<IM>()?.let {
        val retValue = action(it)
        this.itemMeta = it
        return retValue
    }
}

internal fun ItemStack.addAttributeModifier(attribute: Attribute, attributeModifier: AttributeModifier) =
    getThenSetItemMeta<Boolean> { this.addAttributeModifier(attribute, attributeModifier) }

internal fun ItemStack.addItemFlags(vararg itemFlags: ItemFlag) =
    getThenSetItemMeta { this.addItemFlags(*itemFlags) }

internal fun ItemStack.addItemFlags(itemFlags: Collection<ItemFlag>) = addItemFlags(*itemFlags.toTypedArray())

internal fun ItemStack.getAttributeModifiers(): Multimap<Attribute, AttributeModifier> =
    this.itemMeta?.attributeModifiers ?: ImmutableMultimap.of()

internal fun ItemStack.getAttributeModifiers(attribute: Attribute): Collection<AttributeModifier> =
    this.itemMeta?.getAttributeModifiers(attribute) ?: emptyList()

internal fun ItemStack.getAttributeModifiers(slot: EquipmentSlot): Multimap<Attribute, AttributeModifier> =
    this.itemMeta?.getAttributeModifiers(slot) ?: ImmutableMultimap.of()

internal var ItemStack.displayName: String?
    get() = getFromItemMeta { displayName }
    set(value) = getThenSetItemMeta { setDisplayName(value) }

internal var ItemStack.itemFlags: Set<ItemFlag>
    get() = getFromItemMeta { itemFlags } ?: emptySet()
    set(value) = getThenSetItemMeta { value.forEach { addItemFlags(it) } }

internal val ItemStack.localizedName: String?
    get() = getFromItemMeta {
        if (hasLocalizedName()) {
            localizedName
        } else {
            null
        }
    }

internal var ItemStack.lore: List<String>
    get() = getFromItemMeta {
        if (hasLore()) {
            lore
        } else {
            null
        }
    }?.toList() ?: emptyList()
    set(value) = getThenSetItemMeta { lore = value }

internal var ItemStack.customModelData: Int?
    get() = getFromItemMeta { customModelData }
    set(value) = getThenSetItemMeta { setCustomModelData(value) }

internal var ItemStack.isUnbreakable: Boolean
    get() = getFromItemMeta { isUnbreakable } ?: false
    set(value) = getThenSetItemMeta { isUnbreakable = value }

internal fun ItemStack.hasAttributeModifiers(): Boolean = this.itemMeta?.hasAttributeModifiers() ?: false

internal fun ItemStack.hasConflictingEnchantment(ench: Enchantment): Boolean =
    this.itemMeta?.hasConflictingEnchant(ench) ?: false

internal fun ItemStack.hasDisplayName(): Boolean = getFromItemMeta { hasDisplayName() } ?: false

internal fun ItemStack.hasItemFlag(flag: ItemFlag): Boolean = getFromItemMeta { hasItemFlag(flag) } ?: false

internal fun ItemStack.hasLocalizedName(): Boolean = getFromItemMeta { hasLocalizedName() } ?: false

internal fun ItemStack.hasLore(): Boolean = getFromItemMeta { hasLore() } ?: false

internal fun ItemStack.removeAttributeModifier(attribute: Attribute) =
    getThenSetItemMeta<Boolean> { this.removeAttributeModifier(attribute) }

internal fun ItemStack.removeAttributeModifier(attribute: Attribute, modifier: AttributeModifier) =
    getThenSetItemMeta<Boolean> { this.removeAttributeModifier(attribute, modifier) }

internal fun ItemStack.removeAttributeModifier(slot: EquipmentSlot) =
    getThenSetItemMeta<Boolean> { this.removeAttributeModifier(slot) }

internal fun ItemStack.removeItemFlags(vararg itemFlags: ItemFlag) =
    getThenSetItemMeta { this.removeItemFlags(*itemFlags) }

internal fun ItemStack.setAttributeModifiers(attributeModifiers: Multimap<Attribute, AttributeModifier>) =
    getThenSetItemMeta { this.attributeModifiers = attributeModifiers }

internal fun ItemStack.setLocalizedName(string: String?) =
    getThenSetItemMeta { this.setLocalizedName(string) }

internal fun ItemStack.hasCustomModelData() = getFromItemMeta { hasCustomModelData() } ?: false
