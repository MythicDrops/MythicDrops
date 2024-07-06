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
package com.tealcube.minecraft.bukkit.mythicdrops.settings.identification

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.identification.IdentificationItems
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.identification.items.IdentityTomeOptions
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.identification.items.UnidentifiedItemOptions
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.settings.identification.items.MythicIdentityTomeOptions
import com.tealcube.minecraft.bukkit.mythicdrops.settings.identification.items.MythicUnidentifiedItemOptions
import org.bukkit.configuration.ConfigurationSection

internal data class MythicIdentificationItems(
    override val identityTome: IdentityTomeOptions = MythicIdentityTomeOptions(),
    override val unidentifiedItem: UnidentifiedItemOptions = MythicUnidentifiedItemOptions()
) : IdentificationItems {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) =
            MythicIdentificationItems(
                MythicIdentityTomeOptions.fromConfigurationSection(configurationSection.getOrCreateSection("identity-tome")),
                MythicUnidentifiedItemOptions.fromConfigurationSection(configurationSection.getOrCreateSection("unidentified-item"))
            )
    }
}
