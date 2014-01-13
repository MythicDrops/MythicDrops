package net.nunnerycode.bukkit.mythicdrops;

import com.conventnunnery.libraries.config.CommentedConventYamlConfiguration;
import com.conventnunnery.libraries.config.ConventYamlConfiguration;
import com.modcrafting.diablodrops.name.NamesLoader;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.enchantments.MythicEnchantment;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
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
import net.nunnerycode.bukkit.mythicdrops.commands.MythicDropsCommand;
import net.nunnerycode.bukkit.mythicdrops.identification.IdentifyingListener;
import net.nunnerycode.bukkit.mythicdrops.items.CustomItemBuilder;
import net.nunnerycode.bukkit.mythicdrops.items.CustomItemMap;
import net.nunnerycode.bukkit.mythicdrops.names.NameMap;
import net.nunnerycode.bukkit.mythicdrops.repair.MythicRepairCost;
import net.nunnerycode.bukkit.mythicdrops.repair.MythicRepairItem;
import net.nunnerycode.bukkit.mythicdrops.repair.RepairingListener;
import net.nunnerycode.bukkit.mythicdrops.settings.MythicConfigSettings;
import net.nunnerycode.bukkit.mythicdrops.settings.MythicCreatureSpawningSettings;
import net.nunnerycode.bukkit.mythicdrops.settings.MythicIdentifyingSettings;
import net.nunnerycode.bukkit.mythicdrops.settings.MythicRepairingSettings;
import net.nunnerycode.bukkit.mythicdrops.settings.MythicSockettingSettings;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketCommand;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketGem;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketParticleEffect;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketPotionEffect;
import net.nunnerycode.bukkit.mythicdrops.socketting.SockettingListener;
import net.nunnerycode.bukkit.mythicdrops.spawning.ItemSpawningListener;
import net.nunnerycode.bukkit.mythicdrops.tiers.MythicTierBuilder;
import net.nunnerycode.bukkit.mythicdrops.tiers.TierMap;
import net.nunnerycode.bukkit.mythicdrops.utils.ChatColorUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.TierUtil;
import net.nunnerycode.java.libraries.cannonball.DebugPrinter;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.HandlerList;
import org.bukkit.material.MaterialData;
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

public final class MythicDropsPlugin extends JavaPlugin implements MythicDrops {

	private static MythicDropsPlugin _INSTANCE;
	private ConfigSettings configSettings;
	private CreatureSpawningSettings creatureSpawningSettings;
	private RepairingSettings repairingSettings;
	private SockettingSettings sockettingSettings;
	private IdentifyingSettings identifyingSettings;
	private DebugPrinter debugPrinter;
	private CommentedConventYamlConfiguration configYAML;
	private CommentedConventYamlConfiguration customItemYAML;
	private CommentedConventYamlConfiguration itemGroupYAML;
	private CommentedConventYamlConfiguration languageYAML;
	private CommentedConventYamlConfiguration tierYAML;
	private CommentedConventYamlConfiguration creatureSpawningYAML;
	private CommentedConventYamlConfiguration repairingYAML;
	private CommentedConventYamlConfiguration socketGemsYAML;
	private CommentedConventYamlConfiguration sockettingYAML;
	private CommentedConventYamlConfiguration identifyingYAML;
	private NamesLoader namesLoader;
	private CommandHandler commandHandler;

	public static MythicDropsPlugin getInstance() {
		return _INSTANCE;
	}

	@Override
	public void onDisable() {
		HandlerList.unregisterAll(this);
	}

