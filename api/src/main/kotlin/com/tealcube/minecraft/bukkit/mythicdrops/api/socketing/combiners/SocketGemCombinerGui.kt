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
package com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners

import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.InventoryHolder

/**
 * Represents the GUI used for Socket Gem Combining.
 */
interface SocketGemCombinerGui : InventoryHolder, Listener {
    /**
     * Handles when something is clicked inside the combiner.
     *
     * Implementations probably want to annotate this method with [org.bukkit.event.EventHandler].
     *
     * @param event Event to listen to
     */
    fun onGuiClick(event: InventoryClickEvent)

    /**
     * Handles when the combiner is closed.
     *
     * Implementations probably want to annotate this method with [org.bukkit.event.EventHandler].
     *
     * @param event Event to listen to
     */
    fun onGuiClose(event: InventoryCloseEvent)

    /**
     * Shows the GUI to the given [player].
     *
     * @param player Player to show the GUI to
     */
    fun showToPlayer(player: Player)
}
