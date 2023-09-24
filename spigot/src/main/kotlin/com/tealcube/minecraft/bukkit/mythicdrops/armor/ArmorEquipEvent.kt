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
package com.tealcube.minecraft.bukkit.mythicdrops.armor

import com.tealcube.minecraft.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack

/**
 * Modified version of ArmorEquipEvent from ArmorEquipEvent.
 *
 * https://github.com/Arnuh/ArmorEquipEvent
 */
internal class ArmorEquipEvent(
    val player: Player,
    val equipMethod: EquipMethod,
    val armorType: ArmorType,
    val oldArmorPiece: ItemStack?,
    val newArmorPiece: ItemStack?
) : MythicDropsCancellableEvent() {
    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    enum class EquipMethod {
        /**
         * When shift clicking an item to the slot from the inventory
         */
        SHIFT_CLICK,

        /**
         * When picking up and putting an item into an armor slot
         */
        CLICK,

        /**
         * When interacting with AIR with armor in hand and no armor in target slot
         */
        HOTBAR_INTERACT,

        /**
         * When pressing the hotbar key while hovering over a target slot
         */
        HOTBAR_SWAP,

        /**
         * When dispenser shoots armor onto player
         */
        DISPENSER,

        /**
         * When an armor piece breaks
         */
        BROKE,

        /**
         * When player dies
         */
        DEATH
    }

    override fun getHandlers(): HandlerList = handlerList
}
