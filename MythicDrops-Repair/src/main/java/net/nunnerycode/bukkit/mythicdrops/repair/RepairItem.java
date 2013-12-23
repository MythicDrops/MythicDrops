package net.nunnerycode.bukkit.mythicdrops.repair;

import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RepairItem {

	private final String name;
	private final MaterialData materialData;
	private final String itemName;
	private final List<String> itemLore;
	private final Map<String, RepairCost> repairCostMap;

	public RepairItem(String name, MaterialData materialData, String itemName, List<String> itemLore) {
		this.name = name;
		this.materialData = materialData;
		this.itemName = itemName;
		this.itemLore = itemLore;
		repairCostMap = new HashMap<>();
	}

	public String getName() {
		return name;
	}

	public MaterialData getMaterialData() {
		return materialData;
	}

	public String getItemName() {
		return itemName;
	}

	public List<String> getItemLore() {
		return itemLore;
	}

	public List<RepairCost> getRepairCosts() {
		return new ArrayList<>(repairCostMap.values());
	}

	public RepairItem addRepairCosts(RepairCost... repairCosts) {
		for (RepairCost rc : repairCosts) {
			repairCostMap.put(rc.getName(), rc);
		}
		return this;
	}

	public RepairItem removeRepairCosts(String... names) {
		for (String s : names) {
			repairCostMap.remove(s);
		}
		return this;
	}

	public ItemStack toItemStack(int amount) {
		return new MythicItemStack(materialData.getItemType(), amount, (short) 0,
				(itemName == null || itemName.isEmpty()) ? null : itemName,
				(itemLore == null || itemLore.isEmpty()) ? null : itemLore);
	}
}
