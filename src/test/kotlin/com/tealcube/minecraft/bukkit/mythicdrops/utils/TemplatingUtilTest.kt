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
import org.junit.jupiter.api.Test

class TemplatingUtilTest {

    @Test
    fun testOpsStringRand() {
        val randTemplateString = "rand 2-4"
        val randOpsString = TemplatingUtil.opsString(randTemplateString)
        assertThat(randOpsString.operation).isEqualTo("rand")
        assertThat(randOpsString.arguments).isEqualTo("2-4")
    }

    @Test
    fun testOpsStringRandSign() {
        val randSignTemplateString = "randsign"
        val randOpsString = TemplatingUtil.opsString(randSignTemplateString)
        assertThat(randOpsString.operation).isEqualTo("randsign")
        assertThat(randOpsString.arguments).isEqualTo("")
    }

    @Test
    fun doesRandTemplateReturnAccurateValue() {
        val randTemplateString = "+%rand 2-4% Memes"

        val actual = TemplatingUtil.template(randTemplateString)
        assertThat(actual).isIn("+2 Memes", "+3 Memes", "+4 Memes")
    }

    @Test
    fun doesRandsignTemplateReturnAccurateValue() {
        val randsignTemplateString = "%randsign%3 Memes"

        val actual = TemplatingUtil.template(randsignTemplateString)
        assertThat(actual).isIn("-3 Memes", "+3 Memes")
    }

    @Test
    fun doesRandAndRandsignWorkTogether() {
        val randsignTemplateString = "%randsign%%rand 2-4% Memes"

        val actual = TemplatingUtil.template(randsignTemplateString)
        assertThat(actual).isIn(
            "+2 Memes", "+3 Memes", "+4 Memes",
            "-2 Memes", "-3 Memes", "-4 Memes"
        )
    }

    @Test
    fun doMultipleRandsWork() {
        val randTemplateString = "%rand 1-4%%rand 1-4%"

        val actual = TemplatingUtil.template(randTemplateString)
        assertThat(actual).isIn(
            "11", "12", "13", "14", "21", "22", "23", "24", "31", "32", "33", "34", "41", "42", "43",
            "44"
        )
    }
}
