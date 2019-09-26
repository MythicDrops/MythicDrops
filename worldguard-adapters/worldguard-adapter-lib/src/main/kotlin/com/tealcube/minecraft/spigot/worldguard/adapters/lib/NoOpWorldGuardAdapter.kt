package com.tealcube.minecraft.spigot.worldguard.adapters.lib

import com.tealcube.minecraft.spigot.worldguard.adapters.WorldGuardAdapter
import org.bukkit.Location

/**
 * NoOp implementation of [WorldGuardAdapter].
 */
object NoOpWorldGuardAdapter : WorldGuardAdapter {
    // do nothing, return true if no WorldGuard
    override fun isFlagAllowAtLocation(location: Location, flagName: String): Boolean = true

    // do nothing, return false if no WorldGuard
    override fun isFlagDenyAtLocation(location: Location, flagName: String): Boolean = false

    override fun registerFlag(flagName: String) {
        // do nothing, no op
    }
}
