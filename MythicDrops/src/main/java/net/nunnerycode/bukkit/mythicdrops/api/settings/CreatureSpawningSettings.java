package net.nunnerycode.bukkit.mythicdrops.api.settings;

import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.entity.EntityType;

import java.util.Set;

public interface CreatureSpawningSettings {

	boolean isCanMobsPickUpEquipment();

	boolean isBlankMobSpawnEnabled();

	boolean isBlankMobSpawnSkeletonsSpawnWithBows();

	double getGlobalSpawnChance();

	boolean isPreventSpawner();

	boolean isPreventSpawnEgg();

	boolean isPreventCustom();

	double getEntityTypeChanceToSpawn(EntityType entityType);

	double getEntityTypeChanceToSpawn(EntityType entityType, String worldName);

	Set<Tier> getEntityTypeTiers(EntityType entityType);

	Set<Tier> getEntityTypeTiers(EntityType entityType, String worldName);

	boolean isCustomItemsSpawn();

	boolean isOnlyCustomItemsSpawn();

	double getCustomItemSpawnChance();

	int getSpawnHeightLimit(String worldName);

	boolean isEnabled();

	boolean isGiveMobsEquipment();

	boolean isGiveMobsNames();

}
