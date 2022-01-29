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
package com.tealcube.minecraft.bukkit.mythicdrops.items

import com.tealcube.minecraft.bukkit.mythicdrops.api.choices.Choice
import com.tealcube.minecraft.bukkit.mythicdrops.api.choices.WeightedChoice
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItemManager
import java.util.Locale

internal class MythicCustomItemManager : CustomItemManager {
    private val managedCustomItems = mutableMapOf<String, CustomItem>()

    override fun get(): Set<CustomItem> = managedCustomItems.values.toSet()

    override fun contains(id: String): Boolean = managedCustomItems.containsKey(id.lowercase(Locale.getDefault()))

    override fun add(toAdd: CustomItem) {
        managedCustomItems[toAdd.name.lowercase(Locale.getDefault())] = toAdd
    }

    override fun addAll(toAdd: Collection<CustomItem>) {
        toAdd.forEach { add(it) }
    }

    override fun remove(id: String) {
        managedCustomItems.remove(id.lowercase(Locale.getDefault()))
    }

    override fun getById(id: String): CustomItem? = managedCustomItems[id.lowercase(Locale.getDefault())]

    override fun clear() {
        managedCustomItems.clear()
    }

    override fun random(): CustomItem? = Choice.between(get()).choose()

    override fun randomByWeight(block: (CustomItem) -> Boolean): CustomItem? =
        WeightedChoice.between(get()).choose(block)
}
