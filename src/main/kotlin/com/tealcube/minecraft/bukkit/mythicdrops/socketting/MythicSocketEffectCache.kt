/*
 * The MIT License
 * Copyright © 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.socketting

import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.GemTriggerType
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketCache
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketEffect

data class MythicSocketEffectCache(
    private val armorCache: Map<GemTriggerType, Set<SocketEffect>> = emptyMap(),
    private val mainHandCache: Map<GemTriggerType, Set<SocketEffect>> = emptyMap(),
    private val offHandCache: Map<GemTriggerType, Set<SocketEffect>> = emptyMap()
) : SocketCache<SocketEffect> {
    override fun getArmor(gemTriggerType: GemTriggerType): Set<SocketEffect> =
        armorCache.getOrDefault(gemTriggerType, emptySet())

    override fun getMainHand(gemTriggerType: GemTriggerType): Set<SocketEffect> =
        mainHandCache.getOrDefault(gemTriggerType, emptySet())

    override fun getOffHand(gemTriggerType: GemTriggerType): Set<SocketEffect> =
        offHandCache.getOrDefault(gemTriggerType, emptySet())

    override fun setArmor(gemTriggerType: GemTriggerType, set: Set<SocketEffect>): SocketCache<SocketEffect> =
        copy(armorCache = armorCache.plus(gemTriggerType to set))

    override fun setMainHand(gemTriggerType: GemTriggerType, set: Set<SocketEffect>): SocketCache<SocketEffect> =
        copy(mainHandCache = mainHandCache.plus(gemTriggerType to set))

    override fun setOffHand(gemTriggerType: GemTriggerType, set: Set<SocketEffect>): SocketCache<SocketEffect> =
        copy(offHandCache = offHandCache.plus(gemTriggerType to set))

    override fun clearArmor(): SocketCache<SocketEffect> = copy(armorCache = emptyMap())

    override fun clearMainHand(): SocketCache<SocketEffect> = copy(mainHandCache = emptyMap())

    override fun clearOffHand(): SocketCache<SocketEffect> = copy(offHandCache = emptyMap())
}