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
package com.tealcube.minecraft.bukkit.mythicdrops.names

import com.tealcube.minecraft.bukkit.mythicdrops.api.names.NameType
import java.util.ArrayList
import java.util.concurrent.ConcurrentHashMap
import org.apache.commons.lang3.RandomUtils

object NameMap : ConcurrentHashMap<String, List<String>>() {
    fun getJoinedKeys() = keys().toList().joinToString()

    fun getRandomKey(nameType: NameType): String? {
        val matchingKeys = getMatchingKeys(nameType)
        val key = matchingKeys[RandomUtils.nextInt(0, matchingKeys.size)]
        return key.replace(nameType.format, "")
    }

    fun getMatchingKeys(nameType: NameType): List<String> {
        val matchingKeys: MutableList<String> = ArrayList()
        for (key in keys) {
            if (key.startsWith(nameType.format)) {
                matchingKeys.add(key)
            }
        }
        return matchingKeys
    }

    fun getRandom(
        nameType: NameType,
        key: String
    ): String? {
        val list: List<String> = if (containsKey(nameType.format + key)) {
            getForFormat(nameType.format + key) ?: emptyList()
        } else {
            emptyList()
        }
        return if (list.isEmpty()) {
            ""
        } else list[RandomUtils.nextInt(0, list.size)]
    }

    private fun getForFormat(key: String?): List<String>? {
        if (key == null) {
            return emptyList()
        }
        return if (!containsKey(key)) {
            emptyList()
        } else {
            super.get(key)
        }
    }
}
