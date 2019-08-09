package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.LanguageSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.CommandMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.GeneralMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.IdentificationMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.RepairingMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.SocketingMessages
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.MythicCommandMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.MythicGeneralMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.MythicIdentificationMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.MythicRepairingMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.MythicSocketingMessages
import org.bukkit.configuration.ConfigurationSection

data class MythicLanguageSettings(
    override val version: String = "",
    override val general: GeneralMessages = MythicGeneralMessages(),
    override val command: CommandMessages = MythicCommandMessages(),
    override val identification: IdentificationMessages = MythicIdentificationMessages(),
    override val repairing: RepairingMessages = MythicRepairingMessages(),
    override val socketing: SocketingMessages = MythicSocketingMessages(),
    override val displayNames: Map<String, String> = emptyMap()
) : LanguageSettings {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection): MythicLanguageSettings {
            val displayNames = configurationSection.getOrCreateSection("display-names")
                .let { it.getKeys(false).map { key -> key to it.getNonNullString(key) }.toMap() }
            return MythicLanguageSettings(
                configurationSection.getNonNullString("version"),
                MythicGeneralMessages.fromConfigurationSection(configurationSection.getOrCreateSection("general")),
                MythicCommandMessages.fromConfigurationSection(configurationSection.getOrCreateSection("command")),
                MythicIdentificationMessages.fromConfigurationSection(configurationSection.getOrCreateSection("identification")),
                MythicRepairingMessages.fromConfigurationSection(configurationSection.getOrCreateSection("repairing")),
                MythicSocketingMessages.fromConfigurationSection(configurationSection.getOrCreateSection("socketing")),
                displayNames
            )
        }
    }
}