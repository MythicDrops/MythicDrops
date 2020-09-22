package com.tealcube.minecraft.bukkit.mythicdrops.aura

import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.GemTriggerType
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketEffect
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.cache.SocketGemCache
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import kotlin.math.max

object Auras {
    fun applyAuraSocketEffectsForSocketGemCache(socketGemCache: SocketGemCache) {
        val player = Bukkit.getPlayer(socketGemCache.owner) ?: return
        val armorEffectCache = socketGemCache.getArmorSocketEffects(GemTriggerType.AURA)
        val mainHandEffectCache = socketGemCache.getMainHandSocketEffects(GemTriggerType.AURA)
        val offHandEffectCache = socketGemCache.getOffHandSocketEffects(GemTriggerType.AURA)
        val socketEffectsToApply = armorEffectCache + mainHandEffectCache + offHandEffectCache
        if (socketEffectsToApply.isEmpty()) {
            return
        }
        applyAuraSocketEffectsForPlayer(socketEffectsToApply, player)
    }

    private fun applyAuraSocketEffectsForPlayer(
        socketEffectsToApply: Set<SocketEffect>,
        player: Player
    ) {
        val largestRadius = socketEffectsToApply.fold(0) { acc, socketEffect -> max(acc, socketEffect.radius) }
        val anyAffectsTarget = socketEffectsToApply.any { it.affectsTarget }
        val nearbyEntities = if (anyAffectsTarget) {
            // this is expensive, so we should avoid calling it whenever possible
            player.getNearbyEntities(largestRadius.toDouble(), largestRadius.toDouble(), largestRadius.toDouble())
                .mapNotNull { it as? LivingEntity }
        } else {
            emptyList()
        }
        socketEffectsToApply.forEach { socketEffectToApply ->
            applySocketEffect(socketEffectToApply, player, nearbyEntities)
        }
    }

    private fun applySocketEffect(
        socketEffectToApply: SocketEffect,
        player: Player,
        nearbyEntities: List<LivingEntity>
    ) {
        val radius = socketEffectToApply.radius * socketEffectToApply.radius
        if (socketEffectToApply.affectsWielder) {
            socketEffectToApply.apply(player)
        }
        if (socketEffectToApply.affectsTarget) {
            nearbyEntities.forEach { nearbyEntity ->
                if (player.location.distanceSquared(nearbyEntity.location) <= radius) {
                    socketEffectToApply.apply(nearbyEntity)
                }
            }
        }
    }
}
