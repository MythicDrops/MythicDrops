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
package com.tealcube.minecraft.bukkit.mythicdrops.api.tiers;

import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.ChatColor;

public interface Tier extends Comparable<Tier> {

  String getName();

  String getDisplayName();

  ChatColor getDisplayColor();

  ChatColor getIdentificationColor();

  List<String> getBaseLore();

  List<String> getBonusLore();

  int getMinimumBonusLore();

  int getMaximumBonusLore();

  Set<MythicEnchantment> getBaseEnchantments();

  Set<MythicEnchantment> getBonusEnchantments();

  boolean isSafeBaseEnchantments();

  boolean isSafeBonusEnchantments();

  boolean isAllowHighBaseEnchantments();

  boolean isAllowHighBonusEnchantments();

  int getMinimumBonusEnchantments();

  int getMaximumBonusEnchantments();

  double getMaximumDurabilityPercentage();

  double getMinimumDurabilityPercentage();

  @Deprecated
  Map<String, Double> getWorldDropChanceMap();

  double getDropChance();

  @Deprecated
  Map<String, Double> getWorldSpawnChanceMap();

  double getSpawnChance();

  @Deprecated
  Map<String, Double> getWorldIdentifyChanceMap();

  double getIdentifyChance();

  List<String> getAllowedItemGroups();

  List<String> getDisallowedItemGroups();

  List<String> getAllowedItemIds();

  List<String> getDisallowedItemIds();

  int getMinimumSockets();

  int getMaximumSockets();

  double getChanceToHaveSockets();

  boolean isBroadcastOnFind();

  int getOptimalDistance();

  int getMaximumDistance();

  boolean isInfiniteDurability();
}
