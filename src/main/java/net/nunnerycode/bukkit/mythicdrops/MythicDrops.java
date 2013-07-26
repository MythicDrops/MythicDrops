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

import com.conventnunnery.libraries.config.ConventConfigurationManager;
import com.conventnunnery.libraries.config.IConfigurationFile;
import com.conventnunnery.libraries.debug.Debugger;
import net.nunnerycode.bukkit.mythicdrops.api.utils.MythicLoader;
import net.nunnerycode.bukkit.mythicdrops.configuration.MythicConfigurationFile;
import net.nunnerycode.bukkit.mythicdrops.loaders.MythicCustomItemLoader;
import net.nunnerycode.bukkit.mythicdrops.loaders.MythicLanguageLoader;
import net.nunnerycode.bukkit.mythicdrops.loaders.MythicSettingsLoader;
import net.nunnerycode.bukkit.mythicdrops.loaders.MythicTierLoader;
import net.nunnerycode.bukkit.mythicdrops.managers.CustomItemManager;
import net.nunnerycode.bukkit.mythicdrops.managers.DropManager;
import net.nunnerycode.bukkit.mythicdrops.managers.EntityManager;
import net.nunnerycode.bukkit.mythicdrops.managers.ItemManager;
import net.nunnerycode.bukkit.mythicdrops.managers.LanguageManager;
import net.nunnerycode.bukkit.mythicdrops.managers.ModuleManager;
import net.nunnerycode.bukkit.mythicdrops.managers.NameManager;
import net.nunnerycode.bukkit.mythicdrops.managers.SettingsManager;
import net.nunnerycode.bukkit.mythicdrops.managers.TierManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public final class MythicDrops extends JavaPlugin {

    public static MythicDrops instance;
    private Debugger debugger;
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
    private ModuleManager moduleManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    private EntityManager entityManager;
    private DropManager dropManager;

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
        // Prints a debug message that the plugin is disabled
        debug(Level.INFO, getDescription().getName() + " v" + getDescription().getVersion() + " disabled");
    }

    public void debug(Level level, String... messages) {
        if (getSettingsManager().isDebugMode()) {
            debugger.debug(level, messages);
        }
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    @Override
    public void onEnable() {
        instance = this;

        // Initialize Debugger
        debugger = new Debugger(this);

        // Setting up the configuration files
        Set<IConfigurationFile> configurationFiles = new HashSet<IConfigurationFile>();
        Collections.addAll(configurationFiles, MythicConfigurationFile.values());
        configurationManager = new ConventConfigurationManager(this, configurationFiles);

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

        moduleManager = new ModuleManager(this);

        // Prints a debug message that the plugin is enabled
        debug(Level.INFO, getDescription().getName() + " v" + getDescription().getVersion() + " enabled");
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
}
