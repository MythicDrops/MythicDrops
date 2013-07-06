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

package com.conventnunnery.plugins.mythicdrops.managers;

import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.modcrafting.diablodrops.name.NamesLoader;
import org.bukkit.Material;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * A manager for dealing with names and lore.
 */
public class NameManager {

    private final MythicDrops plugin;
    private final List<String> generalPrefixes;
    private final List<String> generalSuffixes;
    private final List<String> generalLore;
    private final Map<Material, List<String>> materialPrefixes;
    private final Map<Material, List<String>> materialSuffixes;
    private final Map<Material, List<String>> materialLore;
    private NamesLoader namesLoader;

    public NameManager(final MythicDrops plugin) {
        this.plugin = plugin;
        generalPrefixes = new ArrayList<String>();
        generalSuffixes = new ArrayList<String>();
        generalLore = new ArrayList<String>();
        materialPrefixes = new HashMap<Material, List<String>>();
        materialSuffixes = new HashMap<Material, List<String>>();
        materialLore = new HashMap<Material, List<String>>();
        // Initializing the NamesLoader
        namesLoader = new NamesLoader(plugin);
        // Loading all prefixes, suffixes, and lore
        loadGeneralPrefixes();
        loadGeneralSuffixes();
        loadGeneralLore();
        loadMaterialPrefixes();
        loadMaterialSuffixes();
        loadMaterialLore();
    }

    public final void debugNames() {
        plugin.getDebugger().debug(Level.INFO, "General prefixes: " + generalPrefixes.size() + " | General suffixes: " +
                generalSuffixes.size() + " | General lore: " + generalLore.size());
        plugin.getDebugger().debug(Level.INFO,
                "Material prefixes: " + materialPrefixes.keySet().size() + " | Material " +
                        "suffixes: " + materialSuffixes.keySet().size() + " | Material lore: " + materialLore.keySet()
                        .size());
    }

    public final void loadGeneralPrefixes() {
        generalPrefixes.clear();

        try {
            namesLoader.writeDefault("resources/prefixes/general.txt", false);
        } catch (Exception e) {
            plugin.getDebugger().debug(Level.WARNING, "Could not write general prefix file");
        }

        try {
            namesLoader.loadFile(generalPrefixes, "resources/prefixes/general.txt");
        } catch (Exception e) {
            plugin.getDebugger().debug(Level.WARNING, "Could not load general prefixes");
        }
    }

    public final void loadMaterialPrefixes() {
        materialPrefixes.clear();
        File folderLoc = new File(plugin.getDataFolder(), "/resources/prefixes/");

        if (!folderLoc.exists() && !folderLoc.mkdir()) {
            return;
        }

        try {
            for (File f : folderLoc.listFiles()) {
                if (f.getName().endsWith(".txt")) {
                    namesLoader.loadMaterialFile(materialPrefixes, "/resources/prefixes/" + f.getName());
                }
            }
        } catch (Exception e) {
            plugin.getDebugger().debug(Level.WARNING, "Could not load prefix file");
        }
    }

    public final void loadGeneralSuffixes() {
        generalSuffixes.clear();

        try {
            namesLoader.writeDefault("resources/suffixes/general.txt", false);
        } catch (Exception e) {
            plugin.getDebugger().debug(Level.WARNING, "Could not write general suffix file");
        }

        try {
            namesLoader.loadFile(generalSuffixes, "resources/suffixes/general.txt");
        } catch (Exception e) {
            plugin.getDebugger().debug(Level.WARNING, "Could not load general suffixes");
        }
    }

    public final void loadMaterialSuffixes() {
        materialSuffixes.clear();
        File folderLoc = new File(plugin.getDataFolder(), "/resources/suffixes/");

        if (!folderLoc.exists() && !folderLoc.mkdir()) {
            return;
        }

        try {
            for (File f : folderLoc.listFiles()) {
                if (f.getName().endsWith(".txt")) {
                    namesLoader.loadMaterialFile(materialSuffixes, "/resources/suffixes/" + f.getName());
                }
            }
        } catch (Exception e) {
            plugin.getDebugger().debug(Level.WARNING, "Could not load suffix file");
        }
    }

    public final void loadGeneralLore() {
        generalLore.clear();

        try {
            namesLoader.writeDefault("resources/lore/general.txt", false);
        } catch (Exception e) {
            plugin.getDebugger().debug(Level.WARNING, "Could not write general lore file");
        }

        try {
            namesLoader.loadFile(generalLore, "resources/lore/general.txt");
        } catch (Exception e) {
            plugin.getDebugger().debug(Level.WARNING, "Could not load general lore");
        }
    }

    public final void loadMaterialLore() {
        materialLore.clear();
        File folderLoc = new File(plugin.getDataFolder(), "/resources/lore/");

        if (!folderLoc.exists() && !folderLoc.mkdir()) {
            return;
        }

        try {
            for (File f : folderLoc.listFiles()) {
                if (f.getName().endsWith(".txt")) {
                    namesLoader.loadMaterialFile(materialLore, "/resources/lore/" + f.getName());
                }
            }
        } catch (Exception e) {
            plugin.getDebugger().debug(Level.WARNING, "Could not load lore file");
        }
    }

    public NamesLoader getNamesLoader() {
        return namesLoader;
    }

    /**
     * Gets the instance of MythicDrops being run.
     *
     * @return instance of MythicDrops
     */
    public MythicDrops getPlugin() {
        return plugin;
    }

    /**
     * Gets the list of general prefixes from the plugin's resources.
     *
     * @return list of general prefixes
     */
    public List<String> getGeneralPrefixes() {
        return generalPrefixes;
    }

    /**
     * Gets the list of general suffixes from the plugin's resources.
     *
     * @return list of general suffixes
     */
    public List<String> getGeneralSuffixes() {
        return generalSuffixes;
    }

    /**
     * Gets the list of general lore from the plugin's resources.
     *
     * @return list of general lore
     */
    public List<String> getGeneralLore() {
        return generalLore;
    }

    /**
     * Gets a Map containing prefixes for various {@link Material}s.
     *
     * @return map of prefixes for Materials
     */
    public Map<Material, List<String>> getMaterialPrefixes() {
        return materialPrefixes;
    }

    /**
     * Gets a Map containing suffixes for various {@link Material}s.
     *
     * @return map of suffixes for Materials
     */
    public Map<Material, List<String>> getMaterialSuffixes() {
        return materialSuffixes;
    }

    /**
     * Gets a Map containing lore for various {@link Material}s.
     *
     * @return map of lore for Materials
     */
    public Map<Material, List<String>> getMaterialLore() {
        return materialLore;
    }
}
