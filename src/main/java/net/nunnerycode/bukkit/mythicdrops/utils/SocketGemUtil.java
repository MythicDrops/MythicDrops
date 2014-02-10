package net.nunnerycode.bukkit.mythicdrops.utils;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketGem;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public final class SocketGemUtil {

  private SocketGemUtil() {
    // do nothing;
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

    double chosenWeight = RandomUtils.nextDouble() * totalWeight;

    double currentWeight = 0;
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
