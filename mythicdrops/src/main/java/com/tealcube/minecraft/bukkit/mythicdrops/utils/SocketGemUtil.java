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
package com.tealcube.minecraft.bukkit.mythicdrops.utils;

import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketGem;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

/** @see GemUtil */
@Deprecated
public final class SocketGemUtil {

  private SocketGemUtil() {
    // do nothing;
  }

  /** @see GemUtil#getSocketGemFromPotentialSocketItem(ItemStack) */
  @Deprecated
  public static SocketGem getSocketGemFromItemStack(ItemStack itemStack) {
    return GemUtil.INSTANCE.getSocketGemFromPotentialSocketItem(itemStack);
  }

  /** @see GemUtil#getSocketGemFromName(String) */
  @Deprecated
  public static SocketGem getSocketGemFromName(String name) {
    return GemUtil.INSTANCE.getSocketGemFromName(name);
  }

  /** @see GemUtil#getRandomSocketGemByWeight() */
  @Deprecated
  public static SocketGem getRandomSocketGemWithChance() {
    return GemUtil.INSTANCE.getRandomSocketGemByWeight();
  }

  /** @see GemUtil#getRandomSocketGemByWeight(EntityType) */
  @Deprecated
  public static SocketGem getRandomSocketGemWithChance(EntityType entityType) {
    return GemUtil.INSTANCE.getRandomSocketGemByWeight(entityType);
  }

  /** @see GemUtil#getRandomSocketGemMaterial() */
  @Deprecated
  public static Material getRandomSocketGemMaterial() {
    return GemUtil.INSTANCE.getRandomSocketGemMaterial();
  }
}
