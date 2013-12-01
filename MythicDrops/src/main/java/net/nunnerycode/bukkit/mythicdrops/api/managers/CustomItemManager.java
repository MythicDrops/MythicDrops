package net.nunnerycode.bukkit.mythicdrops.api.managers;

import java.util.Set;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import org.bukkit.inventory.ItemStack;

/**
 * Used to handle all interactions with CustomItems.
 */
public interface CustomItemManager extends MythicManager {

	/**
	 * Gets an array representing the {@link CustomItem}s loaded on the server.
	 *
	 * @return array of CustomItems loaded on server
	 */
	CustomItem[] getCustomItems();

	/**
	 * Uses the {@link net.nunnerycode.java.libraries.cannonball.DebugPrinter} class to log info about the {@link
	 * CustomItem}s on the server.
	 */
	void debugCustomItems();

	/**
	 * Gets a {@link CustomItem} from an {@link ItemStack}. Returns null if not a CustomItem.
	 *
	 * @param itemStack ItemStack to check
	 * @return CustomItem for ItemStack - or null
	 */
	CustomItem getCustomItemFromItemStack(ItemStack itemStack);

	/**
	 * Creates a {@link CustomItem} from an {@link ItemStack}.
	 *
	 * @param itemStack ItemStack to create CustomItem based on
	 * @return new CustomItem
	 */
	CustomItem createCustomItemFromItemStack(ItemStack itemStack);

	/**
	 * Gets a {@link CustomItem} with a display name. Returns null if not a CustomItem.
	 *
	 * @param displayName Display name
	 * @return CustomItem with display name - or null
	 */
	CustomItem getCustomItemFromDisplayName(String displayName);

	/**
	 * Gets a {@link CustomItem} with a name. Returns null if not a CustomItem.
	 *
	 * @param name Display name
	 * @return CustomItem with name - or null
	 */
	CustomItem getCustomItemFromName(String name);

	/**
	 * Gets a random {@link CustomItem} from the loaded CustomItems - disregards chance.
	 *
	 * @return random CustomItem
	 */
	CustomItem getRandomCustomItem();

	/**
	 * Gets a random {@link CustomItem} from the loaded CustomItems, using the {@link net.nunnerycode.bukkit
	 * .mythicdrops.api.items.CustomItem#getChanceToBeGivenToAMonster()} method.
	 *
	 * @return random CustomItem
	 */
	CustomItem getRandomCustomItemWithChance();

	/**
	 * Gets a random {@link CustomItem} from the given {@link Set} of CustomItems - disregards chance.
	 *
	 * @return random CustomItem
	 */
	CustomItem getRandomCustomItem(Set<CustomItem> set);

	/**
	 * Gets a random {@link CustomItem} from the given {@link Set} of CustomItems - using the {@link net.nunnerycode
	 * .bukkit.mythicdrops.api.items.CustomItem#getChanceToBeGivenToAMonster()} method.
	 *
	 * @return random CustomItem
	 */
	CustomItem getRandomCustomItemWithChance(Set<CustomItem> set);

}
