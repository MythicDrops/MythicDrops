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
package io.pixeloutlaw.minecraft.spigot.config.migration.models.pre

import io.pixeloutlaw.minecraft.spigot.config.migration.models.NamedConfigMigration

/**
 * Simple pair of a name for a [PreConfigMigration].
 */
data class NamedPreConfigMigration(
    override val migrationName: String,
    override val configMigration: PreConfigMigration
) : NamedConfigMigration {
    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): NamedPreConfigMigration {
            val migrationName = map.getOrDefault("migrationName", "").toString()
            val preConfigMigration =
                map.getOrDefault("configMigration", PreConfigMigration.NO_OP) as? PreConfigMigration
            return NamedPreConfigMigration(migrationName, preConfigMigration ?: PreConfigMigration.NO_OP)
        }
    }

    override fun serialize(): MutableMap<String, Any> =
        mutableMapOf("migrationName" to migrationName, "configMigration" to configMigration)
}
