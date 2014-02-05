package net.nunnerycode.bukkit.mythicdrops.spawning;

import com.google.common.base.Joiner;

import net.nunnerycode.bukkit.libraries.ivory.factories.FancyMessageFactory;
import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.names.NameType;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.events.EntityDyingEvent;
import net.nunnerycode.bukkit.mythicdrops.items.CustomItemMap;
import net.nunnerycode.bukkit.mythicdrops.items.MythicDropBuilder;
import net.nunnerycode.bukkit.mythicdrops.names.NameMap;
import net.nunnerycode.bukkit.mythicdrops.tiers.TierMap;
import net.nunnerycode.bukkit.mythicdrops.utils.CustomItemUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.EntityUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.ItemStackUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.TierUtil;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
import java.util.List;

import mkremins.fanciful.IFancyMessage;

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
    if (mythicDrops.getCreatureSpawningSettings().isGiveAllMobsNames()) {
      nameMobs(event.getEntity());
    }
    if (mythicDrops.getCreatureSpawningSettings().isBlankMobSpawnEnabled()) {
      event.getEntity().getEquipment().clear();
      if (event.getEntity() instanceof Skeleton && mythicDrops.getCreatureSpawningSettings()
          .isBlankMobSpawnSkeletonsSpawnWithBows()) {
        event.getEntity().getEquipment().setItemInHand(new ItemStack(Material.BOW, 1));
      }
    }
    if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER
        && mythicDrops.getCreatureSpawningSettings().isPreventSpawner()) {
      event.getEntity()
          .setCanPickupItems(mythicDrops.getCreatureSpawningSettings().isCanMobsPickUpEquipment());
      return;
    }
    if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG
        && mythicDrops.getCreatureSpawningSettings().isPreventSpawner()) {
      event.getEntity()
          .setCanPickupItems(mythicDrops.getCreatureSpawningSettings().isCanMobsPickUpEquipment());
      return;
    }
    if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM &&
        mythicDrops.getCreatureSpawningSettings().isPreventCustom()) {
      event.getEntity()
          .setCanPickupItems(mythicDrops.getCreatureSpawningSettings().isCanMobsPickUpEquipment());
      return;
    }
    if (event.getEntity().getLocation().getY() > mythicDrops.getCreatureSpawningSettings()
        .getSpawnHeightLimit(event.getEntity
            ().getWorld().getName())) {
      event.getEntity()
          .setCanPickupItems(mythicDrops.getCreatureSpawningSettings().isCanMobsPickUpEquipment());
      return;
    }
    event.getEntity()
        .setCanPickupItems(mythicDrops.getCreatureSpawningSettings().isCanMobsPickUpEquipment());
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
        && mythicDrops.getCreatureSpawningSettings().isPreventSpawner()) {
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
    if (!mythicDrops.getCreatureSpawningSettings().isGiveMobsEquipment()) {
      return;
    }
    boolean giveName = false;
    double chance = mythicDrops.getCreatureSpawningSettings().getGlobalSpawnChance() * mythicDrops
        .getCreatureSpawningSettings().getEntityTypeChanceToSpawn(event.getEntityType());
    if (mythicDrops.getCreatureSpawningSettings().isOnlyCustomItemsSpawn()) {
      if (mythicDrops.getCreatureSpawningSettings().isCustomItemsSpawn()
          && RandomUtils.nextDouble() < mythicDrops.getCreatureSpawningSettings().
          getCustomItemSpawnChance() && !CustomItemMap.getInstance().isEmpty()) {

        for (int i = 0; i < 5; i++) {
          if (RandomUtils.nextDouble() < chance) {
            EntityUtil
                .equipEntity(event.getEntity(), CustomItemMap.getInstance().getRandomWithChance()
                    .toItemStack());
            chance *= 0.5;
            giveName = true;
            continue;
          }
          break;
        }
      }
      if (giveName) {
        nameMobs(event.getEntity());
      }
      return;
    }

    double distanceFromWorldSpawn = event.getEntity().getLocation().distanceSquared(event
                                                                                        .getEntity()
                                                                                        .getWorld()
                                                                                        .getSpawnLocation());

    if (mythicDrops.getCreatureSpawningSettings().getEntityTypeChanceToSpawn(event.getEntityType())
        <= 0 &&
        mythicDrops.getCreatureSpawningSettings().getEntityTypeTiers(event.getEntityType())
            .isEmpty()) {
      return;
    }
    for (int i = 0; i < 5; i++) {
      if (RandomUtils.nextDouble() < chance) {
        Tier tier = getTier("*", event.getEntity());
        if (tier == null) {
          continue;
        }
        int attempts = 0;
        while (tier.getReplaceWith() != null && attempts < 20) {
          if (Math.pow(tier.getReplaceDistance(), 2) <= distanceFromWorldSpawn) {
            tier = tier.getReplaceWith();
            attempts++;
          } else {
            break;
          }
        }
        try {
          ItemStack itemStack = new MythicDropBuilder().inWorld(event.getEntity().getWorld())
              .useDurability(true).withTier(tier)
              .withItemGenerationReason(ItemGenerationReason.MONSTER_SPAWN)
              .build();
          EntityUtil.equipEntity(event.getEntity(), itemStack);
        } catch (Exception e) {
          continue;
        }
        giveName = true;
        chance *= 0.5;
        continue;
      }
      break;
    }
    if (mythicDrops.getCreatureSpawningSettings().isCustomItemsSpawn()
        && RandomUtils.nextDouble() < mythicDrops
        .getCreatureSpawningSettings().getCustomItemSpawnChance() && !CustomItemMap.getInstance()
        .isEmpty()) {
      for (int i = 0; i < 5; i++) {
        if (RandomUtils.nextDouble() < chance) {
          EntityUtil.equipEntity(event.getEntity(),
                                 CustomItemMap.getInstance().getRandomWithChance()
                                     .toItemStack());
          chance *= 0.5;
          giveName = true;
          continue;
        }
        break;
      }
    }

    if (giveName) {
      nameMobs(event.getEntity());
    }
  }

  private Tier getTier(String tierName, LivingEntity livingEntity) {
    Tier tier;
    if (tierName.equals("*")) {
      tier =
          TierUtil.randomTierWithChance(mythicDrops.getCreatureSpawningSettings().getEntityTypeTiers
              (livingEntity.getType()));
      if (tier == null) {
        tier =
            TierUtil
                .randomTierWithChance(mythicDrops.getCreatureSpawningSettings().getEntityTypeTiers
                    (livingEntity.getType()));
      }
    } else {
      tier = TierMap.getInstance().get(tierName.toLowerCase());
      if (tier == null) {
        tier = TierMap.getInstance().get(tierName);
      }
    }
    return tier;
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

    if (mythicDrops.getCreatureSpawningSettings().isGiveMobsEquipment()) {
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
      if (is == null || is.getType() == Material.AIR) {
        continue;
      }
      if (!is.hasItemMeta()) {
        continue;
      }
      CustomItem ci;
      try {
        ci = CustomItemUtil.getCustomItemFromItemStack(is);
      } catch (NullPointerException e) {
        ci = null;
      }
      if (ci != null) {
        if (RandomUtils.nextDouble() < ci.getChanceToDropOnDeath()) {
          ItemStack cis = ci.toItemStack();
          newDrops.add(cis);
          if (ci.isBroadcastOnFind() && event.getEntity().getKiller() != null) {
            String locale = mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                       ".found-item-broadcast",
                                                                                       new String[][]{
                                                                                           {"%receiver%",
                                                                                            event
                                                                                                .getEntity()
                                                                                                .getKiller()
                                                                                                .getName()}});
            String[] messages = locale.split("%item%");
            IFancyMessage fancyMessage = FancyMessageFactory.getInstance().getNewFancyMessage();
            for (int i1 = 0; i1 < messages.length; i1++) {
              String key = messages[i1];
              if (i1 < messages.length - 1) {
                fancyMessage.then(key).then(cis.getItemMeta().getDisplayName())
                    .itemTooltip(cis);
              } else {
                fancyMessage.then(key);
              }
            }
            for (Player player : event.getEntity().getWorld().getPlayers()) {
              fancyMessage.send(player);
            }
          }
          continue;
        }
      }

      Tier tier = TierUtil.getTierFromItemStack(is, TierMap.getInstance().values());

      String displayName =
          WordUtils.capitalizeFully(Joiner.on(" ").join(is.getType().name().split("_")));
      if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
        displayName = is.getItemMeta().getDisplayName();
      }

      if (tier != null && tier.isBroadcastOnFind() && event.getEntity().getKiller() != null) {
        String locale = mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                   ".found-item-broadcast",
                                                                                   new String[][]{
                                                                                       {"%receiver%",
                                                                                        event
                                                                                            .getEntity()
                                                                                            .getKiller()
                                                                                            .getName()}});
        String[] messages = locale.split("%item%");
        IFancyMessage fancyMessage = FancyMessageFactory.getInstance().getNewFancyMessage();
        for (int i1 = 0; i1 < messages.length; i1++) {
          String key = messages[i1];
          if (i1 < messages.length - 1) {
            fancyMessage.then(key).then(displayName).itemTooltip(is);
          } else {
            fancyMessage.then(key);
          }
        }
        for (Player player : event.getEntity().getWorld().getPlayers()) {
          fancyMessage.send(player);
        }
      }

      double dropChance = getTierDropChance(tier, event.getEntity().getWorld().getName());

      if (RandomUtils.nextDouble() < dropChance && tier != null) {
        ItemStack newItemStack = is.getData().toItemStack(is.getAmount());
        newItemStack.setItemMeta(is.getItemMeta().clone());
        newItemStack.setDurability(ItemStackUtil.getDurabilityForMaterial(is.getType(),
                                                                          tier.getMinimumDurabilityPercentage(),
                                                                          tier.getMaximumDurabilityPercentage()));
        newDrops.add(newItemStack);
      }

      EntityDyingEvent ede = new EntityDyingEvent(event.getEntity(), array, newDrops);
      Bukkit.getPluginManager().callEvent(ede);

      Location location = event.getEntity().getEyeLocation();

      for (ItemStack itemstack : ede.getEquipmentDrops()) {
        if (itemstack.getType() == Material.AIR) {
          continue;
        }
        location.getWorld().dropItemNaturally(location, itemstack);
      }
    }
  }

  private double getTierDropChance(Tier t, String worldName) {
    if (t == null || worldName == null) {
      return 0.0;
    }
    if (t.getWorldDropChanceMap().containsKey(worldName)) {
      return t.getWorldDropChanceMap().get(worldName);
    }
    if (t.getWorldDropChanceMap().containsKey("default")) {
      return t.getWorldDropChanceMap().get("default");
    }
    return 1.0;
  }

  private void handleEntityDyingWithoutGive(EntityDeathEvent event) {
    List<ItemStack> newDrops = new ArrayList<>();
    ItemStack[] array = new ItemStack[5];
    System.arraycopy(event.getEntity().getEquipment().getArmorContents(), 0, array, 0, 4);
    array[4] = event.getEntity().getEquipment().getItemInHand();

    event.getEntity().getEquipment().setBootsDropChance(0.0F);
    event.getEntity().getEquipment().setLeggingsDropChance(0.0F);
    event.getEntity().getEquipment().setChestplateDropChance(0.0F);
    event.getEntity().getEquipment().setHelmetDropChance(0.0F);
    event.getEntity().getEquipment().setItemInHandDropChance(0.0F);

    double
        chance =
        mythicDrops.getCreatureSpawningSettings().getGlobalSpawnChance() * mythicDrops
            .getCreatureSpawningSettings()
            .getEntityTypeChanceToSpawn(event.getEntityType());
    if (mythicDrops.getCreatureSpawningSettings().isOnlyCustomItemsSpawn()) {
      if (mythicDrops.getCreatureSpawningSettings().isCustomItemsSpawn()
          && RandomUtils.nextDouble() < mythicDrops
          .getCreatureSpawningSettings().getCustomItemSpawnChance() && !CustomItemMap.getInstance()
          .isEmpty()) {
        for (int i = 0; i < 5; i++) {
          if (RandomUtils.nextDouble() < chance) {
            newDrops.add(CustomItemMap.getInstance().getRandomWithChance().toItemStack());
            chance *= 0.5;
            continue;
          }
          break;
        }
      }
      return;
    }
    if (mythicDrops.getCreatureSpawningSettings().getEntityTypeChanceToSpawn(event.getEntityType())
        <= 0 ||
        mythicDrops.getCreatureSpawningSettings().getEntityTypeTiers(event.getEntityType())
            .isEmpty()) {
      return;
    }
    for (int i = 0; i < 5; i++) {
      if (RandomUtils.nextDouble() < chance) {
        Tier tier = getTier("*", event.getEntity());
        if (tier == null) {
          continue;
        }
        try {
          ItemStack
              itemStack =
              new MythicDropBuilder().inWorld(event.getEntity().getWorld()).useDurability(true).
                  withTier(tier).withItemGenerationReason(ItemGenerationReason.MONSTER_SPAWN)
                  .build();
          newDrops.add(itemStack);

          String
              displayName =
              WordUtils.capitalizeFully(Joiner.on(" ").join(itemStack.getType().name().split("_")));
          if (itemStack.hasItemMeta()) {
            if (itemStack.getItemMeta().hasDisplayName()) {
              displayName = itemStack.getItemMeta().getDisplayName();
            }
          }

          if (tier.isBroadcastOnFind() && event.getEntity().getKiller() != null) {
            String locale = mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                       ".found-item-broadcast",
                                                                                       new String[][]{
                                                                                           {"%receiver%",
                                                                                            event
                                                                                                .getEntity()
                                                                                                .getKiller()
                                                                                                .getName()}});
            String[] messages = locale.split("%item%");
            IFancyMessage fancyMessage = FancyMessageFactory.getInstance().getNewFancyMessage();
            for (int i1 = 0; i1 < messages.length; i1++) {
              String key = messages[i1];
              if (i1 < messages.length - 1) {
                fancyMessage.then(key).then(displayName)
                    .itemTooltip(itemStack);
              } else {
                fancyMessage.then(key);
              }
            }
            for (Player player : event.getEntity().getWorld().getPlayers()) {
              fancyMessage.send(player);
            }
          }
        } catch (Exception e) {
          continue;
        }
        chance *= 0.5;
        continue;
      }
      break;
    }
    if (mythicDrops.getCreatureSpawningSettings().isCustomItemsSpawn()
        && RandomUtils.nextDouble() < mythicDrops
        .getCreatureSpawningSettings().getCustomItemSpawnChance() && !CustomItemMap.getInstance()
        .isEmpty()) {
      for (int i = 0; i < 5; i++) {
        if (RandomUtils.nextDouble() < chance) {
          newDrops.add(CustomItemMap.getInstance().getRandomWithChance().toItemStack());
          chance *= 0.5;
          continue;
        }
        break;
      }
    }

    EntityDyingEvent ede = new EntityDyingEvent(event.getEntity(), array, newDrops);
    Bukkit.getPluginManager().callEvent(ede);

    Location location = event.getEntity().getLocation();

    for (ItemStack itemstack : ede.getEquipmentDrops()) {
      if (itemstack.getType() == Material.AIR) {
        continue;
      }
      location.getWorld().dropItemNaturally(location, itemstack);
    }
  }

  private void nameMobs(LivingEntity livingEntity) {
    if (mythicDrops.getCreatureSpawningSettings().isGiveMobsNames()) {
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

}
