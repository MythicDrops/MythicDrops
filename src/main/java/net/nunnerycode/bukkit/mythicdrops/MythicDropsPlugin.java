package net.nunnerycode.bukkit.mythicdrops;

import com.modcrafting.diablodrops.name.NamesLoader;

import net.nunnerycode.bukkit.libraries.ivory.config.VersionedIvoryYamlConfiguration;
import net.nunnerycode.bukkit.mythicdrops.anvil.AnvilListener;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.items.builders.DropBuilder;
import net.nunnerycode.bukkit.mythicdrops.api.names.NameType;
import net.nunnerycode.bukkit.mythicdrops.api.settings.ConfigSettings;
import net.nunnerycode.bukkit.mythicdrops.api.settings.CreatureSpawningSettings;
import net.nunnerycode.bukkit.mythicdrops.api.settings.IdentifyingSettings;
import net.nunnerycode.bukkit.mythicdrops.api.settings.RepairingSettings;
import net.nunnerycode.bukkit.mythicdrops.api.settings.SockettingSettings;
import net.nunnerycode.bukkit.mythicdrops.api.socketting.EffectTarget;
import net.nunnerycode.bukkit.mythicdrops.api.socketting.GemType;
import net.nunnerycode.bukkit.mythicdrops.api.socketting.SocketEffect;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.aura.AuraRunnable;
import net.nunnerycode.bukkit.mythicdrops.commands.MythicDropsCommand;
import net.nunnerycode.bukkit.mythicdrops.crafting.CraftingListener;
import net.nunnerycode.bukkit.mythicdrops.hooks.LeveledMobsWrapper;
import net.nunnerycode.bukkit.mythicdrops.hooks.McMMOWrapper;
import net.nunnerycode.bukkit.mythicdrops.hooks.SplatterWrapper;
import net.nunnerycode.bukkit.mythicdrops.identification.IdentifyingListener;
import net.nunnerycode.bukkit.mythicdrops.items.CustomItemBuilder;
import net.nunnerycode.bukkit.mythicdrops.items.CustomItemMap;
import net.nunnerycode.bukkit.mythicdrops.items.MythicDropBuilder;
import net.nunnerycode.bukkit.mythicdrops.names.NameMap;
import net.nunnerycode.bukkit.mythicdrops.repair.RepairingListener;
import net.nunnerycode.bukkit.mythicdrops.settings.MythicConfigSettings;
import net.nunnerycode.bukkit.mythicdrops.settings.MythicCreatureSpawningSettings;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketCommand;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketGem;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketParticleEffect;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketPotionEffect;
import net.nunnerycode.bukkit.mythicdrops.socketting.SockettingListener;
import net.nunnerycode.bukkit.mythicdrops.spawning.ItemSpawningListener;
import net.nunnerycode.bukkit.mythicdrops.utils.TierUtil;
import net.nunnerycode.java.libraries.cannonball.DebugPrinter;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.mcstats.Metrics;

import se.ranzdo.bukkit.methodcommand.CommandHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import static net.nunnerycode.bukkit.libraries.ivory.config.VersionedIvoryYamlConfiguration.VersionUpdateType;

public final class MythicDropsPlugin extends JavaPlugin implements MythicDrops {

  private static MythicDropsPlugin _INSTANCE;
  private ConfigSettings configSettings;
  private CreatureSpawningSettings creatureSpawningSettings;
  private RepairingSettings repairingSettings;
  private SockettingSettings sockettingSettings;
  private IdentifyingSettings identifyingSettings;
  private DebugPrinter debugPrinter;
  private VersionedIvoryYamlConfiguration configYAML;
  private VersionedIvoryYamlConfiguration customItemYAML;
  private VersionedIvoryYamlConfiguration itemGroupYAML;
  private VersionedIvoryYamlConfiguration languageYAML;
  private VersionedIvoryYamlConfiguration tierYAML;
  private VersionedIvoryYamlConfiguration creatureSpawningYAML;
  private VersionedIvoryYamlConfiguration repairingYAML;
  private VersionedIvoryYamlConfiguration socketGemsYAML;
  private VersionedIvoryYamlConfiguration sockettingYAML;
  private VersionedIvoryYamlConfiguration identifyingYAML;
  private NamesLoader namesLoader;
  private CommandHandler commandHandler;
  private SplatterWrapper splatterWrapper;
  private AuraRunnable auraRunnable;

