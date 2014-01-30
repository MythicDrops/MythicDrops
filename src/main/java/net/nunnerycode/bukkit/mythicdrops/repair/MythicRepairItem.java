package net.nunnerycode.bukkit.mythicdrops.repair;

import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.repair.RepairCost;
import net.nunnerycode.bukkit.mythicdrops.api.repair.RepairItem;

import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MythicRepairItem implements RepairItem {

  private final String name;
  private final MaterialData materialData;
  private final String itemName;
  private final List<String> itemLore;
  private final Map<String, RepairCost> repairCostMap;

  public MythicRepairItem(String name, MaterialData materialData, String itemName,
                          List<String> itemLore) {
    this.name = name;
    this.materialData = materialData;
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
    return materialData;
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
    return new MythicItemStack(materialData.getItemType(), amount, (short) 0,
                               (itemName == null || itemName.isEmpty()) ? null : itemName,
                               (itemLore == null || itemLore.isEmpty()) ? null : itemLore);
  }
}
