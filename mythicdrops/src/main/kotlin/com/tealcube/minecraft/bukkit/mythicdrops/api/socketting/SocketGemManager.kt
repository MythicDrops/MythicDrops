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
package com.tealcube.minecraft.bukkit.mythicdrops.api.socketting

/**
 * A manager for storing and retrieving [SocketGem]s.
 */
interface SocketGemManager {
    /**
     * Fetches the [SocketGem] with a [SocketGem.name] matching [name]. Null if none found.
     *
     * @param name Name to search for
     *
     * @return Matching SocketGem, null if not found
     */
    fun getSocketGem(name: String): SocketGem?

    /**
     * Adds a [SocketGem] to the managed [SocketGem]s. Overwrites if a [SocketGem] with the same [SocketGem.name]
     * exists.
     *
     * @param socketGem SocketGem to manage
     */
    fun addSocketGem(socketGem: SocketGem)

    /**
     * Removes a [SocketGem] from the managed [SocketGem]s.
     *
     * @param name Name of SocketGem to remove
     */
    fun removeSocketGem(name: String)

    /**
     * Returns an unmodifiable [List] of the managed [SocketGem]s.
     *
     * @return unmodifiable [List] of managed [SocketGem]s
     */
    fun getSocketGems(): List<SocketGem>

    /**
     * Returns a random [SocketGem] from the managed [SocketGem]s. Null if one cannot be picked.
     *
     * @return random [SocketGem], null if unable to pick one
     */
    fun getRandom(): SocketGem?

    /**
     * Returns a random [SocketGem] from the managed [SocketGem]s based on [SocketGem.weight] and [block].
     * Null if one cannot be picked.
     *
     * @return random [SocketGem] based on weight, null if unable to pick one
     */
    fun getRandomByWeight(block: (SocketGem) -> Boolean = { true }): SocketGem?

    /**
     * Clears managed [SocketGem]s.
     */
    fun clearSocketGems()
}
