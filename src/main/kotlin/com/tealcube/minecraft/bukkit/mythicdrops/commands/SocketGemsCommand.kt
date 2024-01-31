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
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Dependency
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Subcommand
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketEffect
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.sendMythicMessage
import org.bukkit.command.CommandSender

@CommandAlias("mythicdrops|md")
internal class SocketGemsCommand : BaseCommand() {
    @field:Dependency
    lateinit var mythicDrops: MythicDrops

    @Description("Prints the socket gems that the plugin is aware of.")
    @Subcommand("socketgems")
    @CommandPermission("mythicdrops.command.socketgems")
    fun socketGemsCommand(
        sender: CommandSender,
        @Default("*") socketGem: SocketGem?
    ) {
        if (socketGem == null) {
            sender.sendMythicMessage(
                mythicDrops.settingsManager.languageSettings.command.socketGems.list,
                "%socketgems%" to mythicDrops.socketGemManager.get().joinToString(", ") { it.name }
            )
            return
        }
        sender.sendMythicMessage(
            mythicDrops.settingsManager.languageSettings.command.socketGems.commands,
            "%socketgem%" to socketGem.name,
            "%commands%" to socketGem.commands.joinToString(", ") { "${it.runner}:${it.command}" }
        )
        sender.sendMythicMessage(
            mythicDrops.settingsManager.languageSettings.command.socketGems.effects,
            "%socketgem%" to socketGem.name,
            "%effects%" to socketGem.socketEffects.joinToString(", ", transform = SocketEffect::toDebugString)
        )
        sender.sendMythicMessage(
            mythicDrops.settingsManager.languageSettings.command.socketGems.enchantments,
            "%socketgem%" to socketGem.name,
            "%enchantments%" to socketGem.enchantments.joinToString(", ", transform = MythicEnchantment::toString)
        )
    }
}
