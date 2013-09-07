package net.nunnerycode.bukkit.mythicdrops.loaders;

import com.conventnunnery.libraries.config.ConventConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.utils.MythicLoader;
import net.nunnerycode.bukkit.mythicdrops.managers.SettingsManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class MythicSettingsLoader implements MythicLoader {
	private final MythicDrops plugin;

	public MythicSettingsLoader(final MythicDrops plugin) {
		this.plugin = plugin;
	}

	@Override
	public void load() {
		ConventConfiguration c = plugin.getConfigYAML();
		if (c == null) {
			return;
		}
		FileConfiguration configuration = c.getFileConfiguration();
		SettingsManager settingsManager = getPlugin().getSettingsManager();
		settingsManager.setAutoUpdate(configuration.getBoolean("options.autoUpdate", false));
		settingsManager.setDebugMode(configuration.getBoolean("options.debug", true));
		settingsManager.setCustomItemsSpawn(configuration.getBoolean("customItems.spawn", true));
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

		FileConfiguration fc = getPlugin().getItemGroupsYAML().getFileConfiguration();
		if (!fc.isConfigurationSection("itemGroups")) {
			return;
		}
		ConfigurationSection idCS = fc.getConfigurationSection("itemGroups");

		if (idCS.isConfigurationSection("toolGroups")) {
			List<String> toolGroupList = new ArrayList<String>();
			getPlugin().getSettingsManager().getToolIDTypes().clear();
			ConfigurationSection toolCS = idCS.getConfigurationSection("toolGroups");
			for (String toolKind : toolCS.getKeys(false)) {
				List<String> idList;
				idList = toolCS.getStringList(toolKind);
				if (idList == null) {
					idList = new ArrayList<String>();
				}
				toolGroupList.add(toolKind + " (" + idList.size() + ")");
				getPlugin().getSettingsManager().getIds().put(toolKind.toLowerCase(), idList);
				getPlugin().getSettingsManager().getToolIDTypes().add(toolKind.toLowerCase());
			}
			plugin.debug(Level.INFO, "Loaded tool groups: " + toolGroupList.toString());
		}
		if (idCS.isConfigurationSection("armorGroups")) {
			List<String> armorGroupList = new ArrayList<String>();
			getPlugin().getSettingsManager().getArmorIDTypes().clear();
			ConfigurationSection armorCS = idCS.getConfigurationSection("armorGroups");
			for (String armorKind : armorCS.getKeys(false)) {
				List<String> idList;
				idList = armorCS.getStringList(armorKind);
				if (idList == null) {
					idList = new ArrayList<String>();
				}
				armorGroupList.add(armorKind + " (" + idList.size() + ")");
				getPlugin().getSettingsManager().getIds().put(armorKind.toLowerCase(), idList);
				getPlugin().getSettingsManager().getArmorIDTypes().add(armorKind.toLowerCase());
			}
			plugin.debug(Level.INFO, "Loaded armor groups: " + armorGroupList.toString());
		}
		if (idCS.isConfigurationSection("materialGroups")) {
			List<String> materialGroupList = new ArrayList<String>();
			getPlugin().getSettingsManager().getMaterialIDTypes().clear();
			ConfigurationSection materialCS = idCS.getConfigurationSection("materialGroups");
			for (String materialKind : materialCS.getKeys(false)) {
				List<String> idList;
				idList = materialCS.getStringList(materialKind);
				if (idList == null) {
					idList = new ArrayList<String>();
				}
				materialGroupList.add(materialKind + " (" + idList.size() + ")");
				getPlugin().getSettingsManager().getIds().put(materialKind.toLowerCase(), idList);
				getPlugin().getSettingsManager().getMaterialIDTypes().add(materialKind.toLowerCase());
			}
			plugin.debug(Level.INFO, "Loaded material groups: " + materialGroupList.toString());
		}
	}

	public MythicDrops getPlugin() {
		return plugin;
	}
}