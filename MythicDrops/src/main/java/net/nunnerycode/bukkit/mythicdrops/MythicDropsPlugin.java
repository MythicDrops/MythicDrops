package net.nunnerycode.bukkit.mythicdrops;

import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.settings.ConfigSettings;
import net.nunnerycode.bukkit.mythicdrops.settings.MythicConfigSettings;
import net.nunnerycode.java.libraries.cannonball.DebugPrinter;
import org.bukkit.plugin.java.JavaPlugin;

public final class MythicDropsPlugin extends JavaPlugin implements MythicDrops {

	private static MythicDrops _INSTANCE;

	private ConfigSettings configSettings;
	private DebugPrinter debugPrinter;

	@Override
	public void onEnable() {
		_INSTANCE = this;

		debugPrinter = new DebugPrinter(getDataFolder().getPath(), "debug.log");
		configSettings = new MythicConfigSettings();
	}

	public static MythicDrops getInstance() {
		return _INSTANCE;
	}

	@Override
	public ConfigSettings getConfigSettings() {
		return configSettings;
	}

	@Override
	public DebugPrinter getDebugPrinter() {
		return debugPrinter;
	}

}
