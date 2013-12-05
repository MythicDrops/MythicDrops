package net.nunnerycode.bukkit.mythicdrops;

import net.nunnerycode.bukkit.libraries.config.NunneryConfiguration;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.settings.ConfigSettings;
import net.nunnerycode.bukkit.mythicdrops.settings.MythicConfigSettings;
import net.nunnerycode.java.libraries.cannonball.DebugPrinter;
import org.bukkit.plugin.java.JavaPlugin;

public final class MythicDropsPlugin extends JavaPlugin implements MythicDrops {

	private static MythicDrops _INSTANCE;
	private ConfigSettings configSettings;
	private DebugPrinter debugPrinter;
	private NunneryConfiguration configYAML;
	private NunneryConfiguration customItemYAML;
	private NunneryConfiguration itemGroupsYAML;
	private NunneryConfiguration languageYAML;
	private NunneryConfiguration tierYAML;

	public static MythicDrops getInstance() {
		return _INSTANCE;
	}

	@Override
	public void onEnable() {
		_INSTANCE = this;

		debugPrinter = new DebugPrinter(getDataFolder().getPath(), "debug.log");
		configSettings = new MythicConfigSettings();

		saveResource("config.yml", false);
		saveResource("customItems.yml", false);
		saveResource("itemGroups.yml", false);
		saveResource("language.yml", false);
		saveResource("tier.yml", false);
	}

	@Override
	public ConfigSettings getConfigSettings() {
		return configSettings;
	}

	@Override
	public DebugPrinter getDebugPrinter() {
		return debugPrinter;
	}

	@Override
	public NunneryConfiguration getConfigYAML() {
		return configYAML;
	}

	@Override
	public NunneryConfiguration getCustomItemYAML() {
		return customItemYAML;
	}

	@Override
	public NunneryConfiguration getItemGroupsYAML() {
		return itemGroupsYAML;
	}

	@Override
	public NunneryConfiguration getLanguageYAML() {
		return languageYAML;
	}

	@Override
	public NunneryConfiguration getTierYAML() {
		return tierYAML;
	}
}
