package com.tealcube.minecraft.bukkit.mythicdrops.config.migration

import com.github.zafarkhaja.semver.Version
import org.bukkit.configuration.Configuration

/**
 * A variant of [Configuration] that has a `version` field that adheres to Semantic Versioning.
 */
interface VersionedConfiguration : Configuration {
    /**
     * Parsed [Version] from the `version` field.
     */
    var version: Version
        get() = try {
            Version.valueOf(getString("version"))
        } catch (ex: Exception) {
            Version.forIntegers(0)
        }
        set(value) {
            this.set("version", value.toString())
        }
}
