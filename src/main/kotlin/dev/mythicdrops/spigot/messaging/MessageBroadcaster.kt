package dev.mythicdrops.spigot.messaging

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.LanguageSettings
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
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

internal class MessageBroadcaster(
    private val audiences: BukkitAudiences,
    private val languageSettings: LanguageSettings
) {
    /**
     * To whom should the broadcast be sent?
     */
    enum class BroadcastTarget {
        SERVER,
        WORLD,
        PLAYER,
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
        broadcastTarget: BroadcastTarget = BroadcastTarget.WORLD
    ) {
        val displayName = player.displayName
        val locale = languageSettings.general.foundItemBroadcast.replaceArgs(
            listOf(
                "%receiver%" to "%player%",
                "%player%" to displayName
            )
        ).chatColorize()
        val messages = locale.split("%item%")
        var broadcastComponent = Component.empty()
        val itemStackName =
            itemStack.displayName ?: itemStack.type.name.split("_")
                .joinToString(" ").toTitleCase()
        val itemStackAsJson = itemStack.itemMeta?.asString ?: ""
        val nbtBinary = BinaryTagHolder.binaryTagHolder(itemStackAsJson)
        val showItem = ShowItem.showItem(
            Key.key(
                itemStack.type.key.namespace,
                itemStack.type.key.key,
            ),
            itemStack.amount,
            nbtBinary,
        )
        val itemStackNameComponent =
            LegacyComponentSerializer.legacySection().deserialize(itemStackName).hoverEvent(
                HoverEvent.showItem(showItem),
            )
        messages.indices.forEach { idx ->
            val key = messages[idx]
            broadcastComponent = broadcastComponent.append(LegacyComponentSerializer.legacyAmpersand().deserialize(key))
            if (idx < messages.size - 1) {
                broadcastComponent = broadcastComponent.append(itemStackNameComponent)
            }
        }

        when (broadcastTarget) {
            BroadcastTarget.SERVER -> {
                Bukkit.getServer().onlinePlayers.forEach {
                    audiences.player(it).sendMessage(broadcastComponent)
                }
            }

            BroadcastTarget.WORLD -> {
                player.world.players.forEach {
                    audiences.player(it).sendMessage(broadcastComponent)
                }
            }

            BroadcastTarget.PLAYER -> {
                audiences.player(player).sendMessage(broadcastComponent)
            }
        }
    }

    private fun broadcastTargetFromString(str: String): BroadcastTarget {
        return BroadcastTarget.entries.firstOrNull {
            it.name.equals(str, true)
        } ?: BroadcastTarget.WORLD
    }
}
