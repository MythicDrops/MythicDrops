package net.nunnerycode.bukkit.mythicdrops.api.items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.nunnerycode.bukkit.mythicdrops.api.enchantments.MythicEnchantment;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.junit.Assert;
import org.junit.Test;

public class TierMapTest {

	@Test
	public void testGetRandom() throws Exception {
	    TierMap tierMap = TierMap.getInstance();

		tierMap.put("foo", new TierImp("foo"));
		tierMap.put("bar", new TierImp("bar"));
		tierMap.put("foobar", new TierImp("foobar"));

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

		tierMap.put("foo", new TierImp("foo", worldName, 0.25));
		tierMap.put("bar", new TierImp("bar", worldName, 0.50));
		tierMap.put("foobar", new TierImp("foobar", worldName, 0.25));

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

	class TierImp implements Tier {

		private String name;
		private Map<String, Double> worldSpawnChance;

		TierImp(String name) {
			this.name = name;
			worldSpawnChance = new HashMap<String, Double>();
		}

		TierImp(String name, String worldName, double rate) {
			this.name = name;
			worldSpawnChance = new HashMap<String, Double>();
			worldSpawnChance.put(worldName, rate);
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getDisplayName() {
			return null;
		}

		@Override
		public List<String> getBaseLore() {
			return null;
		}

		@Override
		public List<String> getBonusLore() {
			return null;
		}

		@Override
		public int getMinimumBonusLore() {
			return 0;
		}

		@Override
		public int getMaximumBonusLore() {
			return 0;
		}

		@Override
		public Set<MythicEnchantment> getBaseEnchantments() {
			return null;
		}

		@Override
		public Set<MythicEnchantment> getBonusEnchantements() {
			return null;
		}

		@Override
		public boolean isSafeBaseEnchantments() {
			return false;
		}

		@Override
		public boolean isSafeBonusEnchantments() {
			return false;
		}

		@Override
		public boolean isAllowHighBaseEnchantments() {
			return false;
		}

		@Override
		public boolean isAllowHighBonusEnchantments() {
			return false;
		}

		@Override
		public int getMinimumBonusEnchantments() {
			return 0;
		}

		@Override
		public int getMaximumBonusEnchantments() {
			return 0;
		}

		@Override
		public double getMaximumDurabilityPercentage() {
			return 0;
		}

		@Override
		public double getMinimumDurabilityPercentage() {
			return 0;
		}

		@Override
		public Map<String, Double> getWorldDropChanceMap() {
			return null;
		}

		@Override
		public Map<String, Double> getWorldSpawnChanceMap() {
			return worldSpawnChance;
		}

		@Override
		public List<String> getAllowedItemGroups() {
			return null;
		}

		@Override
		public List<String> getDisallowedItemGroups() {
			return null;
		}

		@Override
		public List<String> getAllowedItemIds() {
			return null;
		}

		@Override
		public List<String> getDisallowedItemIds() {
			return null;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof TierImp)) return false;

			TierImp tierImp = (TierImp) o;

			if (name != null ? !name.equals(tierImp.name) : tierImp.name != null) return false;

			return true;
		}

		@Override
		public int hashCode() {
			return name != null ? name.hashCode() : 0;
		}

	}
}
