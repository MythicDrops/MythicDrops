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
import com.tealcube.minecraft.bukkit.mythicdrops.events.CustomItemGenerationEvent
import com.tealcube.minecraft.bukkit.mythicdrops.getThenSetItemMetaAsDamageable
import com.tealcube.minecraft.bukkit.mythicdrops.identification.IdentityTome
import com.tealcube.minecraft.bukkit.mythicdrops.identification.UnidentifiedItem
import com.tealcube.minecraft.bukkit.mythicdrops.items.builders.MythicDropBuilder
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.SocketItem
import com.tealcube.minecraft.bukkit.mythicdrops.utils.AirUtil.isAir
import com.tealcube.minecraft.bukkit.mythicdrops.utils.BroadcastMessageUtil.broadcastItem
import com.tealcube.minecraft.bukkit.mythicdrops.utils.CustomItemUtil.getCustomItemFromItemStack
import com.tealcube.minecraft.bukkit.mythicdrops.utils.GemUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemStackUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TierUtil
import com.tealcube.minecraft.bukkit.mythicdrops.worldguard.WorldGuardFlags
import com.tealcube.minecraft.spigot.worldguard.adapters.lib.WorldGuardAdapters
import org.apache.commons.lang3.RandomUtils
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.EntityEquipment
import org.bukkit.inventory.ItemStack

class ItemDroppingListener(private val mythicDrops: MythicDrops) : Listener {
    companion object {
        private val logger = JulLoggerFactory.getLogger(ItemDroppingListener::class)
        private const val ZERO = 0
        private const val ONE = 1
        private const val TWO = 2
        private const val THREE = 3
        private const val FOUR = 4
        private const val FIVE = 5
    }

    @EventHandler
    fun onEntityDeathEvent(event: EntityDeathEvent) {
        if (isShouldCancelDrops(event)) return

        if (mythicDrops.settingsManager.configSettings.options.isDisplayMobEquipment) {
            handleEntityDeathEventWithGive(event)
        } else {
            handleEntityDeathEventWithoutGive(event)
        }
    }

    private fun handleEntityDeathEventWithGive(event: EntityDeathEvent) {
        val entityEquipment = event.entity.equipment ?: return
        val itemsToIterateThrough =
            entityEquipment.armorContents.toList() + entityEquipment.itemInMainHand + entityEquipment.itemInOffHand
        itemsToIterateThrough.forEachIndexed { idx, item ->
            val idxOfItemInDrops = event.drops.indexOf(item)

            if (idxOfItemInDrops == -1) {
                return@forEachIndexed
            }

            val dropChanceForSlot = getDropChanceForEquipmentSlot(idx, entityEquipment)

            if (dropChanceForSlot > 1.0F) {
                return@forEachIndexed
            }

            // check if custom item and announce
            getCustomItemFromItemStack(item)?.let {
                // TODO: set durability
                logger.fine("entity equipment $idx customItem=$it")
                if (it.isBroadcastOnFind && event.entity.killer != null) {
                    broadcastItem(
                        mythicDrops.settingsManager.languageSettings,
                        event.entity.killer,
                        item
                    )
                }
            }
            // check if tier and announce
            TierUtil.getTierFromItemStack(item)?.let {
                event.drops[idxOfItemInDrops] = item.clone().apply {
                    val durabilityValue = ItemStackUtil.getDurabilityForMaterial(
                        type,
                        it.minimumDurabilityPercentage,
                        it.maximumDurabilityPercentage
                    )
                    getThenSetItemMetaAsDamageable(
                        { damage = durabilityValue },
                        { durability = durabilityValue.toShort() })
                }
                if (it.isBroadcastOnFind && event.entity.killer != null) {
                    broadcastItem(
                        mythicDrops.settingsManager.languageSettings,
                        event.entity.killer,
                        item
                    )
                }
            }
        }
    }

