package net.nunnerycode.bukkit.mythicdrops.api.module;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

public interface Module extends Listener {

    FileConfiguration getFileConfiguration();

}
