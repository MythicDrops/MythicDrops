package dev.mythicdrops.spigot.configuration

import org.bukkit.configuration.Configuration

/**
 * A version of [Configuration] that is also [FileAware] and [Versioned].
 */
interface VersionedFileAwareConfiguration : Configuration, FileAware, Versioned