    private fun getDropChanceForEquipmentSlot(slot: Int, entityEquipment: EntityEquipment): Float = when (slot) {
        ZERO -> entityEquipment.helmetDropChance
        ONE -> entityEquipment.chestplateDropChance
        TWO -> entityEquipment.leggingsDropChance
        THREE -> entityEquipment.bootsDropChance
        FOUR -> entityEquipment.itemInMainHandDropChance
        FIVE -> entityEquipment.itemInOffHandDropChance
        else -> 0.0F
    }

    private fun handleEntityDeathEventWithoutGive(event: EntityDeathEvent) {
        val itemChance = mythicDrops.settingsManager.configSettings.drops.itemChance
        val creatureSpawningMultiplier = mythicDrops
            .settingsManager
            .creatureSpawningSettings
            .dropMultipliers[event.entity.type] ?: 0.0
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
        var dropChance = 1.0

        // this is here to maintain previous behavior
        if (tieredItemRoll <= tieredItemChanceMultiplied && WorldGuardAdapters.instance.isFlagAllowAtLocation(
                event.entity.location,
                WorldGuardFlags.mythicDropsTiered
            )
        ) {
            TierUtil.getTierForLivingEntity(
                event.entity, mythicDrops.settingsManager.creatureSpawningSettings,
                mythicDrops.tierManager
            )?.also {
                itemStack = MythicDropBuilder(mythicDrops).withItemGenerationReason(ItemGenerationReason.MONSTER_SPAWN)
                    .useDurability(false).withTier(it).build()
                dropChance = it.chanceToDropOnMonsterDeath
            }
        } else if (customItemRoll < customItemChance && WorldGuardAdapters.instance.isFlagAllowAtLocation(
                event.entity.location,
                WorldGuardFlags.mythicDropsCustom
            )
        ) {
            mythicDrops.customItemManager.randomByWeight()?.let {
                val customItemGenerationEvent =
                    CustomItemGenerationEvent(it)
                Bukkit.getPluginManager().callEvent(customItemGenerationEvent)
                if (!customItemGenerationEvent.isCancelled) {
                    itemStack = customItemGenerationEvent.result
                    dropChance = it.chanceToDropOnDeath
                }
            }
        } else if (socketingEnabled && socketGemRoll <= socketGemChance &&
            WorldGuardAdapters.instance.isFlagAllowAtLocation(
                event.entity.location,
                WorldGuardFlags.mythicDropsSocketGem
            )
        ) {
            val socketGem = GemUtil.getRandomSocketGemByWeight(event.entityType)
            val material = GemUtil.getRandomSocketGemMaterial()
            if (socketGem != null && material != null) {
                itemStack =
                    SocketItem(material, socketGem, mythicDrops.settingsManager.socketingSettings.items.socketGem)
            }
        } else if (identifyingEnabled && unidentifiedItemRoll <= unidentifiedItemChance &&
            WorldGuardAdapters.instance.isFlagAllowAtLocation(
                event.entity.location,
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
                            event.entityType,
                            randomizedTier
                        )
                    }
            }
        } else if (identifyingEnabled && identityTomeRoll <= identityTomeChance &&
            WorldGuardAdapters.instance.isFlagAllowAtLocation(
                event.entity.location,
                WorldGuardFlags.mythicDropsIdentityTome
            )
        ) {
            itemStack = IdentityTome(mythicDrops.settingsManager.identifyingSettings.items.identityTome)
        }

        itemStack?.let {
            if (it.amount > 0 && !isAir(it.type) && RandomUtils.nextDouble(0.0, 1.0) <= dropChance) {
                event.drops.add(it)
            }
        }
    }

    private fun isShouldCancelDrops(event: EntityDeathEvent): Boolean {
        return when {
            event.entity is Player -> true
            event.entity.lastDamageCause == null -> true
            event.entity.lastDamageCause?.isCancelled == true -> true // :|
            !mythicDrops
                .settingsManager
                .configSettings
                .multiworld
                .enabledWorlds
                .contains(event.entity.world.name) -> true
            mythicDrops
                .settingsManager
                .configSettings
                .options
                .isRequirePlayerKillForDrops
                && event.entity.killer == null -> true
            else -> false
        }
    }
}
