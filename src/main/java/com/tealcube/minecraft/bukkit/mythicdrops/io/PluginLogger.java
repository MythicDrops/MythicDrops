/**
 * The MIT License
 * Copyright (c) 2013 Teal Cube Games
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.io;

import org.apache.commons.lang3.Validate;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.logging.Level;

/**
 * A class that logs given messages to a file.
 */
public final class PluginLogger {

    private final File file;

    /**
     * Constructs a new PluginLogger with a debug file in the given Plugin's data folder.
     *
     * @param plugin
     *         Plugin owning this PluginLogger
     */
    public PluginLogger(Plugin plugin) {
        this.file = new File(plugin.getDataFolder(), "debug.log");
    }

    /**
     * Constructs a new PluginLogger with the given debug file.
     *
     * @param file
     *         File to log to
     */
    public PluginLogger(File file) {
        this.file = file;
    }

    /**
     * Logs a message with a default {@link java.util.logging.Level} of {@link java.util.logging.Level#INFO}.
     *
     * @param message
     *         message to log
     */
    public void log(String message) {
        log(Level.INFO, message);
    }

    /**
     * Logs a message with a given {@link java.util.logging.Level}.
     *
     * @param level
     *         Level of logging message
     * @param message
     *         message to log
     */
    public void log(Level level, String message) {
        Validate.notNull(level, "level cannot be null");
        Validate.notNull(message, "message cannot be null");
        Validate.notNull(file, "file cannot be null");
        File writeFile = file;
        if (!createFile(writeFile)) {
            return;
        }
        try (FileWriter fileWriter = new FileWriter(writeFile, true);
             PrintWriter writer = new PrintWriter(fileWriter)) {
            writer.println(
                    "[" + level.getName() + "] " + Calendar.getInstance().getTime().toString() + " | " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean createFile(File file) {
        Validate.notNull(file, "file cannot be null");
        boolean succeeded = file.exists();
        if (!succeeded) {
            try {
                if (!createDirectory(file.getParentFile())) {
                    return false;
                }
                succeeded = file.createNewFile();
            } catch (IOException ignored) {
                // do nothing here
            }
        }
        return succeeded;
    }

    private boolean createDirectory(File file) {
        Validate.notNull(file, "file cannot be null");
        return file.exists() || file.mkdirs();
    }

    /**
     * Logs messages with a default {@link java.util.logging.Level} of {@link java.util.logging.Level#INFO}.
     *
     * @param messages
     *         messages to log
     */
    public void log(Iterable<String> messages) {
        log(Level.INFO, messages);
    }

    /**
     * Logs messages with a given {@link java.util.logging.Level}.
     *
     * @param level
     *         Level of logging message
     * @param messages
     *         messages to log
     */
    public void log(Level level, Iterable<String> messages) {
        Validate.notNull(level, "level cannot be null");
        Validate.notNull(messages, "message cannot be null");
        Validate.notNull(file, "file cannot be null");
        File writeFile = file;
        if (!createFile(writeFile)) {
            return;
        }
        try (FileWriter fileWriter = new FileWriter(writeFile, true);
             PrintWriter writer = new PrintWriter(fileWriter)) {
            String time = Calendar.getInstance().getTime().toString();
            for (String message : messages) {
                writer.println("[" + level.getName() + "] " + time + " | " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs messages with a given {@link java.util.logging.Level}.
     *
     * @param level
     *         Level of logging message
     * @param messages
     *         messages to log
     */
    public void log(Level level, String... messages) {
        Validate.notNull(level, "level cannot be null");
        Validate.notNull(messages, "message cannot be null");
        Validate.notNull(file, "file cannot be null");
        File writeFile = file;
        if (!createFile(writeFile)) {
            return;
        }
        try (FileWriter fileWriter = new FileWriter(writeFile, true);
             PrintWriter writer = new PrintWriter(fileWriter)) {
            String time = Calendar.getInstance().getTime().toString();
            for (String message : messages) {
                writer.println("[" + level.getName() + "] " + time + " | " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
