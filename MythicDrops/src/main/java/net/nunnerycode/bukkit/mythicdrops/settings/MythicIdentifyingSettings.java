package net.nunnerycode.bukkit.mythicdrops.settings;

import net.nunnerycode.bukkit.mythicdrops.api.settings.IdentifyingSettings;

import java.util.ArrayList;
import java.util.List;

public class MythicIdentifyingSettings implements IdentifyingSettings {

	private boolean enabled;
	private String identityTomeName;
	private List<String> identityTomeLore;
	private String unidentifiedItemName;
	private List<String> unidentifiedItemLore;
	private double unidentifiedItemChanceToSpawn;
	private double identityTomeChanceToSpawn;

	public MythicIdentifyingSettings() {
		identityTomeLore = new ArrayList<>();
		unidentifiedItemLore = new ArrayList<>();
	}

	@Override
	public String getIdentityTomeName() {
		return identityTomeName;
	}

	@Override
	public List<String> getIdentityTomeLore() {
		return identityTomeLore;
	}

	@Override
	public String getUnidentifiedItemName() {
		return unidentifiedItemName;
	}

	@Override
	public List<String> getUnidentifiedItemLore() {
		return unidentifiedItemLore;
	}

	@Override
	public double getUnidentifiedItemChanceToSpawn() {
		return unidentifiedItemChanceToSpawn;
	}

	@Override
	public double getIdentityTomeChanceToSpawn() {
		return identityTomeChanceToSpawn;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setIdentityTomeChanceToSpawn(double identityTomeChanceToSpawn) {
		this.identityTomeChanceToSpawn = identityTomeChanceToSpawn;
	}

	public void setUnidentifiedItemChanceToSpawn(double unidentifiedItemChanceToSpawn) {
		this.unidentifiedItemChanceToSpawn = unidentifiedItemChanceToSpawn;
	}

	public void setUnidentifiedItemLore(List<String> unidentifiedItemLore) {
		this.unidentifiedItemLore = unidentifiedItemLore;
	}

	public void setUnidentifiedItemName(String unidentifiedItemName) {
		this.unidentifiedItemName = unidentifiedItemName;
	}

	public void setIdentityTomeLore(List<String> identityTomeLore) {
		this.identityTomeLore = identityTomeLore;
	}

	public void setIdentityTomeName(String identityTomeName) {
		this.identityTomeName = identityTomeName;
	}
}
