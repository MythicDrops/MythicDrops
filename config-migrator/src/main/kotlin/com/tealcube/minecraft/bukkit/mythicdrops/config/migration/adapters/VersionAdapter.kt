package com.tealcube.minecraft.bukkit.mythicdrops.config.migration.adapters

import com.github.zafarkhaja.semver.Version
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

/**
 * Moshi adapter for the [Version] class.
 */
object VersionAdapter {
    @ToJson
    fun toJson(version: Version): String = version.toString()

    @FromJson
    fun fromJson(version: String): Version = Version.valueOf(version)
}
