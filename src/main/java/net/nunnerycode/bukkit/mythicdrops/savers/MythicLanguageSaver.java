package net.nunnerycode.bukkit.mythicdrops.savers;

import com.conventnunnery.libraries.config.ConventConfiguration;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.utils.MythicSaver;

import java.util.Map;

public class MythicLanguageSaver implements MythicSaver {

	private MythicDrops plugin;

	public MythicLanguageSaver(MythicDrops plugin) {
		this.plugin = plugin;
	}

	@Override
	public void save() {
		ConventConfiguration c = getPlugin().getLanguageYAML();
		if (c == null) {
			return;
		}
		for (Map.Entry<String, String> key : getPlugin().getLanguageManager().getMessages().entrySet()) {
			c.getFileConfiguration().set(key.getKey(), key.getValue());
		}
		c.save();
	}

	public MythicDrops getPlugin() {
		return plugin;
	}
}
