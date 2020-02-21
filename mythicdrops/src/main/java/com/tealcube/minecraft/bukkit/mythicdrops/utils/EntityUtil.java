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
package com.tealcube.minecraft.bukkit.mythicdrops.utils;

import com.tealcube.minecraft.bukkit.mythicdrops.events.EntityEquipEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/** A utility class containing various methods dealing with entities. */
public final class EntityUtil {

  private EntityUtil() {
    // do nothing
  }

  /**
   * Equips a {@link org.bukkit.entity.LivingEntity} with a specified {@link
   * org.bukkit.inventory.ItemStack}.
   *
   * @param livingEntity LivingEntity to give item to
   * @param is ItemStack to give to LivingEntity
   * @return if successfully gave item to LivingEntity
   */
  public static boolean equipEntity(LivingEntity livingEntity, ItemStack is) {
    return equipEntity(livingEntity, is, 0.0);
  }

  /**
   * Equips an entity with a given item and chance for said item to drop.
   *
   * @param livingEntity LivingEntity to give item to
   * @param is ItemStack to give to LivingEntity
   * @param chance chance for item to drop
   * @return if successfully gave item to LivingEntity
   */
  public static boolean equipEntity(LivingEntity livingEntity, ItemStack is, double chance) {
    if (livingEntity == null || is == null) {
      return false;
    }
    EntityEquipment entityEquipment = livingEntity.getEquipment();
    if (entityEquipment == null) {
      return false;
    }
    EntityEquipEvent entityEquipEvent = new EntityEquipEvent(is, livingEntity);
    Bukkit.getPluginManager().callEvent(entityEquipEvent);
    if (entityEquipEvent.isCancelled()) {
      return false;
    }
    float boundChance = Math.min(Math.max((float) chance, 0.0F), 1.0F);
    ItemStack itemStack = entityEquipEvent.getItemStack();
    if (itemStack.getType().name().toUpperCase().contains("BOOTS")) {
      livingEntity.getEquipment().setBoots(itemStack);
      livingEntity.getEquipment().setBootsDropChance(boundChance);
    } else if (itemStack.getType().name().toUpperCase().contains("LEGGINGS")) {
      livingEntity.getEquipment().setLeggings(itemStack);
      livingEntity.getEquipment().setLeggingsDropChance(boundChance);
    } else if (itemStack.getType().name().toUpperCase().contains("CHESTPLATE")) {
      livingEntity.getEquipment().setChestplate(itemStack);
      livingEntity.getEquipment().setChestplateDropChance(boundChance);
    } else if (itemStack.getType().name().toUpperCase().contains("HELMET")) {
      livingEntity.getEquipment().setHelmet(itemStack);
      livingEntity.getEquipment().setHelmetDropChance(boundChance);
    } else if (itemStack.getType().name().toUpperCase().contains("SHIELD")
        || !AirUtil.INSTANCE.isAir(livingEntity.getEquipment().getItemInMainHand().getType())) {
      livingEntity.getEquipment().setItemInOffHand(itemStack);
      livingEntity.getEquipment().setItemInOffHandDropChance(boundChance);
    } else {
      livingEntity.getEquipment().setItemInMainHand(itemStack);
      livingEntity.getEquipment().setItemInMainHandDropChance(boundChance);
    }
    livingEntity.setRemoveWhenFarAway(true);
    return true;
  }

  public static Entity getEntityAtLocation(Location location) {
    if (location == null) {
      return null;
    }
    World w = location.getWorld();
    for (Entity entity : w.getEntities()) {
      if (entity.getLocation().equals(location)) {
        return entity;
      }
    }
    return null;
  }

  public static EntityType getEntityType(String str) {
    try {
      return EntityType.valueOf(str);
    } catch (Exception ex) {
      return null;
    }
  }
}
