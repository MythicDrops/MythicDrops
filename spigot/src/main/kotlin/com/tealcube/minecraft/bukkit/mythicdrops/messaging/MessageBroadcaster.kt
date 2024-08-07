package com.tealcube.minecraft.bukkit.mythicdrops.messaging

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.messaging.MessageBroadcaster.BroadcastTarget.PLAYER
import com.tealcube.minecraft.bukkit.mythicdrops.messaging.MessageBroadcaster.BroadcastTarget.SERVER
import com.tealcube.minecraft.bukkit.mythicdrops.messaging.MessageBroadcaster.BroadcastTarget.WORLD
import com.tealcube.minecraft.bukkit.mythicdrops.replaceArgs
import io.pixeloutlaw.minecraft.spigot.mythicdrops.displayName
import io.pixeloutlaw.minecraft.spigot.mythicdrops.toTitleCase
import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.api.BinaryTagHolder
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEvent.ShowItem
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.koin.core.annotation.Single

@Single
internal class MessageBroadcaster(
    private val audiences: BukkitAudiences,
    private val settingsManager: SettingsManager
) {
    /**
     * To whom should the broadcast be sent?
     */
    enum class BroadcastTarget {
        SERVER,
        WORLD,
        PLAYER
    }

    /**
     * Broadcasts that an item was found to all players in the given broadcast target. If the broadcast target isn't
     * found, defaults to the player's world.
     */
    fun broadcastItem(
        player: Player,
        itemStack: ItemStack,
        broadcastTarget: String
    ) {
        broadcastItem(player, itemStack, broadcastTargetFromString(broadcastTarget))
    }

    /**
     * Broadcasts that an item was found to all players in the given broadcast target.
     */
    fun broadcastItem(
        player: Player,
        itemStack: ItemStack,
        broadcastTarget: BroadcastTarget = WORLD
    ) {
        val displayName = player.displayName
        val locale =
            settingsManager.languageSettings.general.foundItemBroadcast
                .replaceArgs(
                    listOf(
                        "%receiver%" to "%player%",
                        "%player%" to displayName
                    )
                ).chatColorize()
        val messages = locale.split("%item%")
        var broadcastComponent = Component.empty()
        val itemStackName =
            itemStack.displayName ?: itemStack.type.name
                .split("_")
                .joinToString(" ")
                .toTitleCase()
        val itemStackAsJson = itemStack.itemMeta?.asString ?: ""
        val nbtBinary = BinaryTagHolder.binaryTagHolder(itemStackAsJson)
        val showItem =
            ShowItem.of(
                Key.key(
                    itemStack.type.key.namespace,
                    itemStack.type.key.key
                ),
                itemStack.amount,
                nbtBinary
            )
        val itemStackNameComponent =
            LegacyComponentSerializer.legacySection().deserialize(itemStackName).hoverEvent(
                HoverEvent.showItem(showItem)
            )
        messages.indices.forEach { idx ->
            val key = messages[idx]
            broadcastComponent = broadcastComponent.append(LegacyComponentSerializer.legacyAmpersand().deserialize(key))
            if (idx < messages.size - 1) {
                broadcastComponent = broadcastComponent.append(itemStackNameComponent)
            }
        }

        when (broadcastTarget) {
            SERVER -> {
                Bukkit.getServer().onlinePlayers.forEach {
                    audiences.player(it).sendMessage(broadcastComponent)
                }
            }

            WORLD -> {
                player.world.players.forEach {
                    audiences.player(it).sendMessage(broadcastComponent)
                }
            }

            PLAYER -> {
                audiences.player(player).sendMessage(broadcastComponent)
            }
        }
    }

    private fun broadcastTargetFromString(str: String): BroadcastTarget =
        BroadcastTarget.entries.firstOrNull {
            it.name.equals(str, true)
        } ?: WORLD
}
