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

import com.conventnunnery.libraries.config.ConventYamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import net.nunnerycode.bukkit.libraries.module.Module;
import net.nunnerycode.bukkit.libraries.module.ModuleLoader;
import net.nunnerycode.bukkit.libraries.module.ModulePlugin;
import net.nunnerycode.bukkit.mythicdrops.api.utils.MythicLoader;
import net.nunnerycode.bukkit.mythicdrops.api.utils.MythicSaver;
import net.nunnerycode.bukkit.mythicdrops.commands.MythicDropsCommand;
import net.nunnerycode.bukkit.mythicdrops.loaders.MythicCustomItemLoader;
import net.nunnerycode.bukkit.mythicdrops.loaders.MythicLanguageLoader;
import net.nunnerycode.bukkit.mythicdrops.loaders.MythicSettingsLoader;
import net.nunnerycode.bukkit.mythicdrops.loaders.MythicTierLoader;
import net.nunnerycode.bukkit.mythicdrops.managers.CustomItemManager;
import net.nunnerycode.bukkit.mythicdrops.managers.DropManager;
import net.nunnerycode.bukkit.mythicdrops.managers.EntityManager;
import net.nunnerycode.bukkit.mythicdrops.managers.ItemManager;
import net.nunnerycode.bukkit.mythicdrops.managers.LanguageManager;
import net.nunnerycode.bukkit.mythicdrops.managers.NameManager;
import net.nunnerycode.bukkit.mythicdrops.managers.SettingsManager;
import net.nunnerycode.bukkit.mythicdrops.managers.TierManager;
import net.nunnerycode.bukkit.mythicdrops.savers.MythicCustomItemSaver;
import net.nunnerycode.bukkit.mythicdrops.savers.MythicLanguageSaver;
import net.nunnerycode.bukkit.mythicdrops.savers.MythicSettingsSaver;
import net.nunnerycode.bukkit.mythicdrops.savers.MythicTierSaver;
import net.nunnerycode.java.libraries.cannonball.DebugPrinter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mcstats.Metrics;

public final class MythicDrops extends ModulePlugin {

	public static MythicDrops instance;
	private NameManager nameManager;
	private TierManager tierManager;
	private LanguageManager languageManager;
	private CustomItemManager customItemManager;
	private MythicLoader tierLoader;
	private MythicLoader customItemLoader;
	private SettingsManager settingsManager;
	private MythicLoader languageLoader;
	private MythicLoader settingsLoader;
	private ItemManager itemManager;
	private File jar;
	private EntityManager entityManager;
	private DropManager dropManager;
	private ModuleLoader moduleLoader;
	private MythicSaver customItemSaver;
	private MythicSaver languageSaver;
	private MythicSaver tierSaver;
	private MythicSaver settingsSaver;
	private ConventYamlConfiguration configYAML;
	private ConventYamlConfiguration customItemsYAML;
	private ConventYamlConfiguration itemGroupsYAML;
	private ConventYamlConfiguration languageYAML;
	private ConventYamlConfiguration tierYAML;
	private MythicDropsCommand command;
	private DebugPrinter debugPrinter;

	public MythicDrops() {
		instance = this;
	}

	public ConventYamlConfiguration getConfigYAML() {
		return configYAML;
	}

	public ConventYamlConfiguration getCustomItemsYAML() {
		return customItemsYAML;
	}

	public ConventYamlConfiguration getItemGroupsYAML() {
		return itemGroupsYAML;
	}

	public ConventYamlConfiguration getLanguageYAML() {
		return languageYAML;
	}

