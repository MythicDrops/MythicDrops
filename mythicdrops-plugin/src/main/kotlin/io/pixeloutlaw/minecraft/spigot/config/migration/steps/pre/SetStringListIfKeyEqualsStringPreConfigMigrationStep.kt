package io.pixeloutlaw.minecraft.spigot.config.migration.steps.pre

import org.bukkit.configuration.ConfigurationSection

data class SetStringListIfKeyEqualsStringPreConfigMigrationStep(
    val key: String,
    val value: List<String>,
    val ifKey: String,
    val ifValue: String
) : PreConfigMigrationStep {
    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): SetStringListIfKeyEqualsStringPreConfigMigrationStep {
            val key = map.getOrDefault("key", "").toString()
            val rawValue = map.getOrDefault("value", emptyList<String>())
            val value = if (rawValue is List<*>) rawValue.filterIsInstance<String>() else emptyList()
            val ifKey = map.getOrDefault("ifKey", "").toString()
            val ifValue = map.getOrDefault("ifValue", "").toString()
            return SetStringListIfKeyEqualsStringPreConfigMigrationStep(key, value, ifKey, ifValue)
        }
    }

    override fun migrate(configuration: ConfigurationSection) {
        if (configuration.getString(ifKey) == ifValue) {
            configuration[key] = value
        }
    }

    override fun serialize(): MutableMap<String, Any> =
        mutableMapOf("key" to key, "value" to value, "ifKey" to ifKey, "ifValue" to ifValue)
}
