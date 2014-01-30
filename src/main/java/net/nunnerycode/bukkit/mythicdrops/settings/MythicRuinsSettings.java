package net.nunnerycode.bukkit.mythicdrops.settings;

import net.nunnerycode.bukkit.mythicdrops.api.settings.RuinsSettings;

import java.util.HashMap;
import java.util.Map;

public final class MythicRuinsSettings implements RuinsSettings {

  private boolean enabled;
  private Map<String, Double> chanceToSpawnMap;

  public MythicRuinsSettings() {
    chanceToSpawnMap = new HashMap<>();
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public double getChanceToSpawn(String worldName) {
    return chanceToSpawnMap.containsKey(worldName) ? chanceToSpawnMap.get(worldName) : 0;
  }

  public void setChanceToSpawn(String worldName, double chance) {
    chanceToSpawnMap.put(worldName, chance);
  }

}
