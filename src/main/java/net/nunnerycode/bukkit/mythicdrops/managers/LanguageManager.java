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

package net.nunnerycode.bukkit.mythicdrops.managers;

import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LanguageManager {

    private final MythicDrops plugin;
    private final Map<String, String> messages;

    public LanguageManager(MythicDrops plugin) {
        this.plugin = plugin;
        messages = new LinkedHashMap<String, String>();
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public void sendMessage(CommandSender reciever, String path) {
        String message = getMessage(path);
        if (message == null) {
            return;
        }
        reciever.sendMessage(message);
    }

    public String getMessage(String path) {
        String message = messages.get(path);
        if (message == null) {
            return null;
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public MythicDrops getPlugin() {
        return plugin;
    }

    public void sendMessage(CommandSender reciever, String path,
                            String[][] arguments) {
        String message = getMessage(path, arguments);
        if (message == null) {
            return;
        }
        reciever.sendMessage(message);
    }

    public String getMessage(String path, String[][] arguments) {
        String message = messages.get(path);
        if (message == null) {
            return null;
        }
        message = ChatColor.translateAlternateColorCodes('&', message);
        for (String[] argument : arguments) {
            message = message.replaceAll(argument[0], argument[1]);
        }
        return message;
    }

    public List<String> getStringList(String path) {
        List<String> message = Arrays.asList(messages.get(path).split("^"));
        List<String> strings = new ArrayList<String>();
        for (String s : message) {
            strings.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return strings;
    }

    public List<String> getStringList(String path, String[][] arguments) {
        List<String> message = Arrays.asList(messages.get(path).split("^"));
        List<String> strings = new ArrayList<String>();
        for (String s : message) {
            for (String[] argument : arguments) {
                strings.add(ChatColor.translateAlternateColorCodes('&', s.replace(argument[0], argument[1])));
            }
        }
        return strings;
    }
}