package net.nunnerycode.bukkit.mythicdrops.managers;

import net.nunnerycode.bukkit.libraries.module.ModuleDefinition;
import net.nunnerycode.bukkit.libraries.module.ModuleLoader;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class ModuleManager {

    private final MythicDrops plugin;
    private List<ModuleDefinition> modules;
    private final File moduleFolder;

    public ModuleManager(final MythicDrops plugin) {
        this.plugin = plugin;
        moduleFolder = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "/modules");
        modules = cleanList(new ArrayList<ModuleDefinition>(new ModuleLoader(getModuleFolder()).list()));
    }

    private List<ModuleDefinition> cleanList(List<ModuleDefinition> list) {
        List<ModuleDefinition> internal = new ArrayList<ModuleDefinition>(new ModuleLoader(plugin.getJar()).list());
        for (ModuleDefinition def : internal) {
            for (int i = 0; i < list.size(); i ++) {
                if (list.get(i).getModuleInfo().getName().equalsIgnoreCase(def.getModuleInfo().getName())) {
                    list.remove(i);
                }
            }
            list.add(def);
        }
        return list;
    }

    public MythicDrops getPlugin() {
        return plugin;
    }

    public List<ModuleDefinition> getModules() {
        return modules;
    }

    public File getModuleFolder() {
        return moduleFolder;
    }
}
