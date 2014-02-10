package net.nunnerycode.bukkit.mythicdrops.anvil;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.utils.TierUtil;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class AnvilListener implements Listener {

  @EventHandler(priority = EventPriority.MONITOR)
  public void onItemRename(InventoryClickEvent e) {
    if (!e.isCancelled() && MythicDropsPlugin.getInstance().getConfigSettings().isRepairingEnabled()) {
      HumanEntity ent = e.getWhoClicked();
      if (ent instanceof Player) {
        Inventory inv = e.getInventory();
        if (inv instanceof AnvilInventory) {
          ItemStack fis = inv.getItem(0);
          ItemStack sis = inv.getItem(1);
          Tier ft = fis != null ? TierUtil.getTierFromItemStack(fis) : null;
          Tier st = sis != null ? TierUtil.getTierFromItemStack(sis) : null;
          if (ft != null || st != null) {
            if (e.getSlot() == 2) {
              e.setCancelled(true);
            }
          }
        }
      }
    }
  }

}
