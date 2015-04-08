package com.tealcube.minecraft.bukkit.mythicdrops.api.items;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.List;
import java.util.Map;

public interface CustomItem {

    /**
     * Gets the chance for the item to be given to a monster.
     *
     * @return chance to be given to a monster
     */
    double getChanceToBeGivenToAMonster();

    /**
     * Gets the chance for the item to drop on death.
     *
     * @return chance to drop item on death
     */
    double getChanceToDropOnDeath();

    /**
     * Gets the internal name of the CustomItem.
     *
     * @return iternal name
     */
    String getName();

    /**
     * Gets the name that is displayed on the item.
     *
     * @return display name
     */
    String getDisplayName();

    /**
     * Gets a {@link Map} of {@link Enchantment}s and their {@link Integer} values for the CustomItem.
     *
     * @return Map of Enchantments and levels
     */
    Map<Enchantment, Integer> getEnchantments();

    /**
     * Gets a {@link List} of lore for the CustomItem.
     *
     * @return lore for the item
     */
    List<String> getLore();

    /**
     * Gets the {@link MaterialData} of the item.
     *
     * @return MaterialData of the CustomItem
     */
    @Deprecated
    MaterialData getMaterialData();

    /**
     * Gets the {@link Material} of the item.
     *
     * @return MaterialData of the CustomItem
     */
    Material getMaterial();

    /**
     * Converts the CustomItem to an {@link ItemStack}.
     *
     * @return CustomItem as an ItemStack
     */
    ItemStack toItemStack();

    boolean isBroadcastOnFind();
}
