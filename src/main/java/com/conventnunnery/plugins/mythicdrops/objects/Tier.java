/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.mythicdrops.objects;

import org.bukkit.ChatColor;

import java.util.Set;

public class Tier {

	private final String name;
	private final String displayName;
	private final ChatColor displayColor;
	private final ChatColor identificationColor;
	private final boolean safeBaseEnchantments;
	private final boolean safeBonusEnchantments;
	private final int minimumBonusEnchantments;
	private final int maximumBonusEnchantments;
	private final Set<MythicEnchantment> baseEnchantments;
	private final Set<MythicEnchantment> bonusEnchantments;
	private final double chanceToSpawnOnAMonster;
	private final double chanceToDropOnMonsterDeath;
	private final double minimumDurability;
	private final double maximumDurability;
	private final int minimumSockets;

	public int getMaximumSockets() {
		return maximumSockets;
	}

	private final int maximumSockets;
	private final Set<String> allowedGroups;
	private final Set<String> disallowedGroups;
	private final Set<String> allowedIds;
	private final Set<String> disallowedIds;

	public Tier(String tierName, String tierDisplayName, ChatColor tierDisplayColor,
	            ChatColor tierIdentificationColor, boolean tierSafeBaseEnchantments, boolean tierSafeBonusEnchantments,
	            int tierMinimumBonusEnchantments, int tierMaximumBonusEnchantments,
	            Set<MythicEnchantment> tierBaseEnchantments,
	            Set<MythicEnchantment> tierBonusEnchantments, double tierChanceToSpawnOnAMonster,
	            double tierChanceToDropOnMonsterDeath, double tierMinimumDurability, double tierMaximumDurability,
	            int tierMinimumSockets, int tierMaximumSockets, Set<String> tierAllowedGroups,
	            Set<String> tierDisallowedGroups,
	            Set<String> tierAllowedIds, Set<String> tierDisallowedIds) {
		this.name = tierName;
		this.displayName = tierDisplayName;
		this.displayColor = tierDisplayColor;
		this.identificationColor = tierIdentificationColor;
		this.safeBaseEnchantments = tierSafeBaseEnchantments;
		this.safeBonusEnchantments = tierSafeBonusEnchantments;
		this.minimumBonusEnchantments = tierMinimumBonusEnchantments;
		this.maximumBonusEnchantments = tierMaximumBonusEnchantments;
		this.baseEnchantments = tierBaseEnchantments;
		this.bonusEnchantments = tierBonusEnchantments;
		this.chanceToSpawnOnAMonster = tierChanceToSpawnOnAMonster;
		this.chanceToDropOnMonsterDeath = tierChanceToDropOnMonsterDeath;
		this.minimumDurability = tierMinimumDurability;
		this.maximumDurability = tierMaximumDurability;
		this.minimumSockets = tierMinimumSockets;
		this.maximumSockets = tierMaximumSockets;
		this.allowedGroups = tierAllowedGroups;
		this.disallowedGroups = tierDisallowedGroups;
		this.allowedIds = tierAllowedIds;
		this.disallowedIds = tierDisallowedIds;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public ChatColor getDisplayColor() {
		return displayColor;
	}

	public ChatColor getIdentificationColor() {
		return identificationColor;
	}

	public boolean isSafeBaseEnchantments() {
		return safeBaseEnchantments;
	}

	public boolean isSafeBonusEnchantments() {
		return safeBonusEnchantments;
	}

	public int getMinimumBonusEnchantments() {
		return minimumBonusEnchantments;
	}

	public int getMaximumBonusEnchantments() {
		return maximumBonusEnchantments;
	}

	public Set<MythicEnchantment> getBaseEnchantments() {
		return baseEnchantments;
	}

	public Set<MythicEnchantment> getBonusEnchantments() {
		return bonusEnchantments;
	}

	public double getChanceToSpawnOnAMonster() {
		return chanceToSpawnOnAMonster;
	}

	public double getChanceToDropOnMonsterDeath() {
		return chanceToDropOnMonsterDeath;
	}

	public double getMinimumDurability() {
		return minimumDurability;
	}

	public double getMaximumDurability() {
		return maximumDurability;
	}

	public int getMinimumSockets() {
		return minimumSockets;
	}

	public Set<String> getAllowedGroups() {
		return allowedGroups;
	}

	public Set<String> getDisallowedGroups() {
		return disallowedGroups;
	}

	public Set<String> getAllowedIds() {
		return allowedIds;
	}

	public Set<String> getDisallowedIds() {
		return disallowedIds;
	}

}
