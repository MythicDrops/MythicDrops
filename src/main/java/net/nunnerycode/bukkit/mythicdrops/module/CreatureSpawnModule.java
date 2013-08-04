package net.nunnerycode.bukkit.mythicdrops.module;

import net.nunnerycode.bukkit.libraries.module.Module;
import net.nunnerycode.bukkit.libraries.utils.ItemStackUtils;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public final class CreatureSpawnModule extends Module {

    private final MythicDrops plugin;
    private boolean worldsEnabled;
    private List<String> worldsGenerate;
    private boolean preventSpawner;
    private boolean preventSpawnEgg;
    private boolean preventCustom;
    private double globalSpawnChance;
    private boolean customItemsSpawn;
    private boolean onlyCustomItemsSpawn;
    private double customItemsChance;
    private Map<EntityType, Set<Tier>> tierDrops;
    private Map<EntityType, Double> chanceToSpawn;

    public CreatureSpawnModule() {
        super();
        plugin = MythicDrops.instance;
        if (getFileConfiguration() == null) {
            return;
        }
        globalSpawnChance = getFileConfiguration().getDouble("globalSpawnChance", 0.25);
        worldsEnabled = getFileConfiguration().getBoolean("worlds.enabled", false);
        worldsGenerate = getFileConfiguration().getStringList("worlds.generate");
        preventSpawner = getFileConfiguration().getBoolean("spawnPrevention.spawner", true);
        preventSpawnEgg = getFileConfiguration().getBoolean("spawnPrevention.spawnEgg", true);
        preventCustom = getFileConfiguration().getBoolean("spawnPrevention.custom", true);
        customItemsSpawn = getFileConfiguration().getBoolean("customItems.spawn", true);
        onlyCustomItemsSpawn = getFileConfiguration().getBoolean("customItems.onlySpawn", false);
        customItemsChance = getFileConfiguration().getDouble("customItems.chance", 0.05);

        tierDrops = new HashMap<EntityType, Set<Tier>>();

        if (getFileConfiguration().isConfigurationSection("tierDrops")) {
            ConfigurationSection cs = getFileConfiguration().getConfigurationSection("tierDrops");
            for (String key : cs.getKeys(false)) {
                EntityType entityType = EntityType.fromName(key);
                if (entityType == null) {
                    continue;
                }
                tierDrops.put(entityType, plugin.getTierManager().getTiersFromStringSet(new HashSet<String>(cs
                        .getStringList(key))));
            }
        }

        chanceToSpawn = new HashMap<EntityType, Double>();

        if (getFileConfiguration().isConfigurationSection("spawnWithDropChance")) {
            ConfigurationSection cs = getFileConfiguration().getConfigurationSection("spawnWithDropChance");
            for (String key : cs.getKeys(false)) {
                EntityType entityType = EntityType.fromName(key);
                if (entityType == null) {
                    continue;
                }
                chanceToSpawn.put(entityType, cs.getDouble(key, 1.0));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (worldsEnabled && !worldsGenerate.contains(event.getLocation().getWorld().getName())) {
            return;
        }
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER
                && preventSpawner) {
            return;
        }
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG
                && preventSpawnEgg) {
            return;
        }
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM &&
                preventCustom) {
            return;
        }
        double chance = globalSpawnChance * chanceToSpawn.get(event.getEntity().getType());
        if (onlyCustomItemsSpawn) {
            if (customItemsSpawn && RandomUtils.nextDouble() < customItemsChance && !plugin.getCustomItemManager()
                    .getCustomItems().isEmpty() && chanceToSpawn.containsKey(event.getEntity().getType()) && tierDrops
                    .containsKey(event.getEntity().getType())) {
                for (int i = 0; i < 5; i++) {
                    if (RandomUtils.nextDouble() < chance) {
                        plugin.getEntityManager().equipEntity(event.getEntity(),
                                plugin.getCustomItemManager().getRandomCustomItemWithChance());
                        chance *= 0.5;
                        continue;
                    }
                    break;
                }
            }
            return;
        }
        for (int i = 0; i < 5; i++) {
            if (RandomUtils.nextDouble() < chance) {
                plugin.getEntityManager().equipEntity(event.getEntity(),
                        plugin.getDropManager().constructItemStackFromTier(plugin.getTierManager()
                                .getFilteredRandomTierFromSetWithChance(tierDrops.get(event.getEntity().getType())),
                                ItemGenerationReason.MONSTER_SPAWN));
                chance *= 0.5;
                continue;
            }
            break;
        }
        if (customItemsSpawn && RandomUtils.nextDouble() < customItemsChance && !plugin.getCustomItemManager()
                .getCustomItems().isEmpty() && chanceToSpawn.containsKey(event.getEntity().getType()) && tierDrops
                .containsKey(event.getEntity().getType())) {
            for (int i = 0; i < 5; i++) {
                if (RandomUtils.nextDouble() < chance) {
                    plugin.getEntityManager().equipEntity(event.getEntity(),
                            plugin.getCustomItemManager().getRandomCustomItemWithChance());
                    chance *= 0.5;
                    continue;
                }
                break;
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCreatureDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player || worldsEnabled
                && !worldsGenerate.contains(event.getEntity().getWorld().getName())) {
            return;
        }

        if (event.getEntity().getLastDamageCause() == null) {
            return;
        }

        EntityDamageEvent.DamageCause damageCause = event.getEntity().getLastDamageCause().getCause();

        if (damageCause == EntityDamageEvent.DamageCause.CONTACT || damageCause == EntityDamageEvent.DamageCause
                .SUFFOCATION || damageCause == EntityDamageEvent.DamageCause.FALL || damageCause == EntityDamageEvent
                .DamageCause.FIRE_TICK || damageCause == EntityDamageEvent.DamageCause.MELTING || damageCause ==
                EntityDamageEvent.DamageCause.LAVA || damageCause == EntityDamageEvent.DamageCause.DROWNING ||
                damageCause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || damageCause == EntityDamageEvent
                .DamageCause.BLOCK_EXPLOSION || damageCause == EntityDamageEvent.DamageCause.VOID || damageCause ==
                EntityDamageEvent.DamageCause.LIGHTNING || damageCause == EntityDamageEvent.DamageCause.SUICIDE ||
                damageCause == EntityDamageEvent.DamageCause.STARVATION || damageCause == EntityDamageEvent
                .DamageCause.WITHER || damageCause == EntityDamageEvent.DamageCause.FALLING_BLOCK || damageCause ==
                EntityDamageEvent.DamageCause.CUSTOM) {
            return;
        }

        Set<ItemStack> newDrops = new HashSet<ItemStack>();

        for (ItemStack is : event.getEntity().getEquipment().getArmorContents()) {
            if (is == null || is.getType() == Material.AIR) {
                continue;
            }
            if (!is.hasItemMeta()) {
                continue;
            }
            if (!is.getItemMeta().hasDisplayName()) {
                continue;
            }
            CustomItem ci;
            try {
                ci = plugin.getCustomItemManager().getCustomItemFromItemStack(is);
            } catch (NullPointerException e) {
                ci = null;
            }
            if (ci != null) {
                if (RandomUtils.nextDouble() < ci.getChanceToDropOnDeath()) {
                    newDrops.add(ci.toItemStack());
                    continue;
                }
            }
            Tier tier = plugin.getTierManager().getTierFromItemStack(is);
            if (tier == null) {
                continue;
            }
            if (RandomUtils.nextDouble() < tier.getChanceToDropOnMonsterDeath()) {
                ItemStack newItemStack = is.getData().toItemStack(is.getAmount());
                newItemStack.setItemMeta(is.getItemMeta().clone());
                newItemStack.setDurability(ItemStackUtils.getDurabilityForMaterial(is.getType(),
                        tier.getMinimumDurabilityPercentage(), tier.getMaximumDurabilityPercentage()));
                newDrops.add(newItemStack);
            }
        }

        if (event.getEntity().getEquipment().getItemInHand() != null && event.getEntity().getEquipment()
                .getItemInHand()
                .getType() != Material.AIR) {
            ItemStack is = event.getEntity().getEquipment().getItemInHand();
            if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
                CustomItem ci;
                try {
                    ci = plugin.getCustomItemManager().getCustomItemFromItemStack(is);
                } catch (NullPointerException e) {
                    ci = null;
                }
                if (ci != null) {
                    if (RandomUtils.nextDouble() < ci.getChanceToDropOnDeath()) {
                        newDrops.add(ci.toItemStack());
                    }
                } else {
                    Tier tier = plugin.getTierManager().getTierFromItemStack(is);
                    if (tier != null && RandomUtils.nextDouble() < tier
                            .getChanceToDropOnMonsterDeath()) {
                        ItemStack newItemStack = is.getData().toItemStack(is.getAmount());
                        newItemStack.setItemMeta(is.getItemMeta().clone());
                        newItemStack.setDurability(ItemStackUtils.getDurabilityForMaterial(is.getType(),
                                tier.getMinimumDurabilityPercentage(), tier.getMaximumDurabilityPercentage()));
                        newDrops.add(newItemStack);
                    }
                }
            }
        }

        Location location = event.getEntity().getLocation();
        for (ItemStack itemstack : newDrops) {
            location.getWorld().dropItemNaturally(location, itemstack);
        }
    }

}