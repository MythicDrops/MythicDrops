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
package com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language

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
    val unknownPlayer: String
    val tierList: String
    val debug: String
    val help: String
    val customCreate: CustomCreateMessages
    val dropRandom: DropRandomMessages
    val giveCustom: GiveCustomMessages
    val giveGem: GiveGemMessages
    val giveTome: GiveTomeMessages
    val giveUnidentified: GiveUnidentifiedMessages
    val modify: ModifyMessages
    val spawnCustom: SpawnCustomMessages
    val spawnGem: SpawnGemMessages
    val spawnRandom: SpawnRandomMessages
    val spawnTome: SpawnTomeMessages
    val spawnUnidentified: SpawnUnidentifiedMessages
    val socketGemCombinerAdd: SocketGemCombinerAddMessages
    val socketGemCombinerRemove: SocketGemCombinerRemoveMessages
}
