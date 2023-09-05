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
 * Holds information about a type of Socket Extender.
 *
 * @property name Name of type of Socket (appears in item lore)
 * @property socketExtenderStyle Style of Socket Extender item name
 * @property socketExtenderStyleChatColorized ChatColor-ized version of [socketExtenderStyle]
 * @property socketExtenderStyleStripped Stripped version of [socketExtenderStyleChatColorized]
 * @property customModelData Custom model data applied to the Socket Extender item.
 * @property appliedSocketType Type of Socket applied by this Socket Extender
 * @property socketExtenderHelp Help text added to Socket Extender item
 * @property socketExtenderHelpChatColorized ChatColor-ized version of [socketExtenderHelp]
 * @property socketExtenderHelpStripped ChatColor-ized version of [socketExtenderHelpChatColorized]
 * @property slotStyle Display value for the type of Socket Extender slot
 * @property slotStyleChatColorized ChatColor-ized version of [slotStyle]
 * @property slotStyleStripped Stripped version of [slotStyleChatColorized]
 * @property slotHelp Help text added to item with a slot
 * @property slotHelpChatColorized ChatColor-ized version of [slotHelp]
 * @property slotHelpStripped ChatColor-ized version of [slotHelpChatColorized]
 * @property isIgnoreColors Should parsing for empty slots ignore colors?
 */
interface SocketExtenderType : Weighted {
    val name: String
    val socketExtenderStyle: String
    val socketExtenderStyleChatColorized: String
    val socketExtenderStyleStripped: String
    val customModelData: Int
    val appliedSocketType: SocketType
    val slotStyle: String
    val slotStyleChatColorized: String
    val slotStyleStripped: String
    val slotHelp: List<String>
    val slotHelpChatColorized: List<String>
    val slotHelpStripped: List<String>
    val isIgnoreColors: Boolean
    val socketExtenderHelp: List<String>
    val socketExtenderHelpChatColorized: List<String>
    val socketExtenderHelpStripped: List<String>
}
