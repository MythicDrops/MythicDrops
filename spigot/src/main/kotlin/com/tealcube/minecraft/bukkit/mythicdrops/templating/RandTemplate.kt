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

internal object RandTemplate : Template("rand") {
    private const val DASH_PATTERN_STRING = "\\s*-\\s*"
    private val dashPattern = DASH_PATTERN_STRING.toRegex()

    override fun invoke(arguments: String): String {
        if (arguments.isBlank()) {
            return arguments
        }
        val split = arguments.split(dashPattern).map { it.trim() }.filter(String::isNotEmpty)
        val first = split[0].toIntOrNull() ?: return arguments
        val second = split[1].toIntOrNull() ?: return arguments
        val minVal = min(first, second)
        val maxVal = max(first, second)
        return (minVal..maxVal).random().toString()
    }
}
