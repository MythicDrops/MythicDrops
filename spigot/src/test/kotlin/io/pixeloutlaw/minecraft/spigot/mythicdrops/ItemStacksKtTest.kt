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
package io.pixeloutlaw.minecraft.spigot.mythicdrops

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ItemStacksKtTest {
    @MockK
    lateinit var itemStack: ItemStack

    @Test
    fun `does getHighestEnchantment return null if no enchantments present`() {
        every { itemStack.enchantments } returns emptyMap()
        assertThat(itemStack.getHighestEnchantment()).isNull()
    }

    @Test
    fun `does getHighestEnchantment return highest enchantment from available enchantments`() {
        every { itemStack.enchantments } returns
            mapOf(
                Enchantment.ARROW_DAMAGE to 10,
                Enchantment.ARROW_FIRE to 3,
                Enchantment.ARROW_INFINITE to 5,
                Enchantment.ARROW_KNOCKBACK to 11
            )
        assertThat(itemStack.getHighestEnchantment()).isEqualTo(Enchantment.ARROW_KNOCKBACK)
    }
}
