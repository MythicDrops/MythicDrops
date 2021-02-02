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
package com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories

import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

/**
 * Used for building unidentified items and identity tomes.
 *
 * @since 7.0.0
 */
interface IdentificationItemFactory {
    /**
     * Builds an Identity Tome.
     */
    fun buildIdentityTome(): ItemStack

    /**
     * Builds an unidentified item for a tier by a given name. Returns null if it cannot find a tier by the given name
     * or if it cannot build an item for that tier.
     */
    fun buildUnidentifiedItem(tierName: String): ItemStack?

    /**
     * Builds an unidentified item for a given tier. Returns null if it cannot build an item for that tier.
     */
    fun buildUnidentifiedItem(tier: Tier): ItemStack?

    /**
     * Builds an unidentified item for a given entity type. Returns null if it cannot build an item for that entity
     * type.
     */
    fun buildUnidentifiedItem(entityType: EntityType): ItemStack?

    /**
     * Builds an unidentified item of the given material.
     */
    fun buildUnidentifiedItem(material: Material): ItemStack

    /**
     * Builds an unidentified item of the given material that was dropped by the given type of entity.
     */
    fun buildUnidentifiedItem(material: Material, entityType: EntityType?): ItemStack

    /**
     * Builds an unidentified item of the given material for a given tier that was dropped by the given type of entity.
     */
    fun buildUnidentifiedItem(material: Material, entityType: EntityType?, tier: Tier?): ItemStack

    /**
     * Builds an unidentified item of the given material for a given tier that was dropped by the given type of entity.
     * Tier must be in the given list of allowable tiers.
     */
    fun buildUnidentifiedItem(
        material: Material,
        entityType: EntityType?,
        tier: Tier?,
        allowableTiers: List<Tier>?
    ): ItemStack
}
