package com.conventnunnery.plugins.Mythicdrops.objects.parents;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.Arrays;

public class MythicTome extends MythicItemStack {

	public MythicTome(TomeType tomeType, String title, String author, String[] lore, String[] pages) {
		super(tomeType.toMaterialData());
		ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(getType());
		BookMeta bookMeta = (BookMeta) itemMeta;
		bookMeta.setTitle(title);
		bookMeta.setAuthor(author);
		bookMeta.setLore(Arrays.asList(lore));
		bookMeta.setPages(pages);
		setItemMeta(bookMeta);
	}

	public enum TomeType {
		WRITTEN_BOOK(new MaterialData(Material.WRITTEN_BOOK, (byte) 0)),
		BOOK(new MaterialData(Material.BOOK, (byte) 0)),
		BOOK_AND_QUILL(new MaterialData(Material.BOOK_AND_QUILL, (byte) 0)),
		ENCHANTED_BOOK(new MaterialData(Material.ENCHANTED_BOOK, (byte) 0));
		private final MaterialData materialData;

		private TomeType(MaterialData matData) {
			this.materialData = matData;
		}

		public MaterialData toMaterialData() {
			return materialData;
		}
	}
}
