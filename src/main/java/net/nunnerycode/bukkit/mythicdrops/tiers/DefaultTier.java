/*
 * Copyright (C) 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.nunnerycode.bukkit.mythicdrops.tiers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicEnchantment;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.ChatColor;

public enum DefaultTier implements Tier {
	CUSTOM_ITEM(new HashSet<String>(), new HashSet<String>(), new HashSet<String>(), new HashSet<String>(),
			"CustomItem", "Custom Item", ChatColor.LIGHT_PURPLE, ChatColor.LIGHT_PURPLE, false, false, false,
			false, 0, 0, 0.0, 0.0, 1.0, 1.0, new HashSet<MythicEnchantment>(), new HashSet<MythicEnchantment>(),
			new ArrayList<String>());
	private Set<String> allowedGroups;
	private Set<String> disallowedGroups;
	private Set<String> allowedIds;
	private Set<String> disallowedIds;
	private String tierName;
	private String tierDisplayName;
	private ChatColor tierDisplayColor;
	private ChatColor tierIdentificationColor;
	private boolean safeBaseEnchantments;
	private boolean safeBonusEnchantments;
	private boolean allowHighBaseEnchantments;
	private boolean allowHighBonusEnchantments;
	private int minimumAmountOfBonusEnchantments;
	private int maximumAmountOfBonusEnchantments;
	private double chanceToSpawnOnAMonster;
	private double chanceToDropOnMonsterDeath;
	private double minimumDurabilityPercentage;
	private double maximumDurabilityPercentage;
	private Set<MythicEnchantment> baseEnchantments;
	private Set<MythicEnchantment> bonusEnchantments;
	private List<String> tierLore;

	private DefaultTier(final Set<String> allowedGroups, final Set<String> disallowedGroups,
						final Set<String> allowedIds,
						final Set<String> disallowedIds, final String tierName, final String tierDisplayName,
						final ChatColor tierDisplayColor, final ChatColor tierIdentificationColor,
						final boolean safeBaseEnchantments, final boolean safeBonusEnchantments,
						final boolean allowHighBaseEnchantments, final boolean allowHighBonusEnchantments,
						final int minimumAmountOfBonusEnchantments,
						final int maximumAmountOfBonusEnchantments, final double chanceToSpawnOnAMonster,
						final double chanceToDropOnMonsterDeath, final double minimumDurabilityPercentage,
						final double maximumDurabilityPercentage, final Set<MythicEnchantment> baseEnchantments,
						final Set<MythicEnchantment> bonusEnchantments, final List<String> tierLore) {
		this.allowedGroups = allowedGroups;
		this.disallowedGroups = disallowedGroups;
		this.allowedIds = allowedIds;
		this.disallowedIds = disallowedIds;
		this.tierName = tierName;
		this.tierDisplayName = tierDisplayName;
		this.tierDisplayColor = tierDisplayColor;
		this.tierIdentificationColor = tierIdentificationColor;
		this.safeBaseEnchantments = safeBaseEnchantments;
		this.safeBonusEnchantments = safeBonusEnchantments;
		this.allowHighBaseEnchantments = allowHighBaseEnchantments;
		this.allowHighBonusEnchantments = allowHighBonusEnchantments;
		this.minimumAmountOfBonusEnchantments = minimumAmountOfBonusEnchantments;
		this.maximumAmountOfBonusEnchantments = maximumAmountOfBonusEnchantments;
		this.chanceToSpawnOnAMonster = chanceToSpawnOnAMonster;
		this.chanceToDropOnMonsterDeath = chanceToDropOnMonsterDeath;
		this.minimumDurabilityPercentage = minimumDurabilityPercentage;
		this.maximumDurabilityPercentage = maximumDurabilityPercentage;
		this.baseEnchantments = baseEnchantments;
		this.bonusEnchantments = bonusEnchantments;
		this.tierLore = tierLore;
	}

	@Override
	public Set<String> getAllowedGroups() {
		return allowedGroups;
	}

	@Override
	public Set<String> getDisallowedGroups() {
		return disallowedGroups;
	}

	@Override
	public Set<String> getAllowedIds() {
		return allowedIds;
	}

	@Override
	public Set<String> getDisallowedIds() {
		return disallowedIds;
	}

	@Override
	public String getTierName() {
		return tierName;
	}

	@Override
	public String getTierDisplayName() {
		return tierDisplayName;
	}

	@Override
	public ChatColor getTierDisplayColor() {
		return tierDisplayColor;
	}

	@Override
	public ChatColor getTierIdentificationColor() {
		return tierIdentificationColor;
	}

	@Override
	public boolean isSafeBaseEnchantments() {
		return safeBaseEnchantments;
	}

	@Override
	public boolean isSafeBonusEnchantments() {
		return safeBonusEnchantments;
	}

	@Override
	public boolean isAllowHighBaseEnchantments() {
		return allowHighBaseEnchantments;
	}

	@Override
	public boolean isAllowHighBonusEnchantments() {
		return allowHighBonusEnchantments;
	}

	@Override
	public int getMinimumAmountOfBonusEnchantments() {
		return minimumAmountOfBonusEnchantments;
	}

	@Override
	public int getMaximumAmountOfBonusEnchantments() {
		return maximumAmountOfBonusEnchantments;
	}

	@Override
	public double getChanceToSpawnOnAMonster() {
		return chanceToSpawnOnAMonster;
	}

	@Override
	public double getChanceToDropOnMonsterDeath() {
		return chanceToDropOnMonsterDeath;
	}

	@Override
	public double getMinimumDurabilityPercentage() {
		return minimumDurabilityPercentage;
	}

	@Override
	public double getMaximumDurabilityPercentage() {
		return maximumDurabilityPercentage;
	}

	@Override
	public Set<MythicEnchantment> getBaseEnchantments() {
		return baseEnchantments;
	}

	@Override
	public Set<MythicEnchantment> getBonusEnchantments() {
		return bonusEnchantments;
	}

	@Override
	public List<String> getTierLore() {
		return tierLore;
	}
}