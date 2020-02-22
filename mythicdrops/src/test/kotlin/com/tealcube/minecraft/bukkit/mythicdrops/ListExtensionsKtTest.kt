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

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ListExtensionsKtTest {
    @Test
    fun `does replaceArgs not replace if no args found`() {
        val expected = listOf("dank memes", "danker memes", "dankest memes")
        val actual = expected.replaceArgs("lame memes" to "danker memes")
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `does replaceArgs replace any found args that match full strings`() {
        val template = listOf("lame memes", "kinda dank memes", "sorta dank memes")
        val expected = listOf("barely dank memes", "kinda dank memes", "sorta dank memes")
        val actual = template.replaceArgs("lame memes" to "barely dank memes")
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `does replaceArgs replace any found args that match partial strings`() {
        val template = listOf("lame memes", "kinda dank memes", "sorta dank memes")
        val expected = listOf("lame meemees", "kinda dank meemees", "sorta dank meemees")
        val actual = template.replaceArgs("memes" to "meemees")
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `does replaceWithCollection not replace if element not found`() {
        val expected = listOf("barely dank memes", "kinda dank memes", "sorta dank memes", "lame memes")
        val actual = expected.replaceWithCollection("dank memes", listOf("dank memes", "very dank memes"))
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `does replaceWithCollection replace if element found`() {
        val template = listOf("barely dank memes", "kinda dank memes", "sorta dank memes", "lame memes")
        val expected =
            listOf("barely dank memes", "kinda dank memes", "dank memes", "very dank memes", "lame memes")
        val actual = template.replaceWithCollection("sorta dank memes", listOf("dank memes", "very dank memes"))
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `does replaceWithCollections not replace if elements not found`() {
        val expected = listOf("dank", "memes", "man")
        val actual = expected.replaceWithCollections(
            "lame" to listOf("kinda dank", "sorta dank", "dank"),
            "memeroonis" to listOf("meemees", "maymays", "memes"),
            "manbearpig" to listOf("child", "woman", "man")
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `does replaceWithCollections replace if elements found`() {
        val template = listOf("dank", "memes", "man")
        val expected =
            listOf("kinda dank", "sorta dank", "dank", "meemees", "maymays", "memes", "child", "woman", "man")
        val actual = template.replaceWithCollections(
            "dank" to listOf("kinda dank", "sorta dank", "dank"),
            "memes" to listOf("meemees", "maymays", "memes"),
            "man" to listOf("child", "woman", "man")
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `does trimEmptyFromBeginning remove empty elements in list`() {
        val template = listOf("", "", " ", "\n", "Hi, Mom!", "", "Hi, Dad!", "", "", " ", "\n")
        val expected = listOf("Hi, Mom!", "", "Hi, Dad!", "", "", " ", "\n")
        assertThat(template.trimEmptyFromBeginning()).isEqualTo(expected)
    }

    @Test
    fun `does trimEmptyFromEnd remove empty elements in list`() {
        val template = listOf("", "", " ", "\n", "Hi, Mom!", "", "Hi, Dad!", "", "", " ", "\n")
        val expected = listOf("", "", " ", "\n", "Hi, Mom!", "", "Hi, Dad!")
        assertThat(template.trimEmptyFromEnd()).isEqualTo(expected)
    }

    @Test
    fun `does trimEmpty remove empty elements in list`() {
        val template = listOf("", "", " ", "\n", "Hi, Mom!", "", "Hi, Dad!", "", "", " ", "\n")
        val expected = listOf("Hi, Mom!", "", "Hi, Dad!")
        assertThat(template.trimEmpty()).isEqualTo(expected)
    }
}
