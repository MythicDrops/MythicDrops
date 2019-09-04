/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2019 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops;

import com.modcrafting.diablodrops.name.NamesLoader;
import com.tealcube.minecraft.bukkit.mythicdrops.anvil.AnvilListener;
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItemManager;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.builders.DropBuilder;
import com.tealcube.minecraft.bukkit.mythicdrops.api.names.NameType;
import com.tealcube.minecraft.bukkit.mythicdrops.api.relations.RelationManager;
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairItem;
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairItemManager;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.GemTriggerType;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGemManager;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.cache.SocketGemCacheManager;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerGuiFactory;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerManager;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager;
import com.tealcube.minecraft.bukkit.mythicdrops.aura.AuraRunnable;
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.JarConfigMigrator;
import com.tealcube.minecraft.bukkit.mythicdrops.crafting.CraftingListener;
import com.tealcube.minecraft.bukkit.mythicdrops.identification.IdentificationInventoryDragListener;
import com.tealcube.minecraft.bukkit.mythicdrops.io.SmartTextFile;
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicCustomItem;
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicCustomItemManager;
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicDropBuilder;
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicItemGroup;
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicItemGroupManager;
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory;
import com.tealcube.minecraft.bukkit.mythicdrops.names.NameMap;
import com.tealcube.minecraft.bukkit.mythicdrops.relations.MythicRelation;
import com.tealcube.minecraft.bukkit.mythicdrops.relations.MythicRelationManager;
import com.tealcube.minecraft.bukkit.mythicdrops.repair.MythicRepairCost;
import com.tealcube.minecraft.bukkit.mythicdrops.repair.MythicRepairItem;
import com.tealcube.minecraft.bukkit.mythicdrops.repair.MythicRepairItemManager;
import com.tealcube.minecraft.bukkit.mythicdrops.repair.RepairingListener;
import com.tealcube.minecraft.bukkit.mythicdrops.settings.MythicSettingsManager;
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.MythicSocketGem;
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.MythicSocketGemManager;
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.SocketEffectListener;
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.SocketGemCombinerListener;
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.SocketInventoryDragListener;
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.cache.MythicSocketGemCacheManager;
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.cache.SocketGemCacheListener;
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.combiners.MythicSocketGemCombiner;
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.combiners.MythicSocketGemCombinerGuiFactory;
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.combiners.MythicSocketGemCombinerManager;
import com.tealcube.minecraft.bukkit.mythicdrops.spawning.ItemSpawningListener;
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.MythicTier;
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.MythicTierManager;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.DefaultItemGroups;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.FileUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.worldguard.WorldGuardUtilWrapper;
import io.pixeloutlaw.minecraft.spigot.config.SmartYamlConfiguration;
import io.pixeloutlaw.mythicdrops.mythicdrops.BuildConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.SoftDependency;
import org.bukkit.plugin.java.annotation.dependency.SoftDependsOn;
import org.bukkit.plugin.java.annotation.permission.ChildPermission;
import org.bukkit.plugin.java.annotation.permission.Permission;
import org.bukkit.plugin.java.annotation.permission.Permissions;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.LoadOrder;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.bukkit.plugin.java.annotation.plugin.author.Authors;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

