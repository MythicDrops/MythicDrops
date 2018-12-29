/*
 * The MIT License
 * Copyright © 2013 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.aura;

import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.EffectTarget;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketEffect;
import com.tealcube.minecraft.bukkit.mythicdrops.socketting.SocketGem;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.SocketGemUtil;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

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
        socketGems.addAll(getSocketGems(le.getEquipment().getItemInMainHand()));

        for (SocketGem sg : socketGems) {
          for (SocketEffect se : sg.getSocketEffects()) {
            if (se.getEffectTarget() != EffectTarget.AURA) {
              continue;
            }
            if (se.isAffectsTarget()) {
              for (Entity entity : le.getNearbyEntities(se.getRadius(), se.getRadius(), se.getRadius())) {
                if (!(entity instanceof LivingEntity)) {
                  continue;
                }
                LivingEntity livingEntity = (LivingEntity) entity;
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
    if (itemStack == null || itemStack.getType() == Material.AIR) {
      return socketGemList;
    }
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
