package com.tealcube.minecraft.bukkit.mythicdrops.logging

import java.util.logging.Logger

object JulLoggerFactory {
    private val cachedLoggers = mutableMapOf<String, Logger>()

    fun getLogger(clazz: Class<*>) = cachedLoggers.getOrPut(clazz.canonicalName) {
        Logger.getLogger(clazz.canonicalName)
    }

    fun getLogger(name: String) = cachedLoggers.getOrPut(name) {
        Logger.getLogger(name)
    }
}