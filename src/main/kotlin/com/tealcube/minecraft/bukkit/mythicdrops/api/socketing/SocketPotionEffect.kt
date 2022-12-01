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

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.random.Random

data class SocketPotionEffect(
    val potionEffectType: PotionEffectType,
    override val intensity: Int,
    override val duration: Int,
    override val radius: Int,
    override val chanceToTrigger: Double,
    override val effectTarget: EffectTarget,
    override val affectsWielder: Boolean,
    override val affectsTarget: Boolean
) : SocketEffect {
    companion object {
        private const val msPerTick = 50

        fun fromConfigurationSection(configurationSection: ConfigurationSection, key: String): SocketPotionEffect? {
            val effect = PotionEffectType.getByName(key) ?: return null
            val duration = configurationSection.getInt("$key.duration")
            val intensity = configurationSection.getInt("$key.intensity")
            val radius = configurationSection.getInt("$key.radius")
            val chanceToTrigger = configurationSection.getDouble("$key.chance-to-trigger", 1.0)
            val target = configurationSection.getString("$key.target")
            val effectTarget = EffectTarget.fromName(target)
            val affectsWielder = configurationSection.getBoolean("$key.affects-wielder")
            val affectsTarget = configurationSection.getBoolean("$key.affects-target")
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

    @Transient
    private val durationInTicks = duration / msPerTick

    override fun apply(target: LivingEntity?) {
        if (target == null) {
            return
        }
        // check the targets current potion effects
        val effects = target.activePotionEffects
        // if they have a potion effect of the same type, same strength (or higher strength), or longer duration,
        // kick out
        val shouldKickOut = effects.any {
            val sameType = it.type === potionEffectType
            val sameIntensity = it.amplifier == intensity && it.duration > durationInTicks
            val higherIntensity = it.amplifier > intensity
            sameType && (sameIntensity || higherIntensity)
        }
        // if the chance to trigger doesn't flip, kick out
        if (shouldKickOut || Random.nextDouble(0.0, 1.0) > chanceToTrigger) {
            return
        }
        target.removePotionEffect(potionEffectType)
        target.addPotionEffect(PotionEffect(potionEffectType, durationInTicks, intensity))
    }

    override fun remove(target: LivingEntity?) {
        if (target == null) {
            return
        }

        // check the targets current potion effects
        val effects = target.activePotionEffects

        // if they have a potion effect of the same type and same strength, remove it
        val hasEffect = effects.any { it.type === potionEffectType && it.amplifier == intensity }
        if (hasEffect) {
            target.removePotionEffect(potionEffectType)
        }
    }

    override fun toDebugString(): String {
        return "Potion:$potionEffectType:$intensity:$duration:$radius:$chanceToTrigger:$effectTarget:$affectsWielder:$affectsTarget"
    }
}
