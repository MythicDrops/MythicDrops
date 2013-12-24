package net.nunnerycode.bukkit.mythicdrops.sockets;

import com.conventnunnery.libraries.config.CommentedConventYamlConfiguration;
import com.conventnunnery.libraries.config.ConventYamlConfiguration;
import net.nunnerycode.java.libraries.cannonball.DebugPrinter;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Effect;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class MythicDropsSockets extends JavaPlugin {

	private static MythicDropsSockets _INSTANCE;
	private DebugPrinter debugPrinter;
	private Map<String, String> language;
	private String socketGemName;
	private List<String> socketGemLore;
	private String sockettedItemSocket;
	private List<String> sockettedItemLore;
	private ConventYamlConfiguration configYAML;
	private ConventYamlConfiguration socketGemsYAML;
	private boolean useAttackerItemInHand;
	private boolean useAttackerArmorEquipped;
	private boolean useDefenderItemInHand;
	private boolean useDefenderArmorEquipped;
	private double socketGemChanceToSpawn;
	private List<MaterialData> socketGemMaterialIds;
	private Map<String, SocketGem> socketGemMap;
	private List<String> socketGemPrefixes;
	private List<String> socketGemSuffixes;

	public static MythicDropsSockets getInstance() {
		return _INSTANCE;
	}

	public List<MaterialData> getSocketGemMaterialIds() {
		return socketGemMaterialIds;
	}

	public boolean isUseAttackerItemInHand() {
		return useAttackerItemInHand;
	}

	public boolean isUseAttackerArmorEquipped() {
		return useAttackerArmorEquipped;
	}

	public boolean isUseDefenderItemInHand() {
		return useDefenderItemInHand;
	}

	public boolean isUseDefenderArmorEquipped() {
		return useDefenderArmorEquipped;
	}

	public String getSockettedItemSocket() {
		return sockettedItemSocket;
	}

	public List<String> getSockettedItemLore() {
		return sockettedItemLore;
	}

	public ConventYamlConfiguration getConfigYAML() {
		return configYAML;
	}

	public ConventYamlConfiguration getSocketGemsYAML() {
		return socketGemsYAML;
	}

	@Override
	public void onDisable() {
		debugPrinter.debug(Level.INFO, "v" + getDescription().getVersion() + " disabled");
	}

	@Override
	public void onEnable() {
		debugPrinter = new DebugPrinter(getDataFolder().getPath(), "debug.log");

		language = new HashMap<>();
		socketGemMaterialIds = new ArrayList<>();
		socketGemMap = new HashMap<>();
		socketGemPrefixes = new ArrayList<>();
		socketGemSuffixes = new ArrayList<>();

		unpackConfigurationFiles(new String[]{"config.yml", "socketGems.yml"}, false);

		configYAML = new ConventYamlConfiguration(new File(getDataFolder(), "config.yml"),
				YamlConfiguration.loadConfiguration(getResource("config.yml")).getString("version"));
		configYAML.options().backupOnUpdate(true);
		configYAML.options().updateOnLoad(true);
		configYAML.load();

		socketGemsYAML = new ConventYamlConfiguration(new File(getDataFolder(), "socketGems.yml"),
				YamlConfiguration.loadConfiguration(getResource("socketGems.yml")).getString("version"));
		socketGemsYAML.options().backupOnUpdate(true);
		socketGemsYAML.options().updateOnLoad(true);
		socketGemsYAML.load();

		loadSettings();
		loadGems();

		debugPrinter.debug(Level.INFO, "v" + getDescription().getVersion() + " enabled");
	}

	private void loadGems() {
		socketGemMap.clear();
		List<String> loadedSocketGems = new ArrayList<>();
		for (String key : socketGemsYAML.getKeys(false)) {
			if (!socketGemsYAML.isConfigurationSection(key)) {
				continue;
			}
			ConfigurationSection gemCS = socketGemsYAML.getConfigurationSection(key);
			GemType gemType = GemType.getFromName(gemCS.getString("type"));
			if (gemType == null) {
				gemType = GemType.ANY;
			}
			List<SocketPotionEffect> socketPotionEffects = buildSocketPotionEffects(gemCS);
			List<SocketParticleEffect> socketParticleEffects = buildSocketParticleEffects(gemCS);
			double chance = gemCS.getDouble("chance");
			String prefix = gemCS.getString("prefix");
			if (prefix != null && !prefix.equalsIgnoreCase("")) {
				socketGemPrefixes.add(prefix);
			}
			String suffix = gemCS.getString("suffix");
			if (suffix != null && !suffix.equalsIgnoreCase("")) {
				socketGemSuffixes.add(suffix);
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
			SocketGem sg = new SocketGem(key, gemType, socketPotionEffects, socketParticleEffects, chance, prefix,
					suffix, lore, enchantments, socketCommands);
			socketGemMap.put(key, sg);
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

	private void loadSettings() {
		useAttackerItemInHand = configYAML.getBoolean("options.use-attacker-item-in-hand", true);
		useAttackerArmorEquipped = configYAML.getBoolean("options.use-attacker-armor-equipped", false);
		useDefenderItemInHand = configYAML.getBoolean("options.use-defender-item-in-hand", false);
		useDefenderArmorEquipped = configYAML.getBoolean("options.use-defender-armor-equipped", true);
		socketGemChanceToSpawn = configYAML.getDouble("options.socket-gem-chance-to-spawn", 0.25);

		List<String> socketGemMats = configYAML.getStringList("options.socket-gem-material-ids");
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
			socketGemMaterialIds.add(new MaterialData(id, data));
		}

		socketGemName = configYAML.getString("items.socket-name", "&6Socket Gem - %socketgem%");
		socketGemLore = configYAML.getStringList("items.socket-lore");
		sockettedItemSocket = configYAML.getString("items.socketted-item-socket", "&6(Socket)");
		sockettedItemLore = configYAML.getStringList("items.socketted-item-lore");

		language.clear();
		for (String key : configYAML.getConfigurationSection("language").getKeys(true)) {
			if (configYAML.getConfigurationSection("language").isConfigurationSection(key)) {
				continue;
			}
			language.put(key, configYAML.getConfigurationSection("language").getString(key, key));
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

	public String getLanguageString(String key, String[][] args) {
		String s = getLanguageString(key);
		for (String[] arg : args) {
			s = s.replace(arg[0], arg[1]);
		}
		return s;
	}

	public String getLanguageString(String key) {
		return language.containsKey(key) ? language.get(key) : key;
	}

	public String getFormattedLanguageString(String key, String[][] args) {
		String s = getFormattedLanguageString(key);
		for (String[] arg : args) {
			s = s.replace(arg[0], arg[1]);
		}
		return s;
	}

	public String getFormattedLanguageString(String key) {
		return getLanguageString(key).replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
	}

	public List<String> replaceArgs(List<String> strings, String[][] args) {
		List<String> list = new ArrayList<>();
		for (String s : strings) {
			list.add(replaceArgs(s, args));
		}
		return list;
	}

	public String replaceArgs(String string, String[][] args) {
		String s = string;
		for (String[] arg : args) {
			s = s.replace(arg[0], arg[1]);
		}
		return s;
	}

	public String getSocketGemName() {
		return socketGemName;
	}

	public List<String> getSocketGemLore() {
		return socketGemLore;
	}

	public double getSocketGemChanceToSpawn() {
		return socketGemChanceToSpawn;
	}
}
