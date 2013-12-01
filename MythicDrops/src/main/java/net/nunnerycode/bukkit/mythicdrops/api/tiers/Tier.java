package net.nunnerycode.bukkit.mythicdrops.api.tiers;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.nunnerycode.bukkit.mythicdrops.api.enchantments.MythicEnchantment;

public interface Tier {

	String getName();

	String getDisplayName();

	List<String> getBaseLore();

	List<String> getBonusLore();

	int getMinimumBonusLore();

	int getMaximumBonusLore();

	Set<MythicEnchantment> getBaseEnchantments();

	Set<MythicEnchantment> getBonusEnchantements();

	boolean isSafeBaseEnchantments();

	boolean isSafeBonusEnchantments();

	boolean isAllowHighBaseEnchantments();

	boolean isAllowHighBonusEnchantments();

	int getMinimumBonusEnchantments();

	int getMaximumBonusEnchantments();

	double getMaximumDurabilityPercentage();

	double getMinimumDurabilityPercentage();

	Map<String, Double> getWorldDropChanceMap();

	Map<String, Double> getWorldSpawnChanceMap();

	List<String> getAllowedItemGroups();

	List<String> getDisallowedItemGroups();

	List<String> getAllowedItemIds();

	List<String> getDisallowedItemIds();

}
