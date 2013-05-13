/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.Mythicdrops.objects;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomItem {

	private final String name;
	private final String displayName;
	private final List<String> lore;
	private final Map<Enchantment, Integer> enchantments;
	private final MaterialData matData;
	private final double chance;

	public CustomItem(String name, String displayName, List<String> lore,
	                  Map<Enchantment, Integer> enchantments, MaterialData matData, double chance) {
		this.name = name;
		this.displayName = displayName;
		this.lore = lore;
		this.enchantments = enchantments;
		this.matData = matData;
		this.chance = chance;
	}

	public double getChance() {
		return chance;
	}

	public String getDisplayName() {
		if (displayName != null)
			return displayName;
		return name;
	}

	public Map<Enchantment, Integer> getEnchantments() {
		return enchantments;
	}

	public List<String> getLore() {
		return lore;
	}

	public MaterialData getMatData() {
		return matData;
	}

	public String getName() {
		return name;
	}

	public ItemStack toItemStack() {
		ItemStack is = getMatData().toItemStack(1);
		ItemMeta im;
		if (is.hasItemMeta())
			im = is.getItemMeta();
		else
			im = Bukkit.getItemFactory().getItemMeta(is.getType());
		String displayName = getDisplayName();
		if (displayName.contains("&")) {
			displayName = displayName.replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
		}
		im.setDisplayName(displayName);
		List<String> lore = new ArrayList<String>();
		for (String s : getLore()) {
			lore.add(s.replace('&', '\u00A7').replace("\u00A7\u00A7", "&"));
		}
		im.setLore(lore);
		is.setItemMeta(im);
		is.addUnsafeEnchantments(getEnchantments());
		return is;
	}

}
