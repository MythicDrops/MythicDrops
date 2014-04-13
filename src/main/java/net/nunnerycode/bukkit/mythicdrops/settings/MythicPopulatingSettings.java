package net.nunnerycode.bukkit.mythicdrops.settings;

import net.nunnerycode.bukkit.mythicdrops.api.populating.PopulateWorld;
import net.nunnerycode.bukkit.mythicdrops.api.settings.PopulatingSettings;

import java.util.HashMap;
import java.util.Map;

public final class MythicPopulatingSettings implements PopulatingSettings {

    private Map<String, PopulateWorld> populateWorldMap;

    public MythicPopulatingSettings() {
        populateWorldMap = new HashMap<>();
    }

    @Override
    public PopulateWorld getWorld(String name) {
        if (populateWorldMap.containsKey(name.toLowerCase())) {
            return populateWorldMap.get(name.toLowerCase());
        }
        return null;
    }

    public void addWorld(String name, PopulateWorld world) {
        populateWorldMap.put(name.toLowerCase(), world);
    }

}
