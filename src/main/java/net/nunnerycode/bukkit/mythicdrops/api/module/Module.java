package net.nunnerycode.bukkit.mythicdrops.api.module;

import com.conventnunnery.libraries.config.ConventYamlConfiguration;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.module.wrappers.ModuleInfo;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

import java.io.File;

public class Module implements Listener {

    private FileConfiguration fileConfiguration;
    private boolean enabled, permDisabled;

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public boolean isPermanentlyDisabled() {
        return permDisabled;
    }

    public void setPermanentlyDisabled(final boolean permDisabled) {
        this.permDisabled = permDisabled;
    }

    public boolean isEnabled() {
        return enabled && !isPermanentlyDisabled();
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            setPermanentlyDisabled(false);
        }
    }

    protected void initialize(MythicDrops plugin, ModuleInfo info) throws IllegalStateException {
        if (plugin.getModuleManager().getModules().contains(this)) {
            throw new IllegalStateException(info.getName() + " is already a loaded Module");
        }
        fileConfiguration = ConventYamlConfiguration.loadConfiguration(new File(MythicDrops.instance.getDataFolder(),
                "/modules/" + this.getClass().getName() + ".yml"));
    }
}
