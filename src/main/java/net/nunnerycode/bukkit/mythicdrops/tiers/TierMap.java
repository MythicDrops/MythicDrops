package net.nunnerycode.bukkit.mythicdrops.tiers;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.apache.commons.lang.math.RandomUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class TierMap extends ConcurrentHashMap<String, Tier> {

    private static final TierMap _INSTANCE = new TierMap();

    private TierMap() {
        // do nothing
    }

    /**
     * Gets the instance of CustomItemMap running on the server.
     *
     * @return instance running on the server
     */
    public static TierMap getInstance() {
        return _INSTANCE;
    }

    @Deprecated
    public Tier getRandomWithChance(String worldName) {
        return getRandomWithChance();
    }

    /**
     * Gets a random {@link Tier} out of the ones loaded on the server using chance. Returns null if none found.
     *
     * @return random Tier
     */
    public Tier getRandomWithChance() {
        double totalWeight = 0;
        List<Tier> v = new ArrayList<>(values());
        Collections.shuffle(v);
        for (Tier t : v) {
            totalWeight += t.getSpawnChance();
        }

        double chosenWeight = MythicDropsPlugin.getInstance().getRandom().nextDouble() * totalWeight;

        double currentWeight = 0;

        for (Tier t : v) {
            currentWeight += t.getSpawnChance();

            if (currentWeight >= chosenWeight) {
                return t;
            }
        }

        return null;
    }

    /**
     * Gets a random {@link Tier} out of the ones loaded on the server.
     *
     * @return random Tier
     */
    public Tier getRandom() {
        Tier[] valueArray = values().toArray(new Tier[values().size()]);
        return valueArray[RandomUtils.nextInt(values().size())];
    }

    @Deprecated
    public Tier getRandomWithIdentifyChance(String worldName) {
        return getRandomWithIdentifyChance();
    }

    public Tier getRandomWithIdentifyChance() {
        double totalWeight = 0;
        List<Tier> v = new ArrayList<>(values());
        Collections.shuffle(v);
        for (Tier t : v) {
            totalWeight += t.getIdentifyChance();
        }

        double chosenWeight = MythicDropsPlugin.getInstance().getRandom().nextDouble() * totalWeight;

        double currentWeight = 0;

        for (Tier t : v) {
            currentWeight += t.getIdentifyChance();

            if (currentWeight >= chosenWeight) {
                return t;
            }
        }

        return null;
    }

}
