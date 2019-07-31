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
package com.tealcube.minecraft.bukkit.mythicdrops.crafting

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.ConfigSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SockettingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.utils.GemUtil
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent

class CraftingListener(private val configSettings: ConfigSettings, private val sockettingSettings: SockettingSettings) :
    Listener {
    @EventHandler
    fun onItemCraftEvent(event: CraftItemEvent) {
        if (event.isCancelled) {
            return
        }
        if (!sockettingSettings.isPreventCraftingWithGems) {
            return
        }

        val anySocketGems = event.inventory.matrix.any {
            GemUtil.getSocketGemFromPotentialSocketItem(sockettingSettings, it) != null
        }
        if (anySocketGems) {
            event.isCancelled = true
            (event.whoClicked as? Player)?.sendMessage(configSettings.getFormattedLanguageString("socket.prevented-crafting"))
        }
    }
}
