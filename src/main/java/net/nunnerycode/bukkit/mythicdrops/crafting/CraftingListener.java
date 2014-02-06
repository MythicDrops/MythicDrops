package net.nunnerycode.bukkit.mythicdrops.crafting;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketGem;
import net.nunnerycode.bukkit.mythicdrops.utils.SocketGemUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.StringUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class CraftingListener implements Listener {

  private MythicDrops plugin;

  public CraftingListener(MythicDrops plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onItemCraftEvent(CraftItemEvent event) {
    String replaceString = plugin.getSockettingSettings().getSocketGemName().replace('&',
                                                                                     '\u00A7')
        .replace("\u00A7\u00A7", "&").replaceAll("%(?s)(.*?)%", "").replaceAll("\\s+", " ");
    String[] splitString = ChatColor.stripColor(replaceString).split(" ");
    for (ItemStack is : event.getInventory().getMatrix()) {
      if (is == null) {
        continue;
      }
      if (is.getType() == Material.AIR) {
        continue;
      }
      if (!is.hasItemMeta()) {
        continue;
      }
      ItemMeta im = is.getItemMeta();
      if (!im.hasDisplayName()) {
        continue;
      }
      String displayName = im.getDisplayName();
      String colorlessName = ChatColor.stripColor(displayName);

      for (String s : splitString) {
        if (colorlessName.contains(s)) {
          colorlessName = colorlessName.replace(s, "");
        }
      }

      colorlessName = colorlessName.replaceAll("\\s+", " ").trim();

      SocketGem socketGem = SocketGemUtil.getSocketGemFromName(colorlessName);
      if (socketGem == null) {
        continue;
      }
      String otherName = ChatColor.GOLD + StringUtil.replaceArgs(MythicDropsPlugin
                                                    .getInstance()
                                                    .getSockettingSettings()
                                                    .getSocketGemName(),
                                                new String[][]{
                                                    {"%socketgem%",
                                                     socketGem
                                                         .getName()}}).replace('&', '\u00A7')
          .replace("\u00A7\u00A7", "&") + ChatColor.GOLD;
      if (displayName.equals(otherName)) {
        event.setCancelled(true);
        return;
      }
    }
  }

}
