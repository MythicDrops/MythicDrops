/*
 * The MIT License
 * Copyright © 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.utils

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.ConfigSettings
import mkremins.fanciful.FancyMessage
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object BroadcastMessageUtil {
    /**
     * Broadcasts that an item was found to all players in the player's world.
     */
    fun broadcastItem(configSettings: ConfigSettings, player: Player?, itemStack: ItemStack) {
        val displayName = player?.displayName ?: configSettings.getLanguageString("command.unknown-player")
        val locale = configSettings.getFormattedLanguageString("command.found-item-broadcast", listOf("%receiver%" to displayName))
        val messages = locale.split("%item%")
        val fancyMessage = FancyMessage("")
        for (idx in messages.indices) {
            val key = messages[idx]
            if (idx < messages.size - 1) {
                fancyMessage.then(key).then(itemStack.itemMeta!!.displayName).itemTooltip(itemStack)
            } else {
                fancyMessage.then(key)
            }
        }
        player?.world?.players?.forEach { p ->
            fancyMessage.send(p)
        }
    }
}