package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.socketing.items

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.socketing.items.SocketGemOptions
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

data class MythicSocketGemOptions(
    override val name: String = "",
    override val lore: List<String> = emptyList(),
    override val familyLore: List<String> = emptyList(),
    override val socketTypeLore: List<String> = emptyList()
) : SocketGemOptions {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicSocketGemOptions(
            configurationSection.getNonNullString("name"),
            configurationSection.getStringList("lore"),
            configurationSection.getStringList("family-lore"),
            configurationSection.getStringList("socket-type-lore")
        )
    }
}