package com.tealcube.minecraft.bukkit.mythicdrops

import org.bukkit.entity.Player

/**
 * Converts the current [Player] instance into a [SudoPlayer].
 */
fun Player.sudo() = SudoPlayer(this)
