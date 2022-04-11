package io.pixeloutlaw.minecraft.spigot.config.migration.steps.pre

import org.bukkit.configuration.ConfigurationSection

data class RenameEachPreConfigMigrationStep(val matchRegex: String, val to: String) : PreConfigMigrationStep {
    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): RenameEachPreConfigMigrationStep {
            val matchRegex = map.getOrDefault("matchRegex", "").toString()
            val to = map.getOrDefault("to", "").toString()
            return RenameEachPreConfigMigrationStep(matchRegex, to)
        }
    }

    override fun migrate(configuration: ConfigurationSection) {
        val parentRegex = matchRegex.toRegex()
        val keysThatMatchParent = configuration.getKeys(true).filter { it.matches(parentRegex) }
        for (keyThatMatchesParent in keysThatMatchParent) {
            val oldValue = configuration[keyThatMatchesParent]
            configuration[keyThatMatchesParent] = null
            configuration[to.replace("%self%", keyThatMatchesParent)] = oldValue
        }
    }

    override fun serialize(): MutableMap<String, Any> =
        mutableMapOf("matchRegex" to matchRegex, "to" to to)
}
