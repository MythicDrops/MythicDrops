/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2019 Richard Harrah
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.spawning;

import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin;
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGenerationReason;
import com.tealcube.minecraft.bukkit.mythicdrops.api.names.NameType;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import com.tealcube.minecraft.bukkit.mythicdrops.events.CustomItemGenerationEvent;
import com.tealcube.minecraft.bukkit.mythicdrops.events.EntityNameEvent;
import com.tealcube.minecraft.bukkit.mythicdrops.events.EntitySpawningEvent;
import com.tealcube.minecraft.bukkit.mythicdrops.identification.IdentityTome;
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory;
import com.tealcube.minecraft.bukkit.mythicdrops.names.NameMap;
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.SocketItem;
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.TierMap;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.BroadcastMessageUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.CreatureSpawnEventUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.CustomItemUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.EntityUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemStackUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.SocketGemUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.StringUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TierUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.worldguard.WorldGuardFlagConstantsKt;
import com.tealcube.minecraft.bukkit.mythicdrops.worldguard.WorldGuardUtilWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public final class ItemSpawningListener implements Listener {

  private static final Logger LOGGER =
      JulLoggerFactory.INSTANCE.getLogger(ItemSpawningListener.class);
  private MythicDrops mythicDrops;

  public ItemSpawningListener(MythicDropsPlugin mythicDrops) {
    this.mythicDrops = mythicDrops;
  }

  public MythicDrops getMythicDrops() {
    return mythicDrops;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onCreatureSpawnEventLowest(CreatureSpawnEvent event) {
    if (shouldNotSpawn(event)) {
      return;
    }
    if (mythicDrops.getSettingsManager().getConfigSettings().getOptions().isGiveAllMobsNames()) {
      nameMobs(event.getEntity());
    }
    if (mythicDrops
        .getSettingsManager()
        .getConfigSettings()
        .getOptions()
        .getBlankMobSpawn()
        .isEnabled()) {
      event.getEntity().getEquipment().clear();
      if (event.getEntity() instanceof Skeleton
          && !mythicDrops
              .getSettingsManager()
              .getConfigSettings()
              .getOptions()
              .getBlankMobSpawn()
              .isSkeletonsSpawnWithoutBow()) {
        event.getEntity().getEquipment().setItemInMainHand(new ItemStack(Material.BOW, 1));
      }
    }
    event
        .getEntity()
        .setCanPickupItems(
            mythicDrops
                .getSettingsManager()
                .getConfigSettings()
                .getOptions()
                .isCanMobsPickUpEquipment());
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
    if (shouldNotSpawn(event)) {
      return;
    }
    if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.REINFORCEMENTS
        && mythicDrops
            .getSettingsManager()
            .getCreatureSpawningSettings()
            .getSpawnPrevention()
            .isReinforcements()) {
      return;
    }
    if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER
        && mythicDrops
            .getSettingsManager()
            .getCreatureSpawningSettings()
            .getSpawnPrevention()
            .isSpawner()) {
      return;
    }
    if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG
        && mythicDrops
            .getSettingsManager()
            .getCreatureSpawningSettings()
            .getSpawnPrevention()
            .isSpawnEgg()) {
      return;
    }
    if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM
        && mythicDrops
            .getSettingsManager()
            .getCreatureSpawningSettings()
            .getSpawnPrevention()
            .isSpawner()) {
      return;
    }
    if (mythicDrops
            .getSettingsManager()
            .getCreatureSpawningSettings()
            .getSpawnPrevention()
            .getAboveY()
            .getOrDefault(event.getEntity().getWorld().getName(), 255)
        <= event.getEntity().getLocation().getY()) {
      return;
    }
    if (!mythicDrops
        .getSettingsManager()
        .getConfigSettings()
        .getOptions()
        .isDisplayMobEquipment()) {
      LOGGER.fine("display mob equipment is off");
      return;
    }
    if (WorldGuardUtilWrapper.INSTANCE.isFlagDenyAtLocation(
        event.getLocation(), WorldGuardFlagConstantsKt.mythicDrops)) {
      LOGGER.fine("mythic-drops WorldGuard flag is set to DENY");
      return;
    }

    double itemChance =
        mythicDrops.getSettingsManager().getConfigSettings().getDrops().getItemChance();
    double creatureSpawningMultiplier =
        mythicDrops
            .getSettingsManager()
            .getCreatureSpawningSettings()
            .getDropMultipliers()
            .getOrDefault(event.getEntity().getType(), 0D);
    double itemChanceMultiplied = itemChance * creatureSpawningMultiplier;
    double itemRoll = RandomUtils.nextDouble(0D, 1D);

    if (itemRoll > itemChanceMultiplied) {
      LOGGER.fine(
          String.format(
              "onCreatureSpawnEvent - item (roll > (chance * multiplied)): %f > (%f * %f)",
              itemRoll, itemChance, creatureSpawningMultiplier));
      return;
    }

    double tieredItemChance =
        mythicDrops.getSettingsManager().getConfigSettings().getDrops().getTieredItemChance();
    double customItemChance =
        mythicDrops.getSettingsManager().getConfigSettings().getDrops().getCustomItemChance();
    double socketGemChance =
        mythicDrops.getSettingsManager().getConfigSettings().getDrops().getSocketGemChance();
    double unidentifiedItemChance =
        mythicDrops.getSettingsManager().getConfigSettings().getDrops().getUnidentifiedItemChance();
    double identityTomeChance =
        mythicDrops.getSettingsManager().getConfigSettings().getDrops().getIdentityTomeChance();
    boolean socketingEnabled =
        mythicDrops.getSettingsManager().getConfigSettings().getComponents().isSocketingEnabled();
    boolean identifyingEnabled =
        mythicDrops.getSettingsManager().getConfigSettings().getComponents().isIdentifyingEnabled();

    // Create the item for the mob.
    ItemStack itemStack = null;

    double tieredItemRoll = RandomUtils.nextDouble(0D, 1D);
    double customItemRoll = RandomUtils.nextDouble(0D, 1D);
    double socketGemRoll = RandomUtils.nextDouble(0D, 1D);
    double unidentifiedItemRoll = RandomUtils.nextDouble(0D, 1D);
    double identityTomeRoll = RandomUtils.nextDouble(0D, 1D);

    Tier tier = null;

    // This is here to maintain previous behavior
    if (tieredItemRoll <= (tieredItemChance * creatureSpawningMultiplier)
        && WorldGuardUtilWrapper.INSTANCE.isFlagAllowAtLocation(
            event.getLocation(), WorldGuardFlagConstantsKt.mythicDropsTiered)) {
      tier = getTierForEntity(event.getEntity());
      if (tier != null) {
        itemStack =
            MythicDropsPlugin.getNewDropBuilder()
                .withItemGenerationReason(ItemGenerationReason.MONSTER_SPAWN)
                .useDurability(false)
                .withTier(tier)
                .build();
      } else {
        LOGGER.fine("tier is null for type: " + event.getEntity().getType());
      }
    } else if (customItemRoll <= customItemChance
        && WorldGuardUtilWrapper.INSTANCE.isFlagAllowAtLocation(
            event.getLocation(), WorldGuardFlagConstantsKt.mythicDropsCustom)) {
      LOGGER.fine("onCreatureSpawnEvent - customItemRoll <= customItemChance");
      CustomItem customItem = mythicDrops.getCustomItemManager().randomByWeight();
      if (customItem != null) {
        LOGGER.fine(
            String.format(
                "onCreatureSpawnEvent - customItem != null: customItem.getName()=\"%s\"",
                customItem.getName()));
        CustomItemGenerationEvent customItemGenerationEvent =
            new CustomItemGenerationEvent(customItem, customItem.toItemStack());
        Bukkit.getPluginManager().callEvent(customItemGenerationEvent);
        if (!customItemGenerationEvent.isCancelled()) {
          itemStack = customItemGenerationEvent.getResult();
        }
      }
    } else if (socketingEnabled
        && socketGemRoll <= socketGemChance
        && WorldGuardUtilWrapper.INSTANCE.isFlagAllowAtLocation(
            event.getLocation(), WorldGuardFlagConstantsKt.mythicDropsSocketGem)) {
      SocketGem socketGem = SocketGemUtil.getRandomSocketGemWithChance(event.getEntity().getType());
      Material material = SocketGemUtil.getRandomSocketGemMaterial();
      if (socketGem != null && material != null) {
        itemStack =
            new SocketItem(
                material,
                socketGem,
                mythicDrops.getSettingsManager().getSocketingSettings().getItems().getSocketGem());
      }
    } else if (identifyingEnabled
        && unidentifiedItemRoll <= unidentifiedItemChance
        && WorldGuardUtilWrapper.INSTANCE.isFlagAllowAtLocation(
            event.getLocation(), WorldGuardFlagConstantsKt.mythicDropsUnidentifiedItem)) {
      Tier randomizedTierWithIdentityChance = TierMap.INSTANCE.getRandomTierWithIdentifyChance();
      if (randomizedTierWithIdentityChance != null) {
        Material material =
            ItemUtil.getRandomMaterialFromCollection(
                ItemUtil.getMaterialsFromTier(randomizedTierWithIdentityChance));
        if (material != null) {
          // TODO: FIX UNIDENTIFIED ITEM
          //          itemStack = new UnidentifiedItem(material,
          // mythicDrops.getIdentifyingSettings());
        }
      }
    } else if (identifyingEnabled
        && identityTomeRoll <= identityTomeChance
        && WorldGuardUtilWrapper.INSTANCE.isFlagAllowAtLocation(
            event.getLocation(), WorldGuardFlagConstantsKt.mythicDropsIdentityTome)) {
      itemStack =
          new IdentityTome(
              mythicDrops
                  .getSettingsManager()
                  .getIdentifyingSettings()
                  .getItems()
                  .getIdentityTome());
    }

    EntitySpawningEvent ese = new EntitySpawningEvent(event.getEntity());
    Bukkit.getPluginManager().callEvent(ese);

    EntityUtil.equipEntity(event.getEntity(), itemStack);

    if (itemStack != null) {
      nameMobs(event.getEntity(), tier);
    }
  }

  private Tier getTierForEntity(Entity entity) {
    Collection<Tier> allowableTiers =
        mythicDrops.getSettingsManager().getCreatureSpawningSettings().getTierDrops()
            .getOrDefault(entity.getType(), new ArrayList<>()).stream()
            .map(TierUtil::getTier)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    Map<Tier, Double> chanceMap = new HashMap<>();
    int distFromSpawn =
        (int) entity.getLocation().distanceSquared(entity.getWorld().getSpawnLocation());
    LOGGER.fine("distFromSpawn=" + distFromSpawn);
    for (Tier t : allowableTiers) {
      if (t.getMaximumDistance() == -1 || t.getOptimalDistance() == -1) {
        LOGGER.fine(
            "tier does not have both maximumDistance and optimalDistance: tier=" + t.getName());
        chanceMap.put(t, t.getSpawnChance());
        continue;
      }
      LOGGER.fine(
          String.format(
              "tier has both maximumDistance and optimalDistance: tier=%s maximumDistance=%d optimalDistance=%d",
              t.getName(), t.getMaximumDistance(), t.getOptimalDistance()));
      int squareMaxDist = (int) Math.pow(t.getMaximumDistance(), 2);
      int squareOptDist = (int) Math.pow(t.getOptimalDistance(), 2);
      int minDistFromSpawn = squareOptDist - squareMaxDist;
      int maxDistFromSpawn = squareOptDist + squareMaxDist;
      LOGGER.fine(
          String.format(
              "tier can spawn if distFromSpawn is between: tier=%s minDistFromSpawn=%d maxDistFromSpawn=%d",
              distFromSpawn, minDistFromSpawn, maxDistFromSpawn));
      if (distFromSpawn > maxDistFromSpawn || distFromSpawn < minDistFromSpawn) {
        LOGGER.fine(
            "distFromSpawn > maxDistFromSpawn || distFromSpawn < minDistFromSpawn: tier="
                + t.getName());
        chanceMap.put(t, 0D);
        continue;
      }
      LOGGER.fine("tier can spawn: tier=" + t.getName());
      chanceMap.put(t, t.getSpawnChance());
    }
    return TierUtil.randomTierWithChance(chanceMap);
  }

  @EventHandler
  public void onEntityDeath(EntityDeathEvent event) {
    if (event.getEntity() instanceof Player
        || event.getEntity().getLastDamageCause() == null
        || event.getEntity().getLastDamageCause().isCancelled()) {
      return;
    }
    if (!mythicDrops
        .getSettingsManager()
        .getConfigSettings()
        .getMultiworld()
        .getEnabledWorlds()
        .contains(event.getEntity().getWorld().getName())) {
      return;
    }

    if (event.getEntity().getKiller() == null) {
      return;
    }

    if (mythicDrops.getSettingsManager().getConfigSettings().getOptions().isDisplayMobEquipment()) {
      handleEntityDyingWithGive(event);
    } else {
      handleEntityDyingWithoutGive(event);
    }
  }

  private void handleEntityDyingWithoutGive(EntityDeathEvent event) {
    double itemChance =
        mythicDrops.getSettingsManager().getConfigSettings().getDrops().getItemChance();
    double creatureSpawningMultiplier =
        mythicDrops
            .getSettingsManager()
            .getCreatureSpawningSettings()
            .getDropMultipliers()
            .getOrDefault(event.getEntity().getType(), 0D);
    double itemChanceMultiplied = itemChance * creatureSpawningMultiplier;
    double itemRoll = RandomUtils.nextDouble(0D, 1D);

    if (WorldGuardUtilWrapper.INSTANCE.isFlagDenyAtLocation(
        event.getEntity().getLocation(), WorldGuardFlagConstantsKt.mythicDrops)) {
      LOGGER.fine("mythic-drops WorldGuard flag is DENY");
      return;
    }

    LOGGER.fine(
        String.format(
            "onCreatureSpawnEvent - item (roll <= chance): %f <= %f",
            itemRoll, itemChanceMultiplied));

    if (itemRoll > itemChanceMultiplied) {
      LOGGER.fine("roll is higher than chance, not spawning an item");
      return;
    }

    double tieredItemChance =
        mythicDrops.getSettingsManager().getConfigSettings().getDrops().getTieredItemChance();
    double customItemChance =
        mythicDrops.getSettingsManager().getConfigSettings().getDrops().getCustomItemChance();
    double socketGemChance =
        mythicDrops.getSettingsManager().getConfigSettings().getDrops().getSocketGemChance();
    double unidentifiedItemChance =
        mythicDrops.getSettingsManager().getConfigSettings().getDrops().getUnidentifiedItemChance();
    double identityTomeChance =
        mythicDrops.getSettingsManager().getConfigSettings().getDrops().getIdentityTomeChance();
    boolean socketingEnabled =
        mythicDrops.getSettingsManager().getConfigSettings().getComponents().isSocketingEnabled();
    boolean identifyingEnabled =
        mythicDrops.getSettingsManager().getConfigSettings().getComponents().isIdentifyingEnabled();

    // Create the item for the mob.
    ItemStack itemStack = null;

    double tieredItemRoll = RandomUtils.nextDouble(0D, 1D);
    double customItemRoll = RandomUtils.nextDouble(0D, 1D);
    double socketGemRoll = RandomUtils.nextDouble(0D, 1D);
    double unidentifiedItemRoll = RandomUtils.nextDouble(0D, 1D);
    double identityTomeRoll = RandomUtils.nextDouble(0D, 1D);

    // This is here to maintain previous behavior
    if (tieredItemRoll <= (tieredItemChance * creatureSpawningMultiplier)
        && WorldGuardUtilWrapper.INSTANCE.isFlagAllowAtLocation(
            event.getEntity().getLocation(), WorldGuardFlagConstantsKt.mythicDropsTiered)) {
      Tier tier = getTierForEntity(event.getEntity());
      if (tier != null) {
        itemStack =
            MythicDropsPlugin.getNewDropBuilder()
                .withItemGenerationReason(ItemGenerationReason.MONSTER_SPAWN)
                .useDurability(false)
                .withTier(tier)
                .build();
        if (tier.isBroadcastOnFind()) {
          BroadcastMessageUtil.INSTANCE.broadcastItem(
              mythicDrops.getSettingsManager().getLanguageSettings(),
              event.getEntity().getKiller(),
              itemStack);
        }
      } else {
        LOGGER.fine("tier is null for type: " + event.getEntity().getType());
      }
    } else if (customItemRoll <= customItemChance
        && WorldGuardUtilWrapper.INSTANCE.isFlagAllowAtLocation(
            event.getEntity().getLocation(), WorldGuardFlagConstantsKt.mythicDropsCustom)) {
      CustomItem ci = mythicDrops.getCustomItemManager().randomByWeight();
      if (ci != null) {
        CustomItemGenerationEvent customItemGenerationEvent =
            new CustomItemGenerationEvent(ci, ci.toItemStack());
        Bukkit.getPluginManager().callEvent(customItemGenerationEvent);
        if (!customItemGenerationEvent.isCancelled()) {
          itemStack = customItemGenerationEvent.getResult();
          if (ci.isBroadcastOnFind()) {
            BroadcastMessageUtil.INSTANCE.broadcastItem(
                mythicDrops.getSettingsManager().getLanguageSettings(),
                event.getEntity().getKiller(),
                itemStack);
          }
        }
      }
    } else if (socketingEnabled
        && socketGemRoll <= socketGemChance
        && WorldGuardUtilWrapper.INSTANCE.isFlagAllowAtLocation(
            event.getEntity().getLocation(), WorldGuardFlagConstantsKt.mythicDropsSocketGem)) {
      SocketGem socketGem = SocketGemUtil.getRandomSocketGemWithChance(event.getEntity().getType());
      Material material = SocketGemUtil.getRandomSocketGemMaterial();
      if (socketGem != null && material != null) {
        itemStack =
            new SocketItem(
                material,
                socketGem,
                mythicDrops.getSettingsManager().getSocketingSettings().getItems().getSocketGem());
      }
    } else if (identifyingEnabled
        && unidentifiedItemRoll <= unidentifiedItemChance
        && WorldGuardUtilWrapper.INSTANCE.isFlagAllowAtLocation(
            event.getEntity().getLocation(),
            WorldGuardFlagConstantsKt.mythicDropsUnidentifiedItem)) {
      Tier randomizedTierWithIdentityChance = TierMap.INSTANCE.getRandomTierWithIdentifyChance();
      if (randomizedTierWithIdentityChance != null) {
        Material material =
            ItemUtil.getRandomMaterialFromCollection(
                ItemUtil.getMaterialsFromTier(randomizedTierWithIdentityChance));
        // TODO: FIX UNIDENTIFIED ITEM
        //        itemStack = new UnidentifiedItem(material, mythicDrops.getIdentifyingSettings());
      }
    } else if (identifyingEnabled
        && identityTomeRoll <= identityTomeChance
        && WorldGuardUtilWrapper.INSTANCE.isFlagAllowAtLocation(
            event.getEntity().getLocation(), WorldGuardFlagConstantsKt.mythicDropsIdentityTome)) {
      itemStack =
          new IdentityTome(
              mythicDrops
                  .getSettingsManager()
                  .getIdentifyingSettings()
                  .getItems()
                  .getIdentityTome());
    }

    setEntityEquipmentDropChances(event);

    if (itemStack != null) {
      World w = event.getEntity().getWorld();
      Location l = event.getEntity().getLocation();
      w.dropItemNaturally(l, itemStack);
    }
  }

  private void setEntityEquipmentDropChances(EntityDeathEvent event) {
    event.getEntity().getEquipment().setBootsDropChance(0.0F);
    event.getEntity().getEquipment().setLeggingsDropChance(0.0F);
    event.getEntity().getEquipment().setChestplateDropChance(0.0F);
    event.getEntity().getEquipment().setHelmetDropChance(0.0F);
    event.getEntity().getEquipment().setItemInMainHandDropChance(0.0F);
    event.getEntity().getEquipment().setItemInOffHandDropChance(0.0F);
  }

  private void handleEntityDyingWithGive(EntityDeathEvent event) {
    List<ItemStack> newDrops = new ArrayList<>();

    ItemStack[] array = new ItemStack[6];
    System.arraycopy(event.getEntity().getEquipment().getArmorContents(), 0, array, 0, 4);
    array[4] = event.getEntity().getEquipment().getItemInMainHand();
    array[5] = event.getEntity().getEquipment().getItemInOffHand();

    setEntityEquipmentDropChances(event);

    for (ItemStack is : array) {
      if (is == null || is.getType() == Material.AIR || !is.hasItemMeta()) {
        LOGGER.finest("handleEntityDyingWithGive - !is.hasItemMeta()");
        continue;
      }
      CustomItem ci = CustomItemUtil.INSTANCE.getCustomItemFromItemStack(is);
      if (ci != null) {
        newDrops.add(ci.toItemStack());
        if (ci.isBroadcastOnFind() && event.getEntity().getKiller() != null) {
          BroadcastMessageUtil.INSTANCE.broadcastItem(
              mythicDrops.getSettingsManager().getLanguageSettings(),
              event.getEntity().getKiller(),
              ci.toItemStack());
        }
        continue;
      }
      SocketGem socketGem = SocketGemUtil.getSocketGemFromItemStack(is);
      if (socketGem != null) {
        newDrops.add(
            new SocketItem(
                is.getType(),
                socketGem,
                mythicDrops.getSettingsManager().getSocketingSettings().getItems().getSocketGem()));
        continue;
      }
      IdentityTome identityTome =
          new IdentityTome(
              mythicDrops
                  .getSettingsManager()
                  .getIdentifyingSettings()
                  .getItems()
                  .getIdentityTome());
      if (is.isSimilar(identityTome)) {
        newDrops.add(identityTome);
        continue;
      }
      // TODO: FIX UNIDENTIFIED ITEM
      //      UnidentifiedItem unidentifiedItem =
      //          new UnidentifiedItem(is.getType(), mythicDrops.getIdentifyingSettings());
      //      if (is.isSimilar(unidentifiedItem)) {
      //        newDrops.add(unidentifiedItem);
      //        continue;
      //      }
      Tier t = TierUtil.getTierFromItemStack(is);
      LOGGER.finest(
          String.format(
              "handleEntityDyingWithGive - is.displayName: %s",
              is.getItemMeta().hasDisplayName()
                  ? StringUtil.decolorString(is.getItemMeta().getDisplayName())
                  : ""));
      LOGGER.finest(
          String.format("handleEntityDyingWithGive - tier: %s", t != null ? t.toString() : ""));
      if (t != null && RandomUtils.nextDouble(0D, 1D) < t.getDropChance()) {
        ItemStack nis = is.getData().toItemStack(1);
        nis.setItemMeta(is.getItemMeta());
        ItemStack nisd =
            ItemStackUtil.setDurabilityForItemStack(
                nis, t.getMinimumDurabilityPercentage(), t.getMaximumDurabilityPercentage());
        if (t.isBroadcastOnFind()) {
          if (event.getEntity().getKiller() != null) {
            BroadcastMessageUtil.INSTANCE.broadcastItem(
                mythicDrops.getSettingsManager().getLanguageSettings(),
                event.getEntity().getKiller(),
                nisd);
          }
        }
        newDrops.add(nisd);
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

  private boolean shouldNotSpawn(CreatureSpawnEvent event) {
    if (CreatureSpawnEventUtil.INSTANCE.shouldCancelDropsBasedOnCreatureSpawnEvent(event)) {
      return true;
    }
    if (!mythicDrops
        .getSettingsManager()
        .getConfigSettings()
        .getMultiworld()
        .getEnabledWorlds()
        .contains(event.getEntity().getWorld().getName())) {
      LOGGER.fine("cancelling item spawn because of multiworld support");
      return true;
    }
    return false;
  }

  private void nameMobs(LivingEntity livingEntity) {
    nameMobs(livingEntity, null);
  }

  private void nameMobs(LivingEntity livingEntity, Tier tier) {
    if (!mythicDrops.getSettingsManager().getConfigSettings().getOptions().isGiveMobsNames()) {
      return;
    }
    String generalName = NameMap.getInstance().getRandom(NameType.GENERAL_MOB_NAME, "");
    String specificName =
        NameMap.getInstance()
            .getRandom(
                NameType.SPECIFIC_MOB_NAME, "." + livingEntity.getType().name().toLowerCase());
    String name;
    if (specificName != null && !specificName.isEmpty()) {
      name = specificName;
    } else {
      name = generalName;
    }
    ChatColor displayColor = ChatColor.WHITE;
    if (tier != null
        && mythicDrops
            .getSettingsManager()
            .getConfigSettings()
            .getOptions()
            .isGiveMobsColoredNames()) {
      displayColor = tier.getDisplayColor();
    }

    EntityNameEvent event = new EntityNameEvent(livingEntity, displayColor + name);
    Bukkit.getPluginManager().callEvent(event);
    if (event.isCancelled()) {
      return;
    }

    livingEntity.setCustomName(event.getName());
    livingEntity.setCustomNameVisible(true);
  }
}
