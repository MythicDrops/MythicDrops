package net.nunnerycode.bukkit.mythicdrops.tiers;

import net.nunnerycode.bukkit.mythicdrops.api.enchantments.MythicEnchantment;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class MythicTier implements Tier {

  private final String name;
  private String displayName;
  private ChatColor displayColor;
  private ChatColor identificationColor;
  private List<String> baseLore;
  private List<String> bonusLore;
  private int minimumBonusLore;
  private int maximumBonusLore;
  private Set<MythicEnchantment> baseEnchantments;
  private Set<MythicEnchantment> bonusEnchantments;
  private boolean safeBaseEnchantments;
  private boolean safeBonusEnchantments;
  private boolean allowHighBaseEnchantments;
  private boolean allowHighBonusEnchantments;
  private int minimumBonusEnchantments;
  private int maximumBonusEnchantments;
  private double minimumDurabilityPercentage;
  private double maximumDurabilityPercentage;
  private Map<String, Double> worldDropChanceMap;
  private Map<String, Double> worldSpawnChanceMap;
  private List<String> allowedItemGroups;
  private List<String> disallowedItemGroups;
  private List<String> allowedItemIds;
  private List<String> disallowedItemIds;
  private int minimumSockets;
  private int maximumSockets;
  private Map<String, Double> worldIdentifyChanceMap;
  private double chanceToHaveSockets;
  private boolean broadcastOnFind;

  protected MythicTier(String name) {
    this.name = name;
    baseLore = new ArrayList<>();
    bonusLore = new ArrayList<>();
    baseEnchantments = new HashSet<>();
    bonusEnchantments = new HashSet<>();
    worldDropChanceMap = new HashMap<>();
    worldSpawnChanceMap = new HashMap<>();
    worldIdentifyChanceMap = new HashMap<>();
    allowedItemGroups = new ArrayList<>();
    disallowedItemGroups = new ArrayList<>();
    allowedItemIds = new ArrayList<>();
    disallowedItemIds = new ArrayList<>();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDisplayName() {
    return displayName;
  }

  void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  @Override
  public ChatColor getDisplayColor() {
    return displayColor;
  }

  void setDisplayColor(ChatColor displayColor) {
    this.displayColor = displayColor;
  }

  @Override
  public ChatColor getIdentificationColor() {
    return identificationColor;
  }

  void setIdentificationColor(ChatColor identificationColor) {
    this.identificationColor = identificationColor;
  }

  @Override
  public List<String> getBaseLore() {
    return baseLore;
  }

  void setBaseLore(List<String> baseLore) {
    this.baseLore = baseLore;
  }

  @Override
  public List<String> getBonusLore() {
    return bonusLore;
  }

  void setBonusLore(List<String> bonusLore) {
    this.bonusLore = bonusLore;
  }

  @Override
  public int getMinimumBonusLore() {
    return minimumBonusLore;
  }

  void setMinimumBonusLore(int minimumBonusLore) {
    this.minimumBonusLore = minimumBonusLore;
  }

  @Override
  public int getMaximumBonusLore() {
    return maximumBonusLore;
  }

  void setMaximumBonusLore(int maximumBonusLore) {
    this.maximumBonusLore = maximumBonusLore;
  }

  @Override
  public Set<MythicEnchantment> getBaseEnchantments() {
    return baseEnchantments;
  }

  void setBaseEnchantments(Set<MythicEnchantment> baseEnchantments) {
    this.baseEnchantments = baseEnchantments;
  }

  @Override
  public Set<MythicEnchantment> getBonusEnchantments() {
    return bonusEnchantments;
  }

  void setBonusEnchantments(Set<MythicEnchantment> bonusEnchantments) {
    this.bonusEnchantments = bonusEnchantments;
  }

  @Override
  public boolean isSafeBaseEnchantments() {
    return safeBaseEnchantments;
  }

  void setSafeBaseEnchantments(boolean safeBaseEnchantments) {
    this.safeBaseEnchantments = safeBaseEnchantments;
  }

  @Override
  public boolean isSafeBonusEnchantments() {
    return safeBonusEnchantments;
  }

  void setSafeBonusEnchantments(boolean safeBonusEnchantments) {
    this.safeBonusEnchantments = safeBonusEnchantments;
  }

  @Override
  public boolean isAllowHighBaseEnchantments() {
    return allowHighBaseEnchantments;
  }

  void setAllowHighBaseEnchantments(boolean allowHighBaseEnchantments) {
    this.allowHighBaseEnchantments = allowHighBaseEnchantments;
  }

  @Override
  public boolean isAllowHighBonusEnchantments() {
    return allowHighBonusEnchantments;
  }

  void setAllowHighBonusEnchantments(boolean allowHighBonusEnchantments) {
    this.allowHighBonusEnchantments = allowHighBonusEnchantments;
  }

  @Override
  public int getMinimumBonusEnchantments() {
    return minimumBonusEnchantments;
  }

  void setMinimumBonusEnchantments(int minimumBonusEnchantments) {
    this.minimumBonusEnchantments = minimumBonusEnchantments;
  }

  @Override
  public int getMaximumBonusEnchantments() {
    return maximumBonusEnchantments;
  }

  void setMaximumBonusEnchantments(int maximumBonusEnchantments) {
    this.maximumBonusEnchantments = maximumBonusEnchantments;
  }

  @Override
  public double getMaximumDurabilityPercentage() {
    return maximumDurabilityPercentage;
  }

  void setMaximumDurabilityPercentage(double maximumDurabilityPercentage) {
    this.maximumDurabilityPercentage = maximumDurabilityPercentage;
  }

  @Override
  public double getMinimumDurabilityPercentage() {
    return minimumDurabilityPercentage;
  }

  void setMinimumDurabilityPercentage(double minimumDurabilityPercentage) {
    this.minimumDurabilityPercentage = minimumDurabilityPercentage;
  }

  @Override
  public Map<String, Double> getWorldDropChanceMap() {
    return worldDropChanceMap;
  }

  void setWorldDropChanceMap(Map<String, Double> worldDropChanceMap) {
    this.worldDropChanceMap = worldDropChanceMap;
  }

  @Override
  public Map<String, Double> getWorldSpawnChanceMap() {
    return worldSpawnChanceMap;
  }

  void setWorldSpawnChanceMap(Map<String, Double> worldSpawnChanceMap) {
    this.worldSpawnChanceMap = worldSpawnChanceMap;
  }

  @Override
  public Map<String, Double> getWorldIdentifyChanceMap() {
    return worldIdentifyChanceMap;
  }

  public void setWorldIdentifyChanceMap(Map<String, Double> map) {
    this.worldIdentifyChanceMap = map;
  }

  @Override
  public List<String> getAllowedItemGroups() {
    return allowedItemGroups;
  }

  void setAllowedItemGroups(List<String> allowedItemGroups) {
    this.allowedItemGroups = allowedItemGroups;
  }

  @Override
  public List<String> getDisallowedItemGroups() {
    return disallowedItemGroups;
  }

  void setDisallowedItemGroups(List<String> disallowedItemGroups) {
    this.disallowedItemGroups = disallowedItemGroups;
  }

  @Override
  public List<String> getAllowedItemIds() {
    return allowedItemIds;
  }

  void setAllowedItemIds(List<String> allowedItemIds) {
    this.allowedItemIds = allowedItemIds;
  }

  @Override
  public List<String> getDisallowedItemIds() {
    return disallowedItemIds;
  }

  void setDisallowedItemIds(List<String> disallowedItemIds) {
    this.disallowedItemIds = disallowedItemIds;
  }

  @Override
  public int getMinimumSockets() {
    return minimumSockets;
  }

  public void setMinimumSockets(int minimumSockets) {
    this.minimumSockets = minimumSockets;
  }

  @Override
  public int getMaximumSockets() {
    return maximumSockets;
  }

  public void setMaximumSockets(int maximumSockets) {
    this.maximumSockets = maximumSockets;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (displayColor != null ? displayColor.hashCode() : 0);
    result = 31 * result + (identificationColor != null ? identificationColor.hashCode() : 0);
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MythicTier)) {
      return false;
    }

    MythicTier that = (MythicTier) o;

    return displayColor == that.displayColor && identificationColor == that.identificationColor
           && !(name !=
                null ? !name.equals(that.name) : that.name != null);
  }

  @Override
  public String toString() {
    return "MythicTier{" +
           "name='" + name + '\'' +
           '}';
  }

  @Override
  public double getChanceToHaveSockets() {
    return chanceToHaveSockets;
  }

  public void setChanceToHaveSockets(double chanceToHaveSockets) {
    this.chanceToHaveSockets = chanceToHaveSockets;
  }

  @Override
  public boolean isBroadcastOnFind() {
    return broadcastOnFind;
  }

  public void setBroadcastOnFind(boolean broadcastOnFind) {
    this.broadcastOnFind = broadcastOnFind;
  }

  @Override
  public int compareTo(Tier o) {
    if (o == null || this.equals(o)) {
      return 0;
    }
    double defaultSpawnChanceThis = getWorldSpawnChanceMap().containsKey("default") ?
                                    getWorldSpawnChanceMap().get("default") : 0;
    double defaultSpawnChanceO = o.getWorldSpawnChanceMap().containsKey("default") ? o
                                    .getWorldSpawnChanceMap().get("default") : 0;
    return Double.compare(defaultSpawnChanceThis, defaultSpawnChanceO);
  }

}
