package com.conventnunnery.plugins.mythicdrops.api;

import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.Arrays;
import java.util.List;

/**
 * A class that allows for immediate creation of {@link org.bukkit.inventory.ItemStack}s with {@link ItemMeta}s.
 */
public class MythicItemStack extends NonrepairableItemStack {

    /**
     * Default amount for items
     */
    public static final int DEFAULT_AMOUNT = 1;

    /**
     * Instantiates a new {@link org.bukkit.inventory.ItemStack} with a specified {@link MaterialData}, default amount,
     * display name, and lore.
     *
     * @param materialData MaterialData for new ItemStack
     * @param displayName  Display name of the new ItemStack
     * @param lore         Lore of the new ItemStack
     */
    public MythicItemStack(MaterialData materialData, String displayName, String... lore) {
        this(materialData, DEFAULT_AMOUNT, displayName, Arrays.asList(lore));
    }

    /**
     * Instantiates a new {@link org.bukkit.inventory.ItemStack} with a specified {@link MaterialData}, amount, display
     * name, and lore.
     *
     * @param materialData MaterialData for new ItemStack
     * @param amount       Amount of items in the new ItemStack
     * @param displayName  Display name of the new ItemStack
     * @param lore         Lore of the new ItemStack
     */
    public MythicItemStack(MaterialData materialData, int amount, String displayName, String... lore) {
        this(materialData, amount, displayName, Arrays.asList(lore));
    }

    /**
     * Instantiates a new {@link org.bukkit.inventory.ItemStack} with a specified {@link MaterialData}, default amount,
     * display name, and lore.
     *
     * @param materialData MaterialData for new ItemStack
     * @param displayName  Display name of the new ItemStack
     * @param lore         Lore of the new ItemStack
     */
    public MythicItemStack(MaterialData materialData, String displayName, List<String> lore) {
        this(materialData, DEFAULT_AMOUNT, displayName, lore);
    }

    /**
     * Instantiates a new {@link org.bukkit.inventory.ItemStack} with a specified {@link MaterialData}, amount, display
     * name, and lore.
     *
     * @param materialData MaterialData for new ItemStack
     * @param amount       Amount of items in the new ItemStack
     * @param displayName  Display name of the new ItemStack
     * @param lore         Lore of the new ItemStack
     */
    public MythicItemStack(MaterialData materialData, int amount, String displayName, List<String> lore) {
        super(materialData);
        setAmount(Math.max(amount, DEFAULT_AMOUNT));
        ItemMeta itemMeta;
        if (hasItemMeta()) {
            itemMeta = getItemMeta().clone();
        } else {
            itemMeta = Bukkit.getItemFactory().getItemMeta(getType());
        }
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        setItemMeta(itemMeta);
    }
}
