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

import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops;
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.CustomEnchantmentRegistry;
import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager;
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
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.ConfigMigrator;
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.JarConfigMigrator;
import com.tealcube.minecraft.bukkit.mythicdrops.crafting.CraftingListener;
import com.tealcube.minecraft.bukkit.mythicdrops.debug.DebugListener;
import com.tealcube.minecraft.bukkit.mythicdrops.debug.MythicDebugManager;
import com.tealcube.minecraft.bukkit.mythicdrops.enchantments.MythicCustomEnchantmentRegistry;
import com.tealcube.minecraft.bukkit.mythicdrops.errors.MythicLoadingErrorManager;
import com.tealcube.minecraft.bukkit.mythicdrops.identification.IdentificationInventoryDragListener;
import com.tealcube.minecraft.bukkit.mythicdrops.inventories.AnvilListener;
import com.tealcube.minecraft.bukkit.mythicdrops.inventories.GrindstoneListener;
import com.tealcube.minecraft.bukkit.mythicdrops.io.SmartTextFile;
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicCustomItem;
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicCustomItemManager;
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicItemGroup;
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicItemGroupManager;
import com.tealcube.minecraft.bukkit.mythicdrops.items.builders.MythicDropBuilder;
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
import com.tealcube.minecraft.bukkit.mythicdrops.spawning.ItemDroppingListener;
import com.tealcube.minecraft.bukkit.mythicdrops.spawning.ItemSpawningListener;
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.MythicTier;
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.MythicTierManager;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.EnchantmentUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.FileUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.MinecraftVersionUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.worldguard.WorldGuardFlags;
import com.tealcube.minecraft.spigot.worldguard.adapters.lib.WorldGuardAdapters;
import io.papermc.lib.PaperLib;
import io.pixeloutlaw.minecraft.spigot.config.SmartYamlConfiguration;
import io.pixeloutlaw.mythicdrops.mythicdrops.BuildConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
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
      name = "mythicdrops.command.combiners.open",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops combiners open\" command."),
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
      name = "mythicdrops.command.customcreate",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops customcreate\" command."),
  @Permission(
      name = "mythicdrops.command.customitems",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops customitems\" command."),
  @Permission(
      name = "mythicdrops.command.debug",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops debug\" command."),
  @Permission(
      name = "mythicdrops.command.errors",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops errors\" command."),
  @Permission(
      name = "mythicdrops.command.toggledebug",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops toggledebug\" command."),
  @Permission(
      name = "mythicdrops.command.drop.custom",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops drop custom\" command."),
  @Permission(
      name = "mythicdrops.command.drop.gem",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops drop gem\" command."),
  @Permission(
      name = "mythicdrops.command.drop.tier",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops drop tier\" command."),
  @Permission(
      name = "mythicdrops.command.drop.tome",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops drop tome\" command."),
  @Permission(
      name = "mythicdrops.command.drop.unidentified",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops drop unidentified\" command."),
  @Permission(
      name = "mythicdrops.command.drop.*",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use all \"/mythicdrops drop\" commands.",
      children = {
        @ChildPermission(name = "mythicdrops.command.drop.custom"),
        @ChildPermission(name = "mythicdrops.command.drop.gem"),
        @ChildPermission(name = "mythicdrops.command.drop.tier"),
        @ChildPermission(name = "mythicdrops.command.drop.tome"),
        @ChildPermission(name = "mythicdrops.command.drop.unidentified")
      }),
  @Permission(
      name = "mythicdrops.command.give.custom",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops give custom\" command."),
  @Permission(
      name = "mythicdrops.command.give.gem",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops give gem\" command."),
  @Permission(
      name = "mythicdrops.command.give.tier",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops give tier\" command."),
  @Permission(
      name = "mythicdrops.command.give.tome",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops give tome\" command."),
  @Permission(
      name = "mythicdrops.command.give.unidentified",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops give unidentified\" command."),
  @Permission(
      name = "mythicdrops.command.give.*",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use all \"/mythicdrops give\" commands.",
      children = {
        @ChildPermission(name = "mythicdrops.command.give.custom"),
        @ChildPermission(name = "mythicdrops.command.give.gem"),
        @ChildPermission(name = "mythicdrops.command.give.tier"),
        @ChildPermission(name = "mythicdrops.command.give.tome"),
        @ChildPermission(name = "mythicdrops.command.give.unidentified")
      }),
  @Permission(
      name = "mythicdrops.command.itemgroups",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops itemgroups\" command."),
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
      name = "mythicdrops.command.load",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to reload configuration files."),
  @Permission(
      name = "mythicdrops.command.socketgems",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops socketgems\" command."),
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
      name = "mythicdrops.command.tiers",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use \"/mythicdrops tiers\" command."),
  @Permission(
      name = "mythicdrops.command.*",
      defaultValue = PermissionDefault.OP,
      desc = "Allows player to use all commands.",
      children = {
        @ChildPermission(name = "mythicdrops.command.combiners.*"),
        @ChildPermission(name = "mythicdrops.command.customcreate"),
        @ChildPermission(name = "mythicdrops.command.customitems"),
        @ChildPermission(name = "mythicdrops.command.debug"),
        @ChildPermission(name = "mythicdrops.command.errors"),
        @ChildPermission(name = "mythicdrops.command.toggledebug"),
        @ChildPermission(name = "mythicdrops.command.drop.*"),
        @ChildPermission(name = "mythicdrops.command.give.*"),
        @ChildPermission(name = "mythicdrops.command.itemgroups"),
        @ChildPermission(name = "mythicdrops.command.modify.*"),
        @ChildPermission(name = "mythicdrops.command.load"),
        @ChildPermission(name = "mythicdrops.command.socketgems"),
        @ChildPermission(name = "mythicdrops.command.spawn.*"),
        @ChildPermission(name = "mythicdrops.command.tiers.*"),
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
  private LoadingErrorManager loadingErrorManager;
  private AuraRunnable auraRunnable;
  private BukkitTask auraTask;
  private Random random;
  private Handler logHandler;
  private JarConfigMigrator jarConfigMigrator;
  private CustomEnchantmentRegistry customEnchantmentRegistry;

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

  @NotNull
  @Override
  public LoadingErrorManager getLoadingErrorManager() {
    return loadingErrorManager;
  }

  @NotNull
  @Override
  public CustomEnchantmentRegistry getCustomEnchantmentRegistry() {
    return customEnchantmentRegistry;
  }

  @Override
  public void reloadSettings() {
    reloadStartupSettings();
    loadingErrorManager.clear();
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
      if (!itemGroupYAML.isConfigurationSection(key)) {
        continue;
      }
      ConfigurationSection itemGroupCs = itemGroupYAML.getConfigurationSection(key);
      if (itemGroupCs == null) {
        continue;
      }
      itemGroupManager.add(MythicItemGroup.fromConfigurationSection(itemGroupCs, key));
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
        loadingErrorManager.add(
            String.format("Unable to load socket gem combiner with id=%s", key));
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
        LOGGER.fine(
            String.format(
                "Error when loading custom item (%s): material is equivalent to AIR: %s",
                key, cs.get(String.format("%s.material", key))));
        loadingErrorManager.add(
            String.format(
                "Error when loading custom item (%s): material is equivalent to AIR: %s",
                key, cs.get(String.format("%s.material", key))));
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
    NameMap.INSTANCE.clear();
    loadPrefixes();
    loadSuffixes();
    loadLore();
    loadMobNames();
    LOGGER.fine("Loaded name keys: " + NameMap.INSTANCE.getJoinedKeys());
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
    if (!tierDirectory.exists() && tierDirectory.mkdirs()) {
      writeTierFiles(tierDirectory);
    }
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
      Material mat =
          ConfigurationSectionExtensionsKt.getMaterial(cs, "material-name", Material.AIR);
      if (mat == Material.AIR) {
        loadingErrorManager.add(
            String.format(
                "Not loading repair item \"%s\" as it has an invalid material name", key));
        continue;
      }
      String itemName = cs.getString("item-name");
      List<String> itemLore = cs.getStringList("item-lore");
      List<MythicRepairCost> costList = new ArrayList<>();
      ConfigurationSection costsSection = cs.getConfigurationSection("costs");
      for (String costKey : costsSection.getKeys(false)) {
        if (!costsSection.isConfigurationSection(costKey)) {
          continue;
        }
        ConfigurationSection costSection = costsSection.getConfigurationSection(costKey);
        Material itemCost =
            ConfigurationSectionExtensionsKt.getMaterial(
                costSection, "material-name", Material.AIR);
        if (itemCost == Material.AIR) {
          loadingErrorManager.add(
              String.format(
                  "Not loading repair cost \"%s:%s\" as it has an invalid material name",
                  key, costKey));
          continue;
        }
        int experienceCost = costSection.getInt("experience-cost", 0);
        int priority = costSection.getInt("priority", 0);
        int amount = costSection.getInt("amount", 1);
        double repairPerCost = costSection.getDouble("repair-per-cost", 0.1);
        String costName = costSection.getString("item-name");
        List<String> costLore = null;
        if (costSection.isList("item-lore")) {
          costLore = costSection.getStringList("item-lore");
        }
        Map<Enchantment, Integer> enchantments = null;
        ConfigurationSection enchantmentsSection =
            costSection.getConfigurationSection("enchantments");
        if (enchantmentsSection != null) {
          enchantments = new HashMap<>();
          for (String enchantmentKey : enchantmentsSection.getKeys(false)) {
            Enchantment enchantment = EnchantmentUtil.INSTANCE.getByKeyOrName(enchantmentKey);
            if (enchantment != null) {
              enchantments.put(enchantment, enchantmentsSection.getInt(enchantmentKey));
            }
          }
        }

        MythicRepairCost rc =
            new MythicRepairCost(
                costLore,
                costName,
                itemCost,
                amount,
                repairPerCost,
                experienceCost,
                priority,
                costKey,
                enchantments);
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
        loadingErrorManager.add(
            String.format("Not loading %s as there is already a tier with that name loaded", key));
        continue;
      }
      Tier tier =
          MythicTier.fromConfigurationSection(c, key, itemGroupManager, loadingErrorManager);
      if (tier == null) {
        LOGGER.info("tier == null, key=" + key);
        continue;
      }
      Tier preExistingTier =
          tierManager.getWithColors(tier.getDisplayColor(), tier.getIdentifierColor());
      if (preExistingTier != null) {
        String message =
            String.format(
                "Not loading %s as there is already a tier with that display color and identifier color loaded: %s",
                key, preExistingTier.getName());
        LOGGER.info(message);
        loadingErrorManager.add(message);
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
      auraTask =
          auraRunnable.runTaskTimer(
              this,
              20L,
              20L * settingsManager.getSocketingSettings().getOptions().getAuraRefreshInSeconds());
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

    socketGemCacheManager.clear();
    socketGemManager.clear();
    itemGroupManager.clear();
    socketGemCombinerManager.clear();
    repairItemManager.clear();
    customItemManager.clear();
    relationManager.clear();
    tierManager.clear();
    loadingErrorManager.clear();

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
    WorldGuardAdapters.getInstance().registerFlag(WorldGuardFlags.mythicDrops);
    WorldGuardAdapters.getInstance().registerFlag(WorldGuardFlags.mythicDropsCustom);
    WorldGuardAdapters.getInstance().registerFlag(WorldGuardFlags.mythicDropsIdentityTome);
    WorldGuardAdapters.getInstance().registerFlag(WorldGuardFlags.mythicDropsSocketGem);
    WorldGuardAdapters.getInstance().registerFlag(WorldGuardFlags.mythicDropsTiered);
    WorldGuardAdapters.getInstance().registerFlag(WorldGuardFlags.mythicDropsUnidentifiedItem);
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

    customEnchantmentRegistry = new MythicCustomEnchantmentRegistry(this);
    customEnchantmentRegistry.registerEnchantments();

    jarConfigMigrator =
        new JarConfigMigrator(
            getFile(),
            getDataFolder(),
            ConfigMigrator.Companion.getDefaultMoshi(),
            settingsManager.getStartupSettings().isBackupOnConfigMigrate());

    LOGGER.fine("Loading configuration files...");
    reloadConfigurationFiles();

    writeResourceFiles();

    socketGemCacheManager = new MythicSocketGemCacheManager();
    socketGemManager = new MythicSocketGemManager();
    itemGroupManager = new MythicItemGroupManager();
    socketGemCombinerManager = new MythicSocketGemCombinerManager();
    repairItemManager = new MythicRepairItemManager();
    customItemManager = new MythicCustomItemManager();
    relationManager = new MythicRelationManager();
    tierManager = new MythicTierManager();
    loadingErrorManager = new MythicLoadingErrorManager();

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

    Bukkit.getPluginManager().registerEvents(new DebugListener(MythicDebugManager.INSTANCE), this);
    Bukkit.getPluginManager().registerEvents(new AnvilListener(settingsManager), this);
    Bukkit.getPluginManager().registerEvents(new CraftingListener(settingsManager), this);
    if (MinecraftVersionUtil.INSTANCE.hasGrindstoneInventory()) {
      Bukkit.getPluginManager().registerEvents(new GrindstoneListener(settingsManager), this);
    }

    MythicDropsPluginExtensionsKt.setupCommands(this);

    if (settingsManager.getConfigSettings().getComponents().isCreatureSpawningEnabled()) {
      getLogger().info("Mobs spawning with equipment enabled");
      LOGGER.info("Mobs spawning with equipment enabled");
      Bukkit.getPluginManager().registerEvents(new ItemDroppingListener(this), this);
      Bukkit.getPluginManager().registerEvents(new ItemSpawningListener(this), this);
    }
    if (settingsManager.getConfigSettings().getComponents().isRepairingEnabled()) {
      getLogger().info("Repairing enabled");
      LOGGER.info("Repairing enabled");
      Bukkit.getPluginManager()
          .registerEvents(new RepairingListener(repairItemManager, settingsManager), this);
    }
    if (settingsManager.getConfigSettings().getComponents().isSocketingEnabled()) {
      getLogger().info("Socketing enabled");
      LOGGER.info("Socketing enabled");
      Bukkit.getPluginManager()
          .registerEvents(
              new SocketInventoryDragListener(itemGroupManager, settingsManager, socketGemManager),
              this);
      Bukkit.getPluginManager()
          .registerEvents(
              new SocketEffectListener(this, socketGemCacheManager, settingsManager), this);
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
                  itemGroupManager, relationManager, settingsManager, tierManager),
              this);
    }

    PaperLib.suggestPaper(this);
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
    settingsManager
        .getStartupSettings()
        .getLoggingLevels()
        .forEach(
            (logger, level) -> {
              Logger.getLogger(logger).setLevel(level);
            });
  }

  private void writeTierFiles(File tiersFolder) {
    List<String> tiers =
        Arrays.asList(
            "/tiers/artisan.yml",
            "/tiers/common.yml",
            "/tiers/epic.yml",
            "/tiers/exotic.yml",
            "/tiers/legendary.yml",
            "/tiers/rare.yml",
            "/tiers/README.txt",
            "/tiers/uncommon.yml");
    for (String tier : tiers) {
      File actual = new File(getDataFolder(), tier);
      File parentFile = actual.getParentFile();
      if (!parentFile.exists() && !parentFile.mkdirs() || actual.exists()) {
        continue;
      }
      try (BufferedSource source =
              Okio.buffer(Okio.source(this.getClass().getResourceAsStream(tier)));
          BufferedSink sink = Okio.buffer(Okio.sink(actual))) {
        for (String lineInSource; (lineInSource = source.readUtf8Line()) != null; ) {
          sink.writeUtf8(lineInSource).writeUtf8("\n");
        }
      } catch (Exception ex) {
        LOGGER.log(Level.SEVERE, String.format("Unable to write tier file! tier=%s", tier), ex);
      }
    }
  }

  private void writeResourceFiles() {
    List<String> resources =
        Arrays.asList(
            "/resources/lore/general.txt",
            "/resources/lore/enchantments/damage_all.txt",
            "/resources/lore/materials/diamond_sword.txt",
            "/resources/lore/tiers/legendary.txt",
            "/resources/lore/itemtypes/sword.txt",
            "/resources/prefixes/general.txt",
            "/resources/prefixes/enchantments/damage_all.txt",
            "/resources/prefixes/materials/diamond_sword.txt",
            "/resources/prefixes/tiers/legendary.txt",
            "/resources/prefixes/itemtypes/sword.txt",
            "/resources/suffixes/general.txt",
            "/resources/suffixes/enchantments/damage_all.txt",
            "/resources/suffixes/materials/diamond_sword.txt",
            "/resources/suffixes/tiers/legendary.txt",
            "/resources/suffixes/itemtypes/sword.txt",
            "/resources/mobnames/general.txt");
    for (String resource : resources) {
      File actual = new File(getDataFolder(), resource);
      File parentFile = actual.getParentFile();
      if (parentFile.exists() || !parentFile.exists() && !parentFile.mkdirs() || actual.exists()) {
        continue;
      }
      try (BufferedSource source =
              Okio.buffer(Okio.source(this.getClass().getResourceAsStream(resource)));
          BufferedSink sink = Okio.buffer(Okio.sink(actual))) {
        for (String lineInSource; (lineInSource = source.readUtf8Line()) != null; ) {
          sink.writeUtf8(lineInSource).writeUtf8("\n");
        }
      } catch (Exception ex) {
        LOGGER.log(
            Level.SEVERE, String.format("Unable to write resource! resource=%s", resource), ex);
      }
    }
  }

  private void loadMobNames() {
    Map<String, List<String>> mobNames = new HashMap<>();

    File mobNameFolder = new File(getDataFolder(), "/resources/mobnames/");
    if (!mobNameFolder.exists() && !mobNameFolder.mkdirs()) {
      return;
    }

    SmartTextFile generalMobNamesText =
        new SmartTextFile(new File(getDataFolder(), "/resources/mobnames/general.txt"));
    List<String> generalMobNames = generalMobNamesText.read();
    mobNames.put(NameType.GENERAL_MOB_NAME.getFormat(), generalMobNames);
    int numOfLoadedMobNames = generalMobNames.size();

    for (String s : mobNameFolder.list()) {
      if (s.endsWith(".txt") && !s.equals("general.txt")) {
        SmartTextFile mobNameText = new SmartTextFile(new File(mobNameFolder, s));
        List<String> nameList = mobNameText.read();
        mobNames.put(
            NameType.SPECIFIC_MOB_NAME.getFormat() + "." + s.replace(".txt", "").toLowerCase(),
            nameList);
        numOfLoadedMobNames += nameList.size();
      }
    }

    LOGGER.info("Loaded mob names: " + numOfLoadedMobNames);
    NameMap.INSTANCE.putAll(mobNames);
  }

  private void loadLore() {
    Map<String, List<String>> lore = new HashMap<>();

    File loreFolder = new File(getDataFolder(), "/resources/lore/");
    if (!loreFolder.exists() && !loreFolder.mkdirs()) {
      return;
    }

    SmartTextFile generalLoreText =
        new SmartTextFile(new File(getDataFolder(), "/resources/lore/general.txt"));
    List<String> generalLore = generalLoreText.read();
    lore.put(NameType.GENERAL_LORE.getFormat(), generalLore);

    int numOfLoadedLore = generalLore.size();

    File tierLoreFolder = new File(loreFolder, "/tiers/");
    if (tierLoreFolder.exists() && tierLoreFolder.isDirectory()) {
      for (String s : tierLoreFolder.list()) {
        if (s.endsWith(".txt")) {
          SmartTextFile tierLoreText = new SmartTextFile(new File(tierLoreFolder, s));
          List<String> loreList = tierLoreText.read();
          lore.put(NameType.TIER_LORE.getFormat() + s.replace(".txt", "").toLowerCase(), loreList);
          numOfLoadedLore += loreList.size();
        }
      }
    }

    File materialLoreFolder = new File(loreFolder, "/materials/");
    if (materialLoreFolder.exists() && materialLoreFolder.isDirectory()) {
      for (String s : materialLoreFolder.list()) {
        if (s.endsWith(".txt")) {
          SmartTextFile materialLoreText = new SmartTextFile(new File(materialLoreFolder, s));
          List<String> loreList = materialLoreText.read();
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
          SmartTextFile enchantmentLoreText = new SmartTextFile(new File(enchantmentLoreFolder, s));
          List<String> loreList = enchantmentLoreText.read();
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
          SmartTextFile itemTypeLoreText = new SmartTextFile(new File(itemTypeLoreFolder, s));
          List<String> loreList = itemTypeLoreText.read();
          lore.put(
              NameType.ITEMTYPE_LORE.getFormat() + s.replace(".txt", "").toLowerCase(), loreList);
          numOfLoadedLore += loreList.size();
        }
      }
    }

    LOGGER.info("Loaded lore: " + numOfLoadedLore);
    NameMap.INSTANCE.putAll(lore);
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
    NameMap.INSTANCE.putAll(suffixes);
  }

  private void loadPrefixes() {
    Map<String, List<String>> prefixes = new HashMap<>();

    File prefixFolder = new File(getDataFolder(), "/resources/prefixes/");
    if (!prefixFolder.exists() && !prefixFolder.mkdirs()) {
      return;
    }

    SmartTextFile generalPrefixText =
        new SmartTextFile(new File(getDataFolder(), "/resources/prefixes/general.txt"));
    List<String> generalPrefixes = generalPrefixText.read();
    prefixes.put(NameType.GENERAL_PREFIX.getFormat(), generalPrefixes);

    int numOfLoadedPrefixes = generalPrefixes.size();

    File tierPrefixFolder = new File(prefixFolder, "/tiers/");
    if (tierPrefixFolder.exists() && tierPrefixFolder.isDirectory()) {
      for (String s : tierPrefixFolder.list()) {
        if (s.endsWith(".txt")) {
          SmartTextFile tierPrefixText = new SmartTextFile(new File(tierPrefixFolder, s));
          List<String> prefixList = tierPrefixText.read();
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
          SmartTextFile materialPrefixText = new SmartTextFile(new File(materialPrefixFolder, s));
          List<String> prefixList = materialPrefixText.read();
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
          SmartTextFile enchantmentPrefixText =
              new SmartTextFile(new File(enchantmentPrefixFolder, s));
          List<String> prefixList = enchantmentPrefixText.read();
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
          SmartTextFile itemTypePrefixText = new SmartTextFile(new File(itemTypePrefixFolder, s));
          List<String> prefixList = itemTypePrefixText.read();
          prefixes.put(
              NameType.ITEMTYPE_PREFIX.getFormat() + s.replace(".txt", "").toLowerCase(),
              prefixList);
          numOfLoadedPrefixes += prefixList.size();
        }
      }
    }

    LOGGER.info("Loaded prefixes: " + numOfLoadedPrefixes);
    NameMap.INSTANCE.putAll(prefixes);
  }
}
