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
package com.tealcube.minecraft.bukkit.mythicdrops.socketing

import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketExtenderType
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketType
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import com.tealcube.minecraft.bukkit.mythicdrops.stripColors
import io.pixeloutlaw.kindling.Log
import org.bukkit.configuration.ConfigurationSection

internal data class MythicSocketExtenderType(
    override val name: String,
    override val appliedSocketType: SocketType,
    override val socketExtenderStyle: String = "",
    override val socketExtenderStyleChatColorized: String = socketExtenderStyle.chatColorize(),
    override val socketExtenderStyleStripped: String = socketExtenderStyleChatColorized.stripColors(),
    override val socketExtenderHelp: List<String> = emptyList(),
    override val socketExtenderHelpChatColorized: List<String> = socketExtenderHelp.chatColorize(),
    override val socketExtenderHelpStripped: List<String> = socketExtenderHelpChatColorized.stripColors(),
    override val slotStyle: String = "",
    override val slotStyleChatColorized: String = slotStyle.chatColorize(),
    override val slotStyleStripped: String = slotStyleChatColorized.stripColors(),
    override val slotHelp: List<String> = emptyList(),
    override val slotHelpChatColorized: List<String> = slotHelp.chatColorize(),
    override val slotHelpStripped: List<String> = slotHelpChatColorized.stripColors(),
    override val isIgnoreColors: Boolean = false,
    override val customModelData: Int = 0,
    override val weight: Double = 0.0
) : SocketExtenderType {
    companion object {
        fun fromConfigurationSection(
            configurationSection: ConfigurationSection,
            key: String,
            socketTypeManager: SocketTypeManager,
            loadingErrorManager: LoadingErrorManager
        ): MythicSocketExtenderType? {
            val weight = configurationSection.getDouble("weight")
            val socketExtenderStyle = configurationSection.getNonNullString("socket-extender-style")
            val slotStyle = configurationSection.getNonNullString("slot-style")
            val customModelData = configurationSection.getInt("custom-model-data")
            val rawAppliedSocketType = configurationSection.getNonNullString("applied-socket-type")
            val help = configurationSection.getStringList("socket-extender-help")
            val isIgnoreColors = configurationSection.getBoolean("ignore-colors")
            val slotHelp = configurationSection.getStringList("slot-help")
            val appliedSocketType = socketTypeManager.getById(rawAppliedSocketType)
            if (appliedSocketType == null) {
                Log.debug("appliedSocketType == null: key=$key rawAppliedSocketType=$rawAppliedSocketType")
                loadingErrorManager.add(
                    "Not loading socket gem $key as there isn't a socket type by name: '$rawAppliedSocketType'"
                )
                return null
            }
            return MythicSocketExtenderType(
                name = key,
                weight = weight,
                socketExtenderHelp = help,
                customModelData = customModelData,
                appliedSocketType = appliedSocketType,
                socketExtenderStyle = socketExtenderStyle,
                isIgnoreColors = isIgnoreColors,
                slotStyle = slotStyle,
                slotHelp = slotHelp
            )
        }
    }
}
