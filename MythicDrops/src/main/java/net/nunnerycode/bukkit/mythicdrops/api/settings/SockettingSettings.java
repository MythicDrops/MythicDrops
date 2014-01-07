package net.nunnerycode.bukkit.mythicdrops.api.settings;

import org.bukkit.material.MaterialData;

import java.util.List;

public interface SockettingSettings {

	String getSocketGemName();

	List<String> getSocketGemLore();

	String getSockettedItemString();

	List<String> getSockettedItemLore();

	boolean isUseAttackerItemInHand();

	boolean isUseAttackerArmorEquipped();

	boolean isUseDefenderItemInHand();

	boolean isUseDefenderArmorEquipped();

	double getSocketGemChanceToSpawn();

	boolean isPreventMultipleChangesFromSockets();

	List<MaterialData> getSocketGemMaterialDatas();

	List<String> getSocketGemPrefixes();

	List<String> getSocketGemSuffixes();

}
