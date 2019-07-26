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
package com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.cache

import java.util.UUID

/**
 * A manager for storing and retrieving [SocketGemCache]s.
 */
interface SocketGemCacheManager {
    /**
     * Gets the appropriate [SocketGemCache] or creates it if necessary.
     *
     * @param uuid UUID of owner of cache
     * @return cached socket gem data
     */
    fun getOrCreateSocketGemCache(uuid: UUID): SocketGemCache

    /**
     * Adds a [SocketGemCache] to the managed collection. Replaces if one already exists for the owner.
     *
     * @param socketGemCache cache to manage
     */
    fun addSocketGemCache(socketGemCache: SocketGemCache)

    /**
     * Removes any cached data with the given [UUID].
     *
     * @param uuid UUID of owner of cache
     */
    fun removeSocketGemCache(uuid: UUID)

    /**
     * Removes the given cached data if it is managed.
     *
     * @param socketGemCache cached data to remove
     */
    fun removeSocketGemCache(socketGemCache: SocketGemCache)

    /**
     * Returns an unmodifiable [Set] of the [SocketGemCache]s managed.
     *
     * @return unmodifiable [Set]
     */
    fun getSocketGemCaches(): Set<SocketGemCache>

    /**
     * Checks if there are any managed [SocketGemCache]s with an [SocketGemCache.owner] that match [uuid].
     *
     * @return if any match
     */
    fun hasSocketGemCache(uuid: UUID): Boolean
}
