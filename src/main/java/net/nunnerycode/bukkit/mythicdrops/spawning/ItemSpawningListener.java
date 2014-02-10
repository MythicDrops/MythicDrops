package net.nunnerycode.bukkit.mythicdrops.spawning;

import com.google.common.base.Joiner;

import net.nunnerycode.bukkit.libraries.ivory.factories.FancyMessageFactory;
import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.names.NameType;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.events.EntityDyingEvent;
import net.nunnerycode.bukkit.mythicdrops.names.NameMap;
import net.nunnerycode.bukkit.mythicdrops.tiers.TierMap;
import net.nunnerycode.bukkit.mythicdrops.utils.CustomItemUtil;
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

      double dropChance = tier != null ? tier.getDropChance() : 0.0;

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

      Location location = event.getEntity().getLocation();

      for (int i = 0; i < 5; i++) {
        if (i >= ede.getEquipmentDrops().size()) {
          break;
        }
        ItemStack itemstack = ede.getEquipmentDrops().get(i);
        if (itemstack.getType() == Material.AIR) {
          i--;
          continue;
        }
        itemstack.setAmount(1);
        location.getWorld().dropItemNaturally(location, itemstack);
      }
    }
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
