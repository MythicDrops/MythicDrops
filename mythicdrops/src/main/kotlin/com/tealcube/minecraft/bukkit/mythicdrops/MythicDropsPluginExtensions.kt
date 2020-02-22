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
package com.tealcube.minecraft.bukkit.mythicdrops

import co.aikar.commands.ConditionFailedException
import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.PaperCommandManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroup
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.commands.CombinerCommands
import com.tealcube.minecraft.bukkit.mythicdrops.commands.CustomCreateCommand
import com.tealcube.minecraft.bukkit.mythicdrops.commands.CustomItemsCommand
import com.tealcube.minecraft.bukkit.mythicdrops.commands.DebugCommand
import com.tealcube.minecraft.bukkit.mythicdrops.commands.DropCommands
import com.tealcube.minecraft.bukkit.mythicdrops.commands.GiveCommands
import com.tealcube.minecraft.bukkit.mythicdrops.commands.HelpCommand
import com.tealcube.minecraft.bukkit.mythicdrops.commands.ItemGroupsCommand
import com.tealcube.minecraft.bukkit.mythicdrops.commands.ModifyCommands
import com.tealcube.minecraft.bukkit.mythicdrops.commands.ReloadCommand
import com.tealcube.minecraft.bukkit.mythicdrops.commands.SocketGemsCommand
import com.tealcube.minecraft.bukkit.mythicdrops.commands.SpawnCommands
import com.tealcube.minecraft.bukkit.mythicdrops.commands.TiersCommand
import com.tealcube.minecraft.bukkit.mythicdrops.logging.MythicLoggingFormatter
import com.tealcube.minecraft.bukkit.mythicdrops.logging.rebelliousAddHandler
import com.tealcube.minecraft.bukkit.mythicdrops.utils.EnchantmentUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TierUtil
import java.util.logging.FileHandler
import java.util.logging.Handler
import java.util.logging.Level
import java.util.logging.Logger
import org.bstats.bukkit.Metrics
import org.bukkit.enchantments.Enchantment

private const val BSTATS_PLUGIN_ID = 5147

fun MythicDropsPlugin.setupLogHandler(): Handler? = try {
    val pathToLogOutput = String.format("%s/mythicdrops.log", dataFolder.absolutePath)
    val fileHandler = FileHandler(pathToLogOutput, true)
    fileHandler.formatter = MythicLoggingFormatter()

    Logger.getLogger("com.tealcube.minecraft.bukkit.mythicdrops").rebelliousAddHandler(fileHandler)
    Logger.getLogger("io.pixeloutlaw.minecraft.spigot").rebelliousAddHandler(fileHandler)
    Logger.getLogger("po.io.pixeloutlaw.minecraft.spigot").rebelliousAddHandler(fileHandler)

    logger.info("MythicDrops logging has been setup")
    fileHandler
} catch (ex: Exception) {
    logger.log(Level.SEVERE, "Unable to setup logging for MythicDrops", ex)
    null
}

fun MythicDropsPlugin.setupMetrics() {
    val metrics = Metrics(this, BSTATS_PLUGIN_ID)
    metrics.addCustomChart(Metrics.SingleLineChart("amount_of_tiers") { tierManager.get().size })
    metrics.addCustomChart(Metrics.SingleLineChart("amount_of_custom_items") { customItemManager.get().size })
    metrics.addCustomChart(Metrics.SingleLineChart("amount_of_socket_gems") { socketGemManager.get().size })
}

fun MythicDropsPlugin.setupCommands() {
    val commandManager = PaperCommandManager(this)
    @Suppress("DEPRECATION")
    commandManager.enableUnstableAPI("help")
    commandManager.registerDependency(CustomItemManager::class.java, customItemManager)
    commandManager.registerDependency(MythicDrops::class.java, this)
    commandManager.registerDependency(LoadingErrorManager::class.java, loadingErrorManager)
    commandManager.registerDependency(SettingsManager::class.java, settingsManager)
    commandManager.registerDependency(TierManager::class.java, tierManager)
    registerContexts(commandManager)
    registerConditions(commandManager)
    registerCompletions(commandManager)
    registerCommands(commandManager)
}

