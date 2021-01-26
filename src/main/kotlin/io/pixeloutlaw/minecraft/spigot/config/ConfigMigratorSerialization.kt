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

import io.pixeloutlaw.minecraft.spigot.config.migration.models.ConfigMigration
import io.pixeloutlaw.minecraft.spigot.config.migration.models.ConfigMigrationStep
import io.pixeloutlaw.minecraft.spigot.config.migration.models.NamedConfigMigration
import org.bukkit.configuration.serialization.ConfigurationSerialization

/**
 * Registers configuration serializable classes.
 */
internal object ConfigMigratorSerialization {
    fun registerAll() {
        ConfigMigrationStep.defaultSteps.forEach {
            ConfigurationSerialization.registerClass(it.java)
        }
        ConfigurationSerialization.registerClass(SemVer::class.java)
        ConfigurationSerialization.registerClass(ConfigMigration::class.java)
        ConfigurationSerialization.registerClass(NamedConfigMigration::class.java)
    }

    fun unregisterAll() {
        ConfigurationSerialization.unregisterClass(NamedConfigMigration::class.java)
        ConfigurationSerialization.unregisterClass(ConfigMigration::class.java)
        ConfigurationSerialization.unregisterClass(SemVer::class.java)
        ConfigMigrationStep.defaultSteps.forEach {
            ConfigurationSerialization.unregisterClass(it.java)
        }
    }
}
