package net.nunnerycode.bukkit.mythicdrops;

import com.conventnunnery.libraries.config.CommentedConventYamlConfiguration;
import com.conventnunnery.libraries.config.ConventYamlConfiguration;
import com.modcrafting.diablodrops.name.NamesLoader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.enchantments.MythicEnchantment;
import net.nunnerycode.bukkit.mythicdrops.api.settings.ConfigSettings;
import net.nunnerycode.bukkit.mythicdrops.items.TierMap;
import net.nunnerycode.bukkit.mythicdrops.settings.MythicConfigSettings;
import net.nunnerycode.bukkit.mythicdrops.tiers.MythicTierBuilder;
import net.nunnerycode.bukkit.mythicdrops.utils.ChatColorUtils;
import net.nunnerycode.java.libraries.cannonball.DebugPrinter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

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
				YamlConfiguration.loadConfiguration(getResource("config.yml")).getString("version"));
		tierYAML.options().backupOnUpdate(true);
		tierYAML.options().updateOnLoad(true);
		tierYAML.load();

		writeResourceFiles();

		reloadSettings();
		reloadTiers();
		reloadCustomItems();
		reloadNames();
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
			MythicTierBuilder builder = new MythicTierBuilder(key);
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

			TierMap.getInstance().put(key, builder.build());
			loadedTierNames.add(key);
		}

		debugPrinter.debug(Level.INFO, "Loaded tiers: " + loadedTierNames.toString());
	}

	@Override
	public void reloadCustomItems() {

	}

	@Override
	public void reloadNames() {

	}
}
