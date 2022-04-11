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
package com.tealcube.minecraft.bukkit.mythicdrops.utils

import org.assertj.core.api.Assertions.assertThat
import org.bukkit.Material
import org.junit.jupiter.api.Test

internal class AirUtilTest {
    @Test
    fun `is Material_AIR air`() {
        assertThat(Material.AIR.isAir).isTrue()
    }

    @Test
    fun `is Material_CAVE_AIR air`() {
        assertThat(Material.CAVE_AIR.isAir).isTrue()
    }

    @Test
    fun `is Material_VOID_AIR air`() {
        assertThat(Material.VOID_AIR.isAir).isTrue()
    }

    @Suppress("DEPRECATION")
    @Test
    fun `is Material_LEGACY_AIR air`() {
        assertThat(Material.LEGACY_AIR.isAir).isTrue()
    }

    @Test
    fun `is Material_ACACIA_STAIRS not air`() {
        assertThat(Material.ACACIA_STAIRS.isAir).isFalse()
    }
}
