package com.tealcube.minecraft.bukkit.mythicdrops.config.migration

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/**
 * A rewrite of spigot-commons' `SmartYamlConfiguration` in kotlin.
 */
open class SmarterYamlConfiguration @JvmOverloads constructor(override var file: File? = null) :
    YamlConfiguration(), SmarterConfiguration {
    init {
        load()
    }

    /**
     * Loads from the file passed into the constructor. Ignores any exceptions thrown.
     *
     * Equivalent of calling [load] and passing in [file].
     */
    override fun load() {
        try {
            load(file ?: return)
        } catch (ignored: Throwable) {
        }
    }

    /**
     * Saves to the file passed into the constructor. Ignores any exceptions thrown.
     *
     * Equivalent of calling [save] and passing in [file].
     */
    override fun save() {
        try {
            save(file ?: return)
        } catch (ignored: Throwable) {
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ConfigurationSection) return false

        val ourKeys = getKeys(true)
        val theirKeys = other.getKeys(true)
        if (theirKeys != ourKeys) return false

        for (key in ourKeys) {
            val ourValue = get(key)
            val theirValue = other.get(key)
            if (ourValue != theirValue) {
                return false
            }
        }

        return true
    }

    override fun toString(): String {
        val keysAndValues = getKeys(true).mapNotNull {
            if (isConfigurationSection(it)) {
                return@mapNotNull null
            }
            it to get(it)
        }.toMap()
        return "SmarterYamlConfiguration($keysAndValues)"
    }

    override fun hashCode(): Int {
        return file?.hashCode() ?: 0
    }
}
