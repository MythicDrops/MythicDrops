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

import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketGemCacheManager
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.CraftingInventory

class SocketGemCacheListener(private val socketGemCacheManager: SocketGemCacheManager) : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onInventoryCloseEvent(event: InventoryCloseEvent) {
        val inventory = event.inventory as? CraftingInventory ?: return
        val player = inventory.holder as? Player ?: return
        if (player.isDead || player.health <= 0.0) {
            return
        }
        val socketGemCache = socketGemCacheManager.getOrCreateSocketGemCache(player.uniqueId)
        socketGemCacheManager.addSocketGemCache(
            socketGemCache.updateArmor().updateOffHand()
        )
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.isCancelled) {
            return
        }
        val eventDamager = event.damager
        val damager = when (eventDamager) {
            is Projectile -> {
                eventDamager.shooter as? Player ?: return
            }
            is Player -> {
                eventDamager
            }
            else -> {
                return
            }
        }
        val socketGemCache = socketGemCacheManager.getOrCreateSocketGemCache(damager.uniqueId)
        socketGemCacheManager.addSocketGemCache(
            socketGemCache.updateMainHand()
        )
    }
}
