package com.conventnunnery.plugins.mythicdrops.managers;

import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.objects.RepairCost;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.HashSet;
import java.util.Set;

public class RepairManager {
    private final MythicDrops plugin;
    private Set<RepairCost> repairCostSet;

    public RepairManager(MythicDrops plugin) {
        this.plugin = plugin;
        repairCostSet = new HashSet<RepairCost>();
    }

    public Set<RepairCost> getRepairCostSet() {
        return repairCostSet;
    }

    public RepairCost getRepairCost(MaterialData materialData) {
        MaterialData backup = new MaterialData(materialData.getItemType(), (byte) 0);
        for (RepairCost repairCost : repairCostSet) {
            if (repairCost.getMaterialData().equals(materialData) || repairCost.getMaterialData().equals(backup)) {
                return repairCost;
            }
        }
        return null;
    }

    public ItemStack repairItemStack(ItemStack itemStack, Inventory inventory) {
        if (itemStack == null) {
            return itemStack;
        }
        ItemStack repaired = itemStack.clone();
        RepairCost repairCost = getRepairCost(repaired.getData());
        if (repairCost == null) {
            return repaired;
        }
        if (!inventory.containsAtLeast(repairCost.getMaterialData().toItemStack(1), repairCost.getAmountRequired())) {
            return repaired;
        }
        int taken = 0;
        ItemStack slot;
        while (taken < repairCost.getAmountRequired() &&
                inventory.containsAtLeast(repairCost.getMaterialData().toItemStack(1),
                        repairCost.getAmountRequired() - taken)) {
            slot = inventory.getItem(inventory.first(repairCost.getMaterialData().getItemType()));
            if (slot.getAmount() >= repairCost.getAmountRequired()) {
                slot.setAmount(slot.getAmount() - repairCost.getAmountRequired());
                taken += repairCost.getAmountRequired();
            } else {
                taken += slot.getAmount();
                slot.setAmount(0);
            }
        }
        if (taken < repairCost.getAmountRequired()) {
            for (HumanEntity humanEntity : inventory.getViewers()) {
                if (humanEntity instanceof Player) {
                    ((Player) humanEntity).updateInventory();
                }
            }
            return repaired;
        }
        short currentDurability = repaired.getDurability();
        short newDurability = (short) (currentDurability - (repaired.getType().getMaxDurability()
                * repairCost.getPercentageRestored()));
        repaired.setDurability(newDurability);
        for (HumanEntity humanEntity : inventory.getViewers()) {
            if (humanEntity instanceof Player) {
                ((Player) humanEntity).updateInventory();
            }
        }
        return repaired;
    }

    public void debugRepairCosts() {
        getPlugin().getDebug().debug("Loaded repair costs: " + repairCostSet.size());
    }

    public MythicDrops getPlugin() {
        return plugin;
    }
}
