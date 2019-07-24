package com.tealcube.minecraft.bukkit.mythicdrops.api.socketting

import org.bukkit.entity.LivingEntity

interface SocketEffect {
    val intensity: Int
    val duration: Int
    val effectTarget: EffectTarget
    val radius: Int
    val chanceToTrigger: Double
    val affectsWielder: Boolean
    val affectsTarget: Boolean

    fun apply(target: LivingEntity?)
}