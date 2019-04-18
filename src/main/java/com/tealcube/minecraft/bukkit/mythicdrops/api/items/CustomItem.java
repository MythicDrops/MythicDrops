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
package com.tealcube.minecraft.bukkit.mythicdrops.api.items;

import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public interface CustomItem {

  /**
   * Gets the chance for the item to be given to a monster.
   *
   * @return chance to be given to a monster
   */
  double getChanceToBeGivenToAMonster();

  /**
   * Gets the chance for the item to drop on death.
   *
   * @return chance to drop item on death
   */
  double getChanceToDropOnDeath();

  /**
   * Gets the internal name of the CustomItem.
   *
   * @return iternal name
   */
  String getName();

  /**
   * Gets the name that is displayed on the item.
   *
   * @return display name
   */
  String getDisplayName();

  /**
   * Gets a {@link List} of {@link MythicEnchantment}s for the CustomItem.
   *
   * @return Map of Enchantments and levels
   */
  List<MythicEnchantment> getEnchantments();

  /**
   * Gets a {@link List} of lore for the CustomItem.
   *
   * @return lore for the item
   */
  List<String> getLore();

  /**
   * Gets the {@link MaterialData} of the item.
   *
   * @return MaterialData of the CustomItem
   */
  @Deprecated
  MaterialData getMaterialData();

  /**
   * Gets the {@link Material} of the item.
   *
   * @return MaterialData of the CustomItem
   */
  Material getMaterial();

  /**
   * Converts the CustomItem to an {@link ItemStack}.
   *
   * @return CustomItem as an ItemStack
   */
  ItemStack toItemStack();

  boolean isBroadcastOnFind();

  short getDurability();

  boolean isUnbreakable();

  boolean hasDurability();
}
