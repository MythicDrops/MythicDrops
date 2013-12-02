package net.nunnerycode.bukkit.mythicdrops.names;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.enchantments.Enchantment;

public final class EnchantmentPrefixMap extends ConcurrentHashMap<Enchantment, List<String>> {

	private static final EnchantmentPrefixMap _INSTANCE = new EnchantmentPrefixMap();

	private EnchantmentPrefixMap() {
		// do nothing
	}

	public static EnchantmentPrefixMap getInstance() {
		return _INSTANCE;
	}

	public String getRandom(Enchantment enchantment) {
		if (!containsKey(enchantment)) {
			return null;
		}
		List<String> enchantmentPrefixes = get(enchantment);
		return enchantmentPrefixes.get(RandomUtils.nextInt(enchantmentPrefixes.size()));
	}

}
