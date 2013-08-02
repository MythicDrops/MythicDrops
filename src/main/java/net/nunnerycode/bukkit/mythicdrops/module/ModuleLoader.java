package net.nunnerycode.bukkit.mythicdrops.module;

import me.sirrus86.s86powers.tools.utils.IOHelper;
import net.nunnerycode.bukkit.mythicdrops.api.module.Module;
import net.nunnerycode.bukkit.mythicdrops.api.module.ModuleManifest;
import net.nunnerycode.bukkit.mythicdrops.module.wrappers.ModuleDefinition;
import net.nunnerycode.bukkit.mythicdrops.module.wrappers.ModuleInfo;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModuleLoader {

    private final File[] files;

    public ModuleLoader(final File... file) {
        files = file;
    }

    public LinkedList<ModuleDefinition> list() {
        final LinkedList<ModuleDefinition> defs = new LinkedList<ModuleDefinition>();
        for (final File file : files) {
            list(file, defs);
        }
        return defs;
    }

    private void list(final File file, final LinkedList<ModuleDefinition> defs) {
        if (file != null) {
            if (file.isDirectory()) {
                try {
                    final ClassLoader loader = new ModuleClassLoader(file.toURI().toURL());
                    for (final File item : file.listFiles()) {
                        load(item, defs, loader);
                    }
                } catch (final IOException ignored) {
                }
            } else if (IOHelper.isJar(file)) {
                try {
                    final ClassLoader ldr = new ModuleClassLoader(IOHelper.getJarUrl(file));
                    load(ldr, defs, new JarFile(file));
                } catch (final IOException ignored) {
                }
            }
        }
    }

    public void load(final File file, final LinkedList<ModuleDefinition> defs, ClassLoader loader) throws IOException {
        if (IOHelper.isJar(file)) {
            load(new ModuleClassLoader(IOHelper.getJarUrl(file)), defs, new JarFile(file));
        } else {
            if (loader == null) {
                loader = new ModuleClassLoader(file.getParentFile().toURI().toURL());
            }
            load(loader, defs, file, "");
        }
    }

    private void load(final ClassLoader loader, final LinkedList<ModuleDefinition> Modules, final JarFile jar) {
        final Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            final JarEntry e = entries.nextElement();
            final String name = e.getName().replace('/', '.');
            final String ext = ".class";
            if (name.endsWith(ext)
                    && !name.contains("$")) {
                load(loader, Modules, name.substring(0, name.length() - ext.length()), jar.getName());
            }
        }
    }

    private void load(final ClassLoader loader, final LinkedList<ModuleDefinition> Modules, final File file, final String prefix) {
        if (file.isDirectory()) {
            if (!file.getName().startsWith(".")) {
                for (final File f : file.listFiles()) {
                    load(loader, Modules, f, prefix + file.getName() + ".");
                }
            }
        } else {
            String name = prefix + file.getName();
            final String ext = ".class";
            if (name.endsWith(ext)
                    && !name.startsWith(".")
                    && !name.contains("!")
                    && !name.contains("$")) {
                name = name.substring(0, name.length() - ext.length());
                load(loader, Modules, name, file.getAbsolutePath());
            }
        }
    }

    private void load(final ClassLoader loader, final LinkedList<ModuleDefinition> Modules, final String name, final String path) {
        Class<?> clazz;
        try {
            clazz = loader.loadClass(name);
        } catch (Exception e) {
            return;
        } catch (Error e) {
            return;
        }
        if (clazz != null
                && clazz.isAnnotationPresent(ModuleManifest.class)) {
            final ModuleManifest manifest = clazz
                    .getAnnotation(ModuleManifest.class);
            if (manifest == null
                    || manifest.author().equalsIgnoreCase("")
                    || manifest.name().equalsIgnoreCase("")) {
                return;
            }
            final ModuleDefinition def = new ModuleDefinition(clazz, this,
                    new ModuleInfo(manifest.name(), manifest.author(), manifest.description(),
                            manifest.version()));
            Modules.add(def);
            return;
        }
    }

    public Module load(final ModuleDefinition def)
            throws InstantiationException, IllegalAccessException {
        if (def.getModuleClass().getSuperclass().equals(Module.class)) {
            return def.getModuleClass().asSubclass(Module.class).newInstance();
        }
        return null;
    }

}
