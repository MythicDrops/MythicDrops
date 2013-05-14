/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.mythicdrops.managers;

import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.objects.CustomItem;
import com.conventnunnery.plugins.mythicdrops.objects.MythicEnchantment;
import com.conventnunnery.plugins.mythicdrops.objects.SocketGem;
import com.conventnunnery.plugins.mythicdrops.objects.SocketItem;
import com.conventnunnery.plugins.mythicdrops.objects.Tier;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A class that handles all of the plugin's drop creation.
 */
public class DropManager {
	private final MythicDrops plugin;
	private List<CustomItem> customItems;

	/**
	 * Instantiates a new Drop API.
	 *
	 * @param plugin the plugin
	 */
	public DropManager(MythicDrops plugin) {
		this.plugin = plugin;
		customItems = new ArrayList<CustomItem>();
	}

	/**
	 * Construct a random ItemStack.
	 *
	 * @param reason the reason
	 * @return random ItemStack
	 */
	public ItemStack constructItemStack(GenerationReason reason) {
		switch (reason) {
			case MOB_SPAWN:
				if (getPlugin().getPluginSettings().isAllowCustomToSpawn() &&
						getPlugin().getPluginSettings().isOnlyCustomItems() ||
						getPlugin().getRandom().nextDouble() <=
								getPlugin().getPluginSettings().getPercentageCustomDrop() &&
								!customItems.isEmpty()) {
					return randomCustomItemWithChance().toItemStack();
				}
				if (getPlugin().getPluginSettings().isSocketGemsEnabled()
						&& getPlugin().getRandom().nextDouble() < getPlugin()
						.getPluginSettings().getSocketGemsChance()) {
					MaterialData materialData = getPlugin().getSocketGemManager().getRandomSocketGemMaterial();
					SocketGem socketGem = getPlugin().getSocketGemManager().getRandomSocketGemWithChance();
					if (materialData != null && socketGem != null)
						return new SocketItem(materialData, socketGem);
				}
				return constructItemStack(getPlugin().getTierManager().randomTierWithChance(), reason);
			case COMMAND:
				return constructItemStack(getPlugin().getTierManager()
						.randomTierWithChance(), reason);
			case EXTERNAL:
				return constructItemStack(getPlugin().getTierManager()
						.randomTierWithChance(), reason);
			default:
				return constructItemStack(getPlugin().getTierManager()
						.randomTierWithChance(), reason);
		}
	}

