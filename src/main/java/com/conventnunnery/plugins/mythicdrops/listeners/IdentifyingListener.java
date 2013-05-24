/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.mythicdrops.listeners;

import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.events.ItemIdentifiedEvent;
import com.conventnunnery.plugins.mythicdrops.managers.DropManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class IdentifyingListener implements Listener {

    private MythicDrops plugin;
    private Map<String, ItemStack> heldIdentify;

    public IdentifyingListener(MythicDrops plugin) {
        this.plugin = plugin;
        heldIdentify = new HashMap<String, ItemStack>();
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
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getItem() == null) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack itemInHand = event.getItem();
        String itemType = getPlugin().getItemManager().itemTypeFromMatData(itemInHand.getData());
        if (getPlugin().getPluginSettings().getSocketGemMaterials().contains(itemInHand.getData())) {
            event.setUseItemInHand(Event.Result.DENY);
        }
        if (getPlugin().getItemManager().isArmor(itemType) && itemInHand.hasItemMeta()) {
            event.setUseItemInHand(Event.Result.DENY);
            player.updateInventory();
        }
        if (heldIdentify.containsKey(player.getName())) {
            identifyItem(event, player, itemInHand, itemType);
        } else {
            addHeldIdentify(event, player, itemInHand);
        }
    }

    private void addHeldIdentify(PlayerInteractEvent event, final Player player, ItemStack itemInHand) {
        if (itemInHand.getData().getItemType() != Material.ENCHANTED_BOOK || !itemInHand.hasItemMeta()) {
            return;
        }
        ItemMeta im = itemInHand.getItemMeta();
        if (!im.hasDisplayName()) {
            return;
        }
        if (!im.getDisplayName().equals(getPlugin().getLanguageManager().getMessage("items.identity-tome" +
                ".name"))) {
            return;
        }
        getPlugin().getLanguageManager().sendMessage(player, "identify.instructions",
                new String[][]{{"%unidentified-name%", getPlugin().getLanguageManager().getMessage("items" +
                        ".unidentified.name")}});
        heldIdentify.put(player.getName(), itemInHand);
        Bukkit.getScheduler().runTaskLaterAsynchronously(getPlugin(), new Runnable() {
            @Override
            public void run() {
                heldIdentify.remove(player.getName());
            }
        }, 20L * 30);
        event.setCancelled(true);
        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);
        player.updateInventory();
    }

    private void identifyItem(PlayerInteractEvent event, Player player, ItemStack itemInHand, String itemType) {
        if (getPlugin().getItemManager().isArmor(itemType) || getPlugin().getItemManager().isTool(itemType)) {
            if (!itemInHand.hasItemMeta()) {
                getPlugin().getLanguageManager().sendMessage(player, "identify.cannot-use");
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setUseItemInHand(Event.Result.DENY);
                heldIdentify.remove(player.getName());
                player.updateInventory();
                return;
            }
            if (player.getInventory().contains(heldIdentify.get(player.getName()))) {
                int indexOfItem = player.getInventory().first(heldIdentify.get(player.getName()));
                ItemStack inInventory = player.getInventory().getItem(indexOfItem);
                inInventory.setAmount(inInventory.getAmount() - 1);
                player.getInventory().setItem(indexOfItem, inInventory);
                player.updateInventory();
            } else {
                getPlugin().getLanguageManager().sendMessage(player, "identify.do-not-have");
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setUseItemInHand(Event.Result.DENY);
                heldIdentify.remove(player.getName());
                player.updateInventory();
                return;
            }
            ItemStack iih = getPlugin().getDropManager().constructItemStack(itemInHand.getData(), DropManager
                    .GenerationReason.IDENTIFYING);
            ItemIdentifiedEvent ise = new ItemIdentifiedEvent(player, iih);
            Bukkit.getPluginManager().callEvent(ise);
            if (ise.isCancelled()) {
                getPlugin().getLanguageManager().sendMessage(player, "identify.cannot-use");
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setUseItemInHand(Event.Result.DENY);
                heldIdentify.remove(player.getName());
                player.updateInventory();
                return;
            }
            player.setItemInHand(ise.getItemStack());
            getPlugin().getLanguageManager().sendMessage(player, "identify.success");
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            heldIdentify.remove(player.getName());
            player.updateInventory();
        } else {
            getPlugin().getLanguageManager().sendMessage(player, "identify.cannot-use");
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            heldIdentify.remove(player.getName());
            player.updateInventory();
        }
    }

    public MythicDrops getPlugin() {
        return plugin;
    }

}
