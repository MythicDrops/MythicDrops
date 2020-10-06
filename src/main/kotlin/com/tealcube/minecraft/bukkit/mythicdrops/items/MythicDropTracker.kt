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
package com.tealcube.minecraft.bukkit.mythicdrops.items

/**
 * Simple in-memory tracker for how many drops are occurring.
 */
@Suppress("detekt.TooManyFunctions")
object MythicDropTracker {
    var spawns = 0
        private set
    var items = 0
        private set
    var tieredItems = 0
        private set
    var customItems = 0
        private set
    var socketGems = 0
        private set
    var unidentifiedItems = 0
        private set
    var identityTomes = 0
        private set
    var socketExtenders = 0
        private set

    fun spawn() {
        spawns += 1
    }

    fun item() {
        items += 1
    }

    fun tieredItem() {
        tieredItems += 1
    }

    fun customItem() {
        customItems += 1
    }

    fun socketGem() {
        socketGems += 1
    }

    fun unidentifiedItem() {
        unidentifiedItems += 1
    }

    fun identityTome() {
        identityTomes += 1
    }

    fun socketExtender() {
        socketExtenders += 1
    }

    fun reset() {
        spawns = 0
        items = 0
        tieredItems = 0
        customItems = 0
        socketGems = 0
        unidentifiedItems = 0
        identityTomes = 0
        socketExtenders = 0
    }

    fun itemRate() = items.toDouble() / items

    fun tieredItemRate() = tieredItems.toDouble() / items

    fun customItemRate() = customItems.toDouble() / items

    fun socketGemRate() = socketGems.toDouble() / items

    fun unidentifiedItemRate() = unidentifiedItems.toDouble() / items

    fun identityTomeRate() = identityTomes.toDouble() / items

    fun socketExtenderRate() = socketExtenders.toDouble() / items
}
