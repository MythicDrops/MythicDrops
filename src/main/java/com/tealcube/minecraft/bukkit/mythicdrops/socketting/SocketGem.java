package com.tealcube.minecraft.bukkit.mythicdrops.socketting;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby
 * granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER
 * IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.GemType;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketEffect;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.enchantments.Enchantment;

import java.util.List;
import java.util.Map;

public final class SocketGem {

    private final String name;
    private final GemType gemType;
    private final List<SocketEffect> socketEffects;
    private final double chance;
    private final String prefix;
    private final String suffix;
    private final List<String> lore;
    private final Map<Enchantment, Integer> enchantments;
    private final List<SocketCommand> commands;
    private final String description;

    public SocketGem(String name, GemType gemType, List<SocketEffect> socketEffects, double chance,
                     String prefix, String suffix, List<String> lore,
                     Map<Enchantment, Integer> enchantments, List<SocketCommand> commands) {
        this(name, gemType, socketEffects, chance, prefix, suffix, lore, enchantments, commands,
             "");
    }

    public SocketGem(
            String name, GemType gemType, List<SocketEffect> socketEffects, double chance,
            String prefix,
            String suffix, List<String> lore, Map<Enchantment, Integer> enchantments,
            List<SocketCommand> commands, String description) {
        this.name = name;
        this.gemType = gemType;
        this.socketEffects = socketEffects;
        this.chance = chance;
        this.prefix = prefix;
        this.suffix = suffix;
        this.lore = lore;
        this.enchantments = enchantments;
        this.commands = commands;
        this.description = description;
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
        return WordUtils.capitalize(getGemType().getName());
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

    public String getDescription() {
        return description;
    }

}
