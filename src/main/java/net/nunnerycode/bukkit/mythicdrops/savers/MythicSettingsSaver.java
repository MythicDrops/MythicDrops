package net.nunnerycode.bukkit.mythicdrops.savers;

import com.conventnunnery.libraries.config.ConventConfiguration;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.managers.SettingsManager;
import net.nunnerycode.bukkit.mythicdrops.api.savers.ConfigSaver;
import org.bukkit.configuration.file.FileConfiguration;

public class MythicSettingsSaver implements ConfigSaver {
	private final MythicDrops plugin;

	public MythicSettingsSaver(final MythicDrops plugin) {
		this.plugin = plugin;
	}

	@Override
	public synchronized void save() {
		SettingsManager mythicSettingsManager = getPlugin().getSettingsManager();
		ConventConfiguration c = plugin.getConfigYAML();
		FileConfiguration configuration;
		if (c != null) {
			configuration = c.getFileConfiguration();
			configuration.set("options.autoUpdate", mythicSettingsManager.isAutoUpdate());
			configuration.set("options.debug", mythicSettingsManager.isDebugMode());
			configuration.set("customItems.spawn", mythicSettingsManager.isCustomItemsSpawn());
			configuration.set("customItems.onlySpawn", mythicSettingsManager.isOnlyCustomItemsSpawn());
			configuration.set("customItems.chance", mythicSettingsManager.getCustomItemChanceToSpawn());
			configuration.set("spawnPrevention.spawnEgg", mythicSettingsManager.isPreventSpawningFromSpawnEgg());
			configuration.set("spawnPrevention.spawner", mythicSettingsManager.isPreventSpawningFromMonsterSpawner());
			configuration.set("spawnPrevention.custom", mythicSettingsManager.isPreventSpawningFromCustom());
			configuration.set("display.itemDisplayNameFormat", mythicSettingsManager.getItemDisplayNameFormat());
			configuration.set("display.tooltips.randomLoreEnabled", mythicSettingsManager.isRandomLoreEnabled());
			configuration.set("display.tooltips.randomLoreChance", mythicSettingsManager.getRandomLoreChance());
			configuration.set("display.tooltips.format", mythicSettingsManager.getLoreFormat());
			c.save();
		}
		c = getPlugin().getItemGroupsYAML();
		if (c != null) {
			configuration = c.getFileConfiguration();
			for (String toolGroup : mythicSettingsManager.getToolIDTypes()) {
				configuration.set("itemGroups.toolGroups." + toolGroup, mythicSettingsManager.getIds().get(toolGroup));
			}
			for (String armorGroup : mythicSettingsManager.getToolIDTypes()) {
				configuration.set("itemGroups.armorGroups." + armorGroup, mythicSettingsManager.getIds().get(armorGroup));
			}
			for (String materialGroup : mythicSettingsManager.getMaterialIDTypes()) {
				configuration.set("itemGroups.materialGroups." + materialGroup, mythicSettingsManager.getIds().get
						(materialGroup));
			}
			c.save();
		}
	}

	public MythicDrops getPlugin() {
		return plugin;
	}
}
