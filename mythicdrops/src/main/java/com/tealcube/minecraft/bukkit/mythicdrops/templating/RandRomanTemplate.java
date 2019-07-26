/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2019 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.templating;

import com.google.common.base.Splitter;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public final class RandRomanTemplate extends Template {

  private static final Pattern DASH_PATTERN = Pattern.compile("\\s*[-]\\s*");
  private static final TreeMap<Integer, String> ROMAN_MAP = new TreeMap<>();

  static {
    ROMAN_MAP.put(1000, "M");
    ROMAN_MAP.put(900, "CM");
    ROMAN_MAP.put(500, "D");
    ROMAN_MAP.put(400, "CD");
    ROMAN_MAP.put(100, "C");
    ROMAN_MAP.put(90, "XC");
    ROMAN_MAP.put(50, "L");
    ROMAN_MAP.put(40, "XL");
    ROMAN_MAP.put(10, "X");
    ROMAN_MAP.put(9, "IX");
    ROMAN_MAP.put(5, "V");
    ROMAN_MAP.put(4, "IV");
    ROMAN_MAP.put(1, "I");
  }

  public RandRomanTemplate() {
    super("randroman");
  }

  @Override
  public String apply(String s) {
    if (StringUtils.isEmpty(s)) {
      return s;
    }
    List<String> split = Splitter.on(DASH_PATTERN).trimResults().omitEmptyStrings().splitToList(s);
    int first = NumberUtils.toInt(split.get(0));
    int second = NumberUtils.toInt(split.get(1));
    int min = Math.min(first, second);
    int max = Math.max(first, second);
    int random = (int) Math.round((Math.random() * (max - min) + min));
    return toRoman(random);
  }

  private String toRoman(int number) {
    int l = ROMAN_MAP.floorKey(number);
    if (number == l) {
      return ROMAN_MAP.get(number);
    }
    return ROMAN_MAP.get(l) + toRoman(number - l);
  }

}
