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
