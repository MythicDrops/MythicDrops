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

import com.tealcube.minecraft.bukkit.mythicdrops.api.names.NameType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NameMapTest {

  @Test
  public void testGetMatchingKeys() throws Exception {
    NameMap nameMap = NameMap.getInstance();
    nameMap.clear();

    nameMap.put(
        NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_SWORD",
        Arrays.asList("foo", "bar", "foobar"));
    nameMap.put(
        NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_PICKAXE",
        Arrays.asList("foo", "bar", "foobar"));
    nameMap.put(
        NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_AXE",
        Arrays.asList("foo", "bar", "foobar"));
    nameMap.put(
        NameType.MATERIAL_SUFFIX.getFormat() + "DIAMOND_SWORD",
        Arrays.asList("foo", "bar", "foobar"));

    List<String> matchingKeys = nameMap.getMatchingKeys(NameType.MATERIAL_PREFIX);

    Assertions.assertNotNull(matchingKeys);
    Assertions.assertTrue(
        matchingKeys.contains(NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_SWORD"));
    Assertions.assertTrue(
        matchingKeys.contains(NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_PICKAXE"));
    Assertions.assertTrue(
        matchingKeys.contains(NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_AXE"));
    Assertions.assertFalse(
        matchingKeys.contains(NameType.MATERIAL_SUFFIX.getFormat() + "DIAMOND_SWORD"));
  }

  @Test
  public void testGetRandom() throws Exception {
    NameMap nameMap = NameMap.getInstance();
    nameMap.clear();

    nameMap.put(
        NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_SWORD",
        Arrays.asList("foo", "bar", "foobar"));
    nameMap.put(
        NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_PICKAXE",
        Arrays.asList("foo", "bar", "foobar"));
    nameMap.put(
        NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_AXE",
        Arrays.asList("foo", "bar", "foobar"));

    int numOfRuns = 1000;
    for (Map.Entry<String, List<String>> entry : nameMap.entrySet()) {
      String key = entry.getKey().replace(NameType.MATERIAL_PREFIX.getFormat(), "");
      int[] results = new int[3];
      for (int j = 0; j < numOfRuns; j++) {
        String s = nameMap.getRandom(NameType.MATERIAL_PREFIX, key);
        Assertions.assertNotNull(s);
        Assertions.assertNotEquals(s, "");

        if ("foo".equals(s)) {
          results[0]++;
        } else if ("bar".equals(s)) {
          results[1]++;
        } else if ("foobar".equals(s)) {
          results[2]++;
        } else {
          Assertions.fail("Unexpected value");
        }
      }

      Assertions.assertTrue(results[0] > 200);
      Assertions.assertTrue(results[1] > 200);
      Assertions.assertTrue(results[2] > 200);
    }
  }

  @Test
  public void testGetRandomKey() throws Exception {
    NameMap nameMap = NameMap.getInstance();
    nameMap.clear();

    nameMap.put(
        NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_SWORD",
        Arrays.asList("foo", "bar", "foobar"));
    nameMap.put(
        NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_PICKAXE",
        Arrays.asList("foo", "bar", "foobar"));
    nameMap.put(
        NameType.MATERIAL_PREFIX.getFormat() + "DIAMOND_AXE",
        Arrays.asList("foo", "bar", "foobar"));
    nameMap.put(
        NameType.MATERIAL_SUFFIX.getFormat() + "DIAMOND_SWORD",
        Arrays.asList("foo", "bar", "foobar"));

    int[] results = new int[3];
    int numOfRuns = 1000;
    for (int i = 0; i < numOfRuns; i++) {
      String key = nameMap.getRandomKey(NameType.MATERIAL_PREFIX);
      Assertions.assertNotNull(key);

      if ("DIAMOND_SWORD".equals(key)) {
        results[0]++;
      } else if ("DIAMOND_PICKAXE".equals(key)) {
        results[1]++;
      } else if ("DIAMOND_AXE".equals(key)) {
        results[2]++;
      } else {
        Assertions.fail("Unexpected value");
      }
    }

    Assertions.assertTrue(results[0] > 200);
    Assertions.assertTrue(results[1] > 200);
    Assertions.assertTrue(results[2] > 200);
  }
}
