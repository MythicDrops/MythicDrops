/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2019 Richard Harrah
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

import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin;
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroup;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.TierMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Material;

public final class ItemUtil {

  private static MythicDrops plugin = MythicDropsPlugin.getInstance();

  private ItemUtil() {
    // do nothing
  }

  public static Material getRandomMaterialFromCollection(Collection<Material> collection) {
    if (collection == null || collection.size() == 0) {
      return Material.AIR;
    }
    Material[] array = collection.toArray(new Material[collection.size()]);
    return array[RandomUtils.nextInt(0, array.length)];
  }

  /**
   * Gets a {@link Collection} of {@link Tier}s that the given {@link Material} can be used by.
   *
   * @param material Material to check
   * @return All Tiers that can use the given Material
   */
  public static Collection<Tier> getTiersFromMaterial(Material material) {
    List<Tier> list = new ArrayList<>();
    if (material == null) {
      return list;
    }
    for (Tier t : TierMap.INSTANCE.values()) {
      Collection<Material> materials = getMaterialsFromTier(t);
      if (materials.contains(material)) {
        list.add(t);
      }
    }
    return list;
  }

  /**
   * Gets a {@link Collection} of {@link Material}s that the given {@link Tier} contains.
   *
   * @param tier Tier to check
   * @return All Materials for the given Tier
   */
  public static Collection<Material> getMaterialsFromTier(Tier tier) {
    if (tier == null) {
      return new ArrayList<>();
    }
    List<Material> materials = new ArrayList<>(tier.getAllowedItemIds());
    for (ItemGroup itemGroup : tier.getAllowedItemGroups()) {
      materials.addAll(itemGroup.getMaterials());
    }
    for (ItemGroup itemGroup : tier.getDisallowedItemGroups()) {
      materials.removeAll(itemGroup.getMaterials());
    }
    materials.removeAll(tier.getDisallowedItemIds());
    return materials.stream()
        .filter(Objects::nonNull)
        .filter(material -> material != Material.AIR)
        .collect(Collectors.toList());
  }
}
