package com.tealcube.minecraft.bukkit.mythicdrops.config.migration

import org.bukkit.configuration.Configuration
import java.io.File

/**
 * A variant of [Configuration] that can load/save itself from/to a [File].
 */
interface SmarterConfiguration : Configuration {
    /**
     * File that this can [load] from or [save] to.
     */
    val file: File?

    /**
     * The name of the [file]. Returns `""` if [file] is `null`.
     */
    val fileName: String
        get() = file?.name ?: ""

    /**
     * Loads this [Configuration] from [file]. Swallows any exceptions thrown.
     */
    fun load()

    /**
     * Saves this [Configuration] to [file]. Swallows any exceptions thrown.
     */
    fun save()
}
