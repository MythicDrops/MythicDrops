package net.nunnerycode.bukkit.mythicdrops.utils;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketGem;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class SocketGemUtil {

	private SocketGemUtil() {
		// do nothing;
	}

	public static ItemStack addSockets(ItemStack stack, int amount) {
		Validate.notNull(stack, "ItemStack cannot be null");

		ItemStack is = stack;
		for (int i = 0; i < amount; i++) {
			is = addSocket(is);
		}
		return is;
	}

	public static SocketGem getSocketGemFromName(String name) {
		for (SocketGem sg : MythicDropsPlugin.getInstance().getSockettingSettings().getSocketGemMap().values()) {
			if (sg.getName().equalsIgnoreCase(name)) {
				return sg;
			}
		}
		return null;
	}

	public static ItemStack addSocket(ItemStack stack) {
		Validate.notNull(stack, "ItemStack cannot be null");

		ItemMeta itemMeta = stack.hasItemMeta() ? stack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(stack.getType());
		if (itemMeta == null) {
			return stack;
		}

		List<String> lore = new ArrayList<>();
		if (itemMeta.hasLore()) {
			lore = new ArrayList<>(itemMeta.getLore());
		}

		lore.add(MythicDropsPlugin.getInstance().getSockettingSettings().getSockettedItemString());

		itemMeta.setLore(lore);
		stack.setItemMeta(itemMeta);

		return stack;
	}

}
