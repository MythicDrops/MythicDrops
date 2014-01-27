package net.nunnerycode.bukkit.mythicdrops.settings;

import net.nunnerycode.bukkit.mythicdrops.api.armorsets.ArmorSet;
import net.nunnerycode.bukkit.mythicdrops.api.settings.ArmorSetsSettings;

import java.util.HashMap;
import java.util.Map;

public final class MythicArmorSetsSettings implements ArmorSetsSettings {

	private boolean enabled;
	private Map<String, ArmorSet> armorSetMap;
	private String setIdentifier;

	public MythicArmorSetsSettings() {
		armorSetMap = new HashMap<>();
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public Map<String, ArmorSet> getArmorSetMap() {
		return armorSetMap;
	}

	@Override
	public String getSetIdentifier() {
		return setIdentifier;
	}

	public void setSetIdentifier(String setIdentifier) {
		this.setIdentifier = setIdentifier;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
