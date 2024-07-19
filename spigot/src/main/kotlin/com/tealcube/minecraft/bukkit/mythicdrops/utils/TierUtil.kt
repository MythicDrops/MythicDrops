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
package com.tealcube.minecraft.bukkit.mythicdrops.utils

import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.CreatureSpawningSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getTier
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack

// Trying to not use static methods as they make future unit testing difficult
// REMOVE IN 9.0.0
@Deprecated("Use TierManager or the extension methods instead")
internal object TierUtil {
    private val internalTierManager: TierManager
        get() {
            return MythicDropsApi.mythicDrops.tierManager
        }

    @Deprecated(
        "Use the method on the TierManager instead",
        ReplaceWith("tierManager.getByName(name)", "com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager")
    )
    fun getTier(name: String): Tier? = internalTierManager.getByName(name)

    @Deprecated(
        "Use the extensions method instead",
        ReplaceWith("itemStack.getTier(tierManager)", "io.pixeloutlaw.minecraft.spigot.mythicdrops.getTier")
    )
    fun getTierFromItemStack(itemStack: ItemStack): Tier? = itemStack.getTier(internalTierManager, false)

    @Deprecated(
        "Use the extensions method instead",
        ReplaceWith("itemStack.getTier(tierManager)", "io.pixeloutlaw.minecraft.spigot.mythicdrops.getTier")
    )
    fun getTierFromItemStack(
        itemStack: ItemStack,
        tiers: Collection<Tier>
    ): Tier? = itemStack.getTier(tiers, false)

    @Deprecated(
        "Use the extensions method instead",
        ReplaceWith(
            "livingEntity.getTier(creatureSpawningSettings, tierManager)",
            "io.pixeloutlaw.minecraft.spigot.mythicdrops.getTier"
        )
    )
    fun getTierForLivingEntity(
        livingEntity: LivingEntity,
        creatureSpawningSettings: CreatureSpawningSettings,
        tierManager: TierManager
    ): Tier? = livingEntity.getTier(creatureSpawningSettings, tierManager)
}
