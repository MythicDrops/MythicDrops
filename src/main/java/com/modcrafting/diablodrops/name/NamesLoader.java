package com.modcrafting.diablodrops.name;

import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 *  Originally by deathmarine
 *  Modified by rmh4209/Nunnery/ToppleTheNun on 6/16/14
 *  Modified by rmh4209/Nunnery/ToppleTheNun on 7/19/15
 */
public class NamesLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(NamesLoader.class);
    private MythicDropsPlugin plugin;

    public NamesLoader(final MythicDropsPlugin instance) {
        plugin = instance;
    }

    /**
     * Takes values from a file and adds them to list
     *
     * @param l List of strings to add values
     * @param name Name of the file to take values from
     */
    @Deprecated
    public void loadFile(final List<String> l, final String name) {
        File file = new File(plugin.getDataFolder(), name);
        FileReader fileReader;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            LOGGER.warn("Could not find file " + name);
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
            LOGGER.warn("Could not load file " + name);
        }
    }

    /**
     * Creates a file with given name
     *
     * @param name Name of the file to write
     * @param overwrite If file should overwrite existing files
     */
    @Deprecated
    public void writeDefault(final String name, boolean overwrite) {
        writeDefault(name, overwrite, false);
    }

    @Deprecated
    public void writeDefault(final String name, boolean overwrite, boolean onlyIfNew) {
        File actual = new File(plugin.getDataFolder(), name);
        if (name.contains(".jar")) {
            actual = new File(plugin.getDataFolder().getParent(), name);
        }
        File parentFile = actual.getParentFile();
        if (parentFile.exists() && onlyIfNew || !parentFile.exists() && !parentFile.mkdirs()) {
            return;
        }
        if (!actual.exists() || overwrite) {
            InputStream input;
            if (name.startsWith("/")) {
                input = this.getClass().getResourceAsStream(name);
            } else {
                input = this.getClass().getResourceAsStream(
                        "/" + name);
            }
            if (input == null) {
                LOGGER.warn("Not an actual file: " + name);
                return;
            }
            FileOutputStream output;
            try {
                output = new FileOutputStream(actual, false);
            } catch (FileNotFoundException e) {
                LOGGER.warn("Could not find file " + name);
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
                LOGGER.warn("Could not write file " + name);
            }
        }
    }
}
