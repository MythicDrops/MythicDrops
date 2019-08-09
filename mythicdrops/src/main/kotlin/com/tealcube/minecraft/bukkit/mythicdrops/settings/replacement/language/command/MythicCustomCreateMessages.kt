package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.CustomCreateMessages
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

data class MythicCustomCreateMessages internal constructor(
    override val success: String = "",
    override val failure: String = ""
) : CustomCreateMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicCustomCreateMessages(
            configurationSection.getNonNullString("success"),
            configurationSection.getNonNullString("failure")
        )
    }
}