package net.nunnerycode.bukkit.mythicdrops.module.wrappers;

import net.nunnerycode.bukkit.mythicdrops.module.ModuleLoader;

public class ModuleDefinition {
    private final Class<?> clazz;
    private final ModuleLoader source;
    private final ModuleInfo info;

    public ModuleDefinition(final Class<?> clazz, final ModuleLoader source, final ModuleInfo info) {
        this.clazz = clazz;
        this.source = source;
        this.info = info;
    }

    public Class<?> getModuleClass() {
        return clazz;
    }

    public ModuleLoader getSource() {
        return source;
    }

    public ModuleInfo getModuleInfo() {
        return info;
    }
}
