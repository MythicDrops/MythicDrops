package com.tealcube.minecraft.bukkit.mythicdrops.worldguard

import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.flags.StateFlag
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException

fun StateFlag.register() {
    try {
        WorldGuard.getInstance().flagRegistry.register(this)
    } catch (ex: FlagConflictException) {
        // do nothing
    }
}
