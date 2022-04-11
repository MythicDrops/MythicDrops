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
import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.debug.MythicDebugManager
import com.tealcube.minecraft.bukkit.mythicdrops.sendMythicMessage
import com.tealcube.minecraft.bukkit.mythicdrops.toggleDebug
import io.pixeloutlaw.kindling.Log
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getAttributeModifiers
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getPersistentDataKeys
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getPersistentDataString
import io.pixeloutlaw.mythicdrops.mythicdrops.BuildConfig
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot

@CommandAlias("mythicdrops|md")
internal class DebugCommand : BaseCommand() {
    private val equipmentSlots by lazy {
        EquipmentSlot.values()
    }

    @field:Dependency
    lateinit var customItemManager: CustomItemManager

    @field:Dependency
    lateinit var loadingErrorManager: LoadingErrorManager

    @field:Dependency
    lateinit var mythicDebugManager: MythicDebugManager

    @field:Dependency
    lateinit var settingsManager: SettingsManager

    @field:Dependency
    lateinit var tierManager: TierManager

    @Description("Prints information to log. Useful for getting help in the Discord.")
    @Subcommand("debug")
    @CommandPermission("mythicdrops.command.debug")
    fun debug(sender: CommandSender) {
        Log.info("server package: ${Bukkit.getServer().javaClass.getPackage()}")
        Log.info("number of tiers: ${tierManager.get().size}")
        Log.info("number of custom items: ${customItemManager.get().size}")
        Log.info("settings: $settingsManager")
        sender.sendMythicMessage(settingsManager.languageSettings.command.debug)
    }

    @Description("Prints any loading errors. Useful for getting help in the Discord.")
    @Subcommand("errors")
    @CommandPermission("mythicdrops.command.errors")
    fun errors(sender: CommandSender) {
        sender.sendMythicMessage("&6=== MythicDrops Loading Errors ===")
        loadingErrorManager.get().forEach { sender.sendMythicMessage(it) }
    }

    @Description("Toggles debug mode for the running player. This is intended for development use only.")
    @Subcommand("toggledebug")
    @CommandPermission("mythicdrops.command.toggledebug")
    fun toggleDebug(sender: Player) {
        sender.toggleDebug(mythicDebugManager)
    }

    @Description("Prints information about the item in your hand.")
    @Subcommand("hand")
    @CommandPermission("mythicdrops.command.hand")
    fun hand(sender: Player) {
        sender.sendMythicMessage("&6>>> MythicDrops Hand Report >>>")
        val itemInMainHand = sender.equipment?.itemInMainHand
        if (itemInMainHand == null) {
            sender.sendMythicMessage("You have nothing in your main hand")
            sender.sendMythicMessage("&6<<< MythicDrops Hand Report <<<")
            return
        }
        sender.sendMythicMessage("&aPersistent Data")
        itemInMainHand.getPersistentDataKeys(BuildConfig.NAME).forEach {
            sender.sendMessage("$it - ${itemInMainHand.getPersistentDataString(it)}")
        }
        sender.sendMythicMessage("&aAttributes")
        equipmentSlots.forEach { slot ->
            sender.sendMythicMessage("&b> ${slot.name}")
            val attributes = itemInMainHand.getAttributeModifiers(slot).asMap()
            attributes.entries.forEach { mapping ->
                val attribute = mapping.key
                val attributeModifiers = mapping.value
                attributeModifiers.forEach {
                    sender.sendMythicMessage("${attribute.key.key} - ${it.operation} - ${it.amount}")
                }
            }
        }
        loadingErrorManager.get().forEach { sender.sendMythicMessage(it) }
        sender.sendMythicMessage("&6<<< MythicDrops Hand Report <<<")
    }
}
