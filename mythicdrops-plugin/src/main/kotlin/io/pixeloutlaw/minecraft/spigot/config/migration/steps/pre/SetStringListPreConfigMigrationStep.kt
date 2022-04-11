package io.pixeloutlaw.minecraft.spigot.config.migration.steps.pre

import org.bukkit.configuration.ConfigurationSection

data class SetStringListPreConfigMigrationStep(val key: String, val value: List<String>) : PreConfigMigrationStep {
    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): SetStringListPreConfigMigrationStep {
            val key = map.getOrDefault("key", "").toString()
            val rawValue = map.getOrDefault("value", emptyList<String>())
            val value = if (rawValue is List<*>) rawValue.filterIsInstance<String>() else emptyList()
            return SetStringListPreConfigMigrationStep(key, value)
        }
    }

    override fun migrate(configuration: ConfigurationSection) {
        configuration[key] = value
    }

    override fun serialize(): MutableMap<String, Any> =
        mutableMapOf("key" to key, "value" to value)
}
