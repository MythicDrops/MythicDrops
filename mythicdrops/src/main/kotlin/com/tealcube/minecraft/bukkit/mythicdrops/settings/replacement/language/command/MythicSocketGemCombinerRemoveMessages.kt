package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.SocketGemCombinerRemoveMessages
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

data class MythicSocketGemCombinerRemoveMessages internal constructor(
    override val success: String = "",
    override val failure: String = ""
) : SocketGemCombinerRemoveMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) =
            MythicSocketGemCombinerRemoveMessages(
                configurationSection.getNonNullString("success"),
                configurationSection.getNonNullString("failure")
            )
    }
}