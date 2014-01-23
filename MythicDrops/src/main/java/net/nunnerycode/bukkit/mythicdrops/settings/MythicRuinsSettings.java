package net.nunnerycode.bukkit.mythicdrops.settings;

import net.nunnerycode.bukkit.mythicdrops.api.settings.RuinsSettings;

public class MythicRuinsSettings implements RuinsSettings {

	private boolean enabled;
	private double chanceToSpawn;

	public MythicRuinsSettings() {
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public double getChanceToSpawn() {
		return chanceToSpawn;
	}

	public void setChanceToSpawn(double chanceToSpawn) {
		this.chanceToSpawn = chanceToSpawn;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
