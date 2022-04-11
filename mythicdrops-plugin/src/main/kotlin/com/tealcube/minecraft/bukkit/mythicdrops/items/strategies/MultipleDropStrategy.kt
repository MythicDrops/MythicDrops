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
package com.tealcube.minecraft.bukkit.mythicdrops.items.strategies

import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi
import com.tealcube.minecraft.bukkit.mythicdrops.api.events.CustomItemGenerationEvent
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGenerationReason
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicDropTracker
import com.tealcube.minecraft.bukkit.mythicdrops.utils.GemUtil
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getMaterials
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getTier
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import org.koin.core.annotation.Single
import kotlin.random.Random

// New drop strategy for MythicDrops
@Single
internal class MultipleDropStrategy(
    private val mythicDrops: MythicDrops
) : AbstractDropStrategy() {
    companion object {
        private const val ONE_HUNDRED_PERCENT = 1.0
        private const val ONE_HUNDRED_TEN_PERCENT = 1.1
    }

    override val name: String = "multiple"

    override val itemChance: Double
        get() =
            mythicDrops.settingsManager.configSettings.drops.itemChance

    override val tieredItemChance: Double
        get() =
            itemChance * mythicDrops.settingsManager.configSettings.drops.tieredItemChance

    override val customItemChance: Double
        get() =
            itemChance * mythicDrops.settingsManager.configSettings.drops.customItemChance

    override val socketGemChance: Double
        get() =
            itemChance * mythicDrops.settingsManager.configSettings.drops.socketGemChance

    override val unidentifiedItemChance: Double
        get() =
            itemChance * mythicDrops.settingsManager.configSettings.drops.unidentifiedItemChance

    override val identityTomeChance: Double
        get() =
            itemChance * mythicDrops.settingsManager.configSettings.drops.identityTomeChance

    override val socketExtenderChance: Double
        get() =
            itemChance * mythicDrops.settingsManager.configSettings.drops.socketExtenderChance

    override fun getDropsForCreatureSpawnEvent(event: CreatureSpawnEvent): List<Pair<ItemStack, Double>> {
        val entity = event.entity
        val location = event.location
        return getDropsForEntityAndLocation(entity, location)
    }

    override fun getDropsForEntityDeathEvent(event: EntityDeathEvent): List<Pair<ItemStack, Double>> {
        val entity = event.entity
        val location = entity.location
        return getDropsForEntityAndLocation(entity, location)
    }

    @Suppress("detekt.ComplexMethod", "detekt.LongMethod")
    private fun getDropsForEntityAndLocation(
        entity: LivingEntity,
        location: Location
    ): List<Pair<ItemStack, Double>> {
        val itemChance = mythicDrops.settingsManager.configSettings.drops.itemChance
        val creatureSpawningMultiplier = mythicDrops
            .settingsManager
            .creatureSpawningSettings
            .dropMultipliers[entity.type] ?: 0.0
        val itemChanceMultiplied = itemChance * creatureSpawningMultiplier
        val itemRoll = Random.nextDouble(0.0, 1.0)

        if (itemRoll > itemChanceMultiplied) {
            return emptyList()
        }

        MythicDropTracker.item()
        val (
            tieredItemChance,
            customItemChance,
            socketGemChance,
            unidentifiedItemChance,
            identityTomeChance,
            socketExtenderChance
        ) = getDropChances(
            mythicDrops.settingsManager.configSettings.drops
        )

        val socketingEnabled = mythicDrops.settingsManager.configSettings.components.isSocketingEnabled
        val identifyingEnabled = mythicDrops.settingsManager.configSettings.components.isIdentifyingEnabled

        val tieredItemRoll = Random.nextDouble(0.0, 1.0)
        val customItemRoll = Random.nextDouble(0.0, 1.0)
        val socketGemRoll = Random.nextDouble(0.0, 1.0)
        val unidentifiedItemRoll = Random.nextDouble(0.0, 1.0)
        val identityTomeRoll = Random.nextDouble(0.0, 1.0)
        val socketExtenderRoll = Random.nextDouble(0.0, 1.0)

        // this is here to maintain previous behavior
        val tieredItemChanceMultiplied = tieredItemChance * creatureSpawningMultiplier

        val drops = mutableListOf<Pair<ItemStack, Double>>()
        // due to the way that spigot/minecraft handles drop chances, it won't drop items with a 100% drop chance
        // if a player isn't the one that killed it.
        val defaultDropChance = if (mythicDrops.settingsManager.configSettings.options.isRequirePlayerKillForDrops) {
            ONE_HUNDRED_PERCENT
        } else {
            ONE_HUNDRED_TEN_PERCENT
        }

        // check WorldGuard flags
        val (
            tieredAllowedAtLocation,
            customItemAllowedAtLocation,
            socketGemAllowedAtLocation,
            unidentifiedItemAllowedAtLocation,
            identityTomeAllowedAtLocation,
            socketExtenderAllowedAtLocation
        ) = getWorldGuardFlags(location)

        if (tieredItemRoll <= tieredItemChanceMultiplied && tieredAllowedAtLocation) {
            getTieredDrop(entity)?.let {
                MythicDropTracker.tieredItem()
                val dropChance = it.first
                val itemStack = it.second
                if (itemStack != null) {
                    drops.add(itemStack to dropChance)
                }
            }
        }
        if (customItemRoll < customItemChance && customItemAllowedAtLocation) {
            getCustomItemDrop()?.let {
                MythicDropTracker.customItem()
                val dropChance = it.first
                val itemStack = it.second
                if (itemStack != null) {
                    drops.add(itemStack to dropChance)
                }
            }
        }
        if (socketingEnabled && socketGemRoll <= socketGemChance && socketGemAllowedAtLocation) {
            getSocketGemDrop(entity)?.let {
                MythicDropTracker.socketGem()
                drops.add(it to defaultDropChance)
            }
        }
        if (
            identifyingEnabled && unidentifiedItemRoll <= unidentifiedItemChance &&
            unidentifiedItemAllowedAtLocation
        ) {
            getUnidentifiedItemDrop(entity)?.let {
                MythicDropTracker.unidentifiedItem()
                drops.add(it to defaultDropChance)
            }
        }
        if (identifyingEnabled && identityTomeRoll <= identityTomeChance && identityTomeAllowedAtLocation) {
            MythicDropTracker.identityTome()
            drops.add(
                MythicDropsApi.mythicDrops.productionLine.identificationItemFactory
                    .buildIdentityTome() to defaultDropChance
            )
        }
        if (socketingEnabled && socketExtenderRoll <= socketExtenderChance && socketExtenderAllowedAtLocation) {
            MythicDropsApi.mythicDrops.productionLine.socketGemItemFactory.buildSocketExtender()?.let {
                MythicDropTracker.socketExtender()
                drops.add(
                    it to defaultDropChance
                )
            }
        }

        return drops.toList()
    }

    private fun getUnidentifiedItemDrop(
        entity: LivingEntity
    ): ItemStack? {
        val allowableTiersForEntity =
            mythicDrops.settingsManager.creatureSpawningSettings.tierDrops[entity.type] ?: emptyList()

        return mythicDrops.tierManager.randomByIdentityWeight { allowableTiersForEntity.contains(it.name) }
            ?.let { randomizedTier ->
                randomizedTier.getMaterials().randomOrNull()?.let { material ->
                    MythicDropsApi.mythicDrops.productionLine.identificationItemFactory.buildUnidentifiedItem(
                        material,
                        entity.type,
                        randomizedTier
                    )
                }
            }
    }

    private fun getSocketGemDrop(
        entity: LivingEntity
    ): ItemStack? {
        val socketGem = GemUtil.getRandomSocketGemByWeight(entity.type)
        val material = GemUtil.getRandomSocketGemMaterial()
        return if (socketGem != null && material != null) {
            MythicDropsApi.mythicDrops.productionLine.socketGemItemFactory.toItemStack(socketGem)
        } else {
            null
        }
    }

    private fun getCustomItemDrop(): Pair<Double, ItemStack?>? {
        return mythicDrops.customItemManager.randomByWeight()?.let {
            val customItemGenerationEvent =
                CustomItemGenerationEvent(
                    it,
                    MythicDropsApi.mythicDrops.productionLine.customItemFactory.toItemStack(it)
                )
            Bukkit.getPluginManager().callEvent(customItemGenerationEvent)
            if (!customItemGenerationEvent.isCancelled) {
                it.chanceToDropOnDeath to customItemGenerationEvent.result
            } else {
                null
            }
        }
    }

    private fun getTieredDrop(
        entity: LivingEntity
    ): Pair<Double, ItemStack?>? {
        return entity.getTier(
            mythicDrops.settingsManager.creatureSpawningSettings,
            mythicDrops.tierManager
        )?.let {
            it.chanceToDropOnMonsterDeath to MythicDropsApi.mythicDrops.productionLine.tieredItemFactory
                .getNewDropBuilder()
                .withItemGenerationReason(ItemGenerationReason.MONSTER_SPAWN)
                .useDurability(false)
                .withTier(it)
                .build()
        }
    }
}
