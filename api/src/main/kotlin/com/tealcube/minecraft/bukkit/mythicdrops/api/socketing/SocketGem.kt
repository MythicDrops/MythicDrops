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
package com.tealcube.minecraft.bukkit.mythicdrops.api.socketing

import com.tealcube.minecraft.bukkit.mythicdrops.api.attributes.MythicAttribute
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroup
import com.tealcube.minecraft.bukkit.mythicdrops.api.weight.Weighted
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemFlag

/**
 * Holds information about a Socket Gem.
 *
 * @property name Name of Socket Gem (appears in item lore)
 * @property prefix Prefix of Socket Gem (defaults to empty string)
 * @property suffix Suffix of Socket Gem (defaults to empty string)
 * @property lore Lore of Socket Gem (defaults to empty list)
 * @property socketEffects Socket Effects of Socket Gem (defaults to empty set)
 * @property anyOfItemGroups Any-Of Item Groups of Socket Gem (defaults to empty list)
 * @property allOfItemGroups Any-Of Item Groups of Socket Gem (defaults to empty list)
 * @property noneOfItemGroups None-Of Item Groups of Socket Gem (defaults to empty list)
 * @property gemTriggerType Gem Type of Socket Gem (defaults to [GemTriggerType.ON_HIT_AND_WHEN_HIT])
 * @property enchantments Enchantments of Socket Gem (defaults to empty set)
 * @property commands Commands of Socket Gem (defaults to empty list)
 * @property entityTypesCanDropFrom Entity Types that the Socket Gem can drop from (defaults to empty list)
 * @property family Family of the Socket Gem
 * @property level Level of the Socket Gem
 * @property attributes Attributes to add to items (defaults to empty set)
 * @property isBroadcastOnFind Should be broadcast to server when found?
 */
interface SocketGem : Weighted {
    val name: String
    val prefix: String
    val suffix: String
    val lore: List<String>
    val socketEffects: Set<SocketEffect>
    val anyOfItemGroups: List<ItemGroup>
    val allOfItemGroups: List<ItemGroup>
    val noneOfItemGroups: List<ItemGroup>
    val gemTriggerType: GemTriggerType
    val enchantments: Set<MythicEnchantment>
    val commands: List<SocketCommand>
    val entityTypesCanDropFrom: Set<EntityType>
    val family: String
    val level: Int
    val attributes: Set<MythicAttribute>
    val isBroadcastOnFind: Boolean
    val hasCustomModelData: Boolean
    val customModelData: Int
    val socketType: SocketType
    val itemFlags: Set<ItemFlag>

    /**
     * Determines if this can drop from a given [EntityType].
     *
     * @return true if it can drop, false otherwise
     */
    fun canDropFrom(entityType: EntityType): Boolean

    @Deprecated(
        "Use getPresentableItemGroupType instead.",
        ReplaceWith("getPresentableItemGroupType(allOfLore, anyOfLore, noneOfLore)")
    )
    fun getPresentableType(
        allOfLore: List<String>,
        anyOfLore: List<String>,
        noneOfLore: List<String>
    ): List<String>

    fun getPresentableItemGroupType(
        allOfLore: List<String>,
        anyOfLore: List<String>,
        noneOfLore: List<String>
    ): List<String>
}
