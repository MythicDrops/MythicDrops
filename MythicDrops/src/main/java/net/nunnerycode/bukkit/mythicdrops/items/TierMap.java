package net.nunnerycode.bukkit.mythicdrops.items;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.apache.commons.lang.math.RandomUtils;

public final class TierMap extends ConcurrentHashMap<String, Tier> {

	private static final TierMap _INSTANCE = new TierMap();

	private TierMap() {
		// do nothing
	}

	/**
	 * Gets the instance of CustomItemMap running on the server.
	 * @return instance running on the server
	 */
	public static TierMap getInstance() {
		return _INSTANCE;
	}

	/**
	 * Gets a random {@link Tier} out of the ones loaded on the server.
	 * @return random Tier
	 */
	public Tier getRandom() {
		Tier[] valueArray = values().toArray(new Tier[values().size()]);
		return valueArray[RandomUtils.nextInt(values().size())];
	}

	/**
	 * Gets a random {@link Tier} out of the ones loaded on the server using chance. Returns null if none found.
	 * @return random Tier
	 */
	public Tier getRandomWithChance(String worldName) {
		Tier[] valueArray = values().toArray(new Tier[values().size()]);
		Tier randomTier = null;
		Set<Tier> zeroSize = new HashSet<Tier>();
		while (randomTier == null && zeroSize.size() < valueArray.length) {
			Tier t = getRandom();
			if (zeroSize.contains(t)) {
				continue;
			}
			if (!t.getWorldSpawnChanceMap().containsKey(worldName)) {
				zeroSize.add(t);
				continue;
			}
			if (RandomUtils.nextDouble() < t.getWorldSpawnChanceMap().get(worldName)) {
				randomTier = t;
			}
		}
		return randomTier;
	}

}
