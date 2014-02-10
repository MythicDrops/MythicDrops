package net.nunnerycode.bukkit.mythicdrops.api.settings;

import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;

import org.bukkit.entity.EntityType;

import java.util.Set;

public interface CreatureSpawningSettings {

  double getGlobalSpawnChance();

  boolean isPreventSpawner();

  boolean isPreventSpawnEgg();

  boolean isPreventCustom();

  double getEntityTypeChanceToSpawn(EntityType entityType);

  @Deprecated
  double getEntityTypeChanceToSpawn(EntityType entityType, String worldName);

  Set<Tier> getEntityTypeTiers(EntityType entityType);

  @Deprecated
  Set<Tier> getEntityTypeTiers(EntityType entityType, String worldName);

  boolean isCustomItemsSpawn();

  boolean isOnlyCustomItemsSpawn();

  int getSpawnHeightLimit(String worldName);

  @Deprecated
  boolean isEnabled();

}
