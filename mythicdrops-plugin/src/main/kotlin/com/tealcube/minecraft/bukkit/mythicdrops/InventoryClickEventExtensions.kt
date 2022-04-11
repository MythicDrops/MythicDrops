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
package com.tealcube.minecraft.bukkit.mythicdrops

import io.pixeloutlaw.minecraft.spigot.plumbing.lib.ItemAttributes
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

@Suppress("detekt.ReturnCount")
internal fun InventoryClickEvent.getTargetItemAndCursorAndPlayer(
    allowedClickType: ClickType = ClickType.RIGHT
): Triple<ItemStack, ItemStack, Player>? {
    val eventCurrentItem = currentItem
    val eventCursor = cursor
    val player = whoClicked as? Player
    if (eventCurrentItem == null || eventCursor == null || player == null) {
        return null
    }
    if (
        eventCurrentItem.type.isAir ||
        eventCursor.type.isAir ||
        click != allowedClickType
    ) {
        return null
    }

    val targetItem = ItemAttributes.conditionallyCloneWithDefaultAttributes(eventCurrentItem)
    val cursor = eventCursor.clone()

    return Triple(targetItem, cursor, player)
}

internal fun InventoryClickEvent.updateCurrentItemAndSubtractFromCursor(currentItem: ItemStack) {
    // set the current item
    this.currentItem = currentItem

    // subtract one from the cursor, and if the amount drops below one, get rid of the item on the cursor
    cursor?.let {
        it.amount = it.amount - 1
        if (it.amount <= 0) {
            @Suppress("DEPRECATION")
            cursor = null
        }
    }

    isCancelled = true
    result = Event.Result.DENY

    (whoClicked as? Player)?.updateInventory()
}
