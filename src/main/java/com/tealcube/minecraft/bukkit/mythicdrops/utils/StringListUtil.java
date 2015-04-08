package com.tealcube.minecraft.bukkit.mythicdrops.utils;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
     * @param strings     List of Strings
     * @param index       index to add
     * @param string      String to add to List
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
     * @param string  String to remove
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
     * @param strings      List from which to remove
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
            if (!subList.equals(otherStrings)) {
                continue;
            }
            for (int j = size; j > 0; j--) {
                list.remove(i + j - 1);
            }
            i -= i;
        }

        return list;
    }

    public static boolean equalsColorless(List<String> strings, List<String> otherStrings) {
        if (strings == null || otherStrings == null) {
            throw new IllegalArgumentException("List<String> cannot be null");
        }
        if (strings.size() != otherStrings.size()) {
            return false;
        }
        for (int i = 0; i < strings.size(); i++) {
            String colorlessOne = strings.get(i).replace(String.valueOf('\u00A7'), "");
            String colorlessTwo = otherStrings.get(i).replace(String.valueOf('\u00A7'), "");
            if (!colorlessOne.equals(colorlessTwo)) {
                return false;
            }
        }
        return true;
    }

    public static List<String> removeIfMatchesColorless(List<String> strings,
                                                        List<String> otherStrings) {
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
            if (!equalsColorless(subList, otherStrings)) {
                continue;
            }
            for (int j = size; j > 0; j--) {
                list.remove(i + j - 1);
            }
            i -= i;
        }

        return list;
    }

    /**
     * Goes through a List and replaces a specified character with the {@link org.bukkit.ChatColor} symbol while
     * replacing two ChatColor symbols with a specified character.
     *
     * @param strings List of Strings
     * @param symbol  character to replace color codes with
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

    public static List<String> colorList(List<String> strings) {
        return colorList(strings, '&');
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
            list.add(s.replace(String.valueOf('\u00A7'), ""));
        }
        return list;
    }

}
