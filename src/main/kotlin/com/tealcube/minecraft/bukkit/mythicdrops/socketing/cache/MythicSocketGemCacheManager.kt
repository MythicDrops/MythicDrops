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
package com.tealcube.minecraft.bukkit.mythicdrops.socketing.cache

import com.tealcube.minecraft.bukkit.mythicdrops.api.choices.Choice
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.cache.SocketGemCache
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.cache.SocketGemCacheManager
import java.util.UUID

class MythicSocketGemCacheManager : SocketGemCacheManager {
    private val socketGemCaches = mutableMapOf<UUID, SocketGemCache>()

    override fun getOrCreateSocketGemCache(uuid: UUID): SocketGemCache = socketGemCaches.getOrPut(uuid) {
        MythicSocketGemCache(uuid)
    }

    override fun add(toAdd: SocketGemCache) {
        socketGemCaches[toAdd.owner] = toAdd
    }

    override fun remove(id: UUID) {
        socketGemCaches.remove(id)
    }

    override fun get(): Set<SocketGemCache> = socketGemCaches.values.toSet()

    override fun contains(id: UUID): Boolean = socketGemCaches.containsKey(id)

    override fun getById(id: UUID): SocketGemCache? = socketGemCaches[id]

    override fun clear() {
        socketGemCaches.clear()
    }

    override fun random(): SocketGemCache? = Choice.between(socketGemCaches.values).choose()
}
