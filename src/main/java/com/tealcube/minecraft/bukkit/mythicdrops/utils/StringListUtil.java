/*
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
package com.tealcube.minecraft.bukkit.mythicdrops.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;

/**
 * A class designed to handle manipulation of {@link java.util.List}s of {@link java.lang.String}s.
 */
public final class StringListUtil {

  private static final String EMPTY_STRING = "";

  private StringListUtil() {
    // do nothing
  }

  /**
   * Adds a {@link String} to a {@link List} at a specified index.
   *
   * @param strings List of Strings
   * @param index index to add
   * @param string String to add to List
   * @param startAtZero if index is a zero-count number
   * @return List with the added String
   */
  public static List<String> addString(List<String> strings, int index, String string,
      boolean startAtZero) {
    if (strings == null) {
      throw new IllegalArgumentException("List<String> cannot be null");
    }
    if (string == null) {
      throw new IllegalArgumentException("String cannot be null");
    }

    List<String> list = new ArrayList<>(strings);

    int actualIndex = index;
    if (!startAtZero) {
      actualIndex--;
    }

    if (list.size() < actualIndex) {
      int sizeDifference = actualIndex - list.size();
      for (; sizeDifference > 0; sizeDifference--) {
        list.add(EMPTY_STRING);
      }
    }

    list.add(Math.max(actualIndex, 0), string);
    return list;
  }

  /**
   * Removes all occurences of a {@link String} from a {@link List}.
   *
   * @param strings List of Strings
   * @param string String to remove
   * @return List without String
   */
  public static List<String> removeAll(List<String> strings, String string) {
    if (strings == null) {
      throw new IllegalArgumentException("List<String> cannot be null");
    }
    if (string == null) {
      throw new IllegalArgumentException("String cannot be null");
    }

    List<String> list = new ArrayList<>(strings);

    Iterator<String> iterator = list.iterator();
    while (iterator.hasNext()) {
      if (iterator.next().equals(string)) {
        iterator.remove();
      }
    }

    return list;
  }

  /**
   * Removes a {@link java.util.List} of {@link java.lang.String}s from a {@link java.util.List} of {@link
   * java.lang.String}s, but only if it matches exactly.
   *
   * @param strings List from which to remove
   * @param otherStrings Strings to match in order to remove
   * @return List without matched Strings
   */
  public static List<String> removeIfMatches(List<String> strings, List<String> otherStrings) {
    if (strings == null || otherStrings == null) {
      throw new IllegalArgumentException("List<String> cannot be null");
    }

    List<String> list = new ArrayList<>(strings);

    int size = otherStrings.size();

    if (list.size() < size) {
      return list;
    }

    for (int i = 0; i < list.size(); i++) {
      if (i + size > list.size()) {
        break;
      }
      List<String> subList = list.subList(i, i + size);
      if (subList.equals(otherStrings)) {
        for (int j = size; j > 0; j--) {
          list.remove(i + j - 1);
        }
        break;
      }
    }

    return list;
  }

  public static List<String> removeIfMatchesColorless(List<String> strings, List<String> otherStrings) {
    if (strings == null || otherStrings == null) {
      throw new IllegalArgumentException("List<String> cannot be null");
    }

    List<String> list = new ArrayList<>(strings);

    int size = otherStrings.size();

    if (list.size() < size) {
      return list;
    }

    int index = indexOfColorless(strings, otherStrings);
    if (index < 0) {
      return list;
    }

    List<String> stringsBeforeIndex = strings.subList(0, index);
    List<String> stringsAfterIndex = strings.subList(index + otherStrings.size(), strings.size());

    List<String> retStrings = new ArrayList<>(stringsBeforeIndex);
    retStrings.addAll(stringsAfterIndex);
    return retStrings;
  }

  public static boolean equalsColorless(List<String> strings, List<String> otherStrings) {
    if (strings == null || otherStrings == null) {
      throw new IllegalArgumentException("List<String> cannot be null");
    }
    return removeColor(strings).equals(removeColor(otherStrings));
  }

  public static int indexOfColorless(List<String> strings, List<String> otherStrings) {
    List<String> colorlessStrings = removeColor(strings);
    List<String> colorlessOtherStrings = removeColor(otherStrings);
    return Collections.indexOfSubList(colorlessStrings, colorlessOtherStrings);
  }

  public static List<String> colorList(List<String> strings) {
    return colorList(strings, '&');
  }

  /**
   * Goes through a List and replaces a specified character with the {@link ChatColor} symbol while replacing two
   * ChatColor symbols with a specified character.
   *
   * @param strings List of Strings
   * @param symbol character to replace color codes with
   * @return colored List
   */
  public static List<String> colorList(List<String> strings, char symbol) {
    if (strings == null) {
      throw new IllegalArgumentException("List<String> cannot be null");
    }

    List<String> list = new ArrayList<>();
    for (String s : strings) {
      list.add(s.replace(symbol, '\u00A7').replace("\u00A7\u00A7", String.valueOf(symbol)));
    }

    return list;
  }

  public static List<String> replaceWithList(List<String> containingList, String key,
      List<String> list) {
    if (containingList == null) {
      throw new IllegalArgumentException("List<String> cannot be null");
    }
    if (key == null) {
      throw new IllegalArgumentException("String cannot be null");
    }
    if (list == null) {
      throw new IllegalArgumentException("List<String> cannot be null");
    }

    List<String> l = new ArrayList<>(containingList);
    for (int i = 0; i < l.size(); i++) {
      String k = l.get(i);
      if (k.equals(key)) {
        l.remove(i);
        l.addAll(i, list);
      }
    }
    return l;
  }

  public static List<String> replaceArgs(List<String> strings, String[][] args) {
    List<String> list = new ArrayList<>();
    for (String s : strings) {
      list.add(StringUtil.replaceArgs(s, args));
    }
    return list;
  }

  public static List<String> removeColor(List<String> strings) {
    if (strings == null) {
      throw new IllegalArgumentException("List<String> cannot be null");
    }
    List<String> list = new ArrayList<>();
    for (String s : strings) {
      list.add(ChatColor.stripColor(s));
    }
    return list;
  }

}
