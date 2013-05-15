package com.conventnunnery.plugins.mythicdrops.configuration;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/*
 * Based on CommentedYamlConfiguration by dumptruckman
 * https://github.com/dumptruckman/PluginBase/blob/master/bukkit/src/main/java/com
 * /dumptruckman/minecraft/pluginbase/config/CommentedYamlConfiguration.java
 */

public class CommentedYamlConfiguration extends YamlConfiguration {

	private final File file;

	public CommentedYamlConfiguration(File file) {
		super();
		this.file = file;
	}

	public boolean load() {
		try {
			load(file);
			return true;
		} catch (Exception e) {
			Bukkit.getLogger().severe(e.getMessage());
			return false;
		}
	}

	public boolean save() {
		try {
			save(file);
			return true;
		} catch (IOException e) {
			Bukkit.getLogger().severe(e.getMessage());
			return false;
		}
	}

}
