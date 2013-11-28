package net.nunnerycode.bukkit.mythicdrops.api.items;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

/**
 * A class that simplifies creating books with names, lore, and pages.
 */
public class MythicTome extends MythicItemStack {

	/**
	 * Instantiates a new MythicTome with a specified {@link MaterialData} of a {@link TomeType}. It is also
	 * instantiated with a specified title and author, specified lore and pages.
	 *
	 * @param tomeType Type of tome to create
	 * @param title    Name of the tome
	 * @param author   Author of the tome
	 * @param lore     Lore of the tome
	 * @param pages    Pages for the tome
	 */
	public MythicTome(TomeType tomeType, String title, String author, String[] lore, String... pages) {
		this(tomeType, title, author, Arrays.asList(lore), pages);
	}

	/**
	 * Instantiates a new MythicTome with a specified {@link MaterialData} of a {@link TomeType}. It is also
	 * instantiated with a specified title and author, specified lore and pages.
	 *
	 * @param tomeType Type of tome to create
	 * @param title    Name of the tome
	 * @param author   Author of the tome
	 * @param lore     Lore of the tome
	 * @param pages    Pages for the tome
	 */
	public MythicTome(TomeType tomeType, String title, String author, List<String> lore, String[] pages) {
		super(tomeType.toMaterialData().getItemType());
		ItemMeta itemMeta = getItemMeta();
		if (itemMeta instanceof BookMeta) {
			BookMeta bookMeta = (BookMeta) itemMeta;
			bookMeta.setTitle(title);
			bookMeta.setAuthor(author);
			bookMeta.setLore(lore);
			bookMeta.setPages(pages);
			setItemMeta(bookMeta);
		} else {
			itemMeta.setDisplayName(title);
			itemMeta.setLore(lore);
			setItemMeta(itemMeta);
		}
		setAmount(1);
	}

	/**
	 * An Enum holding the different varieties of Tomes allowed to be created.
	 */
	public enum TomeType {
		/**
		 * Represents the {@link Material#WRITTEN_BOOK}
		 */
		WRITTEN_BOOK(new MaterialData(Material.WRITTEN_BOOK, (byte) 0)),
		/**
		 * Represents the {@link Material#BOOK}
		 */
		BOOK(new MaterialData(Material.BOOK, (byte) 0)),
		/**
		 * Represents the {@link Material#BOOK_AND_QUILL}
		 */
		BOOK_AND_QUILL(new MaterialData(Material.BOOK_AND_QUILL, (byte) 0)),
		/**
		 * Represents the {@link Material#ENCHANTED_BOOK}
		 */
		ENCHANTED_BOOK(new MaterialData(Material.ENCHANTED_BOOK, (byte) 0));
		private final MaterialData materialData;

		private TomeType(MaterialData matData) {
			this.materialData = matData;
		}

		/**
		 * Returns a {@link MaterialData} represented by the TomeType.
		 *
		 * @return MaterialData represented by the TomeType
		 */
		public MaterialData toMaterialData() {
			return materialData;
		}
	}
}
