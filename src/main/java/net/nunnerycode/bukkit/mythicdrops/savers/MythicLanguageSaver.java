package net.nunnerycode.bukkit.mythicdrops.savers;

import com.conventnunnery.libraries.config.ConventConfiguration;
import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.savers.ConfigSaver;

import java.util.Iterator;
import java.util.Map;

public class MythicLanguageSaver implements ConfigSaver {

	private MythicDropsPlugin plugin;

	public MythicLanguageSaver(MythicDropsPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public synchronized void save() {
		ConventConfiguration c = getPlugin().getLanguageYAML();
		if (c == null) {
			return;
		}
		Iterator<Map.Entry<String, String>> iterator = getPlugin().getMythicLanguageManager().getMessages().entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> entry = iterator.next();
			c.getFileConfiguration().set(entry.getKey(), entry.getValue());
		}
		c.save();
	}

	public MythicDropsPlugin getPlugin() {
		return plugin;
	}
}
