package net.nunnerycode.bukkit.mythicdrops.api.items;

import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.math.RandomUtils;

/**
 * An extension of {@link ConcurrentHashMap} designed to allow easy developer access to {@link CustomItem}s.
 */
public final class CustomItemMap extends ConcurrentHashMap<String, CustomItem> {

	private static final CustomItemMap _INSTANCE = new CustomItemMap();

	private CustomItemMap() {
		// do nothing
	}

	/**
	 * Gets the instance of CustomItemMap running on the server.
	 * @return instance running on the server
	 */
	public static CustomItemMap getInstance() {
		return _INSTANCE;
	}

	/**
	 * Returns a random {@link CustomItem}.
	 * @return a random CustomItem
	 */
	public CustomItem getRandom() {
		CustomItem[] valueArray = values().toArray(new CustomItem[values().size()]);
		return valueArray[RandomUtils.nextInt(values().size())];
	}

	/**
	 * Returns a random {@link CustomItem} by using chances of items. Returns null if unable to get an item.
	 * @return random CustomItem
	 */
	public CustomItem getRandomWithChance() {
		CustomItem[] valueArray = values().toArray(new CustomItem[values().size()]);
		CustomItem randomCustomItem = null;
		int zeroSize = 0;
		while (randomCustomItem == null && zeroSize < valueArray.length) {
			CustomItem ci = getRandom();
			if (ci.getChanceToBeGivenToAMonster() <= 0D) {
				zeroSize++;
				continue;
			}
			if (RandomUtils.nextDouble() < ci.getChanceToBeGivenToAMonster()) {
				randomCustomItem = ci;
			}
		}
		return randomCustomItem;
	}

}
