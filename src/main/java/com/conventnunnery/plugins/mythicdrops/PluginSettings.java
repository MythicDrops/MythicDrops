/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.mythicdrops;


import com.conventnunnery.plugins.mythicdrops.managers.ConfigurationManager;
import com.conventnunnery.plugins.mythicdrops.utils.NumberUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginSettings {

	private final com.conventnunnery.plugins.mythicdrops.MythicDrops plugin;
	private String displayItemNameFormat;
	private double percentageMobSpawnWithItemChance;
	private double percentageCustomDrop;
	private boolean worldsEnabled;
	private boolean automaticUpdate;
	private boolean safeEnchantsOnly;
	private boolean preventSpawnEgg;
	private boolean preventSpawner;
	private boolean preventCustom;
	private boolean preventMultipleChangesFromSockets;
	private boolean allowCustomToSpawn;
	private boolean onlyCustomItems;
	private boolean allowEnchantsPastNormalLevel;
	private boolean debugOnStartup;
	private boolean socketGemsEnabled;
	private boolean socketItemsEnabled;
	private double socketGemsChance;
	private double spawnWithSocketChance;
	private List<String> worldsGenerate = new ArrayList<String>();
	private List<String> worldsUse = new ArrayList<String>();
	private Map<String, List<String>> ids = new HashMap<String, List<String>>();
	private List<MaterialData> socketGemMaterials = new ArrayList<MaterialData>();
	private Map<String, Double> advancedMobSpawnWithItemChance = new HashMap<String, Double>();
	private Map<String, List<String>> tiersPerMob = new HashMap<String, List<String>>();
	private List<String> advancedToolTipFormat = new ArrayList<String>();
	private List<String> armorIDTypes = new ArrayList<String>();
	private List<String> toolIDTypes = new ArrayList<String>();
	private List<String> materialIDTypes = new ArrayList<String>();
	private boolean randomLoreEnabled;
	private double randomLoreChance;

	public PluginSettings(MythicDrops plugin) {
		this.plugin = plugin;
	}

	public Map<String, List<String>> getTiersPerMob() {
		return tiersPerMob;
	}

	public List<String> getMaterialIDTypes() {
		return materialIDTypes;
	}

	public void setMaterialIDTypes(List<String> materialIDTypes) {
		this.materialIDTypes = materialIDTypes;
	}

	public boolean isPreventMultipleChangesFromSockets() {
		return preventMultipleChangesFromSockets;
	}

	public void setPreventMultipleChangesFromSockets(boolean preventMultipleChangesFromSockets) {
		this.preventMultipleChangesFromSockets = preventMultipleChangesFromSockets;
	}

	public boolean isPreventCustom() {
		return preventCustom;
	}

	public void setPreventCustom(boolean preventCustom) {
		this.preventCustom = preventCustom;
	}

	public List<String> getArmorIDTypes() {
		return armorIDTypes;
	}

	@SuppressWarnings("unused")
	public void setArmorIDTypes(List<String> armorIDTypes) {
		this.armorIDTypes = armorIDTypes;
	}

	public List<String> getToolIDTypes() {
		return toolIDTypes;
	}

	@SuppressWarnings("unused")
	public void setToolIDTypes(List<String> toolIDTypes) {
		this.toolIDTypes = toolIDTypes;
	}

	public double getSpawnWithSocketChance() {
		return spawnWithSocketChance;
	}

	public void setSpawnWithSocketChance(double spawnWithSocketChance) {
		this.spawnWithSocketChance = spawnWithSocketChance;
	}

	public List<MaterialData> getSocketGemMaterials() {
		return socketGemMaterials;
	}

	public void setSocketGemMaterials(List<MaterialData> socketGemMaterials) {
		this.socketGemMaterials = socketGemMaterials;
	}

	public boolean isSocketGemsEnabled() {
		return socketGemsEnabled;
	}

	public void setSocketGemsEnabled(boolean socketGemsEnabled) {
		this.socketGemsEnabled = socketGemsEnabled;
	}

	public boolean isSockettedItemsEnabled() {
		return socketItemsEnabled;
	}

	public void setSockettedItemsEnabled(boolean socketItemsEnabled) {
		this.socketItemsEnabled = socketItemsEnabled;
	}

	public double getSocketGemsChance() {
		return socketGemsChance;
	}

	public void setSocketGemsChance(double socketGemsChance) {
		this.socketGemsChance = socketGemsChance;
	}

	public boolean isRandomLoreEnabled() {
		return randomLoreEnabled;
	}

	public void setRandomLoreEnabled(boolean randomLoreEnabled) {
		this.randomLoreEnabled = randomLoreEnabled;
	}

	public double getRandomLoreChance() {
		return randomLoreChance;
	}

	public void setRandomLoreChance(double randomLoreChance) {
		this.randomLoreChance = randomLoreChance;
	}

	public boolean isDebugOnStartup() {
		return debugOnStartup;
	}

	public void setDebugOnStartup(boolean debugOnStartup) {
		this.debugOnStartup = debugOnStartup;
	}

	public boolean isAllowEnchantsPastNormalLevel() {
		return allowEnchantsPastNormalLevel;
	}

	public void setAllowEnchantsPastNormalLevel(boolean allowEnchantsPastNormalLevel) {
		this.allowEnchantsPastNormalLevel = allowEnchantsPastNormalLevel;
	}

	public void debugSettings() {
		getPlugin().getDebug().debug("Auto Update: " + isAutomaticUpdate(),
				"Safe Enchants Only: " + isSafeEnchantsOnly(),
				"Multiworld Support Enabled: " + isWorldsEnabled(), "Item Name Format: " + getDisplayItemNameFormat());
		if (isWorldsEnabled()) {
			getPlugin().getDebug().debug(
					"Generate Worlds: " + getWorldsGenerate(),
					"Use Worlds: " + getWorldsUse());
		}
		getPlugin().getDebug().debug(
				"Global Spawn Rate: " + getPercentageMobSpawnWithItemChance());
		List<String> strings = new ArrayList<String>();
		for (MaterialData materialData : getSocketGemMaterials()) {
			strings.add(materialData.getItemTypeId() + ";" + materialData.getData());
		}
		getPlugin().getDebug().debug("Socket Gem materials: " + strings.toString());
	}

	public Map<String, Double> getAdvancedMobSpawnWithItemChanceMap() {
		return advancedMobSpawnWithItemChance;
	}

	public void setAdvancedMobSpawnWithItemChanceMap(
			Map<String, Double> advanced_mobSpawnWithItemChance) {
		this.advancedMobSpawnWithItemChance = advanced_mobSpawnWithItemChance;
	}

	public List<String> getAdvancedToolTipFormat() {
		return advancedToolTipFormat;
	}

	public void setAdvancedToolTipFormat(List<String> advanced_toolTipFormat) {
		this.advancedToolTipFormat = advanced_toolTipFormat;
	}

	public String getDisplayItemNameFormat() {
		return displayItemNameFormat;
	}

	public void setDisplayItemNameFormat(String displayItemNameFormat) {
		this.displayItemNameFormat = displayItemNameFormat;
	}

	public Map<String, List<String>> getIDs() {
		return ids;
	}

	/**
	 * @return the percentageCustomDrop
	 */
	public double getPercentageCustomDrop() {
		return percentageCustomDrop;
	}

	/**
	 * @param percentageCustomDrop the percentageCustomDrop to set
	 */
	public void setPercentageCustomDrop(double percentageCustomDrop) {
		this.percentageCustomDrop = percentageCustomDrop;
	}

	public double getPercentageMobSpawnWithItemChance() {
		return percentageMobSpawnWithItemChance;
	}

	public void setPercentageMobSpawnWithItemChance(
			double percentageMobSpawnWithItemChance) {
		this.percentageMobSpawnWithItemChance = percentageMobSpawnWithItemChance;
	}

	public MythicDrops getPlugin() {
		return plugin;
	}

	public List<String> getWorldsGenerate() {
		return worldsGenerate;
	}

	public void setWorldsGenerate(List<String> worldsGenerate) {
		this.worldsGenerate = worldsGenerate;
	}

	public List<String> getWorldsUse() {
		return worldsUse;
	}

	public void setWorldsUse(List<String> worldsUse) {
		this.worldsUse = worldsUse;
	}

	/**
	 * @return the allowCustomToSpawn
	 */
	public boolean isAllowCustomToSpawn() {
		return allowCustomToSpawn;
	}

	/**
	 * @param allowCustomToSpawn the allowCustomToSpawn to set
	 */
	public void setAllowCustomToSpawn(boolean allowCustomToSpawn) {
		this.allowCustomToSpawn = allowCustomToSpawn;
	}

	/**
	 * @return the automaticUpdate
	 */
	public boolean isAutomaticUpdate() {
		return automaticUpdate;
	}

	/**
	 * @param automaticUpdate the automaticUpdate to set
	 */
	public void setAutomaticUpdate(boolean automaticUpdate) {
		this.automaticUpdate = automaticUpdate;
	}

	/**
	 * @return the onlyCustomItems
	 */
	public boolean isOnlyCustomItems() {
		return onlyCustomItems;
	}

	/**
	 * @param onlyCustomItems the onlyCustomItems to set
	 */
	public void setOnlyCustomItems(boolean onlyCustomItems) {
		this.onlyCustomItems = onlyCustomItems;
	}

	/**
	 * @return the preventSpawnEgg
	 */
	public boolean isPreventSpawnEgg() {
		return preventSpawnEgg;
	}

	/**
	 * @param preventSpawnEgg the preventSpawnEgg to set
	 */
	public void setPreventSpawnEgg(boolean preventSpawnEgg) {
		this.preventSpawnEgg = preventSpawnEgg;
	}

	/**
	 * @return the preventSpawner
	 */
	public boolean isPreventSpawner() {
		return preventSpawner;
	}

	/**
	 * @param preventSpawner the preventSpawner to set
	 */
	public void setPreventSpawner(boolean preventSpawner) {
		this.preventSpawner = preventSpawner;
	}

	public boolean isSafeEnchantsOnly() {
		return safeEnchantsOnly;
	}

	public void setSafeEnchantsOnly(boolean safeEnchantsOnly) {
		this.safeEnchantsOnly = safeEnchantsOnly;
	}

	public boolean isWorldsEnabled() {
		return worldsEnabled;
	}

	public void setWorldsEnabled(boolean worldsEnabled) {
		this.worldsEnabled = worldsEnabled;
	}

	private void loadIDs() {
		FileConfiguration fc = getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.ITEMGROUPS);
		if (!fc.isConfigurationSection("itemGroups")) {
            return;
        }
		ConfigurationSection idCS = fc.getConfigurationSection("itemGroups");
		if (idCS.isConfigurationSection("toolGroups")) {
			toolIDTypes.clear();
			ConfigurationSection toolCS = idCS.getConfigurationSection("toolGroups");
			for (String toolKind : toolCS.getKeys(false)) {
				List<String> idList;
				idList = toolCS.getStringList(toolKind);
				if (idList == null) {
                    idList = new ArrayList<String>();
                }
				ids.put(toolKind.toLowerCase(), idList);
				toolIDTypes.add(toolKind.toLowerCase());
			}
		}
		if (idCS.isConfigurationSection("armorGroups")) {
			armorIDTypes.clear();
			ConfigurationSection armorCS = idCS.getConfigurationSection("armorGroups");
			for (String armorKind : armorCS.getKeys(false)) {
				List<String> idList;
				idList = armorCS.getStringList(armorKind);
				if (idList == null) {
                    idList = new ArrayList<String>();
                }
				ids.put(armorKind.toLowerCase(), idList);
				armorIDTypes.add(armorKind.toLowerCase());
			}
		}
		if (idCS.isConfigurationSection("materialGroups")) {
			materialIDTypes.clear();
			ConfigurationSection materialCS = idCS.getConfigurationSection("materialGroups");
			for (String materialKind : materialCS.getKeys(false)) {
				List<String> idList;
				idList = materialCS.getStringList(materialKind);
				if (idList == null) {
                    idList = new ArrayList<String>();
                }
				ids.put(materialKind.toLowerCase(), idList);
				materialIDTypes.add(materialKind.toLowerCase());
			}
		}
	}

	public void loadPluginSettings() {
		setAutomaticUpdate(getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
				.getBoolean("options.autoUpdate"));
		setPercentageCustomDrop(getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
				.getDouble("options.customDropChance"));
		setSafeEnchantsOnly(getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
				.getBoolean("options.safeEnchantsOnly"));
		setAllowEnchantsPastNormalLevel(getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
				.getBoolean("options.allowEnchantsPastNormalLevel"));
		setOnlyCustomItems(getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
				.getBoolean("options.customItemsOnly"));
		setAllowCustomToSpawn(getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
				.getBoolean("options.customItemsSpawn"));
		setDebugOnStartup(getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
				.getBoolean("options.debugOnStartup"));
		setDisplayItemNameFormat(getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
				.getString("display.itemNameFormat"));
		setPreventMultipleChangesFromSockets(getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
				.getBoolean("display.preventMultipleChangesFromSockets"));
		setPreventSpawnEgg(getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
				.getBoolean("spawnPrevention.spawnEgg"));
		setPreventSpawner(getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
				.getBoolean("spawnPrevention.spawner"));
		setPreventCustom(getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
				.getBoolean("spawnPrevention.custom"));
		setPercentageMobSpawnWithItemChance(getPlugin()
				.getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
				.getDouble("percentages.mobSpawnWithItemChance"));
		setWorldsEnabled(getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
				.getBoolean("worlds.enabled"));
		setWorldsGenerate(getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
				.getStringList("worlds.generate"));
		if (getWorldsGenerate() == null) {
            setWorldsGenerate(new ArrayList<String>());
        }
		setWorldsUse(getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
				.getStringList("worlds.use"));
		if (getWorldsUse() == null) {
            setWorldsUse(new ArrayList<String>());
        }
		loadIDs();
		Map<String, Double> map = new HashMap<String, Double>();
		if (getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.DROPRATES)
				.isConfigurationSection("spawnWithItemChance")) {
			for (String creature : getPlugin().getConfigurationManager()
					.getConfiguration(ConfigurationManager.ConfigurationFile.DROPRATES)
					.getConfigurationSection("spawnWithItemChance")
					.getKeys(false)) {
				map.put(creature.toUpperCase(),
						getPlugin()
								.getConfigurationManager()
								.getConfiguration(ConfigurationManager.ConfigurationFile.DROPRATES)
								.getConfigurationSection("spawnWithItemChance")
								.getDouble(creature));

			}
		}
		setAdvancedMobSpawnWithItemChanceMap(map);
		List<String> toolTipFormat = getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
				.getStringList("display.tooltips.format");
		if (toolTipFormat == null) {
			toolTipFormat = new ArrayList<String>();
			getPlugin().getConfigurationManager()
					.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
					.set("display.tooltips.format", toolTipFormat);
		}
		setAdvancedToolTipFormat(toolTipFormat);
		setRandomLoreEnabled(getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
				.getBoolean("display.tooltips.randomLoreEnabled"));
		setRandomLoreChance(getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
				.getDouble("display.tooltips.randomLoreChance"));
		setSocketGemsEnabled(getPlugin().getConfigurationManager().getConfiguration(
				ConfigurationManager.ConfigurationFile.CONFIG)
				.getBoolean("sockets.socketGemsEnabled"));
		setSocketGemsChance(getPlugin().getConfigurationManager().getConfiguration(
				ConfigurationManager.ConfigurationFile.CONFIG)
				.getDouble("sockets.socketGemsChance"));
		setSockettedItemsEnabled(getPlugin().getConfigurationManager().getConfiguration(
				ConfigurationManager.ConfigurationFile.CONFIG)
				.getBoolean("sockets.sockettedItemsEnabled"));
		List<MaterialData> materialDatas = new ArrayList<MaterialData>();
		for (String s : getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CONFIG)
				.getStringList("sockets.socketGemMaterialIDs")) {
			int id;
			byte data;
			if (s.contains(";")) {
				String[] split = s.split(";");
				id = NumberUtils.getInt(split[0], 0);
				data = (byte) NumberUtils.getInt(split[1], 0);
			} else {
				id = NumberUtils.getInt(s, 0);
				data = 0;
			}
			if (id == 0) {
                continue;
            }

			materialDatas.add(new MaterialData(id, data));
		}
		setSocketGemMaterials(materialDatas);
		setSpawnWithSocketChance(getPlugin().getConfigurationManager().getConfiguration(
				ConfigurationManager.ConfigurationFile.CONFIG)
				.getDouble("sockets.spawnWithSocketChance"));
		getPlugin().getConfigurationManager().saveConfig();
	}
}
