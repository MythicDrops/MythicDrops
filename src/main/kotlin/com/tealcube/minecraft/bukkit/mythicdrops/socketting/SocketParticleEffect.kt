/*
 * The MIT License
 * Copyright © 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.socketting

import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.EffectTarget
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketEffect
import com.tealcube.minecraft.bukkit.mythicdrops.utils.RandomRangeUtil
import org.bukkit.Bukkit
import org.bukkit.Effect
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.LivingEntity

data class SocketParticleEffect(
    val particleEffect: Effect,
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
            val effect = try {
                Effect.valueOf(key)
            } catch (ex: Exception) {
                return null
            }
            val duration = configurationSection.getInt("$key.duration")
            val intensity = configurationSection.getInt("$key.intensity")
            val radius = configurationSection.getInt("$key.radius")
            val chanceToTrigger = configurationSection.getDouble("$key.chanceToTrigger", 1.0)
            val target = configurationSection.getString("$key.target")
            var effectTarget = EffectTarget.getFromName(target)
            val affectsWielder = configurationSection.getBoolean("$key.affectsWielder")
            val affectsTarget = configurationSection.getBoolean("$key.affectsTarget")
            return SocketParticleEffect(
                effect,
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
        if (RandomRangeUtil.randomRangeDouble(0.0, 1.0) > chanceToTrigger) {
            return
        }
        for (i in 0 until duration) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(
                MythicDropsPlugin.getInstance(),
                {
                    target.world
                        .playEffect(target.eyeLocation, particleEffect, RandomRangeUtil.randomRange(0, 4))
                },
                i * 10L
            )
        }
    }
}