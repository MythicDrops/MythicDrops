package net.nunnerycode.bukkit.mythicdrops.repair;

import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.java.libraries.cannonball.DebugPrinter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class MythicDropsRepair extends JavaPlugin {

	private DebugPrinter debugPrinter;
	private Map<String, RepairItem> repairItemMap;
	private Map<String, ItemStack> repairing;
	private Map<String, String> language;
	private MythicDrops mythicDrops;

	@Override
	public void onEnable() {
		debugPrinter = new DebugPrinter(getDataFolder().getPath(), "debug.log");

		mythicDrops = (MythicDrops) Bukkit.getPluginManager().getPlugin("MythicDrops");

		repairItemMap = new HashMap<>();
		repairing = new HashMap<>();

		debugPrinter.debug(Level.INFO, "v" + getDescription().getVersion() + " enabled");
	}

	@Override
	public void onDisable() {
		debugPrinter.debug(Level.INFO, "v" + getDescription().getVersion() + " disabled");
	}


}
