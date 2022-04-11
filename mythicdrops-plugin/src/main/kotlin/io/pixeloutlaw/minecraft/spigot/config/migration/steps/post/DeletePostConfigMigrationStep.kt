package io.pixeloutlaw.minecraft.spigot.config.migration.steps.post

import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import org.bukkit.configuration.ConfigurationSection

data class DeletePostConfigMigrationStep(val path: String) : PostConfigMigrationStep {
    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): DeletePostConfigMigrationStep =
            DeletePostConfigMigrationStep(map.getOrDefault("path", "").toString())
    }

    override fun migrate(configuration: ConfigurationSection, mythicDrops: MythicDrops) {
        configuration[path] = null
    }

    override fun serialize(): MutableMap<String, Any> =
        mutableMapOf("path" to path)
}
