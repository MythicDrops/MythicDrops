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
package io.pixeloutlaw.minecraft.spigot.config.migration.models

import io.pixeloutlaw.minecraft.spigot.config.SemVer
import org.bukkit.configuration.serialization.ConfigurationSerializable

/**
 * Represents a migration.
 *
 * @property fileGlobs gitignore style globs for files to match
 * @property fromVersion semver version to migrate from
 * @property toVersion semver version to migrate to
 * @property configMigrationSteps steps to apply in order
 * @property createBackup if backup should be created for migration
 * @property overwrite if this migration is just overwriting a file from the JAR
 */
data class ConfigMigration(
    val fileGlobs: List<String>,
    val fromVersion: SemVer,
    val toVersion: SemVer,
    val configMigrationSteps: List<ConfigMigrationStep> = emptyList(),
    val createBackup: Boolean = true,
    val overwrite: Boolean = false
) : ConfigurationSerializable {
    companion object {
        @JvmStatic
        val NO_OP: ConfigMigration = ConfigMigration(emptyList(), SemVer(), SemVer())

        @JvmStatic
        fun deserialize(map: Map<String, Any>): ConfigMigration {
            val fileGlobsRaw = map.getOrDefault("fileGlobs", emptyList<String>())
            val fileGlobs = if (fileGlobsRaw is List<*>) fileGlobsRaw.filterIsInstance<String>() else emptyList()
            val fromVersion = SemVer.parseOrDefault(map.getOrDefault("fromVersion", "").toString(), SemVer())
            val toVersion = SemVer.parseOrDefault(map.getOrDefault("toVersion", "").toString(), SemVer())
            val configMigrationStepsRaw = map.getOrDefault("configMigrationSteps", emptyList<ConfigMigrationStep>())
            val configMigrationSteps =
                if (configMigrationStepsRaw is List<*>) {
                    configMigrationStepsRaw.filterIsInstance<ConfigMigrationStep>()
                } else {
                    emptyList()
                }
            val createBackup = map.getOrDefault("createBackup", true) as? Boolean ?: true
            val overwrite = map.getOrDefault("overwrite", false) as? Boolean ?: false
            return ConfigMigration(fileGlobs, fromVersion, toVersion, configMigrationSteps, createBackup, overwrite)
        }
    }

    override fun serialize(): MutableMap<String, Any> =
        mutableMapOf(
            "fileGlobs" to fileGlobs,
            "toVersion" to toVersion,
            "fromVersion" to fromVersion,
            "configMigrationSteps" to configMigrationSteps,
            "createBackup" to createBackup,
            "overwrite" to overwrite
        )
}
