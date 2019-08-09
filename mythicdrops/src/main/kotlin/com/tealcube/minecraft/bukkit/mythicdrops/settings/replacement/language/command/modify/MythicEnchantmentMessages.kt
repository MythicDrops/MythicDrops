package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command.modify

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.modify.EnchantmentMessages
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

data class MythicEnchantmentMessages internal constructor(
    override val add: String = "",
    override val remove: String = ""
) : EnchantmentMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicEnchantmentMessages(
            configurationSection.getNonNullString("add"),
            configurationSection.getNonNullString("remove")
        )
    }
}