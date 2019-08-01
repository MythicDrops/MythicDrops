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
package com.tealcube.minecraft.bukkit.mythicdrops.socketting

import com.tealcube.minecraft.bukkit.mythicdrops.api.locations.Vec3
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.combiners.SocketGemCombinerManager
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory
import org.bukkit.block.Chest
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryOpenEvent

class SocketGemCombinerListener(private val socketGemCombinerManager: SocketGemCombinerManager) : Listener {
    private val logger = JulLoggerFactory.getLogger(SocketGemCombinerListener::class)

    @EventHandler
    fun onInventoryOpenEvent(event: InventoryOpenEvent) {
        val inventory = event.inventory
        val holder = inventory.holder as? Chest ?: return
        val loc = Vec3.fromLocation(holder.location)

        if (!socketGemCombinerManager.isSocketGemCombinerAtLocation(loc)) {
            logger.fine("!socketGemCombinerManager.isSocketGemCombinerAtLocation(loc)")
            return
        }
    }
}
