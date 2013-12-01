package net.nunnerycode.bukkit.mythicdrops.api.items;

import java.util.List;
import java.util.Map;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.junit.Assert;
import org.junit.Test;

public class CustomItemMapTest {

	@Test
	public void testGetRandom() throws Exception {
		CustomItemMap customItemMap = CustomItemMap.getInstance();

		customItemMap.put("foo", new CustomItemImp("foo"));
		customItemMap.put("bar", new CustomItemImp("bar"));
		customItemMap.put("foobar", new CustomItemImp("foobar"));

		int[] results = new int[3];
		int numOfRuns = 1000;
		for (int i = 0; i < numOfRuns; i++) {
			CustomItem ci = customItemMap.getRandom();
			Assert.assertNotNull(ci);
			if ("foo".equals(ci.getName())) {
				results[0]++;
			} else if ("bar".equals(ci.getName())) {
				results[1]++;
			} else if ("foobar".equals(ci.getName())) {
				results[2]++;
			} else {
				Assert.fail("Unexpected value");
			}
		}

		Assert.assertTrue(results[0] > 100);
		Assert.assertTrue(results[1] > 100);
		Assert.assertTrue(results[2] > 100);
	}

	@Test
	public void testGetRandomWithChance() throws Exception {
		CustomItemMap customItemMap = CustomItemMap.getInstance();

		customItemMap.put("foo", new CustomItemImp("foo", 0.25));
		customItemMap.put("bar", new CustomItemImp("bar", 0.50));
		customItemMap.put("foobar", new CustomItemImp("foobar", 0.25));

		int[] results = new int[3];
		int numOfRuns = 1000;
		for (int i = 0; i < numOfRuns; i++) {
			CustomItem ci = customItemMap.getRandomWithChance();
			Assert.assertNotNull(ci);
			if ("foo".equals(ci.getName())) {
				results[0]++;
			} else if ("bar".equals(ci.getName())) {
				results[1]++;
			} else if ("foobar".equals(ci.getName())) {
				results[2]++;
			} else {
				Assert.fail("Unexpected value");
			}
		}

		Assert.assertTrue(results[0] > 200);
		Assert.assertTrue(results[1] > 400);
		Assert.assertTrue(results[2] > 200);
	}

	class CustomItemImp implements CustomItem {

		private String name;
		private double chance;

		CustomItemImp(String name) {
			this(name, 1.0);
		}

		CustomItemImp(String name, double chance) {
			this.name = name;
			this.chance = chance;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof CustomItemImp)) return false;

			CustomItemImp that = (CustomItemImp) o;

			if (name != null ? !name.equals(that.name) : that.name != null) return false;

			return true;
		}

		@Override
		public int hashCode() {
			return name != null ? name.hashCode() : 0;
		}

		@Override
		public double getChanceToBeGivenToAMonster() {
			return chance;
		}

		@Override
		public double getChanceToDropOnDeath() {
			return 0;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getDisplayName() {
			return name;
		}

		@Override
		public Map<Enchantment, Integer> getEnchantments() {
			return null;
		}

		@Override
		public List<String> getLore() {
			return null;
		}

		@Override
		public MaterialData getMaterialData() {
			return null;
		}

		@Override
		public ItemStack toItemStack() {
			return null;
		}
	}
}
