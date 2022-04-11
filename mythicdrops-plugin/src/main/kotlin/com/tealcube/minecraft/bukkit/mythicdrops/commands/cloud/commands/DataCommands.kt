package com.tealcube.minecraft.bukkit.mythicdrops.commands.cloud.commands

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroup
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.sendMythicMessage
import org.bukkit.command.CommandSender
import org.koin.core.annotation.Single

@CommandMethod("mythicdrops|md")
@Single
internal class DataCommands(
    private val customItemManager: CustomItemManager,
    private val itemGroupManager: ItemGroupManager,
    private val settingsManager: SettingsManager,
    private val socketGemManager: SocketGemManager,
    private val tierManager: TierManager
) : CloudCommand {
    @CommandDescription(
        "Prints the item groups that the plugin is aware of. " +
            "If given an item group name, prints the contents of the group."
    )
    @CommandMethod("customitems")
    @CommandPermission("mythicdrops.command.customitems")
    fun customItemsCommand(
        sender: CommandSender
    ) {
        sender.sendMythicMessage(
            settingsManager.languageSettings.command.customItemList,
            "%customitems%" to customItemManager.get().joinToString(", ") { it.name }
        )
    }

    @CommandDescription(
        "Prints the item groups that the plugin is aware of." +
            "If given an item group name, prints the contents of the group."
    )
    @CommandMethod("itemgroups [itemGroup]")
    @CommandPermission("mythicdrops.command.itemgroups")
    fun itemGroupsCommand(
        sender: CommandSender,
        @Argument("itemGroup", description = "Item group to print") itemGroup: ItemGroup?
    ) {
        if (itemGroup == null) {
            sender.sendMythicMessage(
                settingsManager.languageSettings.command.itemGroups.list,
                "%itemgroups%" to itemGroupManager.get().joinToString(", ") { it.name }
            )
            return
        }
        sender.sendMythicMessage(
            settingsManager.languageSettings.command.itemGroups.materialsList,
            "%itemgroup%" to itemGroup.name
        )
        sender.sendMythicMessage(
            settingsManager.languageSettings.command.itemGroups.priority,
            "%itemgroup%" to itemGroup.name,
            "%priority%" to itemGroup.priority.toString()
        )
    }

    @CommandDescription("Prints the socket gems that the plugin is aware of.")
    @CommandMethod("socketgems")
    @CommandPermission("mythicdrops.command.socketgems")
    fun socketGemsCommand(sender: CommandSender) {
        sender.sendMythicMessage(
            settingsManager.languageSettings.command.socketGemList,
            "%socketgems%" to socketGemManager.get().joinToString(", ") { it.name }
        )
    }

    @CommandDescription("Prints the tiers that the plugin is aware of.")
    @CommandMethod("tiers")
    @CommandPermission("mythicdrops.command.tiers")
    fun tiersCommand(sender: CommandSender) {
        sender.sendMythicMessage(
            settingsManager.languageSettings.command.tierList,
            "%tiers%" to tierManager.get().joinToString(", ") { it.name }
        )
    }
}
