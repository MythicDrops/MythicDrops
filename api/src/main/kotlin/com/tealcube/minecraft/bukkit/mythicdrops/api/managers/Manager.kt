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
package com.tealcube.minecraft.bukkit.mythicdrops.api.managers

/**
 * A manager for storing and retrieving [T] with a key type of [ID].
 */
interface Manager<T, ID> {

    /**
     * Gets an unmodifiable [Set] of [T]s.
     *
     * @return unmodifiable Set of T
     */
    fun get(): Set<T>

    /**
     * Gets if this manager contains an item with the given [id].
     *
     * @param id ID of item to look for
     *
     * @return if item with [id] is managed
     */
    fun contains(id: ID): Boolean

    /**
     * Adds a [T] to the managed [T]s. Will overwrite any other [T] with the same [ID].
     *
     * @param toAdd item to add
     */
    fun add(toAdd: T)

    /**
     * Adds all of the given values to the managed [T]s. Will overwrite any other [T]s with the same [ID]s.
     *
     * @param toAdd items to add
     */
    fun addAll(toAdd: Collection<T>)

    /**
     * Removes any [T]s that have the given [id].
     *
     * @param id ID to remove
     */
    fun remove(id: ID)

    /**
     * Attempts to fetch a [T] by the given [id]. Returns null if none found.
     *
     * @param id ID to search by
     *
     * @return managed item if found, null if not
     */
    fun getById(id: ID): T?

    /**
     * Clears the managed items.
     */
    fun clear()

    /**
     * Returns a random item from the managed items, null if unable to pick one.
     *
     * @return random item, null if unable to pick
     */
    fun random(): T?
}
