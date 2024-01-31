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

import com.google.common.base.Preconditions
import com.tealcube.minecraft.bukkit.mythicdrops.firstChatColors
import org.bukkit.ChatColor

internal object ChatColorUtil {
    /**
     * Returns the [ChatColor] associated with the given [str] with an optional fallback.
     *
     * @param str String to convert to [ChatColor]
     * @param fallback fallback [ChatColor], defaults to null
     */
    fun getChatColor(str: String?): ChatColor? {
        if (str == null) {
            return null
        }
        return try {
            ChatColor.valueOf(str)
        } catch (e: Exception) {
            null
        }
    }

    fun getChatColor(
        str: String?,
        fallback: ChatColor
    ): ChatColor {
        if (str == null) {
            return fallback
        }
        return try {
            ChatColor.valueOf(str)
        } catch (e: Exception) {
            fallback
        }
    }

    /**
     * Returns a random [ChatColor] from a non-empty [Collection].
     *
     * @param chatColors [Collection] of [ChatColor]s
     * @throws IllegalArgumentException if [chatColors] is empty
     */
    fun getRandomChatColor(chatColors: Collection<ChatColor>): ChatColor {
        Preconditions.checkArgument(!chatColors.isEmpty())
        return chatColors.random()
    }

    /**
     * Returns the first [ChatColor]s found in the given [string].
     *
     * @param string String to check
     *
     * @return first [ChatColor]s found, null if none found
     */
    fun getFirstColors(string: String): String {
        return string.firstChatColors()
    }

    /**
     * Returns the first [ChatColor] found in the given [string].
     *
     * @param string String to check
     *
     * @return first [ChatColor] found, null if none found
     */
    fun getFirstColor(string: String): ChatColor? {
        var index = 0
        while (index < string.length - 1) {
            if (string[index] == ChatColor.COLOR_CHAR && index + 1 < string.length) {
                return ChatColor.getByChar(string[index + 1])
            }
            index++
        }
        return null
    }
}
