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
package com.tealcube.minecraft.bukkit.mythicdrops

import com.google.common.truth.Truth.assertThat
import io.pixeloutlaw.minecraft.spigot.config.migration.ConfigMigrator
import io.pixeloutlaw.minecraft.spigot.config.migration.SmarterYamlConfiguration
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class ConfigMigrationsTest {
    @Test
    fun `does config_yml 5_1_0 to 7_0_0 migration apply correctly`(@TempDir dataFolder: File) {
        testMigrationExact(
            dataFolder,
            "/config_5_1_0.yml",
            "/config_7_0_0.yml",
            "migrations/config_yml-5_1_0-to-7_0_0.migration.json",
            "config.yml"
        )
    }

    @Test
    fun `does creatureSpawning_yml 4_0_3 to 5_0_0 migration apply correctly`(@TempDir dataFolder: File) {
        testMigrationExact(
            dataFolder,
            "/creatureSpawning_4_0_3.yml",
            "/creatureSpawning_5_0_0.yml",
            "migrations/creatureSpawning_yml-4_0_3-to-5_0_0.migration.json",
            "creatureSpawning.yml"
        )
    }

    @Test
    fun `does identifying_yml 4_0_0 to 5_0_0 migration apply correctly`(@TempDir dataFolder: File) {
        testMigrationExact(
            dataFolder,
            "/identifying_4_0_0.yml",
            "/identifying_5_0_0.yml",
            "migrations/identifying_yml-4_0_0-to-5_0_0.migration.json",
            "identifying.yml"
        )
    }

    @Test
    fun `does language_yml 2_7_15 to 3_0_0 migration apply correctly`(@TempDir dataFolder: File) {
        testMigrationFromContainsAllTo(
            dataFolder,
            "/language_2_7_15.yml",
            "/language_3_0_0.yml",
            "migrations/language_yml-2_7_15-to-3_0_0.migration.json",
            "language.yml"
        )
    }

    private fun testMigrationFromContainsAllTo(
        dataFolder: File,
        fromResourceName: String,
        toResourceName: String,
        migrationName: String,
        fileName: String
    ) {
        val fromResourceContents = javaClass.getResource(fromResourceName).readText()
        val file = dataFolder.toPath().resolve(fileName).toFile()
        file.writeText(fromResourceContents)

        val configurationMigrator = ConfigMigrator(dataFolder, setOf(migrationName))
        configurationMigrator.migrate(fileName)

        val fromYamlConfiguration = SmarterYamlConfiguration(file)
        val toResourceContents = javaClass.getResource(toResourceName).readText()
        val toYamlConfiguration = SmarterYamlConfiguration().apply { loadFromString(toResourceContents) }

        assertThat(fromYamlConfiguration.getKeys(true)).containsAtLeastElementsIn(toYamlConfiguration.getKeys(true))
    }

    private fun testMigrationExact(
        dataFolder: File,
        fromResourceName: String,
        toResourceName: String,
        migrationName: String,
        fileName: String
    ) {
        val fromResourceContents = javaClass.getResource(fromResourceName).readText()
        val file = dataFolder.toPath().resolve(fileName).toFile()
        file.writeText(fromResourceContents)

        val configurationMigrator = ConfigMigrator(dataFolder, setOf(migrationName))
        configurationMigrator.migrate(fileName)

        val fromYamlConfiguration = SmarterYamlConfiguration(file)
        val toResourceContents = javaClass.getResource(toResourceName).readText()
        val toYamlConfiguration = SmarterYamlConfiguration().apply { loadFromString(toResourceContents) }

        assertThat(fromYamlConfiguration.getKeys(true)).isEqualTo(toYamlConfiguration.getKeys(true))
        for (key in fromYamlConfiguration.getKeys(true)) {
            assertThat(fromYamlConfiguration.isConfigurationSection(key)).isEqualTo(
                toYamlConfiguration.isConfigurationSection(
                    key
                )
            )
        }
    }
}
