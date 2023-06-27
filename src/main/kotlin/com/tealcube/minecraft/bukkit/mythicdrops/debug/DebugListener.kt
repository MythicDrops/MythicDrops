/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2020 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.debug

import com.tealcube.minecraft.bukkit.mythicdrops.isDebug
import com.tealcube.minecraft.bukkit.mythicdrops.sendDebugMessage
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getPersistentDataKeys
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getPersistentDataString
import io.pixeloutlaw.minecraft.spigot.mythicdrops.mythicDropsTier
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDamageEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent

internal class DebugListener(private val mythicDebugManager: MythicDebugManager) : Listener {
    @EventHandler
    fun onEntityDamageByEntityEvent(event: EntityDamageByEntityEvent) {
        val damager = event.damager
        val target = event.entity
        if (damager !is Player || !damager.isDebug(mythicDebugManager) || target !is LivingEntity) {
            return
        }
        val entityEquipment = target.equipment
        if (entityEquipment == null) {
            damager.sendDebugMessage(mythicDebugManager, "entity has no equipment")
            return
        }
        val entityEquipmentChances = EntityEquipmentChances(
            entityEquipment.helmetDropChance,
            entityEquipment.chestplateDropChance,
            entityEquipment.leggingsDropChance,
            entityEquipment.bootsDropChance,
            entityEquipment.itemInMainHandDropChance,
            entityEquipment.itemInOffHandDropChance
        )
        damager.sendDebugMessage(mythicDebugManager, entityEquipmentChances.toString())
    }

    @EventHandler
    fun onBlockDamageEvent(event: BlockDamageEvent) {
        if (!event.player.isDebug(mythicDebugManager)) {
            return
        }
        event.player.equipment?.itemInMainHand?.let { mainHand ->
            mainHand.getPersistentDataKeys(mythicDropsTier.namespace).forEach {
                event.player.sendMessage("$it - ${mainHand.getPersistentDataString(it)}")
            }
        }
    }
}
