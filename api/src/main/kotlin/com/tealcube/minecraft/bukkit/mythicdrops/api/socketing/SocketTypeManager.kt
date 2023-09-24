package com.tealcube.minecraft.bukkit.mythicdrops.api.socketing

import com.tealcube.minecraft.bukkit.mythicdrops.api.managers.ConfigurationBasedManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.managers.WeightedManager

/**
 * A manager for storing and retrieving [SocketType]s.
 */
interface SocketTypeManager : ConfigurationBasedManager<SocketType>, WeightedManager<SocketType, String> {
    fun getIgnoreColors(): Set<SocketType> = get().filter { it.isIgnoreColors }.toSet()
}
