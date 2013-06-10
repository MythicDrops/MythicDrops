/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.mythicdrops.listeners;

import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.managers.DropManager;
import com.conventnunnery.plugins.mythicdrops.objects.CustomItem;
import com.conventnunnery.plugins.mythicdrops.objects.Tier;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EntityListener implements Listener {
    public static final long ONE_SECOND = 20L;
    public static final long THIRTY_SECONDS = ONE_SECOND * 30;
    public static final int NUMBER_OF_SLOTS = 5;
    public static final double HALF = 0.5;
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onProjectileLaunch(final ProjectileLaunchEvent event) {
        if (event.isCancelled()) {
            return;
        }
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
            }, THIRTY_SECONDS);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
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
        if (d instanceof LivingEntity) {
            led = (LivingEntity) d;
        } else if (d instanceof Projectile) {
            led = ((Projectile) d).getShooter();
            if (projectileMap.containsKey(d)) {
                projectileMap.remove(d);
            }
        } else {
            return;
        }
        getPlugin().getSocketGemManager().applyEffects(led, lee);
        getPlugin().getSocketGemManager().runCommands(led, lee);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player || getPlugin().getPluginSettings().isWorldsEnabled()
                && !getPlugin().getPluginSettings().getWorldsGenerate()
                .contains(event.getEntity().getWorld().getName())) {
            return;
        }

        if (event.getEntity().getLastDamageCause() == null) {
            return;
        }

        switch (event.getEntity().getLastDamageCause().getCause()) {
            case CONTACT:
            case SUFFOCATION:
            case FALL:
            case FIRE_TICK:
            case MELTING:
            case LAVA:
            case DROWNING:
            case BLOCK_EXPLOSION:
            case VOID:
            case LIGHTNING:
            case SUICIDE:
            case STARVATION:
            case WITHER:
            case FALLING_BLOCK:
            case CUSTOM:
                return;
        }

        Set<ItemStack> newDrops = new HashSet<ItemStack>();

        for (ItemStack is : event.getEntity().getEquipment().getArmorContents()) {
            if (is == null || is.getType() == Material.AIR) { continue; }
            if (!is.hasItemMeta()) { continue; }
            if (!is.getItemMeta().hasDisplayName()) { continue; }
            Tier tier = getPlugin().getTierManager().getTierFromItemStack(is);
            if (tier == null) { continue; }
            if (plugin.getRandom().nextDouble() < tier.getChanceToDropOnMonsterDeath()) {
                ItemStack newItemStack = getPlugin().getDropManager().constructItemStack(tier, is.getData(),
                        DropManager.GenerationReason.MOB_SPAWN);
                newDrops.add(newItemStack);
            }
        }

        if (event.getEntity().getEquipment().getItemInHand() != null && event.getEntity().getEquipment()
                .getItemInHand()
                .getType() != Material.AIR) {
            ItemStack is = event.getEntity().getEquipment().getItemInHand();
            if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
                Tier tier = getPlugin().getTierManager().getTierFromItemStack(is);
                if (tier != null && plugin.getRandom().nextDouble() < tier
                        .getChanceToDropOnMonsterDeath()) {
                    ItemStack newItemStack = getPlugin().getDropManager().constructItemStack(tier, is.getData(),
                            DropManager.GenerationReason.MOB_SPAWN);
                    newDrops.add(newItemStack);
                }
            }
        }

        Location location = event.getEntity().getLocation();
        for (ItemStack itemstack : newDrops) {
            location.getWorld().dropItemNaturally(location, itemstack);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if (event.isCancelled()) {
            return;
        }
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
            for (int i = 0; i < NUMBER_OF_SLOTS; i++) {
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
                            chance *= HALF;
                        }
                        continue;
                    }
                    if (!getPlugin().getPluginSettings().isOnlyCustomItems() && !getPlugin()
                            .getDropManager()
                            .getCustomItems().isEmpty()) {
                        CustomItem customItem = getPlugin().getDropManager().randomCustomItemWithChance();
                        getPlugin().getEntityManager().equipEntity(event.getEntity(),
                                customItem);
                        chance *= HALF;
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
        for (int i = 0; i < NUMBER_OF_SLOTS; i++) {
            if (getPlugin().getRandom().nextDouble() < chance) {
                List<String> tiersPerMob = getPlugin().getPluginSettings().getTiersPerMob().get(event.getEntity()
                        .getType().name());
                List<Tier> tierSet = getPlugin().getTierManager()
                        .getTiersFromString(tiersPerMob);
                Tier t = getPlugin().getTierManager().filteredRandomTierWithChance(tierSet);
                if (t == null) {
                    continue;
                }
                ItemStack is = getPlugin().getDropManager().constructItemStack(t, DropManager.GenerationReason
                        .MOB_SPAWN);
                getPlugin()
                        .getEntityManager()
                        .equipEntity(event.getEntity(), is);
                chance *= HALF;
            } else {
                return;
            }
        }
    }
}
