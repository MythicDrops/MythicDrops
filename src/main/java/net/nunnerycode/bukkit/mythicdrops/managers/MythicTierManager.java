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

package net.nunnerycode.bukkit.mythicdrops.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.managers.TierManager;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.tiers.DefaultTier;
import net.nunnerycode.bukkit.mythicdrops.utils.RandomRangeUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MythicTierManager implements TierManager {

    private final MythicDrops plugin;
    private final Set<Tier> tiers;

    public MythicTierManager(MythicDrops plugin) {
        this.plugin = plugin;
        tiers = new LinkedHashSet<Tier>();
    }

    public Set<Tier> getTiersFromStringSet(Set<String> set) {
        Set<Tier> tierSet = new HashSet<Tier>();
        Tier t;
        for (String s : set) {
			try {
            	t = getTierFromName(s);
			} catch (NullPointerException e) {
				try {
					t = getTierFromDisplayName(s);
				} catch (NullPointerException e1) {
					t = null;
				}
			}
            if (t != null) {
                tierSet.add(t);
            }
        }
        return tierSet;
    }

    public Tier getTierFromName(String name) throws NullPointerException {
        for (Tier t : tiers) {
            if (t.getTierName().equalsIgnoreCase(name)) {
                return t;
            }
        }
        throw new NullPointerException(name + " is not the name of a loaded Tier");
    }

    public Tier getTierFromDisplayName(String displayName) throws NullPointerException {
        for (Tier t : tiers) {
            if (t.getTierDisplayName().equalsIgnoreCase(displayName)) {
                return t;
            }
        }
        throw new NullPointerException(displayName + " is not the display name of a loaded Tier");
    }

    public Tier getTierFromColors(ChatColor displayColor, ChatColor identificationColor) throws NullPointerException {
        for (Tier t : tiers) {
            if (t.getTierDisplayColor().equals(displayColor) && t.getTierIdentificationColor().equals(identificationColor)) {
                return t;
            }
        }
        throw new NullPointerException(displayColor.name() + " and " + identificationColor.name() + " do not identify a Tier");
    }

    public void debugTiers() {
        List<String> tierNames = new ArrayList<String>();
        for (Tier t : tiers) {
            tierNames.add(t.getTierName() + " (" + t.getChanceToSpawnOnAMonster() + ")");
        }
        getPlugin().debug(Level.INFO, "Loaded tiers: " + tierNames.toString().replace("[",
                "").replace("]", ""));
    }

    public MythicDrops getPlugin() {
        return plugin;
    }

    /**
     * Returns a random {@link Tier} from a Set containing both the loaded Tiers and the Tiers from {@link
     * net.nunnerycode.bukkit.mythicdrops.tiers.DefaultTier}.
     *
     * @return random Tier from loaded Tiers and DefaultTier
     */
    public Tier getFilteredRandomTier() {
        return getFilteredRandomTierFromSet(tiers);
    }

    public Tier getFilteredRandomTierFromSet(Set<Tier> tier) {
        Set<Tier> filteredTiers = new HashSet<Tier>(tier);
        Collections.addAll(filteredTiers, DefaultTier.values());
        return getRandomTierFromSet(filteredTiers);
    }

    /**
     * Returns a random {@link Tier} from a {@link Set} that is passed in.
     *
     * @param tiers Set of Tiers to choose from
     * @return random Tier
     */
    public Tier getRandomTierFromSet(Set<Tier> tiers) {
        Tier[] array = tiers.toArray(new Tier[tiers.size()]);
        return array[(int) RandomRangeUtils.randomRangeLongExclusive(0, array.length)];
    }

    /**
     * Returns a random {@link Tier} from a Set containing both the loaded Tiers and the Tiers from {@link DefaultTier}
     * after checking the Tier's chance against a random number.
     *
     * @return random Tier from loaded Tiers and DefaultTier
     */
    public Tier getFilteredRandomTierWithChance() {
        return getFilteredRandomTierFromSetWithChance(tiers);
    }

    public Tier getFilteredRandomTierFromSetWithChance(Set<Tier> tier) {
        Set<Tier> filteredTiers = new HashSet<Tier>(tier);
        Collections.addAll(filteredTiers, DefaultTier.values());
        return getRandomTierFromSetWithChance(filteredTiers);
    }

    /**
     * Returns a random {@link Tier} from a {@link Set} that is passed in after checking the Tier's chance against a
     * random number.
     *
     * @param tiers Set of Tiers to choose from
     * @return random tier
     */
    public Tier getRandomTierFromSetWithChance(Set<Tier> tiers) {
        Tier tier = null;
        Tier t;
        Set<Tier> zeroChanceTiers = new HashSet<Tier>();
        while (tier == null && zeroChanceTiers.size() != tiers.size()) {
            t = getRandomTierFromSet(tiers);
            if (t.getChanceToSpawnOnAMonster() <= 0.0) {
                zeroChanceTiers.add(t);
                continue;
            }
            if (RandomRangeUtils.randomRangeDoubleInclusive(0.0, 1.0) <= t.getChanceToSpawnOnAMonster()) {
                tier = t;
            }
        }
        return tier;
    }

    /**
     * Returns a random {@link Tier} from the {@link Set} of Tiers that the plugin has loaded. <br></br> Essentially the
     * same as using {@code getRandomTierFromSet(getTiers())}.
     *
     * @return random Tier from loaded Tiers
     */
    public Tier getRandomTier() {
        return getRandomTierFromSet(tiers);
    }

    /**
     * Returns a random {@link Tier} from the {@link Set} of Tiers that the plugin has loaded after checking the Tier's
     * chance to spawn on a monster against a random number. <br></br> Essentially the same as using {@code
     * getRandomTierFromSetWithChance(getTiers())}.
     *
     * @return random Tier from loaded Tiers
     */
    public Tier getRandomTierWithChance() {
        return getRandomTierFromSetWithChance(tiers);
    }

    private ChatColor findColor(final String s) {
        char[] c = s.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == (char) 167 && (i + 1) < c.length) {
                return ChatColor.getByChar(c[i + 1]);
            }
        }
        return null;
    }

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
        if (initColor == null || endColor == null) {
            return null;
        }
        for (Tier t : tiers) {
            if (t.getTierDisplayColor() != null && t.getTierIdentificationColor() != null && t.getTierDisplayColor()
                    == initColor && t.getTierIdentificationColor() == endColor) {
                return t;
            }
        }
        if (initColor == DefaultTier.CUSTOM_ITEM.getTierDisplayColor() && endColor
                == DefaultTier.CUSTOM_ITEM.getTierIdentificationColor()) {
            return DefaultTier.CUSTOM_ITEM;
        }
        return null;
    }

    public Set<Tier> getTiers() {
        return tiers;
    }
}