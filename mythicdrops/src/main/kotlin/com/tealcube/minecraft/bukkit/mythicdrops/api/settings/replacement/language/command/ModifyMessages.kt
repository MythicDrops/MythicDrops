package com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.modify.EnchantmentMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.modify.LoreMessages

/**
 * Represents the `command.modify` section in the language.yml. Names map practically one-to-one.
 */
interface ModifyMessages {
    val failure: String
    val name: String
    val lore: LoreMessages
    val enchantment: EnchantmentMessages
}