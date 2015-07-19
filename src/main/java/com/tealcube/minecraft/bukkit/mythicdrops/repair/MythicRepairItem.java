package com.tealcube.minecraft.bukkit.mythicdrops.repair;

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


import com.tealcube.minecraft.bukkit.mythicdrops.api.items.MythicItemStack;
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairCost;
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MythicRepairItem implements RepairItem {

    private final String name;
    private final Material material;
    private final String itemName;
    private final List<String> itemLore;
    private final Map<String, RepairCost> repairCostMap;

    @Deprecated
    public MythicRepairItem(String name, MaterialData materialData, String itemName,
                            List<String> itemLore) {
        this(name, materialData.getItemType(), itemName, itemLore);
    }

    public MythicRepairItem(String name, Material material, String itemName,
                            List<String> itemLore) {
        this.name = name;
        this.material = material;
        this.itemName = itemName;
        this.itemLore = itemLore;
        repairCostMap = new HashMap<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MaterialData getMaterialData() {
        return new MaterialData(material);
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    @Override
    public List<String> getItemLore() {
        return itemLore;
    }

    @Override
    public List<RepairCost> getRepairCosts() {
        return new ArrayList<>(repairCostMap.values());
    }

    @Override
    public MythicRepairItem addRepairCosts(MythicRepairCost... mythicRepairCosts) {
        for (MythicRepairCost rc : mythicRepairCosts) {
            repairCostMap.put(rc.getName(), rc);
        }
        return this;
    }

    @Override
    public MythicRepairItem removeRepairCosts(String... names) {
        for (String s : names) {
            repairCostMap.remove(s);
        }
        return this;
    }

    @Override
    public ItemStack toItemStack(int amount) {
        return new MythicItemStack(material, amount, (short) 0,
                                   (itemName == null || itemName.isEmpty()) ? null : itemName,
                                   (itemLore == null || itemLore.isEmpty()) ? null : itemLore);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (material != null ? material.hashCode() : 0);
        result = 31 * result + (itemName != null ? itemName.hashCode() : 0);
        result = 31 * result + (itemLore != null ? itemLore.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MythicRepairItem that = (MythicRepairItem) o;

        return !(itemLore != null ? !itemLore.equals(that.itemLore) : that.itemLore != null) && !(
                itemName != null ? !itemName.equals(that.itemName) : that.itemName != null)
                && material == that.material && !(name != null ? !name.equals(that.name)
                                                               : that.name != null);
    }

}
