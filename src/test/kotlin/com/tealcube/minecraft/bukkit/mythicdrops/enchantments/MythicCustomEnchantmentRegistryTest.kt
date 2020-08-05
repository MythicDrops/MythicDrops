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
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class MythicCustomEnchantmentRegistryTest {

    @MockK
    private lateinit var mythicDropsPlugin: MythicDropsPlugin
    private lateinit var mythicCustomEnchantmentRegistry: MythicCustomEnchantmentRegistry

    @BeforeEach
    fun setup() {
        every { mythicDropsPlugin.name } returns "MythicDrops"
        mythicCustomEnchantmentRegistry = MythicCustomEnchantmentRegistry(mythicDropsPlugin)
    }

    @Test
    fun `does getCustomEnchantmentByKey return matching enchantment for TRIDENT`() {
        val enchantment = mythicCustomEnchantmentRegistry.getCustomEnchantmentByKey(CustomEnchantmentRegistry.GLOW, Material.TRIDENT)
        assertThat(enchantment).isNotNull
        assertThat(enchantment!!.key).isEqualTo(NamespacedKey(mythicDropsPlugin, "${CustomEnchantmentRegistry.GLOW}-BREAKABLE"))
    }

    @Test
    fun `does getCustomEnchantmentByKey not return matching enchantment for STICK`() {
        val enchantment = mythicCustomEnchantmentRegistry.getCustomEnchantmentByKey(CustomEnchantmentRegistry.GLOW, Material.STICK)
        assertThat(enchantment).isNull()
    }
}
