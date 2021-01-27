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
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGenerationReason
import com.tealcube.minecraft.bukkit.mythicdrops.events.CustomItemGenerationEvent
import com.tealcube.minecraft.bukkit.mythicdrops.identification.IdentityTome
import com.tealcube.minecraft.bukkit.mythicdrops.identification.UnidentifiedItem
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicDropTracker
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.SocketExtender
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.SocketItem
import com.tealcube.minecraft.bukkit.mythicdrops.utils.GemUtil
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getMaterials
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getTier
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

// Default drop strategy for MythicDrops
internal class SingleDropStrategy(
    private val mythicDrops: MythicDrops
) : AbstractDropStrategy() {
    companion object {
        private const val ONE_HUNDRED_PERCENT = 1.0
        private const val ONE_HUNDRED_TEN_PERCENT = 1.1
    }

    override val name: String = "single"

    override val itemChance: Double
        get() =
            mythicDrops.settingsManager.configSettings.drops.itemChance

    override val tieredItemChance: Double
        get() =
            itemChance * mythicDrops.settingsManager.configSettings.drops.tieredItemChance

    override val customItemChance: Double
        get() =
            itemChance *
                (1.0 - mythicDrops.settingsManager.configSettings.drops.tieredItemChance) *
                mythicDrops.settingsManager.configSettings.drops.customItemChance

    override val socketGemChance: Double
        get() =
            itemChance *
                (1.0 - mythicDrops.settingsManager.configSettings.drops.tieredItemChance) *
                (1.0 - mythicDrops.settingsManager.configSettings.drops.customItemChance) *
                mythicDrops.settingsManager.configSettings.drops.socketGemChance

    override val unidentifiedItemChance: Double
        get() =
            itemChance *
                (1.0 - mythicDrops.settingsManager.configSettings.drops.tieredItemChance) *
                (1.0 - mythicDrops.settingsManager.configSettings.drops.customItemChance) *
                (1.0 - mythicDrops.settingsManager.configSettings.drops.socketGemChance) *
                mythicDrops.settingsManager.configSettings.drops.unidentifiedItemChance

    override val identityTomeChance: Double
        get() =
            itemChance *
                (1.0 - mythicDrops.settingsManager.configSettings.drops.tieredItemChance) *
                (1.0 - mythicDrops.settingsManager.configSettings.drops.customItemChance) *
                (1.0 - mythicDrops.settingsManager.configSettings.drops.socketGemChance) *
                (1.0 - mythicDrops.settingsManager.configSettings.drops.unidentifiedItemChance) *
                mythicDrops.settingsManager.configSettings.drops.identityTomeChance

    override val socketExtenderChance: Double
        get() =
            itemChance *
                (1.0 - mythicDrops.settingsManager.configSettings.drops.tieredItemChance) *
                (1.0 - mythicDrops.settingsManager.configSettings.drops.customItemChance) *
                (1.0 - mythicDrops.settingsManager.configSettings.drops.socketGemChance) *
                (1.0 - mythicDrops.settingsManager.configSettings.drops.unidentifiedItemChance) *
                (1.0 - mythicDrops.settingsManager.configSettings.drops.identityTomeChance) *
                mythicDrops.settingsManager.configSettings.drops.socketExtenderChance

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

        var itemStack: ItemStack? = null
        // due to the way that spigot/minecraft handles drop chances, it won't drop items with a 100% drop chance
        // if a player isn't the one that killed it.
        var dropChance = if (mythicDrops.settingsManager.configSettings.options.isRequirePlayerKillForDrops) {
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
            MythicDropTracker.tieredItem()
            val pair = getTieredDrop(entity, itemStack, dropChance)
            dropChance = pair.first
            itemStack = pair.second
        } else if (customItemRoll < customItemChance && customItemAllowedAtLocation) {
            MythicDropTracker.customItem()
            val pair = getCustomItemDrop(itemStack, dropChance)
            dropChance = pair.first
            itemStack = pair.second
        } else if (socketingEnabled && socketGemRoll <= socketGemChance && socketGemAllowedAtLocation) {
            MythicDropTracker.socketGem()
            itemStack = getSocketGemDrop(entity, itemStack)
        } else if (
            identifyingEnabled && unidentifiedItemRoll <= unidentifiedItemChance &&
            unidentifiedItemAllowedAtLocation
        ) {
            MythicDropTracker.unidentifiedItem()
            itemStack = getUnidentifiedItemDrop(itemStack, entity)
        } else if (identifyingEnabled && identityTomeRoll <= identityTomeChance && identityTomeAllowedAtLocation) {
            MythicDropTracker.identityTome()
            itemStack = IdentityTome(mythicDrops.settingsManager.identifyingSettings.items.identityTome)
        } else if (socketingEnabled && socketExtenderRoll <= socketExtenderChance && socketExtenderAllowedAtLocation) {
            mythicDrops.settingsManager.socketingSettings.options.socketExtenderMaterialIds.randomOrNull()?.let {
                MythicDropTracker.socketExtender()
                itemStack = SocketExtender(it, mythicDrops.settingsManager.socketingSettings.items.socketExtender)
            }
        }

        return itemStack?.let { listOf(it to dropChance) } ?: emptyList()
    }

    private fun getUnidentifiedItemDrop(
        itemStack: ItemStack?,
        entity: LivingEntity
    ): ItemStack? {
        val allowableTiersForEntity =
            mythicDrops.settingsManager.creatureSpawningSettings.tierDrops[entity.type] ?: emptyList()

        return mythicDrops.tierManager.randomByIdentityWeight { allowableTiersForEntity.contains(it.name) }
            ?.let { randomizedTier ->
                randomizedTier.getMaterials().randomOrNull()?.let { material ->
                    UnidentifiedItem.build(
                        mythicDrops.settingsManager.creatureSpawningSettings,
                        mythicDrops.settingsManager.languageSettings.displayNames,
                        material,
                        mythicDrops.tierManager,
                        mythicDrops.settingsManager.identifyingSettings.items.unidentifiedItem,
                        entity.type,
                        randomizedTier
                    )
                }
            } ?: itemStack
    }

    private fun getSocketGemDrop(
        entity: LivingEntity,
        itemStack: ItemStack?
    ): ItemStack? {
        var itemStack1 = itemStack
        val socketGem = GemUtil.getRandomSocketGemByWeight(entity.type)
        val material = GemUtil.getRandomSocketGemMaterial()
        if (socketGem != null && material != null) {
            itemStack1 =
                SocketItem(material, socketGem, mythicDrops.settingsManager.socketingSettings.items.socketGem)
        }
        return itemStack1
    }

    private fun getCustomItemDrop(
        itemStack: ItemStack?,
        dropChance: Double
    ): Pair<Double, ItemStack?> {
        var customItemItemStack = itemStack
        var customItemDropChance = dropChance
        mythicDrops.customItemManager.randomByWeight()?.let {
            val customItemGenerationEvent =
                CustomItemGenerationEvent(
                    it,
                    MythicDropsApi.mythicDrops.productionLine.customItemFactory.toItemStack(it)
                )
            Bukkit.getPluginManager().callEvent(customItemGenerationEvent)
            if (!customItemGenerationEvent.isCancelled) {
                customItemItemStack = customItemGenerationEvent.result
                customItemDropChance = it.chanceToDropOnDeath
            }
        }
        return Pair(customItemDropChance, customItemItemStack)
    }

    private fun getTieredDrop(
        entity: LivingEntity,
        itemStack: ItemStack?,
        dropChance: Double
    ): Pair<Double, ItemStack?> {
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
        } ?: dropChance to itemStack
    }
}
