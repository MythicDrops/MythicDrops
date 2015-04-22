package com.tealcube.minecraft.bukkit.mythicdrops.api.settings;

import java.util.List;

public interface RelationSettings {

    List<String> getLoreFromName(String name);

    @Deprecated
    boolean isEnabled();

}
