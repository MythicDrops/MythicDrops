package com.conventnunnery.plugins.mythicdrops.objects.parents;

import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.Arrays;

public class MythicRegularItem extends MythicItemStack {

	public MythicRegularItem(MaterialData materialData, String displayName, String... lore) {
		super(materialData);
		ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(materialData.getItemType());
		itemMeta.setDisplayName(displayName);
		itemMeta.setLore(Arrays.asList(lore));
		setItemMeta(itemMeta);
        setAmount(1);
	}

}
