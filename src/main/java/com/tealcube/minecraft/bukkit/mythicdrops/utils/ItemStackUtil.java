/**
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.utils;

import java.util.Map;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class ItemStackUtil {

  private ItemStackUtil() {
    // do nothing
  }

  /**
   * Returns a durability value that is acceptable for a specified Material, based on whether or not the specified
   * durability value is less than 0 and is greater than the Material's maximum durability.
   *
   * @param material Material to check
   * @param durability Durability to check
   * @return 0 if durability is less than 0 and Material's maximum durability if durability is larger than maximum
   */
  public static short getAcceptableDurability(Material material, short durability) {
    return (short) Math.max(Math.min(durability, material.getMaxDurability()), 0);
  }

  /**
   * Returns a durability value for a Material, where the value returned is between the Material's maximum durability
   * multiplied by minDurability and maxDurability.
   *
   * @param material Material to check
   * @param minDurability Lowest percentage for durability
   * @param maxDurability Highest percentage for durability
   * @return durability value for Material
   */
  public static short getDurabilityForMaterial(Material material, double minDurability,
      double maxDurability) {
    short
        minimumDurability =
        (short) (material.getMaxDurability() - material.getMaxDurability() * Math.max
            (minDurability, maxDurability));
    short
        maximumDurability =
        (short) (material.getMaxDurability() - material.getMaxDurability() * Math.min
            (minDurability, maxDurability));
    return (short) RandomRangeUtil.randomRange(minimumDurability, maximumDurability);
  }

  /**
   * Gets the highest Enchantment on an ItemStack. Returns null if no Enchantments present.
   *
   * @param itemStack ItemStack to check
   * @return highest Enchantment on an ItemStack
   */
  public static Enchantment getHighestEnchantment(ItemStack itemStack) {
    Enchantment enchantment = null;
    Integer level = 0;
    for (Map.Entry<Enchantment, Integer> e : itemStack.getEnchantments().entrySet()) {
      if (e.getValue() > level) {
        enchantment = e.getKey();
        level = e.getValue();
      }
    }
    return enchantment;
  }

  public static Enchantment getHighestEnchantment(ItemMeta itemMeta) {
    Enchantment enchantment = null;
    Integer level = 0;
    for (Map.Entry<Enchantment, Integer> e : itemMeta.getEnchants().entrySet()) {
      if (e.getValue() > level) {
        enchantment = e.getKey();
        level = e.getValue();
      }
    }
    return enchantment;
  }

}
