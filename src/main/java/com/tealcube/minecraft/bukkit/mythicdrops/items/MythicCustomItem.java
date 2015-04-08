package com.tealcube.minecraft.bukkit.mythicdrops.items;

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


import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.MythicItemStack;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MythicCustomItem implements CustomItem {

    private final String name;
    private double chanceToBeGivenToAMonster;
    private double chanceToDropOnDeath;
    private String displayName;
    private Map<Enchantment, Integer> enchantments;
    private List<String> lore;
    private Material material;
    private boolean broadcastOnFind;

    public MythicCustomItem(String name) {
        this.name = name;
        enchantments = new HashMap<>();
        lore = new ArrayList<>();
    }

    @Override
    public double getChanceToBeGivenToAMonster() {
        return chanceToBeGivenToAMonster;
    }

    void setChanceToBeGivenToAMonster(double chanceToBeGivenToAMonster) {
        this.chanceToBeGivenToAMonster = chanceToBeGivenToAMonster;
    }

    @Override
    public double getChanceToDropOnDeath() {
        return chanceToDropOnDeath;
    }

    void setChanceToDropOnDeath(double chanceToDropOnDeath) {
        this.chanceToDropOnDeath = chanceToDropOnDeath;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    void setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
    }

    @Override
    public List<String> getLore() {
        return lore;
    }

    void setLore(List<String> lore) {
        this.lore = lore;
    }

    @Override
    @Deprecated
    public MaterialData getMaterialData() {
        return new MaterialData(material);
    }

    @Deprecated
    public void setMaterialData(MaterialData materialData) {
        // do nothing
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * Converts the CustomItem to an {@link org.bukkit.inventory.ItemStack}.
     *
     * @return CustomItem as an ItemStack
     */
    @Override
    public ItemStack toItemStack() {
        return new MythicItemStack(material, 1, (short) 0, displayName, lore,
                                   enchantments);
    }

    @Override
    public boolean isBroadcastOnFind() {
        return broadcastOnFind;
    }

    public void setBroadcastOnFind(boolean broadcastOnFind) {
        this.broadcastOnFind = broadcastOnFind;
    }

    @Override
    public int hashCode() {
        int result;
        result = name != null ? name.hashCode() : 0;
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (enchantments != null ? enchantments.hashCode() : 0);
        result = 31 * result + (lore != null ? lore.hashCode() : 0);
        result = 31 * result + (material != null ? material.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MythicCustomItem)) {
            return false;
        }

        MythicCustomItem that = (MythicCustomItem) o;

        return !(displayName != null ? !displayName.equals(that.displayName) : that.displayName != null)
               && !(enchantments != null ? !enchantments.equals(that.enchantments)
                                         : that.enchantments != null) && !(lore != null ? !lore
            .equals(that.lore) : that.lore != null) && !(material != null ? !material
            .equals(that.material) : that.material != null) && !(name != null ? !name.equals(that.name)
                                                                              : that.name != null);
    }
}
