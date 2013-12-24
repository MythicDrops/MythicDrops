package net.nunnerycode.bukkit.mythicdrops.sockets;

import com.conventnunnery.libraries.config.CommentedConventYamlConfiguration;
import net.nunnerycode.java.libraries.cannonball.DebugPrinter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

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

	public static MythicDropsSockets getInstance() {
		return _INSTANCE;
	}

	@Override
	public void onDisable() {
		debugPrinter.debug(Level.INFO, "v" + getDescription().getVersion() + " disabled");
	}

	@Override
	public void onEnable() {
		debugPrinter = new DebugPrinter(getDataFolder().getPath(), "debug.log");

		language = new HashMap<>();

		debugPrinter.debug(Level.INFO, "v" + getDescription().getVersion() + " enabled");
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

	public String replaceArgs(String string, String[][] args) {
		String s = string;
		for (String[] arg : args) {
			s = s.replace(arg[0], arg[1]);
		}
		return s;
	}

	public List<String> replaceArgs(List<String> strings, String[][] args) {
		List<String> list = new ArrayList<>();
		for (String s : strings) {
			list.add(replaceArgs(s, args));
		}
		return list;
	}

}
