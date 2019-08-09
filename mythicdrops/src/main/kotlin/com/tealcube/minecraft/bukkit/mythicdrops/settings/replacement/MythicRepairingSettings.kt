package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.RepairingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

data class MythicRepairingSettings(
    override val version: String = "",
    override val isPlaySounds: Boolean = false
) : RepairingSettings {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicRepairingSettings(
            configurationSection.getNonNullString("version"),
            configurationSection.getBoolean("play-sounds")
        )
    }
}