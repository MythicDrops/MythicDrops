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

import org.bukkit.configuration.serialization.ConfigurationSerializable

/**
 * Version number in [Semantic Versioning 2.0.0](http://semver.org/spec/v2.0.0.html) specification (SemVer).
 *
 * Modified version of the SemVer class from the SemVer library by swiftzer.
 *
 * Original: https://github.com/swiftzer/semver/blob/master/src/main/java/net/swiftzer/semver/SemVer.kt
 *
 * @property major major version, increment it when you make incompatible API changes.
 * @property minor minor version, increment it when you add functionality in a backwards-compatible manner.
 * @property patch patch version, increment it when you make backwards-compatible bug fixes.
 * @property preRelease pre-release version.
 * @property buildMetadata build metadata.
 */

data class SemVer(
    val major: Int = 0,
    val minor: Int = 0,
    val patch: Int = 0,
    val preRelease: String? = null,
    val buildMetadata: String? = null
) : Comparable<SemVer>, ConfigurationSerializable {
    companion object {
        private const val MAJOR_INDEX = 1
        private const val MINOR_INDEX = 2
        private const val PATCH_INDEX = 3
        private const val PRE_RELEASE_INDEX = 4
        private const val BUILD_METADATA_INDEX = 5
        private val buildMetadataRegex = """[\dA-z\-]+(?:\.[\dA-z\-]+)*""".toRegex()
        private val preReleaseRegex = """[\dA-z\-]+(?:\.[\dA-z\-]+)*""".toRegex()
        private val numberRegex = """\d+""".toRegex()

        @Suppress("detekt.MaxLineLength", "ktlint:standard:max-line-length")
        private val semanticVersionRegex =
            """(0|[1-9]\d*)?(?:\.)?(0|[1-9]\d*)?(?:\.)?(0|[1-9]\d*)?(?:-([\dA-z\-]+(?:\.[\dA-z\-]+)*))?(?:\+([\dA-z\-]+(?:\.[\dA-z\-]+)*))?""".toRegex()

        @JvmStatic
        fun deserialize(map: Map<String, Any>): SemVer {
            val major = map.getOrDefault("major", 0) as? Int ?: 0
            val minor = map.getOrDefault("minor", 0) as? Int ?: 0
            val patch = map.getOrDefault("patch", 0) as? Int ?: 0
            val preRelease = map["preRelease"]?.toString()
            val buildMetadata = map["buildMetadata"]?.toString()
            return SemVer(major, minor, patch, preRelease, buildMetadata)
        }

        /**
         * Parse the version string to [SemVer] data object.
         * @param version version string.
         * @throws IllegalArgumentException if the version is not valid.
         */
        @JvmStatic
        fun parse(version: String): SemVer {
            val result = semanticVersionRegex.matchEntire(version)
            requireNotNull(result) { "version is not a valid semantic version: $version" }
            return SemVer(
                major = getIntFromResult(result.groupValues, MAJOR_INDEX),
                minor = getIntFromResult(result.groupValues, MINOR_INDEX),
                patch = getIntFromResult(result.groupValues, PATCH_INDEX),
                preRelease = getStringFromResult(result.groupValues, PRE_RELEASE_INDEX),
                buildMetadata = getStringFromResult(result.groupValues, BUILD_METADATA_INDEX)
            )
        }

        @JvmStatic
        fun parseOrDefault(version: String, def: SemVer): SemVer {
            val result = semanticVersionRegex.matchEntire(version) ?: return def
            return SemVer(
                major = getIntFromResult(result.groupValues, MAJOR_INDEX),
                minor = getIntFromResult(result.groupValues, MINOR_INDEX),
                patch = getIntFromResult(result.groupValues, PATCH_INDEX),
                preRelease = getStringFromResult(result.groupValues, PRE_RELEASE_INDEX),
                buildMetadata = getStringFromResult(result.groupValues, BUILD_METADATA_INDEX)
            )
        }

        private fun getIntFromResult(list: List<String>, idx: Int, def: Int = 0): Int {
            return if (list[idx].isEmpty()) def else list[idx].toInt()
        }

        private fun getStringFromResult(list: List<String>, idx: Int, def: String? = null): String? {
            return if (list[idx].isEmpty()) def else list[idx]
        }
    }

    init {
        require(major >= 0) { "Major version must be a positive integer" }
        require(minor >= 0) { "Minor version must be a positive integer" }
        require(patch >= 0) { "Patch version must be a positive integer" }
        preRelease?.let {
            require(it.matches(preReleaseRegex)) { "Pre-release version is not valid" }
        }
        buildMetadata?.let {
            require(it.matches(buildMetadataRegex)) { "Build metadata is not valid" }
        }
    }

    /**
     * Check the version number is in initial development.
     * @return true if it is in initial development.
     */
    fun isInitialDevelopmentPhase(): Boolean = major == 0

    override fun serialize(): MutableMap<String, Any> {
        val map = mutableMapOf<String, Any>(
            "major" to major,
            "minor" to minor,
            "patch" to patch
        )
        preRelease?.let { map["preRelease"] = it }
        buildMetadata?.let { map["buildMetadata"] = it }
        return map
    }

    override fun toString(): String = buildString {
        append("$major.$minor.$patch")
        preRelease?.let { append("-$it") }
        buildMetadata?.let { append("+$it") }
    }

    /**
     * Compare two SemVer objects using major, minor, patch and pre-release version as specified in SemVer
     * specification.
     *
     * For comparing the whole SemVer object including build metadata, use [equals] instead.
     *
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater
     * than the specified object.
     */
    @Suppress("detekt.ComplexMethod", "detekt.ReturnCount")
    override fun compareTo(other: SemVer): Int {
        if (major > other.major) return 1
        if (major < other.major) return -1
        if (minor > other.minor) return 1
        if (minor < other.minor) return -1
        if (patch > other.patch) return 1
        if (patch < other.patch) return -1

        if (preRelease == null && other.preRelease == null) return 0
        if (preRelease != null && other.preRelease == null) return -1
        if (preRelease == null && other.preRelease != null) return 1

        val parts = preRelease.orEmpty().split(".")
        val otherParts = other.preRelease.orEmpty().split(".")

        val endIndex = Math.min(parts.size, otherParts.size) - 1
        for (i in 0..endIndex) {
            val part = parts[i]
            val otherPart = otherParts[i]
            if (part == otherPart) continue

            val partIsNumeric = part.isNumeric()
            val otherPartIsNumeric = otherPart.isNumeric()

            when {
                partIsNumeric && !otherPartIsNumeric -> {
                    // lower priority
                    return -1
                }

                !partIsNumeric && otherPartIsNumeric -> {
                    // higher priority
                    return 1
                }

                !partIsNumeric && !otherPartIsNumeric -> {
                    if (part > otherPart) return 1
                    if (part < otherPart) return -1
                }

                else -> {
                    val partInt = part.toInt()
                    val otherPartInt = otherPart.toInt()
                    if (partInt > otherPartInt) return 1
                    if (partInt < otherPartInt) return -1
                }
            }
        }

        return if (parts.size == endIndex + 1 && otherParts.size > endIndex + 1) {
            // parts is ended and otherParts is not ended
            -1
        } else if (parts.size > endIndex + 1 && otherParts.size == endIndex + 1) {
            // parts is not ended and otherParts is ended
            1
        } else {
            0
        }
    }

    private fun String.isNumeric(): Boolean = this.matches(numberRegex)
}
