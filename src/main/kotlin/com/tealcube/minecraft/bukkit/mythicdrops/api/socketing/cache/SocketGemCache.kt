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
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketCommand
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketEffect
import org.bukkit.inventory.ItemStack
import java.util.UUID

/**
 * A container of cached data useful for socketting.
 *
 * @property owner Owner of this data
 */
interface SocketGemCache {
    val owner: UUID

    /**
     * Gets any cached [SocketEffect]s for the given [GemTriggerType] associated with armor.
     *
     * @param gemTriggerType type of gem
     * @return cached [SocketEffect]s
     */
    fun getArmorSocketEffects(gemTriggerType: GemTriggerType): Set<SocketEffect>

    /**
     * Gets any cached [SocketEffect]s for the given [GemTriggerType] associated with the main hand.
     *
     * @param gemTriggerType type of gem
     * @return cached [SocketEffect]s
     */
    fun getMainHandSocketEffects(gemTriggerType: GemTriggerType): Set<SocketEffect>

    /**
     * Gets any cached [SocketEffect]s for the given [GemTriggerType] associated with the off hand.
     *
     * @param gemTriggerType type of gem
     * @return cached [SocketEffect]s
     */
    fun getOffHandSocketEffects(gemTriggerType: GemTriggerType): Set<SocketEffect>

    /**
     * Sets cache associated with armor for given [GemTriggerType] to the given [Set] of [SocketEffect]s.
     * May or may not operate on this instance of the cache container.
     *
     * @param gemTriggerType type of gem
     * @param set effects to cache
     * @return cache with updated effects
     */
    fun setArmorSocketEffects(gemTriggerType: GemTriggerType, set: Set<SocketEffect>): SocketGemCache

    /**
     * Sets cache associated with the main hand for given [GemTriggerType] to the given [Set] of [SocketEffect]s.
     * May or may not operate on this instance of the cache container.
     *
     * @param gemTriggerType type of gem
     * @param set effects to cache
     * @return cache with updated effects
     */
    fun setMainHandSocketEffects(gemTriggerType: GemTriggerType, set: Set<SocketEffect>): SocketGemCache

    /**
     * Sets cache associated with the off hand for given [GemTriggerType] to the given [Set] of [SocketEffect]s.
     * May or may not operate on this instance of the cache container.
     *
     * @param gemTriggerType type of gem
     * @param set effects to cache
     * @return cache with updated effects
     */
    fun setOffHandSocketEffects(gemTriggerType: GemTriggerType, set: Set<SocketEffect>): SocketGemCache

    /**
     * Gets any cached [SocketCommand]s for the given [GemTriggerType] associated with armor.
     *
     * @param gemTriggerType type of gem
     * @return cached [SocketCommand]s
     */
    fun getArmorSocketCommands(gemTriggerType: GemTriggerType): Set<SocketCommand>

    /**
     * Gets any cached [SocketCommand]s for the given [GemTriggerType] associated with the main hand.
     *
     * @param gemTriggerType type of gem
     * @return cached [SocketCommand]s
     */
    fun getMainHandSocketCommands(gemTriggerType: GemTriggerType): Set<SocketCommand>

    /**
     * Gets any cached [SocketCommand]s for the given [GemTriggerType] associated with the off hand.
     *
     * @param gemTriggerType type of gem
     * @return cached [SocketCommand]s
     */
    fun getOffHandSocketCommands(gemTriggerType: GemTriggerType): Set<SocketCommand>

    /**
     * Sets cache associated with armor for given [GemTriggerType] to the given [Set] of [SocketCommand]s.
     * May or may not operate on this instance of the cache container.
     *
     * @param gemTriggerType type of gem
     * @param set effects to cache
     * @return cache with updated effects
     */
    fun setArmorSocketCommands(gemTriggerType: GemTriggerType, set: Set<SocketCommand>): SocketGemCache

    /**
     * Sets cache associated with the main hand for given [GemTriggerType] to the given [Set] of [SocketCommand]s.
     * May or may not operate on this instance of the cache container.
     *
     * @param gemTriggerType type of gem
     * @param set effects to cache
     * @return cache with updated effects
     */
    fun setMainHandSocketCommands(gemTriggerType: GemTriggerType, set: Set<SocketCommand>): SocketGemCache

    /**
     * Sets cache associated with the off hand for given [GemTriggerType] to the given [Set] of [SocketCommand]s.
     * May or may not operate on this instance of the cache container.
     *
     * @param gemTriggerType type of gem
     * @param set effects to cache
     * @return cache with updated effects
     */
    fun setOffHandSocketCommands(gemTriggerType: GemTriggerType, set: Set<SocketCommand>): SocketGemCache

    /**
     * Updates the armor cache(s). May or may not operate on this instance of the cache container.
     *
     * @return cache container with updated armor cache(s)
     */
    fun updateArmor(): SocketGemCache

    /**
     * Updates the main hand cache(s). May or may not operate on this instance of the cache container.
     *
     * @return cache container with updated main hand cache(s)
     */
    fun updateMainHand(): SocketGemCache

    /**
     * Updates the main hand cache(s) based on the given [itemInMainHand]. May or may not operate on this instance of
     * the cache container.
     *
     * @return cache container with updated main hand cache(s)
     */
    fun updateMainHand(itemInMainHand: ItemStack?): SocketGemCache

    /**
     * Updates the off hand cache(s). May or may not operate on this instance of the cache container.
     *
     * @return cache container with updated off hand cache(s)
     */
    fun updateOffHand(): SocketGemCache

    /**
     * Updates the off hand cache(s) based on the given [itemInOffHand]. May or may not operate on this instance of
     * the cache container.
     *
     * @return cache container with updated off hand cache(s)
     */
    fun updateOffHand(itemInOffHand: ItemStack?): SocketGemCache
}
