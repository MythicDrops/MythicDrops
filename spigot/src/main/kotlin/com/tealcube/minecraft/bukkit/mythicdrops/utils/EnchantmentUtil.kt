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
package com.tealcube.minecraft.bukkit.mythicdrops.utils

import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment

internal object EnchantmentUtil {
    /**
     * Attempts to find an [Enchantment] by first converting [str] to a [NamespacedKey], then searching by name if no
     * matching [Enchantment]s found.
     *
     * @param str Name to search for
     */
    @Suppress("DEPRECATION")
    fun getByKeyOrName(str: String): Enchantment? =
        getEnchantmentByKey(stringToNamespacedKey(str)) ?: Enchantment.getByName(str)

    @Suppress("DEPRECATION")
    internal fun stringToNamespacedKey(str: String): NamespacedKey? = str.split(":", limit = 2).let {
        if (it.size < 2) {
            return@let null
        }
        return NamespacedKey(it[0], it[1])
    }

    private fun getEnchantmentByKey(namespacedKey: NamespacedKey?): Enchantment? = try {
        Enchantment.getByKey(namespacedKey)
    } catch (ex: Throwable) {
        null
    }
}
