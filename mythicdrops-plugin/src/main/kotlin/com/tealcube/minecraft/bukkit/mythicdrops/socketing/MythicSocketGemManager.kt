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
package com.tealcube.minecraft.bukkit.mythicdrops.socketing

import com.tealcube.minecraft.bukkit.mythicdrops.api.choices.Choice
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGemManager
import com.tealcube.minecraft.bukkit.mythicdrops.choices.WeightedChoice
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import io.pixeloutlaw.kindling.Log
import org.bukkit.configuration.Configuration
import org.koin.core.annotation.Single
import java.util.Locale

@Single
internal class MythicSocketGemManager(private val itemGroupManager: ItemGroupManager) : SocketGemManager {
    private val managedSocketGems = mutableMapOf<String, SocketGem>()

    override fun get(): Set<SocketGem> = managedSocketGems.values.toSet()

    override fun contains(id: String): Boolean = managedSocketGems.containsKey(id.lowercase(Locale.getDefault()))

    override fun getById(id: String): SocketGem? = managedSocketGems[id.lowercase(Locale.getDefault())]

    override fun add(toAdd: SocketGem) {
        managedSocketGems[toAdd.name.lowercase(Locale.getDefault())] = toAdd
    }

    override fun addAll(toAdd: Collection<SocketGem>) {
        toAdd.forEach { add(it) }
    }

    override fun remove(id: String) {
        managedSocketGems.remove(id.lowercase(Locale.getDefault()))
    }

    override fun random(): SocketGem? = Choice.between(get()).choose()

    override fun randomByWeight(block: (SocketGem) -> Boolean): SocketGem? =
        WeightedChoice.between(get()).choose(block)

    override fun clear() {
        managedSocketGems.clear()
    }

    override fun loadFromConfiguration(configuration: Configuration): Set<SocketGem> {
        Log.debug("Loading socket gems")
        val socketGemConfigurationSection = configuration.getOrCreateSection("socket-gems")
        val socketGems = socketGemConfigurationSection.getKeys(false).mapNotNull {
            MythicSocketGem.fromConfigurationSection(
                socketGemConfigurationSection.getOrCreateSection(it),
                it,
                itemGroupManager
            )
        }
        Log.info("Loaded socket gems (${socketGems.size}): ${socketGems.joinToString { it.name }}")
        return socketGems.toSet()
    }
}
