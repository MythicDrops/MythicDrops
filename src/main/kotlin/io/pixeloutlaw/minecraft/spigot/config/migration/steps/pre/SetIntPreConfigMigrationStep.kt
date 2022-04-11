package io.pixeloutlaw.minecraft.spigot.config.migration.steps.pre

import org.bukkit.configuration.ConfigurationSection

 data class SetIntPreConfigMigrationStep(val key: String, val value: Int) : PreConfigMigrationStep {
    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): SetIntPreConfigMigrationStep {
            val key = map.getOrDefault("key", "").toString()
            val value = map.getOrDefault("value", 0) as? Int ?: 0
            return SetIntPreConfigMigrationStep(key, value)
        }
    }

    override fun migrate(configuration: ConfigurationSection) {
        configuration[key] = value
    }

    override fun serialize(): MutableMap<String, Any> =
        mutableMapOf("key" to key, "value" to value)
}
