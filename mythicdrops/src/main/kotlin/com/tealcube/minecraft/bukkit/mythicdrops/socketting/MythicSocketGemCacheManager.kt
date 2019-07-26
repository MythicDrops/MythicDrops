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
package com.tealcube.minecraft.bukkit.mythicdrops.socketting

import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketGemCache
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketGemCacheManager
import java.util.UUID

class MythicSocketGemCacheManager : SocketGemCacheManager {
    private val socketGemCaches = mutableMapOf<UUID, SocketGemCache>()

    override fun getOrCreateSocketGemCache(uuid: UUID): SocketGemCache = socketGemCaches.getOrPut(uuid) {
        MythicSocketGemCache(uuid)
    }

    override fun addSocketGemCache(socketGemCache: SocketGemCache) {
        socketGemCaches[socketGemCache.owner] = socketGemCache
    }

    override fun removeSocketGemCache(uuid: UUID) {
        socketGemCaches.remove(uuid)
    }

    override fun removeSocketGemCache(socketGemCache: SocketGemCache) {
        socketGemCaches.remove(socketGemCache.owner)
    }

    override fun getSocketGemCaches(): Set<SocketGemCache> = socketGemCaches.values.toSet()

    override fun hasSocketGemCache(uuid: UUID): Boolean = socketGemCaches.containsKey(uuid)
}