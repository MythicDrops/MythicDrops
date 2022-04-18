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
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Dependency
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Flags
import co.aikar.commands.annotation.Subcommand
import com.google.common.collect.Sets
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.locations.Vec3
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.sendMythicMessage
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.Locale

@CommandAlias("mythicdrops|md")
internal class CombinerCommands : BaseCommand() {
    @Subcommand("combiners")
    class NestedCombinerCommands(@Suppress("UNUSED_PARAMETER") parent: BaseCommand) : BaseCommand() {
        @field:Dependency
        lateinit var mythicDrops: MythicDrops

        @Description("Lists the socket gem combiners and their locations.")
        @Subcommand("list")
        @CommandPermission("mythicdrops.command.combiners.list")
        fun listCombinersCommand(sender: CommandSender) {
            sender.sendMessage("&6MythicDrops Socket Gem Combiners".chatColorize())
            mythicDrops.socketGemCombinerManager
                .get()
                .forEach { socketGemCombiner ->
                    sender.sendMessage(
                        "%s => %s: %d, %d, %d".format(
                            Locale.ROOT, socketGemCombiner.uuid.toString(),
                            socketGemCombiner.location.world.name,
                            socketGemCombiner.location.x,
                            socketGemCombiner.location.y,
                            socketGemCombiner.location.z
                        )
                    )
                }
        }

        @Description("Lists the socket gem combiners and their locations.")
        @CommandCompletion("@players")
        @Subcommand("open")
        @CommandPermission("mythicdrops.command.combiners.open")
        fun openCombinerCommand(sender: CommandSender, @Flags("other") player: Player) {
            mythicDrops.socketGemCombinerGuiFactory.createAndRegisterSocketGemCombinerGui().showToPlayer(player)
            sender.sendMythicMessage(
                mythicDrops.settingsManager.languageSettings.command.socketGemCombinerOpen,
                "%player%" to player.displayName
            )
        }

        @Description("Adds a socket gem combiner at the block the sender is looking at.")
        @Subcommand("add")
        @CommandPermission("mythicdrops.command.combiners.add")
        fun addCombinerCommand(sender: Player) {
            val blocks = sender.getLineOfSight(Sets.newHashSet(Material.AIR), 10)
            for (block in blocks) {
                if (block.type == Material.CHEST) {
                    val loc = Vec3.fromLocation(block.location)
                    mythicDrops.socketGemCombinerManager.addAtLocation(loc)
                    mythicDrops.saveSocketGemCombiners()
                    sender.sendMessage(
                        mythicDrops
                            .settingsManager
                            .languageSettings
                            .command
                            .socketGemCombinerAdd
                            .success.chatColorize()
                    )
                    return
                }
            }
            sender.sendMessage(
                mythicDrops
                    .settingsManager
                    .languageSettings
                    .command
                    .socketGemCombinerAdd
                    .failure.chatColorize()
            )
        }

        @Description("Removes a socket gem combiner from the block the sender is looking at.")
        @Subcommand("remove")
        @CommandPermission("mythicdrops.command.combiners.remove")
        fun removeCombinerCommand(sender: Player) {
            val blocks = sender.getLineOfSight(Sets.newHashSet(Material.AIR), 10)
            for (block in blocks) {
                if (block.type == Material.CHEST) {
                    val loc = Vec3.fromLocation(block.location)
                    if (mythicDrops.socketGemCombinerManager.containsAtLocation(loc)) {
                        mythicDrops.socketGemCombinerManager.removeAtLocation(loc)
                        mythicDrops.saveSocketGemCombiners()
                        sender.sendMessage(
                            mythicDrops
                                .settingsManager
                                .languageSettings
                                .command
                                .socketGemCombinerRemove
                                .success.chatColorize()
                        )
                        return
                    }
                }
            }
            sender.sendMessage(
                mythicDrops
                    .settingsManager
                    .languageSettings
                    .command
                    .socketGemCombinerRemove
                    .failure.chatColorize()
            )
        }
    }
}
