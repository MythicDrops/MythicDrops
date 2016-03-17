/**
 * The MIT License
 * Copyright (c) 2013 Teal Cube Games
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.durability;

import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TierUtil;

import org.bukkit.Material;
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
        if (event.getPlayer().getEquipment().getItemInMainHand() == null) {
            return;
        }
        ItemStack iimh = event.getPlayer().getEquipment().getItemInMainHand();
        Tier t = iimh != null ? TierUtil.getTierFromItemStack(iimh) : null;
        if (t == null) {
            return;
        }
        if (t.isInfiniteDurability()) {
            iimh.setDurability((short) 0);
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
        ItemStack iimh = p.getEquipment().getItemInMainHand();
        Tier t = iimh != null ? TierUtil.getTierFromItemStack(iimh) : null;
        if (t != null && t.isInfiniteDurability()) {
            p.getEquipment().getItemInMainHand().setDurability((short) 0);
        }
        for (ItemStack is : p.getEquipment().getArmorContents()) {
            if (is == null || is.getType() == Material.AIR) {
                continue;
            }
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
        ItemStack iimh = p.getEquipment().getItemInMainHand();
        Tier t = iimh != null ? TierUtil.getTierFromItemStack(iimh) : null;
        if (t != null && t.isInfiniteDurability()) {
            iimh.setDurability((short) 0);
        }
    }

}
