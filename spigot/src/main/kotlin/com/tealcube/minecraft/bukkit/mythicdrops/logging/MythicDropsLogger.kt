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

import io.pixeloutlaw.kindling.Log
import io.pixeloutlaw.kindling.LogRecord
import io.pixeloutlaw.kindling.Logger
import java.io.PrintWriter
import java.io.StringWriter

internal class MythicDropsLogger(
    override val minimumLogLevel: Log.Level
) : Logger() {
    private companion object {
        const val FIVE = 5
        const val TWO_HUNDRED_FIFTY_SIX = 256
        val javaLogger by lazy {
            JulLoggerFactory.getLogger(MythicDropsLogger::class)
        }
    }

    override fun print(logRecord: LogRecord) {
        val (level, tag, message, throwable) = logRecord

        val trace = Exception().stackTrace[FIVE]
        var logTag = tag
        if (tag.isEmpty()) {
            logTag = getTraceTag(trace)
        }

        var fullMessage = "$message"
        throwable?.let { fullMessage = "$fullMessage\n${it.stackTraceString}" }
        val msg = "$logTag: $fullMessage"

        when (level) {
            Log.Level.VERBOSE -> javaLogger.finest(msg)
            Log.Level.DEBUG -> javaLogger.fine(msg)
            Log.Level.INFO -> javaLogger.info(msg)
            Log.Level.WARNING -> javaLogger.warning(msg)
            Log.Level.ERROR -> javaLogger.severe(msg)
            Log.Level.ASSERT -> javaLogger.severe(msg)
        }
    }

    private val Throwable.stackTraceString
        get(): String {
            val sw = StringWriter(TWO_HUNDRED_FIFTY_SIX)
            val pw = PrintWriter(sw, false)
            printStackTrace(pw)
            pw.flush()
            return sw.toString()
        }

    private fun getTraceTag(trace: StackTraceElement): String {
        val className = trace.className.split(".").last()
        return "$className.${trace.methodName}"
    }
}
