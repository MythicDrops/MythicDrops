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
package com.tealcube.minecraft.bukkit.mythicdrops.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Dependency
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Subcommand
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicDropTracker
import com.tealcube.minecraft.bukkit.mythicdrops.sendMythicMessage
import io.pixeloutlaw.kindling.Log
import org.bukkit.command.CommandSender

@CommandAlias("mythicdrops|md")
internal class ReloadCommand : BaseCommand() {
    @field:Dependency
    lateinit var mythicDrops: MythicDrops

    @Description("Reloads the configuration and data of the plugin")
    @Subcommand("reload")
    @CommandPermission("mythicdrops.command.reload")
    fun reload(sender: CommandSender) {
        Log.info("Reloading the configuration files")
        MythicDropTracker.reset()
        mythicDrops.reloadSettings()
        mythicDrops.reloadItemGroups()
        mythicDrops.reloadTiers()
        mythicDrops.reloadNames()
        mythicDrops.reloadCustomItems()
        mythicDrops.reloadRepairCosts()
        mythicDrops.reloadSocketGems()
        mythicDrops.reloadRelations()
        mythicDrops.reloadSocketGemCombiners()
        Log.info("Done reloading the configuration files")
        sender.sendMythicMessage(mythicDrops.settingsManager.languageSettings.command.reloadConfig)
    }
}
