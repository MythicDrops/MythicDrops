package io.pixeloutlaw.minecraft.spigot.config.migration.steps.pre

import org.bukkit.configuration.ConfigurationSection

data class ForEachPreConfigMigrationStep(
    val matchRegex: String,
    val preConfigMigrationSteps: List<PreConfigMigrationStep> = emptyList()
) : PreConfigMigrationStep {
    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): ForEachPreConfigMigrationStep {
            val matchRegex = map.getOrDefault("matchRegex", "").toString()
            val preConfigMigrationStepsFromMap =
                map.getOrDefault("configMigrationSteps", emptyList<PreConfigMigrationStep>())
            val preConfigMigrationSteps = if (preConfigMigrationStepsFromMap is List<*>) {
                preConfigMigrationStepsFromMap.filterIsInstance<PreConfigMigrationStep>()
            } else {
                emptyList()
            }
            return ForEachPreConfigMigrationStep(matchRegex, preConfigMigrationSteps)
        }
    }

    override fun migrate(configuration: ConfigurationSection) {
        val parentRegex = matchRegex.toRegex()
        val keysThatMatchParent = configuration.getKeys(true).filter { it.matches(parentRegex) }
        for (keyThatMatchesParent in keysThatMatchParent) {
            if (!configuration.isConfigurationSection(keyThatMatchesParent)) {
                continue
            }
            val matchesSection =
                configuration.getConfigurationSection(keyThatMatchesParent) ?: configuration.createSection(
                    keyThatMatchesParent
                )
            for (configMigrationStep in preConfigMigrationSteps) {
                configMigrationStep.migrate(matchesSection)
            }
        }
    }

    override fun serialize(): MutableMap<String, Any> =
        mutableMapOf("matchRegex" to matchRegex, "configMigrationSteps" to preConfigMigrationSteps)
}
