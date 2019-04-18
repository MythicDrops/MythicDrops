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

import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.material.MaterialData;

public final class CustomItemBuilder {

  public final MythicCustomItem customItem;

  public CustomItemBuilder(String name) {
    customItem = new MythicCustomItem(name);
  }

  public CustomItemBuilder withDisplayName(String displayName) {
    customItem.setDisplayName(displayName);
    return this;
  }

  public CustomItemBuilder withLore(List<String> lore) {
    customItem.setLore(lore);
    return this;
  }

  public CustomItemBuilder withEnchantments(List<MythicEnchantment> enchantments) {
    customItem.setEnchantments(enchantments);
    return this;
  }

  @Deprecated
  public CustomItemBuilder withMaterialData(MaterialData materialData) {
    // do nothing
    return this;
  }

  public CustomItemBuilder withMaterial(Material material) {
    customItem.setMaterial(material);
    return this;
  }

  public CustomItemBuilder withChanceToBeGivenToMonster(double chance) {
    customItem.setChanceToBeGivenToAMonster(chance);
    return this;
  }

  public CustomItemBuilder withChanceToDropOnDeath(double chance) {
    customItem.setChanceToDropOnDeath(chance);
    return this;
  }

  public CustomItemBuilder withBroadcastOnFind(boolean b) {
    customItem.setBroadcastOnFind(b);
    return this;
  }

  public CustomItemBuilder withDurability(short durability) {
    customItem.setDurability(durability);
    return this;
  }

  public CustomItemBuilder hasDurability(boolean durability) {
    customItem.setHasDurability(durability);
    return this;
  }

  public CustomItem build() {
    return customItem;
  }

  public CustomItemBuilder withUnbreakable(boolean unbreakable) {
    customItem.setUnbreakable(unbreakable);
    return this;
  }
}
