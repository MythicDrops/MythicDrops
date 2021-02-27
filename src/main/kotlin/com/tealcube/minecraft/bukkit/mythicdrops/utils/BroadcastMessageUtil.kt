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
import com.tealcube.minecraft.bukkit.mythicdrops.replaceArgs
import io.pixeloutlaw.minecraft.spigot.plumbing.api.AbstractMessageBroadcaster
import io.pixeloutlaw.minecraft.spigot.plumbing.lib.MessageBroadcaster
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

internal object BroadcastMessageUtil {
    /**
     * Broadcasts that an item was found to all players in the player's world.
     */
    fun broadcastItem(
        languageSettings: LanguageSettings,
        player: Player,
        itemStack: ItemStack,
        audiences: BukkitAudiences,
        broadcastTarget: String
    ) {
        val convertedBroadcastTarget = broadcastTargetFromString(broadcastTarget)
        broadcastItem(languageSettings, player, itemStack, audiences, convertedBroadcastTarget)
    }

    /**
     * Broadcasts that an item was found to all players in the player's world.
     */
    fun broadcastItem(
        languageSettings: LanguageSettings,
        player: Player,
        itemStack: ItemStack,
        audiences: BukkitAudiences,
        broadcastTarget: AbstractMessageBroadcaster.BroadcastTarget = AbstractMessageBroadcaster.BroadcastTarget.WORLD
    ) {
        MessageBroadcaster.broadcastItem(
            languageSettings.general.foundItemBroadcast.replaceArgs("%receiver%" to "%player%"),
            player,
            itemStack,
            audiences,
            broadcastTarget
        )
    }

    private fun broadcastTargetFromString(str: String): AbstractMessageBroadcaster.BroadcastTarget {
        return AbstractMessageBroadcaster.BroadcastTarget.values().firstOrNull {
            it.name.equals(str, true)
        } ?: AbstractMessageBroadcaster.BroadcastTarget.WORLD
    }
}
