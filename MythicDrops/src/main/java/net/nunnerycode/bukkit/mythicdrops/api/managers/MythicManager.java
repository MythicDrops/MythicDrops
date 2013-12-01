package net.nunnerycode.bukkit.mythicdrops.api.managers;

import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.loaders.MythicLoader;
import net.nunnerycode.bukkit.mythicdrops.api.savers.MythicSaver;

public interface MythicManager extends MythicSaver, MythicLoader {

	MythicDrops getPlugin();

}
