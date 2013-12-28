package net.nunnerycode.bukkit.mythicdrops.utils;

import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.items.CustomItemBuilder;
import net.nunnerycode.bukkit.mythicdrops.items.CustomItemMap;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CustomItemUtil {

	private CustomItemUtil() {
		// do nothing
	}

	public static CustomItem getCustomItemFromItemStack(ItemStack itemStack) {
		if (!itemStack.hasItemMeta()) {
			return null;
		}
		ItemMeta im = itemStack.getItemMeta();
		if (!im.hasDisplayName() || !im.hasLore()) {
			return null;
		}
		String displayName;
		String name;
		if (im.hasDisplayName()) {
			displayName = im.getDisplayName().replace('\u00A7', '&');
			name = ChatColor.stripColor(im.getDisplayName()).replaceAll("\\s+", "");
		} else {
			return null;
		}
		List<String> itemLore = new ArrayList<>();
		if (im.hasLore()) {
			itemLore = im.getLore();
		}

		List<String> lore = new ArrayList<>();
		for (String s : itemLore) {
			lore.add(s.replace('\u00A7', '&'));
		}

		Map<Enchantment, Integer> enchantments = new HashMap<>();
		if (im.hasEnchants()) {
			enchantments = im.getEnchants();
		}

		CustomItem ci = new CustomItemBuilder(name).withDisplayName(displayName).withLore(lore).withEnchantments
				(enchantments).withMaterialData(itemStack.getData()).build();

		for (CustomItem cust : CustomItemMap.getInstance().values()) {
			if (ci.equals(cust)) {
				return cust;
			}
		}

		return null;
	}

}
