package net.nunnerycode.bukkit.mythicdrops.api.settings;

import net.nunnerycode.bukkit.mythicdrops.api.repair.RepairItem;

import java.util.Map;

public interface RepairingSettings {

  boolean isCancelMcMMORepair();

  @Deprecated
  boolean isEnabled();

  boolean isPlaySounds();

  Map<String, RepairItem> getRepairItemMap();

}
