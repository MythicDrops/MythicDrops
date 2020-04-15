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
package com.tealcube.minecraft.bukkit.mythicdrops.repair;

import com.comphenix.xp.rewards.xp.ExperienceManager;
import com.tealcube.minecraft.bukkit.mythicdrops.InventoryExtensionsKt;
import com.tealcube.minecraft.bukkit.mythicdrops.StringExtensionsKt;
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairCost;
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairItem;
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairItemManager;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager;
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import kotlin.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class RepairingListener implements Listener {

  private static final Logger LOGGER = JulLoggerFactory.INSTANCE.getLogger(RepairingListener.class);

  private RepairItemManager repairItemManager;
  private SettingsManager settingsManager;
  private Map<String, ItemStack> repairing = new HashMap<>();

  public RepairingListener(RepairItemManager repairItemManager, SettingsManager settingsManager) {
    this.repairItemManager = repairItemManager;
    this.settingsManager = settingsManager;
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onBlockDamageEvent(BlockDamageEvent event) {
    if (event.isCancelled()) {
      return;
    }
    if (!settingsManager.getConfigSettings().getComponents().isRepairingEnabled()) {
      return;
    }
    if (event.getBlock().getType() != Material.ANVIL) {
      return;
    }
    if (!event.getPlayer().hasPermission("mythicdrops.repair")) {
      LOGGER.fine(event.getPlayer().getName() + " does not have permission to repair");
      return;
    }
    Player player = event.getPlayer();
    if (repairing.containsKey(player.getName())) {
      ItemStack oldInHand = repairing.get(player.getName());
      ItemStack currentInHand = player.getEquipment().getItemInMainHand();
      if (oldInHand.getType() != currentInHand.getType()) {
        LOGGER.fine("oldInHand.getType() != currentInHand.getType(): player=" + player.getName());
        player.sendMessage(
            StringExtensionsKt.chatColorize(
                settingsManager.getLanguageSettings().getRepairing().getCannotUse()));
        repairing.remove(player.getName());
        return;
      }
      if (oldInHand.getDurability() == 0 || currentInHand.getDurability() == 0) {
        LOGGER.fine("durability == 0: player=" + player.getName());
        player.sendMessage(
            StringExtensionsKt.chatColorize(
                settingsManager.getLanguageSettings().getRepairing().getCannotUse()));
        event.setCancelled(true);
        removeMapItem(player);
        return;
      }
      if (!oldInHand.isSimilar(currentInHand)) {
        LOGGER.fine("!oldInHand.isSimilar(currentInHand): player=" + player.getName());
        player.sendMessage(
            StringExtensionsKt.chatColorize(
                settingsManager.getLanguageSettings().getRepairing().getCannotUse()));
        event.setCancelled(true);
        removeMapItem(player);
        return;
      }
      RepairItem mythicRepairItem = getRepairItem(currentInHand);
      if (mythicRepairItem == null) {
        LOGGER.fine("mythicRepairItem == null: player=" + player.getName());
        player.sendMessage(
            StringExtensionsKt.chatColorize(
                settingsManager.getLanguageSettings().getRepairing().getCannotUse()));
        event.setCancelled(true);
        removeMapItem(player);
        return;
      }
      List<RepairCost> mythicRepairCostList = mythicRepairItem.getRepairCosts();
      RepairCost mythicRepairCost = getRepairCost(mythicRepairCostList, player.getInventory());
      if (mythicRepairCost == null) {
        LOGGER.fine("mythicRepairCost == null: player=" + player.getName());
        player.sendMessage(
            StringExtensionsKt.chatColorize(
                settingsManager.getLanguageSettings().getRepairing().getCannotUse()));
        event.setCancelled(true);
        removeMapItem(player);
        return;
      }
      ExperienceManager experienceManager = new ExperienceManager(player);
      if (!experienceManager.hasExp(mythicRepairCost.getExperienceCost())) {
        player.sendMessage(
            StringExtensionsKt.chatColorize(
                StringExtensionsKt.replaceArgs(
                    settingsManager.getLanguageSettings().getRepairing().getDoNotHave(),
                    new Pair<>("%material%", "experience"))));
        event.setCancelled(true);
        removeMapItem(player);
        return;
      }
      experienceManager.changeExp(-mythicRepairCost.getExperienceCost());
      player
          .getEquipment()
          .setItemInMainHand(repairItemStack(currentInHand, player.getInventory()));
      removeMapItem(player);
      event.setCancelled(true);
      player.sendMessage(
          StringExtensionsKt.chatColorize(
              settingsManager.getLanguageSettings().getRepairing().getSuccess()));
      player.updateInventory();
      if (settingsManager.getRepairingSettings().isPlaySounds()) {
        player.playSound(event.getBlock().getLocation(), Sound.BLOCK_ANVIL_USE, 1.0F, 1.0F);
      }
    } else {
      EntityEquipment entityEquipment = player.getEquipment();
      if (entityEquipment.getItemInMainHand().getType() == Material.AIR) {
        return;
      }
      if (entityEquipment.getItemInMainHand().getDurability() == 0) {
        return;
      }
      if (getRepairItem(entityEquipment.getItemInMainHand()) == null) {
        return;
      }
      if (entityEquipment.getItemInMainHand().hasItemMeta()) {
        ItemMeta itemMeta = entityEquipment.getItemInMainHand().getItemMeta();
        if (itemMeta.hasLore()) {
          List<String> lore = new ArrayList<>(itemMeta.getLore());
          lore.add(ChatColor.BLACK + "Repairing");
          itemMeta.setLore(lore);
        } else {
          itemMeta.setLore(Collections.singletonList(ChatColor.BLACK + "Repairing"));
        }
        entityEquipment.getItemInMainHand().setItemMeta(itemMeta);
      } else {
        ItemMeta itemMeta =
            Bukkit.getItemFactory().getItemMeta(entityEquipment.getItemInMainHand().getType());
        itemMeta.setLore(Collections.singletonList(ChatColor.BLACK + "Repairing"));
        entityEquipment.getItemInMainHand().setItemMeta(itemMeta);
      }
      repairing.put(player.getName(), entityEquipment.getItemInMainHand());
      player.sendMessage(
          StringExtensionsKt.chatColorize(
              settingsManager.getLanguageSettings().getRepairing().getInstructions()));
    }
  }

  private void removeMapItem(Player player) {
    EntityEquipment entityEquipment = player.getEquipment();
    repairing.remove(player.getName());
    if (entityEquipment.getItemInMainHand().hasItemMeta()) {
      ItemMeta itemMeta = entityEquipment.getItemInMainHand().getItemMeta();
      if (itemMeta.hasLore()) {
        List<String> lore = removeAllString(itemMeta.getLore(), ChatColor.BLACK + "Repairing");
        itemMeta.setLore(lore);
      }
      entityEquipment.getItemInMainHand().setItemMeta(itemMeta);
    }
  }

  private List<String> removeAllString(List<String> list, String toRemove) {
    List<String> newList = new ArrayList<>(list);
    newList.removeIf(s -> s.equals(toRemove));
    return newList;
  }

  private RepairItem getRepairItem(ItemStack itemStack) {
    String displayName = null;
    List<String> lore = new ArrayList<>();
    Material material = itemStack.getType();
    if (itemStack.hasItemMeta()) {
      if (itemStack.getItemMeta().hasDisplayName()) {
        displayName = itemStack.getItemMeta().getDisplayName();
      }
      if (itemStack.getItemMeta().hasLore()) {
        lore = itemStack.getItemMeta().getLore();
      }
    }
    for (RepairItem repItem : repairItemManager.get()) {
      if (repItem.getMaterial() != material) {
        continue;
      }
      if (repItem.getItemName() != null
          && (displayName == null
              || !ChatColor.translateAlternateColorCodes('&', repItem.getName())
                  .equals(displayName))) {
        continue;
      }
      if (repItem.getItemLore() != null && !repItem.getItemLore().isEmpty()) {
        if (lore == null) {
          continue;
        }
        List<String> coloredLore = new ArrayList<>();
        for (String s : repItem.getItemLore()) {
          coloredLore.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        if (!coloredLore.equals(lore)) {
          continue;
        }
      }
      return repItem;
    }
    return null;
  }

  private RepairCost getRepairCost(List<RepairCost> mythicRepairCostsList, Inventory inventory) {
    RepairCost repCost = null;
    for (RepairCost mythicRepairCost : mythicRepairCostsList) {
      if (InventoryExtensionsKt.containsAtLeast(
          inventory,
          mythicRepairCost.getMaterial(),
          mythicRepairCost.getItemName(),
          mythicRepairCost.getItemLore(),
          mythicRepairCost.getEnchantments(),
          mythicRepairCost.getAmount())) {
        if (repCost == null) {
          repCost = mythicRepairCost;
          continue;
        }
        if (repCost.getPriority() > mythicRepairCost.getPriority()) {
          repCost = mythicRepairCost;
        }
      }
    }
    return repCost;
  }

  private ItemStack repairItemStack(ItemStack itemStack, Inventory inventory) {
    if (itemStack == null) {
      return null;
    }
    ItemStack repaired = itemStack.clone();
    RepairItem mythicRepairItem = getRepairItem(itemStack);
    if (mythicRepairItem == null) {
      return repaired;
    }
    List<RepairCost> mythicRepairCostList = mythicRepairItem.getRepairCosts();
    RepairCost mythicRepairCost = getRepairCost(mythicRepairCostList, inventory);
    if (mythicRepairCost == null) {
      return repaired;
    }
    if (!InventoryExtensionsKt.containsAtLeast(
        inventory,
        mythicRepairCost.getMaterial(),
        mythicRepairCost.getItemName(),
        mythicRepairCost.getItemLore(),
        mythicRepairCost.getEnchantments(),
        mythicRepairCost.getAmount())) {
      return repaired;
    }

    InventoryExtensionsKt.removeItem(
        inventory,
        mythicRepairCost.getMaterial(),
        mythicRepairCost.getItemName(),
        mythicRepairCost.getItemLore(),
        mythicRepairCost.getEnchantments(),
        mythicRepairCost.getAmount());

    short currentDurability = repaired.getDurability();
    short newDurability =
        (short)
            (currentDurability
                - repaired.getType().getMaxDurability()
                    * mythicRepairCost.getRepairPercentagePerCost());
    repaired.setDurability((short) Math.max(newDurability, 0));
    for (HumanEntity humanEntity : inventory.getViewers()) {
      if (humanEntity instanceof Player) {
        ((Player) humanEntity).updateInventory();
      }
    }
    return repaired;
  }
}
