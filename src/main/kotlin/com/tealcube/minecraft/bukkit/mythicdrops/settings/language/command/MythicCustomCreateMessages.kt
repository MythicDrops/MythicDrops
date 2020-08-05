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
package com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command

import com.squareup.moshi.JsonClass
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.CustomCreateMessages
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

@JsonClass(generateAdapter = true)
data class MythicCustomCreateMessages internal constructor(
    override val success: String = "",
    override val failure: String = "",
    override val requiresItem: String = "",
    override val requiresItemMeta: String = "",
    override val requiresDisplayName: String = ""
) : CustomCreateMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicCustomCreateMessages(
            success = configurationSection.getNonNullString("success"),
            failure = configurationSection.getNonNullString("failure"),
            requiresItem = configurationSection.getNonNullString("requires-item"),
            requiresItemMeta = configurationSection.getNonNullString("requires-item-meta"),
            requiresDisplayName = configurationSection.getNonNullString("requires-display-name")
        )
    }
}
