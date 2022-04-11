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
package io.pixeloutlaw.minecraft.spigot.mythicdrops

import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin
import org.bukkit.NamespacedKey

/**
 * Creates a [NamespacedKey] in the "MythicDrops" namespace in a "Spigot Approved"^TM way.
 *
 * @param key
 */
internal fun mythicDrops(key: String): NamespacedKey = NamespacedKey(MythicDropsPlugin.getInstance(), key)

/**
 * The [NamespacedKey] used for storing which custom item is used.
 */
val mythicDropsCustomItem = mythicDrops("custom-item")

/**
 * The [NamespacedKey] used for storing which socket gem is used.
 */
val mythicDropsSocketGem = mythicDrops("socket-gem")

/**
 * The [NamespacedKey] used for storing which tier is used.
 */
val mythicDropsTier = mythicDrops("tier")

/**
 * The [NamespacedKey] used for storing if the current item is a socket extender.
 */
val mythicDropsSocketExtender = mythicDrops("socket-extender")
