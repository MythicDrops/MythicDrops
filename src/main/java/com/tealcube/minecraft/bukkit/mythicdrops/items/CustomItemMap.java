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
package com.tealcube.minecraft.bukkit.mythicdrops.items;

import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.RandomUtils;

/**
 * An extension of {@link ConcurrentHashMap} designed to allow easy developer access to {@link CustomItem}s.
 */
public final class CustomItemMap extends ConcurrentHashMap<String, CustomItem> {

  private static final CustomItemMap _INSTANCE = new CustomItemMap();

  private CustomItemMap() {
    // do nothing
  }

  /**
   * Gets the instance of CustomItemMap running on the server.
   *
   * @return instance running on the server
   */
  public static CustomItemMap getInstance() {
    return _INSTANCE;
  }

  /**
   * Gets a random {@link CustomItem} out of the ones loaded on the server using chance.
   *
   * @return random CustomItem
   */
  public CustomItem getRandom() {
    CustomItem[] valueArray = values().toArray(new CustomItem[values().size()]);
    return valueArray[RandomUtils.nextInt(0, values().size())];
  }

  /**
   * Gets a random {@link CustomItem} out of the ones loaded on the server using chance. Returns null if none found.
   *
   * @return random CustomItem
   */
  public CustomItem getRandomWithChance() {
    double totalWeight = 0;
    for (CustomItem ci : values()) {
      totalWeight += ci.getChanceToBeGivenToAMonster();
    }

    double chosenWeight = RandomUtils.nextDouble(0D, 1D) * totalWeight;

    double currentWeight = 0;
    for (CustomItem ci : values()) {
      currentWeight += ci.getChanceToBeGivenToAMonster();

      if (currentWeight >= chosenWeight) {
        return ci;
      }
    }

    return null;
  }

}
