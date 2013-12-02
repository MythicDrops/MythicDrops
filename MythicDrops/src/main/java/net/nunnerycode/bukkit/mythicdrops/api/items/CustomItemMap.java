package net.nunnerycode.bukkit.mythicdrops.api.items;

import java.util.HashSet;
import java.util.Set;
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
	 * Gets a random {@link CustomItem} out of the ones loaded on the server using chance.
	 * @return random CustomItem
	 */
	public CustomItem getRandom() {
		CustomItem[] valueArray = values().toArray(new CustomItem[values().size()]);
		return valueArray[RandomUtils.nextInt(values().size())];
	}

	/**
	 * Gets a random {@link CustomItem} out of the ones loaded on the server using chance. Returns null if none found.
	 * @return random CustomItem
	 */
	public CustomItem getRandomWithChance() {
		CustomItem[] valueArray = values().toArray(new CustomItem[values().size()]);
		CustomItem randomCustomItem = null;
		Set<CustomItem> zeroSize = new HashSet<CustomItem>();
		while (randomCustomItem == null && zeroSize.size() < valueArray.length) {
			CustomItem ci = getRandom();
			if (ci.getChanceToBeGivenToAMonster() <= 0D) {
				zeroSize.add(ci);
				continue;
			}
			if (RandomUtils.nextDouble() < ci.getChanceToBeGivenToAMonster()) {
				randomCustomItem = ci;
			}
		}
		return randomCustomItem;
	}

}
