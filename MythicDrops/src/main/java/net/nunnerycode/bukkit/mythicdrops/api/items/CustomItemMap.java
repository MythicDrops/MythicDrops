package net.nunnerycode.bukkit.mythicdrops.api.items;

public interface CustomItemMap {

	/**
	 * Returns a random {@link CustomItem}.
	 * @return a random CustomItem
	 */
	public CustomItem getRandom();

	/**
	 * Returns a random {@link CustomItem} by using chances of items. Returns null if unable to get an item.
	 * @return random CustomItem
	 */
	public CustomItem getRandomWithChance();

}
