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

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

/**
 * Modified version of SemVerTest by swiftzer.
 *
 * Original: https://github.com/swiftzer/semver/blob/master/src/test/java/net/swiftzer/semver/SemVerTest.kt
 */
internal class SemVerTest {
    @Test
    fun initValid() {
        SemVer(12, 23, 34, "alpha.12", "test.34")
    }

    @Test
    fun initInvalidMajor() {
        assertThatThrownBy {
            SemVer(
                -1,
                23,
                34,
                "alpha.12",
                "test.34"
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun initInvalidMinor() {
        assertThatThrownBy {
            SemVer(
                12,
                -1,
                34,
                "alpha.12",
                "test.34"
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun initInvalidPatch() {
        assertThatThrownBy {
            SemVer(
                12,
                23,
                -1,
                "alpha.12",
                "test.34"
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun initInvalidPreRelease() {
        assertThatThrownBy {
            SemVer(
                12,
                23,
                34,
                "alpha.12#",
                "test.34"
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun initInvalidMetadata() {
        assertThatThrownBy {
            SemVer(
                12,
                23,
                34,
                "alpha.12",
                "test.34#"
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun parseNumeric() {
        val actual = SemVer.parse("1.0.45")
        val expected = SemVer(1, 0, 45)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun parseIncompleteNumeric1() {
        val actual = SemVer.parse("432")
        val expected = SemVer(432)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun parseIncompleteNumeric2() {
        val actual = SemVer.parse("53.203")
        val expected = SemVer(53, 203)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun parseIncompleteNumeric3() {
        val actual = SemVer.parse("2..235")
        val expected = SemVer(2, 0, 235)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun parsePreRelease() {
        val actual = SemVer.parse("1.0.0-alpha.beta-a.12")
        val expected = SemVer(1, 0, 0, preRelease = "alpha.beta-a.12")
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun parseIncompletePreRelease() {
        val actual = SemVer.parse("34..430-alpha.beta.gamma-a.12")
        val expected = SemVer(34, 0, 430, preRelease = "alpha.beta.gamma-a.12")
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun parseMetadata() {
        val actual = SemVer.parse("1.0.0+exp.sha-part.5114f85")
        val expected = SemVer(1, 0, 0, buildMetadata = "exp.sha-part.5114f85")
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun parseIncompleteMetadata() {
        val actual = SemVer.parse("88.30+exp.sha-part.5114f85")
        val expected = SemVer(88, 30, 0, buildMetadata = "exp.sha-part.5114f85")
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun parseAll() {
        val actual = SemVer.parse("1.0.0-beta+exp.sha.5114f85")
        val expected = SemVer(1, 0, 0, preRelease = "beta", buildMetadata = "exp.sha.5114f85")
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun parseIncompleteAll1() {
        val actual = SemVer.parse("..-beta+exp.sha.5114f85")
        val expected = SemVer(0, 0, 0, preRelease = "beta", buildMetadata = "exp.sha.5114f85")
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun parseIncompleteAll2() {
        val actual = SemVer.parse(".34.-beta+exp.sha.5114f85")
        val expected = SemVer(0, 34, 0, preRelease = "beta", buildMetadata = "exp.sha.5114f85")
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun parseInvalid() {
        assertThatThrownBy { SemVer.parse("1.0.1.4-beta+exp.sha.5114f85") }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun isInitialDevelopmentPhaseTrue() {
        assertThat(SemVer(0, 23, 34, "alpha.123", "testing.123").isInitialDevelopmentPhase()).isTrue()
    }

    @Test
    fun isInitialDevelopmentPhaseFalse() {
        assertThat(SemVer(1, 23, 34, "alpha.123", "testing.123").isInitialDevelopmentPhase()).isFalse()
    }

    @Test
    fun toStringNumeric() {
        val semVer = SemVer(1, 0, 45)
        assertThat(semVer.toString()).isEqualTo("1.0.45")
    }

    @Test
    fun toStringPreRelease() {
        val semVer = SemVer(1, 0, 0, preRelease = "alpha.beta-a.12")
        assertThat(semVer.toString()).isEqualTo("1.0.0-alpha.beta-a.12")
    }

    @Test
    fun toStringMetadata() {
        val semVer = SemVer(1, 0, 0, buildMetadata = "exp.sha-part.5114f85")
        assertThat(semVer.toString()).isEqualTo("1.0.0+exp.sha-part.5114f85")
    }

    @Test
    fun toStringAll() {
        val semVer = SemVer(1, 0, 0, preRelease = "beta", buildMetadata = "exp.sha.5114f85")
        assertThat(semVer.toString()).isEqualTo("1.0.0-beta+exp.sha.5114f85")
    }

    @Test
    fun compareToNumeric1() {
        val semVer1 = SemVer(1, 0, 0)
        val semVer2 = SemVer(1, 0, 0)
        assertThat(semVer1.compareTo(semVer2)).isEqualTo(0)
    }

    @Test
    fun compareToNumeric2() {
        val semVer1 = SemVer(1, 0, 0)
        val semVer2 = SemVer(2, 0, 0)
        assertThat(semVer1.compareTo(semVer2)).isEqualTo(-1)
    }

    @Test
    fun compareToNumeric3() {
        val semVer1 = SemVer(2, 0, 0)
        val semVer2 = SemVer(2, 1, 0)
        assertThat(semVer1.compareTo(semVer2)).isEqualTo(-1)
    }

    @Test
    fun compareToNumeric4() {
        val semVer1 = SemVer(2, 1, 4)
        val semVer2 = SemVer(2, 1, 0)
        assertThat(semVer1.compareTo(semVer2)).isEqualTo(1)
    }

    @Test
    fun compareToNumeric5() {
        val semVer1 = SemVer(2, 0, 0)
        val semVer2 = SemVer(1, 0, 0)
        assertThat(semVer1.compareTo(semVer2)).isEqualTo(1)
    }

    @Test
    fun compareToNumeric6() {
        val semVer1 = SemVer(1, 2, 0)
        val semVer2 = SemVer(1, 0, 0)
        assertThat(semVer1.compareTo(semVer2)).isEqualTo(1)
    }

    @Test
    fun compareToNumeric7() {
        val semVer1 = SemVer(1, 0, 0)
        val semVer2 = SemVer(1, 0, 2)
        assertThat(semVer1.compareTo(semVer2)).isEqualTo(-1)
    }

    @Test
    fun compareToPreRelease1() {
        val semVer1 = SemVer(1, 0, 0)
        val semVer2 = SemVer(1, 0, 0, preRelease = "alpha")
        assertThat(semVer1.compareTo(semVer2)).isEqualTo(1)
    }

    @Test
    fun compareToPreRelease2() {
        val semVer1 = SemVer(1, 0, 0, preRelease = "alpha")
        val semVer2 = SemVer(1, 0, 0)
        assertThat(semVer1.compareTo(semVer2)).isEqualTo(-1)
    }

    @Test
    fun compareToPreRelease3() {
        val semVer1 = SemVer(1, 0, 0, preRelease = "alpha")
        val semVer2 = SemVer(1, 0, 0, preRelease = "alpha.1")
        assertThat(semVer1.compareTo(semVer2)).isEqualTo(-1)
    }

    @Test
    fun compareToPreRelease4() {
        val semVer1 = SemVer(1, 0, 0, preRelease = "alpha.1")
        val semVer2 = SemVer(1, 0, 0, preRelease = "alpha")
        assertThat(semVer1.compareTo(semVer2)).isEqualTo(1)
    }

    @Test
    fun compareToPreRelease5() {
        val semVer1 = SemVer(1, 0, 0, preRelease = "alpha.1")
        val semVer2 = SemVer(1, 0, 0, preRelease = "alpha.beta")
        assertThat(semVer1.compareTo(semVer2)).isEqualTo(-1)
    }

    @Test
    fun compareToPreRelease6() {
        val semVer1 = SemVer(1, 0, 0, preRelease = "alpha.beta")
        val semVer2 = SemVer(1, 0, 0, preRelease = "alpha.1")
        assertThat(semVer1.compareTo(semVer2)).isEqualTo(1)
    }

    @Test
    fun compareToPreRelease7() {
        val semVer1 = SemVer(1, 0, 0, preRelease = "alpha.1")
        val semVer2 = SemVer(1, 0, 0, preRelease = "beta")
        assertThat(semVer1.compareTo(semVer2)).isEqualTo(-1)
    }

    @Test
    fun compareToPreRelease8() {
        val semVer1 = SemVer(1, 0, 0, preRelease = "beta")
        val semVer2 = SemVer(1, 0, 0, preRelease = "alpha.1")
        assertThat(semVer1.compareTo(semVer2)).isEqualTo(1)
    }

    @Test
    fun compareToPreRelease9() {
        val semVer1 = SemVer(1, 0, 0, preRelease = "alpha.1")
        val semVer2 = SemVer(1, 0, 0, preRelease = "alpha.2")
        assertThat(semVer1.compareTo(semVer2)).isEqualTo(-1)
    }

    @Test
    fun compareToPreRelease10() {
        val semVer1 = SemVer(1, 0, 0, preRelease = "alpha.2")
        val semVer2 = SemVer(1, 0, 0, preRelease = "alpha.1")
        assertThat(semVer1.compareTo(semVer2)).isEqualTo(1)
    }

    @Test
    fun compareToPreRelease11() {
        val semVer1 = SemVer(1, 0, 0, preRelease = "alpha.1")
        val semVer2 = SemVer(1, 0, 0, preRelease = "alpha.1")
        assertThat(semVer1.compareTo(semVer2)).isEqualTo(0)
    }

    @Test
    fun compareToPreRelease12() {
        val semVer1 = SemVer(1, 0, 0, preRelease = "alpha")
        val semVer2 = SemVer(1, 0, 0, preRelease = "alpha")
        assertThat(semVer1.compareTo(semVer2)).isEqualTo(0)
    }

    @Test
    fun compareToPreReleaseMetadata() {
        val semVer1 = SemVer(1, 0, 0, preRelease = "alpha", buildMetadata = "xyz")
        val semVer2 = SemVer(1, 0, 0, preRelease = "alpha", buildMetadata = "abc")
        assertThat(semVer1.compareTo(semVer2)).isEqualTo(0)
    }
}
