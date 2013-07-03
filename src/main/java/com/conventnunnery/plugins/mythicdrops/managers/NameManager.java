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
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A manager for dealing with names and lore.
 */
public class NameManager {

    private final MythicDrops plugin;
    private final List<String> generalPrefixes;
    private final List<String> generalSuffixes;
    private final List<String> generalLore;
    private final Map<Material, String> materialPrefixes;
    private final Map<Material, String> materialSuffixes;
    private final Map<Material, String> materialLore;

    public NameManager(final MythicDrops plugin) {
        this.plugin = plugin;
        generalPrefixes = new ArrayList<String>();
        generalSuffixes = new ArrayList<String>();
        generalLore = new ArrayList<String>();
        materialPrefixes = new HashMap<Material, String>();
        materialSuffixes = new HashMap<Material, String>();
        materialLore = new HashMap<Material, String>();
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
    public Map<Material, String> getMaterialPrefixes() {
        return materialPrefixes;
    }

    /**
     * Gets a Map containing suffixes for various {@link Material}s.
     *
     * @return map of suffixes for Materials
     */
    public Map<Material, String> getMaterialSuffixes() {
        return materialSuffixes;
    }

    /**
     * Gets a Map containing lore for various {@link Material}s.
     *
     * @return map of lore for Materials
     */
    public Map<Material, String> getMaterialLore() {
        return materialLore;
    }
}
