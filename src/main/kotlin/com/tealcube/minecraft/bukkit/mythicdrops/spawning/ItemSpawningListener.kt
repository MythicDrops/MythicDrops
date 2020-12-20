/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2020 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.spawning

import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.names.NameType
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.events.EntityNameEvent
import com.tealcube.minecraft.bukkit.mythicdrops.events.EntitySpawningEvent
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicDropTracker
import com.tealcube.minecraft.bukkit.mythicdrops.names.NameMap
import com.tealcube.minecraft.bukkit.mythicdrops.utils.CreatureSpawnEventUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.EquipmentUtils
import com.tealcube.minecraft.bukkit.mythicdrops.worldguard.WorldGuardFlags
import com.tealcube.minecraft.spigot.worldguard.adapters.lib.WorldGuardAdapters
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getTier
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.inventory.ItemStack

class ItemSpawningListener(private val mythicDrops: MythicDrops) : Listener {
    companion object {
        private const val WORLD_MAX_HEIGHT = 255
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onCreatureSpawnEventLowest(creatureSpawnEvent: CreatureSpawnEvent) {
        if (shouldNotHandleSpawnEvent(creatureSpawnEvent)) return
        if (mythicDrops.settingsManager.configSettings.options.isGiveAllMobsNames) {
            giveLivingEntityName(creatureSpawnEvent.entity)
        }

        // handle blank mob spawn
        if (mythicDrops.settingsManager.configSettings.options.blankMobSpawn.isEnabled) {
            creatureSpawnEvent.entity.equipment?.let {
                it.clear()
                if (
                    creatureSpawnEvent.entityType == EntityType.SKELETON &&
                    !mythicDrops.settingsManager.configSettings.options.blankMobSpawn.isSkeletonsSpawnWithoutBow
                ) {
                    it.setItemInMainHand(ItemStack(Material.BOW))
                }
            }
        }

        // turn on or off can pick up items per mob
        creatureSpawnEvent.entity.canPickupItems =
            mythicDrops.settingsManager.configSettings.options.isCanMobsPickUpEquipment
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onCreatureSpawnEventLow(creatureSpawnEvent: CreatureSpawnEvent) {
        if (shouldNotHandleSpawnEvent(creatureSpawnEvent)) return

        val disableLegacyItemCheck = mythicDrops.settingsManager.configSettings.options.isDisableLegacyItemChecks
        val dropStrategy =
            mythicDrops.dropStrategyManager.getById(mythicDrops.settingsManager.configSettings.drops.strategy)
                ?: return

        MythicDropTracker.spawn()
        val drops = dropStrategy.getDropsForCreatureSpawnEvent(creatureSpawnEvent)

        val tiers = drops.mapNotNull { it.first.getTier(mythicDrops.tierManager, disableLegacyItemCheck) }

        val ese = EntitySpawningEvent(creatureSpawnEvent.entity)
        Bukkit.getPluginManager().callEvent(ese)

        drops.forEach {
            val itemStack = it.first
            val dropChance = it.second

            EquipmentUtils.equipEntity(creatureSpawnEvent.entity, itemStack, dropChance)
        }

        val rarestTier = tiers.minByOrNull { it.weight }

        if (drops.isNotEmpty()) {
            giveLivingEntityName(creatureSpawnEvent.entity, rarestTier)
        }
    }

    private fun giveLivingEntityName(livingEntity: LivingEntity, tier: Tier? = null) {
        // due to the order of checks, we may end up in here without checking if mobs can be given names,
        // so check if they even can be given names.
        if (!mythicDrops.settingsManager.configSettings.options.isGiveMobsNames) return

        val generalName = NameMap
            .getRandom(NameType.GENERAL_MOB_NAME, "")
        val specificName = NameMap
            .getRandom(
                NameType.SPECIFIC_MOB_NAME,
                "." + livingEntity.type.name.toLowerCase()
            )
        val name = if (!specificName.isNullOrBlank()) {
            specificName
        } else {
            generalName
        }
        val displayColor =
            if (tier != null &&
                mythicDrops
                    .settingsManager
                    .configSettings
                    .options
                    .isGiveMobsColoredNames
            ) {
                tier.displayColor
            } else {
                ChatColor.WHITE
            }

        val event = EntityNameEvent(livingEntity, displayColor.toString() + name)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) {
            return
        }

        livingEntity.customName = event.name
        livingEntity.isCustomNameVisible = true
    }

    // returns true if we should NOT spawn based on event spawn reason criteria
    private fun isShouldNotSpawnBasedOnSpawnReason(event: CreatureSpawnEvent): Boolean {
        val spawnPrevention = mythicDrops.settingsManager.creatureSpawningSettings.spawnPrevention
        return when {
            event.spawnReason == CreatureSpawnEvent.SpawnReason.DROWNED && spawnPrevention.isDrowned -> true
            event.spawnReason == CreatureSpawnEvent.SpawnReason.REINFORCEMENTS &&
                spawnPrevention.isReinforcements -> true
            event.spawnReason == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG && spawnPrevention.isSpawnEgg -> true
            event.spawnReason == CreatureSpawnEvent.SpawnReason.SPAWNER && spawnPrevention.isSpawner -> true
            mythicDrops
                .settingsManager
                .creatureSpawningSettings
                .spawnPrevention
                .aboveY[event.entity.world.name] ?: WORLD_MAX_HEIGHT
                <= event.entity.location.y -> true
            else -> false
        }
    }

    // returns true if we should NOT spawn based on event criteria
    private fun shouldNotHandleSpawnEvent(event: CreatureSpawnEvent): Boolean {
        return when {
            CreatureSpawnEventUtil.shouldCancelDropsBasedOnCreatureSpawnEvent(event) -> true
            !mythicDrops
                .settingsManager
                .configSettings
                .multiworld
                .enabledWorlds
                .contains(event.entity.world.name) -> {
                true
            }
            isShouldNotSpawnBasedOnSpawnReason(event) -> true
            !mythicDrops
                .settingsManager
                .configSettings
                .options
                .isDisplayMobEquipment -> true
            WorldGuardAdapters.isFlagDenyAtLocation(event.location, WorldGuardFlags.mythicDrops) -> true
            else -> false
        }
    }
}
