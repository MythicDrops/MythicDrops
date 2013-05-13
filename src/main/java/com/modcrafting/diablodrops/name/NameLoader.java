package com.modcrafting.diablodrops.name;

/*
 * Originally created by deathmarine
 * Modified by Nunnery on March 10, 2013
 */


import com.conventnunnery.plugins.mythicdrops.MythicDrops;

import java.io.*;
import java.util.List;

public class NameLoader {

    private final File dataFolder;
    private final MythicDrops plugin;

    public NameLoader(final MythicDrops instance) {
        plugin = instance;
        dataFolder = instance.getDataFolder();
    }

    /**
     * @return the plugin
     */
    public MythicDrops getPlugin() {
        return plugin;
    }

    public void loadFile(final List<String> l, final String name) throws IOException {
        BufferedReader list = new BufferedReader(new FileReader(new File(
                dataFolder, name)));
        String p;
        while ((p = list.readLine()) != null) {
            if (!p.contains("#") && p.length() > 0) {
                l.add(p);
            }
        }
        list.close();
    }

    /**
     * Creates a file with given name
     *
     * @param name Name of the file to write
     */
    public void writeDefault(final String name, boolean overwrite) throws IOException {
        File actual = new File(dataFolder, name);
        if (name.contains(".jar")) {
            actual = new File(dataFolder.getParent(), name);
        }
        if (!actual.exists() || overwrite) {
            if (!actual.getParentFile().exists() && !actual.getParentFile().mkdirs()) {
                return;
            }
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
        }
    }

}
