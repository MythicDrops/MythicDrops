package net.nunnerycode.bukkit.mythicdrops;

import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.builders.DropBuilder;
import net.nunnerycode.bukkit.mythicdrops.api.names.factories.NameFactory;
import net.nunnerycode.bukkit.mythicdrops.api.settings.ConfigSettings;
import net.nunnerycode.bukkit.mythicdrops.items.builders.MythicDropBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public final class MythicDropsPlugin extends JavaPlugin implements MythicDrops {

	private static MythicDrops _INSTANCE;

	private ConfigSettings configSettings;

	@Override
	public void onEnable() {
		_INSTANCE = this;
	}

	public static MythicDrops getInstance() {
		return _INSTANCE;
	}

	@Override
	public ConfigSettings getConfigSettings() {
		return configSettings;
	}

	@Override
	public DropBuilder getDropFactory() {
		return new MythicDropBuilder();
	}

	@Override
	public NameFactory getNameFactory() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

}
