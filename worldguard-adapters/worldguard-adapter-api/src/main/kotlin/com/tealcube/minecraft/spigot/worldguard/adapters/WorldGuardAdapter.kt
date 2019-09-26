package com.tealcube.minecraft.spigot.worldguard.adapters

import org.bukkit.Location

/**
 * An adapter for checking flag statuses at a location.
 */
interface WorldGuardAdapter {
    /**
     * Returns true if flag is allow at location.
     *
     * @param location Location to check
     * @param flagName Name of flag
     *
     * @return if flag is allow at location
     */
    fun isFlagAllowAtLocation(location: Location, flagName: String): Boolean

    /**
     * Returns true if flag is deny at location.
     *
     * @param location Location to check
     * @param flagName Name of flag
     *
     * @return if flag is deny at location
     */
    fun isFlagDenyAtLocation(location: Location, flagName: String): Boolean

    /**
     * Registers a flag with WorldGuard.
     *
     * @param flagName Name of the flag
     */
    fun registerFlag(flagName: String)
}
