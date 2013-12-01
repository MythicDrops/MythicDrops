package net.nunnerycode.bukkit.mythicdrops.api.managers;

import java.util.Set;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import org.bukkit.inventory.ItemStack;

public interface CustomItemManager {

	CustomItem[] getCustomItems();

	void debugCustomItems();

	MythicDrops getPlugin();

	CustomItem getCustomItemFromItemStack(ItemStack itemStack);

	CustomItem createCustomItemFromItemStack(ItemStack itemStack);

	CustomItem getCustomItemFromDisplayName(String displayName);

	CustomItem getCustomItemFromName(String name);

	CustomItem getRandomCustomItem();

	CustomItem getRandomCustomItemWithChance();

	CustomItem getRandomCustomItem(Set<CustomItem> set);

	CustomItem getRandomCustomItemWithChance(Set<CustomItem> set);

}
