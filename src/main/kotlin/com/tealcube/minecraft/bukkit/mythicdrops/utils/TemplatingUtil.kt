/*
 * The MIT License
 * Copyright © 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.utils

import com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER
import com.tealcube.minecraft.bukkit.mythicdrops.logging.MythicLoggerFactory
import com.tealcube.minecraft.bukkit.mythicdrops.templating.OpString
import com.tealcube.minecraft.bukkit.mythicdrops.templating.RandRomanTemplate
import com.tealcube.minecraft.bukkit.mythicdrops.templating.RandSignTemplate
import com.tealcube.minecraft.bukkit.mythicdrops.templating.RandTemplate
import org.apache.commons.lang3.StringUtils
import java.util.regex.Pattern

object TemplatingUtil {
    private val logger = MythicLoggerFactory.getLogger(TemplatingUtil::class.java)
    private val percentagePattern = Pattern.compile("%(?s)(.*?)%")

    private val randIntegerRangeTemplate = RandTemplate()
    private val randSignTemplate = RandSignTemplate()
    private val randRomanTemplate = RandRomanTemplate()

    internal fun opsString(str: String): OpString {
        val opString = StringUtils.trimToEmpty(str).split("\\s+".toRegex(), 2).toTypedArray()
        val operation = if (opString.isNotEmpty()) opString[0] else ""
        val args = if (opString.size > 1) opString[1] else ""
        return OpString(operation, args)
    }

    fun template(string: String): String {
        var retString = string
        val m = percentagePattern.matcher(string)
        while (m.find()) {
            val check = m.group()
            val checkWithoutPercentages = check.replace("%", "")
            val opString = opsString(checkWithoutPercentages)
            LOGGER.fine("opString=\"$opString\"")
            when {
                randIntegerRangeTemplate.test(opString.operation) -> {
                    LOGGER.fine("Templating using randIntegerRangeTemplate")
                    retString = StringUtils.replace(retString, check, randIntegerRangeTemplate.apply(opString.arguments))
                }
                randSignTemplate.test(opString.operation) -> {
                    LOGGER.fine("Templating using randSignTemplate")
                    retString = StringUtils.replace(retString, check, randSignTemplate.apply(opString.arguments))
                }
                randRomanTemplate.test(opString.operation) -> {
                    LOGGER.fine("Templating using randRomanTemplate")
                    retString = StringUtils.replace(retString, check, randRomanTemplate.apply(opString.arguments))
                }
            }
        }
        return retString
    }
}