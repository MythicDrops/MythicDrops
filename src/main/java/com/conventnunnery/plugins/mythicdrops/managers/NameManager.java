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
import com.conventnunnery.plugins.mythicdrops.api.tiers.Tier;
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
    private final Map<Tier, List<String>> tierPrefixes;
    private final Map<Tier, List<String>> tierSuffixes;
    private final Map<Tier, List<String>> tierLore;
    private NamesLoader namesLoader;

    public NameManager(final MythicDrops plugin) {
        this.plugin = plugin;
        // Initializing the general Maps
        generalPrefixes = new ArrayList<String>();
        generalSuffixes = new ArrayList<String>();
        generalLore = new ArrayList<String>();
        // Initializing the Material Maps
        materialPrefixes = new HashMap<Material, List<String>>();
        materialSuffixes = new HashMap<Material, List<String>>();
        materialLore = new HashMap<Material, List<String>>();
        // Initializing the Tier Maps
        tierPrefixes = new HashMap<Tier, List<String>>();
        tierSuffixes = new HashMap<Tier, List<String>>();
        tierLore = new HashMap<Tier, List<String>>();
        // Initializing the NamesLoader
        namesLoader = new NamesLoader(plugin);
        // Loading all prefixes, suffixes, and lore
        loadGeneralPrefixes();
        loadGeneralSuffixes();
        loadGeneralLore();
        loadMaterialPrefixes();
        loadMaterialSuffixes();
        loadMaterialLore();
        loadTierPrefixes();
        loadTierSuffixes();
        loadTierLore();
    }

    public Map<Tier, List<String>> getTierPrefixes() {
        return tierPrefixes;
    }

    public Map<Tier, List<String>> getTierSuffixes() {
        return tierSuffixes;
    }

    public Map<Tier, List<String>> getTierLore() {
        return tierLore;
    }

    public final void debugNames() {
        plugin.getDebugger().debug(Level.INFO, "General prefixes: " + String.valueOf(generalPrefixes.size()) + " | " +
                "General suffixes: " + String.valueOf(generalSuffixes.size()) + " | General lore: " +
                String.valueOf(generalLore.size()));
        plugin.getDebugger().debug(Level.INFO,
                "Material prefixes: " + String.valueOf(materialPrefixes.keySet().size()) + " | Material " +
                        "suffixes: " + String.valueOf(materialSuffixes.keySet().size()) + " | Material lore: " +
                        String.valueOf(materialLore.keySet().size()));
        plugin.getDebugger().debug(Level.INFO,
                "Tier prefixes: " + String.valueOf(tierPrefixes.keySet().size()) + " | Tier " +
                        "suffixes: " + String.valueOf(tierSuffixes.keySet().size()) + " | Tier lore: " +
                        String.valueOf(tierLore.keySet().size()));
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
        File folderLoc = new File(plugin.getDataFolder(), "/resources/prefixes/materials/");

        if (!folderLoc.exists() && !folderLoc.mkdir()) {
            return;
        }

        namesLoader.writeDefault("/resources/prefixes/materials/diamond_sword.txt", false);

        try {
            for (File f : folderLoc.listFiles()) {
                if (f.getName().endsWith(".txt")) {
                    namesLoader.loadMaterialFile(materialPrefixes, "/resources/prefixes/materials/" + f.getName());
                }
            }
        } catch (Exception e) {
            plugin.getDebugger().debug(Level.WARNING, "Could not load prefix file");
        }
    }

    public final void loadTierPrefixes() {
        tierPrefixes.clear();
        File folderLoc = new File(plugin.getDataFolder(), "/resources/prefixes/tiers/");

        if (!folderLoc.exists() && !folderLoc.mkdir()) {
            return;
        }

        namesLoader.writeDefault("/resources/prefixes/tiers/diamond_sword.txt", false);

        try {
            for (File f : folderLoc.listFiles()) {
                if (f.getName().endsWith(".txt")) {
                    namesLoader.loadTierFile(tierPrefixes, "/resources/prefixes/tiers/" + f.getName());
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
        File folderLoc = new File(plugin.getDataFolder(), "/resources/suffixes/materials/");

        if (!folderLoc.exists() && !folderLoc.mkdir()) {
            return;
        }

        namesLoader.writeDefault("/resources/suffixes/materials/diamond_sword.txt", false);

        try {
            for (File f : folderLoc.listFiles()) {
                if (f.getName().endsWith(".txt")) {
                    namesLoader.loadMaterialFile(materialSuffixes, "/resources/suffixes/materials/" + f.getName());
                }
            }
        } catch (Exception e) {
            plugin.getDebugger().debug(Level.WARNING, "Could not load suffix file");
        }
    }

    public final void loadTierSuffixes() {
        tierPrefixes.clear();
        File folderLoc = new File(plugin.getDataFolder(), "/resources/suffixes/tiers/");

        if (!folderLoc.exists() && !folderLoc.mkdir()) {
            return;
        }

        namesLoader.writeDefault("/resources/suffixes/tiers/diamond_sword.txt", false);

        try {
            for (File f : folderLoc.listFiles()) {
                if (f.getName().endsWith(".txt")) {
                    namesLoader.loadTierFile(tierSuffixes, "/resources/suffixes/tiers/" + f.getName());
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
        File folderLoc = new File(plugin.getDataFolder(), "/resources/lore/materials/");

        if (!folderLoc.exists() && !folderLoc.mkdir()) {
            return;
        }

        namesLoader.writeDefault("/resources/lore/materials/diamond_sword.txt", false);

        try {
            for (File f : folderLoc.listFiles()) {
                if (f.getName().endsWith(".txt")) {
                    namesLoader.loadMaterialFile(materialLore, "/resources/lore/materials/" + f.getName());
                }
            }
        } catch (Exception e) {
            plugin.getDebugger().debug(Level.WARNING, "Could not load lore file");
        }
    }

    public final void loadTierLore() {
        tierPrefixes.clear();
        File folderLoc = new File(plugin.getDataFolder(), "/resources/lore/tiers/");

        if (!folderLoc.exists() && !folderLoc.mkdir()) {
            return;
        }

        namesLoader.writeDefault("/resources/lore/tiers/diamond_sword.txt", false);

        try {
            for (File f : folderLoc.listFiles()) {
                if (f.getName().endsWith(".txt")) {
                    namesLoader.loadTierFile(tierLore, "/resources/lore/tiers/" + f.getName());
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
