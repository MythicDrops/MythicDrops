package net.nunnerycode.bukkit.mythicdrops.api.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A wrapper around ItemStack that enables immediate ItemMeta access.
 */
public class MythicItemStack extends ItemStack {

	/**
	 * Instantiates an ItemStack.
	 *
	 * @param type material
	 */
	public MythicItemStack(Material type) {
		this(type, 1, (short) 0, null, null, null);
	}

	/**
	 * Instantiates an ItemStack.
	 *
	 * @param type       material
	 * @param durability damage / durability
	 */
	public MythicItemStack(Material type, short durability) {
		this(type, 1, durability, null, null, null);
	}

	/**
	 * Instantiates an ItemStack.
	 *
	 * @param type   material
	 * @param amount amount
	 */
	public MythicItemStack(Material type, int amount) {
		this(type, amount, (short) 0, null, null, null);
	}

	/**
	 * Instantiates an ItemStack with a display name, lore, and enchantments.
	 *
	 * @param type         material
	 * @param amount       amount
	 * @param durability   damage / durability
	 * @param displayName  name of item
	 * @param lore         lore for item
	 * @param enchantments enchantments for item
	 */
	public MythicItemStack(Material type, int amount, short durability, String displayName, List<String> lore,
						   Map<Enchantment, Integer> enchantments) {
		super(type, amount, durability);
		ItemMeta itemMeta = (hasItemMeta()) ? getItemMeta() : Bukkit.getItemFactory().getItemMeta(type);
		itemMeta.setDisplayName(displayName);
		itemMeta.setLore(lore);
		if (enchantments != null) {
			for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
				itemMeta.addEnchant(entry.getKey(), entry.getValue(), true);
			}
		}
		setItemMeta(itemMeta);
	}

	/**
	 * Instantiates an ItemStack.
	 *
	 * @param type       material
	 * @param amount     amount
	 * @param durability damage / durability
	 */
	public MythicItemStack(Material type, int amount, short durability) {
		this(type, amount, durability, null, null, null);
	}

	/**
	 * Instantiates an ItemStack with a display name.
	 *
	 * @param type        material
	 * @param amount      amount
	 * @param durability  damage / durability
	 * @param displayName name of item
	 */
	public MythicItemStack(Material type, int amount, short durability, String displayName) {
		this(type, amount, durability, displayName, new ArrayList<String>(), new HashMap<Enchantment, Integer>());
	}

	/**
	 * Instantiates an ItemStack with lore.
	 *
	 * @param type       material
	 * @param amount     amount
	 * @param durability damage / durability
	 * @param lore       lore for item
	 */
	public MythicItemStack(Material type, int amount, short durability, List<String> lore) {
		this(type, amount, durability, null, lore, new HashMap<Enchantment, Integer>());
	}

	/**
	 * Instantiates an ItemStack with enchantments.
	 *
	 * @param type         material
	 * @param amount       amount
	 * @param durability   damage / durability
	 * @param enchantments enchantments for item
	 */
	public MythicItemStack(Material type, int amount, short durability, Map<Enchantment, Integer> enchantments) {
		this(type, amount, durability, null, new ArrayList<String>(), enchantments);
	}

	/**
	 * Instantiates an ItemStack with a display name and lore.
	 *
	 * @param type        material
	 * @param amount      amount
	 * @param durability  damage / durability
	 * @param displayName name of item
	 * @param lore        lore for item
	 */
	public MythicItemStack(Material type, int amount, short durability, String displayName, List<String> lore) {
		this(type, amount, durability, displayName, lore, new HashMap<Enchantment, Integer>());
	}

	/**
	 * Instantiates an ItemStack with a display name and enchantments.
	 *
	 * @param type         material
	 * @param amount       amount
	 * @param durability   damage / durability
	 * @param displayName  name of item
	 * @param enchantments enchantments for item
	 */
	public MythicItemStack(Material type, int amount, short durability, String displayName, Map<Enchantment,
			Integer> enchantments) {
		this(type, amount, durability, displayName, new ArrayList<String>(), enchantments);
	}

	/**
	 * Instantiates an ItemStack with lore and enchantments.
	 *
	 * @param type         material
	 * @param amount       amount
	 * @param durability   damage / durability
	 * @param lore         lore for item
	 * @param enchantments enchantments for item
	 */
	public MythicItemStack(Material type, int amount, short durability, List<String> lore, Map<Enchantment,
			Integer> enchantments) {
		this(type, amount, durability, null, lore, enchantments);
	}

}
