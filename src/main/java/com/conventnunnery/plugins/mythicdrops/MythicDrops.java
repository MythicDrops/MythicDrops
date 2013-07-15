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

package com.conventnunnery.plugins.mythicdrops;

import com.conventnunnery.libraries.config.ConventConfigurationManager;
import com.conventnunnery.libraries.config.IConfigurationFile;
import com.conventnunnery.libraries.debug.Debugger;
import com.conventnunnery.plugins.mythicdrops.api.utils.MythicLoader;
import com.conventnunnery.plugins.mythicdrops.configuration.MythicConfigurationFile;
import com.conventnunnery.plugins.mythicdrops.loaders.MythicTierLoader;
import com.conventnunnery.plugins.mythicdrops.managers.LanguageManager;
import com.conventnunnery.plugins.mythicdrops.managers.NameManager;
import com.conventnunnery.plugins.mythicdrops.managers.TierManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class MythicDrops extends JavaPlugin {

    private Debugger debugger;
    private NameManager nameManager;
    private ConventConfigurationManager configurationManager;
    private TierManager tierManager;
    private LanguageManager languageManager;
    private MythicLoader tierLoader;

    public ConventConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public NameManager getNameManager() {
        return nameManager;
    }

    public Debugger getDebugger() {
        return debugger;
    }

    @Override
    public void onEnable() {
        // Initialize Debugger
        debugger = new Debugger(this);

        // Setting up the configuration files
        Set<IConfigurationFile> configurationFiles = new HashSet<IConfigurationFile>();
        Collections.addAll(configurationFiles, MythicConfigurationFile.values());
        configurationManager = new ConventConfigurationManager(this, configurationFiles);

        // Initializing the LanguageManager
        languageManager = new LanguageManager(this);

        // Initializing the NameManager
        nameManager = new NameManager(this);

        nameManager.debugNames();

        // Initializing the TierManager
        tierManager = new TierManager(this);

        // Initialize loaders
        tierLoader = new MythicTierLoader(this);

        // Build loaders
        tierLoader.load();

        tierManager.debugTiers();

        // Prints a debug message that the plugin is enabled
        debugger.debug(Level.INFO, "Plugin enabled");
    }

    @Override
    public void onDisable() {
        // Prints a debug message that the plugin is disabled
        debugger.debug(Level.INFO, "Plugin disabled");
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
}
