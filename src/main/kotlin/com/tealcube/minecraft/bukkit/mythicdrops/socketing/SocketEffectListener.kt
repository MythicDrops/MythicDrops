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
package com.tealcube.minecraft.bukkit.mythicdrops.socketing

import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.EffectTarget
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.GemTriggerType
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketCommand
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketCommandRunner
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketEffect
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.cache.SocketGemCacheManager
import com.tealcube.minecraft.bukkit.mythicdrops.sudoDispatchCommand
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class SocketEffectListener(
    private val mythicDropsPlugin: MythicDropsPlugin,
    private val socketGemCacheManager: SocketGemCacheManager,
    private val settingsManager: SettingsManager
) : Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    fun onEntityDamageByEntityEvent(event: EntityDamageByEntityEvent) {
        if (event.isCancelled) {
            return
        }
        val eventEntity = event.entity
        val eventDamager = event.damager
        val defender = eventEntity as? LivingEntity ?: return
        val attacker = if (eventDamager is LivingEntity) {
            eventDamager
        } else if (eventDamager is Projectile && eventDamager.shooter is LivingEntity) {
            eventDamager.shooter as LivingEntity
        } else {
            return
        }
        if (defender == attacker) {
            return
        }

        if (defender is Player) {
            applyPlayerDuringEntityDamageByEntityEvent(
                defender,
                attacker,
                settingsManager.socketingSettings.options.isUseDefenderArmorEquipped,
                settingsManager.socketingSettings.options.isUseDefenderItemInHand,
                true
            )
        }

        if (attacker !is Player) {
            return
        }

        applyPlayerDuringEntityDamageByEntityEvent(
            attacker,
            defender,
            settingsManager.socketingSettings.options.isUseAttackerArmorEquipped,
            settingsManager.socketingSettings.options.isUseAttackerItemInHand,
            false
        )
    }

    private fun applyPlayerDuringEntityDamageByEntityEvent(
        applier: Player,
        recipient: LivingEntity,
        useArmorEquipped: Boolean,
        useItemInHand: Boolean,
        beingHit: Boolean
    ) {
        val socketCache = socketGemCacheManager.getOrCreateSocketGemCache(applier.uniqueId)
        val socketEffects = mutableSetOf<SocketEffect>()
        val socketCommands = mutableSetOf<SocketCommand>()
        val hitTriggerType = if (beingHit) {
            GemTriggerType.WHEN_HIT
        } else {
            GemTriggerType.ON_HIT
        }
        if (useArmorEquipped) {
            socketEffects.addAll(socketCache.getArmorSocketEffects(hitTriggerType))
            socketEffects.addAll(socketCache.getArmorSocketEffects(GemTriggerType.ON_HIT_AND_WHEN_HIT))
            socketCommands.addAll(socketCache.getArmorSocketCommands(hitTriggerType))
            socketCommands.addAll(socketCache.getArmorSocketCommands(GemTriggerType.ON_HIT_AND_WHEN_HIT))
        }
        if (useItemInHand) {
            socketEffects.addAll(socketCache.getMainHandSocketEffects(hitTriggerType))
            socketEffects.addAll(socketCache.getMainHandSocketEffects(GemTriggerType.ON_HIT_AND_WHEN_HIT))
            socketEffects.addAll(socketCache.getOffHandSocketEffects(hitTriggerType))
            socketEffects.addAll(socketCache.getOffHandSocketEffects(GemTriggerType.ON_HIT_AND_WHEN_HIT))
            socketCommands.addAll(socketCache.getMainHandSocketCommands(hitTriggerType))
            socketCommands.addAll(socketCache.getMainHandSocketCommands(GemTriggerType.ON_HIT_AND_WHEN_HIT))
            socketCommands.addAll(socketCache.getOffHandSocketCommands(hitTriggerType))
            socketCommands.addAll(socketCache.getOffHandSocketCommands(GemTriggerType.ON_HIT_AND_WHEN_HIT))
        }
        applyEffectsDuringEntityDamageByEntityEvent(socketEffects, applier, recipient)
        applyCommandsDuringEntityDamageByEntityEvent(socketCommands, applier, recipient)
    }

    private fun applyEffectsDuringEntityDamageByEntityEvent(
        effects: Set<SocketEffect>,
        applier: Player,
        recipient: LivingEntity
    ) {
        for (effect in effects) {
            when (effect.effectTarget) {
                EffectTarget.SELF -> {
                    effect.apply(applier)
                }
                EffectTarget.OTHER -> {
                    effect.apply(recipient)
                }
                EffectTarget.AREA -> {
                    val radius = effect.radius.toDouble()
                    val nearbyLivingEntities =
                        recipient.getNearbyEntities(radius, radius, radius).filterIsInstance<LivingEntity>()
                    nearbyLivingEntities.forEach {
                        if (!effect.affectsWielder && it.uniqueId == applier.uniqueId) {
                            return@forEach
                        }
                        effect.apply(it)
                    }
                }
                else -> {
                }
            }
        }
    }

    private fun applyCommandsDuringEntityDamageByEntityEvent(
        socketCommands: Set<SocketCommand>,
        applier: Player,
        recipient: LivingEntity
    ) {
        for (socketCommand in socketCommands) {
            var commandToRun = socketCommand.command
            if (commandToRun.contains("%wielder%")) {
                commandToRun = commandToRun.replace("%wielder%", applier.name)
            }
            if (commandToRun.contains("%target%")) {
                if (recipient is Player) {
                    commandToRun = commandToRun.replace("%target%", recipient.name)
                } else {
                    continue
                }
            }
            when (socketCommand.runner) {
                SocketCommandRunner.CONSOLE -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToRun)
                }
                SocketCommandRunner.PLAYER -> {
                    Bukkit.dispatchCommand(applier, commandToRun)
                }
                SocketCommandRunner.SUDO -> {
                    applier.sudoDispatchCommand(mythicDropsPlugin, socketCommand)
                }
            }
        }
    }
}