	/**
	 * Construct an ItemStack based on a Tier.
	 *
	 * @param tier   Tier to base the ItemStack on
	 * @param reason reason to generate the ItemStack
	 * @return constructed ItemStack
	 */
	public ItemStack constructItemStack(Tier tier, GenerationReason reason) {
		ItemStack itemstack = null;
		MaterialData matData = null;
		int attempts = 0;
		if (tier == null) {
			return null;
		}
		while (matData == null && attempts < 10) {
			matData = getPlugin().getItemManager().getMatDataFromTier(tier);
			attempts++;
		}
		if (matData == null || matData.getItemTypeId() == 0
				|| matData.getItemType() == Material.AIR)
			return itemstack;
		itemstack = matData.toItemStack(1);
		if (itemstack == null) {
			return itemstack;
		}
		if (reason != null && reason != GenerationReason.COMMAND) {
			double min = Math.min(tier.getMinimumDurability(), tier.getMaximumDurability()) *
					itemstack.getType().getMaxDurability();
			double max = Math.max(tier.getMinimumDurability(), tier.getMaximumDurability()) *
					itemstack.getType().getMaxDurability();
			double minDuraPercent = itemstack.getType().getMaxDurability() -
					Math.max(min, max) *
							itemstack.getType().getMaxDurability();
			double maxDuraPercent =
					itemstack.getType().getMaxDurability() -
							Math.min(min, max) *
									itemstack.getType().getMaxDurability();
			int minDura =
					(int) minDuraPercent;
			int maxDura = (int) maxDuraPercent;
			short dura = (short) (getPlugin().getRandom()
					.nextInt(
							Math.abs(Math.max(minDura, maxDura) - Math.min(minDura, maxDura)) + 1) +
					Math.min(minDura, maxDura));
			itemstack.setDurability(dura);
		}
		for (MythicEnchantment me : tier.getBaseEnchantments()) {
            if (me.getEnchantment() == null){
                continue;
            }
			if (tier.isSafeBaseEnchantments() && me.getEnchantment().canEnchantItem(itemstack)) {
				itemstack.addEnchantment(me.getEnchantment(), Math.abs(me.getMinimumLevel() +
						getPlugin().getRandom().nextInt(me.getMaximumLevel() - me.getMinimumLevel())));
			} else if (!tier.isSafeBaseEnchantments()) {
				itemstack.addUnsafeEnchantment(me.getEnchantment(), Math.abs(me.getMinimumLevel() +
						getPlugin().getRandom().nextInt(me.getMaximumLevel() - me.getMinimumLevel() + 1)));
			}
		}
		if (tier.getMaximumBonusEnchantments() > 0) {
			int randEnchs = getPlugin().getRandom().nextInt(
					Math.abs(tier.getMaximumBonusEnchantments() - tier.getMinimumBonusEnchantments() + 1)) +
					tier.getMinimumBonusEnchantments();
			for (int i = 0; i < randEnchs; i++) {
				Set<MythicEnchantment> allowEnchs = tier.getBonusEnchantments();
				List<Enchantment> stackEnchs = getEnchantStack(itemstack);
				List<MythicEnchantment> actual = new ArrayList<MythicEnchantment>();
				for (MythicEnchantment te : allowEnchs) {
                    if (te.getEnchantment() == null) {
                        continue;
                    }
					if (stackEnchs.contains(te.getEnchantment())) {
						actual.add(te);
					}
				}
				if (actual.size() > 0) {
					MythicEnchantment ench = actual.get(getPlugin().getRandom()
							.nextInt(actual.size()));
					int lev =
							getPlugin().getRandom()
									.nextInt(Math.abs(ench.getMaximumLevel() - ench.getMinimumLevel()) + 1) +
									ench.getMinimumLevel();
					if (getPlugin().getPluginSettings().isSafeEnchantsOnly()) {
						if (!getPlugin().getPluginSettings().isAllowEnchantsPastNormalLevel()) {
							itemstack.addEnchantment(
									ench.getEnchantment(),
									getAcceptableEnchantmentLevel(ench.getEnchantment(),
											lev <= 0 ? 1 : Math.abs(lev)));
						} else {
							itemstack.addUnsafeEnchantment(ench.getEnchantment(), lev <= 0 ? 1 : Math.abs(lev));
						}
					} else {
						itemstack.addUnsafeEnchantment(ench.getEnchantment(), lev <= 0 ? 1 : Math.abs(lev));
					}
				}
			}
		}
		if (matData.getItemType() == null) {
			return itemstack;
		}
		ItemMeta im;
		if (itemstack.hasItemMeta())
			im = itemstack.getItemMeta();
		else
			im = Bukkit.getItemFactory().getItemMeta(matData.getItemType());
		im.setDisplayName(getPlugin().getNameManager().randomFormattedName(
				itemstack, tier));
		List<String> toolTips = getPlugin().getPluginSettings()
				.getAdvancedToolTipFormat();
		List<String> tt = new ArrayList<String>();
		for (String s : toolTips) {
			tt.add(ChatColor.translateAlternateColorCodes(
					'&',
					s.replace("%itemtype%",
							getPlugin().getNameManager().getItemTypeName(matData))
							.replace("%tiername%",
									tier.getDisplayColor() + tier.getDisplayName())
							.replace(
									"%basematerial%",
									getPlugin().getNameManager()
											.getMinecraftMaterialName(
													itemstack.getType()))
							.replace(
									"%mythicmaterial%",
									getPlugin().getNameManager()
											.getMythicMaterialName(
													itemstack.getData())).replace("%enchantment%",
							tier.getDisplayColor() + getPlugin().getNameManager().getEnchantmentTypeName(itemstack) +
									tier.getIdentificationColor())));
		}
		if (getPlugin().getPluginSettings().isSockettedItemsEnabled() &&
				getPlugin().getRandom().nextDouble() <= getPlugin().getPluginSettings().getSpawnWithSocketChance()) {
			int amtTT = 0;
			for (int i = 0;
			     i < getPlugin().getRandom()
					     .nextInt(Math.abs(tier.getMaximumSockets() - tier.getMinimumSockets()) + 1) +
					     tier.getMinimumSockets(); i++) {
				tt.add(ChatColor.GOLD + "(Socket)");
				amtTT++;
			}
			if (amtTT > 0) {
				tt.add(ChatColor.GRAY + "Find a " + ChatColor.GOLD + "Socket Gem" + ChatColor.GRAY + " to fill a " +
						ChatColor.GOLD + "(Socket)");
			}
		}
		if (getPlugin().getPluginSettings().isRandomLoreEnabled() &&
				getPlugin().getRandom().nextDouble() <= getPlugin().getPluginSettings().getRandomLoreChance() &&
				!getPlugin().getNameManager().getBasicLore().isEmpty()) {
			tt.addAll(getPlugin().getNameManager().randomLore());
		}
		im.setLore(tt);
		if (im instanceof Repairable) {
			Repairable r = (Repairable) im;
			r.setRepairCost(1000);
			itemstack.setItemMeta((ItemMeta) r);
		} else {
			itemstack.setItemMeta(im);
		}
		return itemstack;
	}

