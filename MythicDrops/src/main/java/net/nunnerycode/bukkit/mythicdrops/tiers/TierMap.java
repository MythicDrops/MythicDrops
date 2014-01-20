package net.nunnerycode.bukkit.mythicdrops.tiers;

import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.apache.commons.lang.math.RandomUtils;

import java.util.concurrent.ConcurrentHashMap;

public final class TierMap extends ConcurrentHashMap<String, Tier> {

	private static final TierMap _INSTANCE = new TierMap();

	private TierMap() {
		// do nothing
	}

	/**
	 * Gets the instance of CustomItemMap running on the server.
	 *
	 * @return instance running on the server
	 */
	public static TierMap getInstance() {
		return _INSTANCE;
	}

	/**
	 * Gets a random {@link Tier} out of the ones loaded on the server using chance. Returns null if none found.
	 *
	 * @return random Tier
	 */
	public Tier getRandomWithChance(String worldName) {
		double totalWeight = 0;
		for (Tier t : values()) {
			if (t.getWorldSpawnChanceMap().containsKey(worldName)) {
				totalWeight += t.getWorldSpawnChanceMap().get
						(worldName);
			} else if (t.getWorldSpawnChanceMap().containsKey("default")) {
				totalWeight += t.getWorldSpawnChanceMap().get
						("default");
			}
		}

		double chosenWeight = RandomUtils.nextDouble() * totalWeight;

		double currentWeight = 0;

		for (Tier t : values()) {
			if (t.getWorldSpawnChanceMap().containsKey(worldName)) {
				currentWeight += t.getWorldSpawnChanceMap().get
						(worldName);
			} else if (t.getWorldSpawnChanceMap().containsKey("default")) {
				currentWeight += t.getWorldSpawnChanceMap().get
						("default");
			} else {
				continue;
			}

			if (currentWeight >= chosenWeight) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Gets a random {@link Tier} out of the ones loaded on the server.
	 *
	 * @return random Tier
	 */
	public Tier getRandom() {
		Tier[] valueArray = values().toArray(new Tier[values().size()]);
		return valueArray[RandomUtils.nextInt(values().size())];
	}

	public Tier getRandomWithIdentifyChance(String worldName) {
		double totalWeight = 0;
		for (Tier t : values()) {
			if (t.getWorldIdentifyChanceMap().containsKey(worldName)) {
				totalWeight += t.getWorldIdentifyChanceMap().get
						(worldName);
			} else if (t.getWorldIdentifyChanceMap().containsKey("default")) {
				totalWeight += t.getWorldIdentifyChanceMap().get
						("default");
			}
		}

		double chosenWeight = RandomUtils.nextDouble() * totalWeight;

		double currentWeight = 0;

		for (Tier t : values()) {
			if (t.getWorldIdentifyChanceMap().containsKey(worldName)) {
				currentWeight += t.getWorldIdentifyChanceMap().get
						(worldName);
			} else if (t.getWorldIdentifyChanceMap().containsKey("default")) {
				currentWeight += t.getWorldIdentifyChanceMap().get
						("default");
			} else {
				continue;
			}

			if (currentWeight >= chosenWeight) {
				return t;
			}
		}
		return null;
	}

}
