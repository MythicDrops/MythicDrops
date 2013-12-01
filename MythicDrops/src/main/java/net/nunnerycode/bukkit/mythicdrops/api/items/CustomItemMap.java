package net.nunnerycode.bukkit.mythicdrops.api.items;

import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.math.RandomUtils;

// example of singleton pattern
public final class CustomItemMap extends ConcurrentHashMap<String, CustomItem> {

	private static final CustomItemMap _INSTANCE = new CustomItemMap();

	private CustomItemMap() {
		// do nothing
	}

	public static CustomItemMap getInstance() {
		return _INSTANCE;
	}

	public CustomItem getRandom() {
		CustomItem[] valueArray = values().toArray(new CustomItem[values().size()]);
		return valueArray[RandomUtils.nextInt(values().size())];
	}

	public CustomItem getRandomWithChance() {
		CustomItem[] valueArray = values().toArray(new CustomItem[values().size()]);
		CustomItem randomCustomItem = null;
		int zeroSize = 0;
		while (randomCustomItem == null && zeroSize < valueArray.length) {
			CustomItem ci = getRandom();
			if (RandomUtils.nextDouble() < ci.getChanceToBeGivenToAMonster()) {
				randomCustomItem = ci;
			}
		}
		return randomCustomItem;
	}

}
