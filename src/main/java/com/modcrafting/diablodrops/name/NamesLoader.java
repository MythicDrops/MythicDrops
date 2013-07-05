package com.modcrafting.diablodrops.name;

import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import org.bukkit.Material;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
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
        try {
            BufferedReader list = new BufferedReader(new FileReader(new File(
                    dataFolder, name)));
            String p;
            while ((p = list.readLine()) != null) {
                if (!p.contains("#") && (p.length() > 0)) {
                    l.add(p);
                }
            }
            list.close();
        } catch (Exception e) {
            plugin.getDebugger().debug(Level.WARNING, "Could not load " + name);
        }
    }

    public void loadMaterialFile(final Map<Material, List<String>> hm,
                                 final String name) {
        File f = new File(dataFolder, name);
        Material m = Material.getMaterial(f.getName().replace(".txt", "")
                .toUpperCase());
        List<String> l = new ArrayList<String>();
        try {
            BufferedReader list = new BufferedReader(new FileReader(f));
            String p;
            while ((p = list.readLine()) != null) {
                if (!p.contains("#") && (p.length() > 0)) {
                    l.add(p);
                }
            }

            if (m != null) {
                hm.put(m, l);
            } else {
                hm.put(Material.AIR, l);
            }

            list.close();
        } catch (Exception e) {
            plugin.getDebugger().debug(Level.WARNING, "Could not load " + name);
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
            try {
                InputStream input = this.getClass().getResourceAsStream(
                        "/" + name);
                FileOutputStream output = new FileOutputStream(actual, false);
                byte[] buf = new byte[1024];
                int length;
                while ((length = input.read(buf)) > 0) {
                    output.write(buf, 0, length);
                }
                output.close();
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
                plugin.getDebugger().debug(Level.WARNING, "Could not write " + name);
            }
        }
    }
}