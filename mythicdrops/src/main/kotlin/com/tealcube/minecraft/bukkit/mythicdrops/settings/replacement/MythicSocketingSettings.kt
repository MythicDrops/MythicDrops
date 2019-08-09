package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.SocketingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.socketing.Combining
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.socketing.SocketingItems
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.socketing.SocketingOptions
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.socketing.MythicCombining
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.socketing.MythicSocketingItems
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.socketing.MythicSocketingOptions
import org.bukkit.configuration.ConfigurationSection

data class MythicSocketingSettings(
    override val version: String = "",
    override val options: SocketingOptions = MythicSocketingOptions(),
    override val items: SocketingItems = MythicSocketingItems(),
    override val combining: Combining = MythicCombining()
) : SocketingSettings {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicSocketingSettings(
            configurationSection.getNonNullString("version"),
            MythicSocketingOptions.fromConfigurationSection(configurationSection.getOrCreateSection("options")),
            MythicSocketingItems.fromConfigurationSection(configurationSection.getOrCreateSection("items")),
            MythicCombining.fromConfigurationSection(configurationSection.getOrCreateSection("combining"))
        )
    }
}