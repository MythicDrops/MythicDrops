package net.nunnerycode.bukkit.mythicdrops.api;

import com.conventnunnery.libraries.config.ConventYamlConfiguration;
import net.nunnerycode.bukkit.mythicdrops.api.settings.ConfigSettings;
import net.nunnerycode.java.libraries.cannonball.DebugPrinter;
import se.ranzdo.bukkit.methodcommand.CommandHandler;

public interface MythicDrops {

	ConfigSettings getConfigSettings();

	DebugPrinter getDebugPrinter();

	ConventYamlConfiguration getConfigYAML();

	ConventYamlConfiguration getCustomItemYAML();

	ConventYamlConfiguration getItemGroupYAML();

	ConventYamlConfiguration getLanguageYAML();

	ConventYamlConfiguration getTierYAML();

	void reloadSettings();

	void reloadTiers();

	void reloadCustomItems();

	void reloadNames();

	CommandHandler getCommandHandler();

}
