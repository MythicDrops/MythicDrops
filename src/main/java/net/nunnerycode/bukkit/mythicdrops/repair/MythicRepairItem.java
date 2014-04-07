package net.nunnerycode.bukkit.mythicdrops.repair;

import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.repair.RepairCost;
import net.nunnerycode.bukkit.mythicdrops.api.repair.RepairItem;
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
    public Material getMaterial() {
        return material;
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

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (material != null ? material.hashCode() : 0);
        result = 31 * result + (itemName != null ? itemName.hashCode() : 0);
        result = 31 * result + (itemLore != null ? itemLore.hashCode() : 0);
        return result;
    }

}
