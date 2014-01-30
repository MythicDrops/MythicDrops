package net.nunnerycode.bukkit.mythicdrops.utils;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketGem;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class SocketGemUtil {

  private SocketGemUtil() {
    // do nothing;
  }

  public static ItemStack addSockets(ItemStack stack, int amount) {
    Validate.notNull(stack, "ItemStack cannot be null");

    ItemStack is = stack;
    for (int i = 0; i < amount; i++) {
      is = addSocket(is);
    }
    return is;
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

  public static MaterialData getRandomSocketGemMaterial() {
    List<MaterialData> materialDatas = MythicDropsPlugin.getInstance().getSockettingSettings()
        .getSocketGemMaterialDatas();
    if (materialDatas == null || materialDatas.isEmpty()) {
      return null;
    }
    return materialDatas.get(RandomUtils.nextInt(materialDatas.size()));
  }

  public static ItemStack addSocket(ItemStack stack) {
    Validate.notNull(stack, "ItemStack cannot be null");

    ItemMeta
        itemMeta =
        stack.hasItemMeta() ? stack.getItemMeta()
                            : Bukkit.getItemFactory().getItemMeta(stack.getType());
    if (itemMeta == null) {
      return stack;
    }

    List<String> lore = new ArrayList<>();
    if (itemMeta.hasLore()) {
      lore = new ArrayList<>(itemMeta.getLore());
    }

    lore.add(MythicDropsPlugin.getInstance().getSockettingSettings().getSockettedItemString());

    itemMeta.setLore(lore);
    stack.setItemMeta(itemMeta);

    return stack;
  }

}
