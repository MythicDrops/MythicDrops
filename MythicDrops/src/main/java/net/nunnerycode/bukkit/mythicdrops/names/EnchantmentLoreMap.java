package net.nunnerycode.bukkit.mythicdrops.names;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.enchantments.Enchantment;

public final class EnchantmentLoreMap extends ConcurrentHashMap<Enchantment, List<String>> {

	private static final EnchantmentLoreMap _INSTANCE = new EnchantmentLoreMap();

	private EnchantmentLoreMap() {
		// do nothing
	}

	public static EnchantmentLoreMap getInstance() {
		return _INSTANCE;
	}

	public String getRandom(Enchantment enchantment) {
		if (!containsKey(enchantment)) {
			return null;
		}
		List<String> enchantmentLore = get(enchantment);
		return enchantmentLore.get(RandomUtils.nextInt(enchantmentLore.size()));
	}

}
