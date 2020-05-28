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
import co.aikar.commands.annotation.Split
import co.aikar.commands.annotation.Subcommand
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGenerationReason
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.identification.IdentityTome
import com.tealcube.minecraft.bukkit.mythicdrops.identification.UnidentifiedItem
import com.tealcube.minecraft.bukkit.mythicdrops.items.builders.MythicDropBuilder
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory
import com.tealcube.minecraft.bukkit.mythicdrops.sendMythicMessage
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.SocketItem
import com.tealcube.minecraft.bukkit.mythicdrops.utils.GemUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemUtil
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.CommandSender

@CommandAlias("mythicdrops|md")
class DropCommands : BaseCommand() {
    companion object {
        private val logger = JulLoggerFactory.getLogger(DropCommands::class)
    }

    @Subcommand("drop")
    class NestedDropCommands(parent: BaseCommand) : BaseCommand() {
        @field:Dependency
        lateinit var mythicDrops: MythicDrops

        @Subcommand("custom")
        @CommandCompletion("@customItems @worlds * * * *")
        @Description("Spawns a tiered item in the player's inventory. Use \"*\" to drop any custom item.")
        @CommandPermission("mythicdrops.command.drop.custom")
        fun dropCustomItemCommand(
            sender: CommandSender,
            customItem: CustomItem?,
            world: World,
            @Default("0") x: Int,
            @Default("0") y: Int,
            @Default("0") z: Int,
            @Conditions("limits:min=0") @Default("1") amount: Int
        ) {
            var amountGiven = 0
            repeat(amount) {
                val itemStack =
                    customItem?.toItemStack(mythicDrops.customEnchantmentRegistry)
                        ?: mythicDrops.customItemManager.randomByWeight()
                            ?.toItemStack(mythicDrops.customEnchantmentRegistry)
                if (itemStack != null) {
                    world.dropItem(Location(world, x.toDouble(), y.toDouble(), z.toDouble()), itemStack)
                    amountGiven++
                }
            }
            sender.sendMythicMessage(
                mythicDrops.settingsManager.languageSettings.command.dropCustom.success,
                "%amount%" to amountGiven.toString()
            )
        }

        @Subcommand("gem")
        @CommandCompletion("@socketGems @worlds * * * *")
        @Description("Spawns a Socket Gem in the player's inventory. Use \"*\" to drop any Socket Gem.")
        @CommandPermission("mythicdrops.command.drop.gem")
        fun dropSocketGemCommand(
            sender: CommandSender,
            socketGem: SocketGem?,
            world: World,
            @Default("0") x: Int,
            @Default("0") y: Int,
            @Default("0") z: Int,
            @Conditions("limits:min=0") @Default("1") amount: Int
        ) {
            var amountGiven = 0
            repeat(amount) {
                val chosenSocketGem = socketGem ?: mythicDrops.socketGemManager.randomByWeight() ?: return@repeat
                GemUtil.getRandomSocketGemMaterial()?.let {
                    val itemStack = SocketItem(
                        it,
                        chosenSocketGem,
                        mythicDrops.settingsManager.socketingSettings.items.socketGem
                    )
                    world.dropItem(Location(world, x.toDouble(), y.toDouble(), z.toDouble()), itemStack)
                    amountGiven++
                }
            }
            sender.sendMythicMessage(
                mythicDrops.settingsManager.languageSettings.command.dropGem.success,
                "%amount%" to amountGiven.toString()
            )
        }

        @Subcommand("tier")
        @CommandCompletion("@tiers @worlds * * * *")
        @Description("Spawns a tiered item in the player's inventory. Use \"*\" to drop any tier.")
        @CommandPermission("mythicdrops.command.drop.tier")
        fun dropTierCommand(
            sender: CommandSender,
            tier: Tier?,
            world: World,
            @Default("0") x: Int,
            @Default("0") y: Int,
            @Default("0") z: Int,
            @Conditions("limits:min=0") @Default("1") amount: Int
        ) {
            var amountGiven = 0
            val dropBuilder = MythicDropBuilder(mythicDrops)
            repeat(amount) {
                val chosenTier = tier ?: mythicDrops.tierManager.randomByWeight() ?: return@repeat
                val itemStack = dropBuilder.withItemGenerationReason(ItemGenerationReason.COMMAND)
                    .withTier(chosenTier).build()
                if (itemStack != null) {
                    world.dropItem(Location(world, x.toDouble(), y.toDouble(), z.toDouble()), itemStack)
                    amountGiven++
                }
            }
            sender.sendMythicMessage(
                mythicDrops.settingsManager.languageSettings.command.dropRandom.success,
                "%amount%" to amountGiven.toString()
            )
        }

        @Subcommand("tome")
        @CommandCompletion("@worlds * * * *")
        @Description("Spawns an Identity Tome in the player's inventory.")
        @CommandPermission("mythicdrops.command.drop.tome")
        fun dropIdentityTomeCommand(
            sender: CommandSender,
            world: World,
            @Default("0") x: Int,
            @Default("0") y: Int,
            @Default("0") z: Int,
            @Conditions("limits:min=0") @Default("1") amount: Int
        ) {
            var amountGiven = 0
            repeat(amount) {
                val itemStack = IdentityTome(mythicDrops.settingsManager.identifyingSettings.items.identityTome)
                world.dropItem(Location(world, x.toDouble(), y.toDouble(), z.toDouble()), itemStack)
            }
            sender.sendMythicMessage(
                mythicDrops.settingsManager.languageSettings.command.dropTome.success,
                "%amount%" to amountGiven.toString()
            )
        }

        @Subcommand("unidentified")
        @Description("Spawns an Unidentified Item in the player's inventory.")
        @CommandCompletion("@worlds * * * * *")
        @CommandPermission("mythicdrops.command.drop.unidentified")
        fun dropUnidentifiedItem(
            sender: CommandSender,
            world: World,
            @Default("0") x: Int,
            @Default("0") y: Int,
            @Default("0") z: Int,
            @Conditions("limits:min=0") @Default("1") amount: Int,
            @Default("") @Split(",") allowableTiers: Array<String>
        ) {
            val allowableTierList = allowableTiers.mapNotNull { mythicDrops.tierManager.getByName(it) }
            var amountGiven = 0
            repeat(amount) {
                val randomAllowableTier = if (allowableTierList.isEmpty()) {
                    null
                } else {
                    allowableTierList.random()
                }
                val randomTierFromManager = mythicDrops.tierManager.randomByWeight()
                val tier = randomAllowableTier ?: randomTierFromManager
                // intentionally not folded for readability
                if (tier == null) {
                    return@repeat
                }
                val materials = ItemUtil.getMaterialsFromTier(tier)
                    ?: return@repeat
                if (materials.isEmpty()) {
                    return@repeat
                }
                val material = materials.random()
                val itemStack =
                    UnidentifiedItem(
                        material,
                        mythicDrops.settingsManager.identifyingSettings.items.unidentifiedItem,
                        mythicDrops.settingsManager.languageSettings.displayNames,
                        allowableTierList
                    )
                world.dropItem(Location(world, x.toDouble(), y.toDouble(), z.toDouble()), itemStack)
                amountGiven += 1
            }
            sender.sendMythicMessage(
                mythicDrops.settingsManager.languageSettings.command.dropUnidentified.success,
                "%amount%" to amountGiven.toString()
            )
        }
    }
}
