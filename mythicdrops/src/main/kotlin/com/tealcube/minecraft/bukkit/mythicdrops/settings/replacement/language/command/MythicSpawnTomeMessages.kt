package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.SpawnTomeMessages
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

data class MythicSpawnTomeMessages internal constructor(
    override val success: String = "",
    override val failure: String = ""
) : SpawnTomeMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) =
            MythicSpawnTomeMessages(
                configurationSection.getNonNullString("success"),
                configurationSection.getNonNullString("failure")
            )
    }
}