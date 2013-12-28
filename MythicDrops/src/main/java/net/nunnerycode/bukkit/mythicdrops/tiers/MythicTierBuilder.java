package net.nunnerycode.bukkit.mythicdrops.tiers;

import net.nunnerycode.bukkit.mythicdrops.api.enchantments.MythicEnchantment;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class MythicTierBuilder {

	private final MythicTier mythicTier;

	public MythicTierBuilder(String name) {
		mythicTier = new MythicTier(name);
	}

	public MythicTierBuilder withDisplayName(String displayName) {
		mythicTier.setDisplayName(displayName);
		return this;
	}

	public MythicTierBuilder withDisplayColor(ChatColor chatColor) {
		mythicTier.setDisplayColor(chatColor);
		return this;
	}

	public MythicTierBuilder withIdentificationColor(ChatColor chatColor) {
		mythicTier.setIdentificationColor(chatColor);
		return this;
	}

	public MythicTierBuilder withBaseLore(List<String> baseLore) {
		mythicTier.setBaseLore(baseLore);
		return this;
	}

	public MythicTierBuilder withBonusLore(List<String> bonusLore) {
		mythicTier.setBonusLore(bonusLore);
		return this;
	}

	public MythicTierBuilder withMinimumBonusLore(int minimumBonusLore) {
		mythicTier.setMinimumBonusLore(minimumBonusLore);
		return this;
	}

	public MythicTierBuilder withMaximumBonusLore(int maximumBonusLore) {
		mythicTier.setMaximumBonusLore(maximumBonusLore);
		return this;
	}

	public MythicTierBuilder withBaseEnchantments(Set<MythicEnchantment> baseEnchantments) {
		mythicTier.setBaseEnchantments(baseEnchantments);
		return this;
	}

	public MythicTierBuilder withBonusEnchantments(Set<MythicEnchantment> bonusEnchantments) {
		mythicTier.setBonusEnchantments(bonusEnchantments);
		return this;
	}

	public MythicTierBuilder withSafeBaseEnchantments(boolean b) {
		mythicTier.setSafeBaseEnchantments(b);
		return this;
	}

	public MythicTierBuilder withSafeBonusEnchantments(boolean b) {
		mythicTier.setSafeBonusEnchantments(b);
		return this;
	}

	public MythicTierBuilder withHighBaseEnchantments(boolean b) {
		mythicTier.setAllowHighBaseEnchantments(b);
		return this;
	}

	public MythicTierBuilder withHighBonusEnchantments(boolean b) {
		mythicTier.setAllowHighBonusEnchantments(b);
		return this;
	}

	public MythicTierBuilder withMinimumBonusEnchantments(int minimumBonusEnchantments) {
		mythicTier.setMinimumBonusEnchantments(minimumBonusEnchantments);
		return this;
	}

	public MythicTierBuilder withMaximumBonusEnchantments(int maximumBonusEnchantments) {
		mythicTier.setMaximumBonusEnchantments(maximumBonusEnchantments);
		return this;
	}

	public MythicTierBuilder withMinimumDurabilityPercentage(double minimumDurabilityPercentage) {
		mythicTier.setMinimumDurabilityPercentage(minimumDurabilityPercentage);
		return this;
	}

	public MythicTierBuilder withMaximumDurabilityPercentage(double maximumDurabilityPercentage) {
		mythicTier.setMaximumDurabilityPercentage(maximumDurabilityPercentage);
		return this;
	}

	public MythicTierBuilder withWorldSpawnChanceMap(Map<String, Double> worldSpawnChanceMap) {
		mythicTier.setWorldSpawnChanceMap(worldSpawnChanceMap);
		return this;
	}

	public MythicTierBuilder withWorldDropChanceMap(Map<String, Double> worldDropChanceMap) {
		mythicTier.setWorldDropChanceMap(worldDropChanceMap);
		return this;
	}

	public MythicTierBuilder withAllowedItemGroups(List<String> allowedItemGroups) {
		mythicTier.setAllowedItemGroups(allowedItemGroups);
		return this;
	}

	public MythicTierBuilder withDisallowedItemGroups(List<String> disallowedItemGroups) {
		mythicTier.setDisallowedItemGroups(disallowedItemGroups);
		return this;
	}

	public MythicTierBuilder withAllowedItemIds(List<String> allowedItemIds) {
		mythicTier.setAllowedItemIds(allowedItemIds);
		return this;
	}

	public MythicTierBuilder withDisallowedItemIds(List<String> disallowedItemIds) {
		mythicTier.setDisallowedItemIds(disallowedItemIds);
		return this;
	}

	public MythicTierBuilder withMinimumSockets(int minimumSockets) {
		mythicTier.setMinimumSockets(minimumSockets);
		return this;
	}

	public MythicTierBuilder withMaximumSockets(int maximumSockets) {
		mythicTier.setMaximumSockets(maximumSockets);
		return this;
	}

	public Tier build() {
		return mythicTier;
	}

}
