package net.nunnerycode.bukkit.mythicdrops.utils;

import net.nunnerycode.bukkit.libraries.ivory.utils.StringUtils;
import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketGem;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class SocketGemUtil {

  private SocketGemUtil() {
    // do nothing;
  }

  public static SocketGem getSocketGemFromItemStack(ItemStack itemStack) {
    SocketGem sg = null;
    if (!MythicDropsPlugin.getInstance().getSockettingSettings().getSocketGemMaterials().contains
        (itemStack.getType())) {
      return null;
    }
    if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
      return null;
    }
    String
        replacedArgs =
        ChatColor.stripColor(StringUtils.replaceArgs(MythicDropsPlugin.getInstance()
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
      if (sg.getName().equalsIgnoreCase(name)) {
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
    return materialDatas.get(RandomUtils.nextInt(materialDatas.size()));
  }

}
