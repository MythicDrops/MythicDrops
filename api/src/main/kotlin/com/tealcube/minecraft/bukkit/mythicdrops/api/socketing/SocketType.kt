/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2022 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.api.socketing

import com.tealcube.minecraft.bukkit.mythicdrops.api.weight.Weighted

/**
 * Holds information about a type of empty Socket.
 *
 * @property name Name of type of Socket (appears in item lore)
 * @property socketGemStyle Display value for the type of Socket Gem
 * @property socketGemStyleChatColorized ChatColor-ized version of [socketGemStyle]
 * @property socketGemStyleStripped Stripped version of [socketGemStyleChatColorized]
 * @property socketStyle Display value for the type of Socket
 * @property socketStyleChatColorized ChatColor-ized version of [socketStyle]
 * @property socketStyleStripped Stripped version of [socketStyleChatColorized]
 * @property isIgnoreColors Should parsing for empty sockets ignore colors?
 * @property socketHelp Help text for items with Sockets
 */
interface SocketType : Weighted {
    val name: String
    val socketGemStyle: String
    val socketGemStyleChatColorized: String
    val socketGemStyleStripped: String
    val socketStyle: String
    val socketStyleChatColorized: String
    val socketStyleStripped: String
    val isIgnoreColors: Boolean
    val socketHelp: List<String>
}
