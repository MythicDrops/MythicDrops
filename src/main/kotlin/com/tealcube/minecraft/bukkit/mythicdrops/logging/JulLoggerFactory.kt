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
    private val loggerCustomizers = mutableMapOf<String, List<JulLoggerCustomizer>>()

    fun getLogger(clazz: Class<*>) = getLogger(clazz.canonicalName)

    fun getLogger(clazz: KClass<*>) = getLogger(clazz.java)

    fun getLogger(name: String) = cachedLoggers.getOrPut(name) {
        getCustomizersForName(name).fold(
            Logger.getLogger(name),
            { logger, customizer ->
                customizer.customize(name, logger)
            }
        )
    }

    /**
     * Registers a [JulLoggerCustomizer] to customize a [Logger].
     *
     * @param name name of logger to customize
     * @param customizer customizer
     */
    fun registerLoggerCustomizer(name: String, customizer: JulLoggerCustomizer) {
        loggerCustomizers[name] =
            loggerCustomizers.getOrDefault(name, emptyList()).toMutableList().apply { add(customizer) }.toList()
    }

    // internal-only for testing
    fun getCustomizersForName(name: String): List<JulLoggerCustomizer> {
        val matchingCustomizerNames = loggerCustomizers.keys.filter { name.startsWith(it) }
        return matchingCustomizerNames.mapNotNull { loggerCustomizers[it] }.flatten()
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
}
