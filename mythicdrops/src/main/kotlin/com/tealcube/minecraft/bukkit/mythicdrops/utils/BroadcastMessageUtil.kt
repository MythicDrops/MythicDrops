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
package com.tealcube.minecraft.bukkit.mythicdrops.utils

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.LanguageSettings
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.replaceArgs
import mkremins.fanciful.FancyMessage
import org.apache.commons.text.WordUtils
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object BroadcastMessageUtil {
    /**
     * Broadcasts that an item was found to all players in the player's world.
     */
    fun broadcastItem(languageSettings: LanguageSettings, player: Player?, itemStack: ItemStack) {
        val displayName = player?.displayName ?: languageSettings.command.unknownPlayer
        val locale = languageSettings.general.foundItemBroadcast.replaceArgs("%receiver%" to displayName).chatColorize()
        val messages = locale.split("%item%")
        val fancyMessage = FancyMessage("")
        for (idx in messages.indices) {
            val key = messages[idx]
            if (idx < messages.size - 1) {
                fancyMessage.then(key).then(
                    itemStack.itemMeta?.displayName
                        ?: WordUtils.capitalizeFully(itemStack.type.name.split("_").joinToString(" "))
                ).itemTooltip(itemStack)
            } else {
                fancyMessage.then(key)
            }
        }
        player?.world?.players?.forEach { p ->
            fancyMessage.send(p)
        }
    }
}
