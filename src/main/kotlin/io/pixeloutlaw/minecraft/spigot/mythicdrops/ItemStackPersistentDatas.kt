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

import io.pixeloutlaw.minecraft.spigot.plumbing.api.MinecraftVersions
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

/**
 * Gets keys from the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
internal fun ItemStack.getPersistentDataKeys(namespace: String): List<NamespacedKey> {
    return if (MinecraftVersions.isAtLeastMinecraft116) {
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
internal fun ItemStack.getPersistentDataString(namespacedKey: NamespacedKey): String? {
    return if (MinecraftVersions.isAtLeastMinecraft116) {
        getFromItemMeta {
            if (persistentDataContainer.has(namespacedKey, PersistentDataType.STRING)) {
                persistentDataContainer.get(namespacedKey, PersistentDataType.STRING)
            } else {
                null
            }
        }
    } else {
        null
    }
}

/**
 * Gets a nullable boolean from the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
internal fun ItemStack.getPersistentDataBoolean(namespacedKey: NamespacedKey): Boolean? {
    return if (MinecraftVersions.isAtLeastMinecraft116) {
        getPersistentDataString(namespacedKey)?.toBoolean()
    } else {
        null
    }
}

/**
 * Sets a nullable string in the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
internal fun ItemStack.setPersistentDataString(namespacedKey: NamespacedKey, value: String) {
    if (MinecraftVersions.isAtLeastMinecraft116) {
        getThenSetItemMeta {
            // we use the full class instead of the import in order to work better on < 1.16
            persistentDataContainer.set(namespacedKey, PersistentDataType.STRING, value)
        }
    }
}

/**
 * Sets a nullable boolean in the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
internal fun ItemStack.setPersistentDataBoolean(namespacedKey: NamespacedKey, value: Boolean) {
    if (MinecraftVersions.isAtLeastMinecraft116) {
        setPersistentDataString(namespacedKey, value.toString())
    }
}

internal fun ItemStack.removePersistentData(namespacedKey: NamespacedKey) {
    if (MinecraftVersions.isAtLeastMinecraft116) {
        getThenSetItemMeta {
            persistentDataContainer.remove(namespacedKey)
        }
    }
}