	@Override
	public void onEnable() {
		_INSTANCE = this;

		debugPrinter = new DebugPrinter(getDataFolder().getPath(), "debug.log");
		namesLoader = new NamesLoader(this);

		unpackConfigurationFiles(new String[]{"config.yml", "customItems.yml", "itemGroups.yml", "language.yml",
				"tier.yml", "creatureSpawning.yml", "repairing.yml", "socketGems.yml", "socketting.yml",
				"identifying.yml"}, false);

		configYAML = new CommentedConventYamlConfiguration(new File(getDataFolder(), "config.yml"),
				YamlConfiguration.loadConfiguration(getResource("config.yml")).getString("version"));
		configYAML.options().backupOnUpdate(true);
		configYAML.options().updateOnLoad(true);
		configYAML.load();

		customItemYAML = new CommentedConventYamlConfiguration(new File(getDataFolder(), "customItems.yml"),
				YamlConfiguration.loadConfiguration(getResource("customItems.yml")).getString("version"));
		customItemYAML.options().backupOnUpdate(true);
		customItemYAML.options().updateOnLoad(true);
		customItemYAML.load();

		itemGroupYAML = new CommentedConventYamlConfiguration(new File(getDataFolder(), "itemGroups.yml"),
				YamlConfiguration.loadConfiguration(getResource("itemGroups.yml")).getString("version"));
		itemGroupYAML.options().backupOnUpdate(true);
		itemGroupYAML.options().updateOnLoad(true);
		itemGroupYAML.load();

		languageYAML = new CommentedConventYamlConfiguration(new File(getDataFolder(), "language.yml"),
				YamlConfiguration.loadConfiguration(getResource("language.yml")).getString("version"));
		languageYAML.options().backupOnUpdate(true);
		languageYAML.options().updateOnLoad(true);
		languageYAML.load();

		tierYAML = new CommentedConventYamlConfiguration(new File(getDataFolder(), "tier.yml"),
				YamlConfiguration.loadConfiguration(getResource("tier.yml")).getString("version"));
		tierYAML.options().backupOnUpdate(true);
		tierYAML.options().updateOnLoad(true);
		tierYAML.load();

		creatureSpawningYAML = new CommentedConventYamlConfiguration(new File(getDataFolder(), "creatureSpawning.yml"),
				YamlConfiguration.loadConfiguration(getResource("creatureSpawning.yml")).getString("version"));
		creatureSpawningYAML.options().pathSeparator('/');
		creatureSpawningYAML.options().backupOnUpdate(true);
		creatureSpawningYAML.options().updateOnLoad(true);
		creatureSpawningYAML.load();

		repairingYAML = new CommentedConventYamlConfiguration(new File(getDataFolder(), "repairing.yml"),
				YamlConfiguration.loadConfiguration(getResource("repairing.yml")).getString("version"));
		repairingYAML.options().backupOnUpdate(true);
		repairingYAML.options().updateOnLoad(true);
		repairingYAML.load();

		socketGemsYAML = new CommentedConventYamlConfiguration(new File(getDataFolder(), "socketGems.yml"),
				YamlConfiguration.loadConfiguration(getResource("socketGems.yml")).getString("version"));
		socketGemsYAML.options().backupOnUpdate(true);
		socketGemsYAML.options().updateOnLoad(true);
		socketGemsYAML.load();

		sockettingYAML = new CommentedConventYamlConfiguration(new File(getDataFolder(), "socketting.yml"),
				YamlConfiguration.loadConfiguration(getResource("socketting.yml")).getString("version"));
		sockettingYAML.options().backupOnUpdate(true);
		sockettingYAML.options().updateOnLoad(true);
		sockettingYAML.load();

		identifyingYAML = new CommentedConventYamlConfiguration(new File(getDataFolder(), "identifying.yml"),
				YamlConfiguration.loadConfiguration(getResource("identifying.yml")).getString("version"));
		identifyingYAML.options().backupOnUpdate(true);
		identifyingYAML.options().updateOnLoad(true);
		identifyingYAML.load();

		writeResourceFiles();

		debugInformation();

		reloadTiers();
		reloadSettings();
		reloadCustomItems();
		reloadNames();

		commandHandler = new CommandHandler(this);
		commandHandler.registerCommands(new MythicDropsCommand(this));

		if (getCreatureSpawningSettings().isEnabled()) {
			Bukkit.getPluginManager().registerEvents(new ItemSpawningListener(this), this);
		}
		if (getRepairingSettings().isEnabled()) {
			Bukkit.getPluginManager().registerEvents(new RepairingListener(this), this);
		}
		if (getSockettingSettings().isEnabled()) {
			Bukkit.getPluginManager().registerEvents(new SockettingListener(this), this);
		}
		if (getIdentifyingSettings().isEnabled()) {
			Bukkit.getPluginManager().registerEvents(new IdentifyingListener(this), this);
		}

		startMetrics();

		debugPrinter.debug(Level.INFO, "v" + getDescription().getVersion() + " enabled");
	}

