package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.GiveRandomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

data class MythicGiveRandomMessages internal constructor(
    override val receiverSuccess: String = "",
    override val receiverFailure: String,
    override val senderSuccess: String = "",
    override val senderFailure: String = ""
) : GiveRandomMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicGiveRandomMessages(
            configurationSection.getNonNullString("receiver-success"),
            configurationSection.getNonNullString("receiver-failure"),
            configurationSection.getNonNullString("sender-success"),
            configurationSection.getNonNullString("sender-failure")
        )
    }
}