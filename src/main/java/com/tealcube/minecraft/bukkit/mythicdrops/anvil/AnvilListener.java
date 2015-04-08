package com.tealcube.minecraft.bukkit.mythicdrops.anvil;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import com.tealcube.minecraft.bukkit.mythicdrops.socketting.SocketGem;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.SocketGemUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TierUtil;

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