private fun MythicDropsPlugin.registerContexts(commandManager: PaperCommandManager) {
    commandManager.commandContexts.registerContext(CustomItem::class.java) { c ->
        val firstArg = c.popFirstArg() ?: throw InvalidCommandArgument()
        val customItem = customItemManager.getById(firstArg) ?: customItemManager.getById(firstArg.replace("_", " "))
        if (customItem == null && firstArg != "*") {
            throw InvalidCommandArgument("No custom item found by that name!")
        }
        customItem
    }
    commandManager
        .commandContexts
        .registerContext(
            Enchantment::class.java
        ) { c ->
            val firstArg = c.firstArg
            val enchantment = EnchantmentUtil.getByKeyOrName(firstArg) ?: throw InvalidCommandArgument()
            c.popFirstArg()
            enchantment
        }
    commandManager.commandContexts.registerContext(SocketGem::class.java) { c ->
        val firstArg = c.popFirstArg() ?: throw InvalidCommandArgument()
        val socketGem = socketGemManager.getById(firstArg) ?: socketGemManager.getById(firstArg.replace("_", " "))
        if (socketGem == null && firstArg != "*") {
            throw InvalidCommandArgument("No socket gem found by that name!")
        }
        socketGem
    }
    commandManager.commandContexts.registerContext(Tier::class.java) { c ->
        val firstArg = c.popFirstArg() ?: throw InvalidCommandArgument()
        val tier = TierUtil.getTier(firstArg) ?: TierUtil.getTier(firstArg.replace("_", " "))
        if (tier == null && firstArg != "*") {
            throw InvalidCommandArgument("No tier found by that name!")
        }
        tier
    }
    commandManager.commandContexts.registerContext(ItemGroup::class.java) { c ->
        val firstArg = c.popFirstArg() ?: throw InvalidCommandArgument()
        val itemGroup = itemGroupManager.getById(firstArg) ?: itemGroupManager.getById(firstArg.replace("_", " "))
        if (itemGroup == null && firstArg != "*") {
            throw InvalidCommandArgument("No tier found by that name!")
        }
        itemGroup
    }
}

private fun MythicDropsPlugin.registerConditions(commandManager: PaperCommandManager) {
    commandManager
        .commandConditions
        .addCondition(
            Int::class.java,
            "limits"
        ) { c, _, value ->
            if (value != null) {
                if (c.hasConfig("min") && c.getConfigValue("min", 0) > value) {
                    throw ConditionFailedException(
                        "Min value must be " + c.getConfigValue("min", 0)!!
                    )
                }
                if (c.hasConfig("max") && c.getConfigValue("max", 3) < value) {
                    throw ConditionFailedException(
                        "Max value must be " + c.getConfigValue("max", 3)!!
                    )
                }
            }
        }
}

private fun MythicDropsPlugin.registerCompletions(commandManager: PaperCommandManager) {
    commandManager
        .commandCompletions
        .registerCompletion(
            "enchantments"
        ) { _ ->
            Enchantment.values().map {
                try {
                    it.key.toString()
                } catch (ex: Throwable) {
                    @Suppress("DEPRECATION")
                    it.name
                }
            }
        }
    commandManager.commandCompletions.registerCompletion("customItems") { _ ->
        listOf("*") + customItemManager.get().map { it.name.replace(" ", "_") }
    }
    commandManager.commandCompletions.registerCompletion("socketGems") { _ ->
        listOf("*") + socketGemManager.get().map { it.name.replace(" ", "_") }
    }
    commandManager.commandCompletions.registerCompletion("tiers") { _ ->
        listOf("*") + tierManager.get().map { it.name.replace(" ", "_") }
    }
    commandManager.commandCompletions.registerCompletion("itemGroups") { _ ->
        listOf("*") + itemGroupManager.get().map { it.name.replace(" ", "_") }
    }
}

private fun MythicDropsPlugin.registerCommands(commandManager: PaperCommandManager) {
    commandManager.registerCommand(CombinerCommands())
    commandManager.registerCommand(CustomCreateCommand())
    commandManager.registerCommand(CustomItemsCommand())
    commandManager.registerCommand(DebugCommand())
    commandManager.registerCommand(DropCommands())
    commandManager.registerCommand(GiveCommands())
    commandManager.registerCommand(HelpCommand())
    commandManager.registerCommand(ItemGroupsCommand())
    commandManager.registerCommand(ModifyCommands())
    commandManager.registerCommand(ReloadCommand())
    commandManager.registerCommand(SocketGemsCommand())
    commandManager.registerCommand(SpawnCommands())
    commandManager.registerCommand(TiersCommand())
}
