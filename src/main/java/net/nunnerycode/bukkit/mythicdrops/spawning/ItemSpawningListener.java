package net.nunnerycode.bukkit.mythicdrops.spawning;

import mkremins.fanciful.IFancyMessage;
import net.nunnerycode.bukkit.libraries.ivory.factories.FancyMessageFactory;
import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.distancezones.DistanceZone;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.names.NameType;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.events.EntitySpawningEvent;
import net.nunnerycode.bukkit.mythicdrops.identification.IdentityTome;
import net.nunnerycode.bukkit.mythicdrops.identification.UnidentifiedItem;
import net.nunnerycode.bukkit.mythicdrops.items.CustomItemMap;
import net.nunnerycode.bukkit.mythicdrops.names.NameMap;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketGem;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketItem;
import net.nunnerycode.bukkit.mythicdrops.utils.CustomItemUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.DistanceZoneUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.EntityUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.ItemStackUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.SocketGemUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.TierUtil;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ItemSpawningListener implements Listener {

    private MythicDrops mythicDrops;

    public ItemSpawningListener(MythicDropsPlugin mythicDrops) {
        this.mythicDrops = mythicDrops;
    }

    public MythicDrops getMythicDrops() {
        return mythicDrops;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCreatureSpawnEventLowest(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof Monster) || event.isCancelled()) {
            return;
        }
        if (!mythicDrops.getConfigSettings().getEnabledWorlds().contains(event.getEntity().getWorld()
                .getName())) {
            return;
        }
        if (mythicDrops.getConfigSettings().isGiveAllMobsNames()) {
            nameMobs(event.getEntity());
        }
        if (mythicDrops.getConfigSettings().isBlankMobSpawnEnabled()) {
            event.getEntity().getEquipment().clear();
            if (event.getEntity() instanceof Skeleton && !mythicDrops.getConfigSettings()
                    .isSkeletonsSpawnWithoutBows()) {
                event.getEntity().getEquipment().setItemInHand(new ItemStack(Material.BOW, 1));
            }
        }
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER
                && mythicDrops.getCreatureSpawningSettings().isPreventSpawner()) {
            event.getEntity()
                    .setCanPickupItems(mythicDrops.getConfigSettings().isMobsPickupEquipment());
            return;
        }
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG
                && mythicDrops.getCreatureSpawningSettings().isPreventSpawnEgg()) {
            event.getEntity()
                    .setCanPickupItems(mythicDrops.getConfigSettings().isMobsPickupEquipment());
            return;
        }
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM &&
                mythicDrops.getCreatureSpawningSettings().isPreventCustom()) {
            event.getEntity()
                    .setCanPickupItems(mythicDrops.getConfigSettings().isMobsPickupEquipment());
            return;
        }
        if (event.getEntity().getLocation().getY() > mythicDrops.getCreatureSpawningSettings()
                .getSpawnHeightLimit(event.getEntity
                        ().getWorld().getName())) {
            event.getEntity()
                    .setCanPickupItems(mythicDrops.getConfigSettings().isMobsPickupEquipment());
            return;
        }
        event.getEntity()
                .setCanPickupItems(mythicDrops.getConfigSettings().isMobsPickupEquipment());
    }

    private void nameMobs(LivingEntity livingEntity) {
        if (mythicDrops.getConfigSettings().isGiveMobsNames()) {
            String generalName = NameMap.getInstance().getRandom(NameType.MOB_NAME, "");
            String specificName = NameMap.getInstance().getRandom(NameType.MOB_NAME,
                    "." + livingEntity.getType());
            if (specificName != null && !specificName.isEmpty()) {
                livingEntity.setCustomName(specificName);
            } else {
                livingEntity.setCustomName(generalName);
            }
            livingEntity.setCustomNameVisible(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof Monster) || event.isCancelled()) {
            return;
        }
        if (!mythicDrops.getConfigSettings().getEnabledWorlds().contains(event.getEntity().getWorld()
                .getName())) {
            return;
        }
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER
                && mythicDrops.getCreatureSpawningSettings().isPreventSpawner()) {
            return;
        }
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG
                && mythicDrops.getCreatureSpawningSettings().isPreventSpawnEgg()) {
            return;
        }
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM &&
                mythicDrops.getCreatureSpawningSettings().isPreventSpawner()) {
            return;
        }
        if (mythicDrops.getCreatureSpawningSettings()
                .getSpawnHeightLimit(event.getEntity().getWorld().getName()) <= event
                .getEntity().getLocation().getY()) {
            return;
        }
        if (!mythicDrops.getConfigSettings().isDisplayMobEquipment()) {
            return;
        }

        // Start off with the random item chance. If the mob doesn't pass that, it gets no items.
        double chanceToGetDrop = mythicDrops.getConfigSettings().getItemChance() * mythicDrops
                .getCreatureSpawningSettings().getEntityTypeChanceToSpawn(event.getEntity().getType());
        if (RandomUtils.nextDouble() > chanceToGetDrop) {
            return;
        }

        // Choose a tier for the item that the mob is given. If the tier is null, it gets no items.
        Tier tier = getTierForEvent(event);
        if (tier == null) {
            return;
        }
        double d = event.getEntity().getLocation().distanceSquared(event.getEntity().getWorld().getSpawnLocation());
        int attempts = ChatColor.values().length * (ChatColor.values().length - 1);
        while (attempts > 0) {
            if (tier.getReplaceWith() == null) {
                break;
            }
            if (Math.pow(tier.getReplaceDistance(), 2) < d) {
                break;
            }
            tier = tier.getReplaceWith();
            attempts--;
        }

        // Create the item for the mob.
        ItemStack itemStack = MythicDropsPlugin.getNewDropBuilder().withItemGenerationReason(
                ItemGenerationReason.MONSTER_SPAWN).useDurability(false).withTier(tier).build();

        if (itemStack == null) {
            return;
        }

        // Begin to check for socket gem, identity tome, and unidentified.
        double customItemChance = mythicDrops.getConfigSettings().getCustomItemChance();
        double socketGemChance = mythicDrops.getConfigSettings().getSocketGemChance();
        double unidentifiedItemChance = mythicDrops.getConfigSettings().getUnidentifiedItemChance();
        double identityTomeChance = mythicDrops.getConfigSettings().getIdentityTomeChance();
        boolean sockettingEnabled = mythicDrops.getConfigSettings().isSockettingEnabled();
        boolean identifyingEnabled = mythicDrops.getConfigSettings().isIdentifyingEnabled();

        if (RandomUtils.nextDouble() <= customItemChance) {
            CustomItem customItem = CustomItemMap.getInstance().getRandomWithChance();
            if (customItem != null) {
                itemStack = customItem.toItemStack();
            }
        } else if (sockettingEnabled && RandomUtils.nextDouble() <= socketGemChance) {
            SocketGem socketGem = SocketGemUtil.getRandomSocketGemWithChance();
            Material material = SocketGemUtil.getRandomSocketGemMaterial();
            if (socketGem != null && material != null) {
                itemStack = new SocketItem(material, socketGem);
            }
        } else if (identifyingEnabled && RandomUtils.nextDouble() <= unidentifiedItemChance) {
            Material material = itemStack.getType();
            itemStack = new UnidentifiedItem(material);
        } else if (identifyingEnabled && RandomUtils.nextDouble() <= identityTomeChance) {
            itemStack = new IdentityTome();
        }

        EntitySpawningEvent ese = new EntitySpawningEvent(event.getEntity());
        Bukkit.getPluginManager().callEvent(ese);

        EntityUtil.equipEntity(event.getEntity(), itemStack);

        nameMobs(event.getEntity());
    }

    private Tier getTierForEvent(CreatureSpawnEvent event) {
        if (!mythicDrops.getConfigSettings().isDistanceZonesEnabled()) {
            Collection<Tier> allowableTiers = mythicDrops.getCreatureSpawningSettings()
                    .getEntityTypeTiers(event.getEntity().getType());
            return TierUtil.randomTierWithChance(allowableTiers);
        }
        DistanceZone dz = DistanceZoneUtil.getDistanceZone(event.getLocation());
        if (dz == null) {
            Collection<Tier> allowableTiers = mythicDrops.getCreatureSpawningSettings()
                    .getEntityTypeTiers(event.getEntity().getType());
            return TierUtil.randomTierWithChance(allowableTiers);
        }
        Map<Tier, Double> map = dz.getTierMap();
        Collection<Tier> allowableTiers = mythicDrops.getCreatureSpawningSettings()
                .getEntityTypeTiers(event.getEntity().getType());
        Set<Tier> set = new HashSet<>(map.keySet());
        for (Tier t : set) {
            if (!allowableTiers.contains(t)) {
                map.remove(t);
            }
        }
        return TierUtil.randomTierWithChance(map);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player || event.getEntity().getLastDamageCause() == null
                || event.getEntity().getLastDamageCause().isCancelled()) {
            return;
        }
        if (!mythicDrops.getConfigSettings().getEnabledWorlds().contains(event.getEntity().getWorld()
                .getName())) {
            return;
        }

        EntityDamageEvent.DamageCause damageCause = event.getEntity().getLastDamageCause().getCause();

        switch (damageCause) {
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

        if (mythicDrops.getConfigSettings().isDisplayMobEquipment()) {
            handleEntityDyingWithGive(event);
        } else {
            handleEntityDyingWithoutGive(event);
        }
    }

    private void handleEntityDyingWithGive(EntityDeathEvent event) {
        List<ItemStack> newDrops = new ArrayList<>();

        ItemStack[] array = new ItemStack[5];
        System.arraycopy(event.getEntity().getEquipment().getArmorContents(), 0, array, 0, 4);
        array[4] = event.getEntity().getEquipment().getItemInHand();

        event.getEntity().getEquipment().setBootsDropChance(0.0F);
        event.getEntity().getEquipment().setLeggingsDropChance(0.0F);
        event.getEntity().getEquipment().setChestplateDropChance(0.0F);
        event.getEntity().getEquipment().setHelmetDropChance(0.0F);
        event.getEntity().getEquipment().setItemInHandDropChance(0.0F);

        for (ItemStack is : array) {
            if (is == null || is.getType() == Material.AIR || !is.hasItemMeta()) {
                continue;
            }
            CustomItem ci = CustomItemUtil.getCustomItemFromItemStack(is);
            if (ci != null) {
                newDrops.add(ci.toItemStack());
                if (ci.isBroadcastOnFind() && event.getEntity().getKiller() != null) {
                    broadcastMessage(event.getEntity().getKiller(), ci.toItemStack());
                }
                continue;
            }
            SocketGem socketGem = SocketGemUtil.getSocketGemFromItemStack(is);
            if (socketGem != null) {
                newDrops.add(new SocketItem(is.getType(), socketGem));
                continue;
            }
            IdentityTome identityTome = new IdentityTome();
            if (is.isSimilar(identityTome)) {
                newDrops.add(identityTome);
                continue;
            }
            UnidentifiedItem unidentifiedItem = new UnidentifiedItem(is.getType());
            if (is.isSimilar(unidentifiedItem)) {
                newDrops.add(unidentifiedItem);
                continue;
            }
            Tier t = TierUtil.getTierFromItemStack(is);
            if (t != null && RandomUtils.nextDouble() < t.getDropChance()) {
                ItemStack nis = is.getData().toItemStack(1);
                nis.setItemMeta(is.getItemMeta());
                nis.setDurability(ItemStackUtil.getDurabilityForMaterial(is.getType(),
                        t.getMinimumDurabilityPercentage
                                (),
                        t.getMaximumDurabilityPercentage()
                ));
                if (t.isBroadcastOnFind()) {
                    broadcastMessage(event.getEntity().getKiller(), nis);
                }
                newDrops.add(nis);
            }
        }

        for (ItemStack itemStack : newDrops) {
            if (itemStack.getType() == Material.AIR) {
                continue;
            }
            World w = event.getEntity().getWorld();
            Location l = event.getEntity().getLocation();
            w.dropItemNaturally(l, itemStack);
        }
    }

    private void broadcastMessage(Player player, ItemStack itemStack) {
        String locale = mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                ".found-item-broadcast", new String[][]{{"%receiver%", player.getName()}});
        String[] messages = locale.split("%item%");
        IFancyMessage fancyMessage = FancyMessageFactory.getInstance().getNewFancyMessage();
        for (int i1 = 0; i1 < messages.length; i1++) {
            String key = messages[i1];
            if (i1 < messages.length - 1) {
                fancyMessage.then(key).then(itemStack.getItemMeta().getDisplayName()).itemTooltip(itemStack);
            } else {
                fancyMessage.then(key);
            }
        }
        for (Player p : player.getWorld().getPlayers()) {
            fancyMessage.send(p);
        }
    }

    private void handleEntityDyingWithoutGive(EntityDeathEvent event) {
        // Start off with the random item chance. If the mob doesn't pass that, it gets no items.
        double chanceToGetDrop = mythicDrops.getConfigSettings().getItemChance() * mythicDrops
                .getCreatureSpawningSettings().getEntityTypeChanceToSpawn(event.getEntity().getType());
        if (RandomUtils.nextDouble() > chanceToGetDrop) {
            return;
        }

        // Choose a tier for the item that the mob is given. If the tier is null, it gets no items.
        Collection<Tier> allowableTiers = mythicDrops.getCreatureSpawningSettings()
                .getEntityTypeTiers(event.getEntity().getType());
        Tier tier = TierUtil.randomTierWithChance(allowableTiers);
        if (tier == null) {
            return;
        }

        // Create the item for the mob.
        ItemStack itemStack = MythicDropsPlugin.getNewDropBuilder().withItemGenerationReason(
                ItemGenerationReason.MONSTER_SPAWN).useDurability(true).withTier(tier).build();

        // Begin to check for socket gem, identity tome, and unidentified.
        double customItemChance = mythicDrops.getConfigSettings().getCustomItemChance();
        double socketGemChance = mythicDrops.getConfigSettings().getSocketGemChance();
        double unidentifiedItemChance = mythicDrops.getConfigSettings().getUnidentifiedItemChance();
        double identityTomeChance = mythicDrops.getConfigSettings().getIdentityTomeChance();
        boolean sockettingEnabled = mythicDrops.getConfigSettings().isSockettingEnabled();
        boolean identifyingEnabled = mythicDrops.getConfigSettings().isIdentifyingEnabled();

        if (RandomUtils.nextDouble() <= customItemChance) {
            CustomItem ci = CustomItemMap.getInstance().getRandomWithChance();
            if (ci != null) {
                itemStack = ci.toItemStack();
                if (ci.isBroadcastOnFind()) {
                    broadcastMessage(event.getEntity().getKiller(), itemStack);
                }
            }
        } else if (sockettingEnabled && RandomUtils.nextDouble() <= socketGemChance) {
            SocketGem socketGem = SocketGemUtil.getRandomSocketGemWithChance();
            Material material = SocketGemUtil.getRandomSocketGemMaterial();
            if (socketGem != null && material != null) {
                itemStack = new SocketItem(material, socketGem);
            }
        } else if (identifyingEnabled && RandomUtils.nextDouble() <= unidentifiedItemChance) {
            Material material = itemStack.getType();
            itemStack = new UnidentifiedItem(material);
        } else if (identifyingEnabled && RandomUtils.nextDouble() <= identityTomeChance) {
            itemStack = new IdentityTome();
        } else if (tier.isBroadcastOnFind()) {
            broadcastMessage(event.getEntity().getKiller(), itemStack);
        }

        event.getEntity().getEquipment().setBootsDropChance(0.0F);
        event.getEntity().getEquipment().setLeggingsDropChance(0.0F);
        event.getEntity().getEquipment().setChestplateDropChance(0.0F);
        event.getEntity().getEquipment().setHelmetDropChance(0.0F);
        event.getEntity().getEquipment().setItemInHandDropChance(0.0F);

        World w = event.getEntity().getWorld();
        Location l = event.getEntity().getLocation();
        w.dropItemNaturally(l, itemStack);
    }

}
