/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2021 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.logging

import java.io.PrintWriter
import java.io.StringWriter
import java.text.MessageFormat
import java.util.Date
import java.util.logging.Formatter
import java.util.logging.LogRecord

/**
 * [Formatter] instance that converts [LogRecord]s into standard usable formats.
 */
internal class MythicDropsLoggingFormatter : Formatter() {
    companion object {
        private const val DATE_TIME_FORMAT = "{0,date} {0,time}"
        private const val ONE_THOUSAND = 1000
        private const val ONE_HUNDRED = 100
    }

    private val date: Date = Date()
    private val formatter: MessageFormat by lazy { MessageFormat(DATE_TIME_FORMAT) }
    private val lineSeparator: String = System.lineSeparator()

    /**
     * Attempts to format the log record into a standard usable format.
     *
     * @param record log record instance
     *
     * @return log message
     */
    override fun format(record: LogRecord): String {
        val stringBuilder =
            StringBuilder(getDateTime(record)).append(" ").append(record.level.localizedName).append(": ")

        // Indent - the more serious, the more indented.
        val offset = (ONE_THOUSAND - record.level.intValue()) / ONE_HUNDRED
        for (i in 1..offset) {
            stringBuilder.append(" ")
        }

        stringBuilder.append(formatMessage(record))
        stringBuilder.append(lineSeparator)

        // add the exception to the message if there is one
        record.thrown?.let {
            addThrowableToStringBuilder(it, stringBuilder)
        }

        return stringBuilder.toString()
    }

    private fun addThrowableToStringBuilder(throwable: Throwable, stringBuilder: java.lang.StringBuilder) {
        try {
            StringWriter().use { stringWriter ->
                PrintWriter(stringWriter).use { printWriter ->
                    throwable.printStackTrace(printWriter)
                }
                stringBuilder.append(stringWriter.buffer)
            }
        } catch (ignored: Exception) {
            // if we get an exception while serializing an exception, just eat it
        }
    }

    private fun getDateTime(record: LogRecord): StringBuffer {
        // we want to minimize memory allocations so we reuse the `date` value from above
        date.time = record.millis

        return formatter.format(arrayOf(date), StringBuffer(), null)
    }
}
