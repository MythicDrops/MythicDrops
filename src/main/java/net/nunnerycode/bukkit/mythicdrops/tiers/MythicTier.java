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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicEnchantment;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.ChatColor;

/**
 * A class holding all information about a {@link net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier} of items used by MythicDrops.
 */
public class MythicTier implements Tier {

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

	public MythicTier() {
		this.allowedGroups = new HashSet<String>();
		this.disallowedGroups = new HashSet<String>();
		this.allowedIds = new HashSet<String>();
		this.disallowedIds = new HashSet<String>();
		this.tierName = "";
		this.tierDisplayName = "";
		this.tierDisplayColor = null;
		this.tierIdentificationColor = null;
		this.safeBaseEnchantments = true;
		this.safeBonusEnchantments = true;
		this.allowHighBaseEnchantments = false;
		this.allowHighBonusEnchantments = false;
		this.minimumAmountOfBonusEnchantments = 0;
		this.maximumAmountOfBonusEnchantments = 0;
		this.chanceToSpawnOnAMonster = 0.0;
		this.chanceToDropOnMonsterDeath = 0.0;
		this.minimumDurabilityPercentage = 1.0;
		this.maximumDurabilityPercentage = 1.0;
		this.baseEnchantments = new HashSet<MythicEnchantment>();
		this.bonusEnchantments = new HashSet<MythicEnchantment>();
	}

	public Set<String> getAllowedGroups() {
		return allowedGroups;
	}

	/**
	 * Sets the item groups that can spawn for this MythicTier.
	 *
	 * @param allowedGroups groups that can spawn for this MythicTier
	 */
	public void setAllowedGroups(final Set<String> allowedGroups) {
		this.allowedGroups = allowedGroups;
	}

	public Set<String> getDisallowedGroups() {
		return disallowedGroups;
	}

	/**
	 * Sets the item groups that cannot spawn for this MythicTier.
	 *
	 * @param disallowedGroups groups that cannot spawn for this MythicTier
	 */
	public void setDisallowedGroups(final Set<String> disallowedGroups) {
		this.disallowedGroups = disallowedGroups;
	}

	public Set<String> getAllowedIds() {
		return allowedIds;
	}

	/**
	 * Sets the item ids that can spawn for this MythicTier.
	 *
	 * @param allowedIds item ids that can spawn for this tier
	 */
	public void setAllowedIds(final Set<String> allowedIds) {
		this.allowedIds = allowedIds;
	}

	public Set<String> getDisallowedIds() {
		return disallowedIds;
	}

	/**
	 * Sets the item ids that are cannot spawn for this MythicTier.
	 *
	 * @param disallowedIds item ids that cannot spawn for this MythicTier
	 */
	public void setDisallowedIds(final Set<String> disallowedIds) {
		this.disallowedIds = disallowedIds;
	}

	public String getTierName() {
		return tierName;
	}

	/**
	 * Sets the name of the MythicTier.
	 *
	 * @param tierName name of the MythicTier
	 */
	public void setTierName(final String tierName) {
		this.tierName = tierName;
	}

	public String getTierDisplayName() {
		return tierDisplayName;
	}

	/**
	 * Sets the display name of the MythicTier.
	 *
	 * @param tierDisplayName new display name of the MythicTier
	 */
	public void setTierDisplayName(final String tierDisplayName) {
		this.tierDisplayName = tierDisplayName;
	}

	public ChatColor getTierDisplayColor() {
		return tierDisplayColor;
	}

	public void setTierDisplayColor(final ChatColor tierDisplayColor) throws RuntimeException {
		if (tierDisplayColor == this.tierIdentificationColor) {
			throw new RuntimeException("Tier display color cannot match identification color");
		}
		this.tierDisplayColor = tierDisplayColor;
	}

	public ChatColor getTierIdentificationColor() {
		return tierIdentificationColor;
	}

	public void setTierIdentificationColor(final ChatColor tierIdentificationColor) {
		if (tierIdentificationColor == this.tierDisplayColor) {
			throw new RuntimeException("Tier identification color cannot match display color");
		}
		this.tierIdentificationColor = tierIdentificationColor;
	}

	public boolean isSafeBaseEnchantments() {
		return safeBaseEnchantments;
	}

	public void setSafeBaseEnchantments(final boolean safeBaseEnchantments) {
		this.safeBaseEnchantments = safeBaseEnchantments;
	}

	public boolean isSafeBonusEnchantments() {
		return safeBonusEnchantments;
	}

	public void setSafeBonusEnchantments(final boolean safeBonusEnchantments) {
		this.safeBonusEnchantments = safeBonusEnchantments;
	}

