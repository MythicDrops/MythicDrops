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
package com.tealcube.minecraft.bukkit.mythicdrops.errors

import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.choices.Choice
import org.koin.core.annotation.Single
import java.util.UUID

@Single
internal class MythicLoadingErrorManager : LoadingErrorManager {
    private val managedLoadingErrors = mutableMapOf<UUID, String>()

    override fun get(): Set<String> = managedLoadingErrors.values.toSet()

    override fun contains(id: UUID): Boolean = managedLoadingErrors.containsKey(id)

    override fun add(toAdd: String) {
        managedLoadingErrors[UUID.randomUUID()] = toAdd
    }

    override fun addAll(toAdd: Collection<String>) {
        toAdd.forEach { add(it) }
    }

    override fun remove(id: UUID) {
        managedLoadingErrors.remove(id)
    }

    override fun getById(id: UUID): String? = managedLoadingErrors[id]

    override fun clear() {
        managedLoadingErrors.clear()
    }

    override fun random(): String? = Choice.between(get()).choose()
}
