/**
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2013 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.settings;

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.CreatureSpawningSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.entity.EntityType;

public final class MythicCreatureSpawningSettings implements CreatureSpawningSettings {

  private boolean preventSpawner;
  private boolean preventSpawnEgg;
  private boolean preventReinforcements;
  private Map<EntityType, Set<Tier>> entityTierMap;
  private Map<EntityType, Double> entityChanceMap;
  private Map<String, Integer> preventSpawnAbove;

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
  public double getEntityTypeChanceToSpawn(EntityType entityType) {
    return entityChanceMap.getOrDefault(entityType, 0D);
  }

  @Override
  public Set<Tier> getEntityTypeTiers(EntityType entityType) {
    return entityTierMap.containsKey(entityType) ? entityTierMap.get(entityType)
        : new HashSet<>();
  }

  @Override
  public int getSpawnHeightLimit(String worldName) {
    if (preventSpawnAbove.containsKey(worldName) && preventSpawnAbove.get(worldName) != null) {
      return preventSpawnAbove.get(worldName);
    }
    return 255;
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

  @Override
  public boolean isPreventReinforcements() {
    return preventReinforcements;
  }

  public void setPreventReinforcements(boolean preventReinforcements) {
    this.preventReinforcements = preventReinforcements;
  }

}
