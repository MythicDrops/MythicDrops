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
package com.tealcube.minecraft.bukkit.mythicdrops.socketing.cache

import com.tealcube.minecraft.bukkit.mythicdrops.additivePlus
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.GemTriggerType
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketEffect
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.cache.SocketCache

internal data class MythicSocketEffectCache(
    val armorCache: Map<GemTriggerType, Set<SocketEffect>> = emptyMap(),
    val mainHandCache: Map<GemTriggerType, Set<SocketEffect>> = emptyMap(),
    val offHandCache: Map<GemTriggerType, Set<SocketEffect>> = emptyMap()
) : SocketCache<SocketEffect> {
    override fun getArmor(gemTriggerType: GemTriggerType): Set<SocketEffect> =
        armorCache.getOrDefault(gemTriggerType, emptySet())

    override fun getMainHand(gemTriggerType: GemTriggerType): Set<SocketEffect> =
        mainHandCache.getOrDefault(gemTriggerType, emptySet())

    override fun getOffHand(gemTriggerType: GemTriggerType): Set<SocketEffect> =
        offHandCache.getOrDefault(gemTriggerType, emptySet())

    override fun setArmor(gemTriggerType: GemTriggerType, set: Set<SocketEffect>): SocketCache<SocketEffect> =
        copy(armorCache = armorCache.additivePlus(gemTriggerType to set))

    override fun setMainHand(gemTriggerType: GemTriggerType, set: Set<SocketEffect>): SocketCache<SocketEffect> =
        copy(mainHandCache = mainHandCache.additivePlus(gemTriggerType to set))

    override fun setOffHand(gemTriggerType: GemTriggerType, set: Set<SocketEffect>): SocketCache<SocketEffect> =
        copy(offHandCache = offHandCache.additivePlus(gemTriggerType to set))

    override fun clearArmor(): SocketCache<SocketEffect> = copy(armorCache = emptyMap())

    override fun clearMainHand(): SocketCache<SocketEffect> = copy(mainHandCache = emptyMap())

    override fun clearOffHand(): SocketCache<SocketEffect> = copy(offHandCache = emptyMap())
}
