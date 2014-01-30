package net.nunnerycode.bukkit.mythicdrops.utils;

import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.items.CustomItemMap;

import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;

public final class CustomItemUtil {

  private CustomItemUtil() {
    // do nothing
  }

  public static CustomItem getCustomItemFromItemStack(ItemStack itemStack) {
    Validate.notNull(itemStack, "ItemStack cannot be null");

    for (CustomItem ci : CustomItemMap.getInstance().values()) {
      if (ci.toItemStack().isSimilar(itemStack)) {
        return ci;
      }
    }

    return null;
  }

}
