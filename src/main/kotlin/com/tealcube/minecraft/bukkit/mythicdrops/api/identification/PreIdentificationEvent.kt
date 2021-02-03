package com.tealcube.minecraft.bukkit.mythicdrops.api.identification

import com.tealcube.minecraft.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack

/**
 * Fired when a Player identifies an item. Can modify the result.
 */
class PreIdentificationEvent(unidentifiedItem: ItemStack, tier: Tier, identifier: Player) : MythicDropsCancellableEvent() {
    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    var identifier: Player = identifier
    var isModified: Boolean = false
        private set
    var tier: Tier = tier
        set(value) {
            field = value
            isModified = true
        }
    var unidentifiedItem: ItemStack = unidentifiedItem
        private set

    override fun getHandlers(): HandlerList = handlerList
}