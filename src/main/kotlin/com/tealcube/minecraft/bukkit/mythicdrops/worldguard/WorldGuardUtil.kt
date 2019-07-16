package com.tealcube.minecraft.bukkit.mythicdrops.worldguard

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.flags.StateFlag
import com.sk89q.worldguard.protection.regions.RegionContainer
import org.bukkit.Location

object WorldGuardUtil {
    private val regionContainer: RegionContainer by lazy {
        WorldGuard.getInstance().platform.regionContainer
    }

    private fun getRegionQuery() = regionContainer.createQuery()

    fun isFlagAllowAtLocation(location: Location, flagName: String) = WorldGuardFlags.flagMap[flagName]?.let {
        isFlagAllowAtLocation(location, it)
    } ?: false

    fun isFlagDenyAtLocation(location: Location, flagName: String) = WorldGuardFlags.flagMap[flagName]?.let {
        isFlagDenyAtLocation(location, it)
    } ?: false

    fun isFlagAllowAtLocation(location: Location, flag: StateFlag) =
        getRegionQuery().testState(BukkitAdapter.adapt(location), null, flag)

    fun isFlagDenyAtLocation(location: Location, flag: StateFlag) =
        !getRegionQuery().testState(BukkitAdapter.adapt(location), null, flag)
}
