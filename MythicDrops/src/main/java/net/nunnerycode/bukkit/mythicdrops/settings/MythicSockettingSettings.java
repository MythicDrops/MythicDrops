package net.nunnerycode.bukkit.mythicdrops.settings;

import net.nunnerycode.bukkit.mythicdrops.api.settings.SockettingSettings;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketGem;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MythicSockettingSettings implements SockettingSettings {

	private boolean enabled;
	private String socketGemName;
	private List<String> socketGemLore;
	private String sockettedItemString;
	private List<String> sockettedItemLore;
	private boolean useAttackerItemInHand;
	private boolean useAttackerArmorEquipped;
	private boolean useDefenderItemInHand;
	private boolean useDefenderArmorEquipped;
	private double socketGemChanceToSpawn;
	private List<MaterialData> socketGemMaterialDatas;
	private Map<String, SocketGem> socketGemMap;
	private List<String> socketGemPrefixes;
	private boolean preventMultipleChangesFromSockets;
	private List<String> socketGemSuffixes;

	public MythicSockettingSettings() {
		socketGemLore = new ArrayList<>();
		sockettedItemLore = new ArrayList<>();
		socketGemMaterialDatas = new ArrayList<>();
		socketGemMap = new HashMap<>();
		socketGemPrefixes = new ArrayList<>();
		socketGemSuffixes = new ArrayList<>();
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String getSocketGemName() {
		return socketGemName;
	}

	public void setSocketGemName(String socketGemName) {
		this.socketGemName = socketGemName;
	}

	@Override
	public List<String> getSocketGemLore() {
		return socketGemLore;
	}

	public void setSocketGemLore(List<String> socketGemLore) {
		this.socketGemLore = socketGemLore;
	}

	@Override
	public String getSockettedItemString() {
		return sockettedItemString;
	}

	public void setSockettedItemString(String sockettedItemString) {
		this.sockettedItemString = sockettedItemString;
	}

	@Override
	public List<String> getSockettedItemLore() {
		return sockettedItemLore;
	}

	public void setSockettedItemLore(List<String> sockettedItemLore) {
		this.sockettedItemLore = sockettedItemLore;
	}

	@Override
	public boolean isUseAttackerItemInHand() {
		return useAttackerItemInHand;
	}

	public void setUseAttackerItemInHand(boolean useAttackerItemInHand) {
		this.useAttackerItemInHand = useAttackerItemInHand;
	}

	@Override
	public boolean isUseAttackerArmorEquipped() {
		return useAttackerArmorEquipped;
	}

	public void setUseAttackerArmorEquipped(boolean useAttackerArmorEquipped) {
		this.useAttackerArmorEquipped = useAttackerArmorEquipped;
	}

	@Override
	public boolean isUseDefenderItemInHand() {
		return useDefenderItemInHand;
	}

	public void setUseDefenderItemInHand(boolean useDefenderItemInHand) {
		this.useDefenderItemInHand = useDefenderItemInHand;
	}

	@Override
	public boolean isUseDefenderArmorEquipped() {
		return useDefenderArmorEquipped;
	}

	public void setUseDefenderArmorEquipped(boolean useDefenderArmorEquipped) {
		this.useDefenderArmorEquipped = useDefenderArmorEquipped;
	}

	@Override
	public double getSocketGemChanceToSpawn() {
		return socketGemChanceToSpawn;
	}

	public void setSocketGemChanceToSpawn(double socketGemChanceToSpawn) {
		this.socketGemChanceToSpawn = socketGemChanceToSpawn;
	}

	@Override
	public List<MaterialData> getSocketGemMaterialDatas() {
		return socketGemMaterialDatas;
	}

	public void setSocketGemMaterialDatas(List<MaterialData> socketGemMaterialDatas) {
		this.socketGemMaterialDatas = socketGemMaterialDatas;
	}

	@Override
	public Map<String, SocketGem> getSocketGemMap() {
		return socketGemMap;
	}

	public void setSocketGemMap(Map<String, SocketGem> socketGemMap) {
		this.socketGemMap = socketGemMap;
	}

	@Override
	public List<String> getSocketGemPrefixes() {
		return socketGemPrefixes;
	}

	@Override
	public boolean isPreventMultipleChangesFromSockets() {
		return preventMultipleChangesFromSockets;
	}

	public void setPreventMultipleChangesFromSockets(boolean preventMultipleChangesFromSockets) {
		this.preventMultipleChangesFromSockets = preventMultipleChangesFromSockets;
	}

	@Override
	public List<String> getSocketGemSuffixes() {
		return socketGemSuffixes;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
