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
package com.tealcube.minecraft.bukkit.mythicdrops.items.strategies

import com.tealcube.minecraft.bukkit.mythicdrops.api.items.strategies.DropStrategy
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.config.DropsOptions
import com.tealcube.minecraft.bukkit.mythicdrops.api.worldguard.WorldGuardFlags
import com.tealcube.minecraft.spigot.worldguard.adapters.lib.WorldGuardAdapters
import org.bukkit.Location

internal abstract class AbstractDropStrategy : DropStrategy {
    protected fun getDropChances(dropsOptions: DropsOptions): StrategyDropChances {
        val tieredItemChance = dropsOptions.tieredItemChance
        val customItemChance = dropsOptions.customItemChance
        val socketGemChance = dropsOptions.socketGemChance
        val unidentifiedItemChance =
            dropsOptions.unidentifiedItemChance
        val identityTomeChance = dropsOptions.identityTomeChance
        val socketExtenderChance = dropsOptions.socketExtenderChance

        return StrategyDropChances(
            tieredItemChance,
            customItemChance,
            socketGemChance,
            unidentifiedItemChance,
            identityTomeChance,
            socketExtenderChance
        )
    }

    protected fun getWorldGuardFlags(location: Location): StrategyWorldGuardFlags {
        val tieredAllowedAtLocation =
            WorldGuardAdapters.isFlagAllowAtLocation(location, WorldGuardFlags.mythicDropsTiered)
        val customItemAllowedAtLocation =
            WorldGuardAdapters.isFlagAllowAtLocation(location, WorldGuardFlags.mythicDropsCustom)
        val socketGemAllowedAtLocation =
            WorldGuardAdapters.isFlagAllowAtLocation(location, WorldGuardFlags.mythicDropsSocketGem)
        val unidentifiedItemAllowedAtLocation =
            WorldGuardAdapters.isFlagAllowAtLocation(location, WorldGuardFlags.mythicDropsUnidentifiedItem)
        val identityTomeAllowedAtLocation =
            WorldGuardAdapters.isFlagAllowAtLocation(location, WorldGuardFlags.mythicDropsIdentityTome)
        val socketExtendersAllowedAtLocation =
            WorldGuardAdapters.isFlagAllowAtLocation(location, WorldGuardFlags.mythicDropsSocketExtender)

        return StrategyWorldGuardFlags(
            tieredAllowedAtLocation,
            customItemAllowedAtLocation,
            socketGemAllowedAtLocation,
            unidentifiedItemAllowedAtLocation,
            identityTomeAllowedAtLocation,
            socketExtendersAllowedAtLocation
        )
    }
}
