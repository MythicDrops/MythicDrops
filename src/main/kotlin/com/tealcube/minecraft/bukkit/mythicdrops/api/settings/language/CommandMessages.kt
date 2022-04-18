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
package com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language

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

/**
 * Represents the `command` section in the language.yml. Names map practically one-to-one.
 */
interface CommandMessages {
    val onlyPlayers: String
    val noAccess: String
    val reloadConfig: String
    val reloadPlugin: String
    val saveConfig: String
    val tierDoesNotExist: String
    val customItemDoesNotExist: String
    val playerDoesNotExist: String
    val worldDoesNotExist: String
    val socketGemDoesNotExist: String
    val itemGroupDoesNotExist: String
    val unknownPlayer: String
    val customItemList: String
    val socketGemList: String

    @Deprecated("Use itemGroups instead.", ReplaceWith("itemGroups.list"))
    val itemGroupList: String

    @Deprecated("Use itemGroups instead.", ReplaceWith("itemGroups.materialsList"))
    val itemGroupMaterialsList: String
    val tierList: String
    val debug: String
    val help: String
    val customCreate: CustomCreateMessages
    val dropCustom: DropCustomMessages
    val dropExtender: DropExtenderMessages
    val dropGem: DropGemMessages
    val dropRandom: DropRandomMessages
    val dropTome: DropTomeMessages
    val dropUnidentified: DropUnidentifiedMessages
    val giveCustom: GiveCustomMessages
    val giveExtender: GiveExtenderMessages
    val giveGem: GiveGemMessages
    val giveRandom: GiveRandomMessages
    val giveTome: GiveTomeMessages
    val giveUnidentified: GiveUnidentifiedMessages
    val itemGroups: ItemGroupMessages
    val modify: ModifyMessages
    val spawnCustom: SpawnCustomMessages
    val spawnExtender: SpawnExtenderMessages
    val spawnGem: SpawnGemMessages
    val spawnRandom: SpawnRandomMessages
    val spawnTome: SpawnTomeMessages
    val spawnUnidentified: SpawnUnidentifiedMessages
    val socketGemCombinerAdd: SocketGemCombinerAddMessages
    val socketGemCombinerRemove: SocketGemCombinerRemoveMessages
    val socketGemCombinerOpen: String
}
