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

package net.nunnerycode.bukkit.mythicdrops.sockets;

import net.nunnerycode.bukkit.mythicdrops.api.sockets.GemType;
import net.nunnerycode.bukkit.mythicdrops.api.sockets.SocketAction;
import net.nunnerycode.bukkit.mythicdrops.api.sockets.SocketCommand;
import net.nunnerycode.bukkit.mythicdrops.api.sockets.SocketEffect;
import org.bukkit.enchantments.Enchantment;

import java.util.List;
import java.util.Map;

public class SocketGem {

    private final String name;
    private final GemType gemType;
    private final List<SocketEffect> socketEffects;
    private final double chance;
    private final String prefix;
    private final String suffix;
    private final List<String> lore;
    private final Map<Enchantment, Integer> enchantments;
    private final List<SocketCommand> commands;
    private final List<SocketAction> actions;

    public SocketGem(String name, GemType gemType,
                     List<SocketEffect> socketEffects, double chance, String prefix, String suffix,
                     List<String> lore, Map<Enchantment, Integer> enchantments, List<SocketCommand> commands,
                     final List<SocketAction> actions) {
        this.name = name;
        this.gemType = gemType;
        this.socketEffects = socketEffects;
        this.chance = chance;
        this.prefix = prefix;
        this.suffix = suffix;
        this.lore = lore;
        this.enchantments = enchantments;
        this.commands = commands;
        this.actions = actions;
    }

    public List<SocketAction> getActions() {
        return actions;
    }

    public List<SocketCommand> getCommands() {
        return commands;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public String getName() {
        return name;
    }

    public String getPresentableType() {
        String name = getGemType().getName();
        return name.substring(0, 1).toUpperCase() + name.substring(1, name.length()).toLowerCase();
    }

    public GemType getGemType() {
        return gemType;
    }

    public List<SocketEffect> getSocketEffects() {
        return socketEffects;
    }

    public double getChance() {
        return chance;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public List<String> getLore() {
        return lore;
    }
}
