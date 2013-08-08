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

import com.conventnunnery.libraries.config.ConventConfiguration;
import com.conventnunnery.libraries.config.ConventConfigurationGroup;
import com.conventnunnery.libraries.config.ConventConfigurationManager;
import net.nunnerycode.bukkit.libraries.debug.Debugger;
import net.nunnerycode.bukkit.libraries.module.Module;
import net.nunnerycode.bukkit.libraries.module.ModuleLoader;
import net.nunnerycode.bukkit.libraries.module.ModulePlugin;
import net.nunnerycode.bukkit.mythicdrops.api.utils.MythicLoader;
import net.nunnerycode.bukkit.mythicdrops.api.utils.MythicSaver;
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
import net.nunnerycode.bukkit.mythicdrops.savers.MythicTierSaver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class MythicDrops extends ModulePlugin {

	public static MythicDrops instance;
	private NameManager nameManager;
	private ConventConfigurationManager configurationManager;
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
	private Debugger debugger;
	private ConventConfigurationGroup conventConfigurationGroup;
	private MythicSaver customItemSaver;
	private MythicSaver languageSaver;
	private MythicSaver tierSaver;

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

	public ConventConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	public NameManager getNameManager() {
		return nameManager;
	}

	@Override
	public void onDisable() {
		for (Module m : getModules()) {
			m.disable();
		}

		customItemSaver.save();
		languageSaver.save();
		tierSaver.save();

		// Prints a debug message that the plugin is disabled
		debug(Level.INFO, getDescription().getName() + " v" + getDescription().getVersion() + " disabled");
		debug(Level.INFO, "", "", "");
	}

	@Override
	public void onEnable() {
		instance = this;

		jar = this.getFile();

		debugger = new Debugger(this);

		debug(Level.INFO, "Initializing MythicDrops v" + getDescription().getVersion());

		// Setting up the configuration files
		configurationManager = new ConventConfigurationManager(this);
		configurationManager.unpackConfigurationFiles("config.yml", "customItems.yml",
				"itemGroups.yml", "language.yml", "tier.yml");
		conventConfigurationGroup = configurationManager.getConventConfigurationGroup(getDataFolder());

		conventConfigurationGroup.loadAll();

		for (ConventConfiguration c : conventConfigurationGroup.getConventConfigurations()) {
			getLogger().info("Configuration loaded: " + c.getName());
			debug(Level.INFO, "Configuration loaded: " + c.getName());
		}

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

		// Prints a debug message that the plugin is enabled
		debug(Level.INFO, getDescription().getName() + " v" + getDescription().getVersion() + " enabled");
	}

	public void debug(Level level, String... messages) {
		if (getSettingsManager() != null) {
			if (getSettingsManager().isDebugMode()) {
				getDebugger().debug(level, messages);
			}
		} else {
			getDebugger().debug(level, messages);
		}
	}

	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	public Debugger getDebugger() {
		return debugger;
	}

	private ConventConfigurationGroup setupConventConfigurationGroup(String... s) {
		List<ConventConfiguration> configurationList = new ArrayList<ConventConfiguration>();
		ConventConfiguration c;
		for (String string : s) {
			c = configurationManager.getConventConfiguration(new File(getDataFolder(), string));
			if (c != null) {
				configurationList.add(c);
			}
		}
		return new ConventConfigurationGroup(configurationList);
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

	public ConventConfigurationGroup getConventConfigurationGroup() {
		return conventConfigurationGroup;
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
}