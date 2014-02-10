package net.nunnerycode.bukkit.mythicdrops.api.tiers;

import net.nunnerycode.bukkit.mythicdrops.api.enchantments.MythicEnchantment;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

  Tier getReplaceWith();

  double getReplaceDistance();

}
