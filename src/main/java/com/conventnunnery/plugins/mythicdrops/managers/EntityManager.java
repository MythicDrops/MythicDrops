/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.mythicdrops.managers;

import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.events.CreatureEquippedWithItemStackEvent;
import com.conventnunnery.plugins.mythicdrops.objects.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

/**
 * The type EntityManager.
 */
public class EntityManager {
    private final MythicDrops plugin;

    /**
     * Instantiates a new EntityManager.
     *
     * @param plugin the plugin
     */
    public EntityManager(MythicDrops plugin) {
        this.plugin = plugin;
    }

    public void equipEntity(LivingEntity entity, CustomItem customItem) {
        if (entity == null || customItem == null) {
            return;
        }
        ItemStack itemstack = customItem.toItemStack();
        CreatureEquippedWithItemStackEvent cewise = new CreatureEquippedWithItemStackEvent(entity, itemstack);
        Bukkit.getPluginManager().callEvent(cewise);
        if (cewise.isCancelled()) {
            return;
        }
        itemstack = cewise.getItemStack();
        if (itemstack.getType().name().toUpperCase().contains("BOOTS")) {
            cewise.getEntity().getEquipment().setBoots(itemstack);
        } else if (itemstack.getType().name().toUpperCase().contains("LEGGINGS")) {
            cewise.getEntity().getEquipment().setLeggings(itemstack);
        } else if (itemstack.getType().name().toUpperCase().contains("CHESTPLATE")) {
            cewise.getEntity().getEquipment().setChestplate(itemstack);
        } else if (itemstack.getType().name().toUpperCase().contains("HELMET")) {
            cewise.getEntity().getEquipment().setHelmet(itemstack);
        } else {
            cewise.getEntity().getEquipment().setItemInHand(itemstack);
        }
    }

    /**
     * Equip entity.
     *
     * @param entity    the entity
     * @param itemstack the itemstack
     */
    public void equipEntity(LivingEntity entity, ItemStack itemstack) {
        if (entity == null || itemstack == null) {
            return;
        }
        CreatureEquippedWithItemStackEvent cewise = new CreatureEquippedWithItemStackEvent(entity, itemstack);
        Bukkit.getPluginManager().callEvent(cewise);
        if (cewise.isCancelled()) {
            return;
        }
        itemstack = cewise.getItemStack();
        if (itemstack.getType().name().toUpperCase().contains("BOOTS")) {
            cewise.getEntity().getEquipment().setBoots(itemstack);
        } else if (itemstack.getType().name().toUpperCase().contains("LEGGINGS")) {
            cewise.getEntity().getEquipment().setLeggings(itemstack);
        } else if (itemstack.getType().name().toUpperCase().contains("CHESTPLATE")) {
            cewise.getEntity().getEquipment().setChestplate(itemstack);
        } else if (itemstack.getType().name().toUpperCase().contains("HELMET")) {
            cewise.getEntity().getEquipment().setHelmet(itemstack);
        } else {
            cewise.getEntity().getEquipment().setItemInHand(itemstack);
        }
    }

    /**
     * Gets plugin.
     *
     * @return the plugin
     */
    public MythicDrops getPlugin() {
        return plugin;
    }
}
