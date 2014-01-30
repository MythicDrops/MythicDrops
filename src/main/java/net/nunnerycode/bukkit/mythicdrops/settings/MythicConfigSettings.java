package net.nunnerycode.bukkit.mythicdrops.settings;

import net.nunnerycode.bukkit.mythicdrops.api.settings.ConfigSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MythicConfigSettings implements ConfigSettings {

  private final List<String> armorTypes;
  private final List<String> toolTypes;
  private final List<String> materialTypes;
  private final Map<String, List<String>> itemTypesWithIds;
  private final Map<String, List<String>> materialTypesWithIds;
  private final List<String> tooltipFormat;
  private final Map<String, String> language;
  private boolean debugMode;
  private String itemDisplayNameFormat;
  private boolean randomLoreEnabled;
  private double randomLoreChance;
  private boolean reportingEnabled;

  public MythicConfigSettings() {
    armorTypes = new ArrayList<>();
    toolTypes = new ArrayList<>();
    materialTypes = new ArrayList<>();
    itemTypesWithIds = new HashMap<>();
    materialTypesWithIds = new HashMap<>();
    tooltipFormat = new ArrayList<>();
    language = new HashMap<>();
  }

  public Map<String, String> getLanguageMap() {
    return language;
  }

  @Override
  public List<String> getArmorTypes() {
    return armorTypes;
  }

  @Override
  public List<String> getToolTypes() {
    return toolTypes;
  }

  @Override
  public List<String> getMaterialTypes() {
    return materialTypes;
  }

  @Override
  public Map<String, List<String>> getItemTypesWithIds() {
    return itemTypesWithIds;
  }

  @Override
  public Map<String, List<String>> getMaterialTypesWithIds() {
    return materialTypesWithIds;
  }

  @Override
  public boolean isDebugMode() {
    return debugMode;
  }

  @Override
  public String getItemDisplayNameFormat() {
    return itemDisplayNameFormat;
  }

  @Override
  public boolean isRandomLoreEnabled() {
    return randomLoreEnabled;
  }

  @Override
  public double getRandomLoreChance() {
    return randomLoreChance;
  }

  @Override
  public List<String> getTooltipFormat() {
    return tooltipFormat;
  }

  @Override
  public String getLanguageString(String key) {
    return language.containsKey(key) ? language.get(key) : key;
  }

  @Override
  public String getLanguageString(String key, String[][] args) {
    String s = getLanguageString(key);
    for (String[] arg : args) {
      s = s.replace(arg[0], arg[1]);
    }
    return s;
  }

  @Override
  public String getFormattedLanguageString(String key) {
    return getLanguageString(key).replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
  }

  @Override
  public String getFormattedLanguageString(String key, String[][] args) {
    String s = getFormattedLanguageString(key);
    for (String[] arg : args) {
      s = s.replace(arg[0], arg[1]);
    }
    return s;
  }

  public void setRandomLoreChance(double randomLoreChance) {
    this.randomLoreChance = randomLoreChance;
  }

  public void setRandomLoreEnabled(boolean randomLoreEnabled) {
    this.randomLoreEnabled = randomLoreEnabled;
  }

  public void setItemDisplayNameFormat(String itemDisplayNameFormat) {
    this.itemDisplayNameFormat = itemDisplayNameFormat;
  }

  public void setDebugMode(boolean debugMode) {
    this.debugMode = debugMode;
  }

  @Override
  public boolean isReportingEnabled() {
    return reportingEnabled;
  }

  public void setReportingEnabled(boolean reportingEnabled) {
    this.reportingEnabled = reportingEnabled;
  }
}
