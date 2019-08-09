package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.ModifyMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.modify.EnchantmentMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.modify.LoreMessages
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command.modify.MythicEnchantmentMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command.modify.MythicLoreMessages
import org.bukkit.configuration.ConfigurationSection

data class MythicModifyMessages internal constructor(
    override val failure: String = "",
    override val name: String = "",
    override val lore: LoreMessages = MythicLoreMessages(),
    override val enchantment: EnchantmentMessages = MythicEnchantmentMessages()
) : ModifyMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicModifyMessages(
            configurationSection.getNonNullString("failure"),
            configurationSection.getNonNullString("name"),
            MythicLoreMessages.fromConfigurationSection(configurationSection.getOrCreateSection("lore")),
            MythicEnchantmentMessages.fromConfigurationSection(configurationSection.getOrCreateSection("enchantment"))
        )
    }
}
