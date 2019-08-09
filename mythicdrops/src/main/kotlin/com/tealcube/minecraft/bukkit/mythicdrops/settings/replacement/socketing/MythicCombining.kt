package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.socketing

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.socketing.Combining
import org.bukkit.configuration.ConfigurationSection

data class MythicCombining internal constructor(
    override val isRequireSameFamily: Boolean = false,
    override val isRequireSameLevel: Boolean = false
) : Combining {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicCombining(
            configurationSection.getBoolean("require-same-family"),
            configurationSection.getBoolean("require-same-level")
        )
    }
}