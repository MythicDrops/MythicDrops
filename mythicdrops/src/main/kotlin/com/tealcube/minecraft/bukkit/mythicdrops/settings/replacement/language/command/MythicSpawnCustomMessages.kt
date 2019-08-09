package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.SpawnCustomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

data class MythicSpawnCustomMessages internal constructor(
    override val success: String = "",
    override val failure: String = ""
) : SpawnCustomMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) =
            MythicSpawnCustomMessages(
                configurationSection.getNonNullString("success"),
                configurationSection.getNonNullString("failure")
            )
    }
}