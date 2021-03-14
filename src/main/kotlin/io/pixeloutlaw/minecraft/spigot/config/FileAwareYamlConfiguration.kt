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
package io.pixeloutlaw.minecraft.spigot.config

import io.pixeloutlaw.kindling.Log
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/**
 * An instance of [YamlConfiguration] that is also a [FileAwareConfiguration].
 */
open class FileAwareYamlConfiguration @JvmOverloads constructor(override var file: File? = null) :
    YamlConfiguration(), FileAwareConfiguration {

    init {
        load()
    }

    /**
     * Loads from the file passed into the constructor. Ignores any exceptions thrown.
     *
     * Equivalent of calling [load] and passing in [file].
     */
    final override fun load() {
        val fileToLoad = file
        if (fileToLoad == null) {
            Log.warn("Cannot load from a null file")
            return
        }
        try {
            load(fileToLoad)
        } catch (ignored: Throwable) {
            Log.error("Unable to load FileAwareYamlConfiguration", ignored)
        }
    }

    /**
     * Saves to the file passed into the constructor. Ignores any exceptions thrown.
     *
     * Equivalent of calling [save] and passing in [file].
     */
    final override fun save() {
        val fileToSave = file
        if (fileToSave == null) {
            Log.warn("Cannot save to a null file")
            return
        }
        try {
            save(fileToSave)
        } catch (ignored: Throwable) {
            Log.error("Unable to save FileAwareYamlConfiguration", ignored)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ConfigurationSection) return false

        val ourKeys = getKeys(true)
        val theirKeys = other.getKeys(true)
        if (theirKeys != ourKeys) return false

        for (key in ourKeys) {
            val ourValue = get(key)
            val theirValue = other.get(key)
            if (ourValue != theirValue) {
                return false
            }
        }

        return true
    }

    override fun toString(): String {
        val keysAndValues = getKeys(true).mapNotNull {
            if (isConfigurationSection(it)) {
                return@mapNotNull null
            }
            it to get(it)
        }.toMap()
        return "FileAwareYamlConfiguration($keysAndValues)"
    }

    override fun hashCode(): Int {
        return file?.hashCode() ?: 0
    }
}
