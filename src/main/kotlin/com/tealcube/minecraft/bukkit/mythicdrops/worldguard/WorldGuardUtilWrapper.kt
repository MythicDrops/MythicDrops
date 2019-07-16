package com.tealcube.minecraft.bukkit.mythicdrops.worldguard

import org.bukkit.Bukkit
import org.bukkit.Location

object WorldGuardUtilWrapper {
    fun isFlagAllowAtLocation(location: Location, flagName: String): Boolean {
        if (!canCallWorldGuard()) {
            return try {
                WorldGuardUtil.isFlagAllowAtLocation(location, flagName)
            } catch (e: NoClassDefFoundError) {
                return false
            }
        }
        return false
    }

    fun isFlagDenyAtLocation(location: Location, flagName: String): Boolean {
        if (!canCallWorldGuard()) {
            return try {
                WorldGuardUtil.isFlagDenyAtLocation(location, flagName)
            } catch (e: NoClassDefFoundError) {
                return false
            }
        }
        return false
    }

    fun registerFlags() {
        if (canCallWorldGuard()) {
            WorldGuardFlags.registerAllFlags()
        }
    }

    fun canCallWorldGuard() = Bukkit.getPluginManager().getPlugin("WorldGuard") != null
}