	public ConventYamlConfiguration getTierYAML() {
		return tierYAML;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public DropManager getDropManager() {
		return dropManager;
	}

	public MythicLoader getLanguageLoader() {
		return languageLoader;
	}

	public MythicLoader getSettingsLoader() {
		return settingsLoader;
	}

	public MythicLoader getCustomItemLoader() {
		return customItemLoader;
	}

	public NameManager getNameManager() {
		return nameManager;
	}

	public void reload() {
		disable();
		debug(Level.INFO, getDescription().getName() + " v" + getDescription().getVersion() + " reloaded");
		enable();
	}

	private void enable() {
		// Setting up the configuration files
		configYAML = new ConventYamlConfiguration(new File(getDataFolder().getPath(), "config.yml"),
				YamlConfiguration.loadConfiguration(getResource("config.yml")).getString("version"));
		configYAML.options().updateOnLoad(true);
		configYAML.options().createDefaultFile(true);
		configYAML.load();
		customItemsYAML = new ConventYamlConfiguration(new File(getDataFolder().getPath(), "customItems.yml"),
				YamlConfiguration.loadConfiguration(getResource("customItems.yml")).getString("version"));
		customItemsYAML.options().updateOnLoad(true);
		customItemsYAML.options().createDefaultFile(true);
		customItemsYAML.load();
		itemGroupsYAML = new ConventYamlConfiguration(new File(getDataFolder().getPath(), "itemGroups.yml"),
				YamlConfiguration.loadConfiguration(getResource("itemGroups.yml")).getString("version"));
		itemGroupsYAML.options().updateOnLoad(true);
		itemGroupsYAML.options().createDefaultFile(true);
		itemGroupsYAML.load();
		languageYAML = new ConventYamlConfiguration(new File(getDataFolder().getPath(), "language.yml"),
				YamlConfiguration.loadConfiguration(getResource("language.yml")).getString("version"));
		languageYAML.options().updateOnLoad(true);
		languageYAML.options().createDefaultFile(true);
		languageYAML.load();
		tierYAML = new ConventYamlConfiguration(new File(getDataFolder().getPath(), "tier.yml"),
				YamlConfiguration.loadConfiguration(getResource("tier.yml")).getString("version"));
		tierYAML.options().updateOnLoad(true);
		tierYAML.options().createDefaultFile(true);
		tierYAML.load();

		settingsManager = new SettingsManager(this);

		settingsLoader = new MythicSettingsLoader(this);

		settingsLoader.load();

		// Initializing the LanguageManager
		languageManager = new LanguageManager(this);

		languageLoader = new MythicLanguageLoader(this);

		languageLoader.load();

		// Initializing the TierManager
		tierManager = new TierManager(this);

		// Initialize loaders
		tierLoader = new MythicTierLoader(this);

		// Build loaders
		tierLoader.load();

		tierManager.debugTiers();

		// Initializing the NameManager
		nameManager = new NameManager(this);

		nameManager.debugNames();

		// Initializing the CustomItemsManager
		customItemManager = new CustomItemManager(this);

		customItemLoader = new MythicCustomItemLoader(this);

		customItemLoader.load();

		customItemManager.debugCustomItems();

		itemManager = new ItemManager(this);

		dropManager = new DropManager(this);

		entityManager = new EntityManager(this);

		moduleLoader = new ModuleLoader(this);

		command = new MythicDropsCommand(this);

		getModules().clear();

		moduleLoader.loadModules(new File(getDataFolder(), "/modules/"));

		for (Module m : getModules()) {
			if (m.isEnabled()) {
				getLogger().log(Level.INFO, "Module loaded: " + m.getName());
				debug(Level.INFO, "Module loaded: " + m.getName());
			}
		}

		customItemSaver = new MythicCustomItemSaver(this);
		languageSaver = new MythicLanguageSaver(this);
		tierSaver = new MythicTierSaver(this);
		settingsSaver = new MythicSettingsSaver(this);
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

	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	public DebugPrinter getDebugPrinter() {
		return debugPrinter;
	}

	private void disable() {
		for (Module m : getModules()) {
			m.disable();
		}

//		customItemSaver.save();
//		languageSaver.save();
//		tierSaver.save();
//		settingsSaver.save();
	}

	@Override
	public void onLoad() {
		jar = this.getFile();

		debugPrinter = new DebugPrinter(getDataFolder().getPath(), "debug.log");

		debug(Level.INFO, "Initializing MythicDrops v" + getDescription().getVersion());
	}

	@Override
	public void onDisable() {
		disable();

		// Prints a debug message that the plugin is disabled
		debug(Level.INFO, getDescription().getName() + " v" + getDescription().getVersion() + " disabled");
		debug(Level.INFO, "", "", "");
	}

	@Override
	public void onEnable() {
		enable();

		startMetrics();
		// Prints a debug message that the plugin is enabled
		debug(Level.INFO, getDescription().getName() + " v" + getDescription().getVersion() + " enabled");
	}

	public void debug(String... messages) {
		debug(Level.INFO, messages);
	}

	private void startMetrics() {
		try {
			Metrics m = new Metrics(this);
			Metrics.Graph moduleUsedGraph = m.createGraph("Modules Used");
			for (final Module mod : getModules()) {
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

	public MythicLoader getTierLoader() {
		return tierLoader;
	}

	public TierManager getTierManager() {
		return tierManager;
	}

	public LanguageManager getLanguageManager() {
		return languageManager;
	}

	public CustomItemManager getCustomItemManager() {
		return customItemManager;
	}

	public ItemManager getItemManager() {
		return itemManager;
	}

	public File getJar() {
		return jar;
	}

	public ModuleLoader getModuleLoader() {
		return moduleLoader;
	}

	public MythicSaver getCustomItemSaver() {
		return customItemSaver;
	}

	public MythicSaver getLanguageSaver() {
		return languageSaver;
	}

	public MythicSaver getTierSaver() {
		return tierSaver;
	}

	public MythicSaver getSettingsSaver() {
		return settingsSaver;
	}

	public MythicDropsCommand getCommand() {
		return command;
	}
}