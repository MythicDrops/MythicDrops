package io.pixeloutlaw.minecraft.spigot.mythicdrops

import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection

internal fun ConfigurationSection.getOrCreateSection(path: String): ConfigurationSection =
    getConfigurationSection(path) ?: createSection(path)

internal fun ConfigurationSection.getChatColor(path: String): ChatColor? = enumValueOrNull<ChatColor>(getString(path))

internal fun ConfigurationSection.getChatColor(path: String, def: ChatColor): ChatColor =
    enumValueOrNull<ChatColor>(getString(path)) ?: def

internal fun ConfigurationSection.getNonNullString(path: String, def: String = "") = getString(path) ?: def

internal fun ConfigurationSection.getMaterial(path: String, def: Material = Material.AIR) =
    Material.getMaterial(getNonNullString(path)) ?: def

internal inline fun <reified T : Enum<T>> ConfigurationSection.getEnum(path: String, def: T): T =
    enumValueOrNull<T>(getNonNullString(path)) ?: def
