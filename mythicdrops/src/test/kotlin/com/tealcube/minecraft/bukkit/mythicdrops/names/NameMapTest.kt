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
package com.tealcube.minecraft.bukkit.mythicdrops.names

import com.tealcube.minecraft.bukkit.mythicdrops.api.names.NameType
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class NameMapTest {
    @BeforeEach
    fun setup() {
        NameMap.clear()
    }

    @Test
    fun testGetMatchingKeys() {
        NameMap[NameType.MATERIAL_PREFIX.format + "DIAMOND_SWORD"] = listOf("foo", "bar", "foobar")
        NameMap[NameType.MATERIAL_PREFIX.format + "DIAMOND_PICKAXE"] = listOf("foo", "bar", "foobar")
        NameMap[NameType.MATERIAL_PREFIX.format + "DIAMOND_AXE"] = listOf("foo", "bar", "foobar")
        NameMap[NameType.MATERIAL_SUFFIX.format + "DIAMOND_SWORD"] = listOf("foo", "bar", "foobar")
        val matchingKeys =
            NameMap.getMatchingKeys(NameType.MATERIAL_PREFIX)
        assertNotNull(matchingKeys)
        assertTrue(
            matchingKeys.contains(NameType.MATERIAL_PREFIX.format + "DIAMOND_SWORD")
        )
        assertTrue(
            matchingKeys.contains(NameType.MATERIAL_PREFIX.format + "DIAMOND_PICKAXE")
        )
        assertTrue(
            matchingKeys.contains(NameType.MATERIAL_PREFIX.format + "DIAMOND_AXE")
        )
        assertFalse(
            matchingKeys.contains(NameType.MATERIAL_SUFFIX.format + "DIAMOND_SWORD")
        )
    }

    @Test
    fun testGetRandom() {
        NameMap[NameType.MATERIAL_PREFIX.format + "DIAMOND_SWORD"] = listOf("foo", "bar", "foobar")
        NameMap[NameType.MATERIAL_PREFIX.format + "DIAMOND_PICKAXE"] = listOf("foo", "bar", "foobar")
        NameMap[NameType.MATERIAL_PREFIX.format + "DIAMOND_AXE"] = listOf("foo", "bar", "foobar")
        val numOfRuns = 1000
        for ((key1) in NameMap) {
            val key = key1.replace(
                NameType.MATERIAL_PREFIX.format,
                ""
            )
            val results = IntArray(3)
            for (j in 0 until numOfRuns) {
                val s =
                    NameMap.getRandom(NameType.MATERIAL_PREFIX, key)
                assertNotNull(s)
                assertNotEquals(s, "")
                when (s) {
                    "foo" -> {
                        results[0]++
                    }
                    "bar" -> {
                        results[1]++
                    }
                    "foobar" -> {
                        results[2]++
                    }
                    else -> {
                        fail<Any>("Unexpected value")
                    }
                }
            }
            assertTrue(results[0] > 200)
            assertTrue(results[1] > 200)
            assertTrue(results[2] > 200)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testGetRandomKey() {
        NameMap[NameType.MATERIAL_PREFIX.format + "DIAMOND_SWORD"] = listOf("foo", "bar", "foobar")
        NameMap[NameType.MATERIAL_PREFIX.format + "DIAMOND_PICKAXE"] = listOf("foo", "bar", "foobar")
        NameMap[NameType.MATERIAL_PREFIX.format + "DIAMOND_AXE"] = listOf("foo", "bar", "foobar")
        NameMap[NameType.MATERIAL_SUFFIX.format + "DIAMOND_SWORD"] = listOf("foo", "bar", "foobar")
        val results = IntArray(3)
        val numOfRuns = 1000
        for (i in 0 until numOfRuns) {
            val key =
                NameMap.getRandomKey(NameType.MATERIAL_PREFIX)
            assertNotNull(key)
            when (key) {
                "DIAMOND_SWORD" -> {
                    results[0]++
                }
                "DIAMOND_PICKAXE" -> {
                    results[1]++
                }
                "DIAMOND_AXE" -> {
                    results[2]++
                }
                else -> {
                    fail<Any>("Unexpected value")
                }
            }
        }
        assertTrue(results[0] > 200)
        assertTrue(results[1] > 200)
        assertTrue(results[2] > 200)
    }
}
