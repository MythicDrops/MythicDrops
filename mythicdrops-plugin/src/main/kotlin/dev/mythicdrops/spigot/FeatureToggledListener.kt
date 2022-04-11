package dev.mythicdrops.spigot

import org.bukkit.event.Listener

/**
 * Represents a Bukkit event listener that is conditionally enabled.
 */
internal interface FeatureToggledListener : Listener {
    /**
     * Returns true if this listener should be enabled.
     */
    fun isEnabled(): Boolean
}
