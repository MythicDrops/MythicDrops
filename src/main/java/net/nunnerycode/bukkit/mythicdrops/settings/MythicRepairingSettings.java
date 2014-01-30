package net.nunnerycode.bukkit.mythicdrops.settings;

import net.nunnerycode.bukkit.mythicdrops.api.repair.RepairItem;
import net.nunnerycode.bukkit.mythicdrops.api.settings.RepairingSettings;

import java.util.HashMap;
import java.util.Map;

public final class MythicRepairingSettings implements RepairingSettings {

  private boolean enabled;
  private boolean playSounds;
  private Map<String, RepairItem> repairItemMap;

  public MythicRepairingSettings() {
    repairItemMap = new HashMap<>();
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public boolean isPlaySounds() {
    return playSounds;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public void setPlaySounds(boolean playSounds) {
    this.playSounds = playSounds;
  }

  @Override
  public Map<String, RepairItem> getRepairItemMap() {
    return repairItemMap;
  }

}
