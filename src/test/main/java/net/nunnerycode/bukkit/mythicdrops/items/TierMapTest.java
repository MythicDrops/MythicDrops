package net.nunnerycode.bukkit.mythicdrops.items;

import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.tiers.MythicTierBuilder;
import net.nunnerycode.bukkit.mythicdrops.tiers.TierMap;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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
