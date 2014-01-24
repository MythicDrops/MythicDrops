package net.nunnerycode.bukkit.mythicdrops.api.settings;

import net.nunnerycode.bukkit.mythicdrops.api.armorsets.ArmorSet;

import java.util.Map;

public interface ArmorSetsSettings {

	boolean isEnabled();

	Map<String, ArmorSet> getArmorSetMap();

}
