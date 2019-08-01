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
package com.tealcube.minecraft.bukkit.mythicdrops.socketting.combiners

import com.tealcube.minecraft.bukkit.mythicdrops.api.locations.Vec3
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.combiners.SocketGemCombiner
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.combiners.SocketGemCombinerManager
import java.util.UUID

class MythicSocketGemCombinerManager : SocketGemCombinerManager {
    private val mutableSocketGemCombiners = mutableSetOf<SocketGemCombiner>()

    override val socketGemCombiners: Set<SocketGemCombiner>
        get() = mutableSocketGemCombiners.toSet()

    override fun addSocketGemCombiner(socketGemCombiner: SocketGemCombiner) {
        mutableSocketGemCombiners.add(socketGemCombiner)
    }

    override fun addSocketGemCombinerAtLocation(vec3: Vec3): SocketGemCombiner {
        val socketGemCombiner = MythicSocketGemCombiner(UUID.randomUUID(), vec3)
        mutableSocketGemCombiners.add(socketGemCombiner)
        return socketGemCombiner
    }

    override fun removeSocketGemCombinerAtLocation(vec3: Vec3) {
        mutableSocketGemCombiners.removeIf { it.location == vec3 }
    }

    override fun removeSocketGemCombinerByUuid(uuid: UUID) {
        mutableSocketGemCombiners.removeIf { it.uuid == uuid }
    }

    override fun clearSocketGemCombiners() {
        mutableSocketGemCombiners.clear()
    }

    override fun isSocketGemCombinerAtLocation(vec3: Vec3): Boolean = socketGemCombiners.any { it.location == vec3 }
}
