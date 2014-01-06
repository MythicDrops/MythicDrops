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

	@Override
	public ChatColor getDisplayColor() {
		return displayColor;
	}

	@Override
	public ChatColor getIdentificationColor() {
		return identificationColor;
	}

	@Override
	public List<String> getBaseLore() {
		return baseLore;
	}

	@Override
	public List<String> getBonusLore() {
		return bonusLore;
	}

	@Override
	public int getMinimumBonusLore() {
		return minimumBonusLore;
	}

	@Override
	public int getMaximumBonusLore() {
		return maximumBonusLore;
	}

	@Override
	public Set<MythicEnchantment> getBaseEnchantments() {
		return baseEnchantments;
	}

	@Override
	public Set<MythicEnchantment> getBonusEnchantments() {
		return bonusEnchantments;
	}

	@Override
	public boolean isSafeBaseEnchantments() {
		return safeBaseEnchantments;
	}

	@Override
	public boolean isSafeBonusEnchantments() {
		return safeBonusEnchantments;
	}

	@Override
	public boolean isAllowHighBaseEnchantments() {
		return allowHighBaseEnchantments;
	}

	@Override
	public boolean isAllowHighBonusEnchantments() {
		return allowHighBonusEnchantments;
	}

	@Override
	public int getMinimumBonusEnchantments() {
		return minimumBonusEnchantments;
	}

	@Override
	public int getMaximumBonusEnchantments() {
		return maximumBonusEnchantments;
	}

	@Override
	public double getMaximumDurabilityPercentage() {
		return maximumDurabilityPercentage;
	}

	@Override
	public double getMinimumDurabilityPercentage() {
		return minimumDurabilityPercentage;
	}

	@Override
	public Map<String, Double> getWorldDropChanceMap() {
		return worldDropChanceMap;
	}

	@Override
	public Map<String, Double> getWorldSpawnChanceMap() {
		return worldSpawnChanceMap;
	}

	@Override
	public Map<String, Double> getWorldIdentifyChanceMap() {
		return worldIdentifyChanceMap;
	}

	@Override
	public List<String> getAllowedItemGroups() {
		return allowedItemGroups;
	}

	@Override
	public List<String> getDisallowedItemGroups() {
		return disallowedItemGroups;
	}

	@Override
	public List<String> getAllowedItemIds() {
		return allowedItemIds;
	}

	@Override
	public List<String> getDisallowedItemIds() {
		return disallowedItemIds;
	}

	@Override
	public int getMinimumSockets() {
		return minimumSockets;
	}

	@Override
	public int getMaximumSockets() {
		return maximumSockets;
	}

	public void setMaximumSockets(int maximumSockets) {
		this.maximumSockets = maximumSockets;
	}

	public void setMinimumSockets(int minimumSockets) {
		this.minimumSockets = minimumSockets;
	}

	void setDisallowedItemIds(List<String> disallowedItemIds) {
		this.disallowedItemIds = disallowedItemIds;
	}

	void setAllowedItemIds(List<String> allowedItemIds) {
		this.allowedItemIds = allowedItemIds;
	}

	void setDisallowedItemGroups(List<String> disallowedItemGroups) {
		this.disallowedItemGroups = disallowedItemGroups;
	}

	void setAllowedItemGroups(List<String> allowedItemGroups) {
		this.allowedItemGroups = allowedItemGroups;
	}

	public void setWorldIdentifyChanceMap(Map<String, Double> map) {
		this.worldIdentifyChanceMap = map;
	}

	void setWorldSpawnChanceMap(Map<String, Double> worldSpawnChanceMap) {
		this.worldSpawnChanceMap = worldSpawnChanceMap;
	}

	void setWorldDropChanceMap(Map<String, Double> worldDropChanceMap) {
		this.worldDropChanceMap = worldDropChanceMap;
	}

	void setMinimumDurabilityPercentage(double minimumDurabilityPercentage) {
		this.minimumDurabilityPercentage = minimumDurabilityPercentage;
	}

	void setMaximumDurabilityPercentage(double maximumDurabilityPercentage) {
		this.maximumDurabilityPercentage = maximumDurabilityPercentage;
	}

	void setMaximumBonusEnchantments(int maximumBonusEnchantments) {
		this.maximumBonusEnchantments = maximumBonusEnchantments;
	}

	void setMinimumBonusEnchantments(int minimumBonusEnchantments) {
		this.minimumBonusEnchantments = minimumBonusEnchantments;
	}

	void setAllowHighBonusEnchantments(boolean allowHighBonusEnchantments) {
		this.allowHighBonusEnchantments = allowHighBonusEnchantments;
	}

	void setAllowHighBaseEnchantments(boolean allowHighBaseEnchantments) {
		this.allowHighBaseEnchantments = allowHighBaseEnchantments;
	}

	void setSafeBonusEnchantments(boolean safeBonusEnchantments) {
		this.safeBonusEnchantments = safeBonusEnchantments;
	}

	void setSafeBaseEnchantments(boolean safeBaseEnchantments) {
		this.safeBaseEnchantments = safeBaseEnchantments;
	}

	void setBonusEnchantments(Set<MythicEnchantment> bonusEnchantments) {
		this.bonusEnchantments = bonusEnchantments;
	}

	void setBaseEnchantments(Set<MythicEnchantment> baseEnchantments) {
		this.baseEnchantments = baseEnchantments;
	}

	void setMaximumBonusLore(int maximumBonusLore) {
		this.maximumBonusLore = maximumBonusLore;
	}

	void setMinimumBonusLore(int minimumBonusLore) {
		this.minimumBonusLore = minimumBonusLore;
	}

	void setBonusLore(List<String> bonusLore) {
		this.bonusLore = bonusLore;
	}

	void setBaseLore(List<String> baseLore) {
		this.baseLore = baseLore;
	}

	void setIdentificationColor(ChatColor identificationColor) {
		this.identificationColor = identificationColor;
	}

	void setDisplayColor(ChatColor displayColor) {
		this.displayColor = displayColor;
	}

	void setDisplayName(String displayName) {
		this.displayName = displayName;
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
		if (this == o) return true;
		if (!(o instanceof MythicTier)) return false;

		MythicTier that = (MythicTier) o;

		return displayColor == that.displayColor && identificationColor == that.identificationColor && !(name !=
				null ? !name.equals(that.name) : that.name != null);
	}

	@Override
	public String toString() {
		return "MythicTier{" +
				"name='" + name + '\'' +
				'}';
	}
}
