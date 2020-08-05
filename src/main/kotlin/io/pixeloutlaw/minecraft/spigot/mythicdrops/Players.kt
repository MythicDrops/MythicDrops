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
package io.pixeloutlaw.minecraft.spigot.mythicdrops

import com.tealcube.minecraft.bukkit.mythicdrops.sendMythicMessage
import java.util.UUID
import org.bukkit.entity.Player

// Number of milliseconds between messages
const val MILLISECONDS_BETWEEN_MESSAGES = 1000

// Holds player UUIDs and when they last got a message
private val spamBuster: MutableMap<UUID, Long> = mutableMapOf()

/**
 * Sends a message to a player once every [MILLISECONDS_BETWEEN_MESSAGES].
 *
 * @param message message to send
 * @param args args to replace in the message
 */
fun Player.sendNonSpamMessage(message: String, args: Collection<Pair<String, String>> = emptyList()) {
    val currentTime = System.currentTimeMillis()
    if (currentTime - spamBuster.getOrDefault(uniqueId, 0) >= MILLISECONDS_BETWEEN_MESSAGES) {
        sendMythicMessage(message, args)
        spamBuster[uniqueId] = currentTime
    }
}

/**
 * Sends a message to a player once every [MILLISECONDS_BETWEEN_MESSAGES].
 *
 * @param message message to send
 * @param args args to replace in the message
 */
fun Player.sendNonSpamMessage(message: String, vararg args: Pair<String, String>) =
    sendNonSpamMessage(message, args.toList())
