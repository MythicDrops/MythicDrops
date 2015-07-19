/**
 * The MIT License
 * Copyright (c) 2013 Teal Cube Games
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.repair;

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
