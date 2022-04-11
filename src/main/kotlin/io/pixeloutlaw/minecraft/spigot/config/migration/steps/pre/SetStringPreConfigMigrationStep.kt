package io.pixeloutlaw.minecraft.spigot.config.migration.steps.pre

import org.bukkit.configuration.ConfigurationSection

 data class SetStringPreConfigMigrationStep(val key: String, val value: String) : PreConfigMigrationStep {
    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): SetStringPreConfigMigrationStep {
            val key = map.getOrDefault("key", "").toString()
            val value = map.getOrDefault("value", "").toString()
            return SetStringPreConfigMigrationStep(key, value)
        }
    }

    override fun migrate(configuration: ConfigurationSection) {
        configuration[key] = value
    }

    override fun serialize(): MutableMap<String, Any> =
        mutableMapOf("key" to key, "value" to value)
}