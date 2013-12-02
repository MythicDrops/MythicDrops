package net.nunnerycode.bukkit.mythicdrops.names;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.apache.commons.lang.math.RandomUtils;

public final class TierLoreMap extends ConcurrentHashMap<Tier, List<String>> {

	private static final TierLoreMap _INSTANCE = new TierLoreMap();

	private TierLoreMap() {
		// do nothing
	}

	public static TierLoreMap getInstance() {
		return _INSTANCE;
	}

	public String getRandom(Tier tier) {
		if (!containsKey(tier)) {
			return null;
		}
		List<String> enchantmentLore = get(tier);
		return enchantmentLore.get(RandomUtils.nextInt(enchantmentLore.size()));
	}

}
