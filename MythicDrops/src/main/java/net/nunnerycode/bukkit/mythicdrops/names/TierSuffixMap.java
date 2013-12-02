package net.nunnerycode.bukkit.mythicdrops.names;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.apache.commons.lang.math.RandomUtils;

public final class TierSuffixMap extends ConcurrentHashMap<Tier, List<String>> {

	private static final TierSuffixMap _INSTANCE = new TierSuffixMap();

	private TierSuffixMap() {
		// do nothing
	}

	public static TierSuffixMap getInstance() {
		return _INSTANCE;
	}

	public String getRandom(Tier tier) {
		if (!containsKey(tier)) {
			return null;
		}
		List<String> tierSuffixes = get(tier);
		return tierSuffixes.get(RandomUtils.nextInt(tierSuffixes.size()));
	}

}
