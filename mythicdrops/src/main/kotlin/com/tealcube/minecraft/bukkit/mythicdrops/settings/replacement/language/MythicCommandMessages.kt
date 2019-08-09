package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.CommandMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.CustomCreateMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.DropRandomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.GiveCustomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.GiveGemMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.GiveTomeMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.GiveUnidentifiedMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.ModifyMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.SocketGemCombinerAddMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.SocketGemCombinerRemoveMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.SpawnCustomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.SpawnGemMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.SpawnRandomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.SpawnTomeMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.command.SpawnUnidentifiedMessages
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command.MythicCustomCreateMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command.MythicDropRandomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command.MythicGiveCustomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command.MythicGiveGemMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command.MythicGiveTomeMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command.MythicGiveUnidentifiedMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command.MythicModifyMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command.MythicSocketGemCombinerAddMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command.MythicSocketGemCombinerRemoveMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command.MythicSpawnCustomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command.MythicSpawnGemMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command.MythicSpawnRandomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command.MythicSpawnTomeMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language.command.MythicSpawnUnidentifiedMessages
import org.bukkit.configuration.ConfigurationSection

data class MythicCommandMessages internal constructor(
    override val onlyPlayers: String = "",
    override val noAccess: String = "",
    override val reloadConfig: String = "",
    override val reloadPlugin: String = "",
    override val saveConfig: String = "",
    override val tierDoesNotExist: String = "",
    override val customItemDoesNotExist: String = "",
    override val playerDoesNotExist: String = "",
    override val worldDoesNotExist: String = "",
    override val socketGemDoesNotExist: String = "",
    override val unknownPlayer: String = "",
    override val tierList: String = "",
    override val debug: String = "",
    override val help: String = "",
    override val customCreate: CustomCreateMessages = MythicCustomCreateMessages(),
    override val dropRandom: DropRandomMessages = MythicDropRandomMessages(),
    override val giveCustom: GiveCustomMessages = MythicGiveCustomMessages(),
    override val giveGem: GiveGemMessages = MythicGiveGemMessages(),
    override val giveTome: GiveTomeMessages = MythicGiveTomeMessages(),
    override val giveUnidentified: GiveUnidentifiedMessages = MythicGiveUnidentifiedMessages(),
    override val modify: ModifyMessages = MythicModifyMessages(),
    override val spawnCustom: SpawnCustomMessages = MythicSpawnCustomMessages(),
    override val spawnGem: SpawnGemMessages = MythicSpawnGemMessages(),
    override val spawnRandom: SpawnRandomMessages = MythicSpawnRandomMessages(),
    override val spawnTome: SpawnTomeMessages = MythicSpawnTomeMessages(),
    override val spawnUnidentified: SpawnUnidentifiedMessages = MythicSpawnUnidentifiedMessages(),
    override val socketGemCombinerAdd: SocketGemCombinerAddMessages = MythicSocketGemCombinerAddMessages(),
    override val socketGemCombinerRemove: SocketGemCombinerRemoveMessages = MythicSocketGemCombinerRemoveMessages()
) : CommandMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicCommandMessages(
            configurationSection.getNonNullString("only-players"),
            configurationSection.getNonNullString("no-access"),
            configurationSection.getNonNullString("reload-config"),
            configurationSection.getNonNullString("reload-plugin"),
            configurationSection.getNonNullString("save-config"),
            configurationSection.getNonNullString("tier-does-not-exist"),
            configurationSection.getNonNullString("custom-item-does-not-exist"),
            configurationSection.getNonNullString("player-does-not-exist"),
            configurationSection.getNonNullString("world-does-not-exist"),
            configurationSection.getNonNullString("socket-gem-does-not-exist"),
            configurationSection.getNonNullString("unknown-player"),
            configurationSection.getNonNullString("tier-list"),
            configurationSection.getNonNullString("debug"),
            configurationSection.getNonNullString("help"),
            MythicCustomCreateMessages.fromConfigurationSection(configurationSection.getOrCreateSection("custom-create")),
            MythicDropRandomMessages.fromConfigurationSection(configurationSection.getOrCreateSection("drop-random")),
            MythicGiveCustomMessages.fromConfigurationSection(configurationSection.getOrCreateSection("give-custom")),
            MythicGiveGemMessages.fromConfigurationSection(configurationSection.getOrCreateSection("give-gem")),
            MythicGiveTomeMessages.fromConfigurationSection(configurationSection.getOrCreateSection("give-tome")),
            MythicGiveUnidentifiedMessages.fromConfigurationSection(configurationSection.getOrCreateSection("give-unidentified")),
            MythicModifyMessages.fromConfigurationSection(configurationSection.getOrCreateSection("modify")),
            MythicSpawnCustomMessages.fromConfigurationSection(configurationSection.getOrCreateSection("spawn-custom")),
            MythicSpawnGemMessages.fromConfigurationSection(configurationSection.getOrCreateSection("spawn-gem")),
            MythicSpawnRandomMessages.fromConfigurationSection(configurationSection.getOrCreateSection("spawn-random")),
            MythicSpawnTomeMessages.fromConfigurationSection(configurationSection.getOrCreateSection("spawn-tome")),
            MythicSpawnUnidentifiedMessages.fromConfigurationSection(configurationSection.getOrCreateSection("spawn-unidentified")),
            MythicSocketGemCombinerAddMessages.fromConfigurationSection(configurationSection.getOrCreateSection("socket-gem-combiner-add")),
            MythicSocketGemCombinerRemoveMessages.fromConfigurationSection(configurationSection.getOrCreateSection("socket-gem-combiner-remove"))
        )
    }
}