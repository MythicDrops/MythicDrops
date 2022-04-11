package io.pixeloutlaw.minecraft.spigot.config.migration.steps.pre

import org.bukkit.configuration.ConfigurationSection

 data class RenamePreConfigMigrationStep(val from: String, val to: String) : PreConfigMigrationStep {
    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): RenamePreConfigMigrationStep {
            val from = map.getOrDefault("from", "").toString()
            val to = map.getOrDefault("to", "").toString()
            return RenamePreConfigMigrationStep(from, to)
        }
    }

    override fun migrate(configuration: ConfigurationSection) {
        if (configuration[from] == null) {
            return
        }
        configuration[to] = configuration[from]
        configuration[from] = null
    }

    override fun serialize(): MutableMap<String, Any> =
        mutableMapOf("from" to from, "to" to to)
}
