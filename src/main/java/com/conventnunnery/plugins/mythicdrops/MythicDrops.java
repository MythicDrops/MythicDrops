package com.conventnunnery.plugins.mythicdrops;

import com.conventnunnery.libraries.debug.Debugger;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class MythicDrops extends JavaPlugin {

    // Instantiate a new instance of the Debugger class
    private final Debugger debugger = new Debugger(this);

    @Override
    public void onEnable() {
        // Prints a debug message that the plugin is enabled
        debugger.debug(Level.INFO, "Plugin enabled");
    }

    @Override
    public void onDisable() {
        // Prints a debug message that the plugin is disabled
        debugger.debug(Level.INFO, "Plugin disabled");
    }
}
