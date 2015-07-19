package com.tealcube.minecraft.bukkit.mythicdrops.api.items;

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


import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A wrapper around ItemStack that enables immediate ItemMeta access.
 */
public class MythicItemStack extends ItemStack {

    /**
     * Instantiates an ItemStack.
     *
     * @param type
     *         material
     */
    @Deprecated
    public MythicItemStack(MaterialData type) {
        this(type.getItemType(), 1, (short) 0, null, null, null);
    }

    /**
     * Instantiates an ItemStack with a display name, lore, and enchantments.
     *
     * @param type
     *         material
     * @param amount
     *         amount
     * @param durability
     *         damage / durability
     * @param displayName
     *         name of item
     * @param lore
     *         lore for item
     * @param enchantments
     *         enchantments for item
     */
    public MythicItemStack(Material type, int amount, short durability, String displayName,
                           List<String> lore,
                           Map<Enchantment, Integer> enchantments) {
        super(type);
        setAmount(amount);
        setDurability(durability);
        ItemMeta
                itemMeta =
                hasItemMeta() ? getItemMeta() : Bukkit.getItemFactory().getItemMeta(getType());
        Validate.notNull(itemMeta, "ItemMeta cannot be null");
        itemMeta.setDisplayName(
                displayName != null ? displayName.replace('&', '\u00A7').replace("\u00A7\u00A7",
                                                                                 "&") : null
                               );
        List<String> coloredLore = new ArrayList<>();
        if (lore != null) {
            for (String s : lore) {
                coloredLore.add(s.replace('&', '\u00A7').replace("\u00A7\u00A7", "&"));
            }
        }
        itemMeta.setLore(coloredLore);
        if (enchantments != null) {
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                itemMeta.addEnchant(entry.getKey(), entry.getValue(), true);
            }
        }
        setItemMeta(itemMeta);
    }

    /**
     * Instantiates an ItemStack with a display name, lore, and enchantments.
     *
     * @param type
     *         material
     * @param amount
     *         amount
     * @param durability
     *         damage / durability
     * @param displayName
     *         name of item
     * @param lore
     *         lore for item
     * @param enchantments
     *         enchantments for item
     */
    @Deprecated
    public MythicItemStack(MaterialData type, int amount, short durability, String displayName,
                           List<String> lore,
                           Map<Enchantment, Integer> enchantments) {
        this(type.getItemType(), amount, durability, displayName, lore, enchantments);
    }

    /**
     * Instantiates an ItemStack.
     *
     * @param type
     *         material
     * @param durability
     *         damage / durability
     */
    @Deprecated
    public MythicItemStack(MaterialData type, short durability) {
        this(type.getItemType(), 1, durability, null, null, null);
    }

    /**
     * Instantiates an ItemStack.
     *
     * @param type
     *         material
     * @param amount
     *         amount
     */
    @Deprecated
    public MythicItemStack(MaterialData type, int amount) {
        this(type.getItemType(), amount, (short) 0, null, null, null);
    }

    /**
     * Instantiates an ItemStack with a display name.
     *
     * @param type
     *         material
     * @param amount
     *         amount
     * @param durability
     *         damage / durability
     * @param displayName
     *         name of item
     */
    @Deprecated
    public MythicItemStack(MaterialData type, int amount, short durability, String displayName) {
        this(type.getItemType(), amount, durability, displayName, new ArrayList<String>(),
             new HashMap<Enchantment, Integer>());
    }

    /**
     * Instantiates an ItemStack with lore.
     *
     * @param type
     *         material
     * @param amount
     *         amount
     * @param durability
     *         damage / durability
     * @param lore
     *         lore for item
     */
    @Deprecated
    public MythicItemStack(MaterialData type, int amount, short durability, List<String> lore) {
        this(type.getItemType(), amount, durability, null, lore, new HashMap<Enchantment, Integer>());
    }

    /**
     * Instantiates an ItemStack with enchantments.
     *
     * @param type
     *         material
     * @param amount
     *         amount
     * @param durability
     *         damage / durability
     * @param enchantments
     *         enchantments for item
     */
    @Deprecated
    public MythicItemStack(MaterialData type, int amount, short durability,
                           Map<Enchantment, Integer> enchantments) {
        this(type.getItemType(), amount, durability, null, new ArrayList<String>(), enchantments);
    }

    /**
     * Instantiates an ItemStack with a display name and lore.
     *
     * @param type
     *         material
     * @param amount
     *         amount
     * @param durability
     *         damage / durability
     * @param displayName
     *         name of item
     * @param lore
     *         lore for item
     */
    @Deprecated
    public MythicItemStack(MaterialData type, int amount, short durability, String displayName,
                           List<String> lore) {
        this(type.getItemType(), amount, durability, displayName, lore,
             new HashMap<Enchantment, Integer>());
    }

    /**
     * Instantiates an ItemStack with a display name and enchantments.
     *
     * @param type
     *         material
     * @param amount
     *         amount
     * @param durability
     *         damage / durability
     * @param displayName
     *         name of item
     * @param enchantments
     *         enchantments for item
     */
    @Deprecated
    public MythicItemStack(MaterialData type, int amount, short durability, String displayName,
                           Map<Enchantment,
                                   Integer> enchantments) {
        this(type.getItemType(), amount, durability, displayName, new ArrayList<String>(),
             enchantments);
    }

    /**
     * Instantiates an ItemStack with lore and enchantments.
     *
     * @param type
     *         material
     * @param amount
     *         amount
     * @param durability
     *         damage / durability
     * @param lore
     *         lore for item
     * @param enchantments
     *         enchantments for item
     */
    @Deprecated
    public MythicItemStack(MaterialData type, int amount, short durability, List<String> lore,
                           Map<Enchantment,
                                   Integer> enchantments) {
        this(type.getItemType(), amount, durability, null, lore, enchantments);
    }

    /**
     * Instantiates an ItemStack.
     *
     * @param type
     *         material
     */
    public MythicItemStack(Material type) {
        this(type, 1, (short) 0, null, null, null);
    }

    /**
     * Instantiates an ItemStack.
     *
     * @param type
     *         material
     * @param durability
     *         damage / durability
     */
    public MythicItemStack(Material type, short durability) {
        this(type, 1, durability, null, null, null);
    }

    /**
     * Instantiates an ItemStack.
     *
     * @param type
     *         material
     * @param amount
     *         amount
     */
    public MythicItemStack(Material type, int amount) {
        this(type, amount, (short) 0, null, null, null);
    }

    /**
     * Instantiates an ItemStack with a display name.
     *
     * @param type
     *         material
     * @param amount
     *         amount
     * @param durability
     *         damage / durability
     * @param displayName
     *         name of item
     */
    public MythicItemStack(Material type, int amount, short durability, String displayName) {
        this(type, amount, durability, displayName, new ArrayList<String>(),
             new HashMap<Enchantment, Integer>());
    }

    /**
     * Instantiates an ItemStack with lore.
     *
     * @param type
     *         material
     * @param amount
     *         amount
     * @param durability
     *         damage / durability
     * @param lore
     *         lore for item
     */
    public MythicItemStack(Material type, int amount, short durability, List<String> lore) {
        this(type, amount, durability, null, lore, new HashMap<Enchantment, Integer>());
    }

    /**
     * Instantiates an ItemStack with enchantments.
     *
     * @param type
     *         material
     * @param amount
     *         amount
     * @param durability
     *         damage / durability
     * @param enchantments
     *         enchantments for item
     */
    public MythicItemStack(Material type, int amount, short durability,
                           Map<Enchantment, Integer> enchantments) {
        this(type, amount, durability, null, new ArrayList<String>(), enchantments);
    }

    /**
     * Instantiates an ItemStack with a display name and lore.
     *
     * @param type
     *         material
     * @param amount
     *         amount
     * @param durability
     *         damage / durability
     * @param displayName
     *         name of item
     * @param lore
     *         lore for item
     */
    public MythicItemStack(Material type, int amount, short durability, String displayName,
                           List<String> lore) {
        this(type, amount, durability, displayName, lore, new HashMap<Enchantment, Integer>());
    }

    /**
     * Instantiates an ItemStack with a display name and enchantments.
     *
     * @param type
     *         material
     * @param amount
     *         amount
     * @param durability
     *         damage / durability
     * @param displayName
     *         name of item
     * @param enchantments
     *         enchantments for item
     */
    public MythicItemStack(Material type, int amount, short durability, String displayName,
                           Map<Enchantment,
                                   Integer> enchantments) {
        this(type, amount, durability, displayName, new ArrayList<String>(), enchantments);
    }

    /**
     * Instantiates an ItemStack with lore and enchantments.
     *
     * @param type
     *         material
     * @param amount
     *         amount
     * @param durability
     *         damage / durability
     * @param lore
     *         lore for item
     * @param enchantments
     *         enchantments for item
     */
    public MythicItemStack(Material type, int amount, short durability, List<String> lore,
                           Map<Enchantment,
                                   Integer> enchantments) {
        this(type, amount, durability, null, lore, enchantments);
    }

    /**
     * Wraps an {@link ItemStack} in a MythicItemStack.
     *
     * @param itemStack
     *         ItemStack to wrap
     */
    public MythicItemStack(ItemStack itemStack) {
        this(itemStack.getType(), itemStack.getAmount(), itemStack.getDurability());
        if (itemStack.hasItemMeta()) {
            setItemMeta(itemStack.getItemMeta().clone());
        } else {
            setItemMeta(Bukkit.getItemFactory().getItemMeta(itemStack.getType()));
        }
    }

    /**
     * Instantiates an ItemStack.
     *
     * @param type
     *         material
     * @param amount
     *         amount
     * @param durability
     *         damage / durability
     */
    public MythicItemStack(Material type, int amount, short durability) {
        this(type, amount, durability, null, null, null);
    }

}
