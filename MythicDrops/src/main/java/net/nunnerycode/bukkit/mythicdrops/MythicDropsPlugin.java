package net.nunnerycode.bukkit.mythicdrops;

import com.modcrafting.diablodrops.name.NamesLoader;
import java.io.File;
import net.nunnerycode.bukkit.libraries.config.CommentedNunneryYamlConfiguration;
import net.nunnerycode.bukkit.libraries.config.NunneryConfiguration;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.settings.ConfigSettings;
import net.nunnerycode.bukkit.mythicdrops.settings.MythicConfigSettings;
import net.nunnerycode.java.libraries.cannonball.DebugPrinter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class MythicDropsPlugin extends JavaPlugin implements MythicDrops {

	private static MythicDrops _INSTANCE;
	private ConfigSettings configSettings;
	private DebugPrinter debugPrinter;
	private CommentedNunneryYamlConfiguration configYAML;
	private CommentedNunneryYamlConfiguration customItemYAML;
	private CommentedNunneryYamlConfiguration itemGroupYAML;
	private CommentedNunneryYamlConfiguration languageYAML;
	private CommentedNunneryYamlConfiguration tierYAML;
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

		namesLoader.writeDefault("config.yml", false);
		namesLoader.writeDefault("customItems.yml", false);
		namesLoader.writeDefault("itemGroups.yml", false);
		namesLoader.writeDefault("language.yml", false);
		namesLoader.writeDefault("tier.yml", false);
		namesLoader.writeDefault("variables.txt", false);

		configYAML = new CommentedNunneryYamlConfiguration(new File(getDataFolder(), "config.yml"),
				YamlConfiguration.loadConfiguration(getResource("config.yml")).getString("version"));
		configYAML.options().backupOnUpdate(true);
		configYAML.options().updateOnLoad(true);
		configYAML.load();

		customItemYAML = new CommentedNunneryYamlConfiguration(new File(getDataFolder(), "customItems.yml"),
				YamlConfiguration.loadConfiguration(getResource("customItems.yml")).getString("version"));
		customItemYAML.options().backupOnUpdate(true);
		customItemYAML.options().updateOnLoad(true);
		customItemYAML.load();

		itemGroupYAML = new CommentedNunneryYamlConfiguration(new File(getDataFolder(), "itemGroup.yml"),
				YamlConfiguration.loadConfiguration(getResource("itemGroup.yml")).getString("version"));
		itemGroupYAML.options().backupOnUpdate(true);
		itemGroupYAML.options().updateOnLoad(true);
		itemGroupYAML.load();

		languageYAML = new CommentedNunneryYamlConfiguration(new File(getDataFolder(), "language.yml"),
				YamlConfiguration.loadConfiguration(getResource("language.yml")).getString("version"));
		languageYAML.options().backupOnUpdate(true);
		languageYAML.options().updateOnLoad(true);
		languageYAML.load();

		tierYAML = new CommentedNunneryYamlConfiguration(new File(getDataFolder(), "tier.yml"),
				YamlConfiguration.loadConfiguration(getResource("config.yml")).getString("version"));
		tierYAML.options().backupOnUpdate(true);
		tierYAML.options().updateOnLoad(true);
		tierYAML.load();

		writeResourceFiles();
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
	public NunneryConfiguration getConfigYAML() {
		return configYAML;
	}

	@Override
	public NunneryConfiguration getCustomItemYAML() {
		return customItemYAML;
	}

	@Override
	public NunneryConfiguration getItemGroupYAML() {
		return itemGroupYAML;
	}

	@Override
	public NunneryConfiguration getLanguageYAML() {
		return languageYAML;
	}

	@Override
	public NunneryConfiguration getTierYAML() {
		return tierYAML;
	}
}
