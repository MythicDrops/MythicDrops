package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.RepairingMessages
import org.bukkit.configuration.ConfigurationSection

data class MythicRepairingMessages internal constructor(
    override val cannotUse: String = "&6[MythicDrops] &CYou cannot repair that item!",
    override val doNotHave: String = "&6[MythicDrops] &CYou do not have enough materials to repair that item!",
    override val success: String = "&6[MythicDrops] &AYou successfully repaired your item!",
    override val instructions: String = "&6[MythicDrops] &ASmack this item on an anvil again to repair it!"
) : RepairingMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicRepairingMessages(
            configurationSection.getString("cannot-use") ?: "&6[MythicDrops] &CYou cannot repair that item!",
            configurationSection.getString("do-not-have")
                ?: "&6[MythicDrops] &CYou do not have enough materials to repair that item!",
            configurationSection.getString("success") ?: "&6[MythicDrops] &AYou successfully repaired your item!",
            configurationSection.getString("instructions")
                ?: "&6[MythicDrops] &ASmack this item on an anvil again to repair it!"
        )
    }
}