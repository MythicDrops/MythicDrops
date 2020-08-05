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
package com.tealcube.minecraft.bukkit.mythicdrops.api.items

import org.bukkit.Material

/**
 * Represents a group of [Material]s.
 *
 * @property name Name of the group - e.g., Sword, Pickaxe, Gold, Iron
 * @property materials Materials in this group
 * @property isInverse Negative matching of materials
 */
interface ItemGroup {
    val name: String
    val materials: Set<Material>
    val isInverse: Boolean
    val priority: Int

    /**
     * Returns a copy of this [ItemGroup] with the added [Material]s.
     *
     * @param material Material(s) to add
     * @return copy with added Material(s)
     */
    fun addMaterials(vararg material: Material): ItemGroup

    /**
     * Returns a copy of this [ItemGroup] with the removed [Material]s.
     *
     * @param material Material(s) to remove
     * @return copy with removed Material(s)
     */
    fun removeMaterials(vararg material: Material): ItemGroup

    /**
     * Returns a copy of this [ItemGroup] with the inverse flag flipped.
     *
     * @return copy with flipped [isInverse]
     */
    fun inverse(): ItemGroup
}
