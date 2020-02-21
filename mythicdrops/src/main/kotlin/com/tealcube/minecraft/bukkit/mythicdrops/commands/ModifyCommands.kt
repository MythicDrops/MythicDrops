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
import co.aikar.commands.annotation.Conditions
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Dependency
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Subcommand
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.setDisplayNameChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.utils.StringListUtil
import io.pixeloutlaw.minecraft.spigot.hilt.getLore
import io.pixeloutlaw.minecraft.spigot.hilt.setLore
import kotlin.math.max
import kotlin.math.min
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

@CommandAlias("mythicdrops|md")
class ModifyCommands : BaseCommand() {
    @Subcommand("modify")
    class NestedModifyCommands(parent: BaseCommand) : BaseCommand() {
        @field:Dependency
        lateinit var settingsManager: SettingsManager

        @Description("Sets the name of the item in the main hand of the player.")
        @Subcommand("name")
        @CommandPermission("mythicdrops.command.modify.name")
        fun modifyNameCommand(
            sender: Player,
            args: Array<String>
        ) {
            val entityEquipment = sender.equipment
            if (entityEquipment == null) {
                sender.sendMessage(
                    settingsManager.languageSettings.command.modify.failure.chatColorize()
                )
                return
            }
            val itemInHand = entityEquipment.itemInMainHand
            if (itemInHand.type == Material.AIR) {
                sender.sendMessage(
                    settingsManager.languageSettings.command.modify.failure.chatColorize()
                )
                return
            }
            itemInHand.setDisplayNameChatColorized(args.joinToString(" "))
            sender.sendMessage(
                settingsManager.languageSettings.command.modify.name.chatColorize()
            )
        }

        @Subcommand("lore")
        class NestedModifyLoreCommands(parent: BaseCommand) : BaseCommand() {
            @field:Dependency
            lateinit var settingsManager: SettingsManager

            @Description("Adds a line of lore to the item in the main hand of the player.")
            @Subcommand("add")
            @CommandPermission("mythicdrops.command.modify.lore.add")
            fun addLoreCommand(
                sender: Player,
                args: Array<String>
            ) {
                val entityEquipment = sender.equipment
                if (entityEquipment == null) {
                    sender.sendMessage(
                        settingsManager.languageSettings.command.modify.failure.chatColorize()
                    )
                    return
                }
                val itemInHand = entityEquipment.itemInMainHand
                if (itemInHand.type == Material.AIR) {
                    sender.sendMessage(
                        settingsManager.languageSettings.command.modify.failure.chatColorize()
                    )
                    return
                }
                itemInHand.setLore(itemInHand.getLore().plus(args.joinToString(" ").chatColorize()))
                sender.sendMessage(
                    settingsManager.languageSettings.command.modify.lore.add.chatColorize()
                )
            }

            @Description("Removes a line of lore at index (starting at 1) from the item in the main hand of the player.")
            @Subcommand("remove")
            @CommandPermission("mythicdrops.command.modify.lore.remove")
            fun removeLoreCommand(
                sender: Player,
                @Conditions("limits:min=1") @Default("1") index: Int
            ) {
                val entityEquipment = sender.equipment
                if (entityEquipment == null) {
                    sender.sendMessage(
                        settingsManager.languageSettings.command.modify.failure.chatColorize()
                    )
                    return
                }
                val itemInHand = entityEquipment.itemInMainHand
                if (itemInHand.type == Material.AIR) {
                    sender.sendMessage(
                        settingsManager.languageSettings.command.modify.failure.chatColorize()
                    )
                    return
                }
                val lore = itemInHand.getLore().toMutableList()
                lore.removeAt(max(min(index - 1, lore.size), 0))
                itemInHand.setLore(lore.toList())
                sender.sendMessage(
                    settingsManager.languageSettings.command.modify.lore.remove.chatColorize()
                )
            }

