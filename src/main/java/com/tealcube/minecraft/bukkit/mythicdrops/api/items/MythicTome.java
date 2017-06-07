/**
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.api.items;

import java.util.ArrayList;
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
   * @param title Name of the tome
   * @param author Author of the tome
   * @param lore Lore of the tome
   * @param pages Pages for the tome
   */
  public MythicTome(TomeType tomeType, String title, String author, String[] lore,
      String... pages) {
    this(tomeType, title, author, Arrays.asList(lore), pages);
  }

  /**
   * Instantiates a new MythicTome with a specified {@link MaterialData} of a {@link TomeType}. It is also
   * instantiated with a specified title and author, specified lore and pages.
   *
   * @param tomeType Type of tome to create
   * @param title Name of the tome
   * @param author Author of the tome
   * @param lore Lore of the tome
   * @param pages Pages for the tome
   */
  public MythicTome(TomeType tomeType, String title, String author, List<String> lore,
      String[] pages) {
    super(tomeType.toMaterial());
    ItemMeta itemMeta = getItemMeta();
    List<String> coloredLore = new ArrayList<>();
    for (String s : lore) {
      coloredLore.add(s.replace('&', '\u00A7').replace("\u00A7\u00A7", "&"));
    }
    if (itemMeta instanceof BookMeta) {
      BookMeta bookMeta = (BookMeta) itemMeta;
      bookMeta.setTitle(title.replace('&', '\u00A7').replace("\u00A7\u00A7", "&"));
      bookMeta.setAuthor(author);
      bookMeta.setLore(coloredLore);
      bookMeta.setPages(pages);
      setItemMeta(bookMeta);
    } else {
      itemMeta.setDisplayName(title.replace('&', '\u00A7').replace("\u00A7\u00A7", "&"));
      itemMeta.setLore(coloredLore);
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
    WRITTEN_BOOK(Material.WRITTEN_BOOK),
    /**
     * Represents the {@link Material#BOOK}
     */
    BOOK(Material.BOOK),
    /**
     * Represents the {@link Material#BOOK_AND_QUILL}
     */
    BOOK_AND_QUILL(Material.BOOK_AND_QUILL),
    /**
     * Represents the {@link Material#ENCHANTED_BOOK}
     */
    ENCHANTED_BOOK(Material.ENCHANTED_BOOK);
    private final Material material;

    private TomeType(Material mat) {
      this.material = mat;
    }

    /**
     * Returns a {@link Material} represented by the TomeType.
     *
     * @return Material represented by the TomeType
     */
    public Material toMaterial() {
      return material;
    }
  }
}
