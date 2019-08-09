package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.SocketGemCombinerAddMessages
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

data class MythicSocketGemCombinerAddMessages internal constructor(
    override val success: String = "",
    override val failure: String = ""
) : SocketGemCombinerAddMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicSocketGemCombinerAddMessages(
            configurationSection.getNonNullString("success"),
            configurationSection.getNonNullString("failure")
        )
    }
}