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
import com.conventnunnery.plugins.mythicdrops.events.ItemSockettedEvent;
import com.conventnunnery.plugins.mythicdrops.objects.SocketGem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

@SuppressWarnings("deprecation")
public class SockettingListener implements Listener {

    private MythicDrops plugin;
    private Map<String, HeldItem> heldSocket;

    public SockettingListener(MythicDrops plugin) {
        this.plugin = plugin;
        heldSocket = new HashMap<String, HeldItem>();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (heldSocket.containsKey(player.getName())) {
            heldSocket.remove(player.getName());
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
            player.updateInventory();
        }
        if (getPlugin().getItemManager().isArmor(itemType) && itemInHand.hasItemMeta()) {
            event.setUseItemInHand(Event.Result.DENY);
            player.updateInventory();
        }
        if (heldSocket.containsKey(player.getName())) {
            socketItem(event, player, itemInHand, itemType);
        } else {
            addHeldSocket(event, player, itemInHand);
        }
    }

    private void addHeldSocket(PlayerInteractEvent event, final Player player, ItemStack itemInHand) {
        if (!getPlugin().getPluginSettings().getSocketGemMaterials().contains(itemInHand.getData())) {
            return;
        }
        if (!itemInHand.hasItemMeta()) {
            return;
        }
        ItemMeta im = itemInHand.getItemMeta();
        if (!im.hasDisplayName()) {
            return;
        }
        String type = ChatColor.stripColor(im.getDisplayName().replace(getPlugin().getLanguageManager()
                .getMessage("items.socket.name", new String[][]{{"%socketgem%", ""}}), ""));
        if (type == null) {
            return;
        }
        SocketGem socketGem = getPlugin().getSocketGemManager().getSocketGemFromName(type);
        if (socketGem == null) {
            return;
        }
        getPlugin().getLanguageManager().sendMessage(player, "socket.instructions");
        HeldItem hg = new HeldItem(socketGem.getName(), itemInHand);
        heldSocket.put(player.getName(), hg);
        Bukkit.getScheduler().runTaskLaterAsynchronously(getPlugin(), new Runnable() {
            @Override
            public void run() {
                heldSocket.remove(player.getName());
            }
        }, 20L * 30);
        event.setCancelled(true);
        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);
        player.updateInventory();
    }

    private void socketItem(PlayerInteractEvent event, Player player, ItemStack itemInHand, String itemType) {
        if (getPlugin().getItemManager().isArmor(itemType) || getPlugin().getItemManager().isTool(itemType)) {
            if (!itemInHand.hasItemMeta()) {
                getPlugin().getLanguageManager().sendMessage(player, "socket.cannot-use");
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setUseItemInHand(Event.Result.DENY);
                heldSocket.remove(player.getName());
                player.updateInventory();
                return;
            }
            ItemMeta im = itemInHand.getItemMeta();
            if (!im.hasLore()) {
                getPlugin().getLanguageManager().sendMessage(player, "socket.cannot-use");
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setUseItemInHand(Event.Result.DENY);
                heldSocket.remove(player.getName());
                player.updateInventory();
                return;
            }
            List<String> lore = new ArrayList<String>(im.getLore());
            int index = indexOfStripColor(lore, "(Socket)");
            if (index < 0) {
                getPlugin().getLanguageManager().sendMessage(player, "socket.cannot-use");
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setUseItemInHand(Event.Result.DENY);
                heldSocket.remove(player.getName());
                player.updateInventory();
                return;
            }
            HeldItem heldSocket1 = heldSocket.get(player.getName());
            String socketGemType = ChatColor.stripColor(heldSocket1
                    .getName());
            SocketGem socketGem = getPlugin().getSocketGemManager().getSocketGemFromName(socketGemType);
            if (socketGem == null ||
                    !getPlugin().getSocketGemManager().socketGemTypeMatchesItemStack(socketGem, itemInHand)) {
                getPlugin().getLanguageManager().sendMessage(player, "socket.cannot-use");
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setUseItemInHand(Event.Result.DENY);
                heldSocket.remove(player.getName());
                player.updateInventory();
                return;
            }
            lore.set(index, ChatColor.GOLD + socketGem.getName());
            lore.remove(
                    ChatColor.GRAY + "Find a " + ChatColor.GOLD + "Socket Gem" + ChatColor.GRAY + " to fill a " +
                            ChatColor.GOLD + "(Socket)");
            im.setLore(lore);
            itemInHand.setItemMeta(im);
            getPlugin().getSocketGemManager().prefixItemStack(itemInHand, socketGem);
            getPlugin().getSocketGemManager().suffixItemStack(itemInHand, socketGem);
            getPlugin().getSocketGemManager().loreItemStack(itemInHand, socketGem);
            getPlugin().getSocketGemManager().enchantmentItemStack(itemInHand, socketGem);
            if (player.getInventory().contains(heldSocket1.getItemStack())) {
                int indexOfItem = player.getInventory().first(heldSocket1.getItemStack());
                ItemStack inInventory = player.getInventory().getItem(indexOfItem);
                inInventory.setAmount(inInventory.getAmount() - 1);
                player.getInventory().setItem(indexOfItem, inInventory);
                player.updateInventory();
            } else {
                getPlugin().getLanguageManager().sendMessage(player, "socket.do-not-have");
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setUseItemInHand(Event.Result.DENY);
                heldSocket.remove(player.getName());
                player.updateInventory();
                return;
            }
            ItemSockettedEvent ise = new ItemSockettedEvent(player, itemInHand, socketGem);
            Bukkit.getPluginManager().callEvent(ise);
            if (ise.isCancelled()) {
                getPlugin().getLanguageManager().sendMessage(player, "socket.cannot-use");
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setUseItemInHand(Event.Result.DENY);
                heldSocket.remove(player.getName());
                player.updateInventory();
                return;
            }
            player.setItemInHand(ise.getItemStack());
            getPlugin().getLanguageManager().sendMessage(player, "socket.success");
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            heldSocket.remove(player.getName());
            player.updateInventory();
        } else {
            getPlugin().getLanguageManager().sendMessage(player, "socket.cannot-use");
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            heldSocket.remove(player.getName());
            player.updateInventory();
        }
    }

    public int indexOfStripColor(List<String> list, String string) {
        String[] array = list.toArray(new String[list.size()]);
        for (int i = 0; i < array.length; i++) {
            if (ChatColor.stripColor(array[i]).equalsIgnoreCase(ChatColor.stripColor(string))) {
                return i;
            }
        }
        return -1;
    }

    public MythicDrops getPlugin() {
        return plugin;
    }

    private static class HeldItem {

        private final String name;
        private final ItemStack itemStack;

        public HeldItem(String name, ItemStack itemStack) {
            this.name = name;
            this.itemStack = itemStack;
        }

        public String getName() {
            return name;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

    }

}
