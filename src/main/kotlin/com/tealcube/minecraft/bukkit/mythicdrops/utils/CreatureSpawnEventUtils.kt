package com.tealcube.minecraft.bukkit.mythicdrops.utils

import org.bukkit.entity.EnderDragon
import org.bukkit.entity.Monster
import org.bukkit.entity.Slime
import org.bukkit.event.entity.CreatureSpawnEvent

object CreatureSpawnEventUtils {
    fun shouldCancelDropsBasedOnCreatureSpawnEvent(event: CreatureSpawnEvent): Boolean {
        if (event.isCancelled) {
            return true
        }
        val isSlime = event.entity is Slime
        val isEnderDragon = event.entity is EnderDragon
        val isMonster = event.entity is Monster
        return !isSlime && !isEnderDragon && !isMonster
    }
}