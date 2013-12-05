package net.nunnerycode.bukkit.mythicdrops.api;

import net.nunnerycode.bukkit.libraries.config.NunneryConfiguration;
import net.nunnerycode.bukkit.mythicdrops.api.settings.ConfigSettings;
import net.nunnerycode.java.libraries.cannonball.DebugPrinter;

public interface MythicDrops {

	ConfigSettings getConfigSettings();

	DebugPrinter getDebugPrinter();

	NunneryConfiguration getConfigYAML();

	NunneryConfiguration getCustomItemYAML();

	NunneryConfiguration getItemGroupsYAML();

	NunneryConfiguration getLanguageYAML();

	NunneryConfiguration getTierYAML();

}
