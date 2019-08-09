package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.IdentificationMessages
import org.bukkit.configuration.ConfigurationSection

data class MythicIdentificationMessages internal constructor(
    override val success: String = "&6[MythicDrops] &AYou successfully identified your item!",
    override val failure: String = "&6[MythicDrops] &CYou cannot identify that item!",
    override val notUnidentifiedItem: String = "&6[MythicDrops] &CYou cannot identify an already identified item!"
) : IdentificationMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicIdentificationMessages(
            configurationSection.getString("success") ?: "&6[MythicDrops] &AYou successfully identified your item!",
            configurationSection.getString("failure") ?: "&6[MythicDrops] &CYou cannot identify that item!",
            configurationSection.getString("not-unidentified-item")
                ?: "&6[MythicDrops] &CYou cannot identify an already identified item!"
        )
    }
}
