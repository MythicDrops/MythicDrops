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
package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.identification

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.identification.UnidentifiedItemOptions
import org.bukkit.configuration.ConfigurationSection

data class MythicUnidentifiedItemOptions(
    override val name: String = "&dUnidentified Item",
    override val lore: List<String> = emptyList(),
    override val allowableTiersPrefix: String = "&7Tiers: (&F",
    override val allowableTiersSeparator: String = "&7, &F",
    override val allowableTiersSuffix: String = "&7)",
    override val droppedByPrefix: String = "&7Dropped by: &F",
    override val droppedBySuffix: String = "",
    override val tierPrefix: String = "&7Tier: &F",
    override val tierSuffix: String = ""
) : UnidentifiedItemOptions {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicUnidentifiedItemOptions(
            configurationSection.getString("name") ?: "&dUnidentified Item",
            configurationSection.getStringList("lore"),
            configurationSection.getString("allowable-tiers-prefix") ?: "&7Tiers: (&F",
            configurationSection.getString("allowable-tiers-separator") ?: "&7, &F",
            configurationSection.getString("allowable-tiers-suffix") ?: "&7)",
            configurationSection.getString("dropped-by-prefix") ?: "&7Dropped by: &F",
            configurationSection.getString("dropped-by-suffix") ?: "",
            configurationSection.getString("tier-prefix") ?: "&7Tier: &F",
            configurationSection.getString("tier-suffix") ?: ""
        )
    }
}
