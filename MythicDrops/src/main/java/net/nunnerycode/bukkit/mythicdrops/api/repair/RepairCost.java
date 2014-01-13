package net.nunnerycode.bukkit.mythicdrops.api.repair;

import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.List;

public interface RepairCost {
	List<String> getItemLore();

	String getItemName();

	MaterialData getMaterialData();

	int getAmount();

	double getRepairPercentagePerCost();

	int getExperienceCost();

	int getPriority();

	String getName();

	ItemStack toItemStack(int amount);
}
