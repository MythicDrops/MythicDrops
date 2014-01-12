package net.nunnerycode.bukkit.mythicdrops.api;

import com.conventnunnery.libraries.config.ConventYamlConfiguration;
import net.nunnerycode.bukkit.mythicdrops.api.settings.ConfigSettings;
import net.nunnerycode.bukkit.mythicdrops.api.settings.CreatureSpawningSettings;
import net.nunnerycode.bukkit.mythicdrops.api.settings.IdentifyingSettings;
import net.nunnerycode.bukkit.mythicdrops.api.settings.RepairingSettings;
import net.nunnerycode.bukkit.mythicdrops.api.settings.SockettingSettings;
import net.nunnerycode.java.libraries.cannonball.DebugPrinter;
import se.ranzdo.bukkit.methodcommand.CommandHandler;

public interface MythicDrops {

	ConfigSettings getConfigSettings();

	CreatureSpawningSettings getCreatureSpawningSettings();

	RepairingSettings getRepairingSettings();

	SockettingSettings getSockettingSettings();

	IdentifyingSettings getIdentifyingSettings();

	DebugPrinter getDebugPrinter();

	ConventYamlConfiguration getConfigYAML();

	ConventYamlConfiguration getCustomItemYAML();

	ConventYamlConfiguration getItemGroupYAML();

	ConventYamlConfiguration getLanguageYAML();

	ConventYamlConfiguration getTierYAML();

	ConventYamlConfiguration getSocketGemsYAML();

	ConventYamlConfiguration getSockettingYAML();

	ConventYamlConfiguration getRepairingYAML();

	ConventYamlConfiguration getIdentifyingYAML();

	void reloadSettings();

	void reloadTiers();

	void reloadCustomItems();

	void reloadNames();

	CommandHandler getCommandHandler();

}
