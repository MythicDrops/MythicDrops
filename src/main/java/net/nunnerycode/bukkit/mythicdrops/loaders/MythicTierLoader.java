/*
 * Copyright (C) 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.nunnerycode.bukkit.mythicdrops.loaders;

import com.conventnunnery.libraries.config.ConventYamlConfiguration;
import net.nunnerycode.bukkit.libraries.utils.ChatColorUtils;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicEnchantment;
import net.nunnerycode.bukkit.mythicdrops.api.utils.MythicLoader;
import net.nunnerycode.bukkit.mythicdrops.configuration.MythicConfigurationFile;
import net.nunnerycode.bukkit.mythicdrops.tiers.MythicTier;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;

import java.util.HashSet;
import java.util.List;

public class MythicTierLoader implements MythicLoader {

    private final MythicDrops plugin;

    public MythicTierLoader(final MythicDrops plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        getPlugin().getTierManager().getTiers().clear();
        ConventYamlConfiguration configuration = plugin.getConfigurationManager()
                .getConfiguration(MythicConfigurationFile.TIER);
        for (String key : configuration.getKeys(false)) {
            // Check if the key has other fields under it and if not, move on to the next
            if (!configuration.isConfigurationSection(key)) {
                continue;
            }
            ConfigurationSection cs = configuration.getConfigurationSection(key);
            MythicTier tier = new MythicTier();
            // Start loading settings
            tier.setTierName(key);
            tier.setTierDisplayName(cs.getString("displayName", key));
            try {
                tier.setTierDisplayColor(
                        ChatColorUtils.getChatColorOrFallback(cs.getString("displayColor"), tier.getTierDisplayColor()));
            tier.setTierIdentificationColor(ChatColorUtils
                    .getChatColorOrFallback(cs.getString("identifierColor"), tier.getTierIdentificationColor()));
            } catch (RuntimeException e) {
                getPlugin().getLogger().warning("Tier " + key + " cannot be loaded due to displayColor and " +
                        "identificationColor being the same.");
                continue;
            }
            // Start loading enchantments
            ConfigurationSection enchcs = cs.getConfigurationSection("enchantments");
            if (enchcs != null) {
                tier.setSafeBaseEnchantments(enchcs.getBoolean("safeBaseEnchantments", true));
                tier.setSafeBonusEnchantments(enchcs.getBoolean("safeBonusEnchantments", true));
                tier.setAllowHighBaseEnchantments(enchcs.getBoolean("allowHighBaseEnchantments", true));
                tier.setAllowHighBonusEnchantments(enchcs.getBoolean("allowHighBonusEnchantments", true));
                tier.setMinimumAmountOfBonusEnchantments(enchcs.getInt("minimumBonusEnchantments", 0));
                tier.setMaximumAmountOfBonusEnchantments(enchcs.getInt("maximumBonusEnchantments", 0));
                List<String> baseEnchantStrings = enchcs.getStringList("baseEnchantments");
                for (String s : baseEnchantStrings) {
                    Enchantment ench;
                    int value1, value2;
                    String[] split = s.split(":");
                    MythicEnchantment mythicEnchantment;
                    switch (split.length) {
                        case 0:
                            continue;
                        case 1:
                            continue;
                        case 2:
                            ench = Enchantment.getByName(split[0]);
                            if (ench == null) {
                                continue;
                            }
                            value1 = value2 = NumberUtils.toInt(split[1], 1);
                            mythicEnchantment = new MythicEnchantment(ench, value1, value2);
                            break;
                        default:
                            ench = Enchantment.getByName(split[0]);
                            if (ench == null) {
                                continue;
                            }
                            value1 = NumberUtils.toInt(split[1], 1);
                            value2 = NumberUtils.toInt(split[2], 1);
                            mythicEnchantment = new MythicEnchantment(ench, value1, value2);
                            break;
                    }
                    tier.getBaseEnchantments().add(mythicEnchantment);
                }
                List<String> bonusEnchantStrings = enchcs.getStringList("bonusEnchantments");
                for (String s : bonusEnchantStrings) {
                    Enchantment ench;
                    int value1, value2;
                    String[] split = s.split(":");
                    MythicEnchantment mythicEnchantment;
                    switch (split.length) {
                        case 0:
                            continue;
                        case 1:
                            continue;
                        case 2:
                            ench = Enchantment.getByName(split[0]);
                            if (ench == null) {
                                continue;
                            }
                            value1 = value2 = NumberUtils.toInt(split[1], 1);
                            mythicEnchantment = new MythicEnchantment(ench, value1, value2);
                            break;
                        default:
                            ench = Enchantment.getByName(split[0]);
                            if (ench == null) {
                                continue;
                            }
                            value1 = NumberUtils.toInt(split[1], 1);
                            value2 = NumberUtils.toInt(split[2], 1);
                            mythicEnchantment = new MythicEnchantment(ench, value1, value2);
                            break;
                    }
                    tier.getBonusEnchantments().add(mythicEnchantment);
                }
                tier.setChanceToSpawnOnAMonster(cs.getDouble("chanceToSpawnOnAMonster", 0.0));
                tier.setChanceToDropOnMonsterDeath(cs.getDouble("chanceToDropOnMonsterDeath", 1.0));
                tier.setChanceToBeIdentified(cs.getDouble("chanceToBeIdentified", 0.0));
                tier.setMinimumDurabilityPercentage(cs.getDouble("minimumDurability", 1.0));
                tier.setMaximumDurabilityPercentage(cs.getDouble("maximumDurability", 1.0));
                tier.setMinimumAmountOfSockets(cs.getInt("minimumSockets", 0));
                tier.setMaximumAmountOfSockets(cs.getInt("maximumSockets", 0));
                tier.setAllowedGroups(new HashSet<String>(cs.getStringList("itemTypes.allowedGroups")));
                tier.setAllowedIds(new HashSet<String>(cs.getStringList("itemTypes.allowedIds")));
                tier.setDisallowedGroups(new HashSet<String>(cs.getStringList("itemTypes.disallowedGroups")));
                tier.setDisallowedIds(new HashSet<String>(cs.getStringList("itemTypes.disallowedIds")));
                getPlugin().getTierManager().getTiers().add(tier);
            }
        }
    }

    public MythicDrops getPlugin() {
        return plugin;
    }
}
