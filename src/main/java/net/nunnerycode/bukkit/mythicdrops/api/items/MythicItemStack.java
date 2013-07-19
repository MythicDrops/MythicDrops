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

package net.nunnerycode.bukkit.mythicdrops.api.items;

import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
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
     * default display name, and default lore.
     *
     * @param materialData MaterialData for new ItemStack
     */
    public MythicItemStack(MaterialData materialData) {
        this(materialData, DEFAULT_AMOUNT, null, new ArrayList<String>());
    }

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
        if (displayName != null) {
            itemMeta.setDisplayName(displayName);
        }
        if (lore != null) {
            itemMeta.setLore(lore);
        }
        setItemMeta(itemMeta);
    }
}
