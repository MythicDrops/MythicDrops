package net.nunnerycode.bukkit.mythicdrops.names;

import java.util.Arrays;
import org.bukkit.enchantments.Enchantment;
import org.junit.Assert;
import org.junit.Test;

public class EnchantmentLoreMapTest {
	@Test
	public void testGetRandom() throws Exception {
		EnchantmentLoreMap enchantmentLoreMap = EnchantmentLoreMap.getInstance();

		enchantmentLoreMap.put(Enchantment.ARROW_DAMAGE, Arrays.asList("foo", "bar", "foobar"));

		int[] results = new int[3];
		int numOfRuns = 1000;
		for (int i = 0; i < numOfRuns; i++) {
			String lore = enchantmentLoreMap.getRandom(Enchantment.ARROW_DAMAGE);
			Assert.assertNotNull(lore);
			if ("foo".equals(lore)) {
				results[0]++;
			} else if ("bar".equals(lore)) {
				results[1]++;
			} else if ("foobar".equals(lore)) {
				results[2]++;
			} else {
				Assert.fail("Unexpected value");
			}
		}

		Assert.assertTrue(results[0] > 300);
		Assert.assertTrue(results[1] > 300);
		Assert.assertTrue(results[2] > 300);
	}
}
