package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.socketing.items

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.socketing.items.SocketGemCombinerOptions
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.socketing.items.combiner.BufferOptions
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.socketing.items.combiner.ClickToCombineOptions
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.socketing.items.combiner.IneligibleToCombineOptions
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.socketing.items.combiner.MythicBufferOptions
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.socketing.items.combiner.MythicClickToCombineOptions
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.socketing.items.combiner.MythicIneligibleToCombineOptions
import org.bukkit.configuration.ConfigurationSection

data class MythicSocketGemCombinerOptions(
    override val name: String = "",
    override val buffer: BufferOptions = MythicBufferOptions(),
    override val clickToCombine: ClickToCombineOptions = MythicClickToCombineOptions(),
    override val ineligibleToCombineOptions: IneligibleToCombineOptions = MythicIneligibleToCombineOptions()
) : SocketGemCombinerOptions {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicSocketGemCombinerOptions(
            configurationSection.getNonNullString("name"),
            MythicBufferOptions.fromConfigurationSection(configurationSection.getOrCreateSection("buffer")),
            MythicClickToCombineOptions.fromConfigurationSection(configurationSection.getOrCreateSection("click-to-combine")),
            MythicIneligibleToCombineOptions.fromConfigurationSection(configurationSection.getOrCreateSection("ineligible-to-combine"))
        )
    }
}
