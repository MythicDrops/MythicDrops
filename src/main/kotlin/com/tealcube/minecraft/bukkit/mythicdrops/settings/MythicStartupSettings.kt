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

import com.squareup.moshi.JsonClass
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.StartupSettings
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import java.util.logging.Level
import org.bukkit.configuration.ConfigurationSection

@JsonClass(generateAdapter = true)
data class MythicStartupSettings internal constructor(
    override var debug: Boolean = false,
    override val isBackupOnConfigMigrate: Boolean = true,
    override val loggingLevels: Map<String, Level> = emptyMap()
) : StartupSettings {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicStartupSettings(
            configurationSection.getBoolean("debug", false),
            configurationSection.getBoolean("backup-on-config-migrate", true),
            configurationSection.getOrCreateSection("logging-levels").getValues(false).mapNotNull {
                val level = try {
                    Level.parse(it.value.toString())
                } catch (ex: IllegalArgumentException) {
                    return@mapNotNull null
                }
                it.key to level
            }.toMap()
        )
    }
}
