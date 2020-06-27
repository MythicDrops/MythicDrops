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
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGenerationReason
import com.tealcube.minecraft.bukkit.mythicdrops.api.names.NameType
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.events.CustomItemGenerationEvent
import com.tealcube.minecraft.bukkit.mythicdrops.events.EntityNameEvent
import com.tealcube.minecraft.bukkit.mythicdrops.events.EntitySpawningEvent
import com.tealcube.minecraft.bukkit.mythicdrops.identification.IdentityTome
import com.tealcube.minecraft.bukkit.mythicdrops.identification.UnidentifiedItem
import com.tealcube.minecraft.bukkit.mythicdrops.items.builders.MythicDropBuilder
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory
import com.tealcube.minecraft.bukkit.mythicdrops.names.NameMap
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.SocketItem
import com.tealcube.minecraft.bukkit.mythicdrops.utils.CreatureSpawnEventUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.EquipmentUtils
import com.tealcube.minecraft.bukkit.mythicdrops.utils.GemUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TierUtil
import com.tealcube.minecraft.bukkit.mythicdrops.worldguard.WorldGuardFlags
import com.tealcube.minecraft.spigot.worldguard.adapters.lib.WorldGuardAdapters
import org.apache.commons.lang3.RandomUtils
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
        private const val ONE_HUNDRED_PERCENT = 1.0
        private const val ONE_HUNDRED_TEN_PERCENT = 1.1
        private val logger = JulLoggerFactory.getLogger(ItemSpawningListener::class)
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

        val itemChance = mythicDrops.settingsManager.configSettings.drops.itemChance
        val creatureSpawningMultiplier = mythicDrops
            .settingsManager
            .creatureSpawningSettings
            .dropMultipliers[creatureSpawnEvent.entity.type] ?: 0.0
        val itemChanceMultiplied = itemChance * creatureSpawningMultiplier
        val itemRoll = RandomUtils.nextDouble(0.0, 1.0)

        if (itemRoll > itemChanceMultiplied) {
            return
        }

        val tieredItemChance = mythicDrops.settingsManager.configSettings.drops.tieredItemChance
        val customItemChance = mythicDrops.settingsManager.configSettings.drops.customItemChance
        val socketGemChance = mythicDrops.settingsManager.configSettings.drops.socketGemChance
        val unidentifiedItemChance =
            mythicDrops.settingsManager.configSettings.drops.unidentifiedItemChance
        val identityTomeChance = mythicDrops.settingsManager.configSettings.drops.identityTomeChance
        val socketingEnabled = mythicDrops.settingsManager.configSettings.components.isSocketingEnabled
        val identifyingEnabled = mythicDrops.settingsManager.configSettings.components.isIdentifyingEnabled

        val tieredItemRoll = RandomUtils.nextDouble(0.0, 1.0)
        val customItemRoll = RandomUtils.nextDouble(0.0, 1.0)
        val socketGemRoll = RandomUtils.nextDouble(0.0, 1.0)
        val unidentifiedItemRoll = RandomUtils.nextDouble(0.0, 1.0)
        val identityTomeRoll = RandomUtils.nextDouble(0.0, 1.0)

        val tieredItemChanceMultiplied = tieredItemChance * creatureSpawningMultiplier

        var itemStack: ItemStack? = null
        var tier: Tier? = null
        // due to the way that spigot/minecraft handles drop chances, it won't drop items with a 100% drop chance
        // if a player isn't the one that killed it.
        var dropChance = if (mythicDrops.settingsManager.configSettings.options.isRequirePlayerKillForDrops) {
            ONE_HUNDRED_PERCENT
        } else {
            ONE_HUNDRED_TEN_PERCENT
        }

        // this is here to maintain previous behavior
        if (tieredItemRoll <= tieredItemChanceMultiplied && WorldGuardAdapters.instance.isFlagAllowAtLocation(
                creatureSpawnEvent.location,
                WorldGuardFlags.mythicDropsTiered
            )
        ) {
            tier = TierUtil.getTierForLivingEntity(
                creatureSpawnEvent.entity,
                mythicDrops.settingsManager.creatureSpawningSettings,
                mythicDrops.tierManager
            )?.also {
                itemStack = MythicDropBuilder(mythicDrops).withItemGenerationReason(ItemGenerationReason.MONSTER_SPAWN)
                    .useDurability(false).withTier(it).build()
                dropChance = it.chanceToDropOnMonsterDeath
            }
        } else if (customItemRoll < customItemChance && WorldGuardAdapters.instance.isFlagAllowAtLocation(
                creatureSpawnEvent.location,
                WorldGuardFlags.mythicDropsCustom
            )
        ) {
            mythicDrops.customItemManager.randomByWeight()?.let {
                val customItemGenerationEvent =
                    CustomItemGenerationEvent(it, it.toItemStack(mythicDrops.customEnchantmentRegistry))
                Bukkit.getPluginManager().callEvent(customItemGenerationEvent)
                if (!customItemGenerationEvent.isCancelled) {
                    itemStack = customItemGenerationEvent.result
                    dropChance = it.chanceToDropOnDeath
                }
            }
        } else if (socketingEnabled && socketGemRoll <= socketGemChance &&
            WorldGuardAdapters.instance.isFlagAllowAtLocation(
                creatureSpawnEvent.location,
                WorldGuardFlags.mythicDropsSocketGem
            )
        ) {
            val socketGem = GemUtil.getRandomSocketGemByWeight(creatureSpawnEvent.entityType)
            val material = GemUtil.getRandomSocketGemMaterial()
            if (socketGem != null && material != null) {
                itemStack =
                    SocketItem(material, socketGem, mythicDrops.settingsManager.socketingSettings.items.socketGem)
            }
        } else if (identifyingEnabled && unidentifiedItemRoll <= unidentifiedItemChance &&
            WorldGuardAdapters.instance.isFlagAllowAtLocation(
                creatureSpawnEvent.location,
                WorldGuardFlags.mythicDropsUnidentifiedItem
            )
        ) {
            mythicDrops.tierManager.randomByIdentityWeight()?.let { randomizedTier ->
                ItemUtil.getRandomMaterialFromCollection(ItemUtil.getMaterialsFromTier(randomizedTier))
                    ?.let { material ->
                        itemStack = UnidentifiedItem.build(
                            mythicDrops.settingsManager.creatureSpawningSettings,
                            mythicDrops.settingsManager.languageSettings.displayNames,
                            material,
                            mythicDrops.tierManager,
                            mythicDrops.settingsManager.identifyingSettings.items.unidentifiedItem,
                            creatureSpawnEvent.entityType,
                            randomizedTier
                        )
                    }
            }
        } else if (identifyingEnabled && identityTomeRoll <= identityTomeChance &&
            WorldGuardAdapters.instance.isFlagAllowAtLocation(
                creatureSpawnEvent.location,
                WorldGuardFlags.mythicDropsIdentityTome
            )
        ) {
            itemStack = IdentityTome(mythicDrops.settingsManager.identifyingSettings.items.identityTome)
        }

        val ese = EntitySpawningEvent(creatureSpawnEvent.entity)
        Bukkit.getPluginManager().callEvent(ese)

        EquipmentUtils.equipEntity(creatureSpawnEvent.entity, itemStack, dropChance)

        if (itemStack != null) {
            giveLivingEntityName(creatureSpawnEvent.entity, tier)
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
        return when {
            event.spawnReason == CreatureSpawnEvent.SpawnReason.REINFORCEMENTS &&
                mythicDrops
                    .settingsManager
                    .creatureSpawningSettings
                    .spawnPrevention
                    .isReinforcements -> true
            event.spawnReason == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG &&
                mythicDrops
                    .settingsManager
                    .creatureSpawningSettings
                    .spawnPrevention
                    .isSpawnEgg -> true
            event.spawnReason == CreatureSpawnEvent.SpawnReason.SPAWNER &&
                mythicDrops
                    .settingsManager
                    .creatureSpawningSettings
                    .spawnPrevention
                    .isSpawner -> true
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
                logger.fine("cancelling item spawn because of multiworld support")
                true
            }
            isShouldNotSpawnBasedOnSpawnReason(event) -> true
            !mythicDrops
                .settingsManager
                .configSettings
                .options
                .isDisplayMobEquipment -> true
            WorldGuardAdapters.instance
                .isFlagDenyAtLocation(
                    event.location, WorldGuardFlags.mythicDrops
                ) -> true
            else -> false
        }
    }
}
