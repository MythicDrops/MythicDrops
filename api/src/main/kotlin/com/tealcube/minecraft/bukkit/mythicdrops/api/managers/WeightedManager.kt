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

import com.tealcube.minecraft.bukkit.mythicdrops.api.weight.Weighted

interface WeightedManager<T : Weighted, Key> : Manager<T, Key> {
    /**
     * Returns a random [T] from the managed [T]s based on [Weighted.weight] and [block].
     * Null if one cannot be picked.
     *
     * @param block block that filters applicable managed items
     *
     * @return random [T] based on weight, null if unable to pick one
     */
    fun randomByWeight(block: (T) -> Boolean): T?

    /**
     * Returns a random [T] from the managed [T]s based on [Weighted.weight].
     * Null if one cannot be picked.
     *
     * @return random [T] based on weight, null if unable to pick one
     */
    fun randomByWeight(): T? = randomByWeight { true }
}
