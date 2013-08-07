package net.nunnerycode.bukkit.mythicdrops.managers;

import net.nunnerycode.bukkit.libraries.utils.ItemStackUtils;
import net.nunnerycode.bukkit.libraries.utils.RandomRangeUtils;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicEnchantment;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.tiers.DefaultTier;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DropManager {
	private final MythicDrops plugin;

	public DropManager(final MythicDrops plugin) {
		this.plugin = plugin;
	}

	public ItemStack constructItemStackFromMaterialData(MaterialData matData, ItemGenerationReason reason) {
		MythicItemStack is = null;
		Tier tier;
		tier = getPlugin().getTierManager().getRandomTierFromSetWithChance(
				new HashSet<Tier>(getPlugin().getItemManager().getTiersForMaterialData(matData)));
		if (tier == null) {
			return null;
		}
		if (matData == null || matData.getItemTypeId() == 0
				|| matData.getItemType() == Material.AIR) {
			return is;
		}
		is = new MythicItemStack(matData);
		if (is == null) {
			return is;
		}
		if (reason != null && reason == ItemGenerationReason.MONSTER_SPAWN) {
			is.setDurability(ItemStackUtils.getAcceptableDurability(matData.getItemType(),
					ItemStackUtils.getDurabilityForMaterial(matData.getItemType(), tier.getMinimumDurabilityPercentage(),
							tier.getMaximumDurabilityPercentage())));
		}
		for (MythicEnchantment me : tier.getBaseEnchantments()) {
			if (me.getEnchantment() == null) {
				continue;
			}
			if (tier.isSafeBaseEnchantments() && me.getEnchantment().canEnchantItem(is)) {
				EnchantmentWrapper enchantmentWrapper = new EnchantmentWrapper(me.getEnchantment().getId());
				int minimumLevel = Math.max(me.getMinimumLevel(), enchantmentWrapper.getStartLevel());
				int maximumLevel = Math.min(me.getMaximumLevel(), enchantmentWrapper.getMaxLevel());
				if (tier.isAllowHighBaseEnchantments()) {
					is.addEnchantment(me.getEnchantment(), (int) RandomRangeUtils.randomRangeLongInclusive(minimumLevel,
							maximumLevel));
				} else {
					is.addEnchantment(me.getEnchantment(), getAcceptableEnchantmentLevel(me.getEnchantment(),
							(int) RandomRangeUtils.randomRangeLongInclusive(minimumLevel, maximumLevel)));
				}
			} else if (!tier.isSafeBaseEnchantments()) {
				is.addUnsafeEnchantment(me.getEnchantment(),
						(int) RandomRangeUtils.randomRangeLongInclusive(me.getMinimumLevel(), me.getMaximumLevel()));
			}
		}
		if (tier.getMaximumAmountOfBonusEnchantments() > 0) {
			int randEnchs = (int) RandomRangeUtils
					.randomRangeLongInclusive(tier.getMinimumAmountOfBonusEnchantments(),
							tier.getMaximumAmountOfBonusEnchantments());
			Set<MythicEnchantment> allowEnchs = tier.getBonusEnchantments();
			List<Enchantment> stackEnchs = new ArrayList<Enchantment>();
			for (Enchantment e : Enchantment.values()) {
				if (tier.isSafeBonusEnchantments() && e.canEnchantItem(is)) {
					stackEnchs.add(e);
				}
			}
			List<MythicEnchantment> actual = new ArrayList<MythicEnchantment>();
			for (MythicEnchantment te : allowEnchs) {
				if (te.getEnchantment() == null) {
					continue;
				}
				if (stackEnchs.contains(te.getEnchantment())) {
					actual.add(te);
				}
			}
			for (int i = 0; i < randEnchs; i++) {
				if (actual.size() > 0) {
					MythicEnchantment ench = actual.get((int) RandomRangeUtils.randomRangeLongExclusive(0, actual.size()));
					int lev = (int) RandomRangeUtils
							.randomRangeLongInclusive(ench.getMinimumLevel(), ench.getMaximumLevel());
					if (tier.isSafeBonusEnchantments()) {
						if (!tier.isAllowHighBonusEnchantments()) {
							is.addEnchantment(
									ench.getEnchantment(),
									getAcceptableEnchantmentLevel(ench.getEnchantment(),
											lev <= 0 ? 1 : Math.abs(lev)));
						} else {
							is.addUnsafeEnchantment(ench.getEnchantment(), lev <= 0 ? 1 : Math.abs(lev));
						}
					} else {
						is.addUnsafeEnchantment(ench.getEnchantment(), lev <= 0 ? 1 : Math.abs(lev));
					}
				}
			}
		}
		if (matData.getItemType() == null) {
			return is;
		}
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(getPlugin().getNameManager().randomFormattedName(
				is, tier));
		List<String> toolTips = getPlugin().getSettingsManager().getLoreFormat();
		List<String> tt = new ArrayList<String>();
		String itemType = getPlugin().getNameManager().getItemTypeName(matData);
		String tierName = tier.getTierDisplayName();
		String baseMaterial = getPlugin().getNameManager().getMinecraftMaterialName(is.getType());
		String mythicMaterial = getPlugin().getNameManager().getMythicMaterialName(is.getData());
		String enchantmentString = getPlugin().getNameManager().getEnchantmentTypeName(is);
		for (String s : toolTips) {
			String s1 = s;
			if (s1.contains("%itemtype%")) {
				s1 = s1.replace("%itemtype%", itemType);
			}
			if (s1.contains("%basematerial%")) {
				s1 = s1.replace("%basematerial%", baseMaterial);
			}
			if (s1.contains("%tiername%")) {
				s1 = s1.replace("%tiername%", tierName);
			}
			if (s1.contains("%mythicmaterial%")) {
				s1 = s1.replace("%mythicmaterial%", mythicMaterial);
			}
			if (s1.contains("%enchantment%")) {
				s1 = s1.replace("%enchantment%", enchantmentString);
			}
			tt.add(ChatColor.translateAlternateColorCodes('&', s1));
		}
		if (getPlugin().getSettingsManager().isRandomLoreEnabled() &&
				RandomRangeUtils.randomRangeDoubleExclusive(0.0, 1.0) <= getPlugin().getSettingsManager()
						.getRandomLoreChance()) {
			tt.addAll(getPlugin().getNameManager().randomLore(matData.getItemType(), tier));
		}
		im.setLore(tt);
		return is;
	}

	private int getAcceptableEnchantmentLevel(Enchantment ench, int level) {
		EnchantmentWrapper ew = new EnchantmentWrapper(ench.getId());
		int i = level;
		if (i > ew.getMaxLevel()) {
			i = ew.getMaxLevel();
		} else if (i < ew.getStartLevel()) {
			i = ew.getStartLevel();
		}
		return i;
	}

	public MythicDrops getPlugin() {
		return plugin;
	}

	public ItemStack constructItemStackFromTierAndMaterialData(Tier tier, MaterialData materialData,
															   ItemGenerationReason reason) {
		MythicItemStack is = null;
		if (tier == null) {
			throw new IllegalArgumentException("Tier is null");
		}
		if (materialData == null) {
			throw new IllegalArgumentException("MaterialData is null");
		}
		if (tier.equals(DefaultTier.IDENTITY_TOME)) {
			// identity tome
			return is;
		}
		if (tier.equals(DefaultTier.SOCKET_GEM)) {
			// socket gem
			return is;
		}
		if (tier.equals(DefaultTier.UNIDENTIFIED_ITEM)) {
			// unidentified item
			return is;
		}
		if (materialData == null || materialData.getItemTypeId() == 0
				|| materialData.getItemType() == Material.AIR) {
			throw new NullPointerException("MaterialData cannot be null or AIR");
		}
		is = new MythicItemStack(materialData);
		if (reason != null && reason != ItemGenerationReason.COMMAND) {
			is.setDurability(ItemStackUtils.getAcceptableDurability(materialData.getItemType(),
					ItemStackUtils.getDurabilityForMaterial(materialData.getItemType(), tier.getMinimumDurabilityPercentage(),
							tier.getMaximumDurabilityPercentage())));
		}
		for (MythicEnchantment me : tier.getBaseEnchantments()) {
			if (me.getEnchantment() == null) {
				continue;
			}
			if (tier.isSafeBaseEnchantments() && me.getEnchantment().canEnchantItem(is)) {
				EnchantmentWrapper enchantmentWrapper = new EnchantmentWrapper(me.getEnchantment().getId());
				int minimumLevel = Math.max(me.getMinimumLevel(), enchantmentWrapper.getStartLevel());
				int maximumLevel = Math.min(me.getMaximumLevel(), enchantmentWrapper.getMaxLevel());
				if (tier.isAllowHighBaseEnchantments()) {
					is.addEnchantment(me.getEnchantment(), (int) RandomRangeUtils.randomRangeLongInclusive(minimumLevel,
							maximumLevel));
				} else {
					is.addEnchantment(me.getEnchantment(), getAcceptableEnchantmentLevel(me.getEnchantment(),
							(int) RandomRangeUtils.randomRangeLongInclusive(minimumLevel, maximumLevel)));
				}
			} else if (!tier.isSafeBaseEnchantments()) {
				is.addUnsafeEnchantment(me.getEnchantment(),
						(int) RandomRangeUtils.randomRangeLongInclusive(me.getMinimumLevel(), me.getMaximumLevel()));
			}
		}
		if (tier.getMaximumAmountOfBonusEnchantments() > 0) {
			int randEnchs = (int) RandomRangeUtils
					.randomRangeLongInclusive(tier.getMinimumAmountOfBonusEnchantments(),
							tier.getMaximumAmountOfBonusEnchantments());
			Set<MythicEnchantment> allowEnchs = tier.getBonusEnchantments();
			List<Enchantment> stackEnchs = new ArrayList<Enchantment>();
			for (Enchantment e : Enchantment.values()) {
				if (tier.isSafeBonusEnchantments() && e.canEnchantItem(is)) {
					stackEnchs.add(e);
				}
			}
			List<MythicEnchantment> actual = new ArrayList<MythicEnchantment>();
			for (MythicEnchantment te : allowEnchs) {
				if (te.getEnchantment() == null) {
					continue;
				}
				if (stackEnchs.contains(te.getEnchantment())) {
					actual.add(te);
				}
			}
			for (int i = 0; i < randEnchs; i++) {
				if (actual.size() > 0) {
					MythicEnchantment ench = actual.get((int) RandomRangeUtils.randomRangeLongExclusive(0, actual.size()));
					int lev = (int) RandomRangeUtils
							.randomRangeLongInclusive(ench.getMinimumLevel(), ench.getMaximumLevel());
					if (tier.isSafeBonusEnchantments()) {
						if (!tier.isSafeBonusEnchantments()) {
							is.addEnchantment(
									ench.getEnchantment(),
									getAcceptableEnchantmentLevel(ench.getEnchantment(),
											lev <= 0 ? 1 : Math.abs(lev)));
						} else {
							is.addUnsafeEnchantment(ench.getEnchantment(), lev <= 0 ? 1 : Math.abs(lev));
						}
					} else {
						is.addUnsafeEnchantment(ench.getEnchantment(), lev <= 0 ? 1 : Math.abs(lev));
					}
				}
			}
		}
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(getPlugin().getNameManager().randomFormattedName(
				is, tier));
		List<String> toolTips = getPlugin().getSettingsManager()
				.getLoreFormat();
		List<String> tt = new ArrayList<String>();
		String itemType = getPlugin().getNameManager().getItemTypeName(materialData);
		String tierName = tier.getTierDisplayName();
		String baseMaterial = getPlugin().getNameManager().getMinecraftMaterialName(is.getType());
		String mythicMaterial = getPlugin().getNameManager().getMythicMaterialName(is.getData());
		String enchantmentString = getPlugin().getNameManager().getEnchantmentTypeName(is);
		for (String s : toolTips) {
			String s1 = s;
			if (s1.contains("%itemtype%")) {
				s1 = s1.replace("%itemtype%", itemType);
			}
			if (s1.contains("%basematerial%")) {
				s1 = s1.replace("%basematerial%", baseMaterial);
			}
			if (s1.contains("%tiername%")) {
				s1 = s1.replace("%tiername%", tierName);
			}
			if (s1.contains("%mythicmaterial%")) {
				s1 = s1.replace("%mythicmaterial%", mythicMaterial);
			}
			if (s1.contains("%enchantment%")) {
				s1 = s1.replace("%enchantment%", enchantmentString);
			}
			tt.add(ChatColor.translateAlternateColorCodes('&', s1));
		}
		if (getPlugin().getSettingsManager().isRandomLoreEnabled() &&
				RandomRangeUtils.randomRangeDoubleExclusive(0.0, 1.0) <= getPlugin().getSettingsManager()
						.getRandomLoreChance()) {
			tt.addAll(getPlugin().getNameManager().randomLore(materialData.getItemType(), tier));
		}
		im.setLore(tt);
		return is;
	}

	public ItemStack constructItemStackFromTier(Tier tier, ItemGenerationReason reason) throws IllegalArgumentException {
		MythicItemStack is = null;
		if (tier == null) {
			throw new IllegalArgumentException("Tier is null");
		}
		if (tier.equals(DefaultTier.IDENTITY_TOME)) {
			// identity tome
			return is;
		}
		if (tier.equals(DefaultTier.SOCKET_GEM)) {
			// socket gem
			return is;
		}
		if (tier.equals(DefaultTier.UNIDENTIFIED_ITEM)) {
			// unidentified item
			return is;
		}
		Set<MaterialData> materialDataSet = getPlugin().getItemManager().getMaterialDataSetForTier(tier);
		MaterialData materialData = materialDataSet.toArray(new MaterialData[materialDataSet.size()])[((int) RandomRangeUtils
				.randomRangeLongExclusive(0, materialDataSet.size()))];
		if (materialData == null || materialData.getItemTypeId() == 0
				|| materialData.getItemType() == Material.AIR) {
			throw new NullPointerException("MaterialData cannot be null or AIR");
		}
		is = new MythicItemStack(materialData);
		if (reason != null && reason != ItemGenerationReason.COMMAND) {
			is.setDurability(ItemStackUtils.getAcceptableDurability(materialData.getItemType(),
					ItemStackUtils.getDurabilityForMaterial(materialData.getItemType(), tier.getMinimumDurabilityPercentage(),
							tier.getMaximumDurabilityPercentage())));
		}
		for (MythicEnchantment me : tier.getBaseEnchantments()) {
			if (me.getEnchantment() == null) {
				continue;
			}
			if (tier.isSafeBaseEnchantments() && me.getEnchantment().canEnchantItem(is)) {
				EnchantmentWrapper enchantmentWrapper = new EnchantmentWrapper(me.getEnchantment().getId());
				int minimumLevel = Math.max(me.getMinimumLevel(), enchantmentWrapper.getStartLevel());
				int maximumLevel = Math.min(me.getMaximumLevel(), enchantmentWrapper.getMaxLevel());
				if (tier.isAllowHighBaseEnchantments()) {
					is.addEnchantment(me.getEnchantment(), (int) RandomRangeUtils.randomRangeLongInclusive(minimumLevel,
							maximumLevel));
				} else {
					is.addEnchantment(me.getEnchantment(), getAcceptableEnchantmentLevel(me.getEnchantment(),
							(int) RandomRangeUtils.randomRangeLongInclusive(minimumLevel, maximumLevel)));
				}
			} else if (!tier.isSafeBaseEnchantments()) {
				is.addUnsafeEnchantment(me.getEnchantment(),
						(int) RandomRangeUtils.randomRangeLongInclusive(me.getMinimumLevel(), me.getMaximumLevel()));
			}
		}
		if (tier.getMaximumAmountOfBonusEnchantments() > 0) {
			int randEnchs = (int) RandomRangeUtils
					.randomRangeLongInclusive(tier.getMinimumAmountOfBonusEnchantments(),
							tier.getMaximumAmountOfBonusEnchantments());
			Set<MythicEnchantment> allowEnchs = tier.getBonusEnchantments();
			List<Enchantment> stackEnchs = new ArrayList<Enchantment>();
			for (Enchantment e : Enchantment.values()) {
				if (tier.isSafeBonusEnchantments() && e.canEnchantItem(is)) {
					stackEnchs.add(e);
				}
			}
			List<MythicEnchantment> actual = new ArrayList<MythicEnchantment>();
			for (MythicEnchantment te : allowEnchs) {
				if (te.getEnchantment() == null) {
					continue;
				}
				if (stackEnchs.contains(te.getEnchantment())) {
					actual.add(te);
				}
			}
			for (int i = 0; i < randEnchs; i++) {
				if (actual.size() > 0) {
					MythicEnchantment ench = actual.get((int) RandomRangeUtils.randomRangeLongExclusive(0, actual.size()));
					int lev = (int) RandomRangeUtils
							.randomRangeLongInclusive(ench.getMinimumLevel(), ench.getMaximumLevel());
					if (tier.isSafeBonusEnchantments()) {
						if (!tier.isSafeBonusEnchantments()) {
							is.addEnchantment(
									ench.getEnchantment(),
									getAcceptableEnchantmentLevel(ench.getEnchantment(),
											lev <= 0 ? 1 : Math.abs(lev)));
						} else {
							is.addUnsafeEnchantment(ench.getEnchantment(), lev <= 0 ? 1 : Math.abs(lev));
						}
					} else {
						is.addUnsafeEnchantment(ench.getEnchantment(), lev <= 0 ? 1 : Math.abs(lev));
					}
				}
			}
		}
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(getPlugin().getNameManager().randomFormattedName(
				is, tier));
		List<String> toolTips = getPlugin().getSettingsManager()
				.getLoreFormat();
		List<String> tt = new ArrayList<String>();
		String itemType = getPlugin().getNameManager().getItemTypeName(materialData);
		String tierName = tier.getTierDisplayName();
		String baseMaterial = getPlugin().getNameManager().getMinecraftMaterialName(is.getType());
		String mythicMaterial = getPlugin().getNameManager().getMythicMaterialName(is.getData());
		String enchantmentString = getPlugin().getNameManager().getEnchantmentTypeName(is);
		for (String s : toolTips) {
			String s1 = s;
			if (s1.contains("%itemtype%")) {
				s1 = s1.replace("%itemtype%", itemType);
			}
			if (s1.contains("%basematerial%")) {
				s1 = s1.replace("%basematerial%", baseMaterial);
			}
			if (s1.contains("%tiername%")) {
				s1 = s1.replace("%tiername%", tierName);
			}
			if (s1.contains("%mythicmaterial%")) {
				s1 = s1.replace("%mythicmaterial%", mythicMaterial);
			}
			if (s1.contains("%enchantment%")) {
				s1 = s1.replace("%enchantment%", enchantmentString);
			}
			tt.add(ChatColor.translateAlternateColorCodes('&', s1));
		}
		if (getPlugin().getSettingsManager().isRandomLoreEnabled() &&
				RandomRangeUtils.randomRangeDoubleExclusive(0.0, 1.0) <= getPlugin().getSettingsManager()
						.getRandomLoreChance()) {
			tt.addAll(getPlugin().getNameManager().randomLore(materialData.getItemType(), tier));
		}
		im.setLore(tt);
		return is;
	}
}