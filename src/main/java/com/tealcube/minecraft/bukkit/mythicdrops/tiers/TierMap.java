package com.tealcube.minecraft.bukkit.mythicdrops.tiers;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;

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
