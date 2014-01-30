package net.nunnerycode.bukkit.mythicdrops.utils;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.armorsets.ArmorSet;

import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ArmorSetUtil {

  private ArmorSetUtil() {
    // do nothing
  }

  public static ArmorSet getArmorSetFromName(String name) {
    Validate.notNull(name);

    if (MythicDropsPlugin.getInstance().getArmorSetsSettings().getArmorSetMap().containsKey(name)) {
      return MythicDropsPlugin.getInstance().getArmorSetsSettings().getArmorSetMap().get(name);
    }
    return null;
  }

  public static ArmorSet getArmorSetFromItemStack(ItemStack itemStack) {
    Validate.notNull(itemStack);

    if (itemStack.getType() == Material.AIR) {
      return null;
    }

    ItemMeta
        im =
        itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta
            (itemStack.getType());
    List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<String>();
    String
        identifier =
        MythicDropsPlugin.getInstance().getArmorSetsSettings().getSetIdentifier().replace('&',
                                                                                          '\u00A7')
            .replace("\u00A7\u00A7", "&");
    for (String s : lore) {
      if (s.startsWith(identifier)) {
        String name = s.replace(identifier, "").trim();
        ArmorSet as = getArmorSetFromName(ChatColor.stripColor(name));
        if (as == null) {
          continue;
        }
        return as;
      }
    }
    return null;
  }

  public static Map<ArmorSet, Integer> getArmorSetsFromLivingEntity(LivingEntity entity) {
    Validate.notNull(entity);

    Map<ArmorSet, Integer> map = new HashMap<>();
    for (ItemStack itemStack : entity.getEquipment().getArmorContents()) {
      ArmorSet armorSet = getArmorSetFromItemStack(itemStack);
      if (armorSet == null) {
        continue;
      }
      map.put(armorSet, map.containsKey(armorSet) ? map.get(armorSet) + 1 : 1);
    }
    ArmorSet armorSet = getArmorSetFromItemStack(entity.getEquipment().getItemInHand());
    if (armorSet != null) {
      map.put(armorSet, map.containsKey(armorSet) ? map.get(armorSet) + 1 : 1);
    }
    return map;
  }

}
