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

import java.util.logging.Logger
import kotlin.reflect.KClass

/**
 * Caches
 */
internal object JulLoggerFactory {
    private val cachedLoggers = mutableMapOf<String, Logger>()
    private val loggerCustomizers = mutableListOf<JulLoggerCustomizer>()

    fun getLogger(clazz: KClass<*>) = getLogger(clazz.java.canonicalName)

    /**
     * Registers a [JulLoggerCustomizer] to customize a [Logger].
     *
     * @param customizer customizer
     */
    fun registerLoggerCustomizer(customizer: JulLoggerCustomizer) {
        loggerCustomizers.add(customizer)
    }

    fun clearCustomizers() {
        loggerCustomizers.clear()
    }

    fun clearCachedLoggers() {
        // clear handlers
        cachedLoggers.values.forEach { cachedLogger ->
            cachedLogger.handlers.toList().forEach { handler ->
                handler.close()
                cachedLogger.removeHandler(handler)
            }
        }

        // clear loggers
        cachedLoggers.clear()
    }

    private fun getLogger(name: String) = cachedLoggers.getOrPut(name) {
        loggerCustomizers.fold(
            Logger.getLogger(name)
        ) { logger, customizer ->
            customizer.customize(logger)
        }
    }
}
