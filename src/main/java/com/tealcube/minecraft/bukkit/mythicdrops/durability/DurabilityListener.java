package com.tealcube.minecraft.bukkit.mythicdrops.durability;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby
 * granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER
 * IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TierUtil;
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
