/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.logging;

import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MythicLogger {

    private MythicDrops mythicDrops;
    private Logger logger;
    
    public MythicLogger(MythicDrops mythicDrops, Logger logger) {
        this.mythicDrops = mythicDrops;
        this.logger = logger;
    }
    
    public void debug(String message) {
        if (mythicDrops != null && !mythicDrops.getConfigSettings().isDebugMode()) {
            return;
        }
        logger.log(Level.FINE, message);
    }
    
    public void debug(String message, Object obj) {
        if (mythicDrops != null && !mythicDrops.getConfigSettings().isDebugMode()) {
            return;
        }
        logger.log(Level.FINE, message, obj);
    }

    public void debug(String message, Object[] objs) {
        if (mythicDrops != null && !mythicDrops.getConfigSettings().isDebugMode()) {
            return;
        }
        logger.log(Level.FINE, message, objs);
    }

    public void info(String message) {
        logger.log(Level.INFO, message);
    }

    public void info(String message, Object obj) {
        logger.log(Level.INFO, message, obj);
    }

    public void info(String message, Object[] objs) {
        logger.log(Level.INFO, message, objs);
    }

    public void warn(String message) {
        logger.log(Level.WARNING, message);
    }

    public void warn(String message, Object obj) {
        logger.log(Level.WARNING, message, obj);
    }

    public void warn(String message, Object[] objs) {
        logger.log(Level.WARNING, message, objs);
    }
    
}
