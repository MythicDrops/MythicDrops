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
package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.RepairingMessages
import org.bukkit.configuration.ConfigurationSection

data class MythicRepairingMessages internal constructor(
    override val cannotUse: String = "&6[MythicDrops] &CYou cannot repair that item!",
    override val doNotHave: String = "&6[MythicDrops] &CYou do not have enough materials to repair that item!",
    override val success: String = "&6[MythicDrops] &AYou successfully repaired your item!",
    override val instructions: String = "&6[MythicDrops] &ASmack this item on an anvil again to repair it!"
) : RepairingMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicRepairingMessages(
            configurationSection.getString("cannot-use") ?: "&6[MythicDrops] &CYou cannot repair that item!",
            configurationSection.getString("do-not-have")
                ?: "&6[MythicDrops] &CYou do not have enough materials to repair that item!",
            configurationSection.getString("success") ?: "&6[MythicDrops] &AYou successfully repaired your item!",
            configurationSection.getString("instructions")
                ?: "&6[MythicDrops] &ASmack this item on an anvil again to repair it!"
        )
    }
}
