package io.pixeloutlaw.minecraft.spigot.config.migration.models

import org.bukkit.configuration.serialization.ConfigurationSerializable

interface NamedConfigMigration : ConfigurationSerializable {
    val migrationName: String
    val configMigration: ConfigMigration
}
