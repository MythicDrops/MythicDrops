/*
 * Originally created by deathmarine
 * Modified by Nunnery on March 10, 2013
 */

package com.modcrafting.diablodrops.builders;

import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.managers.ConfigurationManager;
import com.conventnunnery.plugins.mythicdrops.objects.MythicEnchantment;
import com.conventnunnery.plugins.mythicdrops.objects.Tier;
import com.conventnunnery.plugins.mythicdrops.utils.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TierBuilder {

	private final MythicDrops plugin;

	public TierBuilder(MythicDrops plugin) {
		this.plugin = plugin;
	}

	public void build() {
		getPlugin().getTierManager().getTiers().clear();
		FileConfiguration fc = getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.TIER);
		for (String tierName : fc.getKeys(false)) {
			if (!fc.isConfigurationSection(tierName)) {
                continue;
            }
			ConfigurationSection cs = fc.getConfigurationSection(tierName);
			if (!cs.isSet("displayName")) {
				cs.set("displayName", tierName);
			}
			String displayName = cs.getString("displayName");
			ChatColor displayColor;
			ChatColor identifierColor;
			try {
				if (!cs.isSet("displayColor")) {
					cs.set("displayColor", "WHITE");
				}
				displayColor = ChatColor.valueOf(cs.getString("displayColor"));
			} catch (Exception e) {
				displayColor = ChatColor.WHITE;
			}
			try {
				if (!cs.isSet("identifierColor")) {
					cs.set("identifierColor", "WHITE");
				}
				identifierColor = ChatColor.valueOf(cs.getString("identifierColor"));
			} catch (Exception e) {
				identifierColor = ChatColor.WHITE;
			}
			if (displayColor.equals(identifierColor)) {
				getPlugin().getLogger().warning("Could not load tier " + tierName +
						" because it conflicts with new naming rules. Please ensure that the displayColor and" +
						" identifierColor fields are not the same and both are legitimate colors.");
				continue;
			}
			if (!cs.isConfigurationSection("enchantments")) {
				cs.createSection("enchantments");
			}
			ConfigurationSection enchCS = cs.getConfigurationSection("enchantments");
			if (!enchCS.isSet("safeBaseEnchantments")) {
				enchCS.set("safeBaseEnchantments", true);
			}
			boolean safeBaseEnchantments = enchCS.getBoolean("safeBaseEnchantments");
			if (!enchCS.isSet("safeBonusEnchantments")) {
				enchCS.set("safeBonusEnchantments", true);
			}
			boolean safeBonusEnchantments = enchCS.getBoolean("safeBonusEnchantments");
			if (!enchCS.isSet("minimumBonusEnchantments")) {
				enchCS.set("minimumBonusEnchantments", 0);
			}
			int minimumBonusEnchantments = enchCS.getInt("minimumBonusEnchantments");
			if (!enchCS.isSet("maximumBonusEnchantments")) {
				enchCS.set("maximumBonusEnchantments", 0);
			}
			int maximumBonusEnchantments = enchCS.getInt("maximumBonusEnchantments");
			Set<MythicEnchantment> baseEnchantments = new HashSet<MythicEnchantment>();
			if (!enchCS.isSet("baseEnchantments")) {
				enchCS.set("baseEnchantments", new ArrayList<String>());
			}
			List<String> enchStrings = enchCS.getStringList("baseEnchantments");
			for (String s : enchStrings) {
				String[] strings = s.split(":");
				if (strings.length < 3) {
					continue;
				}
				Enchantment enchantment;
				try {
					enchantment = Enchantment.getByName(strings[0]);
				} catch (Exception e) {
					continue;
				}
				int minimum = NumberUtils.getInt(strings[1], 0);
				int maximum = NumberUtils.getInt(strings[2], 0);
				baseEnchantments.add(new MythicEnchantment(enchantment, minimum, maximum));
			}
			Set<MythicEnchantment> bonusEnchantments = new HashSet<MythicEnchantment>();
			if (!enchCS.isSet("bonusEnchantments")) {
				enchCS.set("bonusEnchantments", new ArrayList<String>());
			}
			enchStrings = enchCS.getStringList("bonusEnchantments");
			for (String s : enchStrings) {
				String[] strings = s.split(":");
				if (strings.length < 3) {
					continue;
				}
				Enchantment enchantment;
				try {
					enchantment = Enchantment.getByName(strings[0]);
				} catch (Exception e) {
					continue;
				}
				int minimum = NumberUtils.getInt(strings[1], 0);
				int maximum = NumberUtils.getInt(strings[2], 0);
				bonusEnchantments.add(new MythicEnchantment(enchantment, minimum, maximum));
			}
			if (!cs.isSet("chanceToSpawnOnAMonster")) {
				cs.set("chanceToSpawnOnAMonster", 0.0);
			}
			double chanceToSpawnOnAMonster = cs.getDouble("chanceToSpawnOnAMonster");
			if (!cs.isSet("chanceToDropOnMonsterDeath")) {
				cs.set("chanceToDropOnMonsterDeath", 0.0);
			}
			double chanceToDropOnMonsterDeath = cs.getDouble("chanceToDropOnMonsterDeath");
			if (!cs.isSet("minimumDurability")) {
				cs.set("minimumDurability", 1.0);
			}
			double minimumDurability = cs.getDouble("minimumDurability");
			if (!cs.isSet("maximumDurability")) {
				cs.set("maximumDurability", 1.0);
			}
			double maximumDurability = cs.getDouble("maximumDurability");
			if (!cs.isSet("minimumSockets")) {
				cs.set("minimumSockets", 0);
			}
			int minimumSockets = cs.getInt("minimumSockets");
			if (!cs.isSet("maximumSockets")) {
				cs.set("maximumSockets", 0);
			}
			int maximumSockets = cs.getInt("maximumSockets");
			if (!cs.isSet("itemTypes.allowedGroups")) {
				cs.set("itemTypes.allowedGroups", new ArrayList<String>());
			}
			Set<String> allowedGroups = new LinkedHashSet<String>(cs.getStringList("itemTypes.allowedGroups"));
			if (!cs.isSet("itemTypes.disallowedGroups")) {
				cs.set("itemTypes.disallowedGroups", new ArrayList<String>());
			}
			Set<String> disallowedGroups = new LinkedHashSet<String>(cs.getStringList("itemTypes.disallowedGroups"));
			if (!cs.isSet("itemTypes.allowedIds")) {
				cs.set("itemTypes.allowedIds", new ArrayList<String>());
			}
			Set<String> allowedIds = new LinkedHashSet<String>(cs.getStringList("itemTypes.allowedIds"));
			if (!cs.isSet("itemTypes.disallowedIds")) {
				cs.set("itemTypes.disallowedIds", new ArrayList<String>());
			}
			Set<String> disallowedIds = new LinkedHashSet<String>(cs.getStringList("itemTypes.disallowedIds"));
			Tier tier = new Tier(tierName, displayName, displayColor, identifierColor, safeBaseEnchantments,
					safeBonusEnchantments, minimumBonusEnchantments, maximumBonusEnchantments, baseEnchantments,
					bonusEnchantments, chanceToSpawnOnAMonster, chanceToDropOnMonsterDeath, minimumDurability,
					maximumDurability, minimumSockets, maximumSockets, allowedGroups, disallowedGroups, allowedIds,
					disallowedIds);
			getPlugin().getTierManager().getTiers().add(tier);
		}
		getPlugin().getConfigurationManager().saveConfig();
	}

	/**
	 * @return the plugin
	 */
	public MythicDrops getPlugin() {
		return plugin;
	}

}
