package com.modcrafting.diablodrops.name;

import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.api.tiers.Tier;
import org.bukkit.Material;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/*
 *  Originally by deathmarine
 *  Modified by rmh4209/Nunnery/ToppleTheNun on 7/3/13
 */

public class NamesLoader {
    private File dataFolder;
    private MythicDrops plugin;

    public NamesLoader(final MythicDrops instance) {
        plugin = instance;
        dataFolder = instance.getDataFolder();
    }

    /**
     * Takes values from a file and adds them to list
     *
     * @param l    List of strings to add values
     * @param name Name of the file to take values from
     */
    public void loadFile(final List<String> l, final String name) {
        File file = new File(
                dataFolder, name);
        FileReader fileReader;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            plugin.getDebugger().debug(Level.WARNING, "Could not find file " + name);
            return;
        }
        BufferedReader list = new BufferedReader(fileReader);
        String p;
        try {
            while ((p = list.readLine()) != null) {
                if (!p.contains("#") && p.length() > 0) {
                    l.add(p);
                }
            }
            list.close();
        } catch (IOException exception) {
            plugin.getDebugger().debug(Level.WARNING, "Could not load file " + name);
        }
    }

    public void loadMaterialFile(final Map<Material, List<String>> hm,
                                 final String name) {
        File f = new File(dataFolder, name);
        Material m = Material.getMaterial(f.getName().replace(".txt", "")
                .toUpperCase());
        List<String> l = new ArrayList<String>();
        FileReader fileReader;
        try {
            fileReader = new FileReader(f);
        } catch (FileNotFoundException e) {
            plugin.getDebugger().debug(Level.WARNING, "Could not find file " + name);
            return;
        }
        BufferedReader list = new BufferedReader(fileReader);
        String p;
        try {
            while ((p = list.readLine()) != null) {
                if (!p.contains("#") && p.length() > 0) {
                    l.add(p);
                }
            }
            if (m != null) {
                hm.put(m, l);
            } else {
                hm.put(Material.AIR, l);
            }
            list.close();
        } catch (IOException exception) {
            plugin.getDebugger().debug(Level.WARNING, "Could not read file " + name);
        }
    }

    public void loadTierFile(final Map<Tier, List<String>> hm,
                                 final String name) {
        File f = new File(dataFolder, name);
        Tier t = plugin.getTierManager().getTierFromName(f.getName().replace(".txt", ""));
        if (t == null) {
            plugin.getTierManager().getTierFromDisplayName(f.getName().replace(".txt", ""));
        }
        List<String> l = new ArrayList<String>();
        FileReader fileReader;
        try {
            fileReader = new FileReader(f);
        } catch (FileNotFoundException e) {
            plugin.getDebugger().debug(Level.WARNING, "Could not find file " + name);
            return;
        }
        BufferedReader list = new BufferedReader(fileReader);
        String p;
        try {
            while ((p = list.readLine()) != null) {
                if (!p.contains("#") && p.length() > 0) {
                    l.add(p);
                }
            }
            if (t != null) {
                hm.put(t, l);
            }
            list.close();
        } catch (IOException exception) {
            plugin.getDebugger().debug(Level.WARNING, "Could not read file " + name);
        }
    }

    /**
     * Creates a file with given name
     *
     * @param name Name of the file to write
     */
    public void writeDefault(final String name, boolean overwrite) {
        File actual = new File(dataFolder, name);
        if (name.contains(".jar")) {
            actual = new File(dataFolder.getParent(), name);
        }
        File parentFile = actual.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            return;
        }
        if (!actual.exists() || overwrite) {
            InputStream input = this.getClass().getResourceAsStream(
                    "/" + name);
            FileOutputStream output;
            try {
                output = new FileOutputStream(actual, false);
            } catch (FileNotFoundException e) {
                plugin.getDebugger().debug(Level.WARNING, "Could not find file " + name);
                return;
            }
            byte[] buf = new byte[1024];
            int length;
            try {
                while ((length = input.read(buf)) > 0) {
                    output.write(buf, 0, length);
                }
                output.close();
                input.close();
            } catch (IOException exception) {
                plugin.getDebugger().debug(Level.WARNING, "Could not write file " + name);
            }
        }
    }
}