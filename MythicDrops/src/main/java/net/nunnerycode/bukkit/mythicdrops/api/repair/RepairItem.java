package net.nunnerycode.bukkit.mythicdrops.api.repair;

import net.nunnerycode.bukkit.mythicdrops.repair.MythicRepairCost;
import net.nunnerycode.bukkit.mythicdrops.repair.MythicRepairItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.List;

public interface RepairItem {
	String getName();

	MaterialData getMaterialData();

	String getItemName();

	List<String> getItemLore();

	List<RepairCost> getRepairCosts();

	MythicRepairItem addRepairCosts(MythicRepairCost... mythicRepairCosts);

	MythicRepairItem removeRepairCosts(String... names);

	ItemStack toItemStack(int amount);
}
