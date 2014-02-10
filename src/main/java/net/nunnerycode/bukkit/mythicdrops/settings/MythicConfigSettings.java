package net.nunnerycode.bukkit.mythicdrops.settings;

import net.nunnerycode.bukkit.mythicdrops.api.settings.ConfigSettings;

import org.bukkit.Bukkit;

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
  private boolean reportingEnabled;
  private List<String> enabledWorlds;
  private boolean hookLeveledMobs;
  private boolean hookMcMMO;
  private boolean giveMobsNames;
  private boolean giveAllMobsNames;
  private boolean displayMobEquipment;
  private boolean mobsPickupEquipment;
  private boolean blankMobSpawnEnabled;
  private boolean skeletonsSpawnWithoutBows;
  private double randomItemChance;
  private double socketGemChance;
  private double identityTomeChance;
  private double unidentifiedItemChance;
  private boolean creatureSpawningEnabled;
  private boolean repairingEnabled;
  private boolean identifyingEnabled;
  private boolean sockettingEnabled;

  public MythicConfigSettings() {
    armorTypes = new ArrayList<>();
    toolTypes = new ArrayList<>();
    materialTypes = new ArrayList<>();
    itemTypesWithIds = new HashMap<>();
    materialTypesWithIds = new HashMap<>();
    tooltipFormat = new ArrayList<>();
    language = new HashMap<>();
    enabledWorlds = new ArrayList<>();
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
  public List<String> getTooltipFormat() {
    return tooltipFormat;
  }

  @Override
  public String getLanguageString(String key) {
    Bukkit.getLogger().info(key);
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

  @Override
  public List<String> getEnabledWorlds() {
    return enabledWorlds;
  }

  public void setEnabledWorlds(List<String> enabledWorlds) {
    this.enabledWorlds = enabledWorlds;
  }

  @Override
  public boolean isHookLeveledMobs() {
    return hookLeveledMobs;
  }

  public void setHookLeveledMobs(boolean hookLeveledMobs) {
    this.hookLeveledMobs = hookLeveledMobs;
  }

  @Override
  public boolean isHookMcMMO() {
    return hookMcMMO;
  }

  public void setHookMcMMO(boolean hookMcMMO) {
    this.hookMcMMO = hookMcMMO;
  }

  @Override
  public boolean isGiveMobsNames() {
    return giveMobsNames;
  }

  public void setGiveMobsNames(boolean giveMobsNames) {
    this.giveMobsNames = giveMobsNames;
  }

  @Override
  public boolean isGiveAllMobsNames() {
    return giveAllMobsNames;
  }

  public void setGiveAllMobsNames(boolean giveAllMobsNames) {
    this.giveAllMobsNames = giveAllMobsNames;
  }

  @Override
  public boolean isDisplayMobEquipment() {
    return displayMobEquipment;
  }

  public void setDisplayMobEquipment(boolean displayMobEquipment) {
    this.displayMobEquipment = displayMobEquipment;
  }

  @Override
  public boolean isMobsPickupEquipment() {
    return mobsPickupEquipment;
  }

  public void setMobsPickupEquipment(boolean mobsPickupEquipment) {
    this.mobsPickupEquipment = mobsPickupEquipment;
  }

  @Override
  public boolean isBlankMobSpawnEnabled() {
    return blankMobSpawnEnabled;
  }

  public void setBlankMobSpawnEnabled(boolean blankMobSpawnEnabled) {
    this.blankMobSpawnEnabled = blankMobSpawnEnabled;
  }

  @Override
  public boolean isSkeletonsSpawnWithoutBows() {
    return skeletonsSpawnWithoutBows;
  }

  public void setSkeletonsSpawnWithoutBows(boolean skeletonsSpawnWithoutBow) {
    this.skeletonsSpawnWithoutBows = skeletonsSpawnWithoutBow;
  }

  @Override
  public double getRandomItemChance() {
    return randomItemChance;
  }

  public void setRandomItemChance(double randomItemChance) {
    this.randomItemChance = randomItemChance;
  }

  @Override
  public double getSocketGemChance() {
    return socketGemChance;
  }

  public void setSocketGemChance(double socketGemChance) {
    this.socketGemChance = socketGemChance;
  }

  @Override
  public double getIdentityTomeChance() {
    return identityTomeChance;
  }

  public void setIdentityTomeChance(double identityTomeChance) {
    this.identityTomeChance = identityTomeChance;
  }

  @Override
  public double getUnidentifiedItemChance() {
    return unidentifiedItemChance;
  }

  public void setUnidentifiedItemChance(double unidentifiedItemChance) {
    this.unidentifiedItemChance = unidentifiedItemChance;
  }

  @Override
  public boolean isCreatureSpawningEnabled() {
    return creatureSpawningEnabled;
  }

  public void setCreatureSpawningEnabled(boolean creatureSpawningEnabled) {
    this.creatureSpawningEnabled = creatureSpawningEnabled;
  }

  @Override
  public boolean isRepairingEnabled() {
    return repairingEnabled;
  }

  public void setRepairingEnabled(boolean repairingEnabled) {
    this.repairingEnabled = repairingEnabled;
  }

  @Override
  public boolean isIdentifyingEnabled() {
    return identifyingEnabled;
  }

  public void setIdentifyingEnabled(boolean identifyingEnabled) {
    this.identifyingEnabled = identifyingEnabled;
  }

  @Override
  public boolean isSockettingEnabled() {
    return sockettingEnabled;
  }

  public void setSockettingEnabled(boolean sockettingEnabled) {
    this.sockettingEnabled = sockettingEnabled;
  }
}
