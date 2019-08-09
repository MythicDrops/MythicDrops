package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.socketing

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.socketing.SocketingOptions
import com.tealcube.minecraft.bukkit.mythicdrops.getChatColor
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection

data class MythicSocketingOptions internal constructor(
    override val isPreventCraftingWithGems: Boolean = false,
    override val isPreventMultipleNameChangesFromSockets: Boolean = false,
    override val isUseAttackerItemInHand: Boolean = false,
    override val isUseAttackerArmorEquipped: Boolean = false,
    override val isUseDefenderItemInHand: Boolean = false,
    override val isUseDefenderArmorEquipped: Boolean = false,
    override val socketGemMaterialIds: Set<Material> = emptySet(),
    override val defaultSocketNameColorOnItems: ChatColor = ChatColor.GOLD,
    override val useTierColorForSocketName: Boolean = false
) : SocketingOptions {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicSocketingOptions(
            configurationSection.getBoolean("prevent-crafting-with-gems"),
            configurationSection.getBoolean("prevent-multiple-name-changes-from-sockets"),
            configurationSection.getBoolean("use-attacker-item-in-hand"),
            configurationSection.getBoolean("use-attacker-armor-equipped"),
            configurationSection.getBoolean("use-defender-item-in-hand"),
            configurationSection.getBoolean("use-defender-armor-equipped"),
            configurationSection.getStringList("socket-gem-material-ids").mapNotNull { Material.getMaterial(it) }.toSet(),
            configurationSection.getChatColor("default-socket-name-color-on-items", ChatColor.GOLD),
            configurationSection.getBoolean("use-tier-color-for-socket-name")
        )
    }
}