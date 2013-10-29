package net.nunnerycode.bukkit.mythicdrops.loaders;

import com.conventnunnery.libraries.config.ConventConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.loaders.ConfigLoader;
import net.nunnerycode.bukkit.mythicdrops.managers.MythicSettingsManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class MythicSettingsLoader implements ConfigLoader {
	private final MythicDropsPlugin plugin;

	public MythicSettingsLoader(final MythicDropsPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void load() {
		ConventConfiguration c = plugin.getConfigYAML();
		if (c == null) {
			return;
		}
		FileConfiguration configuration = c.getFileConfiguration();
		MythicSettingsManager mythicSettingsManager = getPlugin().getMythicSettingsManager();
		mythicSettingsManager.setAutoUpdate(configuration.getBoolean("options.autoUpdate", false));
		mythicSettingsManager.setDebugMode(configuration.getBoolean("options.debug", true));
		mythicSettingsManager.setCustomItemsSpawn(configuration.getBoolean("customItems.spawn", true));
		mythicSettingsManager.setOnlyCustomItemsSpawn(configuration.getBoolean("customItems.onlySpawn", false));
		mythicSettingsManager.setCustomItemChanceToSpawn(configuration.getDouble("customItems.chance", 0.05));
		mythicSettingsManager.setPreventSpawningFromSpawnEgg(configuration.getBoolean("spawnPrevention.spawnEgg", true));
		mythicSettingsManager.setPreventSpawningFromMonsterSpawner(configuration.getBoolean("spawnPrevention.spawner",
				true));
		mythicSettingsManager.setPreventSpawningFromCustom(configuration.getBoolean("spawnPrevention.custom", true));
		mythicSettingsManager.setItemDisplayNameFormat(configuration.getString("display.itemDisplayNameFormat",
				"%tiername% %itemtype%"));
		mythicSettingsManager.setPreventMultipleChangesFromSockets(configuration.getBoolean("display" +
				".preventMultipleChangesFromSockets"));
		mythicSettingsManager.setRandomLoreEnabled(configuration.getBoolean("display.tooltips.randomLoreEnabled"));
		mythicSettingsManager.setRandomLoreChance(configuration.getDouble("display.tooltips.randomLoreChance", 0.25));
		mythicSettingsManager.setLoreFormat(configuration.getStringList("display.tooltips.format"));

		FileConfiguration fc = getPlugin().getItemGroupsYAML().getFileConfiguration();
		if (!fc.isConfigurationSection("itemGroups")) {
			return;
		}
		ConfigurationSection idCS = fc.getConfigurationSection("itemGroups");

		if (idCS.isConfigurationSection("toolGroups")) {
			List<String> toolGroupList = new ArrayList<String>();
			getPlugin().getMythicSettingsManager().getToolIDTypes().clear();
			ConfigurationSection toolCS = idCS.getConfigurationSection("toolGroups");
			for (String toolKind : toolCS.getKeys(false)) {
				List<String> idList;
				idList = toolCS.getStringList(toolKind);
				if (idList == null) {
					idList = new ArrayList<String>();
				}
				toolGroupList.add(toolKind + " (" + idList.size() + ")");
				getPlugin().getMythicSettingsManager().getIds().put(toolKind.toLowerCase(), idList);
				getPlugin().getMythicSettingsManager().getToolIDTypes().add(toolKind.toLowerCase());
			}
			plugin.debug(Level.INFO, "Loaded tool groups: " + toolGroupList.toString());
		}
		if (idCS.isConfigurationSection("armorGroups")) {
			List<String> armorGroupList = new ArrayList<String>();
			getPlugin().getMythicSettingsManager().getArmorIDTypes().clear();
			ConfigurationSection armorCS = idCS.getConfigurationSection("armorGroups");
			for (String armorKind : armorCS.getKeys(false)) {
				List<String> idList;
				idList = armorCS.getStringList(armorKind);
				if (idList == null) {
					idList = new ArrayList<String>();
				}
				armorGroupList.add(armorKind + " (" + idList.size() + ")");
				getPlugin().getMythicSettingsManager().getIds().put(armorKind.toLowerCase(), idList);
				getPlugin().getMythicSettingsManager().getArmorIDTypes().add(armorKind.toLowerCase());
			}
			plugin.debug(Level.INFO, "Loaded armor groups: " + armorGroupList.toString());
		}
		if (idCS.isConfigurationSection("materialGroups")) {
			List<String> materialGroupList = new ArrayList<String>();
			getPlugin().getMythicSettingsManager().getMaterialIDTypes().clear();
			ConfigurationSection materialCS = idCS.getConfigurationSection("materialGroups");
			for (String materialKind : materialCS.getKeys(false)) {
				List<String> idList;
				idList = materialCS.getStringList(materialKind);
				if (idList == null) {
					idList = new ArrayList<String>();
				}
				materialGroupList.add(materialKind + " (" + idList.size() + ")");
				getPlugin().getMythicSettingsManager().getIds().put(materialKind.toLowerCase(), idList);
				getPlugin().getMythicSettingsManager().getMaterialIDTypes().add(materialKind.toLowerCase());
			}
			plugin.debug(Level.INFO, "Loaded material groups: " + materialGroupList.toString());
		}
	}

	public MythicDropsPlugin getPlugin() {
		return plugin;
	}
}