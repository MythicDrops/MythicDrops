package net.nunnerycode.bukkit.mythicdrops.managers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicEnchantment;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.events.CustomItemGenerationEvent;
import net.nunnerycode.bukkit.mythicdrops.events.PreCustomItemGenerationEvent;
import net.nunnerycode.bukkit.mythicdrops.events.PreRandomItemGenerationEvent;
import net.nunnerycode.bukkit.mythicdrops.events.RandomItemGenerationEvent;
import net.nunnerycode.bukkit.mythicdrops.tiers.DefaultTier;
import net.nunnerycode.bukkit.mythicdrops.utils.ItemStackUtils;
import net.nunnerycode.bukkit.mythicdrops.utils.RandomRangeUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;

public class DropManager {
	private final MythicDrops plugin;

	public DropManager(final MythicDrops plugin) {
		this.plugin = plugin;
	}

	public ItemStack generateItemStackFromCustomItem(CustomItem customItem, ItemGenerationReason reason) {
		CustomItem ci = customItem;
		PreCustomItemGenerationEvent preEvent = new PreCustomItemGenerationEvent(reason, ci);
		Bukkit.getPluginManager().callEvent(preEvent);
		if (preEvent.isCancelled()) {
			return null;
		}
		ci = preEvent.getCustomItem();
		ItemStack is = ci.toItemStack();
		CustomItemGenerationEvent event = new CustomItemGenerationEvent(reason, is);
		Bukkit.getPluginManager().callEvent(event);
		return is;
	}

	public ItemStack constructItemStackFromMaterialData(MaterialData matData, ItemGenerationReason reason) throws IllegalArgumentException, NullPointerException {
		Tier tier;
		tier = getPlugin().getTierManager().getRandomTierFromSetWithChance(
				new HashSet<Tier>(getPlugin().getItemManager().getTiersForMaterialData(matData)));
		if (tier == null) {
			throw new NullPointerException("Randomly chosen Tier is null");
		}
		if (matData == null) {
			throw new IllegalArgumentException("MaterialData cannot be null");
		}
		if (matData.getItemTypeId() == 0
				|| matData.getItemType() == Material.AIR) {
			throw new IllegalArgumentException("MaterialData cannot be AIR");
		}
		try {
			return constructItemStackFromTierAndMaterialData(tier, matData, reason);
		} catch (IllegalArgumentException e) {
			throw new NullPointerException("Generated ItemStack is null");
		}
	}

	public ItemStack constructItemStackFromTierAndMaterialData(Tier tier, MaterialData materialData,
															   ItemGenerationReason reason) throws IllegalArgumentException {
		MythicItemStack is;
		Tier t = tier;
		MaterialData md = materialData;
		if (t == null) {
			throw new IllegalArgumentException("Tier is null");
		}
		if (md == null) {
			throw new IllegalArgumentException("MaterialData is null");
		}
		if (t.equals(DefaultTier.CUSTOM_ITEM)) {
			throw new IllegalArgumentException("Tier cannot be CUSTOM_ITEM when using this method");
		}
		if (md.getItemTypeId() == 0
				|| md.getItemType() == Material.AIR) {
			throw new IllegalArgumentException("MaterialData cannot be AIR");
		}
		PreRandomItemGenerationEvent preEvent = new PreRandomItemGenerationEvent(reason, t, md);
		Bukkit.getPluginManager().callEvent(preEvent);
		if (preEvent.isCancelled()) {
			return null;
		}
		md = preEvent.getMaterialData();
		t = preEvent.getTier();
		is = new MythicItemStack(md);
		if (reason != null && reason != ItemGenerationReason.COMMAND) {
			is.setDurability(ItemStackUtils.getAcceptableDurability(md.getItemType(),
					ItemStackUtils.getDurabilityForMaterial(md.getItemType(), t.getMinimumDurabilityPercentage(),
							t.getMaximumDurabilityPercentage())));
		}
		addBaseEnchantments(is, t);
		addBonusEnchantments(is, t);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(getPlugin().getNameManager().randomFormattedName(
				is, t));
		generateLore(tier, is, t, md, im);
		if (im instanceof LeatherArmorMeta) {
			((LeatherArmorMeta) im).setColor(Color.fromRGB(RandomUtils.nextInt(256), RandomUtils.nextInt(256),
					RandomUtils.nextInt(256)));
		}
		is.setItemMeta(im);
		RandomItemGenerationEvent event = new RandomItemGenerationEvent(reason, t, is);
		Bukkit.getPluginManager().callEvent(event);
		return is;
	}

