package com.tealcube.minecraft.bukkit.mythicdrops.utils;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby
 * granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER
 * IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


import com.tealcube.minecraft.bukkit.mythicdrops.events.EntityEquipEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

/**
 * A utility class containing various methods dealing with entities.
 */
public final class EntityUtil {

    private EntityUtil() {
        // do nothing
    }

    /**
     * Equips a {@link org.bukkit.entity.LivingEntity} with a specified {@link org.bukkit.inventory.ItemStack}.
     *
     * @param livingEntity
     *         LivingEntity to give item to
     * @param is
     *         ItemStack to give to LivingEntity
     * @return if successfully gave item to LivingEntity
     */
    public static boolean equipEntity(LivingEntity livingEntity, ItemStack is) {
        return equipEntity(livingEntity, is, 0.0);
    }

    /**
     * Equips an entity with a given item and chance for said item to drop.
     *
     * @param livingEntity
     *         LivingEntity to give item to
     * @param is
     *         ItemStack to give to LivingEntity
     * @param chance
     *         chance for item to drop
     * @return if successfully gave item to LivingEntity
     */
    public static boolean equipEntity(LivingEntity livingEntity, ItemStack is, double chance) {
        if (livingEntity == null || is == null) {
            return false;
        }
        EntityEquipEvent entityEquipEvent = new EntityEquipEvent(is, livingEntity);
        Bukkit.getPluginManager().callEvent(entityEquipEvent);
        if (entityEquipEvent.isCancelled()) {
            return false;
        }
        ItemStack itemStack = entityEquipEvent.getItemStack();
        if (itemStack == null) {
            return false;
        }
        if (itemStack.getType().name().toUpperCase().contains("BOOTS")) {
            livingEntity.getEquipment().setBoots(itemStack);
            livingEntity.getEquipment().setBootsDropChance((float) chance);
        } else if (itemStack.getType().name().toUpperCase().contains("LEGGINGS")) {
            livingEntity.getEquipment().setLeggings(itemStack);
            livingEntity.getEquipment().setLeggingsDropChance((float) chance);
        } else if (itemStack.getType().name().toUpperCase().contains("CHESTPLATE")) {
            livingEntity.getEquipment().setChestplate(itemStack);
            livingEntity.getEquipment().setChestplateDropChance((float) chance);
        } else if (itemStack.getType().name().toUpperCase().contains("HELMET")) {
            livingEntity.getEquipment().setHelmet(itemStack);
            livingEntity.getEquipment().setHelmetDropChance((float) chance);
        } else {
            livingEntity.getEquipment().setItemInHand(itemStack);
            livingEntity.getEquipment().setItemInHandDropChance((float) chance);
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

}
