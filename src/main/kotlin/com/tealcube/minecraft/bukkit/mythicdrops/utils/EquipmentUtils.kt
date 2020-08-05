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
package com.tealcube.minecraft.bukkit.mythicdrops.utils

import com.tealcube.minecraft.bukkit.mythicdrops.events.EntityEquipEvent
import com.tealcube.minecraft.bukkit.mythicdrops.utils.AirUtil.isAir
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.EntityEquipment
import org.bukkit.inventory.ItemStack

object EquipmentUtils {
    /**
     * Equips a {@link org.bukkit.entity.LivingEntity} with a specified {@link
     * org.bukkit.inventory.ItemStack}.
     *
     * @param livingEntity LivingEntity to give item to
     * @param itemStack ItemStack to give to LivingEntity
     * @return if successfully gave item to LivingEntity
     */
    @JvmStatic
    fun equipEntity(livingEntity: LivingEntity?, itemStack: ItemStack?): Boolean =
        equipEntity(livingEntity, itemStack, 0.0)

    /**
     * Equips an entity with a given item and chance for said item to drop.
     *
     * @param livingEntity LivingEntity to give item to
     * @param itemStack ItemStack to give to LivingEntity
     * @param chance chance for item to drop
     * @return if successfully gave item to LivingEntity
     */
    @JvmStatic
    fun equipEntity(livingEntity: LivingEntity?, itemStack: ItemStack?, chance: Double): Boolean {
        val entityEquipment = livingEntity?.equipment
        if (livingEntity == null || itemStack == null || entityEquipment == null) {
            return false
        }
        val entityEquipEvent = EntityEquipEvent(itemStack, livingEntity)
        Bukkit.getPluginManager().callEvent(entityEquipEvent)
        return if (entityEquipEvent.isCancelled) {
            false
        } else {
            equipEntityImpl(chance, entityEquipEvent, entityEquipment, livingEntity)
            true
        }
    }

    private fun equipEntityImpl(
        chance: Double,
        entityEquipEvent: EntityEquipEvent,
        entityEquipment: EntityEquipment,
        livingEntity: LivingEntity
    ) {
        val boundChance = chance.toFloat().coerceAtLeast(0.0f).coerceAtMost(2.0f)
        val itemStackFromEvent = entityEquipEvent.itemStack
        if (itemStackFromEvent.type.name.toUpperCase().contains("BOOTS")) {
            entityEquipment.boots = itemStackFromEvent
            entityEquipment.bootsDropChance = boundChance
        } else if (itemStackFromEvent.type.name.toUpperCase().contains("LEGGINGS")) {
            entityEquipment.leggings = itemStackFromEvent
            entityEquipment.leggingsDropChance = boundChance
        } else if (itemStackFromEvent.type.name.toUpperCase().contains("CHESTPLATE")) {
            entityEquipment.chestplate = itemStackFromEvent
            entityEquipment.chestplateDropChance = boundChance
        } else if (itemStackFromEvent.type.name.toUpperCase().contains("HELMET")) {
            entityEquipment.helmet = itemStackFromEvent
            entityEquipment.helmetDropChance = boundChance
        } else if (itemStackFromEvent.type.name.toUpperCase().contains("SHIELD") ||
            !isAir(entityEquipment.itemInMainHand.type)
        ) {
            entityEquipment.setItemInOffHand(itemStackFromEvent)
            entityEquipment.itemInOffHandDropChance = boundChance
        } else {
            entityEquipment.setItemInMainHand(itemStackFromEvent)
            entityEquipment.itemInMainHandDropChance = boundChance
        }
        livingEntity.removeWhenFarAway = true
    }
}
