package com.tealcube.minecraft.bukkit.mythicdrops.config.migration.models

import com.squareup.moshi.JsonClass
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import org.bukkit.configuration.ConfigurationSection

/**
 * Represents a step that can be run as part of a migration.
 */
sealed class ConfigMigrationStep(val reason: String = "") {
    companion object {
        val adapterFactory: PolymorphicJsonAdapterFactory<ConfigMigrationStep> =
            PolymorphicJsonAdapterFactory.of(ConfigMigrationStep::class.java, "stepType")
                .withSubtype(SetBooleanConfigMigrationStep::class.java, "set_boolean")
                .withSubtype(SetDoubleConfigMigrationStep::class.java, "set_double")
                .withSubtype(SetIntConfigMigrationStep::class.java, "set_int")
                .withSubtype(SetStringListConfigMigrationStep::class.java, "set_string_list")
                .withSubtype(SetStringConfigMigrationStep::class.java, "set_string")
                .withSubtype(DeleteConfigMigrationStep::class.java, "delete")
                .withSubtype(RenameConfigMigrationStep::class.java, "rename")
                .withSubtype(SetStringIfEqualsConfigMigrationStep::class.java, "set_string_if_equals")
                .withSubtype(
                    SetStringListIfKeyEqualsStringConfigMigrationStep::class.java,
                    "set_string_list_if_key_equals_string"
                )
                .withSubtype(ForEachConfigMigrationStep::class.java, "for_each")
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
data class ForEachConfigMigrationStep(
    val matchRegex: String,
    val configMigrationSteps: List<ConfigMigrationStep> = emptyList()
) : ConfigMigrationStep() {
    override fun migrate(configuration: ConfigurationSection) {
        val parentRegex = matchRegex.toRegex()
        val keysThatMatchParent = configuration.getKeys(true).filter { it.matches(parentRegex) }
        for (keyThatMatchesParent in keysThatMatchParent) {
            if (!configuration.isConfigurationSection(keyThatMatchesParent)) {
                continue
            }
            val matchesSection =
                configuration.getConfigurationSection(keyThatMatchesParent) ?: configuration.createSection(
                    keyThatMatchesParent
                )
            for (configMigrationStep in configMigrationSteps) {
                configMigrationStep.migrate(matchesSection)
            }
        }
    }
}

@JsonClass(generateAdapter = true)
data class RenameConfigMigrationStep(val from: String, val to: String) : ConfigMigrationStep() {
    override fun migrate(configuration: ConfigurationSection) {
        if (configuration[from] == null) {
            return
        }
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
data class SetStringListIfKeyEqualsStringConfigMigrationStep(
    val key: String,
    val value: List<String>,
    val ifKey: String,
    val ifValue: String
) : ConfigMigrationStep() {
    override fun migrate(configuration: ConfigurationSection) {
        if (configuration.getString(ifKey) == ifValue) {
            configuration[key] = value
        }
    }
}

@JsonClass(generateAdapter = true)
data class SetStringConfigMigrationStep(val key: String, val value: String) : ConfigMigrationStep() {
    override fun migrate(configuration: ConfigurationSection) {
        configuration[key] = value
    }
}

@JsonClass(generateAdapter = true)
data class SetStringIfEqualsConfigMigrationStep(val key: String, val value: String, val ifValue: String) :
    ConfigMigrationStep() {
    override fun migrate(configuration: ConfigurationSection) {
        if (configuration.getString(key) == ifValue) {
            configuration[key] = value
        }
    }
}
