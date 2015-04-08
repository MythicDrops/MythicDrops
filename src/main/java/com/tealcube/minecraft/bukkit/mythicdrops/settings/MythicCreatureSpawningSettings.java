package com.tealcube.minecraft.bukkit.mythicdrops.settings;

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


import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.CreatureSpawningSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;

import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class MythicCreatureSpawningSettings implements CreatureSpawningSettings {

    private boolean preventSpawner;
    private boolean preventSpawnEgg;
    private boolean preventCustom;
    private Map<EntityType, Set<Tier>> entityTierMap;
    private Map<EntityType, Double> entityChanceMap;
    private Map<String, Integer> preventSpawnAbove;
    private boolean tierDropsAreUnion;


    public MythicCreatureSpawningSettings() {
        entityTierMap = new HashMap<>();
        entityChanceMap = new HashMap<>();
        preventSpawnAbove = new HashMap<>();
    }

    @Override
    public boolean isPreventSpawner() {
        return preventSpawner;
    }

    public void setPreventSpawner(boolean preventSpawner) {
        this.preventSpawner = preventSpawner;
    }

    @Override
    public boolean isPreventSpawnEgg() {
        return preventSpawnEgg;
    }

    public void setPreventSpawnEgg(boolean preventSpawnEgg) {
        this.preventSpawnEgg = preventSpawnEgg;
    }

    @Override
    public boolean isPreventCustom() {
        return preventCustom;
    }

    public void setPreventCustom(boolean preventCustom) {
        this.preventCustom = preventCustom;
    }

    @Override
    public double getEntityTypeChanceToSpawn(EntityType entityType) {
        return entityChanceMap.containsKey(entityType) ? entityChanceMap.get(entityType) : 0D;
    }

    @Override
    public double getEntityTypeChanceToSpawn(EntityType entityType, String worldName) {
        return getEntityTypeChanceToSpawn(entityType);
    }

    @Override
    public Set<Tier> getEntityTypeTiers(EntityType entityType) {
        return entityTierMap.containsKey(entityType) ? entityTierMap.get(entityType)
                                                     : new HashSet<Tier>();
    }

    @Override
    public Set<Tier> getEntityTypeTiers(EntityType entityType, String worldName) {
        return getEntityTypeTiers(entityType);
    }

    @Override
    public int getSpawnHeightLimit(String worldName) {
        if (preventSpawnAbove.containsKey(worldName) && preventSpawnAbove.get(worldName) != null) {
            return preventSpawnAbove.get(worldName);
        }
        return 255;
    }

    @Override
    @Deprecated
    public boolean isEnabled() {
        return true;
    }

    @Deprecated
    public void setEnabled(boolean enabled) {
        // do nothing
    }

    public void setEntityTypeChance(EntityType entityType, double chance) {
        this.entityChanceMap.put(entityType, chance);
    }

    public void setEntityTypeTiers(EntityType entityType, Set<Tier> tiers) {
        this.entityTierMap.put(entityType, tiers);
    }

    public void setSpawnHeightLimit(String worldName, int height) {
        this.preventSpawnAbove.put(worldName, height);
    }

}
