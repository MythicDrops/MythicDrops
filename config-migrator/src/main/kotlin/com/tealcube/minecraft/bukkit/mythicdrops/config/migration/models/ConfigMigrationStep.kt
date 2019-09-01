package com.tealcube.minecraft.bukkit.mythicdrops.config.migration.models

import com.squareup.moshi.JsonClass
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import org.bukkit.configuration.ConfigurationSection

/**
 * Represents a step that can be run as part of a migration.
 */
sealed class ConfigMigrationStep(val reason: String = "") {
    companion object {
        val adapterFactory = PolymorphicJsonAdapterFactory.of(ConfigMigrationStep::class.java, "stepType")
            .withSubtype(SetBooleanConfigMigrationStep::class.java, "set_boolean")
            .withSubtype(SetDoubleConfigMigrationStep::class.java, "set_double")
            .withSubtype(SetIntConfigMigrationStep::class.java, "set_int")
            .withSubtype(SetStringListConfigMigrationStep::class.java, "set_string_list")
            .withSubtype(SetStringConfigMigrationStep::class.java, "set_string")
            .withSubtype(DeleteConfigMigrationStep::class.java, "delete")
            .withSubtype(RenameConfigMigrationStep::class.java, "rename")
            .withSubtype(SetStringIfEqualsConfigMigrationStep::class.java, "set_string_if_equals")
    }

    abstract fun migrate(configuration: ConfigurationSection)
}

@JsonClass(generateAdapter = true)
data class DeleteConfigMigrationStep(val path: String) : ConfigMigrationStep() {
    override fun migrate(configuration: ConfigurationSection) {
        configuration[path] = null
    }
}

@JsonClass(generateAdapter = true)
data class RenameConfigMigrationStep(val from: String, val to: String) : ConfigMigrationStep() {
    override fun migrate(configuration: ConfigurationSection) {
        configuration[to] = configuration[from]
        configuration[from] = null
    }
}

@JsonClass(generateAdapter = true)
data class SetBooleanConfigMigrationStep(val key: String, val value: Boolean) : ConfigMigrationStep() {
    override fun migrate(configuration: ConfigurationSection) {
        configuration[key] = value
    }
}

@JsonClass(generateAdapter = true)
data class SetDoubleConfigMigrationStep(val key: String, val value: Double) : ConfigMigrationStep() {
    override fun migrate(configuration: ConfigurationSection) {
        configuration[key] = value
    }
}

@JsonClass(generateAdapter = true)
data class SetIntConfigMigrationStep(val key: String, val value: Int) : ConfigMigrationStep() {
    override fun migrate(configuration: ConfigurationSection) {
        configuration[key] = value
    }
}

@JsonClass(generateAdapter = true)
data class SetStringListConfigMigrationStep(val key: String, val value: List<String> = emptyList()) :
    ConfigMigrationStep() {
    override fun migrate(configuration: ConfigurationSection) {
        configuration[key] = value
    }
}

@JsonClass(generateAdapter = true)
data class SetStringConfigMigrationStep(val key: String, val value: String) : ConfigMigrationStep() {
    override fun migrate(configuration: ConfigurationSection) {
        configuration[key] = value
    }
}

@JsonClass(generateAdapter = true)
data class SetStringIfEqualsConfigMigrationStep(val key: String, val value: String, val ifEquals: String) :
    ConfigMigrationStep() {
    override fun migrate(configuration: ConfigurationSection) {
        if (configuration.getString(key) == ifEquals) {
            configuration[key] = value
        }
    }
}
