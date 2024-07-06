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
package com.tealcube.minecraft.bukkit.mythicdrops.repair

import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairItemManager
import dev.mythicdrops.spigot.choices.Choice
import org.koin.core.annotation.Single
import java.util.Locale

@Single
internal class MythicRepairItemManager : RepairItemManager {
    private val managedRepairItems = mutableMapOf<String, RepairItem>()

    override fun getById(id: String): RepairItem? = managedRepairItems[id.lowercase(Locale.getDefault())]

    override fun add(toAdd: RepairItem) {
        managedRepairItems[toAdd.name.lowercase(Locale.getDefault())] = toAdd
    }

    override fun addAll(toAdd: Collection<RepairItem>) {
        toAdd.forEach { add(it) }
    }

    override fun remove(id: String) {
        managedRepairItems.remove(id.lowercase(Locale.getDefault()))
    }

    override fun clear() {
        managedRepairItems.clear()
    }

    override fun get(): Set<RepairItem> = managedRepairItems.values.toSet()

    override fun contains(id: String): Boolean = managedRepairItems.containsKey(id.lowercase(Locale.getDefault()))

    override fun random(): RepairItem? = Choice.between(get()).choose()
}
