package com.tealcube.minecraft.bukkit.mythicdrops.socketting

import com.tealcube.minecraft.bukkit.mythicdrops.api.locations.Vec3
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.combiners.SocketGemCombinerManager
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory
import org.bukkit.block.Chest
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryOpenEvent

class SocketGemCombinerListener(private val socketGemCombinerManager: SocketGemCombinerManager): Listener {
    private val logger = JulLoggerFactory.getLogger(SocketGemCombinerListener::class)

    @EventHandler
    fun onInventoryOpenEvent(event: InventoryOpenEvent) {
        val inventory = event.inventory
        val holder = inventory.holder as? Chest ?: return
        val loc = Vec3.fromLocation(holder.location)

        if (!socketGemCombinerManager.isSocketGemCombinerAtLocation(loc)) {
            logger.fine("!socketGemCombinerManager.isSocketGemCombinerAtLocation(loc)");
            return
        }
    }
}