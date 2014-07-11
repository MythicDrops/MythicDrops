package net.nunnerycode.bukkit.mythicdrops.anvil;

import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketGem;
import net.nunnerycode.bukkit.mythicdrops.utils.SocketGemUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.TierUtil;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class AnvilListener implements Listener {

    private final MythicDrops mythicDrops;

    public AnvilListener(MythicDrops mythicDrops) {
        this.mythicDrops = mythicDrops;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemRename(InventoryClickEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (!mythicDrops.getConfigSettings().isRepairingEnabled()) {
            return;
        }
        HumanEntity ent = e.getWhoClicked();
        if (!(ent instanceof Player)) {
            return;
        }
        Inventory inv = e.getInventory();
        if (!(inv instanceof AnvilInventory)) {
            return;
        }
        ItemStack fis = inv.getItem(0);
        ItemStack sis = inv.getItem(1);
        Tier ft = fis != null ? TierUtil.getTierFromItemStack(fis) : null;
        Tier st = sis != null ? TierUtil.getTierFromItemStack(sis) : null;
        SocketGem fsg = fis != null ? SocketGemUtil.getSocketGemFromItemStack(fis) : null;
        SocketGem stg = sis != null ? SocketGemUtil.getSocketGemFromItemStack(sis) : null;
        if ((ft != null || st != null || fsg != null || stg != null) && e.getSlot() == 2) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
        }
    }

}