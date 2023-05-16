package com.tealcube.minecraft.bukkit.mythicdrops.tokens

import com.tealcube.minecraft.bukkit.mythicdrops.api.choices.WeightedChoice
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories.TokenItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.TokenManager
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getPersistentDataString
import io.pixeloutlaw.minecraft.spigot.mythicdrops.mythicDropsToken
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action.RIGHT_CLICK_AIR
import org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot.HAND
import org.bukkit.inventory.EquipmentSlot.OFF_HAND

internal class TokenListener(
    private val tokenManager: TokenManager,
    private val tokenItemFactory: TokenItemFactory
) : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerInteractEvent(event: PlayerInteractEvent) {
        if (event.action != RIGHT_CLICK_AIR && event.action != RIGHT_CLICK_BLOCK) {
            return
        }

        val tokenForItem =
            event.item?.getPersistentDataString(mythicDropsToken)?.let { tokenManager.getById(it) } ?: return

        val randomTokenItem = WeightedChoice.between(tokenForItem.items).choose() ?: return
        val randomTokenGeneratedItem = tokenItemFactory.buildTokenItem(randomTokenItem) ?: return

        when (event.hand) {
            HAND -> {
                val itemInHand = event.player.inventory.itemInMainHand.let {
                    if (it.amount > 1) {
                        it.amount = it.amount - 1
                        it
                    } else {
                        null
                    }
                }
                event.player.inventory.setItemInMainHand(itemInHand)
                event.player.inventory.addItem(randomTokenGeneratedItem)
                event.player.updateInventory()
            }

            OFF_HAND -> {
                val itemInHand = event.player.inventory.itemInOffHand.let {
                    if (it.amount > 1) {
                        it.amount = it.amount - 1
                        it
                    } else {
                        null
                    }
                }
                event.player.inventory.setItemInOffHand(itemInHand)
                event.player.inventory.addItem(randomTokenGeneratedItem)
                event.player.updateInventory()
            }

            else -> {
                // do nothing
            }
        }
    }
}
