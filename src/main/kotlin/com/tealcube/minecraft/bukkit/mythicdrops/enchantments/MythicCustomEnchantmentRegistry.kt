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

import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.CustomEnchantmentRegistry
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.plugin.Plugin

@Deprecated("Only used for glow enchantments; use GlowEnchant from Plumbing instead")
internal class MythicCustomEnchantmentRegistry : CustomEnchantmentRegistry {
    private val customEnchantmentMap: Map<String, Enchantment> = emptyMap()

    override fun getCustomEnchantmentByKey(
        key: String,
        material: Material
    ): Enchantment? {
        val enchantmentTargets = EnchantmentTarget.entries.filter { it.includes(material) }
        val potentialEnchantmentKeys = enchantmentTargets.map { "$key-${it.name}" }
        // Find the first enchantment key that matches and return that
        for (enchantmentKey in potentialEnchantmentKeys) {
            val enchantment = customEnchantmentMap[enchantmentKey]
            if (enchantment != null) {
                return enchantment
            }
        }
        // otherwise null
        return null
    }

    override fun registerEnchantments() {
        // do nothing
    }
}
