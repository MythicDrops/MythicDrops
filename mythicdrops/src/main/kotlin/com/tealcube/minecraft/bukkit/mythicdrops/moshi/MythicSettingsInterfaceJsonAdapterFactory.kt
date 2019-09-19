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
package com.tealcube.minecraft.bukkit.mythicdrops.moshi

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory
import java.lang.reflect.Type
import java.util.logging.Level

object MythicSettingsInterfaceJsonAdapterFactory : JsonAdapter.Factory {
    private val logger = JulLoggerFactory.getLogger(MythicSettingsInterfaceJsonAdapterFactory::class)

    override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
        val clazz = Types.getRawType(type)

        // we only care about interfaces
        if (!clazz.isInterface) {
            logger.fine("${type.typeName} is not an interface!")
            return null
        }

        // interfaces from our settings package (or subpackages...)
        if (!clazz.`package`.name.startsWith("com.tealcube.minecraft.bukkit.mythicdrops.api.settings")) {
            logger.fine("${type.typeName} is not an interface!")
            return null
        }

        val nonApiPackageName = clazz.`package`.name.replace(".api", "")
        val mythicClassName = "Mythic${clazz.simpleName}"

        val mythicClass = try {
            Class.forName("$nonApiPackageName.$mythicClassName")
        } catch (ex: Exception) {
            logger.log(Level.SEVERE, "Could not find class for name!", ex)
            return null
        }
        return moshi.adapter(mythicClass)
    }
}