	public boolean isAllowHighBaseEnchantments() {
		return allowHighBaseEnchantments;
	}

	public void setAllowHighBaseEnchantments(final boolean allowHighBaseEnchantments) {
		this.allowHighBaseEnchantments = allowHighBaseEnchantments;
	}

	public boolean isAllowHighBonusEnchantments() {
		return allowHighBonusEnchantments;
	}

	public void setAllowHighBonusEnchantments(final boolean allowHighBonusEnchantments) {
		this.allowHighBonusEnchantments = allowHighBonusEnchantments;
	}

	public int getMinimumAmountOfBonusEnchantments() {
		return minimumAmountOfBonusEnchantments;
	}

	public void setMinimumAmountOfBonusEnchantments(final int minimumAmountOfBonusEnchantments) {
		this.minimumAmountOfBonusEnchantments = minimumAmountOfBonusEnchantments;
	}

	public int getMaximumAmountOfBonusEnchantments() {
		return maximumAmountOfBonusEnchantments;
	}

	public void setMaximumAmountOfBonusEnchantments(final int maximumAmountOfBonusEnchantments) {
		this.maximumAmountOfBonusEnchantments = maximumAmountOfBonusEnchantments;
	}

	public double getChanceToSpawnOnAMonster() {
		return chanceToSpawnOnAMonster;
	}

	public void setChanceToSpawnOnAMonster(final double chanceToSpawnOnAMonster) {
		this.chanceToSpawnOnAMonster = chanceToSpawnOnAMonster;
	}

	public double getChanceToDropOnMonsterDeath() {
		return chanceToDropOnMonsterDeath;
	}

	public void setChanceToDropOnMonsterDeath(final double chanceToDropOnMonsterDeath) {
		this.chanceToDropOnMonsterDeath = chanceToDropOnMonsterDeath;
	}

	public double getMinimumDurabilityPercentage() {
		return minimumDurabilityPercentage;
	}

	public void setMinimumDurabilityPercentage(final double minimumDurabilityPercentage) {
		this.minimumDurabilityPercentage = minimumDurabilityPercentage;
	}

	public double getMaximumDurabilityPercentage() {
		return maximumDurabilityPercentage;
	}

	public void setMaximumDurabilityPercentage(final double maximumDurabilityPercentage) {
		this.maximumDurabilityPercentage = maximumDurabilityPercentage;
	}

	@Override
	public Set<MythicEnchantment> getBaseEnchantments() {
		return baseEnchantments;
	}

	public void setBaseEnchantments(final Set<MythicEnchantment> baseEnchantments) {
		this.baseEnchantments = baseEnchantments;
	}

	@Override
	public Set<MythicEnchantment> getBonusEnchantments() {
		return bonusEnchantments;
	}

	public void setBonusEnchantments(final Set<MythicEnchantment> bonusEnchantments) {
		this.bonusEnchantments = bonusEnchantments;
	}

	@Override
	public String toString() {
		return "MythicTier{" +
				"allowedGroups=" + allowedGroups +
				", disallowedGroups=" + disallowedGroups +
				", allowedIds=" + allowedIds +
				", disallowedIds=" + disallowedIds +
				", tierName='" + tierName + '\'' +
				", tierDisplayName='" + tierDisplayName + '\'' +
				", tierDisplayColor=" + tierDisplayColor +
				", tierIdentificationColor=" + tierIdentificationColor +
				", safeBaseEnchantments=" + safeBaseEnchantments +
				", safeBonusEnchantments=" + safeBonusEnchantments +
				", allowHighBaseEnchantments=" + allowHighBaseEnchantments +
				", allowHighBonusEnchantments=" + allowHighBonusEnchantments +
				", minimumAmountOfBonusEnchantments=" + minimumAmountOfBonusEnchantments +
				", maximumAmountOfBonusEnchantments=" + maximumAmountOfBonusEnchantments +
				", chanceToSpawnOnAMonster=" + chanceToSpawnOnAMonster +
				", chanceToDropOnMonsterDeath=" + chanceToDropOnMonsterDeath +
				", minimumDurabilityPercentage=" + minimumDurabilityPercentage +
				", maximumDurabilityPercentage=" + maximumDurabilityPercentage +
				", baseEnchantments=" + baseEnchantments +
				", bonusEnchantments=" + bonusEnchantments +
				", tierLore=" + tierLore +
				'}';
	}

	public List<String> getTierLore() {
		return tierLore;
	}

	public void setTierLore(List<String> tierLore) {
		this.tierLore = tierLore;
	}
}