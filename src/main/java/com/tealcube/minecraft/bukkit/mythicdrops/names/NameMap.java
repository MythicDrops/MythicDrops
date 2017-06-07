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
package com.tealcube.minecraft.bukkit.mythicdrops.names;

import com.tealcube.minecraft.bukkit.mythicdrops.api.names.NameType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.math.RandomUtils;

public final class NameMap extends ConcurrentHashMap<String, List<String>> {

  private static final NameMap _INSTANCE = new NameMap();

  private NameMap() {
    // do nothing
  }

  public static NameMap getInstance() {
    return _INSTANCE;
  }

  public String getRandomKey(NameType nameType) {
    List<String> matchingKeys = getMatchingKeys(nameType);
    String key = matchingKeys.get(RandomUtils.nextInt(matchingKeys.size()));
    return key.replace(nameType.getFormat(), "");
  }

  public List<String> getMatchingKeys(NameType nameType) {
    List<String> matchingKeys = new ArrayList<>();
    for (String key : keySet()) {
      if (key.startsWith(nameType.getFormat())) {
        matchingKeys.add(key);
      }
    }
    return matchingKeys;
  }

  public String getRandom(NameType nameType, String key) {
    List<String> list = new ArrayList<>();
    if (containsKey(nameType.getFormat() + key)) {
      list = get(nameType.getFormat() + key);
    }
    if (list == null || list.isEmpty()) {
      return "";
    }
    return list.get(RandomUtils.nextInt(list.size()));
  }

  public List<String> get(String key) {
    if (key == null) {
      return new ArrayList<>();
    }
    if (!containsKey(key)) {
      return new ArrayList<>();
    }
    return super.get(key);
  }

  @Override
  public List<String> put(String string, List<String> list) {
    if (string == null) {
      return list;
    }
    if (list == null) {
      return new ArrayList<>();
    }
    return super.put(string, list);
  }

}
