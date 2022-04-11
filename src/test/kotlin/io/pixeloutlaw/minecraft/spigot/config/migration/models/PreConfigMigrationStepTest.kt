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
package io.pixeloutlaw.minecraft.spigot.config.migration.models

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.prop
import io.pixeloutlaw.minecraft.spigot.config.migration.steps.pre.PreConfigMigrationStep
import io.pixeloutlaw.minecraft.spigot.config.migration.steps.pre.SetBooleanPreConfigMigrationStep
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

internal class PreConfigMigrationStepTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun setupAll() {
            ConfigurationSerialization.registerClass(PreConfigMigrationStep::class.java)
            ConfigurationSerialization.registerClass(SetBooleanPreConfigMigrationStep::class.java)
        }

        @AfterAll
        @JvmStatic
        fun teardownAll() {
            ConfigurationSerialization.unregisterClass(PreConfigMigrationStep::class.java)
            ConfigurationSerialization.unregisterClass(SetBooleanPreConfigMigrationStep::class.java)
        }
    }

    @Test
    fun `do PreConfigMigrationStep implementations serialize`() {
        // given
        val toSerialize = SetBooleanPreConfigMigrationStep("test", false)
        val yamlConfiguration = YamlConfiguration()

        // when
        yamlConfiguration["test"] = toSerialize
        val serialized = yamlConfiguration.saveToString()

        // then
        assertThat(serialized).contains("==: ${SetBooleanPreConfigMigrationStep::class.java.name}")
    }

    @Test
    fun `do PreConfigMigrationStep implementations deserialize`() {
        // given
        val toDeserialize =
            PreConfigMigrationStepTest::class.java.classLoader.getResource("config_migration_steps/set_boolean.yml")
                ?.readText() ?: ""
        val yamlConfiguration = YamlConfiguration()

        // when
        yamlConfiguration.loadFromString(toDeserialize)

        // then
        assertThat(
            yamlConfiguration.getSerializable(
                "test",
                SetBooleanPreConfigMigrationStep::class.java
            )
        ).isNotNull().isInstanceOf(SetBooleanPreConfigMigrationStep::class)
        assertThat(
            yamlConfiguration.getSerializable(
                "test",
                SetBooleanPreConfigMigrationStep::class.java
            )
        ).isNotNull().prop("key") { it.key }.isNotNull().isEqualTo("test")
    }
}
