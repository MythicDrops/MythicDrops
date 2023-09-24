package com.tealcube.minecraft.bukkit.mythicdrops.api.socketing

import com.tealcube.minecraft.bukkit.mythicdrops.api.managers.ConfigurationBasedManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.managers.WeightedManager

/**
 * A manager for storing and retrieving [SocketExtenderType]s.
 */
interface SocketExtenderTypeManager :
    ConfigurationBasedManager<SocketExtenderType>,
    WeightedManager<SocketExtenderType, String> {
    fun getIgnoreColors(): Set<SocketExtenderType> = get().filter { it.isIgnoreColors }.toSet()

    fun getNotIgnoreColors(): Set<SocketExtenderType> = get().filter { !it.isIgnoreColors }.toSet()
}
