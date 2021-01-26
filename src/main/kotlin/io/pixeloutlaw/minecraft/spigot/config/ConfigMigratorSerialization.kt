package io.pixeloutlaw.minecraft.spigot.config

import io.pixeloutlaw.minecraft.spigot.config.migration.models.ConfigMigration
import io.pixeloutlaw.minecraft.spigot.config.migration.models.ConfigMigrationStep
import io.pixeloutlaw.minecraft.spigot.config.migration.models.NamedConfigMigration
import org.bukkit.configuration.serialization.ConfigurationSerialization

/**
 * Registers configuration serializable classes.
 */
internal object ConfigMigratorSerialization {
    fun registerAll() {
        ConfigMigrationStep.defaultSteps.forEach {
            ConfigurationSerialization.registerClass(it.java)
        }
        ConfigurationSerialization.registerClass(SemVer::class.java)
        ConfigurationSerialization.registerClass(ConfigMigration::class.java)
        ConfigurationSerialization.registerClass(NamedConfigMigration::class.java)
    }

    fun unregisterAll() {
        ConfigurationSerialization.unregisterClass(NamedConfigMigration::class.java)
        ConfigurationSerialization.unregisterClass(ConfigMigration::class.java)
        ConfigurationSerialization.unregisterClass(SemVer::class.java)
        ConfigMigrationStep.defaultSteps.forEach {
            ConfigurationSerialization.unregisterClass(it.java)
        }
    }
}