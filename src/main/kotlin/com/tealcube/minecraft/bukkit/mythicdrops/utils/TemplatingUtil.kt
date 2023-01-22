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

import com.tealcube.minecraft.bukkit.mythicdrops.templating.OpString
import com.tealcube.minecraft.bukkit.mythicdrops.templating.RandRomanTemplate
import com.tealcube.minecraft.bukkit.mythicdrops.templating.RandSignTemplate
import com.tealcube.minecraft.bukkit.mythicdrops.templating.RandTemplate
import io.pixeloutlaw.minecraft.spigot.mythicdrops.trimToEmpty

internal object TemplatingUtil {
    private val percentageRegex = """%(?s)(.*?)%""".toRegex()
    private val whitespaceRegex = """\s+""".toRegex()

    fun template(string: String): String {
        return percentageRegex.replace(string) {
            val opString = opsString(it.value.replace("%", ""))
            when {
                RandTemplate.test(opString.operation) -> {
                    RandTemplate.invoke(opString.arguments)
                }

                RandSignTemplate.test(opString.operation) -> {
                    RandSignTemplate.invoke(opString.arguments)
                }

                RandRomanTemplate.test(opString.operation) -> {
                    RandRomanTemplate.invoke(opString.arguments)
                }

                else -> {
                    it.value
                }
            }
        }
    }

    internal fun opsString(str: String): OpString {
        val opString = str.trimToEmpty().split(whitespaceRegex, 2).toTypedArray()
        val operation = if (opString.isNotEmpty()) opString[0] else ""
        val args = if (opString.size > 1) opString[1] else ""
        return OpString(operation, args)
    }
}
