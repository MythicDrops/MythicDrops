package com.tealcube.minecraft.bukkit.mythicdrops.tokens

import com.tealcube.minecraft.bukkit.mythicdrops.api.choices.WeightedChoice
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories.TokenItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.TokenManager
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getPersistentDataString
import io.pixeloutlaw.minecraft.spigot.mythicdrops.mythicDropsToken
import io.pixeloutlaw.minecraft.spigot.prerequisites
import org.bukkit.event.Event.Result.DENY
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot.HAND
import org.bukkit.inventory.EquipmentSlot.OFF_HAND

internal class TokenListener(
    private val tokenManager: TokenManager,
    private val tokenItemFactory: TokenItemFactory
) : Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPlayerInteractEvent(event: PlayerInteractEvent) {
        // check our prerequisites for repairing
        val prereqs = prerequisites {
            prerequisite { event.useItemInHand() != DENY }
            prerequisite { event.useInteractedBlock() != DENY }
        }
        if (!prereqs) {
            return
        }

        val tokenForItem =
            event.item?.getPersistentDataString(mythicDropsToken)?.let { tokenManager.getById(it) } ?: return

        val randomTokenItem = WeightedChoice.between(tokenForItem.items).choose() ?: return
        val randomTokenGeneratedItem = tokenItemFactory.buildTokenItem(randomTokenItem) ?: return

        when (event.hand) {
            HAND -> {
                event.player.inventory.setItemInMainHand(randomTokenGeneratedItem)
                event.player.updateInventory()
            }

            OFF_HAND -> {
                event.player.inventory.setItemInOffHand(randomTokenGeneratedItem)
                event.player.updateInventory()
            }

            else -> {
                // do nothing
            }
        }
    }
}
