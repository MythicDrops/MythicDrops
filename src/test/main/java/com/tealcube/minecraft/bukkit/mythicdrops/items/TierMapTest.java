/**
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2013 Teal Cube Games
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
package com.tealcube.minecraft.bukkit.mythicdrops.items;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * # % L
 *
 * %%
 *
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby
 * granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER
 * IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF
 * THIS SOFTWARE.
 * # L %
 * #L%
 */


import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.MythicTierBuilder;
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.TierMap;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class TierMapTest {

    @Test
    public void testGetRandom() throws Exception {
        TierMap tierMap = TierMap.getInstance();

        tierMap.put("foo", new TierImp("foo").tier());
        tierMap.put("bar", new TierImp("bar").tier());
        tierMap.put("foobar", new TierImp("foobar").tier());

        int[] results = new int[3];
        int numOfRuns = 1000;
        for (int i = 0; i < numOfRuns; i++) {
            Tier t = tierMap.getRandom();
            Assert.assertNotNull(t);
            if ("foo".equals(t.getName())) {
                results[0]++;
            } else if ("bar".equals(t.getName())) {
                results[1]++;
            } else if ("foobar".equals(t.getName())) {
                results[2]++;
            } else {
                Assert.fail("Unexpected value");
            }
        }

        Assert.assertTrue(results[0] > 300);
        Assert.assertTrue(results[1] > 300);
        Assert.assertTrue(results[2] > 300);
    }

    @Test
    public void testGetRandomWithChance() throws Exception {
        TierMap tierMap = TierMap.getInstance();

        String worldName = "world";

        tierMap.put("foo", new TierImp("foo", worldName, 0.25).tier());
        tierMap.put("bar", new TierImp("bar", worldName, 0.50).tier());
        tierMap.put("foobar", new TierImp("foobar", worldName, 0.25).tier());

        int[] results = new int[3];
        int numOfRuns = 1000;
        for (int i = 0; i < numOfRuns; i++) {
            Tier t = tierMap.getRandomWithChance(worldName);
            Assert.assertNotNull(t);
            if ("foo".equals(t.getName())) {
                results[0]++;
            } else if ("bar".equals(t.getName())) {
                results[1]++;
            } else if ("foobar".equals(t.getName())) {
                results[2]++;
            } else {
                Assert.fail("Unexpected value");
            }
        }

        Assert.assertTrue(results[0] > 200);
        Assert.assertTrue(results[1] > 400);
        Assert.assertTrue(results[2] > 200);
    }

    class TierImp {

        private Tier tier = null;

        protected TierImp(String name) {
            tier = new MythicTierBuilder(name).build();
        }

        protected TierImp(String name, String worldName, double chance) {
            Map<String, Double> worldChances = new HashMap<>();
            worldChances.put(worldName, chance);
            tier = new MythicTierBuilder(name).withWorldSpawnChanceMap(worldChances).build();
        }

        protected Tier tier() {
            return tier;
        }

    }
}
