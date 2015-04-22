package com.tealcube.minecraft.bukkit.mythicdrops.settings;

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.RelationSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MythicRelationSettings implements RelationSettings {

    private Map<String, List<String>> nameRelationMap;

    public MythicRelationSettings() {
        this.nameRelationMap = new HashMap<>();
    }

    @Override
    public List<String> getLoreFromName(String name) {
        return nameRelationMap.containsKey(name) ? nameRelationMap.get(name) : new ArrayList<String>();
    }

    public void setLoreFromName(String name, List<String> lore) {
        nameRelationMap.put(name, lore);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
