package net.nunnerycode.bukkit.mythicdrops.settings;

import net.nunnerycode.bukkit.mythicdrops.api.settings.CreatureSpawningSettings;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class MythicCreatureSpawningSettings implements CreatureSpawningSettings {

	private boolean enabled;
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
	private boolean giveMobsEquipment;
	private boolean giveMobsNames;

	public MythicCreatureSpawningSettings() {
		entityTierMap = new HashMap<>();
		entityChanceMap = new HashMap<>();
		preventSpawnAbove = new HashMap<>();
	}

	@Override
	public boolean isCanMobsPickUpEquipment() {
		return canMobsPickUpEquipment;
	}

	public void setCanMobsPickUpEquipment(boolean canMobsPickUpEquipment) {
		this.canMobsPickUpEquipment = canMobsPickUpEquipment;
	}

	@Override
	public boolean isBlankMobSpawnEnabled() {
		return blankMobSpawnEnabled;
	}

	public void setBlankMobSpawnEnabled(boolean blankMobSpawnEnabled) {
		this.blankMobSpawnEnabled = blankMobSpawnEnabled;
	}

	@Override
	public boolean isBlankMobSpawnSkeletonsSpawnWithBows() {
		return blankMobSpawnSkeletonsSpawnWithBows;
	}

	public void setBlankMobSpawnSkeletonsSpawnWithBows(boolean blankMobSpawnSkeletonsSpawnWithBows) {
		this.blankMobSpawnSkeletonsSpawnWithBows = blankMobSpawnSkeletonsSpawnWithBows;
	}

	@Override
	public double getGlobalSpawnChance() {
		return globalSpawnChance;
	}

	public void setGlobalSpawnChance(double globalSpawnChance) {
		this.globalSpawnChance = globalSpawnChance;
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

	public void setCustomItemsSpawn(boolean customItemsSpawn) {
		this.customItemsSpawn = customItemsSpawn;
	}

	@Override
	public boolean isOnlyCustomItemsSpawn() {
		return onlyCustomItemsSpawn;
	}

	public void setOnlyCustomItemsSpawn(boolean onlyCustomItemsSpawn) {
		this.onlyCustomItemsSpawn = onlyCustomItemsSpawn;
	}

	@Override
	public double getCustomItemSpawnChance() {
		return customItemSpawnChance;
	}

	public void setCustomItemSpawnChance(double customItemSpawnChance) {
		this.customItemSpawnChance = customItemSpawnChance;
	}

	@Override
	public int getSpawnHeightLimit(String worldName) {
		if (preventSpawnAbove.containsKey(worldName) && preventSpawnAbove.get(worldName) != null) {
			return preventSpawnAbove.get(worldName);
		}
		return 255;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isGiveMobsEquipment() {
		return giveMobsEquipment;
	}

	public void setGiveMobsEquipment(boolean giveMobsEquipment) {
		this.giveMobsEquipment = giveMobsEquipment;
	}

	@Override
	public boolean isGiveMobsNames() {
		return giveMobsNames;
	}

	public void setGiveMobsNames(boolean giveMobsNames) {
		this.giveMobsNames = giveMobsNames;
	}

	public void setPreventCustom(boolean preventCustom) {
		this.preventCustom = preventCustom;
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
