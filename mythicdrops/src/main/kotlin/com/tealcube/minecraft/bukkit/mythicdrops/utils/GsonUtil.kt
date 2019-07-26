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

import com.google.gson.GsonBuilder
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment
import com.tealcube.minecraft.bukkit.mythicdrops.gson.EnchantmentSerializer
import com.tealcube.minecraft.bukkit.mythicdrops.gson.MythicEnchantmentSerializer
import com.tealcube.minecraft.bukkit.mythicdrops.gson.NamespacedKeySerializer
import com.tealcube.minecraft.bukkit.mythicdrops.gson.SerializationExclusionStrategy
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment

object GsonUtil {
    private val gson = GsonBuilder()
        .addSerializationExclusionStrategy(SerializationExclusionStrategy())
        .registerTypeAdapter(Enchantment::class.java, EnchantmentSerializer())
        .registerTypeAdapter(MythicEnchantment::class.java, MythicEnchantmentSerializer())
        .registerTypeAdapter(NamespacedKey::class.java, NamespacedKeySerializer())
        .create()

    fun toJson(o: Any): String {
        return gson.toJson(o)
    }

    fun <T> fromJson(json: String, clazz: Class<T>): T {
        return gson.fromJson(json, clazz)
    }
}
