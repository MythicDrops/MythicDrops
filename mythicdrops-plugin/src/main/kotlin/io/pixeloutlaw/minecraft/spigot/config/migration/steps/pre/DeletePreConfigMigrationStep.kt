package io.pixeloutlaw.minecraft.spigot.config.migration.steps.pre

import org.bukkit.configuration.ConfigurationSection

data class DeletePreConfigMigrationStep(val path: String) : PreConfigMigrationStep {
    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): DeletePreConfigMigrationStep =
            DeletePreConfigMigrationStep(map.getOrDefault("path", "").toString())
    }

    override fun migrate(configuration: ConfigurationSection) {
        configuration[path] = null
    }

    override fun serialize(): MutableMap<String, Any> =
        mutableMapOf("path" to path)
}
