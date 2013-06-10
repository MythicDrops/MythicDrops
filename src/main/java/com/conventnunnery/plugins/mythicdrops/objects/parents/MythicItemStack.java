package com.conventnunnery.plugins.mythicdrops.objects.parents;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.material.MaterialData;

public class MythicItemStack extends ItemStack {

    /**
     * Instantiates a new unrepairable MythicItemStack
     *
     * @param materialData MaterialData of item to create
     */
    public MythicItemStack(MaterialData materialData) {
        super(materialData.getItemTypeId(), materialData.getData());
        ItemMeta itemMeta;
        if (hasItemMeta()) {
            itemMeta = getItemMeta();
        } else {
            itemMeta = Bukkit.getItemFactory().getItemMeta(getType());
        }
        int repairCost = 1000;
        ((Repairable) itemMeta).setRepairCost(repairCost);
        setItemMeta(itemMeta);
    }

}
