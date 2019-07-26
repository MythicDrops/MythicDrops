/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2019 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.names;

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


import com.tealcube.minecraft.bukkit.mythicdrops.api.names.NameType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class NameMapTest {

    @Test
    public void testGetMatchingKeys() throws Exception {
        NameMap nameMap = NameMap.getInstance();
        nameMap.clear();

        nameMap.put(NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_SWORD",
                Arrays.asList("foo", "bar", "foobar"));
        nameMap.put(NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_PICKAXE",
                Arrays.asList("foo", "bar", "foobar"));
        nameMap.put(NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_AXE",
                Arrays.asList("foo", "bar", "foobar"));
        nameMap.put(NameType.MATERIAL_SUFFIX.getFormat() + "DIAMOND_SWORD",
                Arrays.asList("foo", "bar", "foobar"));

        List<String> matchingKeys = nameMap.getMatchingKeys(NameType.MATERIAL_PREFIX);

        Assert.assertNotNull(matchingKeys);
        Assert
                .assertTrue(matchingKeys.contains(NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_SWORD"));
        Assert.assertTrue(
                matchingKeys.contains(NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_PICKAXE"));
        Assert.assertTrue(matchingKeys.contains(NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_AXE"));
        Assert
                .assertFalse(matchingKeys.contains(NameType.MATERIAL_SUFFIX.getFormat() + "DIAMOND_SWORD"));
    }

    @Test
    public void testGetRandom() throws Exception {
        NameMap nameMap = NameMap.getInstance();
        nameMap.clear();

        nameMap.put(NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_SWORD",
                Arrays.asList("foo", "bar", "foobar"));
        nameMap.put(NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_PICKAXE",
                Arrays.asList("foo", "bar", "foobar"));
        nameMap.put(NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_AXE",
                Arrays.asList("foo", "bar", "foobar"));

        int numOfRuns = 1000;
        for (Map.Entry<String, List<String>> entry : nameMap.entrySet()) {
            String key = entry.getKey().replace(NameType.MATERIAL_PREFIX.getFormat(), "");
            int[] results = new int[3];
            for (int j = 0; j < numOfRuns; j++) {
                String s = nameMap.getRandom(NameType.MATERIAL_PREFIX, key);
                Assert.assertNotNull(s);
                Assert.assertNotEquals(s, "");

                if ("foo".equals(s)) {
                    results[0]++;
                } else if ("bar".equals(s)) {
                    results[1]++;
                } else if ("foobar".equals(s)) {
                    results[2]++;
                } else {
                    Assert.fail("Unexpected value");
                }
            }

            Assert.assertTrue(results[0] > 200);
            Assert.assertTrue(results[1] > 200);
            Assert.assertTrue(results[2] > 200);
        }
    }

    @Test
    public void testGetRandomKey() throws Exception {
        NameMap nameMap = NameMap.getInstance();
        nameMap.clear();

        nameMap.put(NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_SWORD",
                Arrays.asList("foo", "bar", "foobar"));
        nameMap.put(NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_PICKAXE",
                Arrays.asList("foo", "bar", "foobar"));
        nameMap.put(NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_AXE",
                Arrays.asList("foo", "bar", "foobar"));
        nameMap.put(NameType.MATERIAL_SUFFIX.getFormat() + "DIAMOND_SWORD",
                Arrays.asList("foo", "bar", "foobar"));

        int[] results = new int[3];
        int numOfRuns = 1000;
        for (int i = 0; i < numOfRuns; i++) {
            String key = nameMap.getRandomKey(NameType.MATERIAL_PREFIX);
            Assert.assertNotNull(key);

            if ("DIAMOND_SWORD".equals(key)) {
                results[0]++;
            } else if ("DIAMOND_PICKAXE".equals(key)) {
                results[1]++;
            } else if ("DIAMOND_AXE".equals(key)) {
                results[2]++;
            } else {
                Assert.fail("Unexpected value");
            }
        }

        Assert.assertTrue(results[0] > 200);
        Assert.assertTrue(results[1] > 200);
        Assert.assertTrue(results[2] > 200);
    }
}
