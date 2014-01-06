package net.nunnerycode.bukkit.mythicdrops.settings;

import net.nunnerycode.bukkit.mythicdrops.api.settings.CreatureSpawningSettings;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class MythicCreatureSpawningSettings implements CreatureSpawningSettings {

	private boolean canMobsPickUpEquipment;
	private boolean blankMobSpawnEnabled;
	private boolean blankMobSpawnSkeletonsSpawnWithBows;
	private double globalSpawnChance;
	private boolean preventSpawner;
	private boolean preventSpawnEgg;
	private boolean preventCustom;
	private Map<EntityType, Set<Tier>> entityTierMap;
	private Map<EntityType, Double> entityChanceMap;
	private boolean customItemsSpawn;
	private boolean onlyCustomItemsSpawn;
	private double customItemSpawnChance;
	private Map<String, Integer> preventSpawnAbove;

	public MythicCreatureSpawningSettings() {
		entityTierMap = new HashMap<>();
		entityChanceMap = new HashMap<>();
		preventSpawnAbove = new HashMap<>();
	}

	@Override
	public boolean isCanMobsPickUpEquipment() {
		return canMobsPickUpEquipment;
	}

	@Override
	public boolean isBlankMobSpawnEnabled() {
		return blankMobSpawnEnabled;
	}

	@Override
	public boolean isBlankMobSpawnSkeletonsSpawnWithBows() {
		return blankMobSpawnSkeletonsSpawnWithBows;
	}

	@Override
	public double getGlobalSpawnChance() {
		return globalSpawnChance;
	}

	@Override
	public boolean isPreventSpawner() {
		return preventSpawner;
	}

	@Override
	public boolean isPreventSpawnEgg() {
		return preventSpawnEgg;
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
		return entityTierMap.containsKey(entityType) ? entityTierMap.get(entityType) : new HashSet<Tier>();
	}

	@Override
	public Set<Tier> getEntityTypeTiers(EntityType entityType, String worldName) {
		return getEntityTypeTiers(entityType);
	}

	@Override
	public boolean isCustomItemsSpawn() {
		return customItemsSpawn;
	}

	@Override
	public boolean isOnlyCustomItemsSpawn() {
		return onlyCustomItemsSpawn;
	}

	@Override
	public double getCustomItemSpawnChance() {
		return customItemSpawnChance;
	}

	@Override
	public int getSpawnHeightLimit(String worldName) {
		if (preventSpawnAbove.containsKey(worldName) && preventSpawnAbove.get(worldName) != null) {
			return preventSpawnAbove.get(worldName);
		}
		return 255;
	}

	public void setCustomItemSpawnChance(double customItemSpawnChance) {
		this.customItemSpawnChance = customItemSpawnChance;
	}

	public void setOnlyCustomItemsSpawn(boolean onlyCustomItemsSpawn) {
		this.onlyCustomItemsSpawn = onlyCustomItemsSpawn;
	}

	public void setCustomItemsSpawn(boolean customItemsSpawn) {
		this.customItemsSpawn = customItemsSpawn;
	}

	public void setPreventSpawnEgg(boolean preventSpawnEgg) {
		this.preventSpawnEgg = preventSpawnEgg;
	}

	public void setPreventSpawner(boolean preventSpawner) {
		this.preventSpawner = preventSpawner;
	}

	public void setGlobalSpawnChance(double globalSpawnChance) {
		this.globalSpawnChance = globalSpawnChance;
	}

	public void setBlankMobSpawnSkeletonsSpawnWithBows(boolean blankMobSpawnSkeletonsSpawnWithBows) {
		this.blankMobSpawnSkeletonsSpawnWithBows = blankMobSpawnSkeletonsSpawnWithBows;
	}

	public void setBlankMobSpawnEnabled(boolean blankMobSpawnEnabled) {
		this.blankMobSpawnEnabled = blankMobSpawnEnabled;
	}

	public void setCanMobsPickUpEquipment(boolean canMobsPickUpEquipment) {
		this.canMobsPickUpEquipment = canMobsPickUpEquipment;
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
