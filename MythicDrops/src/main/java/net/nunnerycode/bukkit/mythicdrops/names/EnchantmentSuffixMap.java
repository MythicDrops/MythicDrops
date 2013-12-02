package net.nunnerycode.bukkit.mythicdrops.names;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.enchantments.Enchantment;

public final class EnchantmentSuffixMap extends ConcurrentHashMap<Enchantment, List<String>> {

	private static final EnchantmentSuffixMap _INSTANCE = new EnchantmentSuffixMap();

	private EnchantmentSuffixMap() {
		// do nothing
	}

	public static EnchantmentSuffixMap getInstance() {
		return _INSTANCE;
	}

	public String getRandom(Enchantment enchantment) {
		if (!containsKey(enchantment)) {
			return null;
		}
		List<String> enchantmentSuffixes = get(enchantment);
		return enchantmentSuffixes.get(RandomUtils.nextInt(enchantmentSuffixes.size()));
	}

}
