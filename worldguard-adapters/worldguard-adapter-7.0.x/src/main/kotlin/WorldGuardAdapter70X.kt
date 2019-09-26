package com.tealcube.minecraft.spigot.worldguard.adapters.v7_0_x

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.flags.StateFlag
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry
import com.sk89q.worldguard.protection.regions.RegionContainer
import com.tealcube.minecraft.spigot.worldguard.adapters.WorldGuardAdapter
import org.bukkit.Location

class WorldGuardAdapter70X : WorldGuardAdapter {
    private val regionContainer: RegionContainer by lazy {
        WorldGuard.getInstance().platform.regionContainer
    }
    private val flagRegistry: FlagRegistry by lazy {
        WorldGuard.getInstance().flagRegistry
    }

    override fun isFlagAllowAtLocation(location: Location, flagName: String): Boolean =
        getFlagFromRegistry(flagName)?.let { isFlagAllowAtLocation(location, it) } ?: true

    override fun isFlagDenyAtLocation(location: Location, flagName: String): Boolean =
        getFlagFromRegistry(flagName)?.let { isFlagDenyAtLocation(location, it) } ?: false

    override fun registerFlag(flagName: String) {
        flagRegistry.register(StateFlag(flagName, true))
    }

    private fun getRegionQuery() = regionContainer.createQuery()

    private fun isFlagAllowAtLocation(location: Location, flag: StateFlag) =
        getRegionQuery().testState(BukkitAdapter.adapt(location), null, flag)

    private fun isFlagDenyAtLocation(location: Location, flag: StateFlag) =
        !getRegionQuery().testState(BukkitAdapter.adapt(location), null, flag)

    private fun getFlagFromRegistry(flagName: String): StateFlag? = flagRegistry.get(flagName) as? StateFlag
}
