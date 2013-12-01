package net.nunnerycode.bukkit.mythicdrops.api;

import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItemMap;
import net.nunnerycode.bukkit.mythicdrops.api.items.DropFactory;
import net.nunnerycode.bukkit.mythicdrops.api.settings.ConfigSettings;

public interface MythicDrops {

	ConfigSettings getConfigSettings();

	DropFactory getDropFactory();

	CustomItemMap getCustomItemMap();

}
