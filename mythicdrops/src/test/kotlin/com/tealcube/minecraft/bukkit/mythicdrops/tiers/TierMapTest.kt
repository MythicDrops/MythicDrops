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
package com.tealcube.minecraft.bukkit.mythicdrops.tiers

import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TierMapTest {
    @Before
    fun setup() {
        TierMap.clear()
    }

    @Test
    @Throws(Exception::class)
    fun testGetRandom() {
        val tierMap = TierMap

        tierMap["foo"] = TierImp("foo").tier()!!
        tierMap["bar"] = TierImp("bar").tier()!!
        tierMap["foobar"] = TierImp("foobar").tier()!!

        val results = IntArray(3)
        val numOfRuns = 1200
        for (i in 0 until numOfRuns) {
            val t = tierMap.getRandomTier()
            Assert.assertNotNull(t)
            when {
                "foo" == t.name -> results[0]++
                "bar" == t.name -> results[1]++
                "foobar" == t.name -> results[2]++
                else -> Assert.fail("Unexpected value")
            }
        }

        Assert.assertTrue(results[0] > 300)
        Assert.assertTrue(results[1] > 300)
        Assert.assertTrue(results[2] > 300)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRandomWithChance() {
        val tierMap = TierMap

        tierMap["foo"] = TierImp("foo", 0.25).tier()!!
        tierMap["bar"] = TierImp("bar", 0.50).tier()!!
        tierMap["foobar"] = TierImp("foobar", 0.25).tier()!!

        val results = IntArray(3)
        val numOfRuns = 1000
        for (i in 0 until numOfRuns) {
            val t = tierMap.getRandomTierWithChance()
            Assert.assertNotNull(t)
            when {
                "foo" == t!!.name -> results[0]++
                "bar" == t.name -> results[1]++
                "foobar" == t.name -> results[2]++
                else -> Assert.fail("Unexpected value")
            }
        }

        Assert.assertTrue(results[0] > 200)
        Assert.assertTrue(results[1] > 300)
        Assert.assertTrue(results[2] > 200)
    }

    internal inner class TierImp {
        private var tier: Tier? = null

        constructor(name: String) {
            tier = MythicTierBuilder(name).build()
        }

        constructor(name: String, chance: Double) {
            tier = MythicTierBuilder(name).withSpawnChance(chance).build()
        }

        fun tier(): Tier? {
            return tier
        }
    }
}
