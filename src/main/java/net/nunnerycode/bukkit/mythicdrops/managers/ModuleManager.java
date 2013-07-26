package net.nunnerycode.bukkit.mythicdrops.managers;

import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.module.Module;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class ModuleManager {

    public MythicDrops getPlugin() {
        return plugin;
    }

    private final Set<Module> moduleSet;
    private final MythicDrops plugin;

    public ModuleManager(final MythicDrops plugin) {
        this.plugin = plugin;
        moduleSet = new HashSet<Module>();
        loadModules();
    }

    private void loadModules() {
        File directory = new File(plugin.getDataFolder(), "/modules");
        if (!directory.exists()) {
            return;
        }
        for (File file : directory.listFiles()) {
            loadAndRegisterModule(file);
        }
    }

    private void registerModule(Module module) {
        plugin.getServer().getPluginManager().registerEvents(module, plugin);
    }

    private Module loadModule(File file) {
        File dir = file.getParentFile();
        if (!dir.exists()) {
            return null;
        }
        ClassLoader classLoader;
        try {
            classLoader = new URLClassLoader(new URL[] { dir.toURI().toURL() },
                    Module.class.getClassLoader());
        } catch (MalformedURLException ex) {
            return null;
        }
        if (!file.exists()) {
            return null;
        }
        if (!file.getName().endsWith(".class")) {
            return null;
        }
        String name1 = file.getName().substring(0,file.getName().lastIndexOf("."));
        try {
            Class<?> clazz = classLoader.loadClass(name1);
            Object object = clazz.newInstance();
            if (!(object instanceof Module)) {
                return null;
            }
            return (Module) object;
        } catch (Exception e) {
            return null;
        }
    }

    private Module loadModule(String directory, String name) {
        return loadModule(new File(directory, name));
    }

    public void loadAndRegisterModule(String directory, String name) {
        Module module = loadModule(directory, name);
        if (module == null) {
            return;
        }
        registerModule(module);
        plugin.debug(Level.INFO, "Loaded module: " + module.getClass().getName());
    }

    public void loadAndRegisterModule(File file) {
        Module module = loadModule(file);
        if (module == null) {
            return;
        }
        registerModule(module);
        plugin.debug(Level.INFO, "Loaded module: " + module.getClass().getName());
    }

}