@Plugin(name = BuildConfig.NAME, version = BuildConfig.VERSION)
@Authors({@Author("ToppleTheNun"), @Author("pur3p0w3r")})
@LoadOrder(PluginLoadOrder.STARTUP)
@ApiVersion(ApiVersion.Target.v1_13)
@SoftDependsOn({@SoftDependency("WorldGuard")})
@Permissions({
  @Permission(
      name = "mythicdrops.identify",
      defaultValue = PermissionDefault.TRUE,
      desc = "Allows a player to identify items."),
  @Permission(
      name = "mythicdrops.socket",
      defaultValue = PermissionDefault.TRUE,
      desc = "Allows a player to use socket gems."),
  @Permission(
      name = "mythicdrops.repair",
      defaultValue = PermissionDefault.TRUE,
      desc = "Allows a player to repair items."),
  @Permission(
      name = "mythicdrops.command.spawn.custom",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops spawn custom\" command."),
  @Permission(
      name = "mythicdrops.command.spawn.gem",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops spawn gem\" command."),
  @Permission(
      name = "mythicdrops.command.spawn.tier",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops spawn tier\" command."),
  @Permission(
      name = "mythicdrops.command.spawn.tome",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops spawn tome\" command."),
  @Permission(
      name = "mythicdrops.command.spawn.unidentified",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops spawn unidentified\" command."),
  @Permission(
      name = "mythicdrops.command.spawn.*",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use all \"/mythicdrops spawn\" commands.",
      children = {
        @ChildPermission(name = "mythicdrops.command.spawn.custom"),
        @ChildPermission(name = "mythicdrops.command.spawn.gem"),
        @ChildPermission(name = "mythicdrops.command.spawn.tier"),
        @ChildPermission(name = "mythicdrops.command.spawn.tome"),
        @ChildPermission(name = "mythicdrops.command.spawn.unidentified")
      }),
  @Permission(
      name = "mythicdrops.command.give",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops give\" command."),
  @Permission(
      name = "mythicdrops.command.give.wildcard",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to give any tier."),
  @Permission(
      name = "mythicdrops.command.load",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to reload configuration files."),
  @Permission(
      name = "mythicdrops.command.unidentified",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops unidentified\" command."),
  @Permission(
      name = "mythicdrops.command.identitytome",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops tome\" command."),
  @Permission(
      name = "mythicdrops.command.customcreate",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops customcreate\" command."),
  @Permission(
      name = "mythicdrops.command.custom",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops custom\" command."),
  @Permission(
      name = "mythicdrops.command.gem",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops gem\" command."),
  @Permission(
      name = "mythicdrops.command.tiers",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops tiers\" command."),
  @Permission(
      name = "mythicdrops.command.modify.name",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops modify name\" command."),
  @Permission(
      name = "mythicdrops.command.modify.lore.add",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops modify lore add\" command."),
  @Permission(
      name = "mythicdrops.command.modify.lore.remove",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops modify lore remove\" command."),
  @Permission(
      name = "mythicdrops.command.modify.lore.insert",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops modify lore insert\" command."),
  @Permission(
      name = "mythicdrops.command.modify.lore.set",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops modify lore set\" command."),
  @Permission(
      name = "mythicdrops.command.modify.lore.*",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops modify lore\" commands.",
      children = {
        @ChildPermission(name = "mythicdrops.command.modify.lore.add"),
        @ChildPermission(name = "mythicdrops.command.modify.lore.remove"),
        @ChildPermission(name = "mythicdrops.command.modify.lore.insert"),
        @ChildPermission(name = "mythicdrops.command.modify.lore.set")
      }),
  @Permission(
      name = "mythicdrops.command.modify.enchantment.add",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops modify enchantment add\" command."),
  @Permission(
      name = "mythicdrops.command.modify.enchantment.remove",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops modify enchantment remove\" command."),
  @Permission(
      name = "mythicdrops.command.modify.enchantment.*",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops modify enchantment\" commands.",
      children = {
        @ChildPermission(name = "mythicdrops.command.modify.enchantment.add"),
        @ChildPermission(name = "mythicdrops.command.modify.enchantment.remove")
      }),
  @Permission(
      name = "mythicdrops.command.modify.*",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops modify\" commands.",
      children = {
        @ChildPermission(name = "mythicdrops.command.modify.name"),
        @ChildPermission(name = "mythicdrops.command.modify.lore.*"),
        @ChildPermission(name = "mythicdrops.command.modify.enchantment.*")
      }),
  @Permission(
      name = "mythicdrops.command.combiners.list",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops combiners list\" command."),
  @Permission(
      name = "mythicdrops.command.combiners.add",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops combiners add\" command."),
  @Permission(
      name = "mythicdrops.command.combiners.remove",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops combiners remove\" command."),
  @Permission(
      name = "mythicdrops.command.combiners.*",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use all \"/mythicdrops combiners\" commands.",
      children = {
        @ChildPermission(name = "mythicdrops.command.combiners.list"),
        @ChildPermission(name = "mythicdrops.command.combiners.add"),
        @ChildPermission(name = "mythicdrops.command.combiners.remove")
      }),
  @Permission(
      name = "mythicdrops.command.*",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use all commands.",
      children = {
        @ChildPermission(name = "mythicdrops.command.spawn"),
        @ChildPermission(name = "mythicdrops.command.spawn.wildcard"),
        @ChildPermission(name = "mythicdrops.command.give"),
        @ChildPermission(name = "mythicdrops.command.give.wildcard"),
        @ChildPermission(name = "mythicdrops.command.load"),
        @ChildPermission(name = "mythicdrops.command.unidentified"),
        @ChildPermission(name = "mythicdrops.command.identitytome"),
        @ChildPermission(name = "mythicdrops.command.customcreate"),
        @ChildPermission(name = "mythicdrops.command.custom"),
        @ChildPermission(name = "mythicdrops.command.gem"),
        @ChildPermission(name = "mythicdrops.command.tiers"),
        @ChildPermission(name = "mythicdrops.command.bug"),
        @ChildPermission(name = "mythicdrops.command.modify.*"),
        @ChildPermission(name = "mythicdrops.command.combiners.*")
      }),
  @Permission(
      name = "mythicdrops.*",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to do all MythicDrops tasks.",
      children = {
        @ChildPermission(name = "mythicdrops.identify"),
        @ChildPermission(name = "mythicdrops.socket"),
        @ChildPermission(name = "mythicdrops.repair"),
        @ChildPermission(name = "mythicdrops.command.*")
      })
})
public final class MythicDropsPlugin extends JavaPlugin implements MythicDrops {

  private static final Logger LOGGER = JulLoggerFactory.INSTANCE.getLogger(MythicDropsPlugin.class);

  private static MythicDropsPlugin _INSTANCE = null;
  private SmartYamlConfiguration configYAML;
  private SmartYamlConfiguration creatureSpawningYAML;
  private SmartYamlConfiguration customItemYAML;
  private SmartYamlConfiguration itemGroupYAML;
  private SmartYamlConfiguration languageYAML;
  private List<SmartYamlConfiguration> tierYAMLs;
  private SmartYamlConfiguration repairingYAML;
  private SmartYamlConfiguration socketGemsYAML;
  private SmartYamlConfiguration socketingYAML;
  private SmartYamlConfiguration identifyingYAML;
  private SmartYamlConfiguration relationYAML;
  private SmartYamlConfiguration repairCostsYAML;
  private SmartYamlConfiguration socketGemCombinersYAML;
  private SmartYamlConfiguration startupYAML;
  private ItemGroupManager itemGroupManager;
  private SocketGemCacheManager socketGemCacheManager;
  private SocketGemManager socketGemManager;
  private SocketGemCombinerManager socketGemCombinerManager;
  private SocketGemCombinerGuiFactory socketGemCombinerGuiFactory;
  private MythicSettingsManager settingsManager;
  private RepairItemManager repairItemManager;
  private CustomItemManager customItemManager;
  private RelationManager relationManager;
  private TierManager tierManager;
  private NamesLoader namesLoader;
  private AuraRunnable auraRunnable;
  private BukkitTask auraTask;
  private Random random;
  private Handler logHandler;
  private JarConfigMigrator jarConfigMigrator;

  public static DropBuilder getNewDropBuilder() {
    return new MythicDropBuilder(getInstance());
  }

  public static MythicDropsPlugin getInstance() {
    return _INSTANCE;
  }

  @NotNull
  @Override
  public SmartYamlConfiguration getStartupYAML() {
    return startupYAML;
  }

  @NotNull
  @Override
  public SmartYamlConfiguration getSocketGemCombinersYAML() {
    return socketGemCombinersYAML;
  }

  @NotNull
  @Override
  public SmartYamlConfiguration getCreatureSpawningYAML() {
    return creatureSpawningYAML;
  }

  @NotNull
  @Override
  public SmartYamlConfiguration getConfigYAML() {
    return configYAML;
  }

  @NotNull
  @Override
  public SmartYamlConfiguration getCustomItemYAML() {
    return customItemYAML;
  }

  @Override
  public SmartYamlConfiguration getItemGroupYAML() {
    return itemGroupYAML;
  }

  @Override
  public SmartYamlConfiguration getLanguageYAML() {
    return languageYAML;
  }

  @Override
  public SmartYamlConfiguration getSocketGemsYAML() {
    return socketGemsYAML;
  }

  @Override
  public SmartYamlConfiguration getSocketingYAML() {
    return socketingYAML;
  }

  @Override
  public SmartYamlConfiguration getRepairingYAML() {
    return repairingYAML;
  }

  @Override
  public SmartYamlConfiguration getRepairCostsYAML() {
    return repairCostsYAML;
  }

  @Override
  public SmartYamlConfiguration getIdentifyingYAML() {
    return identifyingYAML;
  }

  @Override
  public SmartYamlConfiguration getRelationYAML() {
    return relationYAML;
  }

  @NotNull
  @Override
  public ItemGroupManager getItemGroupManager() {
    return itemGroupManager;
  }

  @NotNull
  @Override
  public SocketGemCacheManager getSocketGemCacheManager() {
    return socketGemCacheManager;
  }

  @NotNull
  @Override
  public SocketGemManager getSocketGemManager() {
    return socketGemManager;
  }

  @NotNull
  @Override
  public SocketGemCombinerManager getSocketGemCombinerManager() {
    return socketGemCombinerManager;
  }

  @NotNull
  @Override
  public SocketGemCombinerGuiFactory getSocketGemCombinerGuiFactory() {
    return socketGemCombinerGuiFactory;
  }

  @NotNull
  @Override
  public SettingsManager getSettingsManager() {
    return settingsManager;
  }

  @NotNull
  @Override
  public RepairItemManager getRepairItemManager() {
    return repairItemManager;
  }

  @NotNull
  @Override
  public CustomItemManager getCustomItemManager() {
    return customItemManager;
  }

  @NotNull
  @Override
  public RelationManager getRelationManager() {
    return relationManager;
  }

  @NotNull
  @Override
  public TierManager getTierManager() {
    return tierManager;
  }

  @Override
  public void reloadSettings() {
    reloadStartupSettings();
    configYAML.load();
    settingsManager.loadConfigSettingsFromConfiguration(configYAML);
    languageYAML.load();
    settingsManager.loadLanguageSettingsFromConfiguration(languageYAML);
    creatureSpawningYAML.load();
    settingsManager.loadCreatureSpawningSettingsFromConfiguration(creatureSpawningYAML);
    repairingYAML.load();
    settingsManager.loadRepairingSettingsFromConfiguration(repairingYAML);
    socketingYAML.load();
    settingsManager.loadSocketingSettingsFromConfiguration(socketingYAML);
    identifyingYAML.load();
    settingsManager.loadIdentifyingSettingsFromConfiguration(identifyingYAML);
  }

  @Override
  public void reloadItemGroups() {
    LOGGER.fine("Loading item groups");
    itemGroupManager.clear();
    for (String key : itemGroupYAML.getKeys(false)) {
      if (itemGroupYAML.isConfigurationSection(key)
          || key.equals("version")
          || !itemGroupYAML.isList(key)) {
        continue;
      }
      itemGroupManager.add(MythicItemGroup.fromConfigurationSection(itemGroupYAML, key));
    }
    LOGGER.fine("Loaded item groups: " + itemGroupManager.get().size());
  }

  @Override
  public void reloadRelations() {
    LOGGER.fine("Loading relations");
    relationManager.clear();
    relationYAML.load();
    for (String key : relationYAML.getKeys(false)) {
      if (relationYAML.isConfigurationSection(key)
          || key.equals("version")
          || !relationYAML.isList(key)) {
        continue;
      }
      relationManager.add(MythicRelation.fromConfigurationSection(relationYAML, key));
    }
    LOGGER.fine("Loaded relations: " + relationManager.get().size());
  }

  @Override
  public void reloadSocketGemCombiners() {
    LOGGER.fine("Loading socket gem combiners");
    socketGemCombinerManager.clear();
    socketGemCombinersYAML.load();
    for (String key : socketGemCombinersYAML.getKeys(false)) {
      if (!socketGemCombinersYAML.isConfigurationSection(key)) {
        continue;
      }
      try {
        socketGemCombinerManager.add(
            MythicSocketGemCombiner.fromConfigurationSection(
                socketGemCombinersYAML.getConfigurationSection(key), key));
      } catch (IllegalArgumentException ex) {
        LOGGER.log(
            Level.SEVERE, String.format("Unable to load socket gem combiner with id=%s", key), ex);
      }
    }
    LOGGER.fine("Loaded socket gem combiners: " + socketGemCombinerManager.get().size());
  }

  @Override
  public void saveSocketGemCombiners() {
    socketGemCombinersYAML
        .getKeys(false)
        .forEach(
            key -> {
              socketGemCombinersYAML.set(key, null);
            });
    socketGemCombinerManager
        .get()
        .forEach(
            socketGemCombiner -> {
              String key = socketGemCombiner.getUuid().toString();
              socketGemCombinersYAML.set(
                  String.format("%s.world", key),
                  socketGemCombiner.getLocation().getWorld().getName());
              socketGemCombinersYAML.set(
                  String.format("%s.x", key), socketGemCombiner.getLocation().getX());
              socketGemCombinersYAML.set(
                  String.format("%s.y", key), socketGemCombiner.getLocation().getY());
              socketGemCombinersYAML.set(
                  String.format("%s.z", key), socketGemCombiner.getLocation().getZ());
            });
    socketGemCombinersYAML.save();
  }

  @Override
  public void reloadTiers() {
    LOGGER.fine("Loading tiers");
    tierManager.clear();
    List<String> loadedTierNames = new ArrayList<>();

    if (tierYAMLs != null && !tierYAMLs.isEmpty()) {
      LOGGER.info("Loading tiers from /tiers/");
      getLogger().info("Loading tiers from /tiers/");
      loadedTierNames.addAll(loadTiersFromTierYAMLs());
    } else {
      getLogger()
          .warning(
              "Unable to find/load any tiers. If this is not expected behavior, please alert ToppleTheNun.");
      LOGGER.warning(
          "Unable to find/load any tiers. If this is not expected behavior, please alert ToppleTheNun.");
    }

    LOGGER.info("Loaded tiers: " + loadedTierNames.toString());
  }

  @Override
  public void reloadCustomItems() {
    LOGGER.fine("Loading custom items");
    customItemManager.clear();
    YamlConfiguration c = customItemYAML;
    if (c == null) {
      return;
    }
    for (String key : c.getKeys(false)) {
      if (!c.isConfigurationSection(key)) {
        continue;
      }
      ConfigurationSection cs = c.getConfigurationSection(key);
      CustomItem ci = MythicCustomItem.fromConfigurationSection(cs, key);
      if (ci.getMaterial() == Material.AIR) {
        getLogger()
            .info(
                String.format("Error when loading custom item (%s): materialName is not set", key));
        LOGGER.fine(
            String.format("Error when loading custom item (%s): materialName is not set", key));
        continue;
      }
      customItemManager.add(ci);
      LOGGER.fine("adding " + ci.getName() + " to customItemManager");
    }
    List<String> loadedCustomItemNames =
        customItemManager.get().stream().map(CustomItem::getName).collect(Collectors.toList());
    LOGGER.info("Loaded custom items: " + loadedCustomItemNames.toString());
  }

  @Override
  public void reloadNames() {
    NameMap.getInstance().clear();
    loadPrefixes();
    loadSuffixes();
    loadLore();
    loadMobNames();
  }

  @Override
  public Random getRandom() {
    return random;
  }

  @Override
  public List<SmartYamlConfiguration> getTierYAMLs() {
    return tierYAMLs;
  }

  @Override
  public void reloadConfigurationFiles() {
    LOGGER.fine("ENTRY");
    if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
      getLogger().severe("Unable to create data folder.");
      Bukkit.getPluginManager().disablePlugin(this);
      LOGGER.warning("Unable to create data folder");
      LOGGER.fine("EXIT");
      return;
    }

    FileUtil.INSTANCE.renameFile(
        new File(getDataFolder(), "socketting.yml"), "socketting_RENAMED_TO_socketing.yml.backup");

    jarConfigMigrator.writeYamlFromResourcesIfNotExists("config.yml");
    jarConfigMigrator.writeYamlFromResourcesIfNotExists("creatureSpawning.yml");
    jarConfigMigrator.writeYamlFromResourcesIfNotExists("customItems.yml");
    jarConfigMigrator.writeYamlFromResourcesIfNotExists("language.yml");
    jarConfigMigrator.writeYamlFromResourcesIfNotExists("identifying.yml");
    jarConfigMigrator.writeYamlFromResourcesIfNotExists("itemGroups.yml");
    jarConfigMigrator.writeYamlFromResourcesIfNotExists("relation.yml");
    jarConfigMigrator.writeYamlFromResourcesIfNotExists("repairing.yml");
    jarConfigMigrator.writeYamlFromResourcesIfNotExists("repairCosts.yml");
    jarConfigMigrator.writeYamlFromResourcesIfNotExists("socketing.yml");
    jarConfigMigrator.writeYamlFromResourcesIfNotExists("socketGems.yml");
    jarConfigMigrator.migrate();

    configYAML = new SmartYamlConfiguration(new File(getDataFolder(), "config.yml"));
    creatureSpawningYAML =
        new SmartYamlConfiguration(new File(getDataFolder(), "creatureSpawning.yml"));
    customItemYAML = new SmartYamlConfiguration(new File(getDataFolder(), "customItems.yml"));
    identifyingYAML = new SmartYamlConfiguration(new File(getDataFolder(), "identifying.yml"));
    itemGroupYAML = new SmartYamlConfiguration(new File(getDataFolder(), "itemGroups.yml"));
    languageYAML = new SmartYamlConfiguration(new File(getDataFolder(), "language.yml"));
    relationYAML = new SmartYamlConfiguration(new File(getDataFolder(), "relation.yml"));
    repairingYAML = new SmartYamlConfiguration(new File(getDataFolder(), "repairing.yml"));
    repairCostsYAML = new SmartYamlConfiguration(new File(getDataFolder(), "repairCosts.yml"));
    socketGemCombinersYAML =
        new SmartYamlConfiguration(new File(getDataFolder(), "socketGemCombiners.yml"));
    socketingYAML = new SmartYamlConfiguration(new File(getDataFolder(), "socketing.yml"));
    socketGemsYAML = new SmartYamlConfiguration(new File(getDataFolder(), "socketGems.yml"));

    tierYAMLs = new ArrayList<>();
    File tierDirectory = new File(getDataFolder(), "/tiers/");
    if (tierDirectory.exists() && tierDirectory.isDirectory() || tierDirectory.mkdirs()) {
      for (String s : tierDirectory.list()) {
        if (!s.endsWith(".yml")) {
          continue;
        }
        SmartYamlConfiguration iyc = new SmartYamlConfiguration(new File(tierDirectory, s));
        tierYAMLs.add(iyc);
      }
    }

    LOGGER.fine("EXIT");
  }

  @Override
  public void reloadRepairCosts() {
    YamlConfiguration c = repairCostsYAML;
    if (c == null) {
      return;
    }
    LOGGER.info("Loading repair items");
    repairItemManager.clear();
    ConfigurationSection costs = c;
    if (costs == null) {
      return;
    }
    for (String key : costs.getKeys(false)) {
      if (!costs.isConfigurationSection(key)) {
        continue;
      }
      ConfigurationSection cs = costs.getConfigurationSection(key);
      Material mat = Material.getMaterial(cs.getString("material-name"));
      String itemName = cs.getString("item-name");
      List<String> itemLore = cs.getStringList("item-lore");
      List<MythicRepairCost> costList = new ArrayList<>();
      ConfigurationSection costsSection = cs.getConfigurationSection("costs");
      for (String costKey : costsSection.getKeys(false)) {
        if (!costsSection.isConfigurationSection(costKey)) {
          continue;
        }
        ConfigurationSection costSection = costsSection.getConfigurationSection(costKey);
        Material itemCost = Material.getMaterial(costSection.getString("material-name"));
        int experienceCost = costSection.getInt("experience-cost", 0);
        int priority = costSection.getInt("priority", 0);
        int amount = costSection.getInt("amount", 1);
        double repairPerCost = costSection.getDouble("repair-per-cost", 0.1);
        String costName = costSection.getString("item-name");
        List<String> costLore = costSection.getStringList("item-lore");

        MythicRepairCost rc =
            new MythicRepairCost(
                costLore,
                costName,
                itemCost,
                amount,
                repairPerCost,
                experienceCost,
                priority,
                costKey);
        costList.add(rc);
      }

      RepairItem ri =
          new MythicRepairItem(key, mat, itemName, itemLore)
              .addRepairCosts(costList.toArray(new MythicRepairCost[costList.size()]));

      repairItemManager.add(ri);
    }
    LOGGER.info("Loaded repair items: " + repairItemManager.get().size());
  }

  private List<String> loadTiersFromTierYAMLs() {
    List<String> list = new ArrayList<>();
    for (SmartYamlConfiguration c : tierYAMLs) {
      if (c == null) {
        continue;
      }
      c.load();
      LOGGER.fine("Loading tier from " + c.getFileName());
      String key = c.getFileName().replace(".yml", "");
      if (tierManager.contains(key)) {
        LOGGER.info("Not loading " + key + " as there is already a tier with that name loaded");
        continue;
      }
      Tier tier = MythicTier.fromConfigurationSection(c, key, itemGroupManager);
      if (tier == null) {
        LOGGER.info("tier == null, key=" + key);
        continue;
      }
      if (tierManager.hasWithSameColors(tier.getDisplayColor(), tier.getDisplayColor())) {
        getLogger()
            .info(
                "Not loading "
                    + key
                    + " as there is already a tier with that display color and identifier color loaded");
        LOGGER.info(
            "Not loading "
                + key
                + " as there is already a tier with that display color and identifier color loaded");
        continue;
      }
      tierManager.add(tier);
      list.add(key);
    }
    return list;
  }

  @Override
  public void reloadSocketGems() {
    LOGGER.info("Loading socket gems");
    socketGemManager.clear();
    List<String> loadedSocketGems = new ArrayList<>();
    if (!socketGemsYAML.isConfigurationSection("socket-gems")) {
      return;
    }
    boolean startAuraRunnable = false;
    ConfigurationSection cs = socketGemsYAML.getConfigurationSection("socket-gems");
    for (String key : cs.getKeys(false)) {
      if (!cs.isConfigurationSection(key)) {
        continue;
      }
      SocketGem socketGem =
          MythicSocketGem.fromConfigurationSection(
              cs.getConfigurationSection(key), key, itemGroupManager);
      socketGemManager.add(socketGem);
      loadedSocketGems.add(key);
      startAuraRunnable = startAuraRunnable || socketGem.getGemTriggerType() == GemTriggerType.AURA;
    }
    LOGGER.info("Loaded socket gems: " + loadedSocketGems.toString());

    if (auraTask != null) {
      auraTask.cancel();
    }
    if (startAuraRunnable) {
      auraRunnable = new AuraRunnable(socketGemCacheManager);
      auraTask = auraRunnable.runTaskTimer(this, 20L, 20L * 30);
      getLogger()
          .info(
              "AuraRunnable enabled due to one or more gems detected as using AURA trigger type.");
      LOGGER.info(
          "AuraRunnable enabled due to one or more gems detected as using AURA trigger type.");
    } else {
      getLogger().info("AuraRunnable disabled due to no gems detected as using AURA trigger type.");
      LOGGER.info("AuraRunnable disabled due to no gems detected as using AURA trigger type.");
    }
  }

  @Override
  public void onDisable() {
    HandlerList.unregisterAll(this);
    Bukkit.getScheduler().cancelTasks(this);
    if (logHandler != null) {
      java.util.logging.Logger.getLogger("com.tealcube.minecraft.bukkit.mythicdrops")
          .removeHandler(logHandler);
      java.util.logging.Logger.getLogger("io.pixeloutlaw.minecraft.spigot")
          .removeHandler(logHandler);
      java.util.logging.Logger.getLogger("po.io.pixeloutlaw.minecraft.spigot")
          .removeHandler(logHandler);
    }
  }

  @Override
  public void onLoad() {
    WorldGuardUtilWrapper.INSTANCE.registerFlags();
    settingsManager = new MythicSettingsManager();
    getDataFolder().mkdirs();
    logHandler = MythicDropsPluginExtensionsKt.setupLogHandler(this);
    startupYAML = new SmartYamlConfiguration(new File(getDataFolder(), "startup.yml"));
    reloadStartupSettings();
  }

  @Override
  public void onEnable() {
    _INSTANCE = this;
    random = new Random();

    if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
      getLogger().severe("Unable to create data folder!");
      Bukkit.getPluginManager().disablePlugin(this);
      return;
    }

    jarConfigMigrator = new JarConfigMigrator(getFile(), getDataFolder());
    LOGGER.fine("Found migrations: " + jarConfigMigrator.getConfigMigrationContents().size());

    LOGGER.fine("Loading configuration files...");
    reloadConfigurationFiles();

    namesLoader = new NamesLoader(this);
    writeResourceFiles();

    socketGemCacheManager = new MythicSocketGemCacheManager();
    socketGemManager = new MythicSocketGemManager();
    itemGroupManager = new MythicItemGroupManager();
    socketGemCombinerManager = new MythicSocketGemCombinerManager();
    repairItemManager = new MythicRepairItemManager();
    customItemManager = new MythicCustomItemManager();
    relationManager = new MythicRelationManager();
    tierManager = new MythicTierManager();

    // Going wild here boiz
    reloadSettings();
    reloadItemGroups();
    reloadTiers();
    reloadNames();
    reloadCustomItems();
    reloadRepairCosts();
    reloadSocketGems();
    reloadRelations();
    // SocketGemCombiners need to be loaded after the worlds have been loaded, so run a delayed
    // task:
    getServer()
        .getScheduler()
        .runTask(
            this,
            () -> {
              reloadSocketGemCombiners();
            });

    socketGemCombinerGuiFactory = new MythicSocketGemCombinerGuiFactory(this, settingsManager);

    Bukkit.getPluginManager().registerEvents(new AnvilListener(settingsManager), this);
    Bukkit.getPluginManager().registerEvents(new CraftingListener(settingsManager), this);

    MythicDropsPluginExtensionsKt.setupCommands(this);

    if (settingsManager.getConfigSettings().getComponents().isCreatureSpawningEnabled()) {
      getLogger().info("Mobs spawning with equipment enabled");
      LOGGER.info("Mobs spawning with equipment enabled");
      Bukkit.getPluginManager().registerEvents(new ItemSpawningListener(this), this);
    }
    if (settingsManager.getConfigSettings().getComponents().isRepairingEnabled()) {
      getLogger().info("Repairing enabled");
      LOGGER.info("Repairing enabled");
      Bukkit.getPluginManager()
          .registerEvents(new RepairingListener(repairItemManager, settingsManager), this);
    }
    if (settingsManager.getConfigSettings().getComponents().isSocketingEnabled()) {
      getLogger().info("Socketting enabled");
      LOGGER.info("Socketting enabled");
      Bukkit.getPluginManager()
          .registerEvents(
              new SocketInventoryDragListener(itemGroupManager, settingsManager, socketGemManager),
              this);
      Bukkit.getPluginManager()
          .registerEvents(new SocketEffectListener(socketGemCacheManager, settingsManager), this);
      Bukkit.getPluginManager()
          .registerEvents(new SocketGemCacheListener(socketGemCacheManager), this);
      Bukkit.getPluginManager()
          .registerEvents(
              new SocketGemCombinerListener(socketGemCombinerManager, socketGemCombinerGuiFactory),
              this);
    }
    if (settingsManager.getConfigSettings().getComponents().isIdentifyingEnabled()) {
      getLogger().info("Identifying enabled");
      LOGGER.info("Identifying enabled");
      Bukkit.getPluginManager()
          .registerEvents(
              new IdentificationInventoryDragListener(
                  relationManager, settingsManager, tierManager),
              this);
    }

    MythicDropsPluginExtensionsKt.setupMetrics(this);
    LOGGER.info("v" + getDescription().getVersion() + " enabled");
  }

  public void reloadStartupSettings() {
    startupYAML.load();
    settingsManager.loadStartupSettingsFromConfiguration(startupYAML);
    if (settingsManager.getStartupSettings().getDebug()) {
      Logger.getLogger("com.tealcube.minecraft.bukkit.mythicdrops").setLevel(Level.FINEST);
      Logger.getLogger("io.pixeloutlaw.minecraft.spigot").setLevel(Level.FINEST);
      Logger.getLogger("po.io.pixeloutlaw.minecraft.spigot").setLevel(Level.FINEST);
    } else {
      Logger.getLogger("com.tealcube.minecraft.bukkit.mythicdrops").setLevel(Level.INFO);
      Logger.getLogger("io.pixeloutlaw.minecraft.spigot").setLevel(Level.INFO);
      Logger.getLogger("po.io.pixeloutlaw.minecraft.spigot").setLevel(Level.INFO);
    }
  }

  private void writeResourceFiles() {
    namesLoader.writeDefault("/resources/lore/general.txt", false, true);
    namesLoader.writeDefault("/resources/lore/enchantments/damage_all.txt", false, true);
    namesLoader.writeDefault("/resources/lore/materials/diamond_sword.txt", false, true);
    namesLoader.writeDefault("/resources/lore/tiers/legendary.txt", false, true);
    namesLoader.writeDefault("/resources/lore/itemtypes/sword.txt", false, true);
    namesLoader.writeDefault("/resources/prefixes/general.txt", false, true);
    namesLoader.writeDefault("/resources/prefixes/enchantments/damage_all.txt", false, true);
    namesLoader.writeDefault("/resources/prefixes/materials/diamond_sword.txt", false, true);
    namesLoader.writeDefault("/resources/prefixes/tiers/legendary.txt", false, true);
    namesLoader.writeDefault("/resources/prefixes/itemtypes/sword.txt", false, true);
    namesLoader.writeDefault("/resources/suffixes/general.txt", false, true);
    namesLoader.writeDefault("/resources/suffixes/enchantments/damage_all.txt", false, true);
    namesLoader.writeDefault("/resources/suffixes/materials/diamond_sword.txt", false, true);
    namesLoader.writeDefault("/resources/suffixes/tiers/legendary.txt", false, true);
    namesLoader.writeDefault("/resources/suffixes/itemtypes/sword.txt", false, true);
    namesLoader.writeDefault("/resources/mobnames/general.txt", false, true);
  }

  private void loadMobNames() {
    Map<String, List<String>> mobNames = new HashMap<>();

    File mobNameFolder = new File(getDataFolder(), "/resources/mobnames/");
    if (!mobNameFolder.exists() && !mobNameFolder.mkdirs()) {
      return;
    }

    List<String> generalMobNames = new ArrayList<>();
    namesLoader.loadFile(generalMobNames, "/resources/mobnames/general.txt");
    mobNames.put(NameType.GENERAL_MOB_NAME.getFormat(), generalMobNames);
    int numOfLoadedMobNames = generalMobNames.size();

    for (String s : mobNameFolder.list()) {
      if (s.endsWith(".txt") && !s.equals("general.txt")) {
        List<String> nameList = new ArrayList<>();
        namesLoader.loadFile(nameList, "/resources/mobnames/" + s);
        mobNames.put(
            NameType.SPECIFIC_MOB_NAME.getFormat() + "." + s.replace(".txt", "").toLowerCase(),
            nameList);
        numOfLoadedMobNames += nameList.size();
      }
    }

    LOGGER.info("Loaded mob names: " + numOfLoadedMobNames);
    NameMap.getInstance().putAll(mobNames);
  }

  private void loadLore() {
    Map<String, List<String>> lore = new HashMap<>();

    File loreFolder = new File(getDataFolder(), "/resources/lore/");
    if (!loreFolder.exists() && !loreFolder.mkdirs()) {
      return;
    }

    List<String> generalLore = new ArrayList<>();
    namesLoader.loadFile(generalLore, "/resources/lore/general.txt");
    lore.put(NameType.GENERAL_LORE.getFormat(), generalLore);

    int numOfLoadedLore = generalLore.size();

    File tierLoreFolder = new File(loreFolder, "/tiers/");
    if (tierLoreFolder.exists() && tierLoreFolder.isDirectory()) {
      for (String s : tierLoreFolder.list()) {
        if (s.endsWith(".txt")) {
          List<String> loreList = new ArrayList<>();
          namesLoader.loadFile(loreList, "/resources/lore/tiers/" + s);
          lore.put(NameType.TIER_LORE.getFormat() + s.replace(".txt", "").toLowerCase(), loreList);
          numOfLoadedLore += loreList.size();
        }
      }
    }

    File materialLoreFolder = new File(loreFolder, "/materials/");
    if (materialLoreFolder.exists() && materialLoreFolder.isDirectory()) {
      for (String s : materialLoreFolder.list()) {
        if (s.endsWith(".txt")) {
          List<String> loreList = new ArrayList<>();
          namesLoader.loadFile(loreList, "/resources/lore/materials/" + s);
          lore.put(
              NameType.MATERIAL_LORE.getFormat() + s.replace(".txt", "").toLowerCase(), loreList);
          numOfLoadedLore += loreList.size();
        }
      }
    }

    File enchantmentLoreFolder = new File(loreFolder, "/enchantments/");
    if (enchantmentLoreFolder.exists() && enchantmentLoreFolder.isDirectory()) {
      for (String s : enchantmentLoreFolder.list()) {
        if (s.endsWith(".txt")) {
          List<String> loreList = new ArrayList<>();
          namesLoader.loadFile(loreList, "/resources/lore/enchantments/" + s);
          lore.put(
              NameType.ENCHANTMENT_LORE.getFormat() + s.replace(".txt", "").toLowerCase(),
              loreList);
          numOfLoadedLore += loreList.size();
        }
      }
    }

    File itemTypeLoreFolder = new File(loreFolder, "/itemtypes/");
    if (itemTypeLoreFolder.exists() && itemTypeLoreFolder.isDirectory()) {
      for (String s : itemTypeLoreFolder.list()) {
        if (s.endsWith(".txt")) {
          List<String> loreList = new ArrayList<>();
          namesLoader.loadFile(loreList, "/resources/lore/itemtypes/" + s);
          lore.put(
              NameType.ITEMTYPE_LORE.getFormat() + s.replace(".txt", "").toLowerCase(), loreList);
          numOfLoadedLore += loreList.size();
        }
      }
    }

    LOGGER.info("Loaded lore: " + numOfLoadedLore);
    NameMap.getInstance().putAll(lore);
  }

  private void loadSuffixes() {
    Map<String, List<String>> suffixes = new HashMap<>();

    File suffixFolder = new File(getDataFolder(), "/resources/suffixes/");
    if (!suffixFolder.exists() && !suffixFolder.mkdirs()) {
      return;
    }

    SmartTextFile generalSuffixText =
        new SmartTextFile(new File(getDataFolder(), "/resources/suffixes/general.txt"));
    List<String> generalSuffixes = generalSuffixText.read();
    suffixes.put(NameType.GENERAL_SUFFIX.getFormat(), generalSuffixes);

    int numOfLoadedSuffixes = generalSuffixes.size();

    File tierSuffixFolder = new File(suffixFolder, "/tiers/");
    if (tierSuffixFolder.exists() && tierSuffixFolder.isDirectory()) {
      for (String s : tierSuffixFolder.list()) {
        if (s.endsWith(".txt")) {
          SmartTextFile tierSuffixText = new SmartTextFile(new File(tierSuffixFolder, s));
          List<String> suffixList = tierSuffixText.read();
          suffixes.put(
              NameType.TIER_SUFFIX.getFormat() + s.replace(".txt", "").toLowerCase(), suffixList);
          numOfLoadedSuffixes += suffixList.size();
        }
      }
    }

    File materialSuffixFolder = new File(suffixFolder, "/materials/");
    if (materialSuffixFolder.exists() && materialSuffixFolder.isDirectory()) {
      for (String s : materialSuffixFolder.list()) {
        if (s.endsWith(".txt")) {
          SmartTextFile materialSuffixText = new SmartTextFile(new File(materialSuffixFolder, s));
          List<String> suffixList = materialSuffixText.read();
          suffixes.put(
              NameType.MATERIAL_SUFFIX.getFormat() + s.replace(".txt", "").toLowerCase(),
              suffixList);
          numOfLoadedSuffixes += suffixList.size();
        }
      }
    }

    File enchantmentSuffixFolder = new File(suffixFolder, "/enchantments/");
    if (enchantmentSuffixFolder.exists() && enchantmentSuffixFolder.isDirectory()) {
      for (String s : enchantmentSuffixFolder.list()) {
        if (s.endsWith(".txt")) {
          SmartTextFile enchantmentSuffixText =
              new SmartTextFile(new File(enchantmentSuffixFolder, s));
          List<String> suffixList = enchantmentSuffixText.read();
          suffixes.put(
              NameType.ENCHANTMENT_SUFFIX.getFormat() + s.replace(".txt", "").toLowerCase(),
              suffixList);
          numOfLoadedSuffixes += suffixList.size();
        }
      }
    }

    File itemTypeSuffixFolder = new File(suffixFolder, "/itemtypes/");
    if (itemTypeSuffixFolder.exists() && itemTypeSuffixFolder.isDirectory()) {
      for (String s : itemTypeSuffixFolder.list()) {
        if (s.endsWith(".txt")) {
          SmartTextFile itemtypesSuffixText = new SmartTextFile(new File(itemTypeSuffixFolder, s));
          List<String> suffixList = itemtypesSuffixText.read();
          suffixes.put(
              NameType.ITEMTYPE_SUFFIX.getFormat() + s.replace(".txt", "").toLowerCase(),
              suffixList);
          numOfLoadedSuffixes += suffixList.size();
        }
      }
    }

    LOGGER.info("Loaded suffixes: " + numOfLoadedSuffixes);
    NameMap.getInstance().putAll(suffixes);
  }

  private void loadPrefixes() {
    Map<String, List<String>> prefixes = new HashMap<>();

    File prefixFolder = new File(getDataFolder(), "/resources/prefixes/");
    if (!prefixFolder.exists() && !prefixFolder.mkdirs()) {
      return;
    }

    List<String> generalPrefixes = new ArrayList<>();
    namesLoader.loadFile(generalPrefixes, "/resources/prefixes/general.txt");
    prefixes.put(NameType.GENERAL_PREFIX.getFormat(), generalPrefixes);

    int numOfLoadedPrefixes = generalPrefixes.size();

    File tierPrefixFolder = new File(prefixFolder, "/tiers/");
    if (tierPrefixFolder.exists() && tierPrefixFolder.isDirectory()) {
      for (String s : tierPrefixFolder.list()) {
        if (s.endsWith(".txt")) {
          List<String> prefixList = new ArrayList<>();
          namesLoader.loadFile(prefixList, "/resources/prefixes/tiers/" + s);
          prefixes.put(
              NameType.TIER_PREFIX.getFormat() + s.replace(".txt", "").toLowerCase(), prefixList);
          numOfLoadedPrefixes += prefixList.size();
        }
      }
    }

    File materialPrefixFolder = new File(prefixFolder, "/materials/");
    if (materialPrefixFolder.exists() && materialPrefixFolder.isDirectory()) {
      for (String s : materialPrefixFolder.list()) {
        if (s.endsWith(".txt")) {
          List<String> prefixList = new ArrayList<>();
          namesLoader.loadFile(prefixList, "/resources/prefixes/materials/" + s);
          prefixes.put(
              NameType.MATERIAL_PREFIX.getFormat() + s.replace(".txt", "").toLowerCase(),
              prefixList);
          numOfLoadedPrefixes += prefixList.size();
        }
      }
    }

    File enchantmentPrefixFolder = new File(prefixFolder, "/enchantments/");
    if (enchantmentPrefixFolder.exists() && enchantmentPrefixFolder.isDirectory()) {
      for (String s : enchantmentPrefixFolder.list()) {
        if (s.endsWith(".txt")) {
          List<String> prefixList = new ArrayList<>();
          namesLoader.loadFile(prefixList, "/resources/prefixes/enchantments/" + s);
          prefixes.put(
              NameType.ENCHANTMENT_PREFIX.getFormat() + s.replace(".txt", "").toLowerCase(),
              prefixList);
          numOfLoadedPrefixes += prefixList.size();
        }
      }
    }

    File itemTypePrefixFolder = new File(prefixFolder, "/itemtypes/");
    if (itemTypePrefixFolder.exists() && itemTypePrefixFolder.isDirectory()) {
      for (String s : itemTypePrefixFolder.list()) {
        if (s.endsWith(".txt")) {
          List<String> prefixList = new ArrayList<>();
          namesLoader.loadFile(prefixList, "/resources/prefixes/itemtypes/" + s);
          prefixes.put(
              NameType.ITEMTYPE_PREFIX.getFormat() + s.replace(".txt", "").toLowerCase(),
              prefixList);
          numOfLoadedPrefixes += prefixList.size();
        }
      }
    }

    LOGGER.info("Loaded prefixes: " + numOfLoadedPrefixes);
    NameMap.getInstance().putAll(prefixes);
  }

  private void setupDefaultRepairCosts() {
    for (Material m : DefaultItemGroups.INSTANCE.getWood()) {
      setupRepairingYamlForMaterial(m, Material.OAK_WOOD);
    }
    for (Material m : DefaultItemGroups.INSTANCE.getStone()) {
      setupRepairingYamlForMaterial(m, Material.STONE);
    }
    for (Material m : DefaultItemGroups.INSTANCE.getGold()) {
      setupRepairingYamlForMaterial(m, Material.GOLD_INGOT);
    }
    for (Material m : DefaultItemGroups.INSTANCE.getIron()) {
      setupRepairingYamlForMaterial(m, Material.IRON_INGOT);
    }
    for (Material m : DefaultItemGroups.INSTANCE.getDiamond()) {
      setupRepairingYamlForMaterial(m, Material.DIAMOND);
    }
    for (Material m : DefaultItemGroups.INSTANCE.getLeather()) {
      setupRepairingYamlForMaterial(m, Material.LEATHER);
    }
    for (Material m : DefaultItemGroups.INSTANCE.getChainmail()) {
      setupRepairingYamlForMaterial(m, Material.IRON_BARS);
    }
    repairingYAML.save();
  }

  private void setupRepairingYamlForMaterial(Material m, Material diamond) {
    repairingYAML.set(
        "repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name", m.name());
    repairingYAML.set(
        "repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.material-name",
        diamond.name());
    repairingYAML.set(
        "repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority", 0);
    repairingYAML.set(
        "repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount", 1);
    repairingYAML.set(
        "repair-costs."
            + m.name().toLowerCase().replace("_", "-")
            + ".costs.default.experience-cost",
        0);
    repairingYAML.set(
        "repair-costs."
            + m.name().toLowerCase().replace("_", "-")
            + ".costs.default.repair-per-cost",
        0.1);
  }
}
