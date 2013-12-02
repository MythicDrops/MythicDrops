package net.nunnerycode.bukkit.mythicdrops.items;

import java.util.concurrent.ConcurrentHashMap;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItemMap;
import org.apache.commons.lang.math.RandomUtils;

/**
 * An extension of {@link ConcurrentHashMap} designed to allow easy developer access to {@link CustomItem}s.
 */
public final class MythicCustomItemMap extends ConcurrentHashMap<String, CustomItem> implements CustomItemMap {

	private static final MythicCustomItemMap _INSTANCE = new MythicCustomItemMap();

	private MythicCustomItemMap() {
		// do nothing
	}

	/**
	 * Gets the instance of CustomItemMap running on the server.
	 * @return instance running on the server
	 */
	public static MythicCustomItemMap getInstance() {
		return _INSTANCE;
	}

	@Override
	public CustomItem getRandom() {
		CustomItem[] valueArray = values().toArray(new CustomItem[values().size()]);
		return valueArray[RandomUtils.nextInt(values().size())];
	}

	@Override
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
