/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2021 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.api.items

import com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories.CustomItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories.IdentificationItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories.SocketGemItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories.TieredItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories.TokenItemFactory

/**
 * Used for acquiring the multiple ways to produce a MythicDrops item.
 *
 * @since 7.0.0
 */
interface ProductionLine {
    /**
     * Used for converting [CustomItem]s into [org.bukkit.inventory.ItemStack]s.
     */
    val customItemFactory: CustomItemFactory

    /**
     * Used for building identity tomes and unidentified items.
     */
    val identificationItemFactory: IdentificationItemFactory

    /**
     * Used for converting [com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem]s into
     * [org.bukkit.inventory.ItemStack]s and building
     */
    val socketGemItemFactory: SocketGemItemFactory

    /**
     * Used for converting [com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier]s into
     * [org.bukkit.inventory.ItemStack]s.
     */
    val tieredItemFactory: TieredItemFactory

    /**
     * Used for converting [com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token]s into
     * [org.bukkit.inventory.ItemStack]s.
     */
    val tokenItemFactory: TokenItemFactory
}
