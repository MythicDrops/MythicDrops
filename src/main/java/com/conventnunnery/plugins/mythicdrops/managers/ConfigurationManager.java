/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.mythicdrops.managers;

import com.conventnunnery.plugins.mythicdrops.configuration.CommentedYamlConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

public class ConfigurationManager {

	private final HashMap<ConfigurationFile, CommentedYamlConfiguration> configurations;
	private final Plugin plugin;

	public ConfigurationManager(Plugin plugin) {
		this.plugin = plugin;

		configurations = new HashMap<ConfigurationFile, CommentedYamlConfiguration>();

		loadConfig();
	}

	private void createConfig(ConfigurationFile config) {
		CommentedYamlConfiguration file = new CommentedYamlConfiguration(
				new File(plugin.getDataFolder(), config.filename));
		saveDefaults(file, config);
		configurations.put(config, file);
	}

	public CommentedYamlConfiguration getConfiguration(ConfigurationFile file) {
		return configurations.get(file);
	}

	/**
	 * Loads the plugin's configuration files
	 */
	public void loadConfig() {
		for (ConfigurationFile file : ConfigurationFile.values()) {
			File confFile = new File(plugin.getDataFolder(), file.filename);
			if (confFile.exists()) {
				CommentedYamlConfiguration config = new CommentedYamlConfiguration(
						confFile);
				config.load();
				if (needToUpdate(config, file)) {
					plugin.getLogger().info("Backing up " + file.filename);
					backup(file);
					plugin.getLogger().info("Updating " + file.filename);
					saveDefaults(config, file);
				}
				configurations.put(file, config);
			} else {
				File parentFile = confFile.getParentFile();
				if (!parentFile.exists()) {
					boolean mkdirs = parentFile.mkdirs();
					if (!mkdirs) {
						continue;
					}
				}
				createConfig(file);
			}
		}
	}

	/**
	 * Saves the plugin's configs
	 */
	public void saveConfig() {
		for (ConfigurationFile file : ConfigurationFile.values()) {
			if (configurations.containsKey(file)) {
				try {
					configurations.get(file).save(
							new File(plugin.getDataFolder(), file.filename));
				} catch (IOException e) {
					plugin.getLogger().log(Level.WARNING,
							"Could not save " + file.filename, e);
				}
			}
		}
	}

	private boolean needToUpdate(CommentedYamlConfiguration config, ConfigurationFile file) {
		YamlConfiguration inPlugin = YamlConfiguration.loadConfiguration(plugin
				.getResource(file.filename));
		if (inPlugin == null) {
			return false;
		}
		String configVersion = config.getString("version");
		String currentVersion = inPlugin.getString("version");
		return configVersion == null || currentVersion != null && !(configVersion.equalsIgnoreCase(currentVersion));
	}

	private boolean backup(ConfigurationFile file) {
		File actualFile = new File(plugin.getDataFolder(), file.filename);
		if (!actualFile.exists())
			return false;
		File newFile = new File(plugin.getDataFolder(), file.filename.replace(".yml", "_old.yml"));
		return actualFile.renameTo(newFile);
	}

	private void saveDefaults(CommentedYamlConfiguration config,
	                          ConfigurationFile file) {
		YamlConfiguration yc = CommentedYamlConfiguration.loadConfiguration(plugin
				.getResource(file.filename));
		for (String key : config.getKeys(true)) {
			config.set(key, null);
		}
		config.setDefaults(yc);
		config.options().copyDefaults(true);
		config.save();
	}

	public enum ConfigurationFile {

		CONFIG("config.yml"), LANGUAGE("language.yml"), TIER("tier.yml"), CUSTOMITEM("customItems.yml"),
		SOCKETGEM("socketgem.yml"), DROPRATES("dropRates.yml"), ITEMGROUPS("itemGroups.yml");
		public final String filename;

		private ConfigurationFile(String path) {
			this.filename = path;
		}

	}

}
