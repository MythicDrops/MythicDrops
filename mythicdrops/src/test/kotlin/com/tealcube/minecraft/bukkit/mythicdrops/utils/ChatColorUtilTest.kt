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

import org.assertj.core.api.Assertions.assertThat
import org.bukkit.ChatColor
import org.junit.jupiter.api.Test

class ChatColorUtilTest {
    @Test
    fun `does getFirstColors return empty string for empty param`() {
        val toTest = ""

        assertThat(ChatColorUtil.getFirstColors(toTest)).isEmpty()
    }

    @Test
    fun `does getFirstColors return one ChatColor if param starts with one ChatColor`() {
        val toTest = "${ChatColor.AQUA}Hi Mom"
        val firstColors = ChatColorUtil.getFirstColors(toTest)

        assertThat(firstColors).isEqualTo("${ChatColor.AQUA}")
    }

    @Test
    fun `does getFirstColors return two ChatColors if param starts with two ChatColors`() {
        val toTest = "${ChatColor.AQUA}${ChatColor.ITALIC}Hi Mom"
        val firstColors = ChatColorUtil.getFirstColors(toTest)

        assertThat(firstColors).isEqualTo("${ChatColor.AQUA}${ChatColor.ITALIC}")
    }

    @Test
    fun `does getFirstColors return two ChatColors if param contains three ChatColors but only starts with two`() {
        val toTest = "${ChatColor.AQUA}${ChatColor.ITALIC}Hi Mom${ChatColor.GOLD}"
        val firstColors = ChatColorUtil.getFirstColors(toTest)

        assertThat(firstColors).isEqualTo("${ChatColor.AQUA}${ChatColor.ITALIC}")
    }
}
