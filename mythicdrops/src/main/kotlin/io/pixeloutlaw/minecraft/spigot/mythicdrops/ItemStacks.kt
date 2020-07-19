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
package io.pixeloutlaw.minecraft.spigot.mythicdrops

import com.tealcube.minecraft.bukkit.mythicdrops.utils.MinecraftVersionUtil
import io.pixeloutlaw.minecraft.spigot.hilt.getFromItemMeta
import io.pixeloutlaw.minecraft.spigot.hilt.getThenSetItemMeta
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

/**
 * Gets keys from the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
fun ItemStack.getPersistentDataKeys(namespace: String): List<NamespacedKey> {
    return if (MinecraftVersionUtil.isAtLeastMinecraft116()) {
        getFromItemMeta {
            persistentDataContainer.keys.filter { it.namespace.equals(namespace, ignoreCase = true) }
        } ?: emptyList()
    } else {
        emptyList()
    }
}

/**
 * Gets a nullable string from the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
fun ItemStack.getPersistentDataString(namespacedKey: NamespacedKey): String? {
    return if (MinecraftVersionUtil.isAtLeastMinecraft116()) {
        getFromItemMeta { persistentDataContainer.get(namespacedKey, org.bukkit.persistence.PersistentDataType.STRING) }
    } else {
        null
    }
}

/**
 * Sets a nullable string in the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
fun ItemStack.setPersistentDataString(namespacedKey: NamespacedKey, value: String) {
    if (MinecraftVersionUtil.isAtLeastMinecraft116()) {
        getThenSetItemMeta {
            // we use the full class instead of the import in order to work better on < 1.16
            persistentDataContainer.set(namespacedKey, org.bukkit.persistence.PersistentDataType.STRING, value)
        }
    }
}
