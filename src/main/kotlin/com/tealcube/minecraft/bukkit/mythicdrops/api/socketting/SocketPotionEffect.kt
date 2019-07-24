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
package com.tealcube.minecraft.bukkit.mythicdrops.api.socketting

import org.apache.commons.lang3.RandomUtils
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

data class SocketPotionEffect(
    val potionEffectType: PotionEffectType,
    override val intensity: Int,
    override val duration: Int,
    override val radius: Int,
    override val chanceToTrigger: Double,
    override val effectTarget: EffectTarget,
    override val affectsWielder: Boolean,
    override val affectsTarget: Boolean
): SocketEffect {
    companion object {
        private const val msPerTick = 50

        fun fromConfigurationSection(configurationSection: ConfigurationSection, key: String): SocketPotionEffect? {
            val effect = PotionEffectType.getByName(key) ?: return null
            val duration = configurationSection.getInt("$key.duration")
            val intensity = configurationSection.getInt("$key.intensity")
            val radius = configurationSection.getInt("$key.radius")
            val chanceToTrigger = configurationSection.getDouble("$key.chanceToTrigger", 1.0)
            val target = configurationSection.getString("$key.target")
            var effectTarget = EffectTarget.getFromName(target)
            val affectsWielder = configurationSection.getBoolean("$key.affectsWielder")
            val affectsTarget = configurationSection.getBoolean("$key.affectsTarget")
            return SocketPotionEffect(
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
        if (RandomUtils.nextDouble(0.0, 1.0) > chanceToTrigger) {
            return
        }
        val dur = duration / msPerTick
        val effects = target.activePotionEffects
        // check the targets current potion effects
        for (effect in effects) {
            // if they have a potion effect of the same type as this gem effect
            if (potionEffectType === effect.type) {
                // if the potion effect is the same strength p
                if (intensity == effect.amplifier) {
                    if (dur < effect.duration) {
                        return
                    }
                } else if (intensity < effect.amplifier) {
                    return
                }
            }
        }
        target.removePotionEffect(potionEffectType)
        target.addPotionEffect(PotionEffect(potionEffectType, dur, intensity))
    }
}