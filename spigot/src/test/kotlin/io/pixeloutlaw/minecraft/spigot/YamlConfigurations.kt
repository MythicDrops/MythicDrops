package io.pixeloutlaw.minecraft.spigot

import org.bukkit.configuration.file.YamlConfiguration

fun YamlConfiguration.loadFromResource(path: String): YamlConfiguration {
    val rawText =
        YamlConfiguration::class.java.classLoader
            .getResource(path)
            ?.readText() ?: ""
    loadFromString(rawText)
    return this
}
