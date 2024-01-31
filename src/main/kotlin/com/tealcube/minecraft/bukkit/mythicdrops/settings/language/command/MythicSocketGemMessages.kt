package com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.SocketGemMessages
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

data class MythicSocketGemMessages(
    override val list: String = "",
    override val commands: String = "",
    override val effects: String = "",
    override val enchantments: String = ""
) : SocketGemMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) =
            MythicSocketGemMessages(
                list = configurationSection.getNonNullString("list"),
                commands = configurationSection.getNonNullString("commands"),
                effects = configurationSection.getNonNullString("effects"),
                enchantments = configurationSection.getNonNullString("enchantments")
            )
    }
}
