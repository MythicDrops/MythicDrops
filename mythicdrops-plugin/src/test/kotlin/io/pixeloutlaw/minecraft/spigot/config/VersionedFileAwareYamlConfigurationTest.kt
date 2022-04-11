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

import dev.mythicdrops.spigot.semver.SemVer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

internal class VersionedFileAwareYamlConfigurationTest {
    @Test
    fun `does getting version return default value when no version field exists`() {
        // given
        val configuration = VersionedFileAwareYamlConfiguration()

        // when
        configuration.load()

        // then
        assertThat(configuration.version).isEqualTo(SemVer(0, 0, 0))
    }

    @Test
    fun `does getting version read from file`(@TempDir tempDir: Path) {
        // given
        val tempFile = tempDir.resolve("test.yml")
        val configuration = VersionedFileAwareYamlConfiguration(tempFile.toFile())

        // when
        tempFile.toFile().writeText("version: 1")
        configuration.load()

        // then
        assertThat(configuration.version).isEqualTo(SemVer(1, 0, 0))
    }

    @Test
    fun `does setting version set the version field in the Configuration map`() {
        // given
        val configuration = VersionedFileAwareYamlConfiguration()

        // when
        configuration.version = SemVer(1, 0, 0)

        // then
        assertThat(configuration["version"]).isEqualTo("1.0.0")
    }
}
