package net.nunnerycode.bukkit.mythicdrops;

import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItemMap;
import net.nunnerycode.bukkit.mythicdrops.api.items.factories.DropFactory;
import net.nunnerycode.bukkit.mythicdrops.api.settings.ConfigSettings;
import net.nunnerycode.bukkit.mythicdrops.items.factories.MythicDropFactory;
import org.bukkit.plugin.java.JavaPlugin;

public final class MythicDropsPlugin extends JavaPlugin implements MythicDrops {

	private static MythicDropsPlugin _INSTANCE;

	private ConfigSettings configSettings;
	private CustomItemMap customItemMap;

	@Override
	public void onEnable() {
		_INSTANCE = this;
	}

	public static MythicDropsPlugin getInstance() {
		return _INSTANCE;
	}

	@Override
	public ConfigSettings getConfigSettings() {
		return configSettings;
	}

	@Override
	public DropFactory getDropFactory() {
		return new MythicDropFactory();
	}

	@Override
	public CustomItemMap getCustomItemMap() {
		return customItemMap;
	}
}