	private void startMetrics() {
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void debugInformation() {
		List<String> strings = new ArrayList<>();
		for (Enchantment e : Enchantment.values()) {
			strings.add(new EnchantmentWrapper(e.getId()).getName());
		}
		debugPrinter.debug(Level.INFO, "Enchantments: " + strings.toString());

		strings.clear();

		for (EntityType et : EntityType.values()) {
			strings.add(et.name());
		}
		debugPrinter.debug(Level.INFO, "EntityTypes: " + strings.toString());
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

	private void unpackConfigurationFiles(String[] configurationFiles, boolean overwrite) {
		for (String s : configurationFiles) {
			YamlConfiguration yc = YamlConfiguration.loadConfiguration(getResource(s));
			try {
				File f = new File(getDataFolder(), s);
				if (!f.exists()) {
					yc.save(f);
					continue;
				}
				if (overwrite) {
					yc.save(f);
				}
			} catch (IOException e) {
				getLogger().warning("Could not unpack " + s);
			}
		}
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
	public DebugPrinter getDebugPrinter() {
		return debugPrinter;
	}

	@Override
	public ConventYamlConfiguration getConfigYAML() {
		return configYAML;
	}

	@Override
	public ConventYamlConfiguration getCustomItemYAML() {
		return customItemYAML;
	}

	@Override
	public ConventYamlConfiguration getItemGroupYAML() {
		return itemGroupYAML;
	}

	@Override
	public ConventYamlConfiguration getLanguageYAML() {
		return languageYAML;
	}

	@Override
	public ConventYamlConfiguration getTierYAML() {
		return tierYAML;
	}

	public CommentedConventYamlConfiguration getSocketGemsYAML() {
		return socketGemsYAML;
	}

	@Override
	public CommentedConventYamlConfiguration getSockettingYAML() {
		return sockettingYAML;
	}

	public CommentedConventYamlConfiguration getRepairingYAML() {
		return repairingYAML;
	}

	@Override
	public CommentedConventYamlConfiguration getIdentifyingYAML() {
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

	private void loadCoreSettings() {
		MythicConfigSettings mcs = new MythicConfigSettings();

		if (configYAML != null) {
			mcs.setAutoUpdate(configYAML.getBoolean("options.autoUpdate", false));
			mcs.setDebugMode(configYAML.getBoolean("options.debugMode", false));
			mcs.setScriptsDirectory(configYAML.getString("options.scriptsDirectory", "scripts"));
			mcs.setItemDisplayNameFormat(configYAML.getString("display.itemDisplayNameFormat",
					"%generalprefix% %generalsuffix%"));
			mcs.setRandomLoreEnabled(configYAML.getBoolean("display.tooltips.randomLoreEnabled", false));
			mcs.setRandomLoreChance(configYAML.getDouble("display.tooltips.randomLoreChance", 0.25));
			mcs.getTooltipFormat().addAll(configYAML.getStringList("display.tooltips.format"));
		}

		if (itemGroupYAML != null && itemGroupYAML.isConfigurationSection("itemGroups")) {
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
				debugPrinter.debug(Level.INFO, "Loaded tool groups: " + toolGroupList.toString());
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
				debugPrinter.debug(Level.INFO, "Loaded armor groups: " + armorGroupList.toString());
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
				debugPrinter.debug(Level.INFO, "Loaded material groups: " + materialGroupList.toString());
			}
		}

		if (languageYAML != null) {
			for (String s : languageYAML.getKeys(true)) {
				if (languageYAML.isConfigurationSection(s)) {
					continue;
				}
				mcs.getLanguageMap().put(s, languageYAML.getString(s, s));
			}
		}

		this.configSettings = mcs;
	}

	private void loadCreatureSpawningSettings() {
		MythicCreatureSpawningSettings css = new MythicCreatureSpawningSettings();

		if (creatureSpawningYAML != null) {
			css.setEnabled(creatureSpawningYAML.getBoolean("enabled", true));
			css.setGiveMobsEquipment(creatureSpawningYAML.getBoolean("options/give-mobs-equipment", true));
			css.setGiveMobsNames(creatureSpawningYAML.getBoolean("options/give-mobs-names", true));
			css.setCanMobsPickUpEquipment(creatureSpawningYAML.getBoolean("options/can-mobs-pick-up-equipment", true));
			css.setBlankMobSpawnEnabled(creatureSpawningYAML.getBoolean("options/blank-mob-spawn.enabled", false));
			css.setBlankMobSpawnSkeletonsSpawnWithBows(!creatureSpawningYAML.getBoolean("options/blank-mob-spawn" +
					"/skeletons-no-bow", false));
			css.setGlobalSpawnChance(creatureSpawningYAML.getDouble("globalSpawnChance", 0.25));
			css.setPreventCustom(creatureSpawningYAML.getBoolean("spawnPrevention/custom", true));
			css.setPreventSpawner(creatureSpawningYAML.getBoolean("spawnPrevention/spawner", true));
			css.setPreventSpawnEgg(creatureSpawningYAML.getBoolean("spawnPrevention/spawnEgg", true));

			if (creatureSpawningYAML.isConfigurationSection("spawnPrevention/aboveY")) {
				ConfigurationSection cs = creatureSpawningYAML.getConfigurationSection("spawnPrevention/aboveY");
				for (String wn : cs.getKeys(false)) {
					if (cs.isConfigurationSection(wn)) {
						continue;
					}
					css.setSpawnHeightLimit(wn, cs.getInt(wn, 255));
				}
			}

			css.setCustomItemsSpawn(creatureSpawningYAML.getBoolean("customItems/spawn", true));
			css.setOnlyCustomItemsSpawn(creatureSpawningYAML.getBoolean("customItems/onlySpawn", false));
			css.setCustomItemSpawnChance(creatureSpawningYAML.getDouble("customItems/chance", 0.05));

			if (creatureSpawningYAML.isConfigurationSection("tierDrops")) {
				ConfigurationSection cs = creatureSpawningYAML.getConfigurationSection("tierDrops");
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
					debugPrinter.debug(Level.INFO, et.name() + " | " + TierUtil.getStringsFromTiers(tiers).toString());
					css.setEntityTypeTiers(et, tiers);
				}
			}

			if (creatureSpawningYAML.isConfigurationSection("spawnWithDropChance")) {
				ConfigurationSection cs = creatureSpawningYAML.getConfigurationSection("spawnWithDropChance");
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
					css.setEntityTypeChance(et, d);
				}
			}
		}

		this.creatureSpawningSettings = css;
	}

	@Override
	public void reloadTiers() {
		TierMap.getInstance().clear();
		CommentedConventYamlConfiguration c = tierYAML;
		if (c == null) {
			return;
		}
		List<String> loadedTierNames = new ArrayList<>();
		for (String key : c.getKeys(false)) {
			// Check if the key has other fields under it and if not, move on to the next
			if (!c.isConfigurationSection(key)) {
				continue;
			}
			ConfigurationSection cs = c.getConfigurationSection(key);
			MythicTierBuilder builder = new MythicTierBuilder(key.toLowerCase());
			builder.withDisplayName(cs.getString("displayName", key));
			builder.withDisplayColor(ChatColorUtil.getChatColorOrFallback(cs.getString("displayColor"),
					ChatColorUtil.getRandomChatColor()));
			builder.withIdentificationColor(ChatColorUtil.getChatColorOrFallback(cs.getString("identifierColor")
					, ChatColorUtil.getRandomChatColor()));

			ConfigurationSection enchCS = cs.getConfigurationSection("enchantments");
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

			ConfigurationSection loreCS = cs.getConfigurationSection("lore");
			if (loreCS != null) {
				builder.withMinimumBonusLore(loreCS.getInt("minimumBonusLore", 0));
				builder.withMaximumBonusLore(loreCS.getInt("maximumBonusLore", 0));
				builder.withBaseLore(loreCS.getStringList("baseLore"));
				builder.withBonusLore(loreCS.getStringList("bonusLore"));
			}

			builder.withMinimumDurabilityPercentage(cs.getDouble("minimumDurabilityPercentage", 1.0));
			builder.withMaximumDurabilityPercentage(cs.getDouble("maximumDurabilityPercentage", 1.0));
			builder.withMinimumSockets(cs.getInt("minimumSockets", 0));
			builder.withMaximumSockets(cs.getInt("maximumSockets", 0));
			builder.withAllowedItemGroups(cs.getStringList("itemTypes.allowedGroups"));
			builder.withDisallowedItemGroups(cs.getStringList("itemTypes.disallowedGroups"));
			builder.withAllowedItemIds(cs.getStringList("itemTypes.allowedItemIds"));
			builder.withDisallowedItemIds(cs.getStringList("itemTypes.disallowedItemIds"));

			if (cs.isConfigurationSection("chanceToSpawnOnAMonster")) {
				Map<String, Double> chanceToSpawnMap = new HashMap<>();
				for (String k : cs.getConfigurationSection("chanceToSpawnOnAMonster").getKeys(false)) {
					chanceToSpawnMap.put(k, cs.getDouble("chanceToSpawnOnAMonster." + k, 0));
					chanceToSpawnMap.put("default", cs.getDouble("chanceToSpawnOnAMonster"));
				}
				builder.withWorldSpawnChanceMap(chanceToSpawnMap);
			} else if (cs.isSet("chanceToSpawnOnAMonster")) {
				Map<String, Double> chanceToSpawnMap = new HashMap<>();
				chanceToSpawnMap.put("default", cs.getDouble("chanceToSpawnOnAMonster"));
				builder.withWorldSpawnChanceMap(chanceToSpawnMap);
			}

			if (cs.isConfigurationSection("chanceToDropOnMonsterDeath")) {
				Map<String, Double> chanceToSpawnMap = new HashMap<>();
				for (String k : cs.getConfigurationSection("chanceToDropOnMonsterDeath").getKeys(false)) {
					chanceToSpawnMap.put(k, cs.getDouble("chanceToDropOnMonsterDeath." + k, 1.0));
					chanceToSpawnMap.put("default", cs.getDouble("chanceToDropOnMonsterDeath", 1.0));
				}
				builder.withWorldDropChanceMap(chanceToSpawnMap);
			} else if (cs.isSet("chanceToDropOnMonsterDeath")) {
				Map<String, Double> chanceToSpawnMap = new HashMap<>();
				chanceToSpawnMap.put("default", cs.getDouble("chanceToDropOnMonsterDeath", 1.0));
				builder.withWorldDropChanceMap(chanceToSpawnMap);
			}

			if (cs.isConfigurationSection("chanceToDropBeIdentified")) {
				Map<String, Double> chanceToBeIdentified = new HashMap<>();
				for (String k : cs.getConfigurationSection("chanceToBeIdentified").getKeys(false)) {
					chanceToBeIdentified.put(k, cs.getDouble("chanceToBeIdentified." + k, 1.0));
					chanceToBeIdentified.put("default", cs.getDouble("chanceToBeIdentified", 1.0));
				}
				builder.withWorldIdentifyChanceMap(chanceToBeIdentified);
			} else if (cs.isSet("chanceToBeIdentified")) {
				Map<String, Double> chanceToSpawnMap = new HashMap<>();
				chanceToSpawnMap.put("default", cs.getDouble("chanceToBeIdentified", 1.0));
				builder.withWorldIdentifyChanceMap(chanceToSpawnMap);
			}

			builder.withChanceToHaveSockets(cs.getDouble("chanceToHaveSockets", 1D));
			builder.withBroadcastOnFind(cs.getBoolean("broadcastOnFind", false));

			Tier t = builder.build();

			if (t.getDisplayColor() == t.getIdentificationColor()) {
				debugPrinter.debug(Level.INFO, "Cannot load " + t.getName() + " due to displayColor and " +
						"identificationColor being the same");
				continue;
			}

			TierMap.getInstance().put(key.toLowerCase(), t);
			loadedTierNames.add(key.toLowerCase());
		}

		debugPrinter.debug(Level.INFO, "Loaded tiers: " + loadedTierNames.toString());
	}

	@Override
	public void reloadCustomItems() {
		CustomItemMap.getInstance().clear();
		CommentedConventYamlConfiguration c = customItemYAML;
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
			MaterialData materialData = new MaterialData(cs.getInt("materialID", 0), (byte) cs.getInt("materialData",
					0));
			if (materialData.getItemTypeId() == 0) {
				continue;
			}
			builder.withMaterialData(materialData);
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
			CustomItem ci = builder.build();
			CustomItemMap.getInstance().put(key, ci);
			loadedCustomItemsNames.add(key);
		}
		debugPrinter.debug(Level.INFO, "Loaded custom items: " + loadedCustomItemsNames.toString());
	}

