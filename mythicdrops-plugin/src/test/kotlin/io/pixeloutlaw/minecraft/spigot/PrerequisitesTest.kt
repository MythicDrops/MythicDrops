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
package io.pixeloutlaw.minecraft.spigot

import io.pixeloutlaw.minecraft.spigot.mythicdrops.Prerequisites
import io.pixeloutlaw.minecraft.spigot.mythicdrops.prerequisites
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PrerequisitesTest {
    @Test
    fun `does Prerequisites#prerequisite add a function to the managed list`() {
        // given
        val prerequisites = Prerequisites()

        // when
        prerequisites.prerequisite { true }

        // then
        assertThat(prerequisites.prereqs).hasSize(1)
    }

    @Test
    fun `does Prerequisites#invoke return true if all functions returns true`() {
        // given
        val prerequisites = Prerequisites()

        // when
        prerequisites.prerequisite { true }
        prerequisites.prerequisite { true }

        // then
        assertThat(prerequisites.invoke()).isTrue()
    }

    @Test
    fun `does Prerequisites#invoke return false if one function returns false`() {
        // given
        val prerequisites = Prerequisites()

        // when
        prerequisites.prerequisite { true }
        prerequisites.prerequisite { false }

        // then
        assertThat(prerequisites.invoke()).isFalse()
    }

    @Test
    fun `does prerequisites return true if all functions return true`() {
        // given

        // when
        val result = prerequisites {
            prerequisite { true }
            prerequisite { true }
        }

        // then
        assertThat(result).isTrue()
    }

    @Test
    fun `does prerequisites return false if one function returns false`() {
        // given

        // when
        val result = prerequisites {
            prerequisite { true }
            prerequisite { false }
        }

        // then
        assertThat(result).isFalse()
    }
}
