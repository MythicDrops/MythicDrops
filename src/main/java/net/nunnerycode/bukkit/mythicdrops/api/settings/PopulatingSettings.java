package net.nunnerycode.bukkit.mythicdrops.api.settings;

import net.nunnerycode.bukkit.mythicdrops.api.populating.PopulateWorld;

public interface PopulatingSettings {

    PopulateWorld getWorld(String name);

}
