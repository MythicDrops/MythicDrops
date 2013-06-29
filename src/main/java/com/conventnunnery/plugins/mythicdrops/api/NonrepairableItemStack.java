/*
 * Copyright (C) 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.mythicdrops.api;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.material.MaterialData;

/**
 * A class that makes it so that any {@link ItemStack}s created by the plugin will spawn with a certain repair cost.
 */
public class NonrepairableItemStack extends ItemStack {

    /**
     * The default repair cost is high enough that no player could ever reach it.
     */
    public static final int DEFAULT_REPAIR_COST = 1000;

    /**
     * Instantiates a new {@link ItemStack} with a certain {@link MaterialData} using the default repair cost.
     *
     * @param materialData MaterialData containing information for the ItemStack
     */
    public NonrepairableItemStack(MaterialData materialData) {
        this(materialData, DEFAULT_REPAIR_COST);
    }

    /**
     * Instantiates a new {@link ItemStack} with a certain {@link MaterialData} using a specified repair cost.
     *
     * @param materialData MaterialData containing information for the ItemStack
     * @param repairCost   Cost in levels to repair the item
     */
    public NonrepairableItemStack(MaterialData materialData, int repairCost) {
        super(materialData.getItemTypeId(), materialData.getData());
        ItemMeta itemMeta;
        if (hasItemMeta()) {
            itemMeta = getItemMeta();
        } else {
            itemMeta = Bukkit.getItemFactory().getItemMeta(getType());
        }
        ((Repairable) itemMeta).setRepairCost(Math.max(0, repairCost));
        setItemMeta(itemMeta);
    }


}
