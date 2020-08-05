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
package com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.cache

import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.GemTriggerType

/**
 * A container of cached [T] data.
 */
interface SocketCache<T> {
    /**
     * Gets any cached [T]s for the given [GemTriggerType] associated with armor.
     *
     * @param gemTriggerType type of gem
     * @return cached [T]s
     */
    fun getArmor(gemTriggerType: GemTriggerType): Set<T>

    /**
     * Gets any cached [T]s for the given [GemTriggerType] associated with the main hand.
     *
     * @param gemTriggerType type of gem
     * @return cached [T]s
     */
    fun getMainHand(gemTriggerType: GemTriggerType): Set<T>

    /**
     * Gets any cached [T]s for the given [GemTriggerType] associated with the off hand.
     *
     * @param gemTriggerType type of gem
     * @return cached [T]s
     */
    fun getOffHand(gemTriggerType: GemTriggerType): Set<T>

    /**
     * Sets cache associated with armor for given [GemTriggerType] to the given [Set] of [T]s. May or may not
     * operate on this instance of the cache container.
     *
     * @param gemTriggerType type of gem
     * @param set effects to cache
     * @return cache with updated effects
     */
    fun setArmor(gemTriggerType: GemTriggerType, set: Set<T>): SocketCache<T>

    /**
     * Sets cache associated with the main hand for given [GemTriggerType] to the given [Set] of [T]s. May or may not
     * operate on this instance of the cache container.
     *
     * @param gemTriggerType type of gem
     * @param set effects to cache
     * @return cache with updated effects
     */
    fun setMainHand(gemTriggerType: GemTriggerType, set: Set<T>): SocketCache<T>

    /**
     * Sets cache associated with the off hand for given [GemTriggerType] to the given [Set] of [T]s. May or may not
     * operate on this instance of the cache container.
     *
     * @param gemTriggerType type of gem
     * @param set effects to cache
     * @return cache with updated effects
     */
    fun setOffHand(gemTriggerType: GemTriggerType, set: Set<T>): SocketCache<T>

    /**
     * Clears the cache associated with armor. May or may not operate on this instance of the cache container.
     *
     * @return cache container with cleared armor cache
     */
    fun clearArmor(): SocketCache<T>

    /**
     * Clears the cache associated with main hand. May or may not operate on this instance of the cache container.
     *
     * @return cache container with cleared main hand cache
     */
    fun clearMainHand(): SocketCache<T>

    /**
     * Clears the cache associated with off hand. May or may not operate on this instance of the cache container.
     *
     * @return cache container with cleared off hand cache
     */
    fun clearOffHand(): SocketCache<T>
}
