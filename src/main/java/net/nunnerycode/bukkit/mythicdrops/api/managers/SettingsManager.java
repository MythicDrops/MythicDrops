package net.nunnerycode.bukkit.mythicdrops.api.managers;

import java.util.List;
import java.util.Map;
import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;

public interface SettingsManager {

	List<String> getArmorIDTypes();

	void setArmorIDTypes(List<String> armorIDTypes);

	List<String> getToolIDTypes();

	void setToolIDTypes(List<String> toolIDTypes);

	List<String> getMaterialIDTypes();

	void setMaterialIDTypes(List<String> materialIDTypes);

	boolean isCustomItemsSpawn();

	void setCustomItemsSpawn(boolean customItemsSpawn);

	double getRandomLoreChance();

	void setRandomLoreChance(double randomLoreChance);

	boolean isAutoUpdate();

	void setAutoUpdate(boolean autoUpdate);

	boolean isDebugMode();

	void setDebugMode(boolean debugMode);

	boolean isOnlyCustomItemsSpawn();

	void setOnlyCustomItemsSpawn(boolean onlyCustomItemsSpawn);

	double getCustomItemChanceToSpawn();

	void setCustomItemChanceToSpawn(double customItemChanceToSpawn);

	boolean isPreventSpawningFromSpawnEgg();

	void setPreventSpawningFromSpawnEgg(boolean preventSpawningFromSpawnEgg);

	boolean isPreventSpawningFromMonsterSpawner();

	void setPreventSpawningFromMonsterSpawner(boolean preventSpawningFromMonsterSpawner);

	boolean isPreventSpawningFromCustom();

	void setPreventSpawningFromCustom(boolean preventSpawningFromCustom);

	String getItemDisplayNameFormat();

	void setItemDisplayNameFormat(String itemDisplayNameFormat);

	boolean isRandomLoreEnabled();

	void setRandomLoreEnabled(boolean randomLoreEnabled);

	List<String> getLoreFormat();

	void setLoreFormat(List<String> loreFormat);

	Map<String, List<String>> getIds();

	void setIds(final Map<String, List<String>> ids);

	MythicDropsPlugin getPlugin();

}
