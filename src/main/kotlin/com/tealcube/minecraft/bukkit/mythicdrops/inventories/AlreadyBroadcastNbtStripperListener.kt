package com.tealcube.minecraft.bukkit.mythicdrops.inventories

import io.pixeloutlaw.minecraft.spigot.mythicdrops.mythicDropsAlreadyBroadcast
import io.pixeloutlaw.minecraft.spigot.mythicdrops.removePersistentData
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent

class AlreadyBroadcastNbtStripperListener: Listener {
    @EventHandler
    fun onInventoryCloseEvent(event: InventoryCloseEvent) {
        val inventory = event.inventory
        // edit the inventory contents in place
        inventory.contents.filterNotNull().forEach {
            it.removePersistentData(mythicDropsAlreadyBroadcast)
        }
    }
}
