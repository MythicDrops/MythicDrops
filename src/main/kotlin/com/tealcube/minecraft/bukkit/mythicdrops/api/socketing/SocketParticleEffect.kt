/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2019 Richard Harrah
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.api.socketing

import com.squareup.moshi.JsonClass
import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin
import com.tealcube.minecraft.bukkit.mythicdrops.safeRandom
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.LivingEntity

@JsonClass(generateAdapter = true)
data class SocketParticleEffect(
    val particleEffect: Particle,
    override val intensity: Int,
    override val duration: Int,
    override val radius: Int,
    override val chanceToTrigger: Double,
    override val effectTarget: EffectTarget,
    override val affectsWielder: Boolean,
    override val affectsTarget: Boolean
) : SocketEffect {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection, key: String): SocketParticleEffect? {
            val particle = try {
                Particle.valueOf(key)
            } catch (ex: Exception) {
                return null
            }
            val duration = configurationSection.getInt("$key.duration")
            val intensity = configurationSection.getInt("$key.intensity")
            val radius = configurationSection.getInt("$key.radius")
            val chanceToTrigger = configurationSection.getDouble("$key.chance-to-trigger", 1.0)
            val target = configurationSection.getString("$key.target")
            val effectTarget = EffectTarget.fromName(target)
            val affectsWielder = configurationSection.getBoolean("$key.affects-wielder")
            val affectsTarget = configurationSection.getBoolean("$key.affects-target")
            return SocketParticleEffect(
                particle,
                intensity,
                duration,
                radius,
                chanceToTrigger,
                effectTarget,
                affectsWielder,
                affectsTarget
            )
        }
    }

    override fun apply(target: LivingEntity?) {
        if (target == null) {
            return
        }
        if ((0.0..1.0).safeRandom() > chanceToTrigger) {
            return
        }
        val halfSecondDuration = duration / 10
        for (i in 0 until halfSecondDuration) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(
                MythicDropsPlugin.getInstance(),
                {
                    target.world
                        .spawnParticle(particleEffect, target.eyeLocation, intensity)
                },
                i * 10L
            )
        }
    }
}
