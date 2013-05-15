/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.mythicdrops.listeners;

import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.managers.DropManager;
import com.conventnunnery.plugins.mythicdrops.objects.CustomItem;
import com.conventnunnery.plugins.mythicdrops.objects.Tier;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class EntityListener implements Listener {
    private final MythicDrops plugin;
    private final Map<Projectile, ItemStack> projectileMap;

    public EntityListener(MythicDrops plugin) {
        this.plugin = plugin;
        projectileMap = new HashMap<Projectile, ItemStack>();
    }

    /**
     * @return the plugin
     */
    public MythicDrops getPlugin() {
        return plugin;
    }

    @EventHandler
    public void onProjectileLaunch(final ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() != null) {
            LivingEntity le = event.getEntity().getShooter();
            projectileMap.put(event.getEntity(), le.getEquipment().getItemInHand());
            Bukkit.getScheduler().runTaskLaterAsynchronously(getPlugin(), new Runnable() {
                @Override
                public void run() {
                    if (projectileMap.containsKey(event.getEntity())) {
                        projectileMap.remove(event.getEntity());
                    }
                }
            }, 20L * 30);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (getPlugin().getPluginSettings().isWorldsEnabled()
                && !getPlugin().getPluginSettings().getWorldsUse()
                .contains(event.getEntity().getWorld().getName())) {
            return;
        }
        Entity e = event.getEntity();
        Entity d = event.getDamager();
        if (!(e instanceof LivingEntity)) {
            return;
        }
        LivingEntity lee = (LivingEntity) e;
        LivingEntity led;
        ItemStack attackerHand;
        if (d instanceof LivingEntity) {
            led = (LivingEntity) d;
            attackerHand = led.getEquipment().getItemInHand();
        } else if (d instanceof Projectile) {
            led = ((Projectile) d).getShooter();
            if (!projectileMap.containsKey(d)) {
                return;
            }
            attackerHand = projectileMap.get(d);
            projectileMap.remove(d);
        } else {
            return;
        }
        getPlugin().getSocketGemManager()
                .applyEffects(led, lee, attackerHand, lee.getEquipment().getArmorContents());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player || getPlugin().getPluginSettings().isWorldsEnabled()
                && !getPlugin().getPluginSettings().getWorldsGenerate()
                .contains(event.getEntity().getWorld().getName())) {
            return;
        }

        for (ItemStack is : event.getEntity().getEquipment().getArmorContents()) {
            if (is == null || is.getType() == Material.AIR) {
                continue;
            }
            Tier t = getPlugin().getTierManager().getTierFromItemStack(is);
            if (t == null) {
                continue;
            }
            double min = is.getType().getMaxDurability() -
                    Math.max(t.getMinimumDurability(), t.getMaximumDurability()) *
                            is.getType().getMaxDurability();
            double max =
                    is.getType().getMaxDurability() -
                            Math.min(t.getMinimumDurability(), t.getMaximumDurability()) *
                                    is.getType().getMaxDurability();
            int minDura =
                    (int) min;
            int maxDura = (int) max;
            short dura = (short) (getPlugin().getRandom()
                    .nextInt(
                            Math.abs(Math.max(minDura, maxDura) - Math.min(minDura, maxDura)) + 1) +
                    Math.min(minDura, maxDura));
            is.setDurability(dura);
        }
        ItemStack is = event.getEntity().getEquipment().getItemInHand();
        if (is == null) {
            return;
        }
        Tier t = getPlugin().getTierManager().getTierFromItemStack(is);
        if (t == null) {
            return;
        }
        int minDura = (int) (is.getType().getMaxDurability() -
                Math.min(t.getMinimumDurability(), t.getMaximumDurability()) *
                        is.getType().getMaxDurability());
        int maxDura =
                (int) (is.getType().getMaxDurability() -
                        Math.max(t.getMinimumDurability(), t.getMaximumDurability()) *
                                is.getType().getMaxDurability());
        short dura = (short) (getPlugin().getRandom()
                .nextInt(Math.abs(maxDura - minDura) + 1) + minDura);
        is.setDurability(dura);
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if (getPlugin().getPluginSettings().isWorldsEnabled()
                && !getPlugin().getPluginSettings().getWorldsGenerate()
                .contains(event.getEntity().getWorld().getName())) {
            return;
        }
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER
                && getPlugin().getPluginSettings().isPreventSpawner()) {
            return;
        }
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG
                && getPlugin().getPluginSettings().isPreventSpawnEgg()) {
            return;
        }
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM &&
                getPlugin().getPluginSettings().isPreventCustom()) {
            return;
        }
        if (getPlugin().getPluginSettings().isAllowCustomToSpawn()
                && getPlugin().getRandom().nextDouble() < getPlugin()
                .getPluginSettings().getPercentageCustomDrop() &&
                !getPlugin().getDropManager().getCustomItems().isEmpty()) {
            double globalChanceToSpawn = getPlugin().getPluginSettings()
                    .getPercentageMobSpawnWithItemChance();
            double mobChanceToSpawn = 0.0;
            if (getPlugin().getPluginSettings()
                    .getAdvancedMobSpawnWithItemChanceMap()
                    .containsKey(event.getEntity().getType().name())) {
                mobChanceToSpawn = getPlugin().getPluginSettings()
                        .getAdvancedMobSpawnWithItemChanceMap().get(event.getEntity().getType().name());
            }
            double chance = globalChanceToSpawn * mobChanceToSpawn;
            for (int i = 0; i < 5; i++) {
                if (getPlugin().getRandom().nextDouble() < chance) {
                    if (getPlugin().getPluginSettings().isAllowCustomToSpawn()
                            && getPlugin().getRandom().nextDouble() < getPlugin()
                            .getPluginSettings().getPercentageCustomDrop()) {
                        if (!getPlugin().getDropManager().getCustomItems().isEmpty()) {
                            getPlugin()
                                    .getEntityManager()
                                    .equipEntity(
                                            event.getEntity(),
                                            getPlugin().getDropManager().randomCustomItemWithChance());
                            chance *= 0.5;
                        }
                        continue;
                    }
                    if (!getPlugin().getPluginSettings().isOnlyCustomItems() && !getPlugin()
                            .getDropManager()
                            .getCustomItems().isEmpty()) {
                        CustomItem customItem = getPlugin().getDropManager().randomCustomItemWithChance();
                        getPlugin().getEntityManager().equipEntity(event.getEntity(),
                                customItem);
                        chance *= 0.5;
                    }
                } else {
                    return;
                }
            }
        }
        EntityType entType = event.getEntityType();
        double globalChanceToSpawn = getPlugin().getPluginSettings()
                .getPercentageMobSpawnWithItemChance();
        double mobChanceToSpawn = 0.0;
        if (getPlugin().getPluginSettings()
                .getAdvancedMobSpawnWithItemChanceMap()
                .containsKey(entType.name())) {
            mobChanceToSpawn = getPlugin().getPluginSettings()
                    .getAdvancedMobSpawnWithItemChanceMap().get(entType.name());
        }
        double chance = globalChanceToSpawn * mobChanceToSpawn;
        for (int i = 0; i < 5; i++) {
            if (getPlugin().getRandom().nextDouble() < chance) {
                getPlugin()
                        .getEntityManager()
                        .equipEntity(
                                event.getEntity(),
                                getPlugin().getDropManager().constructItemStack(DropManager.GenerationReason.MOB_SPAWN),
                                null);
                chance *= 0.5;
            } else {
                return;
            }
        }
    }
}
