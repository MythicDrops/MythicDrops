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
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.setDisplayNameChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ChatColorUtil
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getTier
import io.pixeloutlaw.minecraft.spigot.mythicdrops.lore
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import kotlin.math.max
import kotlin.math.min

@CommandAlias("mythicdrops|md")
internal class ModifyCommands : BaseCommand() {
    @Subcommand("modify")
    class NestedModifyCommands(@Suppress("UNUSED_PARAMETER") parent: BaseCommand) : BaseCommand() {
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
        class NestedModifyLoreCommands(@Suppress("UNUSED_PARAMETER") parent: BaseCommand) : BaseCommand() {
            @field:Dependency
            lateinit var settingsManager: SettingsManager

            @field:Dependency
            lateinit var tierManager: TierManager

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
                itemInHand.lore = itemInHand.lore.plus(args.joinToString(" ").chatColorize())
                sender.sendMessage(
                    settingsManager.languageSettings.command.modify.lore.add.chatColorize()
                )
            }

            @Description("Adds an empty socket to the item in the main hand of the player.")
            @Subcommand("socket")
            @CommandPermission("mythicdrops.command.modify.lore.add")
            fun addSocketCommand(
                sender: Player
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
                val targetItemTier =
                    itemInHand.getTier(tierManager)
                val tierColor =
                    if (targetItemTier != null && settingsManager.socketingSettings.options.useTierColorForSocketName) {
                        ChatColorUtil.getFirstColors(targetItemTier.itemDisplayNameFormat.chatColorize())
                    } else {
                        settingsManager.socketingSettings.options.defaultSocketNameColorOnItems
                    }
                val emptySocketString = settingsManager.socketingSettings.items.socketedItem.socket.replace(
                    "%tiercolor%",
                    tierColor
                )

                addLoreCommand(sender, arrayOf(emptySocketString))
            }

            @Description("Adds an empty socket extender slot to the item in the main hand of the player.")
            @Subcommand("extender")
            @CommandPermission("mythicdrops.command.modify.lore.add")
            fun addExtenderCommand(
                sender: Player
            ) {
                addLoreCommand(sender, arrayOf(settingsManager.socketingSettings.items.socketExtender.slot))
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
                val lore = itemInHand.lore.toMutableList()
                lore.removeAt(max(min(index - 1, lore.size), 0))
                itemInHand.lore = lore.toList()
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
                itemInHand.lore =
                    itemInHand.lore.toMutableList().apply { add(index + 1, args.joinToString(" ").chatColorize()) }
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
                val lore = itemInHand.lore.toMutableList()
                if (lore.size >= index - 1) {
                    lore[index - 1] = toAdd
                } else {
                    lore.add(index + 1, toAdd)
                }
                itemInHand.lore = lore
                sender.sendMessage(
                    settingsManager.languageSettings.command.modify.lore.set.chatColorize()
                )
            }
        }

        @Subcommand("enchantment")
        class NestedEnchantmentLoreCommands(@Suppress("UNUSED_PARAMETER") parent: BaseCommand) : BaseCommand() {
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
