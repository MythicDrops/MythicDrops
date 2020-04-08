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
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicCustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory
import com.tealcube.minecraft.bukkit.mythicdrops.sendMythicMessage
import com.tealcube.minecraft.bukkit.mythicdrops.utils.AirUtil
import org.bukkit.ChatColor
import org.bukkit.entity.Player

@CommandAlias("mythicdrops|md")
class CustomCreateCommand : BaseCommand() {
    companion object {
        private val logger = JulLoggerFactory.getLogger(DebugCommand::class)
        private val whitespaceRegex = """\s+""".toRegex()
    }

    @field:Dependency
    lateinit var mythicDrops: MythicDrops

    @Description("Creates a new custom item based on the item in your main hand.")
    @Subcommand("customcreate")
    @CommandPermission("mythicdrops.command.customcreate")
    fun customItemsCommand(sender: Player, @Default("0") weight: Double) {
        val itemInMainHand = sender.equipment?.itemInMainHand
        if (itemInMainHand == null || AirUtil.isAir(itemInMainHand.type)) {
            sender.sendMythicMessage(mythicDrops.settingsManager.languageSettings.command.customCreate.requiresItem)
            return
        }
        val itemMeta = itemInMainHand.itemMeta
        if (itemMeta == null) {
            sender.sendMythicMessage(mythicDrops.settingsManager.languageSettings.command.customCreate.requiresItemMeta)
            return
        }
        if (!itemMeta.hasDisplayName()) {
            sender.sendMythicMessage(
                mythicDrops.settingsManager.languageSettings.command.customCreate.requiresDisplayName
            )
            return
        }
        val name = ChatColor.stripColor(itemMeta.displayName)!!.replace(whitespaceRegex, "")

        val customItem = MythicCustomItem.fromItemStack(itemInMainHand, name, 0.0, weight)
        mythicDrops.customItemManager.add(customItem)
        sender.sendMythicMessage(
            mythicDrops.settingsManager.languageSettings.command.customCreate.success,
            "%name%" to name
        )

        mythicDrops.customItemYAML.set("$name.display-name", customItem.displayName)
        mythicDrops.customItemYAML.set("$name.material", customItem.material.name)
        mythicDrops.customItemYAML.set("$name.lore", customItem.lore)
        mythicDrops.customItemYAML.set("$name.weight", customItem.weight)
        mythicDrops.customItemYAML.set("$name.durability", customItem.durability)
        mythicDrops.customItemYAML.set("$name.chance-to-drop-on-monster-death", customItem.chanceToDropOnDeath)
        mythicDrops.customItemYAML.set("$name.broadcast-on-find", customItem.isBroadcastOnFind)
        mythicDrops.customItemYAML.set("$name.custom-model-data", customItem.customModelData)
        customItem.enchantments.forEach {
            val enchKey = try {
                it.enchantment.key
            } catch (throwable: Throwable) {
                it.enchantment.name
            }
            mythicDrops.customItemYAML.set("$name.enchantments.$enchKey.minimum-level", it.minimumLevel)
            mythicDrops.customItemYAML.set("$name.enchantments.$enchKey.maximum-level", it.maximumLevel)
        }
        mythicDrops.customItemYAML.save()
    }
}
