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
package io.pixeloutlaw.minecraft.spigot.mythicdrops

import java.util.Locale

private val whiteSpaceRegex = "\\s+".toRegex()

/**
 * Converts a string to Title Case.
 *
 * Example: "a boy went to the park".toTitleCase() == "A Boy Went To The Park"
 */
internal fun String.toTitleCase(): String =
    split(whiteSpaceRegex).joinToString(separator = " ") {
        if (it.length > 1) {
            "${it.substring(0, 1).uppercase(Locale.getDefault())}${it.substring(1).lowercase(Locale.getDefault())}"
        } else {
            it.uppercase(Locale.getDefault())
        }
    }

/**
 * If this [String] is null, returns an empty [String]. Otherwise, returns a trimmed copy of this [String].
 */
internal fun String?.trimToEmpty(): String = this?.trim() ?: ""
