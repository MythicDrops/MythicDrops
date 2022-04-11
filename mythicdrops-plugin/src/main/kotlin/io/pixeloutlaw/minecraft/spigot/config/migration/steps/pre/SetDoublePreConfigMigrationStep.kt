package io.pixeloutlaw.minecraft.spigot.config.migration.steps.pre

import org.bukkit.configuration.ConfigurationSection

data class SetDoublePreConfigMigrationStep(val key: String, val value: Double) : PreConfigMigrationStep {
    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): SetDoublePreConfigMigrationStep {
            val key = map.getOrDefault("key", "").toString()
            val value = map.getOrDefault("value", 0.0) as? Double ?: 0.0
            return SetDoublePreConfigMigrationStep(key, value)
        }
    }

    override fun migrate(configuration: ConfigurationSection) {
        configuration[key] = value
    }

    override fun serialize(): MutableMap<String, Any> =
        mutableMapOf("key" to key, "value" to value)
}
