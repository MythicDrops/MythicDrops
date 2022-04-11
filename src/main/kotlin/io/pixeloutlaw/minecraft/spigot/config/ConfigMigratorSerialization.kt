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

import io.pixeloutlaw.minecraft.spigot.config.migration.models.post.NamedPostConfigMigration
import io.pixeloutlaw.minecraft.spigot.config.migration.models.post.PostConfigMigration
import io.pixeloutlaw.minecraft.spigot.config.migration.models.pre.NamedPreConfigMigration
import io.pixeloutlaw.minecraft.spigot.config.migration.models.pre.PreConfigMigration
import io.pixeloutlaw.minecraft.spigot.config.migration.steps.post.PostConfigMigrationStep
import io.pixeloutlaw.minecraft.spigot.config.migration.steps.pre.PreConfigMigrationStep
import org.bukkit.configuration.serialization.ConfigurationSerialization

/**
 * Registers configuration serializable classes.
 */
internal object ConfigMigratorSerialization {
    fun registerAll() {
        PreConfigMigrationStep.defaultSteps.forEach {
            ConfigurationSerialization.registerClass(it.java)
        }
        PostConfigMigrationStep.defaultSteps.forEach {
            ConfigurationSerialization.registerClass(it.java)
        }
        ConfigurationSerialization.registerClass(SemVer::class.java)
        ConfigurationSerialization.registerClass(PreConfigMigration::class.java)
        ConfigurationSerialization.registerClass(NamedPreConfigMigration::class.java)
        ConfigurationSerialization.registerClass(PostConfigMigration::class.java)
        ConfigurationSerialization.registerClass(NamedPostConfigMigration::class.java)
    }

    fun unregisterAll() {
        ConfigurationSerialization.unregisterClass(NamedPostConfigMigration::class.java)
        ConfigurationSerialization.unregisterClass(PostConfigMigration::class.java)
        ConfigurationSerialization.unregisterClass(NamedPreConfigMigration::class.java)
        ConfigurationSerialization.unregisterClass(PreConfigMigration::class.java)
        ConfigurationSerialization.unregisterClass(SemVer::class.java)
        PostConfigMigrationStep.defaultSteps.forEach {
            ConfigurationSerialization.unregisterClass(it.java)
        }
        PreConfigMigrationStep.defaultSteps.forEach {
            ConfigurationSerialization.unregisterClass(it.java)
        }
    }
}
