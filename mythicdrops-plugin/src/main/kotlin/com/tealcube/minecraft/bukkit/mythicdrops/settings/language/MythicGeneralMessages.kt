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
package com.tealcube.minecraft.bukkit.mythicdrops.settings.language

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.GeneralMessages
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

internal data class MythicGeneralMessages(
    override val foundItemBroadcast: String = "&6[MythicDrops] &F%receiver%&A has found a %item%!",
    override val preventedNetheriteUpgrade: String = "&6[MythicDrops] You cannot upgrade this item to Netherite!"
) : GeneralMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicGeneralMessages(
            foundItemBroadcast = configurationSection.getNonNullString("found-item-broadcast"),
            preventedNetheriteUpgrade = configurationSection.getNonNullString("prevented-netherite-upgrade")
        )
    }
}
