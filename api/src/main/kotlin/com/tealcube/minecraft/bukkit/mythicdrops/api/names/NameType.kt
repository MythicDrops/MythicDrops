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
package com.tealcube.minecraft.bukkit.mythicdrops.api.names

enum class NameType(
    val format: String
) {
    ENCHANTMENT_PREFIX("enchantment.prefix."),
    ENCHANTMENT_SUFFIX("enchantment.suffix."),
    ENCHANTMENT_LORE("enchantment.lore."),
    GENERAL_PREFIX("general.prefix"),
    GENERAL_SUFFIX("general.suffix"),
    GENERAL_LORE("general.lore"),
    MATERIAL_PREFIX("material.prefix."),
    MATERIAL_SUFFIX("material.suffix."),
    MATERIAL_LORE("material.lore."),
    TIER_PREFIX("tier.prefix."),
    TIER_SUFFIX("tier.suffix."),
    TIER_LORE("tier.lore."),
    SPECIFIC_MOB_NAME("mobname."),
    GENERAL_MOB_NAME("mobname"),
    ITEMTYPE_PREFIX("itemtype.prefix."),
    ITEMTYPE_SUFFIX("itemtype.suffix."),
    ITEMTYPE_LORE("itemtype.lore.")
}
