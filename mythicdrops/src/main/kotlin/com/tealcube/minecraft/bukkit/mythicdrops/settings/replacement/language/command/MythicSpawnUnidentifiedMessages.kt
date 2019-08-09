package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.SpawnUnidentifiedMessages
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

data class MythicSpawnUnidentifiedMessages internal constructor(
    override val success: String = "",
    override val failure: String = ""
) : SpawnUnidentifiedMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) =
            MythicSpawnUnidentifiedMessages(
                configurationSection.getNonNullString("success"),
                configurationSection.getNonNullString("failure")
            )
    }
}
