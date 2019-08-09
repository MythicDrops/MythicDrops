package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.socketing

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.socketing.SocketingItems
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.socketing.items.SocketGemCombinerOptions
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.socketing.items.SocketGemOptions
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.socketing.items.SocketedItemOptions
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.socketing.items.MythicSocketGemCombinerOptions
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.socketing.items.MythicSocketGemOptions
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.socketing.items.MythicSocketedItemOptions
import org.bukkit.configuration.ConfigurationSection

data class MythicSocketingItems internal constructor(
    override val socketedItem: SocketedItemOptions = MythicSocketedItemOptions(),
    override val socketGem: SocketGemOptions = MythicSocketGemOptions(),
    override val socketGemCombiner: SocketGemCombinerOptions = MythicSocketGemCombinerOptions()
) : SocketingItems {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicSocketingItems(
            MythicSocketedItemOptions.fromConfigurationSection(configurationSection.getOrCreateSection("socketed-item")),
            MythicSocketGemOptions.fromConfigurationSection(configurationSection.getOrCreateSection("socket-gem")),
            MythicSocketGemCombinerOptions.fromConfigurationSection(configurationSection.getOrCreateSection("socket-gem-combiner"))
        )
    }
}