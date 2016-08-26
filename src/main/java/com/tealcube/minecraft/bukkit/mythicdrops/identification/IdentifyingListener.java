/**
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2013 Teal Cube Games
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
package com.tealcube.minecraft.bukkit.mythicdrops.identification;

import com.tealcube.minecraft.bukkit.hilt.HiltItemStack;
import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGenerationReason;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TierUtil;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class IdentifyingListener implements Listener {

    private Map<String, ItemStack> heldIdentify;
    private MythicDropsPlugin plugin;

    public IdentifyingListener(MythicDropsPlugin plugin) {
        this.plugin = plugin;
        heldIdentify = new HashMap<>();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (heldIdentify.containsKey(player.getName())) {
            heldIdentify.remove(player.getName());
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR
                && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack itemInMainHand = player.getEquipment().getItemInMainHand();
        if (itemInMainHand == null || itemInMainHand.getType() == null) {
            return;
        }
        if (!player.hasPermission("mythicdrops.identify")) {
            return;
        }
        String itemInMainHandType = ItemUtil.getItemTypeFromMaterial(itemInMainHand.getType());
        if (itemInMainHandType != null && ItemUtil.isArmor(itemInMainHandType) && itemInMainHand.hasItemMeta()) {
            event.setUseItemInHand(Event.Result.DENY);
            player.updateInventory();
        }
        if (heldIdentify.containsKey(player.getName())) {
            identifyItem(event, player, itemInMainHand, itemInMainHandType);
        } else {
            addHeldIdentify(event, player, itemInMainHand);
        }
    }

    private void addHeldIdentify(PlayerInteractEvent event, final Player player,
                                 ItemStack itemInHand) {
        if (!itemInHand.hasItemMeta()) {
            return;
        }
        ItemMeta im = itemInHand.getItemMeta();
        ItemStack identityTome = new IdentityTome();
        if (!im.hasDisplayName() ||
                !identityTome.getItemMeta().hasDisplayName() ||
                !im.getDisplayName().equals(identityTome.getItemMeta().getDisplayName())) {
            return;
        }
        player.sendMessage(
                plugin.getConfigSettings().getFormattedLanguageString("command.identifying-instructions"));
        heldIdentify.put(player.getName(), itemInHand);
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                heldIdentify.remove(player.getName());
            }
        }, 20L * 30);
        cancelResults(event);
        player.updateInventory();
    }

    private void identifyItem(PlayerInteractEvent event, Player player, ItemStack itemInHand,
                              String itemType) {
        if (ItemUtil.isArmor(itemType) || ItemUtil.isTool(itemType)) {
            if (!itemInHand.hasItemMeta() || !itemInHand.getItemMeta().hasDisplayName()) {
                cannotUse(event, player);
                return;
            }
            if (!player.getInventory().contains(heldIdentify.get(player.getName()))) {
                player.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.identifying-do-not-have"));
                cancelResults(event);
                heldIdentify.remove(player.getName());
                player.updateInventory();
                return;
            }
            UnidentifiedItem uid = new UnidentifiedItem(itemInHand.getData().getItemType());
            boolean b = itemInHand.getItemMeta().getDisplayName().equals(uid.getItemMeta().getDisplayName());
            if (!b) {
                cannotUse(event, player);
                return;
            }
            String potentialTierString = "";
            if (itemInHand.getItemMeta().hasLore() && itemInHand.getItemMeta().getLore().size() > 0) {
                potentialTierString = ChatColor.stripColor(itemInHand.getItemMeta().getLore().get(itemInHand
                        .getItemMeta().getLore().size() - 1));
            }
            Tier potentialTier = TierUtil.getTier(potentialTierString);
            List<Tier> iihTiers = new ArrayList<>(ItemUtil.getTiersFromMaterial(itemInHand.getType()));
            Tier iihTier = potentialTier != null ? potentialTier : TierUtil.randomTierWithIdentifyChance(iihTiers);
            if (iihTier == null) {
                cannotUse(event, player);
                return;
            }

            ItemStack iih = MythicDropsPlugin.getNewDropBuilder().withItemGenerationReason
                    (ItemGenerationReason.EXTERNAL).withMaterial(itemInHand.getType()).withTier(iihTier)
                                             .useDurability(false).build();
            iih.setDurability(itemInHand.getDurability());

            ItemMeta itemMeta = iih.getItemMeta();
            List<String> lore = new ArrayList<>();
            if (itemMeta.hasLore()) {
                lore = itemMeta.getLore();
            }

            itemMeta.setLore(lore);
            iih.setItemMeta(itemMeta);

            IdentificationEvent identificationEvent = new IdentificationEvent(iih, player);
            Bukkit.getPluginManager().callEvent(identificationEvent);

            if (identificationEvent.isCancelled()) {
                cannotUse(event, player);
                return;
            }

            int indexOfItem = player.getInventory().first(heldIdentify.get(player.getName()));
            ItemStack inInventory = player.getInventory().getItem(indexOfItem);
            inInventory.setAmount(inInventory.getAmount() - 1);
            player.getInventory().setItem(indexOfItem, inInventory);
            player.getEquipment().setItemInMainHand(identificationEvent.getResult());
            player.sendMessage(
                    plugin.getConfigSettings().getFormattedLanguageString("command.identifying-success"));
            cancelResults(event);
            heldIdentify.remove(player.getName());
            player.updateInventory();
        } else {
            cannotUse(event, player);
        }
    }

    private void cannotUse(PlayerInteractEvent event, Player player) {
        player.sendMessage(
                plugin.getConfigSettings().getFormattedLanguageString("command.identifying-cannot-use"));
        cancelResults(event);
        heldIdentify.remove(player.getName());
        player.updateInventory();
    }

    private void cancelResults(PlayerInteractEvent event) {
        event.setCancelled(true);
        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);
    }

}
