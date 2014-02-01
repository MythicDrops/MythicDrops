package net.nunnerycode.bukkit.mythicdrops.anvil;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class AnvilListener implements Listener {

  @EventHandler(priority = EventPriority.MONITOR)
  public void onItemRename(InventoryClickEvent e) {
    if (!e.isCancelled() && MythicDropsPlugin.getInstance().getRepairingSettings().isEnabled()) {
      HumanEntity ent = e.getWhoClicked();
      if (ent instanceof Player) {
        Inventory inv = e.getInventory();
        if (inv instanceof AnvilInventory) {
          InventoryView view = e.getView();
          int rawSlot = e.getRawSlot();
          if (rawSlot == view.convertSlot(rawSlot) && rawSlot == 2) {
            ItemStack item = e.getCurrentItem();
            if (item != null) {
              ItemMeta meta = item.getItemMeta();
              if (meta != null && meta.hasDisplayName()) {
                e.setCancelled(true);
              }
            }
          }
        }
      }
    }
  }

}
