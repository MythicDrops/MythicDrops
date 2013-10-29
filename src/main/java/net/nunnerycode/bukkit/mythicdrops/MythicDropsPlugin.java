/*
 * Copyright (C) 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.nunnerycode.bukkit.mythicdrops;

import com.conventnunnery.libraries.config.CommentedConventYamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import net.nunnerycode.bukkit.libraries.module.Module;
import net.nunnerycode.bukkit.libraries.module.ModuleManager;
import net.nunnerycode.bukkit.libraries.module.ModulePlugin;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.commands.MythicCommand;
import net.nunnerycode.bukkit.mythicdrops.api.loaders.ConfigLoader;
import net.nunnerycode.bukkit.mythicdrops.api.managers.CustomItemManager;
import net.nunnerycode.bukkit.mythicdrops.api.managers.DropManager;
import net.nunnerycode.bukkit.mythicdrops.api.managers.EntityManager;
import net.nunnerycode.bukkit.mythicdrops.api.managers.ItemManager;
import net.nunnerycode.bukkit.mythicdrops.api.managers.LanguageManager;
import net.nunnerycode.bukkit.mythicdrops.api.managers.NameManager;
import net.nunnerycode.bukkit.mythicdrops.api.managers.SettingsManager;
import net.nunnerycode.bukkit.mythicdrops.api.managers.TierManager;
import net.nunnerycode.bukkit.mythicdrops.api.savers.ConfigSaver;
import net.nunnerycode.bukkit.mythicdrops.commands.MythicDropsCommand;
import net.nunnerycode.bukkit.mythicdrops.loaders.MythicCustomItemLoader;
import net.nunnerycode.bukkit.mythicdrops.loaders.MythicLanguageLoader;
import net.nunnerycode.bukkit.mythicdrops.loaders.MythicSettingsLoader;
import net.nunnerycode.bukkit.mythicdrops.loaders.MythicTierLoader;
import net.nunnerycode.bukkit.mythicdrops.managers.MythicCustomItemManager;
import net.nunnerycode.bukkit.mythicdrops.managers.MythicDropManager;
import net.nunnerycode.bukkit.mythicdrops.managers.MythicEntityManager;
import net.nunnerycode.bukkit.mythicdrops.managers.MythicItemManager;
import net.nunnerycode.bukkit.mythicdrops.managers.MythicLanguageManager;
import net.nunnerycode.bukkit.mythicdrops.managers.MythicNameManager;
import net.nunnerycode.bukkit.mythicdrops.managers.MythicSettingsManager;
import net.nunnerycode.bukkit.mythicdrops.managers.MythicTierManager;
import net.nunnerycode.bukkit.mythicdrops.savers.MythicCustomItemSaver;
import net.nunnerycode.bukkit.mythicdrops.savers.MythicLanguageSaver;
import net.nunnerycode.bukkit.mythicdrops.savers.MythicSettingsSaver;
import net.nunnerycode.bukkit.mythicdrops.savers.MythicTierSaver;
import net.nunnerycode.java.libraries.cannonball.DebugPrinter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mcstats.Metrics;

public final class MythicDropsPlugin extends ModulePlugin implements MythicDrops {

	public static MythicDropsPlugin instance;
	private NameManager mythicNameManager;
	private TierManager mythicTierManager;
	private LanguageManager mythicLanguageManager;
	private CustomItemManager mythicCustomItemManager;
	private ConfigLoader tierLoader;
	private ConfigLoader customItemLoader;
	private SettingsManager mythicSettingsManager;
	private ConfigLoader languageLoader;
	private ConfigLoader settingsLoader;
	private ItemManager mythicItemManager;
	private File jar;
	private EntityManager mythicEntityManager;
	private DropManager mythicDropManager;
	private ConfigSaver customItemSaver;
	private ConfigSaver languageSaver;
	private ConfigSaver tierSaver;
	private ConfigSaver settingsSaver;
	private CommentedConventYamlConfiguration configYAML;
	private CommentedConventYamlConfiguration customItemsYAML;
	private CommentedConventYamlConfiguration itemGroupsYAML;
	private CommentedConventYamlConfiguration languageYAML;
	private CommentedConventYamlConfiguration tierYAML;
	private MythicCommand command;
	private DebugPrinter debugPrinter;
	private ModuleManager moduleManager;

	public MythicDropsPlugin() {
		instance = this;
	}

	public CommentedConventYamlConfiguration getConfigYAML() {
		return configYAML;
	}

	public CommentedConventYamlConfiguration getCustomItemsYAML() {
		return customItemsYAML;
	}

	public CommentedConventYamlConfiguration getItemGroupsYAML() {
		return itemGroupsYAML;
	}

	public CommentedConventYamlConfiguration getLanguageYAML() {
		return languageYAML;
	}

	public CommentedConventYamlConfiguration getTierYAML() {
		return tierYAML;
	}

	public EntityManager getEntityManager() {
		return mythicEntityManager;
	}

	public DropManager getDropManager() {
		return mythicDropManager;
	}

	public ConfigLoader getLanguageLoader() {
		return languageLoader;
	}

	public ConfigLoader getSettingsLoader() {
		return settingsLoader;
	}

	public ConfigLoader getCustomItemLoader() {
		return customItemLoader;
	}

	public NameManager getNameManager() {
		return mythicNameManager;
	}

	public void reload() {
		disable();
		debug(Level.INFO, getDescription().getName() + " v" + getDescription().getVersion() + " reloaded");
		enable();
	}

	private void enable() {
		unpackConfigurationFiles(new String[]{"config.yml", "customItems.yml", "itemGroups.yml", "language.yml",
				"tier.yml"}, false);

		// Setting up the configuration files
		configYAML = new CommentedConventYamlConfiguration(new File(getDataFolder().getPath(), "config.yml"),
				YamlConfiguration.loadConfiguration(getResource("config.yml")).getString("version"));
		configYAML.options().backupOnUpdate(true);
		configYAML.options().updateOnLoad(true);
		configYAML.load();
		customItemsYAML = new CommentedConventYamlConfiguration(new File(getDataFolder().getPath(), "customItems.yml"),
				YamlConfiguration.loadConfiguration(getResource("customItems.yml")).getString("version"));
		customItemsYAML.options().backupOnUpdate(true);
		customItemsYAML.options().updateOnLoad(true);
		customItemsYAML.load();
		itemGroupsYAML = new CommentedConventYamlConfiguration(new File(getDataFolder().getPath(), "itemGroups.yml"),
				YamlConfiguration.loadConfiguration(getResource("itemGroups.yml")).getString("version"));
		itemGroupsYAML.options().backupOnUpdate(true);
		itemGroupsYAML.options().updateOnLoad(true);
		itemGroupsYAML.load();
		languageYAML = new CommentedConventYamlConfiguration(new File(getDataFolder().getPath(), "language.yml"),
				YamlConfiguration.loadConfiguration(getResource("language.yml")).getString("version"));
		languageYAML.options().backupOnUpdate(true);
		languageYAML.options().updateOnLoad(true);
		languageYAML.load();
		tierYAML = new CommentedConventYamlConfiguration(new File(getDataFolder().getPath(), "tier.yml"),
				YamlConfiguration.loadConfiguration(getResource("tier.yml")).getString("version"));
		tierYAML.options().backupOnUpdate(true);
		tierYAML.options().updateOnLoad(true);
		tierYAML.load();

		mythicSettingsManager = new MythicSettingsManager(this);

		settingsLoader = new MythicSettingsLoader(this);

		settingsLoader.load();

		// Initializing the LanguageManager
		mythicLanguageManager = new MythicLanguageManager(this);

		languageLoader = new MythicLanguageLoader(this);

		languageLoader.load();

		// Initializing the TierManager
		mythicTierManager = new MythicTierManager(this);

		// Initialize loaders
		tierLoader = new MythicTierLoader(this);

		// Build loaders
		tierLoader.load();

		mythicTierManager.debugTiers();

		// Initializing the NameManager
		mythicNameManager = new MythicNameManager(this);

		mythicNameManager.debugNames();

		// Initializing the CustomItemsManager
		mythicCustomItemManager = new MythicCustomItemManager(this);

		customItemLoader = new MythicCustomItemLoader(this);

		customItemLoader.load();

		mythicCustomItemManager.debugCustomItems();

		mythicItemManager = new MythicItemManager(this);

		mythicDropManager = new MythicDropManager(this);

		mythicEntityManager = new MythicEntityManager(this);

		command = new MythicDropsCommand(this);

		customItemSaver = new MythicCustomItemSaver(this);
		languageSaver = new MythicLanguageSaver(this);
		tierSaver = new MythicTierSaver(this);
		settingsSaver = new MythicSettingsSaver(this);
	}

	private void unpackConfigurationFiles(String[] configurationFiles, boolean overwrite) {
		for (String s : configurationFiles) {
			YamlConfiguration yc = YamlConfiguration.loadConfiguration(getResource(s));
			try {
				File f = new File(getDataFolder(), s);
				if (!f.exists()) {
					yc.save(new File(getDataFolder(), s));
					continue;
				}
				if (overwrite) {
					yc.save(new File(getDataFolder(), s));
				}
			} catch (IOException e) {
				getLogger().warning("Could not unpack " + s);
			}
		}
	}

	public SettingsManager getSettingsManager() {
		return mythicSettingsManager;
	}

	public ConfigLoader getTierLoader() {
		return tierLoader;
	}

	public TierManager getTierManager() {
		return mythicTierManager;
	}

	public LanguageManager getLanguageManager() {
		return mythicLanguageManager;
	}

	public CustomItemManager getCustomItemManager() {
		return mythicCustomItemManager;
	}

	public ItemManager getItemManager() {
		return mythicItemManager;
	}

	public File getJar() {
		return jar;
	}

	public ConfigSaver getCustomItemSaver() {
		return customItemSaver;
	}

	public ConfigSaver getLanguageSaver() {
		return languageSaver;
	}

	public ConfigSaver getTierSaver() {
		return tierSaver;
	}

	public ConfigSaver getSettingsSaver() {
		return settingsSaver;
	}

	public MythicCommand getCommand() {
		return command;
	}

	public void debug(Level level, String... messages) {
		if (getSettingsManager() != null) {
			if (getSettingsManager().isDebugMode()) {
				getDebugPrinter().debug(level, messages);
			}
		} else {
			getDebugPrinter().debug(level, messages);
		}
	}

	@Override
	public DebugPrinter getDebugPrinter() {
		return debugPrinter;
	}

	@Override
	public ModuleManager getModuleManager() {
		return moduleManager;
	}

	private void disable() {
//		customItemSaver.save();
//		languageSaver.save();
//		tierSaver.save();
//		settingsSaver.save();
	}

	@Override
	public void onLoad() {
		jar = this.getFile();
	}

	@Override
	public void onDisable() {
		disable();
		getModuleManager().disableModules();
		// Prints a debug message that the plugin is disabled
		debug(Level.INFO, getDescription().getName() + " v" + getDescription().getVersion() + " disabled");
		debug(Level.INFO, "", "", "");
	}

	@Override
	public void onEnable() {
		debugPrinter = new DebugPrinter(getDataFolder().getPath(), getDescription().getName() + ".log");
		debug(Level.INFO, "Using ModuleSystem v" + YamlConfiguration.loadConfiguration(getResource("modulesystem" +
				".yml")).getString("version"));
		moduleManager = new ModuleManager(this);
		enable();
		getModuleManager().loadModules();
		getModuleManager().enableModules();
		startMetrics();
		// Prints a debug message that the plugin is enabled
		debug(Level.INFO, getDescription().getName() + " v" + getDescription().getVersion() + " enabled");
	}

	private void startMetrics() {
		try {
			Metrics m = new Metrics(this);
			Metrics.Graph moduleUsedGraph = m.createGraph("Modules Used");
			for (final Module mod : this.getModuleManager().getModules()) {
				moduleUsedGraph.addPlotter(new Metrics.Plotter(mod.getName()) {
					@Override
					public int getValue() {
						if (mod.isEnabled()) {
							return 1;
						} else {
							return 0;
						}
					}
				});
			}
		} catch (IOException ignored) {
		}
	}

	public void debug(String... messages) {
		debug(Level.INFO, messages);
	}
}