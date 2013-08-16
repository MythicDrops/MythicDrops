package net.nunnerycode.bukkit.mythicdrops.savers;

import com.conventnunnery.libraries.config.ConventConfiguration;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.utils.MythicSaver;
import net.nunnerycode.bukkit.mythicdrops.managers.SettingsManager;
import org.bukkit.configuration.file.FileConfiguration;

public class MythicSettingsSaver implements MythicSaver {
	private final MythicDrops plugin;

	public MythicSettingsSaver(final MythicDrops plugin) {
		this.plugin = plugin;
	}

	@Override
	public void save() {
		SettingsManager settingsManager = getPlugin().getSettingsManager();
		ConventConfiguration c = plugin.getConfigYAML();
		FileConfiguration configuration;
		if (c != null) {
			configuration = c.getFileConfiguration();
			configuration.set("options.autoUpdate", settingsManager.isAutoUpdate());
			configuration.set("options.debug", settingsManager.isDebugMode());
			configuration.set("customItems.spawn", settingsManager.isCustomItemsSpawn());
			configuration.set("customItems.onlySpawn", settingsManager.isOnlyCustomItemsSpawn());
			configuration.set("customItems.chance", settingsManager.getCustomItemChanceToSpawn());
			configuration.set("spawnPrevention.spawnEgg", settingsManager.isPreventSpawningFromSpawnEgg());
			configuration.set("spawnPrevention.spawner", settingsManager.isPreventSpawningFromMonsterSpawner());
			configuration.set("spawnPrevention.custom", settingsManager.isPreventSpawningFromCustom());
			configuration.set("display.itemDisplayNameFormat", settingsManager.getItemDisplayNameFormat());
			configuration.set("display.tooltips.randomLoreEnabled", settingsManager.isRandomLoreEnabled());
			configuration.set("display.tooltips.randomLoreChance", settingsManager.getRandomLoreChance());
			configuration.set("display.tooltips.format", settingsManager.getLoreFormat());
			c.save();
		}
		c = getPlugin().getItemGroupsYAML();
		if (c != null) {
			configuration = c.getFileConfiguration();
			configuration.set("itemGroups.toolGroups", settingsManager.getToolIDTypes());
			configuration.set("itemGroups.armorGroups", settingsManager.getArmorIDTypes());
			configuration.set("itmeGroups.materialGroups", settingsManager.getMaterialIDTypes());
			c.save();
		}
	}

	public MythicDrops getPlugin() {
		return plugin;
	}
}
