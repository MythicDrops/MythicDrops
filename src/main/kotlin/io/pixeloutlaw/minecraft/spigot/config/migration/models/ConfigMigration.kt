package io.pixeloutlaw.minecraft.spigot.config.migration.models

import io.pixeloutlaw.minecraft.spigot.config.SemVer
import io.pixeloutlaw.minecraft.spigot.config.migration.steps.ConfigMigrationStep
import org.bukkit.configuration.serialization.ConfigurationSerializable

interface ConfigMigration : ConfigurationSerializable {
    val fileGlobs: List<String>
    val fromVersion: SemVer
    val toVersion: SemVer
    val configMigrationSteps: List<ConfigMigrationStep>
    val createBackup: Boolean
    val overwrite: Boolean
}
