package net.nunnerycode.bukkit.mythicdrops.aura;

import net.nunnerycode.bukkit.mythicdrops.api.socketting.EffectTarget;
import net.nunnerycode.bukkit.mythicdrops.api.socketting.SocketEffect;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketGem;
import net.nunnerycode.bukkit.mythicdrops.utils.SocketGemUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public final class AuraRunnable extends BukkitRunnable {
Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
@Override
public void run() {
    public void run() {
        for (World w : Bukkit.getWorlds()) {
            for (Entity e : w.getEntities()) {
                if (!(e instanceof LivingEntity)) {
                    continue;
                }
                LivingEntity le = (LivingEntity) e;
                List<SocketGem> socketGems = new ArrayList<>();
                for (ItemStack is : le.getEquipment().getArmorContents()) {
                    socketGems.addAll(getSocketGems(is));
                }
                socketGems.addAll(getSocketGems(le.getEquipment().getItemInHand()));

                for (SocketGem sg : socketGems) {
                    for (SocketEffect se : sg.getSocketEffects()) {
                        if (se.getEffectTarget() != EffectTarget.AURA) {
                            continue;
                        }
                        for (Entity entity : le
                                .getNearbyEntities(se.getRadius(), se.getRadius(), se.getRadius())) {
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
