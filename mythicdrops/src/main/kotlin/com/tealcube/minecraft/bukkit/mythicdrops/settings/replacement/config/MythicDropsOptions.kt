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
package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.config

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.config.DropsOptions
import org.bukkit.configuration.ConfigurationSection

data class MythicDropsOptions internal constructor(
    override val itemChance: Double = 1.0,
    override val tieredItemChance: Double = 0.25,
    override val customItemChance: Double = 0.1,
    override val socketGemChance: Double = 0.2,
    override val unidentifiedItemChance: Double = 0.1,
    override val identityTomeChance: Double = 0.1
) : DropsOptions {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection): MythicDropsOptions = MythicDropsOptions(
            configurationSection.getDouble("item-chance", 1.0),
            configurationSection.getDouble("tiered-item-chance", 0.25),
            configurationSection.getDouble("custom-item-chance", 0.1),
            configurationSection.getDouble("socket-gem-chance", 0.2),
            configurationSection.getDouble("unidentified-item-chance", 0.1),
            configurationSection.getDouble("identity-tome-chance", 0.1)
        )
    }
}
