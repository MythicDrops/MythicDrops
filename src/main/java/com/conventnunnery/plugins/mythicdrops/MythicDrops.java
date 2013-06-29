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

import com.conventnunnery.libraries.debug.Debugger;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class MythicDrops extends JavaPlugin {

    // Instantiate a new instance of the Debugger class
    private final Debugger debugger = new Debugger(this);

    @Override
    public void onEnable() {
        // Prints a debug message that the plugin is enabled
        debugger.debug(Level.INFO, "Plugin enabled");
    }

    @Override
    public void onDisable() {
        // Prints a debug message that the plugin is disabled
        debugger.debug(Level.INFO, "Plugin disabled");
    }
}
