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

import org.bukkit.enchantments.Enchantment
import kotlin.math.max

internal fun <T, U> Map<U, Set<T>>.additivePlus(pair: Pair<U, Set<T>>): Map<U, Set<T>> {
    val originalValue = this[pair.first] ?: emptySet()
    val additiveValue = originalValue + pair.second
    return this.plus(pair.first to additiveValue)
}

internal fun Map<Enchantment, Int>.merge(other: Map<Enchantment, Int>): Map<Enchantment, Int> {
    val merged = toMutableMap()
    for (entry in other) {
        val mergedValue = merged[entry.key]
        val toSet = if (mergedValue != null) {
            max(mergedValue, entry.value)
        } else {
            entry.value
        }
        merged[entry.key] = toSet
    }
    return merged.toMap()
}

internal fun Map<Enchantment, Int>.highestByValue(): Enchantment? = maxByOrNull { it.value }?.key