	private void generateLore(Tier tier, MythicItemStack is, Tier t, MaterialData md, ItemMeta im) {
		List<String> toolTips = (!tier.getTierLore().isEmpty()) ? tier.getTierLore() : getPlugin().getSettingsManager
				().getLoreFormat();
		List<String> tt = new ArrayList<String>();
		String itemType = getPlugin().getNameManager().getItemTypeName(md);
		String tName = t.getTierDisplayName();
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
				s1 = s1.replace("%tiername%", tName);
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
			tt.addAll(getPlugin().getNameManager().randomLore(md.getItemType(), t));
		}
		im.setLore(tt);
	}

	private void addBonusEnchantments(MythicItemStack is, Tier t) {
		if (t.getMaximumAmountOfBonusEnchantments() > 0) {
			int randEnchs = (int) RandomRangeUtils
					.randomRangeLongInclusive(t.getMinimumAmountOfBonusEnchantments(),
							t.getMaximumAmountOfBonusEnchantments());
			Set<MythicEnchantment> allowEnchs = t.getBonusEnchantments();
			List<Enchantment> stackEnchs = new ArrayList<Enchantment>();
			for (Enchantment e : Enchantment.values()) {
				if (t.isSafeBonusEnchantments() && e.canEnchantItem(is)) {
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
					if (t.isSafeBonusEnchantments()) {
						if (!t.isSafeBonusEnchantments()) {
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
	}

	private void addBaseEnchantments(MythicItemStack is, Tier t) {
		for (MythicEnchantment me : t.getBaseEnchantments()) {
			if (me.getEnchantment() == null) {
				continue;
			}
			if (t.isSafeBaseEnchantments() && me.getEnchantment().canEnchantItem(is)) {
				EnchantmentWrapper enchantmentWrapper = new EnchantmentWrapper(me.getEnchantment().getId());
				int minimumLevel = Math.max(me.getMinimumLevel(), enchantmentWrapper.getStartLevel());
				int maximumLevel = Math.min(me.getMaximumLevel(), enchantmentWrapper.getMaxLevel());
				if (t.isAllowHighBaseEnchantments()) {
					is.addEnchantment(me.getEnchantment(), (int) RandomRangeUtils.randomRangeLongInclusive(minimumLevel,
							maximumLevel));
				} else {
					is.addEnchantment(me.getEnchantment(), getAcceptableEnchantmentLevel(me.getEnchantment(),
							(int) RandomRangeUtils.randomRangeLongInclusive(minimumLevel, maximumLevel)));
				}
			} else if (!t.isSafeBaseEnchantments()) {
				is.addUnsafeEnchantment(me.getEnchantment(),
						(int) RandomRangeUtils.randomRangeLongInclusive(me.getMinimumLevel(), me.getMaximumLevel()));
			}
		}
	}

	private int getAcceptableEnchantmentLevel(Enchantment ench, int level) {
		EnchantmentWrapper ew = new EnchantmentWrapper(ench.getId());
		return Math.max(Math.min(level, ew.getMaxLevel()), ew.getStartLevel());
	}

	public MythicDrops getPlugin() {
		return plugin;
	}

	public ItemStack constructItemStackFromTier(Tier tier, ItemGenerationReason reason) throws
			IllegalArgumentException, NullPointerException {
		if (tier == null) {
			throw new IllegalArgumentException("Tier is null");
		}
		Set<MaterialData> materialDataSet = getPlugin().getItemManager().getMaterialDataSetForTier(tier);
		if (materialDataSet.isEmpty()) {
			throw new NullPointerException("Tier " + tier.getTierName() + " has no MaterialData to choose from");
		}
		MaterialData materialData = materialDataSet.toArray(new MaterialData[materialDataSet.size()])
				[RandomUtils.nextInt(materialDataSet.size())];
		if (materialData == null) {
			throw new NullPointerException("Randomly chosen MaterialData is null");
		}
		if (materialData.getItemTypeId() == 0
				|| materialData.getItemType() == Material.AIR) {
			throw new IllegalArgumentException("MaterialData cannot be null or AIR");
		}
		try {
			return constructItemStackFromTierAndMaterialData(tier, materialData, reason);
		} catch (IllegalArgumentException e) {
			throw new NullPointerException("Generated ItemStack is null");
		}
	}
}