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

import com.conventnunnery.libraries.utils.RandomUtils;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.tiers.DefaultTier;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class TierManager {

    private final MythicDrops plugin;
    private final Set<Tier> tiers;

    public TierManager(MythicDrops plugin) {
        this.plugin = plugin;
        tiers = new HashSet<Tier>();
    }

    public Tier getTierFromColors(ChatColor displayColor, ChatColor identificationColor) {
        for (Tier t : tiers) {
            if (t.getTierDisplayColor().equals(displayColor) && t.getTierIdentificationColor().equals(identificationColor)) {
                return t;
            }
        }
        throw new NullPointerException(displayColor.name() + " and " + identificationColor.name() + " do not identify a Tier");
    }

    public Tier getTierFromDisplayName(String displayName) {
        for (Tier t : tiers) {
            if (t.getTierDisplayName().equalsIgnoreCase(displayName)) {
                return t;
            }
        }
        throw new NullPointerException(displayName + " is not the display name of a loaded Tier");
    }

    public Tier getTierFromName(String name) {
        for (Tier t : tiers) {
            if (t.getTierName().equalsIgnoreCase(name)) {
                return t;
            }
        }
        throw new NullPointerException(name + " is not the name of a loaded Tier");
    }

    public void debugTiers() {
        List<String> tierNames = new ArrayList<String>();
        for (Tier t : tiers) {
            tierNames.add(t.getTierName() + " (" + t.getChanceToSpawnOnAMonster() + ")");
        }
        getPlugin().debug(Level.INFO, "Loaded tiers: " + tierNames.toString().replace("[",
                "").replace("]", ""));
    }

    /**
     * Returns a random {@link Tier} from a Set containing both the loaded Tiers and the Tiers from {@link
     * net.nunnerycode.bukkit.mythicdrops.tiers.DefaultTier}.
     *
     * @return random Tier from loaded Tiers and DefaultTier
     */
    public Tier getFilteredRandomTier() {
        Set<Tier> filteredTiers = new HashSet<Tier>(tiers);
        Collections.addAll(filteredTiers, DefaultTier.values());
        return getRandomTierFromSet(filteredTiers);
    }

    /**
     * Returns a random {@link Tier} from a Set containing both the loaded Tiers and the Tiers from {@link DefaultTier}
     * after checking the Tier's chance against a random number.
     *
     * @return random Tier from loaded Tiers and DefaultTier
     */
    public Tier getFilteredRandomTierWithChance() {
        Set<Tier> filteredTiers = new HashSet<Tier>(tiers);
        Collections.addAll(filteredTiers, DefaultTier.values());
        return getRandomTierFromSetWithChance(filteredTiers);
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

    /**
     * Returns a random {@link Tier} from a {@link Set} that is passed in.
     *
     * @param tiers Set of Tiers to choose from
     * @return random Tier
     */
    public Tier getRandomTierFromSet(Set<Tier> tiers) {
        Tier[] array = tiers.toArray(new Tier[tiers.size()]);
        return array[(int) RandomUtils.randomRangeWholeExclusive(0, array.length)];
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
        Tier t = null;
        Set<Tier> zeroChanceTiers = new HashSet<Tier>();
        while (tier == null && zeroChanceTiers.size() != tiers.size()) {
            t = getRandomTierFromSet(tiers);
            if (t.getChanceToSpawnOnAMonster() <= 0.0) {
                zeroChanceTiers.add(t);
                continue;
            }
            if (RandomUtils.randomRangeDecimalInclusive(0.0, 1.0) <= t.getChanceToSpawnOnAMonster()) {
                tier = t;
            }
        }
        return tier;
    }

    public MythicDrops getPlugin() {
        return plugin;
    }

    public Set<Tier> getTiers() {
        return tiers;
    }
}
