package net.nunnerycode.bukkit.mythicdrops.repair;

import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.repair.RepairCost;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.List;

public final class MythicRepairCost implements RepairCost {
	private final String name;
	private final int priority;
	private final int experienceCost;
	private final double repairPercentagePerCost;
	private final int amount;
	private final MaterialData materialData;
	private final String itemName;
	private final List<String> itemLore;

	public MythicRepairCost(String name, int priority, int experienceCost, double repairPercentagePerCost, int amount,
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

	@Override
	public List<String> getItemLore() {
		return itemLore;
	}

	@Override
	public String getItemName() {
		return itemName;
	}

	@Override
	public MaterialData getMaterialData() {
		return materialData;
	}

	@Override
	public int getAmount() {
		return amount;
	}

	@Override
	public double getRepairPercentagePerCost() {
		return repairPercentagePerCost;
	}

	@Override
	public int getExperienceCost() {
		return experienceCost;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemStack toItemStack(int amount) {
		return new MythicItemStack(materialData.getItemType(), amount, (short) 0,
				(itemName == null || itemName.isEmpty()) ? null : itemName,
				(itemLore == null || itemLore.isEmpty()) ? null : itemLore);
	}

}
