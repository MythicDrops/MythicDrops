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

package com.conventnunnery.plugins.mythicdrops.tiers;

import com.conventnunnery.libraries.utils.RandomUtils;
import com.conventnunnery.plugins.mythicdrops.api.tiers.Tier;
import org.bukkit.ChatColor;

import java.util.HashSet;
import java.util.Set;

/**
 * A class holding all information about a {@link Tier} of items used by MythicDrops.
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
    private boolean allowSpawningWithSockets;
    private int minimumAmountOfBonusEnchantments;
    private int maximumAmountOfBonusEnchantments;
    private int minimumAmountOfSockets;
    private int maximumAmountOfSockets;
    private double chanceToSpawnOnAMonster;
    private double chanceToDropOnMonsterDeath;
    private double chanceToBeIdentified;
    private double minimumDurabilityPercentage;
    private double maximumDurabilityPercentage;

    public MythicTier(final Set<String> allowedGroups, final Set<String> disallowedGroups, final Set<String> allowedIds,
                      final Set<String> disallowedIds, final String tierName, final String tierDisplayName,
                      final ChatColor tierDisplayColor, final ChatColor tierIdentificationColor,
                      final boolean safeBaseEnchantments, final boolean safeBonusEnchantments,
                      final boolean allowHighBaseEnchantments, final boolean allowHighBonusEnchantments,
                      final boolean allowSpawningWithSockets, final int minimumAmountOfBonusEnchantments,
                      final int maximumAmountOfBonusEnchantments, final int minimumAmountOfSockets,
                      final int maximumAmountOfSockets, final double chanceToSpawnOnAMonster,
                      final double chanceToDropOnMonsterDeath, final double chanceToBeIdentified,
                      final double minimumDurabilityPercentage, final double maximumDurabilityPercentage) {
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
        this.allowSpawningWithSockets = allowSpawningWithSockets;
        this.minimumAmountOfBonusEnchantments = minimumAmountOfBonusEnchantments;
        this.maximumAmountOfBonusEnchantments = maximumAmountOfBonusEnchantments;
        this.minimumAmountOfSockets = minimumAmountOfSockets;
        this.maximumAmountOfSockets = maximumAmountOfSockets;
        this.chanceToSpawnOnAMonster = chanceToSpawnOnAMonster;
        this.chanceToDropOnMonsterDeath = chanceToDropOnMonsterDeath;
        this.chanceToBeIdentified = chanceToBeIdentified;
        this.minimumDurabilityPercentage = minimumDurabilityPercentage;
        this.maximumDurabilityPercentage = maximumDurabilityPercentage;
    }

    public MythicTier() {
        this.allowedGroups = new HashSet<String>();
        this.disallowedGroups = new HashSet<String>();
        this.allowedIds = new HashSet<String>();
        this.disallowedIds = new HashSet<String>();
        this.tierName = "";
        this.tierDisplayName = "";
        this.tierDisplayColor = ChatColor.values()[((int) RandomUtils
                .randomRangeWholeExclusive(0, ChatColor.values().length))];
        while (this.tierIdentificationColor == null || this.tierIdentificationColor == this.tierDisplayColor) {
            this.tierIdentificationColor = ChatColor.values()[((int) RandomUtils
                    .randomRangeWholeExclusive(0, ChatColor.values().length))];
        }
        this.safeBaseEnchantments = true;
        this.safeBonusEnchantments = true;
        this.allowHighBaseEnchantments = false;
        this.allowHighBonusEnchantments = false;
        this.allowSpawningWithSockets = true;
        this.minimumAmountOfBonusEnchantments = 0;
        this.maximumAmountOfBonusEnchantments = 0;
        this.minimumAmountOfSockets = 0;
        this.maximumAmountOfSockets = 0;
        this.chanceToSpawnOnAMonster = 0.0;
        this.chanceToDropOnMonsterDeath = 0.0;
        this.chanceToBeIdentified = 0.0;
        this.minimumDurabilityPercentage = 1.0;
        this.maximumDurabilityPercentage = 1.0;
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

    public void setTierDisplayColor(final ChatColor tierDisplayColor) {
        this.tierDisplayColor = tierDisplayColor;
    }

    public ChatColor getTierIdentificationColor() {
        return tierIdentificationColor;
    }

    public void setTierIdentificationColor(final ChatColor tierIdentificationColor) {
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

    public boolean isAllowSpawningWithSockets() {
        return allowSpawningWithSockets;
    }

    public void setAllowSpawningWithSockets(final boolean allowSpawningWithSockets) {
        this.allowSpawningWithSockets = allowSpawningWithSockets;
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

    public int getMinimumAmountOfSockets() {
        return minimumAmountOfSockets;
    }

    public void setMinimumAmountOfSockets(final int minimumAmountOfSockets) {
        this.minimumAmountOfSockets = minimumAmountOfSockets;
    }

    public int getMaximumAmountOfSockets() {
        return maximumAmountOfSockets;
    }

    public void setMaximumAmountOfSockets(final int maximumAmountOfSockets) {
        this.maximumAmountOfSockets = maximumAmountOfSockets;
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

    public double getChanceToBeIdentified() {
        return chanceToBeIdentified;
    }

    public void setChanceToBeIdentified(final double chanceToBeIdentified) {
        this.chanceToBeIdentified = chanceToBeIdentified;
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

}