	/**
	 * Gets acceptable Enchantment level.
	 *
	 * @param ench  the Enchantment
	 * @param level the level
	 * @return the acceptable Enchantment level
	 */
	public int getAcceptableEnchantmentLevel(Enchantment ench, int level) {
		EnchantmentWrapper ew = new EnchantmentWrapper(ench.getId());
		int i = level;
		if (i > ew.getMaxLevel()) {
			i = ew.getMaxLevel();
		} else if (i < ew.getStartLevel()) {
			i = ew.getStartLevel();
		}
		return i;
	}

	/**
	 * Gets custom items.
	 *
	 * @return the custom items
	 */
	public List<CustomItem> getCustomItems() {
		return customItems;
	}

	/**
	 * Gets a list of Enchantments that can go on an ItemStack.
	 *
	 * @param ci ItemStack to check
	 * @return list of possible Enchantments
	 */
	public List<Enchantment> getEnchantStack(final ItemStack ci) {
		List<Enchantment> set = new ArrayList<Enchantment>();
		if (ci == null) {
			return set;
		}
		boolean bln = getPlugin().getPluginSettings().isSafeEnchantsOnly();
		for (Enchantment e : Enchantment.values()) {
			if (bln) {
				if (e.canEnchantItem(ci)) {
					set.add(e);
				}
			} else {
				set.add(e);
			}
		}
		return set;
	}

	/**
	 * Gets plugin.
	 *
	 * @return the plugin
	 */
	public MythicDrops getPlugin() {
		return plugin;
	}

	public void debugCustomItems() {
		List<String> customItemNames = new ArrayList<String>();
		for (CustomItem ci : customItems) {
			customItemNames.add(ci.getName());
		}
		getPlugin().getDebug().debug(
				"Loaded custom items: "
						+ customItemNames.toString().replace("[", "")
						.replace("]", ""));
	}

	/**
	 * Random custom item.
	 *
	 * @return the custom item
	 */
	@SuppressWarnings("unused")
	public CustomItem randomCustomItem() {
		return customItems.get(getPlugin().getRandom().nextInt(customItems.size()));
	}

	public CustomItem getCustomItemByName(String name) {
		for (CustomItem i : customItems) {
			if (name.equalsIgnoreCase(i.getName())) {
				return i;
			}
		}
		return null;
	}

	/**
	 * Random custom item with chance.
	 *
	 * @return the custom item
	 */
	public CustomItem randomCustomItemWithChance() {
		CustomItem ci = null;
		if (customItems == null || customItems.isEmpty())
			return ci;
		while (ci == null) {
			for (CustomItem c : customItems) {
				double d = plugin.getRandom().nextDouble();
				if (d <= c.getChance()) {
					ci = c;
					break;
				}
			}
		}
		return ci;
	}

	/**
	 * Enum of GenerationReasons.
	 */
	public enum GenerationReason {
		/**
		 * Use when spawning a mob
		 */MOB_SPAWN, /**
		 * Use for commands
		 */COMMAND, /**
		 * Use for anything else
		 */EXTERNAL
	}

}
