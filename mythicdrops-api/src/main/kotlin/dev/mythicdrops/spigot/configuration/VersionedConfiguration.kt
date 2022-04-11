package dev.mythicdrops.spigot.configuration

import org.bukkit.configuration.Configuration

/**
 * A version of [Configuration] that is also [Versioned].
 */
interface VersionedConfiguration : Configuration, Versioned
