/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.tiers;

import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.RandomUtils;

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
        if (values().size() == 0) {
            return null;
        }

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
        return valueArray[RandomUtils.nextInt(0, values().size())];
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
