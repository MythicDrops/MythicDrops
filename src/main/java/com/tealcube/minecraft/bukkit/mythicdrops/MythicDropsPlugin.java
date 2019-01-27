/*
 * The MIT License
 * Copyright © 2013 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops;

import com.modcrafting.diablodrops.name.NamesLoader;
import com.tealcube.minecraft.bukkit.mythicdrops.anvil.AnvilListener;
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops;
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.builders.DropBuilder;
import com.tealcube.minecraft.bukkit.mythicdrops.api.names.NameType;
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairItem;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.ConfigSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.CreatureSpawningSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.IdentifyingSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.RelationSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.RepairingSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SockettingSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.EffectTarget;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.GemType;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketEffect;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import com.tealcube.minecraft.bukkit.mythicdrops.aura.AuraRunnable;
import com.tealcube.minecraft.bukkit.mythicdrops.commands.MythicDropsCommand;
import com.tealcube.minecraft.bukkit.mythicdrops.crafting.CraftingListener;
import com.tealcube.minecraft.bukkit.mythicdrops.durability.DurabilityListener;
import com.tealcube.minecraft.bukkit.mythicdrops.identification.IdentifyingListener;
import com.tealcube.minecraft.bukkit.mythicdrops.io.SmartTextFile;
import com.tealcube.minecraft.bukkit.mythicdrops.items.CustomItemBuilder;
import com.tealcube.minecraft.bukkit.mythicdrops.items.CustomItemMap;
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicDropBuilder;
import com.tealcube.minecraft.bukkit.mythicdrops.logging.MythicLoggerFactory;
import com.tealcube.minecraft.bukkit.mythicdrops.logging.MythicLoggingFormatter;
import com.tealcube.minecraft.bukkit.mythicdrops.names.NameMap;
import com.tealcube.minecraft.bukkit.mythicdrops.repair.MythicRepairCost;
import com.tealcube.minecraft.bukkit.mythicdrops.repair.MythicRepairItem;
import com.tealcube.minecraft.bukkit.mythicdrops.repair.MythicRepairItemMap;
import com.tealcube.minecraft.bukkit.mythicdrops.repair.RepairingListener;
import com.tealcube.minecraft.bukkit.mythicdrops.settings.MythicConfigSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.settings.MythicCreatureSpawningSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.settings.MythicIdentifyingSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.settings.MythicRelationSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.settings.MythicRepairingSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.settings.MythicSockettingSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.socketting.SocketCommand;
import com.tealcube.minecraft.bukkit.mythicdrops.socketting.SocketGem;
import com.tealcube.minecraft.bukkit.mythicdrops.socketting.SocketParticleEffect;
import com.tealcube.minecraft.bukkit.mythicdrops.socketting.SocketPotionEffect;
import com.tealcube.minecraft.bukkit.mythicdrops.socketting.SockettingListener;
import com.tealcube.minecraft.bukkit.mythicdrops.spawning.ItemSpawningListener;
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.MythicTierBuilder;
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.TierMap;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ChatColorUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TierUtil;
import io.pixeloutlaw.minecraft.spigot.config.SmartYamlConfiguration;
import io.pixeloutlaw.minecraft.spigot.config.VersionedConfiguration;
import io.pixeloutlaw.minecraft.spigot.config.VersionedSmartYamlConfiguration;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import se.ranzdo.bukkit.methodcommand.CommandHandler;

public final class MythicDropsPlugin extends JavaPlugin implements MythicDrops {

  private static final Logger LOGGER = MythicLoggerFactory.getLogger(MythicDropsPlugin.class);

  private static MythicDropsPlugin _INSTANCE = null;
  private ConfigSettings configSettings;
  private CreatureSpawningSettings creatureSpawningSettings;
  private RepairingSettings repairingSettings;
  private SockettingSettings sockettingSettings;
  private IdentifyingSettings identifyingSettings;
  private RelationSettings relationSettings;
  private VersionedSmartYamlConfiguration configYAML;
  private VersionedSmartYamlConfiguration customItemYAML;
  private VersionedSmartYamlConfiguration itemGroupYAML;
  private VersionedSmartYamlConfiguration languageYAML;
  private List<SmartYamlConfiguration> tierYAMLs;
  private VersionedSmartYamlConfiguration creatureSpawningYAML;
  private VersionedSmartYamlConfiguration repairingYAML;
  private VersionedSmartYamlConfiguration socketGemsYAML;
  private VersionedSmartYamlConfiguration sockettingYAML;
  private VersionedSmartYamlConfiguration identifyingYAML;
  private VersionedSmartYamlConfiguration relationYAML;
  private NamesLoader namesLoader;
  private CommandHandler commandHandler;
  private AuraRunnable auraRunnable;
  private BukkitTask auraTask;
  private Random random;
  private Handler logHandler;

  public static DropBuilder getNewDropBuilder() {
    return new MythicDropBuilder(getInstance());
  }

  public static MythicDropsPlugin getInstance() {
    return _INSTANCE;
  }

  @Override
  public VersionedSmartYamlConfiguration getCreatureSpawningYAML() {
    return creatureSpawningYAML;
  }

  @Override
  public ConfigSettings getConfigSettings() {
    return configSettings;
  }

  @Override
  public CreatureSpawningSettings getCreatureSpawningSettings() {
    return creatureSpawningSettings;
  }

  @Override
  public RepairingSettings getRepairingSettings() {
    return repairingSettings;
  }

  @Override
  public SockettingSettings getSockettingSettings() {
    return sockettingSettings;
  }

  @Override
  public IdentifyingSettings getIdentifyingSettings() {
    return identifyingSettings;
  }

  @Override
  public VersionedSmartYamlConfiguration getConfigYAML() {
    return configYAML;
  }

  @Override
  public VersionedSmartYamlConfiguration getCustomItemYAML() {
    return customItemYAML;
  }

  @Override
  public VersionedSmartYamlConfiguration getItemGroupYAML() {
    return itemGroupYAML;
  }

  @Override
  public VersionedSmartYamlConfiguration getLanguageYAML() {
    return languageYAML;
  }

  public VersionedSmartYamlConfiguration getSocketGemsYAML() {
    return socketGemsYAML;
  }

  @Override
  public VersionedSmartYamlConfiguration getSockettingYAML() {
    return sockettingYAML;
  }

  public VersionedSmartYamlConfiguration getRepairingYAML() {
    return repairingYAML;
  }

  @Override
  public VersionedSmartYamlConfiguration getIdentifyingYAML() {
    return identifyingYAML;
  }

  @Override
  public VersionedSmartYamlConfiguration getRelationYAML() {
    return relationYAML;
  }

  @Override
  public void reloadSettings() {
    loadCoreSettings();
    loadCreatureSpawningSettings();
    loadRepairSettings();
    loadSockettingSettings();
    loadSocketGems();
    loadIdentifyingSettings();
    loadRelationSettings();
  }

  @Override
  public void reloadTiers() {
    LOGGER.fine("Loading tiers");
    TierMap.INSTANCE.clear();
    List<String> loadedTierNames = new ArrayList<>();

    if (tierYAMLs != null && !tierYAMLs.isEmpty()) {
      LOGGER.info("Loading tiers from /tiers/");
      getLogger().info("Loading tiers from /tiers/");
      loadedTierNames.addAll(loadTiersFromTierYAMLs());
    } else {
      getLogger()
          .warning("Unable to find/load any tiers. If this is not expected behavior, please alert ToppleTheNun.");
      LOGGER.warning("Unable to find/load any tiers. If this is not expected behavior, please alert ToppleTheNun.");
    }

    LOGGER.info("Loaded tiers: " + loadedTierNames.toString());
  }

  @Override
  public void reloadCustomItems() {
    LOGGER.fine("Loading custom items");
    CustomItemMap.getInstance().clear();
    YamlConfiguration c = customItemYAML;
    if (c == null) {
      return;
    }
    List<String> loadedCustomItemsNames = new ArrayList<>();
    for (String key : c.getKeys(false)) {
      if (!c.isConfigurationSection(key)) {
        continue;
      }
      ConfigurationSection cs = c.getConfigurationSection(key);
      CustomItemBuilder builder = new CustomItemBuilder(key);
      Material material = Material.getMaterial(cs.getString("materialName", "AIR"));
      if (material == null) {
        getLogger().info(String.format("Error when loading custom item (%s): materialName is not valid", key));
        LOGGER.fine("reloadCustomItems - {} - materialName is not valid");
        continue;
      }
      if (material == Material.AIR) {
        getLogger().info(String.format("Error when loading custom item (%s): materialName is not set", key));
        LOGGER.fine("reloadCustomItems - {} - materialName is not set");
        continue;
      }
      List<MythicEnchantment> enchantments = new ArrayList<>();
      if (cs.isConfigurationSection("enchantments")) {
        ConfigurationSection enchantmentsCs = cs.getConfigurationSection("enchantments");
        for (String ench : enchantmentsCs.getKeys(false)) {
          Enchantment enchantment = Enchantment.getByName(ench);
          if (enchantment == null) {
            continue;
          }
          if (!enchantmentsCs.isConfigurationSection(ench)) {
            int level = enchantmentsCs.getInt(ench, 1);
            enchantments.add(new MythicEnchantment(enchantment, level, level));
            continue;
          }
          ConfigurationSection enchCs = enchantmentsCs.getConfigurationSection(ench);
          int minimumLevel = enchCs.getInt("minimumLevel", 1);
          int maximumLevel = enchCs.getInt("maximumLevel", 1);
          enchantments.add(new MythicEnchantment(enchantment, minimumLevel, maximumLevel));
        }
      }
      CustomItem ci = builder.withMaterial(material)
          .withDisplayName(cs.getString("displayName", key))
          .withLore(cs.getStringList("lore"))
          .withChanceToBeGivenToMonster(cs.getDouble("spawnOnMonsterWeight", 0))
          .withChanceToDropOnDeath(cs.getDouble("chanceToDropOnDeath", 0))
          .withEnchantments(enchantments)
          .withBroadcastOnFind(cs.getBoolean("broadcastOnFind", false))
          .hasDurability(cs.contains("durability"))
          .withDurability((short) cs.getInt("durability", 0))
          .withUnbreakable(cs.getBoolean("unbreakable", false))
          .build();
      CustomItemMap.getInstance().put(key, ci);
      loadedCustomItemsNames.add(key);
    }
    LOGGER.info("Loaded custom items: " + loadedCustomItemsNames.toString());
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
  public CommandHandler getCommandHandler() {
    return commandHandler;
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
    LOGGER.fine("reloadConfigurationFiles() - ENTRY");
    if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
      getLogger().severe("Unable to create data folder.");
      Bukkit.getPluginManager().disablePlugin(this);
      LOGGER.warning("Unable to create data folder");
      LOGGER.fine("reloadConfigurationFiles() - EXIT");
      return;
    }

    configYAML = new VersionedSmartYamlConfiguration(new File(getDataFolder(), "config.yml"),
        getResource("config.yml"),
        VersionedConfiguration.VersionUpdateType.BACKUP_AND_UPDATE);
    if (configYAML.update()) {
      LOGGER.info("reloadConfigurationFiles() - Updating config.yml");
      getLogger().info("Updating config.yml");
    } else {
      LOGGER.info("reloadConfigurationFiles() - Not updating config.yml");
    }
    configYAML.load();

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

    if (tierYAMLs.isEmpty()) {
      LOGGER.warning("No tiers are configured");
      getLogger().warning("No tiers are configured");
    }

    customItemYAML =
        new VersionedSmartYamlConfiguration(new File(getDataFolder(), "customItems.yml"),
            getResource("customItems.yml"),
            VersionedConfiguration.VersionUpdateType.BACKUP_AND_UPDATE);
    if (customItemYAML.update()) {
      LOGGER.info("reloadConfigurationFiles() - Updating customItems.yml");
      getLogger().info("Updating customItems.yml");
    } else {
      LOGGER.info("reloadConfigurationFiles() - Not updating customItems.yml");
    }
    customItemYAML.load();

    itemGroupYAML = new VersionedSmartYamlConfiguration(new File(getDataFolder(), "itemGroups.yml"),
        getResource("itemGroups.yml"),
        VersionedConfiguration.VersionUpdateType.BACKUP_AND_UPDATE);
    if (itemGroupYAML.update()) {
      LOGGER.info("reloadConfigurationFiles() - Updating itemGroups.yml");
      getLogger().info("Updating itemGroups.yml");
    } else {
      LOGGER.info("reloadConfigurationFiles() - Not updating itemGroups.yml");
    }
    itemGroupYAML.load();

    languageYAML = new VersionedSmartYamlConfiguration(new File(getDataFolder(), "language.yml"),
        getResource("language.yml"),
        VersionedConfiguration.VersionUpdateType.BACKUP_AND_UPDATE);
    if (languageYAML.update()) {
      LOGGER.info("reloadConfigurationFiles() - Updating language.yml");
      getLogger().info("Updating language.yml");
    } else {
      LOGGER.info("reloadConfigurationFiles() - Not updating language.yml");
    }
    languageYAML.load();

    creatureSpawningYAML =
        new VersionedSmartYamlConfiguration(new File(getDataFolder(), "creatureSpawning.yml"),
            getResource("creatureSpawning.yml"),
            VersionedConfiguration.VersionUpdateType.BACKUP_AND_UPDATE);
    if (creatureSpawningYAML.update()) {
      LOGGER.info("reloadConfigurationFiles() - Updating creatureSpawning.yml");
      getLogger().info("Updating creatureSpawning.yml");
    } else {
      LOGGER.info("reloadConfigurationFiles() - Not updating creatureSpawning.yml");
    }
    creatureSpawningYAML.load();

    repairingYAML = new VersionedSmartYamlConfiguration(new File(getDataFolder(), "repairing.yml"),
        getResource("repairing.yml"),
        VersionedConfiguration.VersionUpdateType.BACKUP_AND_UPDATE);
    if (repairingYAML.update()) {
      LOGGER.info("reloadConfigurationFiles() - Updating repairing.yml");
      getLogger().info("Updating repairing.yml");
    } else {
      LOGGER.info("reloadConfigurationFiles() - Not updating repairing.yml");
    }
    repairingYAML.load();

    socketGemsYAML =
        new VersionedSmartYamlConfiguration(new File(getDataFolder(), "socketGems.yml"),
            getResource("socketGems.yml"),
            VersionedConfiguration.VersionUpdateType.BACKUP_AND_UPDATE);
    if (socketGemsYAML.update()) {
      LOGGER.info("reloadConfigurationFiles() - Updating socketGems.yml");
      getLogger().info("Updating socketGems.yml");
    } else {
      LOGGER.info("reloadConfigurationFiles() - Not updating socketGems.yml");
    }
    socketGemsYAML.load();

    sockettingYAML =
        new VersionedSmartYamlConfiguration(new File(getDataFolder(), "socketting.yml"),
            getResource("socketting.yml"),
            VersionedConfiguration.VersionUpdateType.BACKUP_AND_UPDATE);
    if (sockettingYAML.update()) {
      LOGGER.info("reloadConfigurationFiles() - Updating socketting.yml");
      getLogger().info("Updating socketting.yml");
    } else {
      LOGGER.info("reloadConfigurationFiles() - Not updating socketting.yml");
    }
    sockettingYAML.load();

    identifyingYAML =
        new VersionedSmartYamlConfiguration(new File(getDataFolder(), "identifying.yml"),
            getResource("identifying.yml"),
            VersionedConfiguration.VersionUpdateType.BACKUP_AND_UPDATE);
    if (identifyingYAML.update()) {
      LOGGER.info("reloadConfigurationFiles() - Updating identifying.yml");
      getLogger().info("Updating identifying.yml");
    } else {
      LOGGER.info("reloadConfigurationFiles() - Not updating identifying.yml");
    }
    identifyingYAML.load();

    relationYAML = new VersionedSmartYamlConfiguration(new File(getDataFolder(), "relation.yml"),
        getResource("relation.yml"),
        VersionedConfiguration.VersionUpdateType.BACKUP_AND_UPDATE);
    if (relationYAML.update()) {
      LOGGER.info("reloadConfigurationFiles() - Updating relation.yml");
      getLogger().info("Updating relation.yml");
    } else {
      LOGGER.info("reloadConfigurationFiles() - Not updating relation.yml");
    }

    LOGGER.fine("reloadConfigurationFiles() - EXIT");
  }

  @Override
  public void reloadRepairCosts() {
    YamlConfiguration c = repairingYAML;
    if (c == null) {
      return;
    }
    LOGGER.info("Loading repair items");
    MythicRepairItemMap.getInstance().clear();
    ConfigurationSection costs = c.getConfigurationSection("repair-costs");
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

        MythicRepairCost rc = new MythicRepairCost(
            costKey, priority, experienceCost, repairPerCost, amount, itemCost, costName, costLore);
        costList.add(rc);
      }

      MythicRepairItem ri = new MythicRepairItem(key, mat, itemName, itemLore);
      ri.addRepairCosts(costList.toArray(new MythicRepairCost[costList.size()]));

      MythicRepairItemMap.getInstance().put(ri.getName(), ri);
    }
    Map<String, RepairItem> repairItemMap = MythicRepairItemMap.getInstance();
    LOGGER.info("Loaded repair items: " + repairItemMap.keySet().size());
  }

  @Override
  public RelationSettings getRelationSettings() {
    return relationSettings;
  }

  private List<String> loadTiersFromTierYAMLs() {
    List<String> list = new ArrayList<>();
    for (SmartYamlConfiguration c : tierYAMLs) {
      if (c == null) {
        continue;
      }
      LOGGER.fine("Loading tier from " + c.getFileName());
      String key = c.getFileName().replace(".yml", "");
      if (TierMap.INSTANCE.containsKey(key.toLowerCase())) {
        LOGGER.info("Not loading " + key + " as there is already a tier with that name loaded");
        continue;
      }
      MythicTierBuilder builder = new MythicTierBuilder(key.toLowerCase());
      builder.withDisplayName(c.getString("displayName", key));
      ChatColor displayColor = ChatColorUtil.INSTANCE.getChatColor(c.getString("displayColor"));
      if (displayColor == null) {
        LOGGER.info(c.getString("displayColor") + " is not a valid color");
        continue;
      }
      if (displayColor == ChatColor.WHITE) {
        LOGGER.info(
            displayColor.name() + " doesn't work due to a bug in Spigot, so we're replacing it with RESET instead");
        displayColor = ChatColor.RESET;
      }
      builder.withDisplayColor(displayColor);
      ChatColor identificationColor = ChatColorUtil.INSTANCE.getChatColor(c.getString("identifierColor"));
      if (identificationColor == null) {
        identificationColor = ChatColorUtil.INSTANCE.getChatColor(c.getString("identificationColor"));
        if (identificationColor == null) {
          LOGGER.info(c.getString("identificationColor") + " is not a valid color");
          continue;
        }
      }
      builder.withIdentificationColor(identificationColor);
      if (TierMap.INSTANCE.hasTierWithColors(displayColor, identificationColor)) {
        getLogger().info(
            "Not loading " + key + " as there is already a tier with that display color and identifier color loaded");
        LOGGER.info(
            "Not loading " + key + " as there is already a tier with that display color and identifier color loaded");
        continue;
      }

      ConfigurationSection enchCS = c.getConfigurationSection("enchantments");
      if (enchCS != null) {
        builder.withSafeBaseEnchantments(enchCS.getBoolean("safeBaseEnchantments", true));
        builder.withSafeBonusEnchantments(enchCS.getBoolean("safeBonusEnchantments", true));
        builder.withHighBaseEnchantments(enchCS.getBoolean("allowHighBaseEnchantments", true));
        builder.withHighBonusEnchantments(enchCS.getBoolean("allowHighBonusEnchantments", true));
        builder.withMinimumBonusEnchantments(enchCS.getInt("minimumBonusEnchantments", 0));
        builder.withMaximumBonusEnchantments(enchCS.getInt("maximumBonusEnchantments", 0));

        Set<MythicEnchantment> baseEnchantments = new HashSet<>();
        List<String> baseEnchantStrings = enchCS.getStringList("baseEnchantments");
        for (String s : baseEnchantStrings) {
          MythicEnchantment me = MythicEnchantment.fromString(s);
          if (me != null) {
            baseEnchantments.add(me);
          }
        }
        builder.withBaseEnchantments(baseEnchantments);

        Set<MythicEnchantment> bonusEnchantments = new HashSet<>();
        List<String> bonusEnchantStrings = enchCS.getStringList("bonusEnchantments");
        for (String s : bonusEnchantStrings) {
          MythicEnchantment me = MythicEnchantment.fromString(s);
          if (me != null) {
            bonusEnchantments.add(me);
          }
        }
        builder.withBonusEnchantments(bonusEnchantments);
      }

      ConfigurationSection loreCS = c.getConfigurationSection("lore");
      if (loreCS != null) {
        builder.withMinimumBonusLore(loreCS.getInt("minimumBonusLore", 0));
        builder.withMaximumBonusLore(loreCS.getInt("maximumBonusLore", 0));
        builder.withBaseLore(loreCS.getStringList("baseLore"));
        builder.withBonusLore(loreCS.getStringList("bonusLore"));
      }

      builder.withMinimumDurabilityPercentage(c.getDouble("minimumDurability", 1.0));
      builder.withMaximumDurabilityPercentage(c.getDouble("maximumDurability", 1.0));
      builder.withMinimumSockets(c.getInt("minimumSockets", 0));
      builder.withMaximumSockets(c.getInt("maximumSockets", 0));
      builder.withAllowedItemGroups(c.getStringList("itemTypes.allowedGroups"));
      builder.withDisallowedItemGroups(c.getStringList("itemTypes.disallowedGroups"));
      builder.withAllowedItemIds(c.getStringList("itemTypes.allowedItemIds"));
      builder.withDisallowedItemIds(c.getStringList("itemTypes.disallowedItemIds"));

      builder.withSpawnChance(c.getDouble("chanceToSpawnOnAMonster", 0.0));
      builder.withDropChance(c.getDouble("chanceToDropOnMonsterDeath", 1.0));
      builder.withIdentifyChance(c.getDouble("chanceToBeIdentified", 0.0));

      builder.withChanceToHaveSockets(c.getDouble("chanceToHaveSockets", 1D));
      builder.withBroadcastOnFind(c.getBoolean("broadcastOnFind", false));

      builder.withOptimalDistance(c.getInt("optimalDistance", -1));
      builder.withMaximumDistance(c.getInt("maximumDistance", -1));

      builder.withInfiniteDurability(c.getBoolean("infiniteDurability", false));

      Tier t = builder.build();

      TierMap.INSTANCE.put(key.toLowerCase(), t);
      list.add(key);
    }
    return list;
  }

  @Override
  public void onDisable() {
    HandlerList.unregisterAll(this);
    Bukkit.getScheduler().cancelTasks(this);
    if (logHandler != null) {
      Logger.getLogger("com.tealcube.minecraft.bukkit.mythicdrops").removeHandler(logHandler);
      Logger.getLogger("io.pixeloutlaw.minecraft.spigot").removeHandler(logHandler);
      Logger.getLogger("po.io.pixeloutlaw.minecraft.spigot").removeHandler(logHandler);
    }
  }

  @Override
  public void onEnable() {
    _INSTANCE = this;
    random = new Random();

    namesLoader = new NamesLoader(this);

    if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
      getLogger().severe("Unable to create data folder!");
      Bukkit.getPluginManager().disablePlugin(this);
      return;
    }

    try {
      String pathToLogOutput = String.format("%s/mythicdrops.log", getDataFolder().getAbsolutePath());
      logHandler = new FileHandler(pathToLogOutput, true);
      logHandler.setFormatter(new MythicLoggingFormatter());
      Logger tealCubeLogger = Logger.getLogger("com.tealcube.minecraft.bukkit.mythicdrops");
      tealCubeLogger.setUseParentHandlers(false);
      tealCubeLogger.addHandler(logHandler);
      Logger pixelOutlawLogger = Logger.getLogger("io.pixeloutlaw.minecraft.spigot");
      pixelOutlawLogger.setUseParentHandlers(false);
      pixelOutlawLogger.addHandler(logHandler);
      Logger poPixelOutlawLogger = Logger.getLogger("po.io.pixeloutlaw.minecraft.spigot");
      poPixelOutlawLogger.setUseParentHandlers(false);
      poPixelOutlawLogger.addHandler(logHandler);
      getLogger().info("MythicDrops logging has been setup");
    } catch (Exception e) {
      getLogger().log(Level.SEVERE, "Unable to setup logging for MythicDrops", e);
    }

    LOGGER.fine("Loading configuration files...");
    reloadConfigurationFiles();

    writeResourceFiles();

    // Going wild here boiz
    reloadTiers();
    reloadNames();
    reloadCustomItems();
    reloadRepairCosts();
    reloadSettings();

    Bukkit.getPluginManager().registerEvents(new AnvilListener(this), this);
    Bukkit.getPluginManager().registerEvents(new CraftingListener(this), this);
    Bukkit.getPluginManager().registerEvents(new DurabilityListener(), this);

    commandHandler = new CommandHandler(this);
    commandHandler.registerCommands(new MythicDropsCommand(this));

    if (getConfigSettings().isCreatureSpawningEnabled()) {
      getLogger().info("Mobs spawning with equipment enabled");
      LOGGER.info("Mobs spawning with equipment enabled");
      Bukkit.getPluginManager().registerEvents(new ItemSpawningListener(this), this);
    }
    if (getConfigSettings().isRepairingEnabled()) {
      getLogger().info("Repairing enabled");
      LOGGER.info("Repairing enabled");
      Bukkit.getPluginManager().registerEvents(new RepairingListener(this), this);
    }
    if (getConfigSettings().isSockettingEnabled()) {
      getLogger().info("Socketting enabled");
      LOGGER.info("Socketting enabled");
      Bukkit.getPluginManager().registerEvents(new SockettingListener(this), this);
    }
    if (getConfigSettings().isIdentifyingEnabled()) {
      getLogger().info("Identifying enabled");
      LOGGER.info("Identifying enabled");
      Bukkit.getPluginManager().registerEvents(new IdentifyingListener(this), this);
    }

    LOGGER.info("v" + getDescription().getVersion() + " enabled");
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

  private void loadCoreSettings() {
    MythicConfigSettings mcs = new MythicConfigSettings();

    YamlConfiguration c = configYAML;
    mcs.setDebugMode(c.getBoolean("options.debug", true));
    mcs.setGiveMobsNames(c.getBoolean("options.give-mobs-names", false));
    mcs.setGiveAllMobsNames(c.getBoolean("options.give-all-mobs-names", false));
    mcs.setDisplayMobEquipment(c.getBoolean("options.display-mob-equipment", true));
    mcs.setBlankMobSpawnEnabled(c.getBoolean("options.blank-mob-spawn.enabled", false));
    mcs.setSkeletonsSpawnWithoutBows(c.getBoolean("options.blank-mob-spawn"
        + ".skeletons-spawn-without-bow", false));
    mcs.setAllowRepairingUsingAnvil(c.getBoolean("options.allow-items-to-be-repaired-by-anvil", false));
    mcs.setAllowEquippingItemsViaRightClick(c.getBoolean("options.allow-equipping-items-via-right-click", false));
    mcs.setRandomizeLeatherColors(c.getBoolean("options.randomize-leather-colors", true));
    mcs.setEnabledWorlds(c.getStringList("multiworld.enabled-worlds"));
    mcs.setItemChance(c.getDouble("drops.item-chance", 0.25));
    mcs.setCustomItemChance(c.getDouble("drops.custom-item-chance", 0.1));
    mcs.setSocketGemChance(c.getDouble("drops.socket-gem-chance", 0.2));
    mcs.setIdentityTomeChance(c.getDouble("drops.identity-tome-chance", 0.1));
    mcs.setUnidentifiedItemChance(c.getDouble("drops.unidentified-item-chance", 0.1));
    mcs.setChainItemChance(c.getDouble("drops.chained-item-chance", 0.0));
    mcs.setCreatureSpawningEnabled(c.getBoolean("components.creature-spawning-enabled", true));
    mcs.setSockettingEnabled(c.getBoolean("components.socketting-enabled", true));
    mcs.setRepairingEnabled(c.getBoolean("components.repairing-enabled", true));
    mcs.setIdentifyingEnabled(c.getBoolean("components.identifying-enabled", true));
    mcs.setPopulatingEnabled(c.getBoolean("components.populating-enabled", false));
    mcs.setItemDisplayNameFormat(c.getString("display.item-display-name-format",
        "%generalprefix% %generalsuffix%"));
    mcs.getTooltipFormat().clear();
    mcs.getTooltipFormat().addAll(c.getStringList("display.tooltip-format"));

    c = languageYAML;
    mcs.getLanguageMap().clear();
    for (String key : c.getKeys(true)) {
      if (c.isConfigurationSection(key) || key.equals("version")) {
        continue;
      }
      mcs.getLanguageMap().put(key, c.getString(key, key));
    }

    c = itemGroupYAML;
    if (c.isConfigurationSection("itemGroups")) {
      ConfigurationSection idCS = c.getConfigurationSection("itemGroups");

      if (idCS.isConfigurationSection("toolGroups")) {
        List<String> toolGroupList = new ArrayList<>();
        ConfigurationSection toolCS = idCS.getConfigurationSection("toolGroups");
        for (String toolKind : toolCS.getKeys(false)) {
          List<String> idList = toolCS.getStringList(toolKind);
          toolGroupList.add(toolKind + " (" + idList.size() + ")");
          mcs.getItemTypesWithIds().put(toolKind.toLowerCase(), idList);
          mcs.getToolTypes().add(toolKind.toLowerCase());
        }
        LOGGER.info("Loaded tool groups: " + toolGroupList.toString());
      }
      if (idCS.isConfigurationSection("armorGroups")) {
        List<String> armorGroupList = new ArrayList<>();
        ConfigurationSection armorCS = idCS.getConfigurationSection("armorGroups");
        for (String armorKind : armorCS.getKeys(false)) {
          List<String> idList = armorCS.getStringList(armorKind);
          armorGroupList.add(armorKind + " (" + idList.size() + ")");
          mcs.getItemTypesWithIds().put(armorKind.toLowerCase(), idList);
          mcs.getArmorTypes().add(armorKind.toLowerCase());
        }
        LOGGER.info("Loaded armor groups: " + armorGroupList.toString());
      }
      if (idCS.isConfigurationSection("materialGroups")) {
        List<String> materialGroupList = new ArrayList<>();
        ConfigurationSection materialCS = idCS.getConfigurationSection("materialGroups");
        for (String materialKind : materialCS.getKeys(false)) {
          List<String> idList = materialCS.getStringList(materialKind);
          materialGroupList.add(materialKind + " (" + idList.size() + ")");
          mcs.getMaterialTypesWithIds().put(materialKind.toLowerCase(), idList);
          mcs.getMaterialTypes().add(materialKind.toLowerCase());
        }
        LOGGER.info("Loaded material groups: " + materialGroupList.toString());
      }
    }

    this.configSettings = mcs;
    Logger.getLogger("com.tealcube.minecraft.bukkit.mythicdrops")
        .setLevel(this.configSettings.isDebugMode() ? Level.FINEST : Level.INFO);
  }

  private void loadCreatureSpawningSettings() {
    MythicCreatureSpawningSettings mcss = new MythicCreatureSpawningSettings();
    YamlConfiguration c = creatureSpawningYAML;
    mcss.setPreventSpawner(c.getBoolean("spawnPrevention.spawner", true));
    mcss.setPreventSpawnEgg(c.getBoolean("spawnPrevention.spawnEgg", true));
    mcss.setPreventReinforcements(c.getBoolean("spawnPrevention.reinforcements", true));

    if (c.isConfigurationSection("spawnPrevention.aboveY")) {
      ConfigurationSection cs = c.getConfigurationSection("spawnPrevention.aboveY");
      for (String wn : cs.getKeys(false)) {
        if (cs.isConfigurationSection(wn)) {
          continue;
        }
        mcss.setSpawnHeightLimit(wn, cs.getInt(wn, 255));
      }
    }
    if (c.isConfigurationSection("tierDrops")) {
      ConfigurationSection cs = c.getConfigurationSection("tierDrops");
      for (String key : cs.getKeys(false)) {
        if (cs.isConfigurationSection(key)) {
          continue;
        }
        List<String> strings = cs.getStringList(key);
        EntityType et;
        try {
          et = EntityType.valueOf(key);
        } catch (Exception e) {
          continue;
        }
        Set<Tier> tiers = new HashSet<>(TierUtil.getTiersFromStrings(strings));
        LOGGER.info(et.name() + " | " + TierUtil.getStringsFromTiers(tiers).toString());
        mcss.setEntityTypeTiers(et, tiers);
      }
    }

    if (c.isConfigurationSection("spawnWithDropChance")) {
      ConfigurationSection
          cs =
          c.getConfigurationSection("spawnWithDropChance");
      for (String key : cs.getKeys(false)) {
        if (cs.isConfigurationSection(key)) {
          continue;
        }
        EntityType et;
        try {
          et = EntityType.valueOf(key);
        } catch (Exception e) {
          continue;
        }
        double d = cs.getDouble(key, 0D);
        mcss.setEntityTypeChance(et, d);
      }
    }

    this.creatureSpawningSettings = mcss;
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
          lore.put(NameType.TIER_LORE.getFormat() + s.replace(".txt", "").toLowerCase(),
              loreList);
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
              NameType.MATERIAL_LORE.getFormat() + s.replace(".txt", "").toLowerCase(),
              loreList);
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
              NameType.ITEMTYPE_LORE.getFormat() + s.replace(".txt", "").toLowerCase(),
              loreList);
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

    SmartTextFile generalSuffixText = new SmartTextFile(
        new File(getDataFolder(), "/resources/suffixes/general.txt"));
    List<String> generalSuffixes = generalSuffixText.read();
    suffixes.put(NameType.GENERAL_SUFFIX.getFormat(), generalSuffixes);

    int numOfLoadedSuffixes = generalSuffixes.size();

    File tierSuffixFolder = new File(suffixFolder, "/tiers/");
    if (tierSuffixFolder.exists() && tierSuffixFolder.isDirectory()) {
      for (String s : tierSuffixFolder.list()) {
        if (s.endsWith(".txt")) {
          SmartTextFile tierSuffixText = new SmartTextFile(
              new File(tierSuffixFolder, s));
          List<String> suffixList = tierSuffixText.read();
          suffixes.put(NameType.TIER_SUFFIX.getFormat() + s.replace(".txt", "").toLowerCase(),
              suffixList);
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
          suffixes.put(NameType.MATERIAL_SUFFIX.getFormat() + s.replace(".txt", "").toLowerCase(),
              suffixList);
          numOfLoadedSuffixes += suffixList.size();
        }
      }
    }

    File enchantmentSuffixFolder = new File(suffixFolder, "/enchantments/");
    if (enchantmentSuffixFolder.exists() && enchantmentSuffixFolder.isDirectory()) {
      for (String s : enchantmentSuffixFolder.list()) {
        if (s.endsWith(".txt")) {
          SmartTextFile enchantmentSuffixText = new SmartTextFile(new File(enchantmentSuffixFolder, s));
          List<String> suffixList = enchantmentSuffixText.read();
          suffixes.put(NameType.ENCHANTMENT_SUFFIX.getFormat() + s.replace(".txt", "")
              .toLowerCase(), suffixList);
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
          suffixes.put(NameType.ITEMTYPE_SUFFIX.getFormat() + s.replace(".txt", "").toLowerCase(),
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
          prefixes
              .put(NameType.TIER_PREFIX.getFormat() + s.replace(".txt", "").toLowerCase(),
                  prefixList);
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
          prefixes.put(NameType.ENCHANTMENT_PREFIX.getFormat() + s.replace(".txt", "")
              .toLowerCase(), prefixList);
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

  private void loadRepairSettings() {
    YamlConfiguration c = repairingYAML;
    if (!c.isConfigurationSection("repair-costs")) {
      defaultRepairCosts();
    }
    MythicRepairingSettings mrs = new MythicRepairingSettings();
    mrs.setPlaySounds(c.getBoolean("play-sounds", true));
    mrs.setCancelMcMMORepair(c.getBoolean("cancel-mcmmo-repairs", true));

    repairingSettings = mrs;
  }

  private void defaultRepairCosts() {
    Material[]
        wood =
        {Material.WOODEN_AXE, Material.WOODEN_HOE, Material.WOODEN_PICKAXE, Material.WOODEN_SHOVEL,
            Material.WOODEN_SWORD, Material.BOW, Material.FISHING_ROD};
    Material[]
        stone =
        {Material.STONE_AXE, Material.STONE_PICKAXE, Material.STONE_HOE, Material.STONE_SWORD,
            Material.STONE_SHOVEL};
    Material[]
        leather =
        {Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET,
            Material.LEATHER_LEGGINGS};
    Material[]
        chain =
        {Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET,
            Material.CHAINMAIL_LEGGINGS};
    Material[]
        iron =
        {Material.IRON_AXE, Material.IRON_BOOTS, Material.IRON_CHESTPLATE, Material.IRON_HELMET,
            Material.IRON_LEGGINGS, Material.IRON_PICKAXE, Material.IRON_HOE, Material.IRON_SHOVEL,
            Material.IRON_SWORD};
    Material[] diamond = {Material.DIAMOND_AXE, Material.DIAMOND_BOOTS, Material.DIAMOND_CHESTPLATE,
        Material.DIAMOND_HELMET, Material.DIAMOND_HOE, Material.DIAMOND_LEGGINGS,
        Material.DIAMOND_PICKAXE,
        Material.DIAMOND_SHOVEL, Material.DIAMOND_SWORD};
    Material[]
        gold =
        {Material.GOLDEN_AXE, Material.GOLDEN_BOOTS, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_HELMET,
            Material.GOLDEN_LEGGINGS, Material.GOLDEN_PICKAXE, Material.GOLDEN_HOE, Material.GOLDEN_SHOVEL,
            Material.GOLDEN_SWORD};
    for (Material m : wood) {
      repairingYAML
          .set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name",
              m.name());
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.material-name", Material.OAK_WOOD.name());
      repairingYAML.set(
          "repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority",
          0);
      repairingYAML
          .set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount",
              1);
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.experience-cost", 0);
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.repair-per-cost", 0.1);
    }
    for (Material m : stone) {
      repairingYAML
          .set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name",
              m.name());
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.material-name", Material.STONE.name());
      repairingYAML.set(
          "repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority",
          0);
      repairingYAML
          .set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount",
              1);
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.experience-cost", 0);
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.repair-per-cost", 0.1);
    }
    for (Material m : gold) {
      repairingYAML
          .set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name",
              m.name());
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.material-name", Material.GOLD_INGOT.name());
      repairingYAML.set(
          "repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority",
          0);
      repairingYAML
          .set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount",
              1);
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.experience-cost", 0);
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.repair-per-cost", 0.1);
    }
    for (Material m : iron) {
      repairingYAML
          .set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name",
              m.name());
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.material-name", Material.IRON_INGOT.name());
      repairingYAML.set(
          "repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority",
          0);
      repairingYAML
          .set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount",
              1);
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.experience-cost", 0);
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.repair-per-cost", 0.1);
    }
    for (Material m : diamond) {
      repairingYAML
          .set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name",
              m.name());
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.material-name", Material.DIAMOND.name());
      repairingYAML.set(
          "repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority",
          0);
      repairingYAML
          .set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount",
              1);
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.experience-cost", 0);
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.repair-per-cost", 0.1);
    }
    for (Material m : leather) {
      repairingYAML
          .set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name",
              m.name());
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.material-name", Material.LEATHER.name());
      repairingYAML.set(
          "repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority",
          0);
      repairingYAML
          .set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount",
              1);
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.experience-cost", 0);
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.repair-per-cost", 0.1);
    }
    for (Material m : chain) {
      repairingYAML
          .set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name",
              m.name());
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.material-name", Material.IRON_BARS.name());
      repairingYAML.set(
          "repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority",
          0);
      repairingYAML
          .set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount",
              1);
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.experience-cost", 0);
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
          "-")
          + ".costs.default.repair-per-cost", 0.1);
    }
    repairingYAML.save();
  }

  private void loadSockettingSettings() {
    YamlConfiguration c = sockettingYAML;
    MythicSockettingSettings mss = new MythicSockettingSettings();
    mss.setPreventCraftingWithGems(c.getBoolean("options.prevent-crafting-with-gems", true));
    mss.setCanDropSocketGemsOnItems(c.getBoolean("options.can-drop-socket-gems-on-items", false));
    mss.setUseAttackerItemInHand(c.getBoolean("options.use-attacker-item-in-hand", true));
    mss.setUseAttackerArmorEquipped(c.getBoolean("options.use-attacker-armor-equipped", false));
    mss.setUseDefenderItemInHand(c.getBoolean("options.use-defender-item-in-hand", false));
    mss.setUseDefenderArmorEquipped(c.getBoolean("options.use-defender-armor-equipped", true));
    mss.setPreventMultipleChangesFromSockets(
        c.getBoolean("options.prevent-multiple-changes-from-sockets", true));
    List<String> socketGemMats = c.getStringList("options.socket-gem-material-ids");
    List<Material> socketGemMaterials = new ArrayList<>();
    List<String> loadedSocketGemMats = new ArrayList<>();
    for (String s : socketGemMats) {
      Material mat = Material.getMaterial(s);
      if (mat == null || mat == Material.AIR) {
        continue;
      }
      loadedSocketGemMats.add(mat.toString());
      socketGemMaterials.add(mat);
    }
    mss.setSocketGemMaterials(socketGemMaterials);
    mss.setSocketGemName(c.getString("items.socket-name", "&6Socket Gem - %socketgem%"));
    mss.setSocketGemLore(c.getStringList("items.socket-lore"));
    mss.setSockettedItemString(c.getString("items.socketted-item-socket", "&6(Socket)"));
    mss.setSockettedItemLore(c.getStringList("items.socketted-item-lore"));

    LOGGER.info("Loaded Socket Gems Materials: " + loadedSocketGemMats.toString());

    sockettingSettings = mss;
  }

  private void loadRelationSettings() {
    YamlConfiguration c = relationYAML;
    MythicRelationSettings mrs = new MythicRelationSettings();
    int loadedRelations = 0;
    for (String key : c.getKeys(false)) {
      if (key.equalsIgnoreCase("version")) {
        continue;
      }
      mrs.setLoreFromName(key, c.getStringList(key));
      loadedRelations++;
    }

    LOGGER.info("Loaded relations: " + loadedRelations);

    relationSettings = mrs;
  }

  private List<SocketParticleEffect> buildSocketParticleEffects(ConfigurationSection cs) {
    List<SocketParticleEffect> socketParticleEffectList = new ArrayList<>();
    if (!cs.isConfigurationSection("particle-effects")) {
      return socketParticleEffectList;
    }
    ConfigurationSection cs1 = cs.getConfigurationSection("particle-effects");
    for (String key : cs1.getKeys(false)) {
      Effect pet;
      try {
        pet = Effect.valueOf(key);
      } catch (Exception e) {
        continue;
      }
      int duration = cs1.getInt(key + ".duration");
      int intensity = cs1.getInt(key + ".intensity");
      int radius = cs1.getInt(key + ".radius");
      double chanceToTrigger = cs1.getDouble(key + ".chanceToTrigger", 1D);
      String target = cs1.getString(key + ".target");
      EffectTarget et = EffectTarget.getFromName(target);
      if (et == null) {
        et = EffectTarget.NONE;
      }
      boolean affectsWielder = cs1.getBoolean(key + ".affectsWielder");
      boolean affectsTarget = cs1.getBoolean(key + ".affectsTarget");
      socketParticleEffectList.add(new SocketParticleEffect(pet, intensity, duration, radius, chanceToTrigger, et,
          affectsWielder, affectsTarget));
    }
    return socketParticleEffectList;
  }

  private List<SocketPotionEffect> buildSocketPotionEffects(ConfigurationSection cs) {
    List<SocketPotionEffect> socketPotionEffectList = new ArrayList<>();
    if (!cs.isConfigurationSection("potion-effects")) {
      return socketPotionEffectList;
    }
    ConfigurationSection cs1 = cs.getConfigurationSection("potion-effects");
    for (String key : cs1.getKeys(false)) {
      PotionEffectType pet = PotionEffectType.getByName(key);
      if (pet == null) {
        continue;
      }
      int duration = cs1.getInt(key + ".duration");
      int intensity = cs1.getInt(key + ".intensity");
      int radius = cs1.getInt(key + ".radius");
      double chanceToTrigger = cs1.getDouble(key + ".chanceToTrigger", 1D);
      String target = cs1.getString(key + ".target");
      EffectTarget et = EffectTarget.getFromName(target);
      if (et == null) {
        et = EffectTarget.NONE;
      }
      boolean affectsWielder = cs1.getBoolean(key + ".affectsWielder");
      boolean affectsTarget = cs1.getBoolean(key + ".affectsTarget");
      socketPotionEffectList
          .add(new SocketPotionEffect(pet, intensity, duration, radius, chanceToTrigger, et, affectsWielder,
              affectsTarget));
    }
    return socketPotionEffectList;
  }

  private void loadSocketGems() {
    LOGGER.info("Loading socket gems");
    getSockettingSettings().getSocketGemMap().clear();
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
      ConfigurationSection gemCS = cs.getConfigurationSection(key);
      GemType gemType = GemType.getFromName(gemCS.getString("type"));
      if (gemType == null) {
        gemType = GemType.ANY;
      }

      List<SocketPotionEffect> socketPotionEffects = buildSocketPotionEffects(gemCS);
      List<SocketParticleEffect> socketParticleEffects = buildSocketParticleEffects(gemCS);
      List<SocketEffect> socketEffects = new ArrayList<SocketEffect>(socketPotionEffects);
      socketEffects.addAll(socketParticleEffects);

      for (SocketEffect se : socketEffects) {
        if (se.getEffectTarget() == EffectTarget.AURA) {
          startAuraRunnable = true;
          break;
        }
      }

      double chance = gemCS.getDouble("weight", -1);
      if (chance < 0) {
        chance = gemCS.getDouble("chance");
      }
      String prefix = gemCS.getString("prefix");
      if (prefix != null && !prefix.equalsIgnoreCase("")) {
        getSockettingSettings().getSocketGemPrefixes().add(prefix);
      }
      String suffix = gemCS.getString("suffix");
      if (suffix != null && !suffix.equalsIgnoreCase("")) {
        getSockettingSettings().getSocketGemSuffixes().add(suffix);
      }
      List<String> lore = gemCS.getStringList("lore");
      Map<Enchantment, Integer> enchantments = new HashMap<>();
      if (gemCS.isConfigurationSection("enchantments")) {
        ConfigurationSection enchCS = gemCS.getConfigurationSection("enchantments");
        for (String key1 : enchCS.getKeys(false)) {
          Enchantment ench = Enchantment.getByName(key1);
          if (ench == null) {
            continue;
          }
          int level = Math.min(Math.max(1, enchCS.getInt(key1)), 127);
          enchantments.put(ench, level);
        }
      }
      List<String> commands = gemCS.getStringList("commands");
      List<SocketCommand> socketCommands = new ArrayList<>();
      for (String s : commands) {
        SocketCommand sc = new SocketCommand(s);
        socketCommands.add(sc);
      }
      SocketGem sg = new SocketGem(key, gemType, socketEffects, chance, prefix, suffix, lore, enchantments,
          socketCommands);
      getSockettingSettings().getSocketGemMap().put(key, sg);
      loadedSocketGems.add(key);
    }
    LOGGER.info("Loaded socket gems: " + loadedSocketGems.toString());

    if (auraTask != null) {
      auraTask.cancel();
    }
    if (startAuraRunnable) {
      auraRunnable = new AuraRunnable();
      auraTask = auraRunnable.runTaskTimer(this, 20L * 5, 20L * 5);
      getLogger().info("AuraRunnable enabled due to one or more gems detected as using AURA target type.");
      LOGGER.info("AuraRunnable enabled due to one or more gems detected as using AURA target type.");
    } else {
      getLogger().info("AuraRunnable disabled due to no gems detected as using AURA target type.");
      LOGGER.info("AuraRunnable disabled due to no gems detected as using AURA target type.");
    }
  }

  private void loadIdentifyingSettings() {
    YamlConfiguration c = identifyingYAML;
    MythicIdentifyingSettings mis = new MythicIdentifyingSettings();
    mis.setIdentityTomeName(c.getString("items.identity-tome.name", "&5Identity Tome"));
    mis.setIdentityTomeLore(c.getStringList("items.identity-tome.lore"));
    mis.setUnidentifiedItemName(c.getString("items.unidentified.name", "&FUnidentified Item"));
    mis.setUnidentifiedItemLore(c.getStringList("items.unidentified.lore"));
    identifyingSettings = mis;
  }

  public AuraRunnable getAuraRunnable() {
    return auraRunnable;
  }

}
