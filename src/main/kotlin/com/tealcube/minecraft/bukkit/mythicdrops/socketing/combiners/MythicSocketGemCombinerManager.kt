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
package com.tealcube.minecraft.bukkit.mythicdrops.socketing.combiners

import com.tealcube.minecraft.bukkit.mythicdrops.api.choices.Choice
import com.tealcube.minecraft.bukkit.mythicdrops.api.locations.Vec3
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombiner
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerManager
import java.util.UUID

class MythicSocketGemCombinerManager : SocketGemCombinerManager {
    private val mutableSocketGemCombiners = mutableMapOf<UUID, SocketGemCombiner>()

    override fun add(socketGemCombiner: SocketGemCombiner) {
        mutableSocketGemCombiners.put(socketGemCombiner.uuid, socketGemCombiner)
    }

    override fun get(): Set<SocketGemCombiner> = mutableSocketGemCombiners.values.toSet()

    override fun contains(id: UUID): Boolean = mutableSocketGemCombiners.containsKey(id)

    override fun remove(id: UUID) {
        mutableSocketGemCombiners.remove(id)
    }

    override fun getById(id: UUID): SocketGemCombiner? = mutableSocketGemCombiners[id]

    override fun clear() {
        mutableSocketGemCombiners.clear()
    }

    override fun random(): SocketGemCombiner? = Choice.between(mutableSocketGemCombiners.values).choose()

    override fun addAtLocation(vec3: Vec3): SocketGemCombiner {
        val socketGemCombiner = MythicSocketGemCombiner(UUID.randomUUID(), vec3)
        add(socketGemCombiner)
        return socketGemCombiner
    }

    override fun removeAtLocation(vec3: Vec3) {
        mutableSocketGemCombiners.forEach { (key, value) ->
            if (value.location == vec3) {
                mutableSocketGemCombiners.remove(key)
            }
        }
    }

    override fun containsAtLocation(vec3: Vec3): Boolean = mutableSocketGemCombiners.values.any { it.location == vec3 }
}
