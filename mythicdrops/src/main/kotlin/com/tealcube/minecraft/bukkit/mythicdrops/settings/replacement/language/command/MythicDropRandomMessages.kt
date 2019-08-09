package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.DropRandomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

data class MythicDropRandomMessages internal constructor(
    override val success: String = "",
    override val failure: String = ""
) : DropRandomMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicDropRandomMessages(
            configurationSection.getNonNullString("success"),
            configurationSection.getNonNullString("failure")
        )
    }
}