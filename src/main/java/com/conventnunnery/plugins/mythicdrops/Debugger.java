/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.mythicdrops;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class Debugger {

    public final Plugin plugin;
    private final File dataFolder;

    public Debugger(Plugin plugin) {
        this.plugin = plugin;
        this.dataFolder = plugin.getDataFolder();
    }

    public void debug(String... messages) {
        try {
            if (!dataFolder.exists()) {
                boolean mkdirs = dataFolder.mkdirs();
                if (!mkdirs) {
                    return;
                }
            }
            File saveTo = new File(dataFolder, "debug.log");
            if (!saveTo.exists()) {
                boolean newFile = saveTo.createNewFile();
                if (!newFile) {
                    return;
                }
            }
            FileWriter fw = new FileWriter(saveTo.getPath(), true);
            PrintWriter pw = new PrintWriter(fw);
            for (String message : messages) {
                pw.println(Calendar.getInstance().getTime().toString() + " | "
                        + message);
            }
            pw.flush();
            pw.close();
        } catch (IOException e) {
            Bukkit.getLogger().severe(e.getMessage());
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
