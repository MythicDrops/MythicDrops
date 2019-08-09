package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command.modify

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.modify.LoreMessages
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

data class MythicLoreMessages internal constructor(
    override val add: String = "",
    override val remove: String = "",
    override val insert: String = "",
    override val set: String = ""
) : LoreMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicLoreMessages(
            configurationSection.getNonNullString("add"),
            configurationSection.getNonNullString("remove"),
            configurationSection.getNonNullString("insert"),
            configurationSection.getNonNullString("set")
        )
    }
}