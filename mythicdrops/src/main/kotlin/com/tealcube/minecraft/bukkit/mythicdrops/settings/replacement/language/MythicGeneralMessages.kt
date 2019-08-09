package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.GeneralMessages
import org.bukkit.configuration.ConfigurationSection

data class MythicGeneralMessages internal constructor(
    override val foundItemBroadcast: String = "&6[MythicDrops] &F%receiver%&A has found a %item%!"
) : GeneralMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicGeneralMessages(
            configurationSection.getString("found-item-broadcast")
                ?: "&6[MythicDrops] &F%receiver%&A has found a %item%!"
        )
    }
}