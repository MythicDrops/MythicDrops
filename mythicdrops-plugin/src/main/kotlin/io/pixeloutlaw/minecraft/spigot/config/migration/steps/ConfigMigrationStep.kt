package io.pixeloutlaw.minecraft.spigot.config.migration.steps

import org.bukkit.configuration.serialization.ConfigurationSerializable

/**
 * Represents a step that can be run as part of a migration.
 */
interface ConfigMigrationStep : ConfigurationSerializable
