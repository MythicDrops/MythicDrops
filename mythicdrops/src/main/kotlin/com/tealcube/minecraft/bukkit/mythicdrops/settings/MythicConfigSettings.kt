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
package com.tealcube.minecraft.bukkit.mythicdrops.settings

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.ConfigSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.config.Components
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.config.Display
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.config.DropsOptions
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.config.GeneralOptions
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.config.Multiworld
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.settings.config.MythicComponents
import com.tealcube.minecraft.bukkit.mythicdrops.settings.config.MythicDisplay
import com.tealcube.minecraft.bukkit.mythicdrops.settings.config.MythicDropsOptions
import com.tealcube.minecraft.bukkit.mythicdrops.settings.config.MythicGeneralOptions
import com.tealcube.minecraft.bukkit.mythicdrops.settings.config.MythicMultiworld
import org.bukkit.configuration.ConfigurationSection

data class MythicConfigSettings internal constructor(
    override val version: String = "",
    override val options: GeneralOptions = MythicGeneralOptions(),
    override val multiworld: Multiworld = MythicMultiworld(),
    override val drops: DropsOptions = MythicDropsOptions(),
    override val components: Components = MythicComponents(),
    override val display: Display = MythicDisplay()
) : ConfigSettings {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection): MythicConfigSettings =
            MythicConfigSettings(
                configurationSection.getString("version") ?: "",
                MythicGeneralOptions.fromConfigurationSection(configurationSection.getOrCreateSection("options")),
                MythicMultiworld.fromConfigurationSection(configurationSection.getOrCreateSection("multiworld")),
                MythicDropsOptions.fromConfigurationSection(configurationSection.getOrCreateSection("drops")),
                MythicComponents.fromConfigurationSection(configurationSection.getOrCreateSection("components")),
                MythicDisplay.fromConfigurationSection(configurationSection.getOrCreateSection("display"))
            )
    }
}
