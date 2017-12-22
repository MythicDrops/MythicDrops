/**
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
package com.tealcube.minecraft.bukkit.mythicdrops.api;

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.ConfigSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.CreatureSpawningSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.IdentifyingSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.RelationSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.RepairingSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SockettingSettings;
import io.pixeloutlaw.minecraft.spigot.config.SmartYamlConfiguration;
import io.pixeloutlaw.minecraft.spigot.config.VersionedSmartYamlConfiguration;
import java.util.List;
import java.util.Random;
import se.ranzdo.bukkit.methodcommand.CommandHandler;

public interface MythicDrops {

    VersionedSmartYamlConfiguration getCreatureSpawningYAML();

    ConfigSettings getConfigSettings();

    CreatureSpawningSettings getCreatureSpawningSettings();

    RepairingSettings getRepairingSettings();

    SockettingSettings getSockettingSettings();

    IdentifyingSettings getIdentifyingSettings();

    VersionedSmartYamlConfiguration getConfigYAML();

    VersionedSmartYamlConfiguration getCustomItemYAML();

    VersionedSmartYamlConfiguration getItemGroupYAML();

    VersionedSmartYamlConfiguration getLanguageYAML();

    VersionedSmartYamlConfiguration getSocketGemsYAML();

    VersionedSmartYamlConfiguration getSockettingYAML();

    VersionedSmartYamlConfiguration getRepairingYAML();

    VersionedSmartYamlConfiguration getIdentifyingYAML();

    VersionedSmartYamlConfiguration getRelationYAML();

    void reloadSettings();

    void reloadTiers();

    void reloadCustomItems();

    void reloadNames();

    CommandHandler getCommandHandler();

    Random getRandom();

    List<SmartYamlConfiguration> getTierYAMLs();

    void reloadConfigurationFiles();

    void reloadRepairCosts();

    RelationSettings getRelationSettings();
}
