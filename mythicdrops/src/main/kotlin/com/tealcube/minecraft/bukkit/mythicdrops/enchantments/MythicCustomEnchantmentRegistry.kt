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
package com.tealcube.minecraft.bukkit.mythicdrops.enchantments

import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.CustomEnchantmentRegistry
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment

class MythicCustomEnchantmentRegistry(mythicDropsPlugin: MythicDropsPlugin) : CustomEnchantmentRegistry {
    private val glowEnchantmentNamespacedKey = NamespacedKey(mythicDropsPlugin, CustomEnchantmentRegistry.GLOW)
    private val glowEnchantment = GlowEnchantment(glowEnchantmentNamespacedKey)
    private val customEnchantmentMap = mapOf(CustomEnchantmentRegistry.GLOW to glowEnchantment)

    override fun getCustomEnchantmentByKey(key: String): Enchantment? = customEnchantmentMap[key]

    override fun registerEnchantments() {
        customEnchantmentMap.values.forEach { registerEnchantment(it) }
    }

    private fun registerEnchantment(enchantment: Enchantment) {
        if (Enchantment.getByKey(enchantment.key) != null) {
            return
        }
        try {
            val f = Enchantment::class.java.getDeclaredField("acceptingNew")
            f.isAccessible = true
            f[null] = true
        } catch (ignored: Exception) {
        }
        try {
            Enchantment.registerEnchantment(enchantment)
        } catch (ignored: IllegalStateException) {
        } catch (ignored: IllegalArgumentException) {
        }
    }
}
