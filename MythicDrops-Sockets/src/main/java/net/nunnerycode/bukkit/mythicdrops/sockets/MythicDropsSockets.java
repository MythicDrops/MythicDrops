package net.nunnerycode.bukkit.mythicdrops.sockets;

import net.nunnerycode.java.libraries.cannonball.DebugPrinter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class MythicDropsSockets extends JavaPlugin {

	private DebugPrinter debugPrinter;
	private static MythicDropsSockets _INSTANCE;

	public static MythicDropsSockets getInstance() {
		return _INSTANCE;
	}

	@Override
	public void onEnable() {
		debugPrinter = new DebugPrinter(getDataFolder().getPath(), "debug.log");

		debugPrinter.debug(Level.INFO, "v" + getDescription().getVersion() + " enabled");
	}

	@Override
	public void onDisable() {
		debugPrinter.debug(Level.INFO, "v" + getDescription().getVersion() + " disabled");
	}

}
