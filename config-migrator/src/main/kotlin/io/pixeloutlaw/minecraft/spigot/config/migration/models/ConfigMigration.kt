package io.pixeloutlaw.minecraft.spigot.config.migration.models

import com.github.zafarkhaja.semver.Version
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConfigMigration(
    val fileName: String,
    val fromVersion: Version,
    val toVersion: Version,
    val configMigrationSteps: List<ConfigMigrationStep> = emptyList()
)
