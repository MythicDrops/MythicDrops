package net.nunnerycode.bukkit.mythicdrops.api.module;

import com.conventnunnery.libraries.config.ConventYamlConfiguration;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

import java.io.File;

public class Module implements Listener {

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    private FileConfiguration fileConfiguration;

    public Module() {
        fileConfiguration = ConventYamlConfiguration.loadConfiguration(new File(MythicDrops.instance.getDataFolder(),
                "/modules/" + this.getClass().getName() + ".yml"));
    }


}