	@Override
	public void reloadNames() {
		NameMap.getInstance().clear();
		loadPrefixes();
		loadSuffixes();
		loadLore();
		loadMobNames();
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

		for (File f : mobNameFolder.listFiles()) {
			if (f.getName().endsWith(".txt") && !f.getName().equals("general.txt")) {
				List<String> nameList = new ArrayList<>();
				namesLoader.loadFile(nameList, "/resources/mobnames/" + f.getName());
				mobNames.put(NameType.MOB_NAME.getFormat() + "." + f.getName().replace(".txt", ""), nameList);
				numOfLoadedMobNames += nameList.size();
			}
		}

		debugPrinter.debug(Level.INFO, "Loaded mob names: " + numOfLoadedMobNames);
		NameMap.getInstance().putAll(mobNames);
	}

	@Override
	public CommandHandler getCommandHandler() {
		return commandHandler;
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
			for (File f : tierLoreFolder.listFiles()) {
				if (f.getName().endsWith(".txt")) {
					List<String> loreList = new ArrayList<>();
					namesLoader.loadFile(loreList, "/resources/lore/tiers/" + f.getName());
					lore.put(NameType.TIER_LORE.getFormat() + f.getName().replace(".txt", ""), loreList);
					numOfLoadedLore += loreList.size();
				}
			}
		}

