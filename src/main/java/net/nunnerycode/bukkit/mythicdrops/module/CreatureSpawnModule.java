package net.nunnerycode.bukkit.mythicdrops.module;

import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.module.Module;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;

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

}
