package net.nunnerycode.bukkit.mythicdrops.spawning;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.names.NameType;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.names.NameMap;
import net.nunnerycode.bukkit.mythicdrops.tiers.TierMap;
import net.nunnerycode.bukkit.mythicdrops.utils.TierUtil;

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
        && mythicDrops.getCreatureSpawningSettings().isPreventSpawner()) {
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
    if (!mythicDrops.getConfigSettings().isDisplayMobEquipment()) {
      return;
    }
    // TODO: calculate if mob gets a drop
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

    // TODO: determine if mobs drop items
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

    // TODO: determine if mobs drop items
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

}
