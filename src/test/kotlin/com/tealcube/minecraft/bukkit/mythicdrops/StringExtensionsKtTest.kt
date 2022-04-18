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

import net.md_5.bungee.api.ChatColor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StringExtensionsKtTest {
    @Test
    fun `does replaceArgs not affect Strings without args`() {
        val expected = "dank memes"
        val actual = expected.replaceArgs("lame" to "danker")
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `does replaceArgs replace args`() {
        val template = "lame memes"
        val expected = "dank memes"
        val actual = template.replaceArgs("lame" to "dank")
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `does chatColorize replace hex colors`() {
        val template = "#78BC61Your mother was a hamster"
        val expected = "${ChatColor.of("#78BC61")}Your mother was a hamster"
        val actual = template.chatColorize()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `does unChatColorize convert hex colors`() {
        val template = "#78BC61Your mother was a hamster".chatColorize()
        val expected = "#78BC61Your mother was a hamster"
        val actual = template.unChatColorize()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `does firstChatColors find all values`() {
        val template = "&C#78BC61Your mother was a hamster".chatColorize()
        val expected = "&C#78BC61".chatColorize()
        val actual = template.firstChatColors()
        assertThat(actual).isEqualTo(expected)
    }
}
