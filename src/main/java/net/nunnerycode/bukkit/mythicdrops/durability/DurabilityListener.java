package net.nunnerycode.bukkit.mythicdrops.durability;

import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.utils.TierUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

public final class DurabilityListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getPlayer() == null) {
            return;
        }
        if (event.getPlayer().getItemInHand() == null) {
            return;
        }
        Tier t = TierUtil.getTierFromItemStack(event.getPlayer().getItemInHand());
        if (t == null) {
            return;
        }
        if (t.isInfiniteDurability()) {
            event.getPlayer().getItemInHand().setDurability((short) 0);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) event.getEntity();
        Tier t = TierUtil.getTierFromItemStack(p.getEquipment().getItemInHand());
        if (t != null && t.isInfiniteDurability()) {
            p.getEquipment().getItemInHand().setDurability((short) 0);
        }
        for (ItemStack is : p.getEquipment().getArmorContents()) {
            t = TierUtil.getTierFromItemStack(is);
            if (t == null) {
               continue;
            }
            if (t.isInfiniteDurability()) {
                is.setDurability((short) 0);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityShootBowEvent(EntityShootBowEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) event.getEntity();
        Tier t = TierUtil.getTierFromItemStack(p.getEquipment().getItemInHand());
        if (t != null && t.isInfiniteDurability()) {
            p.getEquipment().getItemInHand().setDurability((short) 0);
        }
    }

}
