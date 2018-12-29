/*
 * The MIT License
 * Copyright © 2013 Richard Harrah
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
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public final class MythicRepairCost implements RepairCost {

  private final String name;
  private final int priority;
  private final int experienceCost;
  private final double repairPercentagePerCost;
  private final int amount;
  private final Material material;
  private final String itemName;
  private final List<String> itemLore;

  public MythicRepairCost(String name, int priority, int experienceCost,
      double repairPercentagePerCost, int amount,
      Material material, String itemName, List<String> itemLore) {
    this.name = name;
    this.priority = priority;
    this.experienceCost = experienceCost;
    this.repairPercentagePerCost = repairPercentagePerCost;
    this.amount = amount;
    this.material = material;
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
    return new MaterialData(material);
  }

  @Override
  public Material getMaterial() {
    return material;
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
    return new MythicItemStack(material, amount, (short) 0,
        (itemName == null || itemName.isEmpty()) ? null : itemName,
        (itemLore == null || itemLore.isEmpty()) ? null : itemLore);
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = name != null ? name.hashCode() : 0;
    result = 31 * result + priority;
    result = 31 * result + experienceCost;
    temp = Double.doubleToLongBits(repairPercentagePerCost);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + amount;
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

    MythicRepairCost that = (MythicRepairCost) o;

    return amount == that.amount && experienceCost == that.experienceCost
        && priority == that.priority
        && Double.compare(that.repairPercentagePerCost, repairPercentagePerCost) == 0 && !(
        itemLore != null ? !itemLore.equals(that.itemLore) : that.itemLore != null) && !(
        itemName != null ? !itemName.equals(that.itemName) : that.itemName != null)
        && material == that.material && !(name != null ? !name.equals(that.name)
        : that.name != null);
  }
}
