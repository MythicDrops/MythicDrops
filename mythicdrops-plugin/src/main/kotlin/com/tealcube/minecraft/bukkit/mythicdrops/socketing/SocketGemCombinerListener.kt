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
package com.tealcube.minecraft.bukkit.mythicdrops.socketing

import com.tealcube.minecraft.bukkit.mythicdrops.api.locations.Vec3
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerGuiFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerManager
import org.bukkit.block.Chest
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryOpenEvent
import org.koin.core.annotation.Single

@Single
internal class SocketGemCombinerListener(
    private val socketGemCombinerManager: SocketGemCombinerManager,
    private val socketGemCombinerGuiFactory: SocketGemCombinerGuiFactory
) : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onChestOpen(event: InventoryOpenEvent) {
        if (event.isCancelled) {
            return
        }

        val player = event.player as? Player ?: return
        val inventory = event.inventory
        val holder = inventory.holder as? Chest ?: return
        val loc = Vec3.fromLocation(holder.location)

        if (!socketGemCombinerManager.containsAtLocation(loc)) {
            return
        }
        event.isCancelled = true

        socketGemCombinerGuiFactory.createAndRegisterSocketGemCombinerGui().showToPlayer(player)
    }
}