		File materialLoreFolder = new File(loreFolder, "/materials/");
		if (materialLoreFolder.exists() && materialLoreFolder.isDirectory()) {
			for (File f : materialLoreFolder.listFiles()) {
				if (f.getName().endsWith(".txt")) {
					List<String> loreList = new ArrayList<>();
					namesLoader.loadFile(loreList, "/resources/lore/materials/" + f.getName());
					lore.put(NameType.MATERIAL_LORE.getFormat() + f.getName().replace(".txt", ""), loreList);
					numOfLoadedLore += loreList.size();
				}
			}
		}

		File enchantmentLoreFolder = new File(loreFolder, "/enchantments/");
		if (enchantmentLoreFolder.exists() && enchantmentLoreFolder.isDirectory()) {
			for (File f : enchantmentLoreFolder.listFiles()) {
				if (f.getName().endsWith(".txt")) {
					List<String> loreList = new ArrayList<>();
					namesLoader.loadFile(loreList, "/resources/lore/enchantments/" + f.getName());
					lore.put(NameType.ENCHANTMENT_LORE.getFormat() + f.getName().replace(".txt", ""), loreList);
					numOfLoadedLore += loreList.size();
				}
			}
		}

		debugPrinter.debug(Level.INFO, "Loaded lore: " + numOfLoadedLore);
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
			for (File f : tierSuffixFolder.listFiles()) {
				if (f.getName().endsWith(".txt")) {
					List<String> suffixList = new ArrayList<>();
					namesLoader.loadFile(suffixList, "/resources/suffixes/tiers/" + f.getName());
					suffixes.put(NameType.TIER_SUFFIX.getFormat() + f.getName().replace(".txt", ""), suffixList);
					numOfLoadedSuffixes += suffixList.size();
				}
			}
		}

		File materialSuffixFolder = new File(suffixFolder, "/materials/");
		if (materialSuffixFolder.exists() && materialSuffixFolder.isDirectory()) {
			for (File f : materialSuffixFolder.listFiles()) {
				if (f.getName().endsWith(".txt")) {
					List<String> suffixList = new ArrayList<>();
					namesLoader.loadFile(suffixList, "/resources/suffixes/materials/" + f.getName());
					suffixes.put(NameType.MATERIAL_SUFFIX.getFormat() + f.getName().replace(".txt", ""), suffixList);
					numOfLoadedSuffixes += suffixList.size();
				}
			}
		}

		File enchantmentSuffixFolder = new File(suffixFolder, "/enchantments/");
		if (enchantmentSuffixFolder.exists() && enchantmentSuffixFolder.isDirectory()) {
			for (File f : enchantmentSuffixFolder.listFiles()) {
				if (f.getName().endsWith(".txt")) {
					List<String> suffixList = new ArrayList<>();
					namesLoader.loadFile(suffixList, "/resources/suffixes/enchantments/" + f.getName());
					suffixes.put(NameType.ENCHANTMENT_SUFFIX.getFormat() + f.getName().replace(".txt", ""), suffixList);
					numOfLoadedSuffixes += suffixList.size();
				}
			}
		}

		debugPrinter.debug(Level.INFO, "Loaded suffixes: " + numOfLoadedSuffixes);
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
			for (File f : tierPrefixFolder.listFiles()) {
				if (f.getName().endsWith(".txt")) {
					List<String> prefixList = new ArrayList<>();
					namesLoader.loadFile(prefixList, "/resources/prefixes/tiers/" + f.getName());
					prefixes.put(NameType.TIER_PREFIX.getFormat() + f.getName().replace(".txt", ""), prefixList);
					numOfLoadedPrefixes += prefixList.size();
				}
			}
		}

		File materialPrefixFolder = new File(prefixFolder, "/materials/");
		if (materialPrefixFolder.exists() && materialPrefixFolder.isDirectory()) {
			for (File f : materialPrefixFolder.listFiles()) {
				if (f.getName().endsWith(".txt")) {
					List<String> prefixList = new ArrayList<>();
					namesLoader.loadFile(prefixList, "/resources/prefixes/materials/" + f.getName());
					prefixes.put(NameType.MATERIAL_PREFIX.getFormat() + f.getName().replace(".txt", ""), prefixList);
					numOfLoadedPrefixes += prefixList.size();
				}
			}
		}

		File enchantmentPrefixFolder = new File(prefixFolder, "/enchantments/");
		if (enchantmentPrefixFolder.exists() && enchantmentPrefixFolder.isDirectory()) {
			for (File f : enchantmentPrefixFolder.listFiles()) {
				if (f.getName().endsWith(".txt")) {
					List<String> prefixList = new ArrayList<>();
					namesLoader.loadFile(prefixList, "/resources/prefixes/enchantments/" + f.getName());
					prefixes.put(NameType.ENCHANTMENT_PREFIX.getFormat() + f.getName().replace(".txt", ""), prefixList);
					numOfLoadedPrefixes += prefixList.size();
				}
			}
		}

		debugPrinter.debug(Level.INFO, "Loaded prefixes: " + numOfLoadedPrefixes);
		NameMap.getInstance().putAll(prefixes);
	}

	private void loadRepairSettings() {
		CommentedConventYamlConfiguration c = repairingYAML;
		if (!c.isConfigurationSection("repair-costs")) {
			defaultRepairCosts();
		}
		MythicRepairingSettings mrs = new MythicRepairingSettings();
		mrs.setEnabled(c.getBoolean("enabled", true));
		mrs.setPlaySounds(c.getBoolean("play-sounds", true));
		ConfigurationSection costs = c.getConfigurationSection("repair-costs");
		for (String key : costs.getKeys(false)) {
			if (!costs.isConfigurationSection(key)) {
				continue;
			}
			ConfigurationSection cs = costs.getConfigurationSection(key);
			MaterialData matData = parseMaterialData(cs);
			String itemName = cs.getString("item-name");
			List<String> itemLore = cs.getStringList("item-lore");
			List<MythicRepairCost> costList = new ArrayList<>();
			ConfigurationSection costsSection = cs.getConfigurationSection("costs");
			for (String costKey : costsSection.getKeys(false)) {
				if (!costsSection.isConfigurationSection(costKey)) {
					continue;
				}
				ConfigurationSection costSection = costsSection.getConfigurationSection(costKey);
				MaterialData itemCost = parseMaterialData(costSection);
				int experienceCost = costSection.getInt("experience-cost", 0);
				int priority = costSection.getInt("priority", 0);
				int amount = costSection.getInt("amount", 1);
				double repairPerCost = costSection.getDouble("repair-per-cost", 0.1);
				String costName = costSection.getString("item-name");
				List<String> costLore = costSection.getStringList("item-lore");

				MythicRepairCost rc = new MythicRepairCost(costKey, priority, experienceCost, repairPerCost, amount, itemCost,
						costName, costLore);
				costList.add(rc);
			}

			MythicRepairItem ri = new MythicRepairItem(key, matData, itemName, itemLore);
			ri.addRepairCosts(costList.toArray(new MythicRepairCost[costList.size()]));

			mrs.getRepairItemMap().put(ri.getName(), ri);
		}

		repairingSettings = mrs;

		debugPrinter.debug(Level.INFO, "Loaded repair items: " + mrs.getRepairItemMap().keySet().size());
	}

	private void defaultRepairCosts() {
		Material[] wood = {Material.WOOD_AXE, Material.WOOD_HOE, Material.WOOD_PICKAXE, Material.WOOD_SPADE,
				Material.WOOD_SWORD, Material.BOW, Material.FISHING_ROD};
		Material[] stone = {Material.STONE_AXE, Material.STONE_PICKAXE, Material.STONE_HOE, Material.STONE_SWORD,
				Material.STONE_SPADE};
		Material[] leather = {Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET,
				Material.LEATHER_LEGGINGS};
		Material[] chain = {Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET,
				Material.CHAINMAIL_LEGGINGS};
		Material[] iron = {Material.IRON_AXE, Material.IRON_BOOTS, Material.IRON_CHESTPLATE, Material.IRON_HELMET,
				Material.IRON_LEGGINGS, Material.IRON_PICKAXE, Material.IRON_HOE, Material.IRON_SPADE,
				Material.IRON_SWORD};
		Material[] diamond = {Material.DIAMOND_AXE, Material.DIAMOND_BOOTS, Material.DIAMOND_CHESTPLATE,
				Material.DIAMOND_HELMET, Material.DIAMOND_HOE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_PICKAXE,
				Material.DIAMOND_SPADE, Material.DIAMOND_SWORD};
		Material[] gold = {Material.GOLD_AXE, Material.GOLD_BOOTS, Material.GOLD_CHESTPLATE, Material.GOLD_HELMET,
				Material.GOLD_LEGGINGS, Material.GOLD_PICKAXE, Material.GOLD_HOE, Material.GOLD_SPADE,
				Material.GOLD_SWORD};
		for (Material m : wood) {
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name", m.name());
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.material-name", Material.WOOD.name());
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority", 0);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount", 1);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.experience-cost", 0);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.repair-per-cost", 0.1);
		}
		for (Material m : stone) {
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name", m.name());
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.material-name", Material.STONE.name());
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority", 0);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount", 1);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.experience-cost", 0);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.repair-per-cost", 0.1);
		}
		for (Material m : gold) {
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name", m.name());
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.material-name", Material.GOLD_INGOT.name());
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority", 0);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount", 1);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.experience-cost", 0);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.repair-per-cost", 0.1);
		}
		for (Material m : iron) {
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name", m.name());
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.material-name", Material.IRON_INGOT.name());
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority", 0);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount", 1);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.experience-cost", 0);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.repair-per-cost", 0.1);
		}
		for (Material m : diamond) {
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name", m.name());
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.material-name", Material.DIAMOND.name());
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority", 0);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount", 1);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.experience-cost", 0);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.repair-per-cost", 0.1);
		}
		for (Material m : leather) {
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name", m.name());
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.material-name", Material.LEATHER.name());
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority", 0);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount", 1);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.experience-cost", 0);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.repair-per-cost", 0.1);
		}
		for (Material m : chain) {
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name", m.name());
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.material-name", Material.IRON_FENCE.name());
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority", 0);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount", 1);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.experience-cost", 0);
			repairingYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.repair-per-cost", 0.1);
		}
		repairingYAML.save();
	}

	private MaterialData parseMaterialData(ConfigurationSection cs) {
		String materialDat = cs.getString("material-data", "");
		String materialName = cs.getString("material-name", "");
		if (materialDat.isEmpty()) {
			return new MaterialData(Material.getMaterial(materialName));
		}
		int id = 0;
		byte data = 0;
		String[] split = materialDat.split(";");
		switch (split.length) {
			case 0:
				break;
			case 1:
				id = NumberUtils.toInt(split[0], 0);
				break;
			default:
				id = NumberUtils.toInt(split[0], 0);
				data = NumberUtils.toByte(split[1], (byte) 0);
				break;
		}
		return new MaterialData(id, data);
	}

	private void loadSockettingSettings() {
		CommentedConventYamlConfiguration c = sockettingYAML;
		MythicSockettingSettings mss = new MythicSockettingSettings();
		mss.setEnabled(c.getBoolean("enabled", true));
		mss.setUseAttackerItemInHand(c.getBoolean("options.use-attacker-item-in-hand", true));
		mss.setUseAttackerArmorEquipped(c.getBoolean("options.use-attacker-armor-equipped", false));
		mss.setUseDefenderItemInHand(c.getBoolean("options.use-defender-item-in-hand", false));
		mss.setUseDefenderArmorEquipped(c.getBoolean("options.use-defender-armor-equipped", true));
		mss.setPreventMultipleChangesFromSockets(c.getBoolean("options.prevent-multiple-changes-from-sockets", true));
		mss.setSocketGemChanceToSpawn(c.getDouble("options.socket-gem-chance-to-spawn", 0.25));
		List<String> socketGemMats = c.getStringList("options.socket-gem-material-ids");
		List<MaterialData> socketGemMaterialDatas = new ArrayList<>();
		for (String s : socketGemMats) {
			int id;
			byte data;
			if (s.contains(";")) {
				String[] split = s.split(";");
				id = NumberUtils.toInt(split[0], 0);
				data = (byte) NumberUtils.toInt(split[1], 0);
			} else {
				id = NumberUtils.toInt(s, 0);
				data = 0;
			}
			if (id == 0) {
				continue;
			}
			socketGemMaterialDatas.add(new MaterialData(id, data));
		}
		mss.setSocketGemMaterialDatas(socketGemMaterialDatas);
		mss.setSocketGemName(c.getString("items.socket-name", "&6Socket Gem - %socketgem%"));
		mss.setSocketGemLore(c.getStringList("items.socket-lore"));
		mss.setSockettedItemString(c.getString("items.socketted-item-socket", "&6(Socket)"));
		mss.setSockettedItemLore(c.getStringList("items.socketted-item-lore"));

		sockettingSettings = mss;
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
			SocketGem sg = new SocketGem(key, gemType, socketEffects, chance, prefix, suffix, lore, enchantments,
					socketCommands);
			getSockettingSettings().getSocketGemMap().put(key, sg);
			loadedSocketGems.add(key);
		}
		debugPrinter.debug(Level.INFO, "Loaded socket gems: " + loadedSocketGems.toString());
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
			socketPotionEffectList.add(new SocketPotionEffect(pet, intensity, duration, radius, et, affectsWielder,
					affectsTarget));
		}
		return socketPotionEffectList;
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

	private void loadIdentifyingSettings() {
		CommentedConventYamlConfiguration c = identifyingYAML;
		MythicIdentifyingSettings mis = new MythicIdentifyingSettings();
		mis.setIdentityTomeName(c.getString("items.identity-tome.name", "&5Identity Tome"));
		mis.setIdentityTomeLore(c.getStringList("items.identity-tome.lore"));
		mis.setIdentityTomeChanceToSpawn(c.getDouble("items.identity-tome.chance-to-spawn", 0.1));
		mis.setUnidentifiedItemName(c.getString("items.unidentified-item.name", "&FUnidentified Item"));
		mis.setUnidentifiedItemLore(c.getStringList("items.unidentified-item.lore"));
		mis.setUnidentifiedItemChanceToSpawn(c.getDouble("items.unidentified-item.chance-to-spawn", 0.5));
		identifyingSettings = mis;
	}
}
