package net.nunnerycode.bukkit.mythicdrops.utils;

import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.tiers.TierMap;
import org.apache.commons.lang.math.RandomUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public final class TierUtil {

	private TierUtil() {
		// do nothing
	}

	public static Tier randomTier() {
		return TierMap.getInstance().getRandom();
	}

	public static Tier randomTierWithChance() {
		return randomTierWithChance("default");
	}

	public static Tier randomTierWithChance(String worldName) {
		return TierMap.getInstance().getRandomWithChance(worldName);
	}

	public static Tier randomTier(Collection<Tier> collection) {
		Tier[] array = collection.toArray(new Tier[collection.size()]);
		return array[RandomUtils.nextInt(array.length)];
	}

	public static Tier randomTierWithChance(Collection<Tier> values) {
		return randomTierWithChance(values, "default");
	}

	public static Tier randomTierWithChance(Collection<Tier> values, String worldName) {
		Tier randomTier = null;
		Set<Tier> zeroSize = new HashSet<Tier>();
		while (randomTier == null && zeroSize.size() < values.size()) {
			Tier t = randomTier(values);
			if (zeroSize.contains(t)) {
				continue;
			}
			if (!worldName.equalsIgnoreCase("default") && !t.getWorldSpawnChanceMap().containsKey(worldName)) {
				zeroSize.add(t);
				continue;
			}
			if (RandomUtils.nextDouble() < t.getWorldSpawnChanceMap().get(worldName)) {
				randomTier = t;
			}
		}
		return randomTier;
	}

	public static Collection<Tier> getTiersFromStrings(Collection<String> strings) {
		Set<Tier> tiers = new LinkedHashSet<>();
		for (String s : strings) {
			Tier t = getTier(s);
			if (t != null) {
				tiers.add(t);
			}
		}
		return tiers;
	}

	public static Tier getTier(String name) {
		for (Tier t : TierMap.getInstance().values()) {
			if (t.getName().equals(name)) {
				return t;
			}
			if (t.getDisplayName().equals(name)) {
				return t;
			}
		}
		return null;
	}

}
