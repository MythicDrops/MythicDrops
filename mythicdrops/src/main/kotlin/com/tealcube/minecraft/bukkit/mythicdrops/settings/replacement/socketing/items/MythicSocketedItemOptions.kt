package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.socketing.items

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.socketing.items.SocketedItemOptions
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

data class MythicSocketedItemOptions(
    override val socket: String = "",
    override val lore: List<String> = emptyList()
) : SocketedItemOptions {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicSocketedItemOptions(
            configurationSection.getNonNullString("socket"),
            configurationSection.getStringList("lore")
        )
    }
}