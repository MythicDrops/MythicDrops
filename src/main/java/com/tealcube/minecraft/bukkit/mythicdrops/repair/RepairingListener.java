package com.tealcube.minecraft.bukkit.mythicdrops.repair;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby
 * granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER
 * IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


import com.comphenix.xp.rewards.xp.ExperienceManager;
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops;
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairCost;
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairItem;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public final class RepairingListener implements Listener {

    private MythicDrops mythicDrops;
    private Map<String, ItemStack> repairing;

    public RepairingListener(MythicDrops mythicDrops) {
        this.mythicDrops = mythicDrops;
        repairing = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockDamageEvent(BlockDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getPlayer() == null) {
            return;
        }
        if (!event.getPlayer().hasPermission("mythicdrops.repair")
                || event.getBlock().getType() != Material.ANVIL) {
            return;
        }
        Player player = event.getPlayer();
        if (repairing.containsKey(player.getName())) {
            ItemStack oldInHand = repairing.get(player.getName());
            ItemStack currentInHand = player.getItemInHand();
            if (oldInHand.getType() != currentInHand.getType()) {
                player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                                      ".repair-cannot-use"));
                repairing.remove(player.getName());
                return;
            }
            if (oldInHand.getDurability() == 0 || currentInHand.getDurability() == 0) {
                player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                                      ".repair-cannot-use"));
                event.setCancelled(true);
                removeMapItem(player);
                return;
            }
            if (!oldInHand.isSimilar(currentInHand)) {
                player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                                      ".repair-cannot-use"));
                event.setCancelled(true);
                removeMapItem(player);
                return;
            }
            RepairItem mythicRepairItem = getRepairItem(currentInHand);
            if (mythicRepairItem == null) {
                player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                                      ".repair-cannot-use"));
                event.setCancelled(true);
                removeMapItem(player);
                return;
            }
            List<RepairCost> mythicRepairCostList = mythicRepairItem.getRepairCosts();
            if (mythicRepairCostList == null) {
                player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                                      ".repair-cannot-use"));
                event.setCancelled(true);
                removeMapItem(player);
                return;
            }
            RepairCost mythicRepairCost =
                    getRepairCost(mythicRepairItem, mythicRepairCostList, player.getInventory());
            if (mythicRepairCost == null) {
                player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                                      ".repair-cannot-use"));
                event.setCancelled(true);
                removeMapItem(player);
                return;
            }
            if (!player.getInventory().containsAtLeast(mythicRepairCost.toItemStack(1),
                                                       mythicRepairCost.getAmount())) {
                player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                                      ".repair-do-not-have",
                                                                                              new String[][]{
                                                                                                      {"%material%",
                                                                                                       mythicRepairItem
                                                                                                               .toItemStack(
                                                                                                                       1)
                                                                                                               .getType()
                                                                                                               .name()}}
                                                                                             ));
                event.setCancelled(true);
                removeMapItem(player);
                return;
            }
            ExperienceManager experienceManager = new ExperienceManager(player);
            if (!experienceManager.hasExp(mythicRepairCost.getExperienceCost())) {
                player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                                      ".repair"
                                                                                                      + ".do-not-have",
                                                                                              new String[][]{
                                                                                                      {"%material%",
                                                                                                       "experience"}}
                                                                                             ));
                event.setCancelled(true);
                removeMapItem(player);
                return;
            }
            experienceManager.changeExp(-mythicRepairCost.getExperienceCost());
            player.setItemInHand(repairItemStack(currentInHand, player.getInventory()));
            removeMapItem(player);
            event.setCancelled(true);
            player.sendMessage(
                    mythicDrops.getConfigSettings().getFormattedLanguageString("command.repair-success"));
            player.updateInventory();
            if (mythicDrops.getRepairingSettings().isPlaySounds()) {
                player.playSound(event.getBlock().getLocation(), Sound.ANVIL_USE, 1.0F, 1.0F);
            }
        } else {
            if (player.getItemInHand().getType() == Material.AIR) {
                return;
            }
            if (player.getItemInHand().getDurability() == 0) {
                return;
            }
            if (getRepairItem(player.getItemInHand()) == null) {
                return;
            }
            if (player.getItemInHand().hasItemMeta()) {
                ItemMeta itemMeta = player.getItemInHand().getItemMeta();
                if (itemMeta.hasLore()) {
                    List<String> lore = new ArrayList<>(itemMeta.getLore());
                    lore.add(ChatColor.BLACK + "Repairing");
                    itemMeta.setLore(lore);
                } else {
                    itemMeta.setLore(Arrays.asList(ChatColor.BLACK + "Repairing"));
                }
                player.getItemInHand().setItemMeta(itemMeta);
            } else {
                ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(player.getItemInHand().getType());
                itemMeta.setLore(Arrays.asList(ChatColor.BLACK + "Repairing"));
                player.getInventory().getItemInHand().setItemMeta(itemMeta);
            }
            repairing.put(player.getName(), player.getItemInHand());
            player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                                  ".repair-instructions"));
        }
    }

    private void removeMapItem(Player player) {
        repairing.remove(player.getName());
        if (player.getItemInHand().hasItemMeta()) {
            ItemMeta itemMeta = player.getItemInHand().getItemMeta();
            if (itemMeta.hasLore()) {
                List<String> lore = removeAllString(itemMeta.getLore(), ChatColor.BLACK + "Repairing");
                itemMeta.setLore(lore);
            }
            player.getItemInHand().setItemMeta(itemMeta);
        }
    }

    private List<String> removeAllString(List<String> list, String toRemove) {
        List<String> newList = new ArrayList<>(list);
        Iterator<String> iterator = newList.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            if (s.equals(toRemove)) {
                iterator.remove();
            }
        }
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
        for (RepairItem repItem : MythicRepairItemMap.getInstance().values()) {
            if (repItem.getMaterial() != material) {
                continue;
            }
            if (repItem.getItemName() != null && (displayName == null || !ChatColor
                    .translateAlternateColorCodes('&',
                                                  repItem.getName()).equals(displayName))) {
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

    private RepairCost getRepairCost(RepairItem mythicRepairItem,
                                     List<RepairCost> mythicRepairCostsList, Inventory inventory) {
        RepairCost repCost = null;
        for (RepairCost mythicRepairCost : mythicRepairCostsList) {
            ItemStack itemStack = mythicRepairCost.toItemStack(1);
            if (inventory.containsAtLeast(itemStack, 1)) {
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
        if (mythicRepairCostList == null) {
            return repaired;
        }
        RepairCost mythicRepairCost = getRepairCost(mythicRepairItem, mythicRepairCostList, inventory);
        if (mythicRepairCost == null) {
            return repaired;
        }
        if (!inventory.containsAtLeast(mythicRepairCost.toItemStack(1), mythicRepairCost.getAmount())) {
            return repaired;
        }

        inventory.removeItem(mythicRepairCost.toItemStack(mythicRepairCost.getAmount()));

        short currentDurability = repaired.getDurability();
        short newDurability = (short) (currentDurability - repaired.getType().getMaxDurability()
                * mythicRepairCost
                .getRepairPercentagePerCost());
        repaired.setDurability((short) Math.max(newDurability, 0));
        for (HumanEntity humanEntity : inventory.getViewers()) {
            if (humanEntity instanceof Player) {
                ((Player) humanEntity).updateInventory();
            }
        }
        return repaired;
    }

}
