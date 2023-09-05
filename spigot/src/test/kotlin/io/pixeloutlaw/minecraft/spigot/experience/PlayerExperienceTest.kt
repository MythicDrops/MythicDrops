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
package io.pixeloutlaw.minecraft.spigot.experience

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PlayerExperienceTest {
    private val totalExperienceForLevels = mapOf(
        0 to 0,
        1 to 7,
        2 to 16,
        3 to 27,
        4 to 40,
        5 to 55,
        6 to 72,
        7 to 91,
        8 to 112,
        9 to 135,
        10 to 160,
        11 to 187,
        12 to 216,
        13 to 247,
        14 to 280,
        15 to 315,
        16 to 352,
        17 to 394,
        18 to 441,
        19 to 493,
        20 to 550,
        21 to 612,
        22 to 679,
        23 to 751,
        24 to 828,
        25 to 910,
        26 to 997,
        27 to 1089,
        28 to 1186,
        29 to 1288,
        30 to 1395,
        31 to 1507,
        32 to 1628,
        33 to 1758,
        50 to 5345,
        100 to 30970,
        250 to 242845,
        500 to 1045970,
        1000 to 4339720
    )
    private val expRequiredForNextLevel = mapOf(
        0 to 7,
        1 to 9,
        2 to 11,
        3 to 13,
        4 to 15,
        5 to 17,
        6 to 19,
        7 to 21,
        8 to 23,
        9 to 25,
        10 to 27,
        11 to 29,
        12 to 31,
        13 to 33,
        14 to 35,
        15 to 37,
        16 to 42,
        17 to 47,
        18 to 52,
        19 to 57,
        20 to 62,
        21 to 67,
        22 to 72,
        23 to 77,
        24 to 82,
        25 to 87,
        26 to 92,
        27 to 97,
        28 to 102,
        29 to 107,
        30 to 112,
        31 to 121,
        32 to 130,
        33 to 139,
        50 to 292,
        100 to 742,
        250 to 2092,
        500 to 4342,
        1000 to 8842
    )

    @Test
    fun `does getExpFromLevel coerce negative numbers to 0`() {
        assertThat(PlayerExperience.getExpFromLevel(-1)).isEqualTo(0)
    }

    @Test
    fun `does getExpFromLevel coerce extremely high numbers to 10000`() {
        assertThat(PlayerExperience.getExpFromLevel(10005)).isEqualTo(448377220)
    }

    @Test
    fun `does getExpFromLevel return correct value for level`() {
        totalExperienceForLevels.entries.forEach {
            assertThat(PlayerExperience.getExpFromLevel(it.key)).isEqualTo(it.value)
        }
    }

    @Test
    fun `does getExpForNextLevel coerce negative numbers to 0`() {
        assertThat(PlayerExperience.getExpForNextLevel(-1)).isEqualTo(7)
    }

    @Test
    fun `does getExpForNextLevel coerce extremely high numbers to 10000`() {
        assertThat(PlayerExperience.getExpForNextLevel(10005)).isEqualTo(89842)
    }

    @Test
    fun `does getExpForNextLevel return correct value for level`() {
        expRequiredForNextLevel.entries.forEach {
            assertThat(PlayerExperience.getExpForNextLevel(it.key)).isEqualTo(it.value)
        }
    }
}
