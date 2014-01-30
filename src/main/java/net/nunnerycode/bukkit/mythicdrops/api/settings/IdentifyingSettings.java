package net.nunnerycode.bukkit.mythicdrops.api.settings;

import java.util.List;

public interface IdentifyingSettings {


  String getIdentityTomeName();

  List<String> getIdentityTomeLore();

  String getUnidentifiedItemName();

  List<String> getUnidentifiedItemLore();

  double getUnidentifiedItemChanceToSpawn();

  double getIdentityTomeChanceToSpawn();

  boolean isEnabled();
}
