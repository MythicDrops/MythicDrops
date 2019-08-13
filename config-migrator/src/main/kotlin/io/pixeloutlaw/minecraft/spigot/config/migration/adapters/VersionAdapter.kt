package io.pixeloutlaw.minecraft.spigot.config.migration.adapters

import com.github.zafarkhaja.semver.Version
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class VersionAdapter {
    @ToJson
    fun toJson(version: Version): String = version.toString()

    @FromJson
    fun fromJson(version: String): Version = Version.valueOf(version)
}
