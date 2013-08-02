package net.nunnerycode.bukkit.mythicdrops.module.wrappers;

public class ModuleInfo {

    private final String name, author, description, version;


    public ModuleInfo(final String name, final String author, final String description, final String version) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }
}
