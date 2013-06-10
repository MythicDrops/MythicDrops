/*
 * Originally created by deathmarine
 * Modified by Nunnery on March 10, 2013
 */

package com.modcrafting.diablodrops.builders;

import com.conventnunnery.plugins.conventlib.utils.NumberUtils;
import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.configuration.MythicConfigurationFile;
import com.conventnunnery.plugins.mythicdrops.objects.MythicEnchantment;
import com.conventnunnery.plugins.mythicdrops.objects.Tier;
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
                .getConfiguration(MythicConfigurationFile.TIER);
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
            setDefaultEnchantmentSettings(enchCS);
            boolean safeBaseEnchantments = enchCS.getBoolean("safeBaseEnchantments");
            boolean safeBonusEnchantments = enchCS.getBoolean("safeBonusEnchantments");
            int minimumBonusEnchantments = enchCS.getInt("minimumBonusEnchantments");
            int maximumBonusEnchantments = enchCS.getInt("maximumBonusEnchantments");
            Set<MythicEnchantment> baseEnchantments = new HashSet<MythicEnchantment>();
            List<String> enchStrings = enchCS.getStringList("baseEnchantments");
            for (String s : enchStrings) {
                String[] strings = s.split(":");
                int zero = 0;
                if (strings.length <= zero) {
                    continue;
                }
                Enchantment enchantment;
                try {
                    enchantment = Enchantment.getByName(strings[0]);
                } catch (Exception e) {
                    continue;
                }
                int minimum = 0;
                int maximum = 0;
                if (strings.length > 1) {
                    minimum = NumberUtils.getInt(strings[1], 0);
                }
                if (strings.length > 2) {
                    maximum = NumberUtils.getInt(strings[2], 0);
                }
                baseEnchantments.add(new MythicEnchantment(enchantment, minimum, maximum));
            }
            Set<MythicEnchantment> bonusEnchantments = new HashSet<MythicEnchantment>();
            enchStrings = enchCS.getStringList("bonusEnchantments");
            for (String s : enchStrings) {
                String[] strings = s.split(":");
                int zero = 0;
                if (strings.length <= zero) {
                    continue;
                }
                Enchantment enchantment;
                try {
                    enchantment = Enchantment.getByName(strings[0]);
                } catch (Exception e) {
                    continue;
                }
                int minimum = 0;
                int maximum = 0;
                if (strings.length > 1) {
                    minimum = NumberUtils.getInt(strings[1], 0);
                }
                if (strings.length > 2) {
                    maximum = NumberUtils.getInt(strings[2], 0);
                }
                bonusEnchantments.add(new MythicEnchantment(enchantment, minimum, maximum));
            }
            setDefaultSettings(cs);
            double chanceToSpawnOnAMonster = cs.getDouble("chanceToSpawnOnAMonster");
            double chanceToDropOnMonsterDeath = cs.getDouble("chanceToDropOnMonsterDeath");
            double minimumDurability = cs.getDouble("minimumDurability");
            double maximumDurability = cs.getDouble("maximumDurability");
            int minimumSockets = cs.getInt("minimumSockets");
            int maximumSockets = cs.getInt("maximumSockets");
            Set<String> allowedGroups = new LinkedHashSet<String>(cs.getStringList("itemTypes.allowedGroups"));
            Set<String> disallowedGroups = new LinkedHashSet<String>(cs.getStringList("itemTypes.disallowedGroups"));
            Set<String> allowedIds = new LinkedHashSet<String>(cs.getStringList("itemTypes.allowedIds"));
            Set<String> disallowedIds = new LinkedHashSet<String>(cs.getStringList("itemTypes.disallowedIds"));
            double chanceToBeIdentified = cs.getDouble("chanceToBeIdentified");
            Tier tier = new Tier(tierName, displayName, displayColor, identifierColor, safeBaseEnchantments,
                    safeBonusEnchantments, minimumBonusEnchantments, maximumBonusEnchantments, baseEnchantments,
                    bonusEnchantments, chanceToSpawnOnAMonster, chanceToDropOnMonsterDeath, minimumDurability,
                    maximumDurability, minimumSockets, maximumSockets, allowedGroups, disallowedGroups, allowedIds,
                    disallowedIds, chanceToBeIdentified);
            getPlugin().getTierManager().getTiers().add(tier);
        }
        getPlugin().getConfigurationManager().saveConfig();
    }

    private void setDefaultSettings(final ConfigurationSection cs) {
        if (!cs.isSet("chanceToSpawnOnAMonster")) {
            cs.set("chanceToSpawnOnAMonster", 0.0);
        }
        if (!cs.isSet("chanceToDropOnMonsterDeath")) {
            cs.set("chanceToDropOnMonsterDeath", 0.0);
        }
        if (!cs.isSet("minimumDurability")) {
            cs.set("minimumDurability", 1.0);
        }
        if (!cs.isSet("maximumDurability")) {
            cs.set("maximumDurability", 1.0);
        }
        if (!cs.isSet("minimumSockets")) {
            cs.set("minimumSockets", 0);
        }
        if (!cs.isSet("maximumSockets")) {
            cs.set("maximumSockets", 0);
        }
        if (!cs.isSet("itemTypes.allowedGroups")) {
            cs.set("itemTypes.allowedGroups", new ArrayList<String>());
        }
        if (!cs.isSet("itemTypes.disallowedGroups")) {
            cs.set("itemTypes.disallowedGroups", new ArrayList<String>());
        }
        if (!cs.isSet("itemTypes.allowedIds")) {
            cs.set("itemTypes.allowedIds", new ArrayList<String>());
        }
        if (!cs.isSet("itemTypes.disallowedIds")) {
            cs.set("itemTypes.disallowedIds", new ArrayList<String>());
        }
        if (!cs.isSet("chanceToBeIdentified")) {
            cs.set("chanceToBeIdentified", 0.0);
        }
    }

    private void setDefaultEnchantmentSettings(final ConfigurationSection enchCS) {
        if (!enchCS.isSet("safeBaseEnchantments")) {
            enchCS.set("safeBaseEnchantments", true);
        }
        if (!enchCS.isSet("safeBonusEnchantments")) {
            enchCS.set("safeBonusEnchantments", true);
        }
        if (!enchCS.isSet("minimumBonusEnchantments")) {
            enchCS.set("minimumBonusEnchantments", 0);
        }
        if (!enchCS.isSet("maximumBonusEnchantments")) {
            enchCS.set("maximumBonusEnchantments", 0);
        }
        if (!enchCS.isSet("baseEnchantments")) {
            enchCS.set("baseEnchantments", new ArrayList<String>());
        }
        if (!enchCS.isSet("bonusEnchantments")) {
            enchCS.set("bonusEnchantments", new ArrayList<String>());
        }
    }

    /**
     * @return the plugin
     */
    public MythicDrops getPlugin() {
        return plugin;
    }

}
