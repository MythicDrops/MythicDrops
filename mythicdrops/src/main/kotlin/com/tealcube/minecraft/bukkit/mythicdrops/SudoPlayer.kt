package com.tealcube.minecraft.bukkit.mythicdrops

import org.bukkit.entity.Player
import org.bukkit.permissions.Permission

/**
 * Converts a [Player] into an implementation that can perform any action.
 */
class SudoPlayer(originalPlayer: Player): Player by originalPlayer {
    override fun isOp(): Boolean = true

    override fun hasPermission(name: String): Boolean = true

    override fun hasPermission(perm: Permission): Boolean = true
}
