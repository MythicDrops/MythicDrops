package com.tealcube.minecraft.bukkit.mythicdrops.api;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


import com.tealcube.minecraft.bukkit.config.SmartYamlConfiguration;
import com.tealcube.minecraft.bukkit.config.VersionedSmartYamlConfiguration;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.ConfigSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.CreatureSpawningSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.IdentifyingSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.RepairingSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SockettingSettings;

import se.ranzdo.bukkit.methodcommand.CommandHandler;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public interface MythicDrops {

    VersionedSmartYamlConfiguration getCreatureSpawningYAML();

    void debug(Level level, String... messages);

    ConfigSettings getConfigSettings();

    CreatureSpawningSettings getCreatureSpawningSettings();

    RepairingSettings getRepairingSettings();

    SockettingSettings getSockettingSettings();

    IdentifyingSettings getIdentifyingSettings();

    VersionedSmartYamlConfiguration getConfigYAML();

    VersionedSmartYamlConfiguration getCustomItemYAML();

    VersionedSmartYamlConfiguration getItemGroupYAML();

    VersionedSmartYamlConfiguration getLanguageYAML();

    VersionedSmartYamlConfiguration getTierYAML();

    VersionedSmartYamlConfiguration getSocketGemsYAML();

    VersionedSmartYamlConfiguration getSockettingYAML();

    VersionedSmartYamlConfiguration getRepairingYAML();

    VersionedSmartYamlConfiguration getIdentifyingYAML();

    void reloadSettings();

    void reloadTiers();

    void reloadCustomItems();

    void reloadNames();

    CommandHandler getCommandHandler();

    Random getRandom();

    List<SmartYamlConfiguration> getTierYAMLs();

    void reloadConfigurationFiles();

    void reloadRepairCosts();

}
