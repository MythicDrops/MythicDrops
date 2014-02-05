package net.nunnerycode.bukkit.mythicdrops.identification;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.items.NonrepairableItemStack;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public final class UnidentifiedItem extends NonrepairableItemStack {

  public UnidentifiedItem(Material material) {
    this(material, 1, (short) 0);
  }

  public UnidentifiedItem(Material material, int amount, short durability) {
    super(material, amount, durability, ChatColor.WHITE + MythicDropsPlugin.getInstance()
        .getIdentifyingSettings().getUnidentifiedItemName() + ChatColor.WHITE,
          MythicDropsPlugin.getInstance()
              .getIdentifyingSettings().getUnidentifiedItemLore());
  }

}