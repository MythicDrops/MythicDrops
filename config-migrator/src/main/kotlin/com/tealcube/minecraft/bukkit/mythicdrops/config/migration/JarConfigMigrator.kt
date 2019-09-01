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
package com.tealcube.minecraft.bukkit.mythicdrops.config.migration

import com.squareup.moshi.Moshi
import java.io.File
import java.util.logging.Logger
import java.util.zip.ZipFile

class JarConfigMigrator @JvmOverloads constructor(
    private val jarFile: File,
    dataFolder: File,
    moshi: Moshi = defaultMoshi
) : ConfigMigrator(dataFolder, emptyList(), moshi) {
    companion object {
        private const val configMigrationPatternString = "config/migration/V\\d+__.+\\.migration\\.json"
        private val configMigrationRegex = configMigrationPatternString.toRegex()
        private val logger = Logger.getLogger(JarConfigMigrator::class.java.canonicalName)
    }

    private val cachedConfigMigrationResources: List<String> by lazy {
        val zipFile = try {
            ZipFile(jarFile)
        } catch (ex: Exception) {
            return@lazy emptyList<String>()
        }
        val retValue = mutableListOf<String>()
        zipFile.use {
            val entries = zipFile.entries()
            while (entries.hasMoreElements()) {
                val zipEntry = entries.nextElement()
                val zipEntryName = zipEntry.name
                if (configMigrationRegex.matches(zipEntryName)) {
                    retValue.add(zipEntryName)
                }
            }
        }
        logger.fine("Migrations: $retValue")
        retValue.toList()
    }

    override fun getConfigMigrationResources(): List<String> = cachedConfigMigrationResources
}
