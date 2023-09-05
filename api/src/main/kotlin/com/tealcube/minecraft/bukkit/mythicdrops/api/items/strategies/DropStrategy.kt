/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2020 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.api.items.strategies

import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack

/**
 * Defines how drops are obtained within MythicDrops.
 */
interface DropStrategy {
    /**
     * Name of the drop strategy.
     */
    val name: String

    /**
     * Chance for a mob to get an item at all.
     */
    val itemChance: Double

    /**
     * Calculated chance for a mob to get a tiered item.
     */
    val tieredItemChance: Double

    /**
     * Calculated chance for a mob to get a custom item.
     */
    val customItemChance: Double

    /**
     * Calculated chance for a mob to get a socket gem.
     */
    val socketGemChance: Double

    /**
     * Calculated chance for a mob to get an unidentified item.
     */
    val unidentifiedItemChance: Double

    /**
     * Calculated chance for a mob to get a identity tome.
     */
    val identityTomeChance: Double

    /**
     * Calculated chance for a mob to get a socket extender.
     */
    val socketExtenderChance: Double

    /**
     * Determines which drops should be given on a [CreatureSpawnEvent]. Returns a list of [ItemStack]s and
     * their respective drop chances.
     *
     * @param event CreatureSpawnEvent to handle
     * @return items to drop and their respective drop chances
     */
    fun getDropsForCreatureSpawnEvent(event: CreatureSpawnEvent): List<Pair<ItemStack, Double>>

    /**
     * Determines which drops should be given on a [EntityDeathEvent]. Returns a list of [ItemStack]s and
     * their respective drop chances.
     *
     * @param event EntityDeathEvent to handle
     * @return items to drop and their respective drop chances
     */
    fun getDropsForEntityDeathEvent(event: EntityDeathEvent): List<Pair<ItemStack, Double>>
}
