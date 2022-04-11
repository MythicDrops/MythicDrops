/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2022 Richard Harrah
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
package dev.mythicdrops.spigot.configuration

import org.bukkit.configuration.Configuration
import java.io.File

/**
 * Denotes that implementation instances are representative of a file.
 */
interface FileAware {
    /**
     * File that this can [load] from or [save] to.
     */
    val file: File?

    /**
     * The name of the [file]. Returns `""` if [file] is `null`.
     */
    val fileName: String
        get() = file?.name ?: ""

    /**
     * Loads this [Configuration] from [file]. Swallows any exceptions thrown.
     */
    fun load()

    /**
     * Saves this [Configuration] to [file]. Swallows any exceptions thrown.
     */
    fun save()
}
