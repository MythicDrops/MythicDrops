package com.tealcube.minecraft.bukkit.mythicdrops.config.migration.models

import com.github.zafarkhaja.semver.Version
import com.squareup.moshi.JsonClass

/**
 * Represents a migration.
 *
 * @property fileGlobs gitignore style globs for files to match
 * @property fromVersion semver version to migrate from
 * @property toVersion semver version to migrate to
 * @property configMigrationSteps steps to apply in order
 */
@JsonClass(generateAdapter = true)
data class ConfigMigration(
    val fileGlobs: List<String>,
    val fromVersion: Version,
    val toVersion: Version,
    val configMigrationSteps: List<ConfigMigrationStep> = emptyList(),
    val createBackup: Boolean = true,
    val overwrite: Boolean = false
)
