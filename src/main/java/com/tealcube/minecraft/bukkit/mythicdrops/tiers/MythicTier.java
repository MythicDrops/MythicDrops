package com.tealcube.minecraft.bukkit.mythicdrops.tiers;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class MythicTier implements Tier {

    private final String name;
    private String displayName;
    private ChatColor displayColor;
    private ChatColor identificationColor;
    private List<String> baseLore;
    private List<String> bonusLore;
    private int minimumBonusLore;
    private int maximumBonusLore;
    private Set<MythicEnchantment> baseEnchantments;
    private Set<MythicEnchantment> bonusEnchantments;
    private boolean safeBaseEnchantments;
    private boolean safeBonusEnchantments;
    private boolean allowHighBaseEnchantments;
    private boolean allowHighBonusEnchantments;
    private int minimumBonusEnchantments;
    private int maximumBonusEnchantments;
    private double minimumDurabilityPercentage;
    private double maximumDurabilityPercentage;
    private List<String> allowedItemGroups;
    private List<String> disallowedItemGroups;
    private List<String> allowedItemIds;
    private List<String> disallowedItemIds;
    private int minimumSockets;
    private int maximumSockets;
    private double chanceToHaveSockets;
    private boolean broadcastOnFind;
    private double spawnChance;
    private double dropChance;
    private double identifyChance;
    private int optimalDistance = -1;
    private int maximumDistance = -1;
    private boolean infiniteDurability;

    protected MythicTier(String name) {
        this.name = name;
        baseLore = new ArrayList<>();
        bonusLore = new ArrayList<>();
        baseEnchantments = new HashSet<>();
        bonusEnchantments = new HashSet<>();
        allowedItemGroups = new ArrayList<>();
        disallowedItemGroups = new ArrayList<>();
        allowedItemIds = new ArrayList<>();
        disallowedItemIds = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public ChatColor getDisplayColor() {
        return displayColor;
    }

    void setDisplayColor(ChatColor displayColor) {
        this.displayColor = displayColor;
    }

    @Override
    public ChatColor getIdentificationColor() {
        return identificationColor;
    }

    void setIdentificationColor(ChatColor identificationColor) {
        this.identificationColor = identificationColor;
    }

    @Override
    public List<String> getBaseLore() {
        return baseLore;
    }

    void setBaseLore(List<String> baseLore) {
        this.baseLore = baseLore;
    }

    @Override
    public List<String> getBonusLore() {
        return bonusLore;
    }

    void setBonusLore(List<String> bonusLore) {
        this.bonusLore = bonusLore;
    }

    @Override
    public int getMinimumBonusLore() {
        return minimumBonusLore;
    }

    void setMinimumBonusLore(int minimumBonusLore) {
        this.minimumBonusLore = minimumBonusLore;
    }

    @Override
    public int getMaximumBonusLore() {
        return maximumBonusLore;
    }

    void setMaximumBonusLore(int maximumBonusLore) {
        this.maximumBonusLore = maximumBonusLore;
    }

    @Override
    public Set<MythicEnchantment> getBaseEnchantments() {
        return baseEnchantments;
    }

    void setBaseEnchantments(Set<MythicEnchantment> baseEnchantments) {
        this.baseEnchantments = baseEnchantments;
    }

    @Override
    public Set<MythicEnchantment> getBonusEnchantments() {
        return bonusEnchantments;
    }

    void setBonusEnchantments(Set<MythicEnchantment> bonusEnchantments) {
        this.bonusEnchantments = bonusEnchantments;
    }

    @Override
    public boolean isSafeBaseEnchantments() {
        return safeBaseEnchantments;
    }

    void setSafeBaseEnchantments(boolean safeBaseEnchantments) {
        this.safeBaseEnchantments = safeBaseEnchantments;
    }

    @Override
    public boolean isSafeBonusEnchantments() {
        return safeBonusEnchantments;
    }

    void setSafeBonusEnchantments(boolean safeBonusEnchantments) {
        this.safeBonusEnchantments = safeBonusEnchantments;
    }

    @Override
    public boolean isAllowHighBaseEnchantments() {
        return allowHighBaseEnchantments;
    }

    void setAllowHighBaseEnchantments(boolean allowHighBaseEnchantments) {
        this.allowHighBaseEnchantments = allowHighBaseEnchantments;
    }

    @Override
    public boolean isAllowHighBonusEnchantments() {
        return allowHighBonusEnchantments;
    }

    void setAllowHighBonusEnchantments(boolean allowHighBonusEnchantments) {
        this.allowHighBonusEnchantments = allowHighBonusEnchantments;
    }

    @Override
    public int getMinimumBonusEnchantments() {
        return minimumBonusEnchantments;
    }

    void setMinimumBonusEnchantments(int minimumBonusEnchantments) {
        this.minimumBonusEnchantments = minimumBonusEnchantments;
    }

    @Override
    public int getMaximumBonusEnchantments() {
        return maximumBonusEnchantments;
    }

    void setMaximumBonusEnchantments(int maximumBonusEnchantments) {
        this.maximumBonusEnchantments = maximumBonusEnchantments;
    }

    @Override
    public double getMaximumDurabilityPercentage() {
        return maximumDurabilityPercentage;
    }

    void setMaximumDurabilityPercentage(double maximumDurabilityPercentage) {
        this.maximumDurabilityPercentage = maximumDurabilityPercentage;
    }

    @Override
    public double getMinimumDurabilityPercentage() {
        return minimumDurabilityPercentage;
    }

    void setMinimumDurabilityPercentage(double minimumDurabilityPercentage) {
        this.minimumDurabilityPercentage = minimumDurabilityPercentage;
    }

    @Override
    public Map<String, Double> getWorldDropChanceMap() {
        return new HashMap<>();
    }

    void setWorldDropChanceMap(Map<String, Double> worldDropChanceMap) {
        // do nothing
    }

    @Override
    public double getDropChance() {
        return dropChance;
    }

    public void setDropChance(double dropChance) {
        this.dropChance = dropChance;
    }

    @Override
    public Map<String, Double> getWorldSpawnChanceMap() {
        return new HashMap<>();
    }

    void setWorldSpawnChanceMap(Map<String, Double> worldSpawnChanceMap) {
        // do nothing
    }

    @Override
    public double getSpawnChance() {
        return spawnChance;
    }

    public void setSpawnChance(double spawnChance) {
        this.spawnChance = spawnChance;
    }

    @Override
    public Map<String, Double> getWorldIdentifyChanceMap() {
        return new HashMap<>();
    }

    public void setWorldIdentifyChanceMap(Map<String, Double> map) {
        // do nothing
    }

    @Override
    public double getIdentifyChance() {
        return identifyChance;
    }

    public void setIdentifyChance(double identifyChance) {
        this.identifyChance = identifyChance;
    }

    @Override
    public List<String> getAllowedItemGroups() {
        return allowedItemGroups;
    }

    void setAllowedItemGroups(List<String> allowedItemGroups) {
        this.allowedItemGroups = allowedItemGroups;
    }

    @Override
    public List<String> getDisallowedItemGroups() {
        return disallowedItemGroups;
    }

    void setDisallowedItemGroups(List<String> disallowedItemGroups) {
        this.disallowedItemGroups = disallowedItemGroups;
    }

    @Override
    public List<String> getAllowedItemIds() {
        return allowedItemIds;
    }

    void setAllowedItemIds(List<String> allowedItemIds) {
        this.allowedItemIds = allowedItemIds;
    }

    @Override
    public List<String> getDisallowedItemIds() {
        return disallowedItemIds;
    }

    void setDisallowedItemIds(List<String> disallowedItemIds) {
        this.disallowedItemIds = disallowedItemIds;
    }

    @Override
    public int getMinimumSockets() {
        return minimumSockets;
    }

    public void setMinimumSockets(int minimumSockets) {
        this.minimumSockets = minimumSockets;
    }

    @Override
    public int getMaximumSockets() {
        return maximumSockets;
    }

    public void setMaximumSockets(int maximumSockets) {
        this.maximumSockets = maximumSockets;
    }

    @Override
    public double getChanceToHaveSockets() {
        return chanceToHaveSockets;
    }

    public void setChanceToHaveSockets(double chanceToHaveSockets) {
        this.chanceToHaveSockets = chanceToHaveSockets;
    }

    @Override
    public boolean isBroadcastOnFind() {
        return broadcastOnFind;
    }

    public void setBroadcastOnFind(boolean broadcastOnFind) {
        this.broadcastOnFind = broadcastOnFind;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (displayColor != null ? displayColor.hashCode() : 0);
        result = 31 * result + (identificationColor != null ? identificationColor.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MythicTier)) {
            return false;
        }

        MythicTier that = (MythicTier) o;

        return displayColor == that.displayColor && identificationColor == that.identificationColor
               && !(name !=
                    null ? !name.equals(that.name) : that.name != null);
    }

    @Override
    public String toString() {
        return "MythicTier{" +
               "name='" + name + '\'' +
               '}';
    }

    @Override
    public int compareTo(Tier o) {
        if (o == null || this.equals(o)) {
            return 0;
        }
        return Double.compare(this.getSpawnChance(), o.getSpawnChance());
    }

    @Override
    public int getOptimalDistance() {
        return optimalDistance;
    }

    public void setOptimalDistance(int optimalDistance) {
        this.optimalDistance = optimalDistance;
    }

    @Override
    public int getMaximumDistance() {
        return maximumDistance;
    }

    public void setMaximumDistance(int maximumDistance) {
        this.maximumDistance = maximumDistance;
    }

    @Override
    public boolean isInfiniteDurability() {
        return infiniteDurability;
    }

    void setInfiniteDurability(boolean infiniteDurability) {
        this.infiniteDurability = infiniteDurability;
    }

}
