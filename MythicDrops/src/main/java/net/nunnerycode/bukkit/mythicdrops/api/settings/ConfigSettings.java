package net.nunnerycode.bukkit.mythicdrops.api.settings;

import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ConfigSettings {

	// itemGroups.yml
	List<String> getArmorTypes();

	List<String> getToolTypes();

	List<String> getMaterialTypes();

	Map<String, List<String>> getItemTypesWithIds();

	Map<String, List<String>> getMaterialTypesWithIds();

	// config.yml
	boolean isAutoUpdate();

	boolean isDebugMode();

	String getItemDisplayNameFormat();

	boolean isRandomLoreEnabled();

	double getRandomLoreChance();

	List<String> getTooltipFormat();

	String getScriptsDirectory();

	// language.yml
	String getLanguageString(String key);

	String getLanguageString(String key, String[][] args);

	String getFormattedLanguageString(String key);

	String getFormattedLanguageString(String key, String[][] args);

	// creatureSpawning.yml
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

}
