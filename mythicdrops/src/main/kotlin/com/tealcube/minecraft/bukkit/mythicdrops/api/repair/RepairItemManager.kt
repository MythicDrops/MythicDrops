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
package com.tealcube.minecraft.bukkit.mythicdrops.api.repair

/**
 * A manager for storing and retrieving [RepairItem]s.
 */
interface RepairItemManager {
    /**
     * Fetches the [RepairItem] with a [RepairItem.name] matching [name]. Null if none found.
     *
     * @param name to search for
     *
     * @return Matching RepairItem, null if not found
     */
    fun getRepairItem(name: String): RepairItem?

    /**
     * Adds a [RepairItem] to the managed [RepairItem]s. Overwrites if a [RepairItem] with the same [RepairItem.name]
     * exists.
     *
     * @param repairItem SocketGem to manage
     */
    fun addRepairItem(repairItem: RepairItem)

    /**
     * Removes a [RepairItem] from the managed [RepairItem]s.
     *
     * @param name Name of RepairItem to remove
     */
    fun removeRepairItem(name: String)

    /**
     * Clears managed [RepairItem]s.
     */
    fun clearRepairItems()

    /**
     * Returns an unmodifiable [List] of the managed [RepairItem]s.
     *
     * @return unmodifiable [List] of the managed [RepairItem]s
     */
    fun getRepairItems(): List<RepairItem>
}
