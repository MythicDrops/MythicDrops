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
import co.aikar.commands.InvalidCommandArgument
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
import org.bukkit.entity.Player

@CommandAlias("mythicdrops|md")
class SpawnCommands : BaseCommand() {
    companion object {
        private val logger = JulLoggerFactory.getLogger(SpawnCommands::class)
    }

    @Subcommand("spawn")
    class NestedSpawnCommands(parent: BaseCommand) : BaseCommand() {
        @field:Dependency
        lateinit var mythicDrops: MythicDrops

        @Subcommand("custom")
        @CommandCompletion("@customItems *")
        @Description("Spawns a tiered item in the player's inventory. Use \"*\" to spawn any custom item.")
        @CommandPermission("mythicdrops.command.spawn.custom")
        fun spawnCustomItemCommand(
            sender: Player,
            @Default("*") customItem: CustomItem?,
            @Conditions("limits:min=0") @Default("1") amount: Int
        ) {
            var amountGiven = 0
            repeat(amount) {
                val itemStack =
                    customItem?.toItemStack(mythicDrops.customEnchantmentRegistry)
                        ?: mythicDrops.customItemManager.randomByWeight()
                            ?.toItemStack(mythicDrops.customEnchantmentRegistry)
                if (itemStack != null) {
                    sender.inventory.addItem(itemStack)
                    amountGiven++
                }
            }
            sender.sendMythicMessage(
                mythicDrops.settingsManager.languageSettings.command.spawnCustom.success,
                "%amount%" to amountGiven.toString()
            )
        }

        @Subcommand("gem")
        @CommandCompletion("@socketGems *")
        @Description("Spawns a Socket Gem in the player's inventory. Use \"*\" to spawn any Socket Gem.")
        @CommandPermission("mythicdrops.command.spawn.gem")
        fun spawnSocketGemCommand(
            sender: Player,
            @Default("*") socketGem: SocketGem?,
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
                    sender.inventory.addItem(itemStack)
                    amountGiven++
                }
            }
            sender.sendMythicMessage(
                mythicDrops.settingsManager.languageSettings.command.spawnGem.success,
                "%amount%" to amountGiven.toString()
            )
        }

        @Subcommand("tier")
        @CommandCompletion("@tiers *")
        @Description("Spawns a tiered item in the player's inventory. Use \"*\" to spawn any tier.")
        @CommandPermission("mythicdrops.command.spawn.tier")
        fun spawnTierCommand(
            sender: Player,
            @Default("*") tier: Tier?,
            @Conditions("limits:min=0") @Default("1") amount: Int
        ) {
            var amountGiven = 0
            val dropBuilder = MythicDropBuilder(mythicDrops)
            repeat(amount) {
                val chosenTier = tier ?: mythicDrops.tierManager.randomByWeight() ?: return@repeat
                val itemStack = dropBuilder.withItemGenerationReason(ItemGenerationReason.COMMAND)
                    .withTier(chosenTier).useDurability(true).build()
                if (itemStack != null) {
                    sender.inventory.addItem(itemStack)
                    amountGiven++
                }
            }
            sender.sendMythicMessage(
                mythicDrops.settingsManager.languageSettings.command.spawnRandom.success,
                "%amount%" to amountGiven.toString()
            )
        }

        @Subcommand("tome")
        @Description("Spawns an Identity Tome in the player's inventory.")
        @CommandPermission("mythicdrops.command.spawn.tome")
        fun spawnIdentityTomeCommand(sender: Player, @Conditions("limits:min=0") @Default("1") amount: Int) {
            var amountGiven = 0
            repeat(amount) {
                val itemStack = IdentityTome(mythicDrops.settingsManager.identifyingSettings.items.identityTome)
                sender.inventory.addItem(itemStack)
                amountGiven++
            }
            sender.sendMythicMessage(
                mythicDrops.settingsManager.languageSettings.command.spawnTome.success,
                "%amount%" to amountGiven.toString()
            )
        }

        @Subcommand("unidentified")
        @Description("Spawns an Unidentified Item in the player's inventory.")
        @CommandPermission("mythicdrops.command.spawn.unidentified")
        fun spawnUnidentifiedItem(
            sender: Player,
            @Conditions("limits:min=0") @Default("1") amount: Int,
            @Default("") @Split(",") allowableTiers: Array<String>
        ) {
            val allowableTierList = allowableTiers.mapNotNull { mythicDrops.tierManager.getByName(it) }
            val randomAllowableTier = if (allowableTierList.isEmpty()) {
                null
            } else {
                allowableTierList.random()
            }
            val randomTierFromManager = mythicDrops.tierManager.randomByWeight()
            val tier = randomAllowableTier ?: randomTierFromManager
            // intentionally not folded for readability
            if (tier == null) {
                throw InvalidCommandArgument("Unable to find a tier for the Unidentified Item!")
            }
            val materials = ItemUtil.getMaterialsFromTier(tier)
                ?: throw InvalidCommandArgument("Unable to find materials for the Unidentified Item!")
            if (materials.isEmpty()) {
                throw InvalidCommandArgument("Unable to find materials for the Unidentified Item!")
            }
            var amountGiven = 0
            repeat(amount) {
                val material = materials.random()
                val itemStack =
                    UnidentifiedItem(
                        material,
                        mythicDrops.settingsManager.identifyingSettings.items.unidentifiedItem,
                        mythicDrops.settingsManager.languageSettings.displayNames,
                        allowableTierList
                    )
                sender.inventory.addItem(itemStack)
                amountGiven++
            }
            sender.sendMythicMessage(
                mythicDrops.settingsManager.languageSettings.command.spawnUnidentified.success,
                "%amount%" to amountGiven.toString()
            )
        }
    }
}
