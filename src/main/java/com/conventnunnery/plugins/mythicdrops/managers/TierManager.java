/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.mythicdrops.managers;

import com.conventnunnery.plugins.conventlib.utils.RandomUtils;
import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.objects.MythicEnchantment;
import com.conventnunnery.plugins.mythicdrops.objects.Tier;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * The type TierManager.
 */
public class TierManager {

    private final List<Tier> tiers;
    private final MythicDrops plugin;
    private final Tier socketGemTier;
    private final Tier unidentifiedItemTier;
    private final Tier identityTomeTier;

    /**
     * Instantiates a new TierManager.
     *
     * @param plugin the plugin
     */
    public TierManager(MythicDrops plugin) {
        this.plugin = plugin;
        tiers = new ArrayList<Tier>();
        socketGemTier = new Tier("SocketGem", "Socket Gem", ChatColor.GOLD, ChatColor.GOLD, true, true, 0, 0,
                new HashSet<MythicEnchantment>(), new HashSet<MythicEnchantment>(), 0.0, 1.0, 1.0, 1.0, 0, 0,
                new LinkedHashSet<String>(), new LinkedHashSet<String>(), new LinkedHashSet<String>(),
                new LinkedHashSet<String>());
        unidentifiedItemTier =
                new Tier("UnidentifiedItem", "Unidentified Item", ChatColor.WHITE, ChatColor.WHITE, true, true, 0, 0,
                        new HashSet<MythicEnchantment>(), new HashSet<MythicEnchantment>(), 0.0, 1.0, 1.0, 1.0, 0, 0,
                        new LinkedHashSet<String>(), new LinkedHashSet<String>(), new LinkedHashSet<String>(),
                        new LinkedHashSet<String>());
        identityTomeTier =
                new Tier("IdentityTome", "Identity Tome", ChatColor.DARK_AQUA, ChatColor.DARK_AQUA, true, true, 0, 0,
                        new HashSet<MythicEnchantment>(), new HashSet<MythicEnchantment>(), 0.0, 1.0, 1.0, 1.0, 0, 0,
                        new LinkedHashSet<String>(), new LinkedHashSet<String>(), new LinkedHashSet<String>(),
                        new LinkedHashSet<String>());
    }

    public Tier getIdentityTomeTier() {
        return identityTomeTier;
    }

    public Tier getSocketGemTier() {
        return socketGemTier;
    }

    public Tier getUnidentifiedItemTier() {
        return unidentifiedItemTier;
    }

    /**
     * Add tier.
     *
     * @param tier the tier
     */
    public void addTier(Tier tier) {
        tiers.add(tier);
    }

    /**
     * Debug tiers.
     */
    public void debugTiers() {
        List<String> tierNames = new ArrayList<String>();
        for (Tier t : tiers) {
            tierNames.add(t.getName() + " (" + t.getChanceToSpawnOnAMonster() + ")");
        }
        getPlugin().getDebug().debug(
                "Loaded tiers: "
                        + tierNames.toString().replace("[", "")
                        .replace("]", ""));
    }

    /**
     * Gets plugin.
     *
     * @return the plugin
     */
    public MythicDrops getPlugin() {
        return plugin;
    }

    /**
     * Gets tier from name.
     *
     * @param name the name
     * @return the tier from name
     */
    public Tier getTierFromName(String name) {
        for (Tier t : tiers) {
            if (t.getName().equalsIgnoreCase(name)) {
                return t;
            }
        }
        return null;
    }

    public Tier getRandomTierWithChance(Collection<Tier> tiers) {
        Tier t = null;
        int attempts = 0;
        while (t == null && attempts < 10) {
            Tier tier = tiers.toArray(new Tier[tiers.size()])[(int) RandomUtils.randomRangeWholeExclusive(0,
                    tiers.size())];
            if (tier.getChanceToSpawnOnAMonster() < RandomUtils.randomRangeDecimalExclusive(0.0, 1.0)) {
                t = tier;
            }
        }
        return t;
    }

    /**
     * Gets tiers.
     *
     * @return the tiers
     */
    public List<Tier> getTiers() {
        return tiers;
    }

    /**
     * Random tier.
     *
     * @return the tier
     */
    @SuppressWarnings("unused")
    public Tier randomTier() {
        return tiers.get(getPlugin().getRandom().nextInt(tiers.size()));
    }

    /**
     * Random tier with chance.
     *
     * @return the tier
     */
    public Tier randomTierWithChance() {
        Tier tier = null;
        if (tiers == null || tiers.isEmpty()) {
            return tier;
        }
        while (tier == null) {
            for (Tier t : tiers) {
                double d = plugin.getRandom().nextDouble();
                if (d <= t.getChanceToSpawnOnAMonster()) {
                    tier = t;
                    break;
                }
            }
        }
        return tier;
    }

    /**
     * Gets tier from ItemStack.
     *
     * @param itemStack the item stack
     * @return the tier from item stack
     */
    public Tier getTierFromItemStack(ItemStack itemStack) {
        ItemMeta im;
        if (itemStack.hasItemMeta()) {
            im = itemStack.getItemMeta();
        } else {
            return null;
        }
        String name;
        if (im.hasDisplayName()) {
            name = im.getDisplayName();
        } else {
            return null;
        }
        ChatColor initColor = findColor(name);
        String colors = ChatColor.getLastColors(name);
        ChatColor endColor = ChatColor.getLastColors(name).contains(String.valueOf(ChatColor.COLOR_CHAR)) ?
                ChatColor.getByChar(colors.substring(1, 2)) : null;
        for (Tier t : tiers) {
            if (t.getDisplayColor() != null && t.getIdentificationColor() != null && t.getDisplayColor() == initColor &&
                    t.getIdentificationColor() == endColor) {
                return t;
            }
        }
        return null;
    }

    /**
     * Find color.
     *
     * @param s the s
     * @return the chat color
     */
    public ChatColor findColor(final String s) {
        char[] c = s.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == (char) 167 && (i + 1) < c.length) {
                return ChatColor.getByChar(c[i + 1]);
            }
        }
        return null;
    }

    /**
     * Remove tier.
     *
     * @param tier the tier
     */
    @SuppressWarnings("unused")
    public void removeTier(Tier tier) {
        if (tiers.contains(tier)) {
            tiers.remove(tier);
        }
    }

}
