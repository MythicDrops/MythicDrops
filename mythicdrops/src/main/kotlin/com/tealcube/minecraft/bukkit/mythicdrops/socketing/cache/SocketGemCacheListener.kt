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
package com.tealcube.minecraft.bukkit.mythicdrops.socketing.cache

import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.cache.SocketGemCacheManager
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.CraftingInventory

class SocketGemCacheListener(private val socketGemCacheManager: SocketGemCacheManager) : Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    fun onInventoryCloseEvent(event: InventoryCloseEvent) {
        val inventory = event.inventory as? CraftingInventory ?: return
        val player = inventory.holder as? Player ?: return
        if (player.isDead || player.health <= 0.0) {
            return
        }
        val socketGemCache = socketGemCacheManager.getOrCreateSocketGemCache(player.uniqueId)
        socketGemCacheManager.add(
            socketGemCache.updateArmor().updateOffHand().updateMainHand()
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
        socketGemCacheManager.add(
            socketGemCache.updateMainHand()
        )
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerItemHeldEvent(playerItemHeldEvent: PlayerItemHeldEvent) {
        if (playerItemHeldEvent.isCancelled) {
            return
        }
        val socketGemCache = socketGemCacheManager.getOrCreateSocketGemCache(playerItemHeldEvent.player.uniqueId)
        socketGemCacheManager.add(
            socketGemCache.updateMainHand(playerItemHeldEvent.player.inventory.getItem(playerItemHeldEvent.newSlot)).updateOffHand()
        )
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerSwapHandItemsEvent(playerSwapHandItemsEvent: PlayerSwapHandItemsEvent) {
        if (playerSwapHandItemsEvent.isCancelled) {
            return
        }
        val socketGemCache = socketGemCacheManager.getOrCreateSocketGemCache(playerSwapHandItemsEvent.player.uniqueId)
        socketGemCacheManager.add(
            socketGemCache.updateMainHand(playerSwapHandItemsEvent.mainHandItem).updateOffHand(playerSwapHandItemsEvent.offHandItem)
        )
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerDropItemEvent(playerDropItemEvent: PlayerDropItemEvent) {
        if (playerDropItemEvent.isCancelled) {
            return
        }
        val socketGemCache = socketGemCacheManager.getOrCreateSocketGemCache(playerDropItemEvent.player.uniqueId)
        socketGemCacheManager.add(socketGemCache.updateMainHand().updateOffHand().updateArmor())
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerQuitEvent(playerQuitEvent: PlayerQuitEvent) {
        socketGemCacheManager.remove(playerQuitEvent.player.uniqueId)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerKickEvent(playerKickEvent: PlayerKickEvent) {
        socketGemCacheManager.remove(playerKickEvent.player.uniqueId)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerLoginEvent(playerLoginEvent: PlayerLoginEvent) {
        val socketGemCache = socketGemCacheManager.getOrCreateSocketGemCache(playerLoginEvent.player.uniqueId)
        socketGemCacheManager.add(socketGemCache.updateMainHand().updateOffHand().updateArmor())
    }
}