            @Description("Inserts a line of lore at index (starting at 1) to the item in the main hand of the player.")
            @Subcommand("insert")
            @CommandPermission("mythicdrops.command.modify.lore.insert")
            fun insertLoreCommand(sender: Player, @Conditions("limits:min=1") index: Int, args: Array<String>) {
                val entityEquipment = sender.equipment
                if (entityEquipment == null) {
                    sender.sendMessage(
                        settingsManager.languageSettings.command.modify.failure.chatColorize()
                    )
                    return
                }
                val itemInHand = entityEquipment.itemInMainHand
                if (itemInHand.type == Material.AIR) {
                    sender.sendMessage(
                        settingsManager.languageSettings.command.modify.failure.chatColorize()
                    )
                    return
                }
                itemInHand.setLore(
                    StringListUtil.addString(
                        itemInHand.getLore().toMutableList(),
                        index,
                        args.joinToString(" ").chatColorize(),
                        false
                    )
                )
                sender.sendMessage(
                    settingsManager.languageSettings.command.modify.lore.insert.chatColorize()
                )
            }

            @Description("Sets a line of lore at index (starting at 1) to the item in the main hand of the player.")
            @Subcommand("set")
            @CommandPermission("mythicdrops.command.modify.lore.set")
            fun setLoreCommand(sender: Player, @Conditions("limits:min=1") index: Int, args: Array<String>) {
                val entityEquipment = sender.equipment
                if (entityEquipment == null) {
                    sender.sendMessage(
                        settingsManager.languageSettings.command.modify.failure.chatColorize()
                    )
                    return
                }
                val itemInHand = entityEquipment.itemInMainHand
                if (itemInHand.type == Material.AIR) {
                    sender.sendMessage(
                        settingsManager.languageSettings.command.modify.failure.chatColorize()
                    )
                    return
                }
                val toAdd = args.joinToString(" ").chatColorize()
                var lore = itemInHand.getLore().toMutableList()
                if (lore.size >= index - 1) {
                    lore[index - 1] = toAdd
                } else {
                    lore = StringListUtil.addString(
                        lore,
                        index,
                        toAdd,
                        false
                    )
                }
                itemInHand.setLore(lore)
                sender.sendMessage(
                    settingsManager.languageSettings.command.modify.lore.set.chatColorize()
                )
            }
        }

        @Subcommand("enchantment")
        class NestedEnchantmentLoreCommands(parent: BaseCommand) : BaseCommand() {
            @field:Dependency
            lateinit var settingsManager: SettingsManager

            @Description("Adds an enchantment to the item in the main hand of the player.")
            @Subcommand("add")
            @CommandPermission("mythicdrops.command.modify.enchantment.add")
            @CommandCompletion("@enchantments *")
            fun addEnchantmentCommand(
                sender: Player,
                enchantment: Enchantment,
                @Conditions("limits:min=1") @Default("1") level: Int
            ) {
                val entityEquipment = sender.equipment
                if (entityEquipment == null) {
                    sender.sendMessage(
                        settingsManager.languageSettings.command.modify.failure.chatColorize()
                    )
                    return
                }
                val itemInHand = entityEquipment.itemInMainHand
                if (itemInHand.type == Material.AIR) {
                    sender.sendMessage(
                        settingsManager.languageSettings.command.modify.failure.chatColorize()
                    )
                    return
                }
                itemInHand.addUnsafeEnchantment(enchantment, level)
                sender.sendMessage(
                    settingsManager.languageSettings.command.modify.enchantment.add.chatColorize()
                )
            }

            @Description("Removes an enchantment from the item in the main hand of the player.")
            @Subcommand("remove")
            @CommandPermission("mythicdrops.command.modify.enchantment.remove")
            @CommandCompletion("@enchantments")
            fun removeEnchantmentCommand(
                sender: Player,
                enchantment: Enchantment
            ) {
                val entityEquipment = sender.equipment
                if (entityEquipment == null) {
                    sender.sendMessage(
                        settingsManager.languageSettings.command.modify.failure.chatColorize()
                    )
                    return
                }
                val itemInHand = entityEquipment.itemInMainHand
                if (itemInHand.type == Material.AIR) {
                    sender.sendMessage(
                        settingsManager.languageSettings.command.modify.failure.chatColorize()
                    )
                    return
                }
                itemInHand.removeEnchantment(enchantment)
                sender.sendMessage(
                    settingsManager.languageSettings.command.modify.enchantment.remove.chatColorize()
                )
            }
        }
    }
}
