/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2021 Richard Harrah
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
package io.pixeloutlaw.minecraft.spigot.config.migration.migrators

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import io.pixeloutlaw.minecraft.spigot.config.migration.ConfigMigrator
import io.pixeloutlaw.minecraft.spigot.config.migration.models.NamedConfigMigration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import org.koin.core.annotation.Single

/**
 * Implementation of [ConfigMigrator] that loads files from a specified [jarFile].
 */
@Single
internal class JarConfigMigrator(
    plugin: Plugin,
    settingsManager: SettingsManager
) : ConfigMigrator(plugin.dataFolder, settingsManager.startupSettings.isBackupOnConfigMigrate) {
    private val cachedNamedConfigMigrations: List<NamedConfigMigration> by lazy {
        val migrationsYamlUrl = JarConfigMigrator::class.java.classLoader.getResource("config/migration/migrations.yml")
        val migrationsYamlText = migrationsYamlUrl?.readText() ?: ""
        val migrationsYaml = YamlConfiguration().apply { loadFromString(migrationsYamlText) }
        val namedMigrationsRaw = migrationsYaml.getList("migrations")

        if (namedMigrationsRaw is List<*>) {
            namedMigrationsRaw.filterIsInstance<NamedConfigMigration>()
        } else {
            emptyList()
        }
    }

    override val namedConfigMigrations: List<NamedConfigMigration> = cachedNamedConfigMigrations
}
