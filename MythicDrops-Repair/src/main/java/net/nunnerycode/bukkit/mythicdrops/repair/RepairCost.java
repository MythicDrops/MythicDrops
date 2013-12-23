package net.nunnerycode.bukkit.mythicdrops.repair;

import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.List;

public final class RepairCost {
	private final String name;
	private final int priority;
	private final int experienceCost;
	private final double repairPercentagePerCost;
	private final int amount;
	private final MaterialData materialData;
	private final String itemName;
	private final List<String> itemLore;

	public RepairCost(String name, int priority, int experienceCost, double repairPercentagePerCost, int amount,
					  MaterialData materialData, String itemName, List<String> itemLore) {
		this.name = name;
		this.priority = priority;
		this.experienceCost = experienceCost;
		this.repairPercentagePerCost = repairPercentagePerCost;
		this.amount = amount;
		this.materialData = materialData;
		this.itemName = itemName;
		this.itemLore = itemLore;
	}

	public List<String> getItemLore() {
		return itemLore;
	}

	public String getItemName() {
		return itemName;
	}

	public MaterialData getMaterialData() {
		return materialData;
	}

	public int getAmount() {
		return amount;
	}

	public double getRepairPercentagePerCost() {
		return repairPercentagePerCost;
	}

	public int getExperienceCost() {
		return experienceCost;
	}

	public int getPriority() {
		return priority;
	}

	public String getName() {
		return name;
	}

	public ItemStack toItemStack(int amount) {
		return new MythicItemStack(materialData.getItemType(), amount, (short) 0,
				(itemName == null || itemName.isEmpty()) ? null : itemName,
				(itemLore == null || itemLore.isEmpty()) ? null : itemLore);
	}
}
