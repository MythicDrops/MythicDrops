package io.pixeloutlaw.minecraft.spigot.config.migration.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConfigMigration(
    val fileName: String,
    val fromVersion: String,
    val toVersion: String,
    val configMigrationSteps: List<ConfigMigrationStep> = emptyList()
)
