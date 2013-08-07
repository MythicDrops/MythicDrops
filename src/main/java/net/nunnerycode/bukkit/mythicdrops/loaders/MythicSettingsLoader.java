package net.nunnerycode.bukkit.mythicdrops.loaders;

import com.conventnunnery.libraries.config.ConventConfiguration;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.utils.MythicLoader;
import net.nunnerycode.bukkit.mythicdrops.managers.SettingsManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MythicSettingsLoader implements MythicLoader {
	private final MythicDrops plugin;

	public MythicSettingsLoader(final MythicDrops plugin) {
		this.plugin = plugin;
	}

	@Override
	public void load() {
		ConventConfiguration c = plugin.getConventConfigurationGroup().getConventConfiguration("config.yml");
		if (c == null) {
			return;
		}
		FileConfiguration configuration = plugin.getConventConfigurationGroup().getConventConfiguration("config" +
				".yml").getFileConfiguration();
		SettingsManager settingsManager = getPlugin().getSettingsManager();
		settingsManager.setAutoUpdate(configuration.getBoolean("options.autoUpdate", false));
		settingsManager.setDebugMode(configuration.getBoolean("options.debug", true));
		settingsManager.setCustomItemsSpawn(configuration.getBoolean("spawn", true));
		settingsManager.setOnlyCustomItemsSpawn(configuration.getBoolean("customItems.onlySpawn", false));
		settingsManager.setCustomItemChanceToSpawn(configuration.getDouble("customItems.chance", 0.05));
		settingsManager.setPreventSpawningFromSpawnEgg(configuration.getBoolean("spawnPrevention.spawnEgg", true));
		settingsManager.setPreventSpawningFromMonsterSpawner(configuration.getBoolean("spawnPrevention.spawner",
				true));
		settingsManager.setPreventSpawningFromCustom(configuration.getBoolean("spawnPrevention.custom", true));
		settingsManager.setItemDisplayNameFormat(configuration.getString("display.itemDisplayNameFormat",
				"%tiername% %itemtype%"));
		settingsManager.setPreventMultipleChangesFromSockets(configuration.getBoolean("display" +
				".preventMultipleChangesFromSockets"));
		settingsManager.setRandomLoreEnabled(configuration.getBoolean("display.tooltips.randomLoreEnabled"));
		settingsManager.setRandomLoreChance(configuration.getDouble("display.tooltips.randomLoreChance", 0.25));
		settingsManager.setLoreFormat(configuration.getStringList("display.tooltips.format"));
		settingsManager.setGlobalSpawnChance(configuration.getDouble("percentages.mobSpawnWithItemChance", 0.25));
		settingsManager.setSocketGemsEnabled(configuration.getBoolean("socketGems.socketGemsEnabled", true));
		settingsManager.setSocketGemsChanceToSpawn(configuration.getDouble("socketGems.socketGemsChance", 0.25));
		settingsManager.setAllowedSocketGemIds(configuration.getStringList("socketGems.socketGemMaterialIDs"));
		settingsManager.setItemsCanSpawnWithSockets(configuration.getBoolean("socketGems.sockettedItemsEnabled",
				true));
		settingsManager.setItemsSpawnWithSocketsChance(configuration.getDouble("socketGems.spawnWithSocketsChance",
				0.1));
		settingsManager.setUseAttackerItemInHandForEffects(configuration.getBoolean("effects.useAttackerItemInHand",
				true));
		settingsManager.setUseAttackerArmorForEffects(configuration.getBoolean("effects.useAttackerArmorEquipped",
				false));
		settingsManager.setUseDefenderItemInHandForEffects(configuration.getBoolean("effects.useDefenderItemInHand",
				false));
		settingsManager.setUseDefenderArmorForEffects(configuration.getBoolean("effects.useDefenderArmorEquipped",
				false));
		settingsManager.setUnidentifiedItemsEnabled(configuration.getBoolean("identification" +
				".unidentifiedItemsEnabled", true));
		settingsManager.setUnidentifiedItemsChanceToSpawn(configuration.getDouble
				("identification.unidentifiedItemsChance", 0.5));
		settingsManager.setIdentityTomesEnabled(configuration.getBoolean("identification" +
				".identityTomesEnabled", true));
		settingsManager.setIdentityTomesChanceToSpawn(configuration.getDouble
				("identification.identityTomesChance", 0.1));
		settingsManager.setRepairingEnabled(configuration.getBoolean("repairing.enabled", true));
		settingsManager.setRepairingPlaySound(configuration.getBoolean("repairing.playSound", true));
		settingsManager.setMultiworldSupport(configuration.getBoolean("worlds.enabled"));
		settingsManager.setGenerateWorlds(configuration.getStringList("worlds.generate"));
		settingsManager.setUseWorlds(configuration.getStringList("worlds.use"));

		FileConfiguration fc = getPlugin().getConventConfigurationGroup().getConventConfiguration("itemGroups.yml")
				.getFileConfiguration();
		if (!fc.isConfigurationSection("itemGroups")) {
			return;
		}
		ConfigurationSection idCS = fc.getConfigurationSection("itemGroups");

		if (idCS.isConfigurationSection("toolGroups")) {
			getPlugin().getSettingsManager().getToolIDTypes().clear();
			ConfigurationSection toolCS = idCS.getConfigurationSection("toolGroups");
			for (String toolKind : toolCS.getKeys(false)) {
				List<String> idList;
				idList = toolCS.getStringList(toolKind);
				if (idList == null) {
					idList = new ArrayList<String>();
				}
				getPlugin().getSettingsManager().getIds().put(toolKind.toLowerCase(), idList);
				getPlugin().getSettingsManager().getToolIDTypes().add(toolKind.toLowerCase());
			}
		}
		if (idCS.isConfigurationSection("armorGroups")) {
			getPlugin().getSettingsManager().getArmorIDTypes().clear();
			ConfigurationSection armorCS = idCS.getConfigurationSection("armorGroups");
			for (String armorKind : armorCS.getKeys(false)) {
				List<String> idList;
				idList = armorCS.getStringList(armorKind);
				if (idList == null) {
					idList = new ArrayList<String>();
				}
				getPlugin().getSettingsManager().getIds().put(armorKind.toLowerCase(), idList);
				getPlugin().getSettingsManager().getArmorIDTypes().add(armorKind.toLowerCase());
			}
		}
		if (idCS.isConfigurationSection("materialGroups")) {
			getPlugin().getSettingsManager().getMaterialIDTypes().clear();
			ConfigurationSection materialCS = idCS.getConfigurationSection("materialGroups");
			for (String materialKind : materialCS.getKeys(false)) {
				List<String> idList;
				idList = materialCS.getStringList(materialKind);
				if (idList == null) {
					idList = new ArrayList<String>();
				}
				getPlugin().getSettingsManager().getIds().put(materialKind.toLowerCase(), idList);
				getPlugin().getSettingsManager().getMaterialIDTypes().add(materialKind.toLowerCase());
			}
		}
	}

	public MythicDrops getPlugin() {
		return plugin;
	}
}