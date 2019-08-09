package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.SpawnGemMessages
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

data class MythicSpawnGemMessages internal constructor(
    override val success: String = "",
    override val failure: String = ""
) : SpawnGemMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) =
            MythicSpawnGemMessages(
                configurationSection.getNonNullString("success"),
                configurationSection.getNonNullString("failure")
            )
    }
}