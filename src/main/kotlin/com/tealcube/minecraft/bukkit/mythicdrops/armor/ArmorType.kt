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
package com.tealcube.minecraft.bukkit.mythicdrops.armor

import org.bukkit.Material
import java.util.Locale

const val HELMET_SLOT = 5
const val CHESTPLATE_SLOT = 6
const val LEGGINGS_SLOT = 7
const val BOOTS_SLOT = 8

/**
 * Modified version of ArmorType from ArmorEquipEvent.
 *
 * https://github.com/Arnuh/ArmorEquipEvent
 */
internal enum class ArmorType(val slot: Int) {
    HELMET(HELMET_SLOT),
    CHESTPLATE(CHESTPLATE_SLOT),
    LEGGINGS(LEGGINGS_SLOT),
    BOOTS(BOOTS_SLOT);

    companion object {
        /**
         * Determines the [ArmorType] from the given [Material].
         */
        fun from(material: Material?): ArmorType? {
            val materialName = material?.name?.uppercase(Locale.getDefault()) ?: return null
            return when {
                materialName.endsWith("_HELMET") -> HELMET
                materialName.endsWith("_CHESTPLATE") || material.name.endsWith("ELYTRA") -> CHESTPLATE
                materialName.endsWith("_LEGGINGS") -> LEGGINGS
                materialName.endsWith("_BOOTS") -> BOOTS
                else -> null
            }
        }
    }
}
