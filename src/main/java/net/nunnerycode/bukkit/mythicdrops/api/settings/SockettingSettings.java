package net.nunnerycode.bukkit.mythicdrops.api.settings;

import net.nunnerycode.bukkit.mythicdrops.socketting.SocketGem;

import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public interface SockettingSettings {

  @Deprecated
  boolean isEnabled();

  String getSocketGemName();

  List<String> getSocketGemLore();

  String getSockettedItemString();

  List<String> getSockettedItemLore();

  boolean isUseAttackerItemInHand();

  boolean isUseAttackerArmorEquipped();

  boolean isUseDefenderItemInHand();

  boolean isUseDefenderArmorEquipped();

  boolean isPreventMultipleChangesFromSockets();

  List<Material> getSocketGemMaterials();

  Map<String, SocketGem> getSocketGemMap();

  List<String> getSocketGemPrefixes();

  List<String> getSocketGemSuffixes();

}
