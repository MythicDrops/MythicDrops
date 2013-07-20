package net.nunnerycode.bukkit.mythicdrops.loaders;

import com.conventnunnery.libraries.config.ConventYamlConfiguration;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.utils.MythicLoader;
import net.nunnerycode.bukkit.mythicdrops.configuration.MythicConfigurationFile;

public class MythicLanguageLoader implements MythicLoader {

    private final MythicDrops plugin;

    public MythicLanguageLoader(final MythicDrops plugin) {
        this.plugin = plugin;
    }

    public MythicDrops getPlugin() {
        return plugin;
    }

    @Override
    public void load() {
        ConventYamlConfiguration configuration = getPlugin().getConfigurationManager().getConfiguration
                (MythicConfigurationFile.LANGUAGE);
        for (String key : configuration.getKeys(true)) {
            if (configuration.isConfigurationSection(key) || key.equalsIgnoreCase("version")) {
                continue;
            }
            getPlugin().getLanguageManager().getMessages().put(key.toLowerCase(),configuration.getString(key, key));
        }
    }
}
