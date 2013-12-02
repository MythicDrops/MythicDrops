package net.nunnerycode.bukkit.mythicdrops.api;

import net.nunnerycode.bukkit.mythicdrops.api.items.builders.DropBuilder;
import net.nunnerycode.bukkit.mythicdrops.api.names.factories.NameFactory;
import net.nunnerycode.bukkit.mythicdrops.api.settings.ConfigSettings;

public interface MythicDrops {

	ConfigSettings getConfigSettings();

	DropBuilder getDropFactory();

	NameFactory getNameFactory();

}
