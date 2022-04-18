package io.pixeloutlaw.minecraft.spigot.config.migration.steps.pre

import org.bukkit.configuration.ConfigurationSection

data class SetStringIfEqualsPreConfigMigrationStep(
    val key: String,
    val value: String,
    val ifValue: String
) : PreConfigMigrationStep {
    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): SetStringIfEqualsPreConfigMigrationStep {
            val key = map.getOrDefault("key", "").toString()
            val value = map.getOrDefault("value", "").toString()
            val ifValue = map.getOrDefault("ifValue", "").toString()
            return SetStringIfEqualsPreConfigMigrationStep(key, value, ifValue)
        }
    }

    override fun migrate(configuration: ConfigurationSection) {
        if (configuration.getString(key) == ifValue) {
            configuration[key] = value
        }
    }

    override fun serialize(): MutableMap<String, Any> =
        mutableMapOf("key" to key, "value" to value, "ifValue" to value)
}
