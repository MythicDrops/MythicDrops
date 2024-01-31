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

import com.tealcube.minecraft.bukkit.mythicdrops.api.choices.WeightedChoice
import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.managers.MythicManager
import io.pixeloutlaw.kindling.Log
import org.bukkit.configuration.Configuration

internal class MythicSocketGemManager(
    private val itemGroupManager: ItemGroupManager,
    private val loadingErrorManager: LoadingErrorManager,
    private val socketTypeManager: SocketTypeManager
) : MythicManager<SocketGem, String>(), SocketGemManager {
    override fun getId(item: SocketGem): String = item.name.lowercase()

    override fun contains(id: String): Boolean = managed.containsKey(id.lowercase())

    override fun getById(id: String): SocketGem? = managed[id.lowercase()]

    override fun remove(id: String) {
        managed.remove(id.lowercase())
    }

    override fun randomByWeight(block: (SocketGem) -> Boolean): SocketGem? = WeightedChoice.between(get()).choose(block)

    override fun loadFromConfiguration(configuration: Configuration): Set<SocketGem> {
        Log.debug("Loading socket gems")
        val socketGemConfigurationSection = configuration.getOrCreateSection("socket-gems")
        val socketGems =
            socketGemConfigurationSection.getKeys(false).mapNotNull {
                MythicSocketGem.fromConfigurationSection(
                    socketGemConfigurationSection.getOrCreateSection(it),
                    it,
                    itemGroupManager,
                    loadingErrorManager,
                    socketTypeManager
                )
            }
        Log.info("Loaded socket gems (${socketGems.size}): ${socketGems.joinToString { it.name }}")
        return socketGems.toSet()
    }
}
