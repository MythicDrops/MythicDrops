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

import com.tealcube.minecraft.bukkit.mythicdrops.trimEmpty
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

/**
 * Gets keys from the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
internal fun ItemStack.getPersistentDataKeys(namespace: String): List<NamespacedKey> = getFromItemMeta {
    persistentDataContainer.keys.filter { it.namespace.equals(namespace, ignoreCase = true) }
} ?: emptyList()

internal fun ItemStack.removePersistentData(namespacedKey: NamespacedKey) = getThenSetItemMeta {
    persistentDataContainer.remove(namespacedKey)
}

internal fun ItemStack.hasPersistentDataString(namespacedKey: NamespacedKey): Boolean = getFromItemMeta {
    persistentDataContainer.has(namespacedKey, PersistentDataType.STRING)
} ?: false

/**
 * Gets a nullable string from the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
internal fun ItemStack.getPersistentDataString(namespacedKey: NamespacedKey): String? = getFromItemMeta {
    if (persistentDataContainer.has(namespacedKey, PersistentDataType.STRING)) {
        persistentDataContainer.get(namespacedKey, PersistentDataType.STRING)
    } else {
        null
    }
}

/**
 * Sets a nullable string in the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
internal fun ItemStack.setPersistentDataString(namespacedKey: NamespacedKey, value: String) = getThenSetItemMeta {
    persistentDataContainer.set(namespacedKey, PersistentDataType.STRING, value)
}

internal fun ItemStack.hasPersistentDataBoolean(namespacedKey: NamespacedKey): Boolean =
    hasPersistentDataString(namespacedKey)

/**
 * Gets a nullable boolean from the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
internal fun ItemStack.getPersistentDataBoolean(namespacedKey: NamespacedKey): Boolean? =
    getPersistentDataString(namespacedKey)?.toBoolean()

/**
 * Sets a nullable boolean in the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
internal fun ItemStack.setPersistentDataBoolean(namespacedKey: NamespacedKey, value: Boolean) =
    setPersistentDataString(namespacedKey, value.toString())

internal fun ItemStack.hasPersistentDataInt(namespacedKey: NamespacedKey): Boolean = getFromItemMeta {
    persistentDataContainer.has(namespacedKey, PersistentDataType.INTEGER)
} ?: false

/**
 * Gets a nullable string from the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
internal fun ItemStack.getPersistentDataInt(namespacedKey: NamespacedKey): Int? = getFromItemMeta {
    if (persistentDataContainer.has(namespacedKey, PersistentDataType.INTEGER)) {
        persistentDataContainer.get(namespacedKey, PersistentDataType.INTEGER)
    } else {
        null
    }
}

/**
 * Sets a nullable string in the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
internal fun ItemStack.setPersistentDataInt(namespacedKey: NamespacedKey, value: Int) = getThenSetItemMeta {
    persistentDataContainer.set(namespacedKey, PersistentDataType.INTEGER, value)
}

internal fun ItemStack.hasPersistentDataStringList(namespacedKey: NamespacedKey): Boolean =
    hasPersistentDataString(namespacedKey)

/**
 * Gets a nullable string from the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
internal fun ItemStack.getPersistentDataStringList(namespacedKey: NamespacedKey): List<String>? = getFromItemMeta {
    if (persistentDataContainer.has(namespacedKey, PersistentDataType.STRING)) {
        persistentDataContainer.get(namespacedKey, PersistentDataType.STRING)?.split(",")?.trimEmpty()
    } else {
        null
    }
}

/**
 * Sets a nullable string in the persistent data container on the [ItemStack] if on 1.16+. Does nothing otherwise.
 */
internal fun ItemStack.setPersistentDataStringList(namespacedKey: NamespacedKey, value: List<String>) =
    getThenSetItemMeta {
        persistentDataContainer.set(namespacedKey, PersistentDataType.STRING, value.joinToString(separator = ","))
    }

