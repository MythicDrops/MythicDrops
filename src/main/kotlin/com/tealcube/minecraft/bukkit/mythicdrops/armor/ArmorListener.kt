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
package com.tealcube.minecraft.bukkit.mythicdrops.armor

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.getThenSetItemMetaAsDamageable
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockDispenseArmorEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemBreakEvent
import org.bukkit.inventory.EntityEquipment
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

/**
 * Modified version of ArmorListener from ArmorEquipEvent.
 *
 * https://github.com/Arnuh/ArmorEquipEvent
 */
internal class ArmorListener(
    private val settingsManager: SettingsManager
) : Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onBlockDispenseArmorEvent(event: BlockDispenseArmorEvent) {
        val armorType = ArmorType.from(event.item.type)
        val player = event.targetEntity as? Player

        if (armorType == null || player == null) {
            return
        }

        val armorEquipEvent =
            ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.DISPENSER, armorType, null, event.item)
        Bukkit.getServer().pluginManager.callEvent(armorEquipEvent)

        if (armorEquipEvent.isCancelled) {
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onInventoryClickEvent(event: InventoryClickEvent) {
        val shift = event.click == ClickType.SHIFT_LEFT || event.click == ClickType.SHIFT_RIGHT
        val numberKey = event.click == ClickType.NUMBER_KEY
        val currentItem = event.currentItem
        val cursor = event.cursor
        val armorType = if (shift) {
            currentItem?.let { ArmorType.from(it.type) }
        } else {
            cursor?.let { ArmorType.from(it.type) }
        }
        val clickedInventory = event.clickedInventory
        val isNotSlotWeCareAbout = event.slotType != InventoryType.SlotType.ARMOR
        val isClickedInventoryNotPlayerInventory =
            clickedInventory == null || clickedInventory.type != InventoryType.PLAYER
        val isInventoryNotPlayerOrCrafting =
            event.inventory.type != InventoryType.PLAYER && event.inventory.type != InventoryType.CRAFTING
        val isWhoClickedNotAPlayer = event.whoClicked !is Player
        val isNotDraggingToCorrectSlot = !shift && armorType != null && event.rawSlot != armorType.slot
        val equipment = (event.whoClicked as? Player)?.equipment
        val firstCondition = event.action == InventoryAction.NOTHING ||
            isNotSlotWeCareAbout ||
            isClickedInventoryNotPlayerInventory
        val secondCondition = isInventoryNotPlayerOrCrafting ||
            isWhoClickedNotAPlayer ||
            isNotDraggingToCorrectSlot
        if (firstCondition || secondCondition || equipment == null) {
            return
        }
        val player = event.whoClicked as Player
        if (shift) {
            handleShiftInventoryClick(armorType, event, equipment, player)
        } else {
            handleNonShiftInventoryClick(
                NonShiftInventoryClickArgs(
                    cursor,
                    currentItem,
                    numberKey,
                    clickedInventory,
                    event,
                    armorType,
                    player
                )
            )
        }
    }

    @EventHandler
    fun onPlayerInteractEvent(event: PlayerInteractEvent) {
        val eventItem = event.item
        val player = event.player
        val equipment = player.equipment
        val firstCondition = event.useItemInHand() == Event.Result.DENY ||
            event.action == Action.PHYSICAL ||
            event.action == Action.LEFT_CLICK_AIR ||
            event.action == Action.LEFT_CLICK_BLOCK
        if (firstCondition || eventItem == null || equipment == null) {
            return
        }

        if (event.useInteractedBlock() != Event.Result.DENY) {
            val clickedBlock = event.clickedBlock
            if (clickedBlock != null && event.action == Action.RIGHT_CLICK_BLOCK && !player.isSneaking) {
                val material = clickedBlock.type
                if (settingsManager.armorSettings.blocked.contains(material)) {
                    // we kick out since this isn't going to be equipping armor anyway
                    return
                }
            }
        }

        val armorType = ArmorType.from(eventItem.type)
        if (armorType != null) {
            val (isEquippingHelmet, isEquippingChestplate) = determineIfEquippingHelmetOrChestplate(
                armorType,
                equipment
            )
            val (isEquippingLeggings, isEquippingBoots) = determineIfEquippingLeggingsOrBoots(armorType, equipment)
            val isTriggerEvent = isEquippingHelmet || isEquippingChestplate || isEquippingLeggings || isEquippingBoots
            if (isTriggerEvent) {
                val armorEquipEvent =
                    ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.HOTBAR_INTERACT, armorType, null, eventItem)
                Bukkit.getServer().pluginManager.callEvent(armorEquipEvent)
                if (armorEquipEvent.isCancelled) {
                    event.isCancelled = true
                    player.updateInventory()
                }
            }
        }
    }

    @EventHandler
    fun onInventoryDragEvent(event: InventoryDragEvent) {
        val player = event.whoClicked as? Player
        val armorType = ArmorType.from(event.oldCursor.type)
        val matchingSlot = event.rawSlots.find { armorType?.slot == it }
        if (matchingSlot == null || armorType == null || player == null) return
        val armorEquipEvent = ArmorEquipEvent(
            player,
            ArmorEquipEvent.EquipMethod.DEATH,
            armorType,
            null,
            event.oldCursor
        )
        Bukkit.getServer().pluginManager.callEvent(armorEquipEvent)

        if (armorEquipEvent.isCancelled) {
            event.result = Event.Result.DENY
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerItemBreakEvent(event: PlayerItemBreakEvent) {
        val armorType = ArmorType.from(event.brokenItem.type) ?: return
        val player = event.player
        val armorEquipEvent = ArmorEquipEvent(
            player,
            ArmorEquipEvent.EquipMethod.DEATH,
            armorType,
            event.brokenItem,
            null
        )
        Bukkit.getServer().pluginManager.callEvent(armorEquipEvent)

        if (!armorEquipEvent.isCancelled) {
            return
        }
        val unbrokenItem = event.brokenItem.clone()
        unbrokenItem.amount = 1
        unbrokenItem.getThenSetItemMetaAsDamageable {
            damage = (damage - 1).coerceAtLeast(0).coerceAtMost(unbrokenItem.type.maxDurability.toInt())
        }
        when (armorType) {
            ArmorType.HELMET -> {
                player.equipment?.helmet = unbrokenItem
            }
            ArmorType.CHESTPLATE -> {
                player.equipment?.chestplate = unbrokenItem
            }
            ArmorType.LEGGINGS -> {
                player.equipment?.leggings = unbrokenItem
            }
            ArmorType.BOOTS -> {
                player.equipment?.boots = unbrokenItem
            }
        }
    }

    @EventHandler
    fun onPlayerDeathEvent(event: PlayerDeathEvent) {
        if (event.keepInventory) return
        val player = event.entity
        player.inventory.armorContents.filterNotNull().filterNot { it.type.isAir }.forEach { itemStack ->
            ArmorType.from(itemStack.type)?.let {
                Bukkit.getServer().pluginManager.callEvent(
                    ArmorEquipEvent(
                        player,
                        ArmorEquipEvent.EquipMethod.DEATH,
                        it,
                        itemStack,
                        null
                    )
                )
            }
        }
    }

    private fun handleNonShiftInventoryClick(
        nonShiftInventoryClickArgs: NonShiftInventoryClickArgs
    ) {
        val (cursor, currentItem, numberKey, clickedInventory, event, armorType, player) = nonShiftInventoryClickArgs
        var newArmorType = armorType
        var newArmorPiece = cursor
        var oldArmorPiece = currentItem

        if (numberKey) {
            if (clickedInventory?.type == InventoryType.PLAYER) { // no crafting 2x2
                val hotbarItem = clickedInventory.getItem(event.hotbarButton)
                if (!isAirOrNull(hotbarItem)) {
                    newArmorType = hotbarItem?.let { ArmorType.from(it.type) }
                    oldArmorPiece = clickedInventory.getItem(event.slot)
                    newArmorPiece = hotbarItem
                } else {
                    newArmorType = if (!isAirOrNull(currentItem)) {
                        ArmorType.from(currentItem?.type)
                    } else {
                        ArmorType.from(cursor?.type)
                    }
                }
            }
        } else {
            if (isAirOrNull(cursor) && !isAirOrNull(currentItem)) {
                newArmorType = ArmorType.from(currentItem?.type)
            }
        }

        if (newArmorType != null && event.rawSlot == newArmorType.slot) {
            val method = if (event.action == InventoryAction.HOTBAR_SWAP || numberKey) {
                ArmorEquipEvent.EquipMethod.HOTBAR_SWAP
            } else {
                ArmorEquipEvent.EquipMethod.CLICK
            }
            val armorEquipEvent = ArmorEquipEvent(player, method, newArmorType, oldArmorPiece, newArmorPiece)
            Bukkit.getServer().pluginManager.callEvent(armorEquipEvent)
            if (armorEquipEvent.isCancelled) {
                event.isCancelled = true
            }
        }
    }

    private fun handleShiftInventoryClick(
        armorType: ArmorType?,
        event: InventoryClickEvent,
        equipment: EntityEquipment,
        player: Player
    ) = armorType?.let {
        val equipping = event.rawSlot != it.slot
        val (isEquippingHelmet, isEquippingChestplate) = determineIfEquippingHelmetOrChestplate(
            armorType,
            equipment
        )
        val (isEquippingLeggings, isEquippingBoots) = determineIfEquippingLeggingsOrBoots(armorType, equipment)
        val isTriggerEvent = isEquippingHelmet || isEquippingChestplate || isEquippingLeggings || isEquippingBoots
        if (isTriggerEvent) {
            val oldArmorPiece = if (equipping) {
                null
            } else {
                event.currentItem
            }
            val newArmorPiece = if (equipping) {
                event.currentItem
            } else {
                null
            }
            val armorEquipEvent =
                ArmorEquipEvent(
                    player,
                    ArmorEquipEvent.EquipMethod.SHIFT_CLICK,
                    armorType,
                    oldArmorPiece,
                    newArmorPiece
                )
            Bukkit.getServer().pluginManager.callEvent(armorEquipEvent)
            if (armorEquipEvent.isCancelled) {
                event.isCancelled = true
                player.updateInventory()
            }
        }
    }

    private fun determineIfEquippingLeggingsOrBoots(
        armorType: ArmorType?,
        equipment: EntityEquipment
    ): Pair<Boolean, Boolean> {
        val isEquippingLeggings = armorType == ArmorType.LEGGINGS && isAirOrNull(equipment.leggings)
        val isEquippingBoots = armorType == ArmorType.BOOTS && isAirOrNull(equipment.boots)
        return Pair(isEquippingLeggings, isEquippingBoots)
    }

    private fun determineIfEquippingHelmetOrChestplate(
        armorType: ArmorType?,
        equipment: EntityEquipment
    ): Pair<Boolean, Boolean> {
        val isEquippingHelmet = armorType == ArmorType.HELMET && isAirOrNull(equipment.helmet)
        val isEquippingChestplate = armorType == ArmorType.CHESTPLATE && isAirOrNull(equipment.chestplate)
        return Pair(isEquippingHelmet, isEquippingChestplate)
    }

    private fun isAirOrNull(item: ItemStack?) = item == null || item.type.isAir

    private data class NonShiftInventoryClickArgs(
        val cursor: ItemStack?,
        val currentItem: ItemStack?,
        val numberKey: Boolean,
        val clickedInventory: Inventory?,
        val event: InventoryClickEvent,
        val armorType: ArmorType?,
        val player: Player
    )
}
