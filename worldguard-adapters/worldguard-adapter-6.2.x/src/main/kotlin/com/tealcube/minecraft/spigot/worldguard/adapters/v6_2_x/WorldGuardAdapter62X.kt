package com.tealcube.minecraft.spigot.worldguard.adapters.v6_2_x

import com.sk89q.worldguard.bukkit.RegionContainer
import com.sk89q.worldguard.bukkit.WGBukkit
import com.sk89q.worldguard.protection.flags.StateFlag
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry
import com.tealcube.minecraft.spigot.worldguard.adapters.WorldGuardAdapter
import org.bukkit.Location
import org.bukkit.entity.Player

class WorldGuardAdapter62X : WorldGuardAdapter {
    private val regionContainer: RegionContainer by lazy {
        WGBukkit.getPlugin().regionContainer
    }
    private val flagRegistry: FlagRegistry by lazy {
        WGBukkit.getPlugin().flagRegistry
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
        getRegionQuery().testState(location, null as? Player, flag)

    private fun isFlagDenyAtLocation(location: Location, flag: StateFlag) =
        !getRegionQuery().testState(location, null as? Player, flag)

    private fun getFlagFromRegistry(flagName: String): StateFlag? = flagRegistry.get(flagName) as? StateFlag
}
