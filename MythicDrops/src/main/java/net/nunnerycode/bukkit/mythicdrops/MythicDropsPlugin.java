package net.nunnerycode.bukkit.mythicdrops;

import com.conventnunnery.libraries.config.CommentedConventYamlConfiguration;
import com.conventnunnery.libraries.config.ConventYamlConfiguration;
import com.google.common.io.Files;
import com.modcrafting.diablodrops.name.NamesLoader;
import groovy.lang.GroovyClassLoader;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.enchantments.MythicEnchantment;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.names.NameType;
import net.nunnerycode.bukkit.mythicdrops.api.scripts.MythicScript;
import net.nunnerycode.bukkit.mythicdrops.api.settings.ConfigSettings;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.commands.MythicDropsCommand;
import net.nunnerycode.bukkit.mythicdrops.items.CustomItemBuilder;
import net.nunnerycode.bukkit.mythicdrops.items.CustomItemMap;
import net.nunnerycode.bukkit.mythicdrops.items.TierMap;
import net.nunnerycode.bukkit.mythicdrops.names.NameMap;
import net.nunnerycode.bukkit.mythicdrops.settings.MythicConfigSettings;
import net.nunnerycode.bukkit.mythicdrops.tiers.MythicTierBuilder;
import net.nunnerycode.bukkit.mythicdrops.utils.ChatColorUtils;
import net.nunnerycode.java.libraries.cannonball.DebugPrinter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import se.ranzdo.bukkit.methodcommand.CommandHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public final class MythicDropsPlugin extends JavaPlugin implements MythicDrops {

	private static MythicDrops _INSTANCE;
	private ConfigSettings configSettings;
	private DebugPrinter debugPrinter;
	private CommentedConventYamlConfiguration configYAML;
	private CommentedConventYamlConfiguration customItemYAML;
	private CommentedConventYamlConfiguration itemGroupYAML;
	private CommentedConventYamlConfiguration languageYAML;
	private CommentedConventYamlConfiguration tierYAML;
	private NamesLoader namesLoader;
	private CommandHandler commandHandler;
	private File scriptsDirectory;
	private Map<String, MythicScript> scriptFiles;

	public static MythicDrops getInstance() {
		return _INSTANCE;
	}

	@Override
	public void onEnable() {
		_INSTANCE = this;

		debugPrinter = new DebugPrinter(getDataFolder().getPath(), "debug.log");
		configSettings = new MythicConfigSettings();
		namesLoader = new NamesLoader(this);

		unpackConfigurationFiles(new String[]{"config.yml", "customItems.yml", "itemGroups.yml", "language.yml",
											  "tier.yml"}, false);

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

		writeResourceFiles();

		reloadSettings();
		reloadTiers();
		reloadCustomItems();
		reloadNames();

		commandHandler = new CommandHandler(this);
		commandHandler.registerCommands(new MythicDropsCommand(this));

		scriptsDirectory = new File(getDataFolder(), "/" + configSettings.getScriptsDirectory() + "/");
		scriptFiles = new ConcurrentHashMap<>();

		if (!scriptsDirectory.exists()) {
			try {
				Files.createParentDirs(scriptsDirectory);
			} catch (IOException e) {
				debugPrinter.debug(Level.INFO, "Unable to create scripts directory");
			}
		}
		if (!scriptsDirectory.isDirectory()) {
			debugPrinter.debug(Level.INFO, "/" + configSettings.getScriptsDirectory() + "/ is not a directory");
		} else {
			ClassLoader classLoader = getClassLoader();
			GroovyClassLoader groovyClassLoader = new GroovyClassLoader(classLoader);

			for (String fileName : scriptsDirectory.list()) {
				if (!fileName.endsWith(".groovy")) {
					try {
						Class clazz = groovyClassLoader.parseClass(new File(getDataFolder(),
																			"/" + configSettings.getScriptsDirectory() +
																					"/" + fileName));
						Object obj = clazz.newInstance();
						if (!(obj instanceof MythicScript)) {
							continue;
						}
						MythicScript mythScript = (MythicScript) obj;
						mythScript.init(this);
						scriptFiles.put(fileName, mythScript);

						debugPrinter.debug(Level.INFO, "Loaded " + fileName + " as a script");
					} catch (Exception e) {
						debugPrinter.debug(Level.INFO, "Unable to load " + fileName + " as a script");
					}
				}
			}
		}
	}

	private void unpackConfigurationFiles(String[] configurationFiles, boolean overwrite) {
		for (String s : configurationFiles) {
			YamlConfiguration yc = CommentedConventYamlConfiguration.loadConfiguration(getResource(s));
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
	}

	@Override
	public ConfigSettings getConfigSettings() {
		return configSettings;
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

	@Override
	public void reloadSettings() {
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
				List<String> materialGroupList = new ArrayList<String>();
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
			builder.withDisplayColor(ChatColorUtils.getChatColorOrFallback(cs.getString("displayColor"),
																		   ChatColorUtils.getRandomChatColor()));
			builder.withIdentificationColor(ChatColorUtils.getChatColorOrFallback(cs.getString("identificationColor")
					, ChatColorUtils.getRandomChatColor()));

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
			builder.withAllowedItemGroups(cs.getStringList("itemTypes.allowedGroups"));
			builder.withDisallowedItemGroups(cs.getStringList("itemTypes.disallowedGroups"));
			builder.withAllowedItemIds(cs.getStringList("itemTypes.allowedItemIds"));
			builder.withDisallowedItemIds(cs.getStringList("itemTypes.disallowedItemIds"));

			if (cs.isConfigurationSection("chanceToSpawnOnAMonster")) {
				Map<String, Double> chanceToSpawnMap = new HashMap<>();
				for (String k : cs.getConfigurationSection("chanceToSpawnOnAMonster").getKeys(false)) {
					chanceToSpawnMap.put(k, cs.getDouble("chanceToSpawnOnAMonster." + k, 0));
				}
				if (chanceToSpawnMap.isEmpty()) {
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
					chanceToSpawnMap.put(k, cs.getDouble("chanceToDropOnMonsterDeath." + k, 0));
				}
				if (chanceToSpawnMap.isEmpty()) {
					chanceToSpawnMap.put("default", cs.getDouble("chanceToDropOnMonsterDeath"));
				}
				builder.withWorldSpawnChanceMap(chanceToSpawnMap);
			} else if (cs.isSet("chanceToDropOnMonsterDeath")) {
				Map<String, Double> chanceToSpawnMap = new HashMap<>();
				chanceToSpawnMap.put("default", cs.getDouble("chanceToDropOnMonsterDeath"));
				builder.withWorldSpawnChanceMap(chanceToSpawnMap);
			}

			Tier t = builder.build();
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
					prefixes.put(NameType.TIER_PREFIX + f.getName().replace(".txt", ""), prefixList);
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
					prefixes.put(NameType.MATERIAL_PREFIX + f.getName().replace(".txt", ""), prefixList);
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
					prefixes.put(NameType.ENCHANTMENT_PREFIX + f.getName().replace(".txt", ""), prefixList);
					numOfLoadedPrefixes += prefixList.size();
				}
			}
		}

		debugPrinter.debug(Level.INFO, "Loaded prefixes: " + numOfLoadedPrefixes);
		NameMap.getInstance().putAll(prefixes);
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
					suffixes.put(NameType.TIER_SUFFIX + f.getName().replace(".txt", ""), suffixList);
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
					suffixes.put(NameType.MATERIAL_SUFFIX + f.getName().replace(".txt", ""), suffixList);
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
					suffixes.put(NameType.ENCHANTMENT_SUFFIX + f.getName().replace(".txt", ""), suffixList);
					numOfLoadedSuffixes += suffixList.size();
				}
			}
		}

		debugPrinter.debug(Level.INFO, "Loaded suffixes: " + numOfLoadedSuffixes);
		NameMap.getInstance().putAll(suffixes);
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
					lore.put(NameType.TIER_LORE + f.getName().replace(".txt", ""), loreList);
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
					lore.put(NameType.MATERIAL_LORE + f.getName().replace(".txt", ""), loreList);
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
					lore.put(NameType.ENCHANTMENT_LORE + f.getName().replace(".txt", ""), loreList);
					numOfLoadedLore += loreList.size();
				}
			}
		}

		debugPrinter.debug(Level.INFO, "Loaded lore: " + numOfLoadedLore);
		NameMap.getInstance().putAll(lore);
	}

	@Override
	public CommandHandler getCommandHandler() {
		return commandHandler;
	}

	@Override
	public File getScriptsDirectory() {
		return scriptsDirectory;
	}
}
