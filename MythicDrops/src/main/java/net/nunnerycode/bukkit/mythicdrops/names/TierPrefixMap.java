package net.nunnerycode.bukkit.mythicdrops.names;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.apache.commons.lang.math.RandomUtils;

public final class TierPrefixMap extends ConcurrentHashMap<Tier, List<String>> {

	private static final TierPrefixMap _INSTANCE = new TierPrefixMap();

	private TierPrefixMap() {
		// do nothing
	}

	public static TierPrefixMap getInstance() {
		return _INSTANCE;
	}

	public String getRandom(Tier tier) {
		if (!containsKey(tier)) {
			return null;
		}
		List<String> tierPrefixes = get(tier);
		return tierPrefixes.get(RandomUtils.nextInt(tierPrefixes.size()));
	}

}