  public static DropBuilder getNewDropBuilder() {
    return new MythicDropBuilder(getInstance());
  }

  public static MythicDropsPlugin getInstance() {
    return _INSTANCE;
  }

  @Override
  public VersionedIvoryYamlConfiguration getCreatureSpawningYAML() {
    return creatureSpawningYAML;
  }

  @Override
  public void debug(Level level, String... messages) {
    if (getConfigSettings() != null && !getConfigSettings().isDebugMode()) {
      return;
    }
    debugPrinter.debug(level, messages);
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
  public VersionedIvoryYamlConfiguration getConfigYAML() {
    return configYAML;
  }

  @Override
  public VersionedIvoryYamlConfiguration getCustomItemYAML() {
    return customItemYAML;
  }

  @Override
  public VersionedIvoryYamlConfiguration getItemGroupYAML() {
    return itemGroupYAML;
  }

  @Override
  public VersionedIvoryYamlConfiguration getLanguageYAML() {
    return languageYAML;
  }

  @Override
  public VersionedIvoryYamlConfiguration getTierYAML() {
    return tierYAML;
  }

  public VersionedIvoryYamlConfiguration getSocketGemsYAML() {
    return socketGemsYAML;
  }

  @Override
  public VersionedIvoryYamlConfiguration getSockettingYAML() {
    return sockettingYAML;
  }

  public VersionedIvoryYamlConfiguration getRepairingYAML() {
    return repairingYAML;
  }

  @Override
  public VersionedIvoryYamlConfiguration getIdentifyingYAML() {
    return identifyingYAML;
  }

  @Override
  public void reloadSettings() {
    loadCoreSettings();
    loadCreatureSpawningSettings();
    loadRepairSettings();
    loadSockettingSettings();
    loadSocketGems();
    loadIdentifyingSettings();
  }

  @Override
  public void reloadTiers() {
    // TODO: load tiers
  }

  @Override
  public void reloadCustomItems() {
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
      if (material == Material.AIR) {
        continue;
      }
      builder.withMaterial(material);
      builder.withDisplayName(cs.getString("displayName", key));
      builder.withLore(cs.getStringList("lore"));
      builder.withChanceToBeGivenToMonster(cs.getDouble("chanceToBeGivenToAMonster", 0));
      builder.withChanceToDropOnDeath(cs.getDouble("chanceToDropOnDeath", 0));
      Map<Enchantment, Integer> enchantments = new HashMap<>();
      if (cs.isConfigurationSection("enchantments")) {
        for (String ench : cs.getConfigurationSection("enchantments").getKeys(false)) {
          Enchantment enchantment = Enchantment.getByName(ench);
          if (enchantment == null) {
            continue;
          }
          enchantments.put(enchantment, cs.getInt("enchantments." + ench));
        }
      }
      builder.withEnchantments(enchantments);
      builder.withBroadcastOnFind(cs.getBoolean("broadcastOnFind", false));
      CustomItem ci = builder.build();
      CustomItemMap.getInstance().put(key, ci);
      loadedCustomItemsNames.add(key);
    }
    debug(Level.INFO, "Loaded custom items: " + loadedCustomItemsNames.toString());
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
  public SplatterWrapper getSplatterWrapper() {
    return splatterWrapper;
  }

  @Override
  public void onDisable() {
    HandlerList.unregisterAll(this);
    if (auraRunnable != null) {
      auraRunnable.cancel();
    }
  }

  @Override
  public void onEnable() {
    _INSTANCE = this;

    debugPrinter = new DebugPrinter(getDataFolder().getPath(), "debug.log");
    namesLoader = new NamesLoader(this);

    configYAML = new VersionedIvoryYamlConfiguration(new File(getDataFolder(), "config.yml"),
                                                     getResource("config.yml"),
                                                     VersionUpdateType.BACKUP_AND_UPDATE);
    if (configYAML.update()) {
      debug(Level.INFO, "Updating config.yml");
      getLogger().info("Updating config.yml");
    }
    configYAML.load();

    tierYAML = new VersionedIvoryYamlConfiguration(new File(getDataFolder(), "tier.yml"),
                                                   getResource("tier.yml"),
                                                   VersionUpdateType.BACKUP_AND_UPDATE);
    if (tierYAML.update()) {
      debug(Level.INFO, "Updating tier.yml");
      getLogger().info("Updating tier.yml");
    }
    tierYAML.load();

    customItemYAML =
        new VersionedIvoryYamlConfiguration(new File(getDataFolder(), "customItems.yml"),
                                            getResource("customItems.yml"),
                                            VersionUpdateType.BACKUP_AND_UPDATE);
    if (customItemYAML.update()) {
      debug(Level.INFO, "Updating customItems.yml");
      getLogger().info("Updating customItems.yml");
    }
    customItemYAML.load();

    itemGroupYAML = new VersionedIvoryYamlConfiguration(new File(getDataFolder(), "itemGroups.yml"),
                                                        getResource("itemGroups.yml"),
                                                        VersionUpdateType.BACKUP_AND_UPDATE);
    if (itemGroupYAML.update()) {
      debug(Level.INFO, "Updating itemGroups.yml");
      getLogger().info("Updating itemGroups.yml");
    }
    itemGroupYAML.load();

    languageYAML = new VersionedIvoryYamlConfiguration(new File(getDataFolder(), "language.yml"),
                                                       getResource("language.yml"),
                                                       VersionUpdateType.BACKUP_AND_UPDATE);
    if (languageYAML.update()) {
      debug(Level.INFO, "Updating language.yml");
      getLogger().info("Updating language.yml");
    }
    languageYAML.load();

    creatureSpawningYAML =
        new VersionedIvoryYamlConfiguration(new File(getDataFolder(), "creatureSpawning.yml"),
                                            getResource("creatureSpawning.yml"),
                                            VersionUpdateType.BACKUP_AND_UPDATE);
    if (creatureSpawningYAML.update()) {
      debug(Level.INFO, "Updating creatureSpawning.yml");
      getLogger().info("Updating creatureSpawning.yml");
    }
    creatureSpawningYAML.load();

    repairingYAML = new VersionedIvoryYamlConfiguration(new File(getDataFolder(), "repairing.yml"),
                                                        getResource("repairing.yml"),
                                                        VersionUpdateType.BACKUP_AND_UPDATE);
    if (repairingYAML.update()) {
      debug(Level.INFO, "Updating repairing.yml");
      getLogger().info("Updating repairing.yml");
    }
    repairingYAML.load();

    socketGemsYAML =
        new VersionedIvoryYamlConfiguration(new File(getDataFolder(), "socketGems.yml"),
                                            getResource("socketGems.yml"),
                                            VersionUpdateType.BACKUP_AND_UPDATE);
    if (socketGemsYAML.update()) {
      debug(Level.INFO, "Updating socketGems.yml");
      getLogger().info("Updating socketGems.yml");
    }
    socketGemsYAML.load();

    sockettingYAML =
        new VersionedIvoryYamlConfiguration(new File(getDataFolder(), "socketting.yml"),
                                            getResource("socketting.yml"),
                                            VersionUpdateType.BACKUP_AND_UPDATE);
    if (sockettingYAML.update()) {
      debug(Level.INFO, "Updating socketting.yml");
      getLogger().info("Updating socketting.yml");
    }
    sockettingYAML.load();

    identifyingYAML =
        new VersionedIvoryYamlConfiguration(new File(getDataFolder(), "identifying.yml"),
                                            getResource("identifying.yml"),
                                            VersionUpdateType.BACKUP_AND_UPDATE);
    if (identifyingYAML.update()) {
      debug(Level.INFO, "Updating identifying.yml");
      getLogger().info("Updating identifying.yml");
    }
    identifyingYAML.load();

    writeResourceFiles();

    reloadTiers();
    reloadNames();
    reloadCustomItems();
    reloadSettings();

    Bukkit.getPluginManager().registerEvents(new AnvilListener(), this);
    Bukkit.getPluginManager().registerEvents(new CraftingListener(this), this);

    commandHandler = new CommandHandler(this);
    commandHandler.registerCommands(new MythicDropsCommand(this));

    if (getConfigSettings().isCreatureSpawningEnabled()) {
      getLogger().info("Mobs spawning with equipment enabled");
      debug(Level.INFO, "Mobs spawning with equipment enabled");
      Bukkit.getPluginManager().registerEvents(new ItemSpawningListener(this), this);
    }
    if (getConfigSettings().isRepairingEnabled()) {
      getLogger().info("Repairing enabled");
      debug(Level.INFO, "Repairing enabled");
      Bukkit.getPluginManager().registerEvents(new RepairingListener(this), this);
    }
    if (getConfigSettings().isSockettingEnabled()) {
      getLogger().info("Socketting enabled");
      debug(Level.INFO, "Socketting enabled");
      Bukkit.getPluginManager().registerEvents(new SockettingListener(this), this);
      auraRunnable = new AuraRunnable();
      auraRunnable.runTaskTimer(this, 20L * 5, 20L * 5);
    }
    if (getConfigSettings().isIdentifyingEnabled()) {
      getLogger().info("Identifying enabled");
      debug(Level.INFO, "Identifying enabled");
      Bukkit.getPluginManager().registerEvents(new IdentifyingListener(this), this);
    }

    if (getConfigSettings().isReportingEnabled()
        && Bukkit.getPluginManager().getPlugin("Splatter") != null) {
      String username = configYAML.getString("options.reporting.github-name", "githubusername");
      String password = configYAML.getString("options.reporting.github-password",
                                             "githubpassword");
      splatterWrapper = new SplatterWrapper(getName(), username, password);
    }

    if (getConfigSettings().isHookLeveledMobs() && Bukkit.getPluginManager().getPlugin
        ("LeveledMobs") != null) {
      getLogger().info("Hooking into LeveledMobs");
      debug(Level.INFO, "Hooking into LeveledMobs");
      Bukkit.getPluginManager().registerEvents(new LeveledMobsWrapper(this), this);
    }

    if (getConfigSettings().isHookMcMMO() && Bukkit.getPluginManager().getPlugin("mcMMO") != null) {
      getLogger().info("Hooking into mcMMO");
      debug(Level.INFO, "Hooking into mcMMO");
      Bukkit.getPluginManager().registerEvents(new McMMOWrapper(this), this);
    }

    startMetrics();

    debug(Level.INFO, "v" + getDescription().getVersion() + " enabled");
  }

  private void startMetrics() {
    try {
      Metrics metrics = new Metrics(this);
      metrics.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void writeResourceFiles() {
    namesLoader.writeDefault("/resources/lore/general.txt", false, true);
    namesLoader.writeDefault("/resources/lore/enchantments/damage_all.txt", false, true);
    namesLoader.writeDefault("/resources/lore/materials/diamond_sword.txt", false, true);
    namesLoader.writeDefault("/resources/lore/tiers/legendary.txt", false, true);
    namesLoader.writeDefault("/resources/prefixes/general.txt", false, true);
    namesLoader.writeDefault("/resources/prefixes/enchantments/damage_all.txt", false, true);
    namesLoader.writeDefault("/resources/prefixes/materials/diamond_sword.txt", false, true);
    namesLoader.writeDefault("/resources/prefixes/tiers/legendary.txt", false, true);
    namesLoader.writeDefault("/resources/suffixes/general.txt", false, true);
    namesLoader.writeDefault("/resources/suffixes/enchantments/damage_all.txt", false, true);
    namesLoader.writeDefault("/resources/suffixes/materials/diamond_sword.txt", false, true);
    namesLoader.writeDefault("/resources/suffixes/tiers/legendary.txt", false, true);
    namesLoader.writeDefault("/resources/mobnames/general.txt", false, true);
  }

  private void loadCoreSettings() {
    MythicConfigSettings mcs = new MythicConfigSettings();

    YamlConfiguration c = configYAML;
    mcs.setDebugMode(c.getBoolean("options.debug", true));
    mcs.setHookLeveledMobs(c.getBoolean("options.hooking.leveled-mobs", false));
    mcs.setHookMcMMO(c.getBoolean("options.hooking.mcmmo", false));
    mcs.setGiveMobsNames(c.getBoolean("options.give-mobs-names", false));
    mcs.setGiveAllMobsNames(c.getBoolean("options.give-all-mobs-names", false));
    mcs.setDisplayMobEquipment(c.getBoolean("options.display-mob-equipment", true));
    mcs.setBlankMobSpawnEnabled(c.getBoolean("options.blank-mob-spawn.enabled", false));
    mcs.setSkeletonsSpawnWithoutBows(c.getBoolean("options.blank-mob-spawn"
                                                  + ".skeletons-spawn-without-bow", false));
    mcs.setEnabledWorlds(c.getStringList("multiworld.enabled-worlds"));
    mcs.setRandomItemChance(c.getDouble("drops.random-item-chance", 1.0));
    mcs.setSocketGemChance(c.getDouble("drops.socket-gem-chance", 0.2));
    mcs.setIdentityTomeChance(c.getDouble("drops.identity-tome-chance", 0.1));
    mcs.setUnidentifiedItemChance(c.getDouble("drops.unidentified-item-chance", 0.1));
    mcs.setCreatureSpawningEnabled(c.getBoolean("components.creature-spawning-enabled", true));
    mcs.setSockettingEnabled(c.getBoolean("components.socketting-enabled", true));
    mcs.setRepairingEnabled(c.getBoolean("components.repairing-enabled", true));
    mcs.setIdentifyingEnabled(c.getBoolean("components.identifying-enabled", true));
    mcs.setItemDisplayNameFormat(c.getString("display.item-display-name-format",
                                             "%generalprefix% %generalsuffix%"));
    mcs.getTooltipFormat().clear();
    mcs.getTooltipFormat().addAll(c.getStringList("display.tooltip-format"));

    c = languageYAML;
    mcs.getLanguageMap().clear();
    for (String key : languageYAML.getKeys(true)) {
      if (languageYAML.isConfigurationSection(key) || key.equals("version")) {
        continue;
      }
      mcs.getLanguageMap().put(key, languageYAML.getString(key, key));
    }

    c = itemGroupYAML;
    if (c.isConfigurationSection("itemGroups")) {
      ConfigurationSection idCS = itemGroupYAML.getConfigurationSection("itemGroups");

      if (idCS.isConfigurationSection("toolGroups")) {
        List<String> toolGroupList = new ArrayList<>();
        ConfigurationSection toolCS = idCS.getConfigurationSection("toolGroups");
        for (String toolKind : toolCS.getKeys(false)) {
          List<String> idList = toolCS.getStringList(toolKind);
          toolGroupList.add(toolKind + " (" + idList.size() + ")");
          mcs.getItemTypesWithIds().put(toolKind.toLowerCase(), idList);
          mcs.getToolTypes().add(toolKind.toLowerCase());
        }
        debug(Level.INFO, "Loaded tool groups: " + toolGroupList.toString());
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
        debug(Level.INFO, "Loaded armor groups: " + armorGroupList.toString());
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
        debug(Level.INFO, "Loaded material groups: " + materialGroupList.toString());
      }
    }

    this.configSettings = mcs;
  }

  private void loadCreatureSpawningSettings() {
    MythicCreatureSpawningSettings mcss = new MythicCreatureSpawningSettings();
    YamlConfiguration c = creatureSpawningYAML;
    mcss.setPreventCustom(c.getBoolean("spawnPrevention/custom", true));
    mcss.setPreventSpawner(c.getBoolean("spawnPrevention/spawner", true));
    mcss.setPreventSpawnEgg(c.getBoolean("spawnPrevention/spawnEgg", true));

    if (c.isConfigurationSection("spawnPrevention/aboveY")) {
      ConfigurationSection
          cs =
          c.getConfigurationSection("spawnPrevention/aboveY");
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
        debug(Level.INFO, et.name() + " | " + TierUtil.getStringsFromTiers(tiers).toString());
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
    mobNames.put(NameType.MOB_NAME.getFormat(), generalMobNames);
    int numOfLoadedMobNames = generalMobNames.size();

    for (String s : mobNameFolder.list()) {
      if (s.endsWith(".txt") && !s.equals("general.txt")) {
        List<String> nameList = new ArrayList<>();
        namesLoader.loadFile(nameList, "/resources/mobnames/" + s);
        mobNames.put(
            NameType.MOB_NAME.getFormat() + "." + s.replace(".txt", "").toLowerCase(),
            nameList);
        numOfLoadedMobNames += nameList.size();
      }
    }

    debug(Level.INFO, "Loaded mob names: " + numOfLoadedMobNames);
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

    debug(Level.INFO, "Loaded lore: " + numOfLoadedLore);
    NameMap.getInstance().putAll(lore);
  }

  private void loadSuffixes() {
    Map<String, List<String>> suffixes = new HashMap<>();

    File suffixFolder = new File(getDataFolder(), "/resources/suffixes/");
    if (!suffixFolder.exists() && !suffixFolder.mkdirs()) {
      return;
    }

    List<String> generalSuffixes = new ArrayList<>();
    namesLoader.loadFile(generalSuffixes, "/resources/suffixes/general.txt");
    suffixes.put(NameType.GENERAL_SUFFIX.getFormat(), generalSuffixes);

    int numOfLoadedSuffixes = generalSuffixes.size();

    File tierSuffixFolder = new File(suffixFolder, "/tiers/");
    if (tierSuffixFolder.exists() && tierSuffixFolder.isDirectory()) {
      for (String s : tierSuffixFolder.list()) {
        if (s.endsWith(".txt")) {
          List<String> suffixList = new ArrayList<>();
          namesLoader.loadFile(suffixList, "/resources/suffixes/tiers/" + s);
          suffixes
              .put(NameType.TIER_SUFFIX.getFormat() + s.replace(".txt", "").toLowerCase(),
                   suffixList);
          numOfLoadedSuffixes += suffixList.size();
        }
      }
    }

    File materialSuffixFolder = new File(suffixFolder, "/materials/");
    if (materialSuffixFolder.exists() && materialSuffixFolder.isDirectory()) {
      for (String s : materialSuffixFolder.list()) {
        if (s.endsWith(".txt")) {
          List<String> suffixList = new ArrayList<>();
          namesLoader.loadFile(suffixList, "/resources/suffixes/materials/" + s);
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
          List<String> suffixList = new ArrayList<>();
          namesLoader.loadFile(suffixList, "/resources/suffixes/enchantments/" + s);
          suffixes.put(NameType.ENCHANTMENT_SUFFIX.getFormat() + s.replace(".txt", "")
              .toLowerCase(), suffixList);
          numOfLoadedSuffixes += suffixList.size();
        }
      }
    }

    debug(Level.INFO, "Loaded suffixes: " + numOfLoadedSuffixes);
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

    debug(Level.INFO, "Loaded prefixes: " + numOfLoadedPrefixes);
    NameMap.getInstance().putAll(prefixes);
  }

  private void loadRepairSettings() {
    // TODO: load repair settings
  }

  private void defaultRepairCosts() {
    Material[]
        wood =
        {Material.WOOD_AXE, Material.WOOD_HOE, Material.WOOD_PICKAXE, Material.WOOD_SPADE,
         Material.WOOD_SWORD, Material.BOW, Material.FISHING_ROD};
    Material[]
        stone =
        {Material.STONE_AXE, Material.STONE_PICKAXE, Material.STONE_HOE, Material.STONE_SWORD,
         Material.STONE_SPADE};
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
         Material.IRON_LEGGINGS, Material.IRON_PICKAXE, Material.IRON_HOE, Material.IRON_SPADE,
         Material.IRON_SWORD};
    Material[] diamond = {Material.DIAMOND_AXE, Material.DIAMOND_BOOTS, Material.DIAMOND_CHESTPLATE,
                          Material.DIAMOND_HELMET, Material.DIAMOND_HOE, Material.DIAMOND_LEGGINGS,
                          Material.DIAMOND_PICKAXE,
                          Material.DIAMOND_SPADE, Material.DIAMOND_SWORD};
    Material[]
        gold =
        {Material.GOLD_AXE, Material.GOLD_BOOTS, Material.GOLD_CHESTPLATE, Material.GOLD_HELMET,
         Material.GOLD_LEGGINGS, Material.GOLD_PICKAXE, Material.GOLD_HOE, Material.GOLD_SPADE,
         Material.GOLD_SWORD};
    for (Material m : wood) {
      repairingYAML
          .set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name",
               m.name());
      repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
                                                                         "-")
                        + ".costs.default.material-name", Material.WOOD.name());
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
                        + ".costs.default.material-name", Material.IRON_FENCE.name());
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
    // TODO: load socketting settings
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
      if (pet == null) {
        continue;
      }
      int duration = cs1.getInt(key + ".duration");
      int intensity = cs1.getInt(key + ".intensity");
      int radius = cs1.getInt(key + ".radius");
      String target = cs1.getString(key + ".target");
      EffectTarget et = EffectTarget.getFromName(target);
      if (et == null) {
        et = EffectTarget.NONE;
      }
      boolean affectsWielder = cs1.getBoolean(key + ".affectsWielder");
      boolean affectsTarget = cs1.getBoolean(key + ".affectsTarget");
      socketParticleEffectList.add(new SocketParticleEffect(pet, intensity, duration, radius, et,
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
      String target = cs1.getString(key + ".target");
      EffectTarget et = EffectTarget.getFromName(target);
      if (et == null) {
        et = EffectTarget.NONE;
      }
      boolean affectsWielder = cs1.getBoolean(key + ".affectsWielder");
      boolean affectsTarget = cs1.getBoolean(key + ".affectsTarget");
      socketPotionEffectList
          .add(new SocketPotionEffect(pet, intensity, duration, radius, et, affectsWielder,
                                      affectsTarget));
    }
    return socketPotionEffectList;
  }

  private void loadSocketGems() {
    getSockettingSettings().getSocketGemMap().clear();
    List<String> loadedSocketGems = new ArrayList<>();
    if (!socketGemsYAML.isConfigurationSection("socket-gems")) {
      return;
    }
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

      double chance = gemCS.getDouble("chance");
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
          Enchantment ench = null;
          for (Enchantment ec : Enchantment.values()) {
            if (ec.getName().equalsIgnoreCase(key1)) {
              ench = ec;
              break;
            }
          }
          if (ench == null) {
            continue;
          }
          int level = enchCS.getInt(key1);
          enchantments.put(ench, level);
        }
      }
      List<String> commands = gemCS.getStringList("commands");
      List<SocketCommand> socketCommands = new ArrayList<>();
      for (String s : commands) {
        SocketCommand sc = new SocketCommand(s);
        socketCommands.add(sc);
      }
      SocketGem
          sg =
          new SocketGem(key, gemType, socketEffects, chance, prefix, suffix, lore, enchantments,
                        socketCommands);
      getSockettingSettings().getSocketGemMap().put(key, sg);
      loadedSocketGems.add(key);
    }
    debug(Level.INFO, "Loaded socket gems: " + loadedSocketGems.toString());
  }

  private void loadIdentifyingSettings() {
    // TODO: load identifying settings
  }

  public AuraRunnable getAuraRunnable() {
    return auraRunnable;
  }
}
