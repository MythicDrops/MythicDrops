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

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

internal class FileAwareYamlConfigurationTest {
    @Test
    fun `does load do nothing when no file is given`() {
        // given
        val configuration = FileAwareYamlConfiguration()

        // when
        configuration.load()

        // then
        assertThat(configuration.getKeys(true)).hasSize(0)
    }

    @Test
    fun `does load load contents when file is given`(
        @TempDir tempDir: Path
    ) {
        // given
        val tempFile = tempDir.resolve("test.yml")
        val configuration = FileAwareYamlConfiguration(tempFile.toFile())

        // when
        tempFile.toFile().writeText("test: 1")
        configuration.load()

        // then
        assertThat(configuration.getKeys(true)).hasSize(1)
    }

    @Test
    fun `does save do nothing when no file is given`(
        @TempDir tempDir: Path
    ) {
        // given
        val configuration = FileAwareYamlConfiguration()

        // when
        configuration.save()

        // then
        assertThat(tempDir.toFile().list()).isNotNull().isEmpty()
    }

    @Test
    fun `does save save contents when file is given`(
        @TempDir tempDir: Path
    ) {
        // given
        val tempFile = tempDir.resolve("test.yml")
        val configuration = FileAwareYamlConfiguration(tempFile.toFile())
        configuration.set("test", 1)

        // when
        configuration.save()

        // then
        assertThat(tempDir.toFile().list()).isNotNull().isNotEmpty()
        assertThat(tempFile.toFile().readText()).isEqualTo("test: 1\n")
    }
}
