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

import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory;
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.TierMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.Validate;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class TierUtil {

  private static final Random RANDOM = new Random();
  private static final Logger LOGGER = JulLoggerFactory.INSTANCE.getLogger(TierUtil.class);

  private TierUtil() {
    // do nothing
  }

  public static Tier randomTier(Collection<Tier> collection) {
    Validate.notNull(collection, "Collection<Tier> cannot be null");
    Tier[] array = collection.toArray(new Tier[collection.size()]);
    return array[RandomUtils.nextInt(0, array.length)];
  }

  @Deprecated
  public static Tier randomTierWithChance(Collection<Tier> values, String worldName) {
    Validate.notNull(values, "Collection<Tier> cannot be null");
    return randomTierWithChance(values);
  }

  public static Tier randomTierWithChance(Collection<Tier> values) {
    Validate.notNull(values, "Collection<Tier> cannot be null");

    // Get all of the Tiers from the given collection with a spawn chance of greater than 0
    List<Tier> v =
        values.stream().filter(tier -> tier.getSpawnChance() > 0).collect(Collectors.toList());

    // Randomize the contents of the tiers
    Collections.shuffle(v, RANDOM);

    // Add all of the applicable tiers weights together
    double totalWeight = v.stream().mapToDouble(Tier::getSpawnChance).sum();
    double chosenWeight = RANDOM.nextDouble() * totalWeight;

    LOGGER.fine("totalWeight: " + totalWeight);
    LOGGER.fine("chosenWeight: " + chosenWeight);

    return getTierFromListWithWeight(v, chosenWeight);
  }

  @Nullable
  public static Tier getTierFromListWithWeight(List<Tier> v, double chosenWeight) {
    double currentWeight = 0;

    for (Tier t : v) {
      currentWeight += t.getSpawnChance();

      if (currentWeight >= chosenWeight) {
        return t;
      }
    }

    return null;
  }

  @Deprecated
  public static Tier randomTierWithIdentifyChance(Collection<Tier> values, String worldName) {
    Validate.notNull(values, "Collection<Tier> cannot be null");
    return randomTierWithIdentifyChance(values);
  }

  public static Tier randomTierWithIdentifyChance(Collection<Tier> values) {
    Validate.notNull(values, "Collection<Tier> cannot be null");

    // Get all of the Tiers from the given collection with an identify chance of greater than 0
    List<Tier> v =
        values.stream().filter(tier -> tier.getIdentifyChance() > 0).collect(Collectors.toList());

    // Randomize the contents of the tiers
    Collections.shuffle(v, RANDOM);

    // Add all of the applicable tiers weights together
    double totalWeight = v.stream().mapToDouble(Tier::getIdentifyChance).sum();
    double chosenWeight = RANDOM.nextDouble() * totalWeight;

    LOGGER.fine("totalWeight: " + totalWeight);
    LOGGER.fine("chosenWeight: " + chosenWeight);

    double currentWeight = 0;

    for (Tier t : v) {
      currentWeight += t.getIdentifyChance();

      if (currentWeight >= chosenWeight) {
        return t;
      }
    }

    return null;
  }

  public static Collection<Tier> getTiersFromStrings(Collection<String> strings) {
    Validate.notNull(strings, "Collection<String> cannot be null");
    Set<Tier> tiers = new LinkedHashSet<>();
    for (String s : strings) {
      Tier t = getTier(s);
      if (t != null) {
        tiers.add(t);
      }
    }
    return tiers;
  }

  public static Tier getTier(String name) {
    Validate.notNull(name, "String cannot be null");
    Tier tier = TierMap.INSTANCE.get(name.toLowerCase());
    if (tier != null) {
      return tier;
    }
    for (Tier t : TierMap.INSTANCE.values()) {
      if (t.getName().equalsIgnoreCase(name)) {
        return t;
      }
      if (t.getDisplayName().equalsIgnoreCase(name)) {
        return t;
      }
    }
    return null;
  }

  public static List<String> getStringsFromTiers(Collection<Tier> collection) {
    Validate.notNull(collection, "Collection<Tier> cannot be null");
    List<String> col = new ArrayList<>();
    for (Tier t : collection) {
      col.add(t.getName());
    }
    return col;
  }

  public static Tier getTierFromItemStack(ItemStack itemStack) {
    return getTierFromItemStack(itemStack, TierMap.INSTANCE.values());
  }

  public static Tier getTierFromItemStack(ItemStack itemStack, Collection<Tier> tiers) {
    Validate.notNull(itemStack);
    Validate.notNull(tiers);
    if (!itemStack.hasItemMeta()) {
      return null;
    }
    if (!itemStack.getItemMeta().hasDisplayName()) {
      return null;
    }
    String displayName = itemStack.getItemMeta().getDisplayName();
    ChatColor initColor = findColor(displayName);
    String colors = ChatColor.getLastColors(displayName);
    ChatColor endColor =
        ChatColor.getLastColors(displayName).contains(String.valueOf(ChatColor.COLOR_CHAR))
            ? ChatColor.getByChar(colors.substring(1, 2))
            : null;
    if (initColor == null || endColor == null || initColor == endColor) {
      return null;
    }
    for (Tier t : tiers) {
      if (t.getDisplayColor() == initColor && t.getIdentificationColor() == endColor) {
        return t;
      }
    }
    return null;
  }

  private static ChatColor findColor(final String s) {
    char[] c = s.toCharArray();
    for (int i = 0; i < c.length; i++) {
      if (c[i] == (char) 167 && (i + 1) < c.length) {
        return ChatColor.getByChar(c[i + 1]);
      }
    }
    return null;
  }

  @Deprecated
  public static Collection<Tier> skewTierCollectionToRarer(
      Collection<Tier> values, int numberToKeep) {
    return values;
  }

  public static Tier randomTierWithChance(Map<Tier, Double> chanceMap) {
    Validate.notNull(chanceMap, "Map<Tier, Double> cannot be null");

    // Get all of the Tiers from the given Map with a chance of greater than 0
    List<Tier> v =
        chanceMap.entrySet().stream()
            .filter(tierDoubleEntry -> tierDoubleEntry.getValue() > 0)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

    // Randomize the contents of the tiers
    Collections.shuffle(v, RANDOM);

    // Add all of the applicable tiers weights together
    double totalWeight = v.stream().mapToDouble(chanceMap::get).sum();
    double chosenWeight = RANDOM.nextDouble() * totalWeight;

    LOGGER.fine("totalWeight: " + totalWeight);
    LOGGER.fine("chosenWeight: " + chosenWeight);

    double currentWeight = 0;

    for (Tier t : v) {
      currentWeight += chanceMap.get(t);

      if (currentWeight >= chosenWeight) {
        return t;
      }
    }
    return null;
  }
}
