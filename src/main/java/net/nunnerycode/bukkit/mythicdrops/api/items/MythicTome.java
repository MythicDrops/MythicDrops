/*
 * Copyright (C) 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.nunnerycode.bukkit.mythicdrops.api.items;

import org.bukkit.Material;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.Arrays;

/**
 * A class that simplifies creating books with names, lore, and pages.
 */
public class MythicTome extends MythicItemStack {

    /**
     * Instantiates a new MythicTome with a specified {@link MaterialData} of a {@link TomeType}. It is also
     * instantiated with a specified title and author, specified lore and pages.
     *
     * @param tomeType
     * @param title
     * @param author
     * @param lore
     * @param pages
     */
    public MythicTome(TomeType tomeType, String title, String author, String[] lore, String[] pages) {
        super(tomeType.toMaterialData());
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta instanceof BookMeta) {
            BookMeta bookMeta = (BookMeta) itemMeta;
            bookMeta.setTitle(title);
            bookMeta.setAuthor(author);
            bookMeta.setLore(Arrays.asList(lore));
            bookMeta.setPages(pages);
            setItemMeta(bookMeta);
        } else {
            itemMeta.setDisplayName(title);
            itemMeta.setLore(Arrays.asList(lore));
            setItemMeta(itemMeta);
        }
        setAmount(1);
    }

    /**
     * An Enum holding the different varieties of Tomes allowed to be created.
     */
    public enum TomeType {
        /**
         * Represents the {@link Material}.WRITTEN_BOOK
         */
        WRITTEN_BOOK(new MaterialData(Material.WRITTEN_BOOK, (byte) 0)),
        /**
         * Represents the {@link Material}.BOOK
         */
        BOOK(new MaterialData(Material.BOOK, (byte) 0)),
        /**
         * Represents the {@link Material}.BOOK_AND_QUILL
         */
        BOOK_AND_QUILL(new MaterialData(Material.BOOK_AND_QUILL, (byte) 0)),
        /**
         * Represents the {@link Material}.ENCHANTED_BOOK
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
