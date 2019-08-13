package io.pixeloutlaw.minecraft.spigot.config.migration.models

import com.squareup.moshi.JsonClass
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import org.bukkit.configuration.ConfigurationSection

sealed class ConfigMigrationStep {
    companion object {
        val adapterFactory = PolymorphicJsonAdapterFactory.of(ConfigMigrationStep::class.java, "stepType")
            .withSubtype(AddBooleanConfigMigrationStep::class.java, "add_boolean")
            .withSubtype(AddIntConfigMigrationStep::class.java, "add_int")
            .withSubtype(AddStringListConfigMigrationStep::class.java, "add_string_list")
            .withSubtype(AddStringConfigMigrationStep::class.java, "add_string")
            .withSubtype(DeleteConfigMigrationStep::class.java, "delete")
            .withSubtype(RenameConfigMigrationStep::class.java, "rename")
    }

    abstract fun migrate(configuration: ConfigurationSection)
}

@JsonClass(generateAdapter = true)
data class AddBooleanConfigMigrationStep(val key: String, val value: Boolean) : ConfigMigrationStep() {
    override fun migrate(configuration: ConfigurationSection) {
        configuration[key] = value
    }
}

@JsonClass(generateAdapter = true)
data class AddIntConfigMigrationStep(val key: String, val value: Int) : ConfigMigrationStep() {
    override fun migrate(configuration: ConfigurationSection) {
        configuration[key] = value
    }
}

@JsonClass(generateAdapter = true)
data class AddStringListConfigMigrationStep(val key: String, val value: List<String> = emptyList()) :
    ConfigMigrationStep() {
    override fun migrate(configuration: ConfigurationSection) {
        configuration[key] = value
    }
}

@JsonClass(generateAdapter = true)
data class AddStringConfigMigrationStep(val key: String, val value: String) : ConfigMigrationStep() {
    override fun migrate(configuration: ConfigurationSection) {
        configuration[key] = value
    }
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
