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

import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin;
import com.tealcube.minecraft.bukkit.mythicdrops.socketting.SocketGem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.RandomUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class SocketGemUtil {

  private SocketGemUtil() {
    // do nothing;
  }

  public static SocketGem getSocketGemFromItemStack(ItemStack itemStack) {
    SocketGem sg;
    if (!MythicDropsPlugin.getInstance().getSockettingSettings().getSocketGemMaterials().contains
        (itemStack.getType())) {
      return null;
    }
    if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
      return null;
    }
    String
        replacedArgs =
        ChatColor.stripColor(StringUtil.replaceArgs(MythicDropsPlugin.getInstance()
                .getSockettingSettings()
                .getSocketGemName(),
            new String[][]{{"%socketgem%", ""}}
        )
            .replace('&', '\u00A7')
            .replace("\u00A7\u00A7", "&"));
    String type = ChatColor.stripColor(
        itemStack.getItemMeta().getDisplayName().replace(replacedArgs, ""));
    if (type == null) {
      return null;
    }
    sg = MythicDropsPlugin.getInstance().getSockettingSettings().getSocketGemMap().get(type);
    if (sg == null) {
      sg = SocketGemUtil.getSocketGemFromName(type);
    }
    return sg;
  }

  public static SocketGem getSocketGemFromName(String name) {
    for (SocketGem sg : MythicDropsPlugin.getInstance().getSockettingSettings().getSocketGemMap()
        .values()) {
      if (sg.getName().equalsIgnoreCase(name) || sg.getName().equalsIgnoreCase(name.replace("_", " "))) {
        return sg;
      }
    }
    return null;
  }

  public static SocketGem getRandomSocketGemWithChance() {
    Map<String, SocketGem>
        socketGemMap =
        MythicDropsPlugin.getInstance().getSockettingSettings().getSocketGemMap
            ();
    if (socketGemMap == null || socketGemMap.isEmpty()) {
      return null;
    }
    double totalWeight = 0;
    for (SocketGem sg : socketGemMap.values()) {
      totalWeight += sg.getChance();
    }

    double chosenWeight = MythicDropsPlugin.getInstance().getRandom().nextDouble() * totalWeight;

    double currentWeight = 0;

    List<SocketGem> l = new ArrayList<>(socketGemMap.values());
    Collections.shuffle(l);

    for (SocketGem sg : socketGemMap.values()) {
      currentWeight += sg.getChance();

      if (currentWeight >= chosenWeight) {
        return sg;
      }
    }
    return null;
  }

  public static Material getRandomSocketGemMaterial() {
    List<Material> materialDatas = MythicDropsPlugin.getInstance().getSockettingSettings()
        .getSocketGemMaterials();
    if (materialDatas == null || materialDatas.isEmpty()) {
      return null;
    }
    return materialDatas.get(RandomUtils.nextInt(0, materialDatas.size()));
  }

}
