package net.nunnerycode.bukkit.mythicdrops.settings;

import net.nunnerycode.bukkit.mythicdrops.api.settings.ConfigSettings;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class MythicConfigSettings implements ConfigSettings {

	private final List<String> armorTypes;
	private final List<String> toolTypes;
	private final List<String> materialTypes;
	private final Map<String, List<String>> itemTypesWithIds;
	private final Map<String, List<String>> materialTypesWithIds;
	private final List<String> tooltipFormat;
	private final Map<String, String> language;
	private String scriptsDirectory;
	private boolean autoUpdate;
	private boolean debugMode;
	private String itemDisplayNameFormat;
	private boolean randomLoreEnabled;
	private double randomLoreChance;
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

	public MythicConfigSettings() {
		armorTypes = new ArrayList<>();
		toolTypes = new ArrayList<>();
		materialTypes = new ArrayList<>();
		itemTypesWithIds = new HashMap<>();
		materialTypesWithIds = new HashMap<>();
		tooltipFormat = new ArrayList<>();
		language = new HashMap<>();
		entityTierMap = new HashMap<>();
		entityChanceMap = new HashMap<>();
	}

	public Map<String, String> getLanguageMap() {
		return language;
	}

	@Override
	public List<String> getArmorTypes() {
		return armorTypes;
	}

	@Override
	public List<String> getToolTypes() {
		return toolTypes;
	}

	@Override
	public List<String> getMaterialTypes() {
		return materialTypes;
	}

	@Override
	public Map<String, List<String>> getItemTypesWithIds() {
		return itemTypesWithIds;
	}

	@Override
	public Map<String, List<String>> getMaterialTypesWithIds() {
		return materialTypesWithIds;
	}

	@Override
	public boolean isAutoUpdate() {
		return autoUpdate;
	}

	public void setAutoUpdate(boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
	}

	@Override
	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

	@Override
	public String getItemDisplayNameFormat() {
		return itemDisplayNameFormat;
	}

	public void setItemDisplayNameFormat(String itemDisplayNameFormat) {
		this.itemDisplayNameFormat = itemDisplayNameFormat;
	}

	@Override
	public boolean isRandomLoreEnabled() {
		return randomLoreEnabled;
	}

	public void setRandomLoreEnabled(boolean randomLoreEnabled) {
		this.randomLoreEnabled = randomLoreEnabled;
	}

	@Override
	public double getRandomLoreChance() {
		return randomLoreChance;
	}

	public void setRandomLoreChance(double randomLoreChance) {
		this.randomLoreChance = randomLoreChance;
	}

	@Override
	public List<String> getTooltipFormat() {
		return tooltipFormat;
	}

	@Override
	public String getScriptsDirectory() {
		return scriptsDirectory;
	}

	public void setScriptsDirectory(String scriptsDirectory) {
		this.scriptsDirectory = scriptsDirectory;
	}

	@Override
	public String getLanguageString(String key) {
		return language.containsKey(key) ? language.get(key) : key;
	}

	@Override
	public String getLanguageString(String key, String[][] args) {
		String s = getLanguageString(key);
		for (String[] arg : args) {
			s = s.replace(arg[0], arg[1]);
		}
		return s;
	}

	@Override
	public String getFormattedLanguageString(String key) {
		return getLanguageString(key).replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
	}

	@Override
	public String getFormattedLanguageString(String key, String[][] args) {
		String s = getFormattedLanguageString(key);
		for (String[] arg : args) {
			s = s.replace(arg[0], arg[1]);
		}
		return s;
	}

	@Override
	public boolean isCanMobsPickUpEquipment() {
		return canMobsPickUpEquipment;
	}

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

}
