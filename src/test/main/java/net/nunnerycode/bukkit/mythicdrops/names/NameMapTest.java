package net.nunnerycode.bukkit.mythicdrops.names;

import net.nunnerycode.bukkit.mythicdrops.api.names.NameType;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
