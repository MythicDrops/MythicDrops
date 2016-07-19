/**
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2013 Teal Cube Games
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
package com.tealcube.minecraft.bukkit.mythicdrops.tiers;

import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.ChatColor;

import java.util.HashSet;
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
        mythicTier.setBaseEnchantments(baseEnchantments != null ? baseEnchantments : new HashSet<MythicEnchantment>());
        return this;
    }

    public MythicTierBuilder withBonusEnchantments(Set<MythicEnchantment> bonusEnchantments) {
        mythicTier
                .setBonusEnchantments(bonusEnchantments != null ? bonusEnchantments : new HashSet<MythicEnchantment>());
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

    @Deprecated
    public MythicTierBuilder withWorldSpawnChanceMap(Map<String, Double> worldSpawnChanceMap) {
        mythicTier.setWorldSpawnChanceMap(worldSpawnChanceMap);
        return this;
    }

    public MythicTierBuilder withSpawnChance(double d) {
        mythicTier.setSpawnChance(d);
        return this;
    }

    @Deprecated
    public MythicTierBuilder withWorldDropChanceMap(Map<String, Double> worldDropChanceMap) {
        mythicTier.setWorldDropChanceMap(worldDropChanceMap);
        return this;
    }

    public MythicTierBuilder withDropChance(double d) {
        mythicTier.setDropChance(d);
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

    @Deprecated
    public MythicTierBuilder withWorldIdentifyChanceMap(Map<String, Double> worldIdentifyChanceMap) {
        mythicTier.setWorldIdentifyChanceMap(worldIdentifyChanceMap);
        return this;
    }

    public MythicTierBuilder withIdentifyChance(double d) {
        mythicTier.setIdentifyChance(d);
        return this;
    }

    public MythicTierBuilder withChanceToHaveSockets(double chance) {
        mythicTier.setChanceToHaveSockets(chance);
        return this;
    }

    public MythicTierBuilder withBroadcastOnFind(boolean broadcastOnFind) {
        mythicTier.setBroadcastOnFind(broadcastOnFind);
        return this;
    }

    public MythicTierBuilder withOptimalDistance(int distance) {
        mythicTier.setOptimalDistance(distance);
        return this;
    }

    public MythicTierBuilder withMaximumDistance(int distance) {
        mythicTier.setMaximumDistance(distance);
        return this;
    }

    public MythicTierBuilder withInfiniteDurability(boolean b) {
        mythicTier.setInfiniteDurability(b);
        return this;
    }

    public Tier build() {
        return mythicTier;
    }

}
