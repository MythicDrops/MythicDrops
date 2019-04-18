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
package com.tealcube.minecraft.bukkit.mythicdrops.items;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.MythicItemStack;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.RandomRangeUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public final class MythicCustomItem implements CustomItem {

  private final String name;
  private double chanceToBeGivenToAMonster;
  private double chanceToDropOnDeath;
  private String displayName;
  private List<MythicEnchantment> enchantments;
  private List<String> lore;
  private Material material;
  private boolean broadcastOnFind;
  private short durability;
  private boolean unbreakable;
  private boolean hasDurability;

  MythicCustomItem(String name) {
    this.name = name;
    enchantments = new ArrayList<>();
    lore = new ArrayList<>();
  }

  @Override
  public double getChanceToBeGivenToAMonster() {
    return chanceToBeGivenToAMonster;
  }

  void setChanceToBeGivenToAMonster(double chanceToBeGivenToAMonster) {
    this.chanceToBeGivenToAMonster = chanceToBeGivenToAMonster;
  }

  @Override
  public double getChanceToDropOnDeath() {
    return chanceToDropOnDeath;
  }

  void setChanceToDropOnDeath(double chanceToDropOnDeath) {
    this.chanceToDropOnDeath = chanceToDropOnDeath;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDisplayName() {
    return displayName;
  }

  void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  @Override
  public List<MythicEnchantment> getEnchantments() {
    return enchantments;
  }

  void setEnchantments(List<MythicEnchantment> enchantments) {
    this.enchantments = enchantments;
  }

  @Override
  public List<String> getLore() {
    return lore;
  }

  void setLore(List<String> lore) {
    this.lore = lore;
  }

  @Override
  @Deprecated
  public MaterialData getMaterialData() {
    return new MaterialData(material);
  }

  @Deprecated
  public void setMaterialData(MaterialData materialData) {
    // do nothing
  }

  public Material getMaterial() {
    return this.material;
  }

  public void setMaterial(Material material) {
    this.material = material;
  }

  /**
   * Converts the CustomItem to an {@link org.bukkit.inventory.ItemStack}.
   *
   * @return CustomItem as an ItemStack
   */
  @Override
  public ItemStack toItemStack() {
    Preconditions.checkNotNull(material, "material cannot be null");
    Map<Enchantment, Integer> enchs = enchantments.stream()
        .collect(Collectors.toMap(MythicEnchantment::getEnchantment, MythicEnchantment::getRandomLevel));
    MythicItemStack mythicItemStack = new MythicItemStack(material, 1, durability, displayName, lore,
        enchs);
    mythicItemStack.setUnbreakable(unbreakable);
    return mythicItemStack;
  }

  @Override
  public boolean isBroadcastOnFind() {
    return broadcastOnFind;
  }

  public void setBroadcastOnFind(boolean broadcastOnFind) {
    this.broadcastOnFind = broadcastOnFind;
  }

  @Override
  public short getDurability() {
    return 0;
  }

  public void setDurability(short durability) {
    this.durability = durability;
  }

  @Override
  public boolean isUnbreakable() {
    return unbreakable;
  }

  public void setUnbreakable(boolean unbreakable) {
    this.unbreakable = unbreakable;
  }

  @Override
  public boolean hasDurability() {
    return hasDurability;
  }

  public void setHasDurability(boolean hasDurability) {
    this.hasDurability = hasDurability;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", name)
        .add("chanceToBeGivenToAMonster", chanceToBeGivenToAMonster)
        .add("chanceToDropOnDeath", chanceToDropOnDeath)
        .add("displayName", displayName)
        .add("enchantments", enchantments)
        .add("lore", lore)
        .add("material", material)
        .add("broadcastOnFind", broadcastOnFind)
        .add("durability", durability)
        .add("unbreakable", unbreakable)
        .add("hasDurability", hasDurability)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MythicCustomItem that = (MythicCustomItem) o;
    return Double.compare(that.chanceToBeGivenToAMonster, chanceToBeGivenToAMonster) == 0 &&
        Double.compare(that.chanceToDropOnDeath, chanceToDropOnDeath) == 0 &&
        broadcastOnFind == that.broadcastOnFind &&
        durability == that.durability &&
        unbreakable == that.unbreakable &&
        hasDurability == that.hasDurability &&
        Objects.equals(name, that.name) &&
        Objects.equals(displayName, that.displayName) &&
        Objects.equals(enchantments, that.enchantments) &&
        Objects.equals(lore, that.lore) &&
        material == that.material;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, chanceToBeGivenToAMonster, chanceToDropOnDeath, displayName, enchantments, lore, material,
        broadcastOnFind, durability, unbreakable, hasDurability);
  }
}
