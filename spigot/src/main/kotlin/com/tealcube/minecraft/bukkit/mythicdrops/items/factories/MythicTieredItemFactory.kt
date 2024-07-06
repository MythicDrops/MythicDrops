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
package com.tealcube.minecraft.bukkit.mythicdrops.items.factories

import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.builders.DropBuilder
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories.TieredItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.relations.RelationManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketExtenderTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.items.builders.MythicDropBuilder
import org.bukkit.inventory.ItemStack

/**
 * Implementation of [TieredItemFactory].
 */
internal class MythicTieredItemFactory(
    private val itemGroupManager: ItemGroupManager,
    private val relationManager: RelationManager,
    private val settingsManager: SettingsManager,
    private val tierManager: TierManager,
    private val socketTypeManager: SocketTypeManager,
    private val socketExtenderTypeManager: SocketExtenderTypeManager
) : TieredItemFactory {
    @Suppress("DEPRECATION")
    override fun getNewDropBuilder(): DropBuilder =
        MythicDropBuilder(
            itemGroupManager,
            relationManager,
            settingsManager,
            tierManager,
            socketTypeManager,
            socketExtenderTypeManager
        )

    override fun toItemStack(tier: Tier): ItemStack? = getNewDropBuilder().withTier(tier).build()
}
