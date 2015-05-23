package com.tealcube.minecraft.bukkit.mythicdrops.aura;

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


import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.EffectTarget;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketEffect;
import com.tealcube.minecraft.bukkit.mythicdrops.socketting.SocketGem;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.SocketGemUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public final class AuraRunnable extends BukkitRunnable {

    @Override
    public void run() {
        for (World w : Bukkit.getWorlds()) {
            for (Entity e : w.getEntities()) {
                if (!(e instanceof LivingEntity)) {
                    continue;
                }
                LivingEntity le = (LivingEntity) e;
                List<SocketGem> socketGems = new ArrayList<>();
                for (ItemStack is : le.getEquipment().getArmorContents()) {
                    if (is == null || is.getType() == Material.AIR) {
                        continue;
                    }
                    socketGems.addAll(getSocketGems(is));
                }
                socketGems.addAll(getSocketGems(le.getEquipment().getItemInHand()));

                for (SocketGem sg : socketGems) {
                    for (SocketEffect se : sg.getSocketEffects()) {
                        if (se.getEffectTarget() != EffectTarget.AURA) {
                            continue;
                        }
                        for (Entity entity : le.getNearbyEntities(se.getRadius(), se.getRadius(), se.getRadius())) {
                            if (!(entity instanceof LivingEntity)) {
                                continue;
                            }
                            LivingEntity livingEntity = (LivingEntity) entity;
                            if (se.isAffectsTarget()) {
                                se.apply(livingEntity);
                            }
                        }
                        if (se.isAffectsWielder()) {
                            se.apply(le);
                        }
                    }
                }
            }
        }
    }

    private List<SocketGem> getSocketGems(ItemStack itemStack) {
        List<SocketGem> socketGemList = new ArrayList<>();
        ItemMeta im;
        if (itemStack.hasItemMeta()) {
            im = itemStack.getItemMeta();
        } else {
            return socketGemList;
        }
        List<String> lore = im.getLore();
        if (lore == null) {
            return socketGemList;
        }
        for (String s : lore) {
            SocketGem sg = SocketGemUtil.getSocketGemFromName(ChatColor.stripColor(s));
            if (sg == null) {
                continue;
            }
            socketGemList.add(sg);
        }
        return socketGemList;
    }
}
