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
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi
import com.tealcube.minecraft.bukkit.mythicdrops.hdb.HeadDatabaseAdapter
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicCustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.sendMythicMessage
import org.bukkit.ChatColor
import org.bukkit.entity.Player

@CommandAlias("mythicdrops|md")
internal class CustomCreateCommand : BaseCommand() {
    companion object {
        private val whitespaceRegex = """\s+""".toRegex()
    }

    @field:Dependency
    lateinit var headDatabaseAdapter: HeadDatabaseAdapter

    @field:Dependency
    lateinit var mythicDrops: MythicDrops

    @Description("Creates a new custom item based on the item in your main hand.")
    @Subcommand("customcreate")
    @CommandPermission("mythicdrops.command.customcreate")
    fun customItemsCommand(
        sender: Player,
        @Default("0") weight: Double
    ) {
        val customCreateMessages = MythicDropsApi.mythicDrops.settingsManager.languageSettings.command.customCreate
        val itemInMainHand = sender.equipment?.itemInMainHand
        if (itemInMainHand == null || itemInMainHand.type.isAir) {
            sender.sendMythicMessage(
                customCreateMessages.requiresItem
            )
            return
        }
        val itemMeta = itemInMainHand.itemMeta
        if (itemMeta == null) {
            sender.sendMythicMessage(
                customCreateMessages.requiresItemMeta
            )
            return
        }
        if (!itemMeta.hasDisplayName()) {
            sender.sendMythicMessage(
                customCreateMessages.requiresDisplayName
            )
            return
        }
        val name = ChatColor.stripColor(itemMeta.displayName)!!.replace(whitespaceRegex, "")

        val customItem = MythicCustomItem.fromItemStack(itemInMainHand, name, 0.0, weight, headDatabaseAdapter)
        MythicDropsApi.mythicDrops.customItemManager.add(customItem)
        sender.sendMythicMessage(
            customCreateMessages.success,
            "%name%" to name
        )

        mythicDrops.configLoader.saveCustomItems()
    }
}
