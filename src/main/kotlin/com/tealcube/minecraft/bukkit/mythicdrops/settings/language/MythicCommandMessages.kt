/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2019 Richard Harrah
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.settings.language

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.CommandMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.CustomCreateMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.DropCustomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.DropExtenderMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.DropGemMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.DropRandomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.DropTomeMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.DropUnidentifiedMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.GiveCustomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.GiveExtenderMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.GiveGemMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.GiveRandomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.GiveTomeMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.GiveUnidentifiedMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.ItemGroupMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.ModifyMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.SocketGemCombinerAddMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.SocketGemCombinerRemoveMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.SpawnCustomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.SpawnExtenderMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.SpawnGemMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.SpawnRandomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.SpawnTomeMessages
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.command.SpawnUnidentifiedMessages
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicCustomCreateMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicDropCustomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicDropExtenderMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicDropGemMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicDropRandomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicDropTomeMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicDropUnidentifiedMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicGiveCustomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicGiveExtenderMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicGiveGemMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicGiveRandomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicGiveTomeMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicGiveUnidentifiedMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicItemGroupMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicModifyMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicSocketGemCombinerAddMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicSocketGemCombinerRemoveMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicSpawnCustomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicSpawnExtenderMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicSpawnGemMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicSpawnRandomMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicSpawnTomeMessages
import com.tealcube.minecraft.bukkit.mythicdrops.settings.language.command.MythicSpawnUnidentifiedMessages
import org.bukkit.configuration.ConfigurationSection

internal data class MythicCommandMessages(
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
    override val itemGroupDoesNotExist: String = "",
    override val unknownPlayer: String = "",
    override val customItemList: String = "",
    override val socketGemList: String = "",
    @Deprecated("Use itemGroups instead.", replaceWith = ReplaceWith("itemGroups.list"))
    override val itemGroupList: String = "",
    @Deprecated("Use itemGroups instead.", replaceWith = ReplaceWith("itemGroups.materialsList"))
    override val itemGroupMaterialsList: String = "",
    override val tierList: String = "",
    override val debug: String = "",
    override val help: String = "",
    override val customCreate: CustomCreateMessages = MythicCustomCreateMessages(),
    override val dropCustom: DropCustomMessages = MythicDropCustomMessages(),
    override val dropExtender: DropExtenderMessages = MythicDropExtenderMessages(),
    override val dropGem: DropGemMessages = MythicDropGemMessages(),
    override val dropRandom: DropRandomMessages = MythicDropRandomMessages(),
    override val dropTome: DropTomeMessages = MythicDropTomeMessages(),
    override val dropUnidentified: DropUnidentifiedMessages = MythicDropUnidentifiedMessages(),
    override val giveCustom: GiveCustomMessages = MythicGiveCustomMessages(),
    override val giveExtender: GiveExtenderMessages = MythicGiveExtenderMessages(),
    override val giveGem: GiveGemMessages = MythicGiveGemMessages(),
    override val giveRandom: GiveRandomMessages = MythicGiveRandomMessages(),
    override val giveTome: GiveTomeMessages = MythicGiveTomeMessages(),
    override val giveUnidentified: GiveUnidentifiedMessages = MythicGiveUnidentifiedMessages(),
    override val itemGroups: ItemGroupMessages = MythicItemGroupMessages(),
    override val modify: ModifyMessages = MythicModifyMessages(),
    override val spawnCustom: SpawnCustomMessages = MythicSpawnCustomMessages(),
    override val spawnExtender: SpawnExtenderMessages = MythicSpawnExtenderMessages(),
    override val spawnGem: SpawnGemMessages = MythicSpawnGemMessages(),
    override val spawnRandom: SpawnRandomMessages = MythicSpawnRandomMessages(),
    override val spawnTome: SpawnTomeMessages = MythicSpawnTomeMessages(),
    override val spawnUnidentified: SpawnUnidentifiedMessages = MythicSpawnUnidentifiedMessages(),
    override val socketGemCombinerAdd: SocketGemCombinerAddMessages = MythicSocketGemCombinerAddMessages(),
    override val socketGemCombinerRemove: SocketGemCombinerRemoveMessages = MythicSocketGemCombinerRemoveMessages(),
    override val socketGemCombinerOpen: String = ""
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
            configurationSection.getNonNullString("item-group-does-not-exist"),
            configurationSection.getNonNullString("unknown-player"),
            configurationSection.getNonNullString("custom-item-list"),
            configurationSection.getNonNullString("socket-gem-list"),
            configurationSection.getNonNullString("item-groups.list"),
            configurationSection.getNonNullString("item-groups.materials-list"),
            configurationSection.getNonNullString("tier-list"),
            configurationSection.getNonNullString("debug"),
            configurationSection.getNonNullString("help"),
            MythicCustomCreateMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("custom-create")
            ),
            MythicDropCustomMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("drop-custom")
            ),
            MythicDropExtenderMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("drop-extender")
            ),
            MythicDropGemMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("drop-gem")
            ),
            MythicDropRandomMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("drop-random")
            ),
            MythicDropTomeMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("drop-tome")
            ),
            MythicDropUnidentifiedMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("drop-unidentified")
            ),
            MythicGiveCustomMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("give-custom")
            ),
            MythicGiveExtenderMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("give-extender")
            ),
            MythicGiveGemMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("give-gem")
            ),
            MythicGiveRandomMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("give-random")
            ),
            MythicGiveTomeMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("give-tome")
            ),
            MythicGiveUnidentifiedMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("give-unidentified")
            ),
            MythicItemGroupMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("item-groups")
            ),
            MythicModifyMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("modify")
            ),
            MythicSpawnCustomMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("spawn-custom")
            ),
            MythicSpawnExtenderMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("spawn-extender")
            ),
            MythicSpawnGemMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("spawn-gem")
            ),
            MythicSpawnRandomMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("spawn-random")
            ),
            MythicSpawnTomeMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("spawn-tome")
            ),
            MythicSpawnUnidentifiedMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("spawn-unidentified")
            ),
            MythicSocketGemCombinerAddMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("socket-gem-combiner-add")
            ),
            MythicSocketGemCombinerRemoveMessages.fromConfigurationSection(
                configurationSection.getOrCreateSection("socket-gem-combiner-remove")
            ),
            configurationSection.getNonNullString("socket-gem-combiner-open")
        )
    }
}
