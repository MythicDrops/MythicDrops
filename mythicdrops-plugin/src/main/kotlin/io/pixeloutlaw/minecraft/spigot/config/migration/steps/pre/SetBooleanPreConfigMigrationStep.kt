package io.pixeloutlaw.minecraft.spigot.config.migration.steps.pre

import org.bukkit.configuration.ConfigurationSection

data class SetBooleanPreConfigMigrationStep(val key: String, val value: Boolean) : PreConfigMigrationStep {
    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): SetBooleanPreConfigMigrationStep {
            val key = map.getOrDefault("key", "").toString()
            val value = map.getOrDefault("value", false) as? Boolean ?: false
            return SetBooleanPreConfigMigrationStep(key, value)
        }
    }

    override fun migrate(configuration: ConfigurationSection) {
        configuration[key] = value
    }

    override fun serialize(): MutableMap<String, Any> =
        mutableMapOf("key" to key, "value" to value)
}
