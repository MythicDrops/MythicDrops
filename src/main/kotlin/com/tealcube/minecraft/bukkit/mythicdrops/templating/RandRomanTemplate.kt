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
package com.tealcube.minecraft.bukkit.mythicdrops.templating

import kotlin.math.max
import kotlin.math.min

internal object RandRomanTemplate : DashStringTemplate("randroman") {
    private val romanNumerals = mapOf(
        (0 to ""),
        (1 to "I"),
        (2 to "II"),
        (3 to "III"),
        (4 to "IV"),
        (5 to "V"),
        (6 to "VI"),
        (7 to "VII"),
        (8 to "VIII"),
        (9 to "IX"),
        (10 to "X"),
        (40 to "XL"),
        (50 to "L"),
        (90 to "XC"),
        (100 to "C"),
        (400 to "CD"),
        (500 to "D"),
        (900 to "CM"),
        (1000 to "M")
    )

    override fun splitDashInvoke(original: String, split: List<String>): String {
        val first = split[0].toIntOrNull() ?: return original
        val second = split[1].toIntOrNull() ?: return original
        val minVal = min(first, second)
        val maxVal = max(first, second)
        val randomizedVal = (minVal..maxVal).random()
        return intToString(randomizedVal)
    }

    private fun intToString(input: Int): String {
        return romanNumerals.keys.filter { it != 0 }.sortedDescending()
            .fold((0 to "")) { result, next -> convert(input, result, next) }
            .second
    }

    private fun convert(input: Int, previous: Pair<Int, String>, divisor: Int): Pair<Int, String> {
        require(divisor != 0)
        var s = previous.second
        val digit = (input - previous.first) / divisor
        if (divisor == 1) {
            s += romanNumerals[digit]
        } else {
            for (i in 1..digit) {
                s += romanNumerals[divisor]
            }
        }
        return Pair(previous.first + digit * divisor, s)
    }
}
