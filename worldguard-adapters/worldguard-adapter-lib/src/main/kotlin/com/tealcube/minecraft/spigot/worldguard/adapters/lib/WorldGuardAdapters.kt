package com.tealcube.minecraft.spigot.worldguard.adapters.lib

import com.tealcube.minecraft.spigot.worldguard.adapters.WorldGuardAdapter
import com.tealcube.minecraft.spigot.worldguard.adapters.v6_2_x.WorldGuardAdapter62X
import com.tealcube.minecraft.spigot.worldguard.adapters.v7_0_x.WorldGuardAdapter70X
import org.bukkit.Bukkit
import java.util.logging.Level
import java.util.logging.Logger

object WorldGuardAdapters {
    private val logger: Logger = Logger.getLogger(WorldGuardAdapters::class.java.canonicalName)
    @JvmStatic
    val instance: WorldGuardAdapter by lazy {
        val worldGuardPlugin = Bukkit.getPluginManager().getPlugin("WorldGuard") ?: return@lazy NoOpWorldGuardAdapter
        return@lazy try {
            val versionOfWorldGuard = worldGuardPlugin.description.version
            with(versionOfWorldGuard) {
                when {
                    startsWith("6.2") -> WorldGuardAdapter62X()
                    startsWith("7.0") -> WorldGuardAdapter70X()
                    else -> {
                        logger.warning("Using an unsupported WorldGuard version! Defaulting to 7.0.x adapter!")
                        WorldGuardAdapter70X()
                    }
                }
            }
        } catch (ex: Exception) {
            logger.log(Level.WARNING, "Unable to find correct WorldGuardAdapter, defaulting to no-op!", ex)
            NoOpWorldGuardAdapter
        }
    }
}
