/**
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2013 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.socketting;

import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin;
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SockettingSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.GemType;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketCommandRunner;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketEffect;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.SocketGemUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.StringListUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TierUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SockettingListener implements Listener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SockettingListener.class);

    private final Map<String, HeldItem> heldSocket = new HashMap<>();
    private MythicDropsPlugin mythicDrops;

    public SockettingListener(MythicDropsPlugin mythicDrops) {
        this.mythicDrops = mythicDrops;
    }

    public MythicDrops getMythicDrops() {
        return mythicDrops;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR
                && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            LOGGER.debug("onRightClick() - event.getAction() != RIGHT_CLICK_AIR && event.getAction() != RIGHT_CLICK_BLOCK");
            return;
        }
        Player player = event.getPlayer();
        ItemStack itemInMainHand = player.getEquipment().getItemInMainHand();
        if (itemInMainHand == null || itemInMainHand.getType() == null) {
            LOGGER.debug("onRightClick() - itemInMainHand == null || itemInMainHand.getType() == null");
            return;
        }
        if (!player.hasPermission("mythicdrops.socket")) {
            LOGGER.debug("onRightClick() - !player.hasPermission(\"mythicdrops.socket\")");
            return;
        }
        String itemInMainHandType = ItemUtil.getItemTypeFromMaterial(itemInMainHand.getType());
        if (!mythicDrops.getConfigSettings().isAllowEquippingItemsViaRightClick() &&
                itemInMainHandType != null && ItemUtil.isArmor(itemInMainHandType) && itemInMainHand.hasItemMeta()) {
            event.setUseItemInHand(Event.Result.DENY);
            player.updateInventory();
        }
        if (heldSocket.containsKey(player.getName())) {
            LOGGER.debug("onRightClick() - heldSocket.containsKey({})", player.getName());
            socketItem(event, player, itemInMainHand, itemInMainHandType);
            heldSocket.remove(player.getName());
        } else {
            LOGGER.debug("onRightClick() - !heldSocket.containsKey({})", player.getName());
            addHeldSocket(event, player, itemInMainHand);
        }
    }

    private void addHeldSocket(PlayerInteractEvent event, final Player player, ItemStack itemInHand) {
        if (!mythicDrops.getSockettingSettings().getSocketGemMaterials()
                .contains(itemInHand.getType())) {
            LOGGER.debug("addHeldSocket() - !socketGemMaterials.contains(itemInHand.getType())");
            return;
        }
        if (!itemInHand.hasItemMeta()) {
            LOGGER.debug("addHeldSocket() - !itemInHand.hasItemMeta()");
            return;
        }
        ItemMeta im = itemInHand.getItemMeta();
        if (!im.hasDisplayName()) {
            LOGGER.debug("addHeldSocket() - !im.hasDisplayName()");
            return;
        }
        String
                replacedArgs =
                ChatColor.stripColor(replaceArgs(mythicDrops.getSockettingSettings().getSocketGemName(),
                        new String[][]{{"%socketgem%", ""}}).replace('&', '\u00A7')
                        .replace("\u00A7\u00A7", "&"));
        String type = ChatColor.stripColor(im.getDisplayName().replace(replacedArgs, ""));
        if (type == null) {
            LOGGER.debug("addHeldSocket() - type == null");
            return;
        }
        if (!ChatColor.stripColor(replaceArgs(mythicDrops.getSockettingSettings().getSocketGemName(),
                new String[][]{{"%socketgem%", type}})).equals(
                        ChatColor.stripColor(im.getDisplayName()))) {
            LOGGER.debug("addHeldSocket() - !replaceArgs");
            return;
        }
        SocketGem socketGem = mythicDrops.getSockettingSettings().getSocketGemMap().get(type);
        if (socketGem == null) {
            socketGem = SocketGemUtil.getSocketGemFromName(type);
            if (socketGem == null) {
                LOGGER.debug("addHeldSocket() - socketGem == null");
                return;
            }
        }
        player.sendMessage(
                mythicDrops.getConfigSettings().getFormattedLanguageString("command.socket-instructions",
                        new String[][]{})
        );
        HeldItem hg = new HeldItem(socketGem.getName(), itemInHand);
        heldSocket.put(player.getName(), hg);
        Bukkit.getScheduler().runTaskLaterAsynchronously(mythicDrops, new Runnable() {
            @Override
            public void run() {
                heldSocket.remove(player.getName());
            }
        }, 30 * 20L);
        event.setCancelled(true);
        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);
        player.updateInventory();
    }

    private String replaceArgs(String string, String[][] args) {
        String s = string;
        for (String[] arg : args) {
            s = s.replace(arg[0], arg[1]);
        }
        return s;
    }

    private void socketItem(PlayerInteractEvent event, Player player, ItemStack itemInHand,
                            String itemType) {
        if (ItemUtil.isArmor(itemType) || ItemUtil.isTool(itemType)) {
            if (!itemInHand.hasItemMeta()) {
                LOGGER.debug("socketItem() - !itemInHand.hasItemMeta()");
                player.sendMessage(
                        mythicDrops.getConfigSettings().getFormattedLanguageString(
                                "command.socket-cannot-use", new String[][]{}));
                cancelDenyRemove(event, player);
                player.updateInventory();
                return;
            }
            ItemMeta im = itemInHand.getItemMeta();
            if (!im.hasLore()) {
                LOGGER.debug("socketItem() - !im.hasLore()");
                player.sendMessage(
                        mythicDrops.getConfigSettings().getFormattedLanguageString(
                                "command.socket-cannot-use", new String[][]{}));
                cancelDenyRemove(event, player);
                player.updateInventory();
                return;
            }
            List<String> lore = new ArrayList<>(im.getLore());
            String socketString =
                    mythicDrops.getSockettingSettings().getSockettedItemString().replace('&', '\u00A7')
                            .replace("\u00A7\u00A7", "&").replace("%tiercolor%", "");
            int index = indexOfStripColor(lore, socketString);
            if (index < 0) {
                LOGGER.debug("socketItem() - index < 0: lore=[{}], socketString=\"{}\"", lore, socketString);
                player.sendMessage(
                        mythicDrops.getConfigSettings().getFormattedLanguageString(
                                "command.socket-cannot-use", new String[][]{}));
                cancelDenyRemove(event, player);
                player.updateInventory();
                return;
            }
            HeldItem heldSocket1 = heldSocket.get(player.getName());
            String socketGemType = ChatColor.stripColor(heldSocket1.getName());
            SocketGem socketGem = SocketGemUtil.getSocketGemFromName(socketGemType);
            if (socketGem == null || !socketGemTypeMatchesItemStack(socketGem, itemInHand)) {
                LOGGER.debug("socketItem() - socketGem == null || !socketGemTypeMatchesItemStack()");
                player.sendMessage(
                        mythicDrops.getConfigSettings().getFormattedLanguageString(
                                "command.socket-cannot-use", new String[][]{}));
                cancelDenyRemove(event, player);
                player.updateInventory();
                return;
            }

            if (!player.getInventory().contains(heldSocket1.getItemStack())) {
                LOGGER.debug("socketItem() - !player.getInventory().contains(heldSocket1.getItemStack())");
                player.sendMessage(
                        mythicDrops.getConfigSettings().getFormattedLanguageString("command.socket-do-not-have",
                                new String[][]{})
                );
                cancelDenyRemove(event, player);
                player.updateInventory();
                return;
            }

            if (itemInHand.getAmount() > heldSocket1.getItemStack().getAmount()) {
                LOGGER.debug("socketItem() - itemInHand.getAmount() > heldSocket1.getItemStack().getAmount()");
                player.sendMessage(
                        mythicDrops.getConfigSettings().getFormattedLanguageString(
                                "command.socket-do-not-have", new String[][]{}));
                cancelDenyRemove(event, player);
                player.updateInventory();
                return;
            }

            Tier tier = TierUtil.getTierFromItemStack(itemInHand);

            ChatColor cc = tier != null ? tier.getDisplayColor() : ChatColor.GOLD;

            lore.set(index, cc + socketGem.getName());

            List<String> colorCoded = StringListUtil.replaceArgs(
                    StringListUtil.colorList(mythicDrops.getSockettingSettings().getSockettedItemLore()),
                    new String[][]{{"%tiercolor%", tier != null ? tier.getDisplayColor() + "" : ""}});

            lore = StringListUtil.removeIfMatchesColorless(lore, colorCoded);

            im.setLore(lore);
            im = prefixItemStack(im, socketGem);
            im = suffixItemStack(im, socketGem);
            im = loreItemStack(im, socketGem);
            im = enchantmentItemStack(im, socketGem);

            int indexOfItem = player.getInventory().first(heldSocket1.getItemStack());
            ItemStack inInventory = player.getInventory().getItem(indexOfItem);
            inInventory.setAmount(inInventory.getAmount() - itemInHand.getAmount());
            player.getInventory().setItem(indexOfItem, inInventory);
            player.updateInventory();
            itemInHand.setItemMeta(im);
            player.getEquipment().setItemInMainHand(itemInHand);
            player.sendMessage(
                    mythicDrops.getConfigSettings().getFormattedLanguageString(
                            "command.socket-success", new String[][]{}));
            cancelDenyRemove(event, player);
            player.updateInventory();
        } else {
            LOGGER.debug("socketItem() - !ItemUtil.isArmor(\"{}\") && !ItemUtil.isTool(\"{}\")", itemType, itemType);
            player.sendMessage(
                    mythicDrops.getConfigSettings().getFormattedLanguageString(
                            "command.socket-cannot-use", new String[][]{}));
            cancelDenyRemove(event, player);
            player.updateInventory();
        }
    }

    private void cancelDenyRemove(PlayerInteractEvent event, Player player) {
        cancelResults(event);
        heldSocket.remove(player.getName());
    }

    private void cancelResults(PlayerInteractEvent event) {
        LOGGER.debug("cancelResults - cancelling results");
        event.setCancelled(true);
        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);
        event.getPlayer().updateInventory();
    }

    private boolean socketGemTypeMatchesItemStack(SocketGem socketGem, ItemStack itemStack) {
        String itemType = ItemUtil.getItemTypeFromMaterial(itemStack.getType());
        if (itemType == null) {
            return false;
        }
        switch (socketGem.getGemType()) {
            case TOOL:
                return ItemUtil.isTool(itemType);
            case ARMOR:
                return ItemUtil.isArmor(itemType);
            case ANY:
                return true;
            default:
                return false;
        }
    }

    private int indexOfStripColor(List<String> list, String string) {
        String[] array = list.toArray(new String[list.size()]);
        for (int i = 0; i < array.length; i++) {
            if (ChatColor.stripColor(array[i]).equalsIgnoreCase(ChatColor.stripColor(string))) {
                return i;
            }
        }
        return -1;
    }

    private ItemMeta loreItemStack(ItemMeta im, SocketGem socketGem) {
        if (!im.hasLore()) {
            im.setLore(new ArrayList<String>());
        }
        List<String> lore = new ArrayList<>(im.getLore());
        if (lore.containsAll(socketGem.getLore())) {
            return im;
        }
        for (String s : socketGem.getLore()) {
            lore.add(s.replace('&', '\u00A7').replace("\u00A7\u00A7", "&"));
        }
        im.setLore(lore);
        return im;
    }

    private ItemMeta enchantmentItemStack(ItemMeta itemMeta, SocketGem socketGem) {
        if (itemMeta == null || socketGem == null) {
            return itemMeta;
        }

        Map<Enchantment, Integer> itemStackEnchantments = new HashMap<>(itemMeta.getEnchants());
        for (Map.Entry<Enchantment, Integer> entry : socketGem.getEnchantments().entrySet()) {
            int currentLevel = itemStackEnchantments.containsKey(entry.getKey()) ?
                    itemStackEnchantments.get(entry.getKey()) : 0;
            currentLevel += entry.getValue();
            itemStackEnchantments.put(entry.getKey(), currentLevel);
        }

        for (Enchantment ench : itemStackEnchantments.keySet()) {
            itemMeta.removeEnchant(ench);
        }

        for (Map.Entry<Enchantment, Integer> entry : itemStackEnchantments.entrySet()) {
            itemMeta.addEnchant(entry.getKey(), entry.getValue(), true);
        }

        return itemMeta;
    }

    private ItemMeta suffixItemStack(ItemMeta im, SocketGem socketGem) {
        String name = im.getDisplayName();
        if (name == null) {
            return im;
        }
        ChatColor beginColor = findColor(name);
        String lastColors = ChatColor.getLastColors(name);
        if (beginColor == null) {
            beginColor = ChatColor.WHITE;
        }
        String suffix = socketGem.getSuffix();
        if (suffix == null || suffix.equalsIgnoreCase("")) {
            return im;
        }
        if (mythicDrops.getSockettingSettings().isPreventMultipleChangesFromSockets() &&
                ChatColor.stripColor(name).contains(suffix) ||
                containsAnyFromList(ChatColor.stripColor(name),
                        mythicDrops.getSockettingSettings().getSocketGemSuffixes())) {
            return im;
        }
        im.setDisplayName(name + " " + beginColor + suffix + lastColors);
        return im;
    }

    private ItemMeta prefixItemStack(ItemMeta im, SocketGem socketGem) {
        String name = im.getDisplayName();
        if (name == null) {
            return im;
        }
        ChatColor beginColor = findColor(name);
        if (beginColor == null) {
            beginColor = ChatColor.WHITE;
        }
        String prefix = socketGem.getPrefix();
        if (prefix == null || prefix.equalsIgnoreCase("")) {
            return im;
        }
        if (mythicDrops.getSockettingSettings().isPreventMultipleChangesFromSockets() &&
                ChatColor.stripColor(name).contains(prefix) || containsAnyFromList(
                ChatColor.stripColor(name),
                mythicDrops.getSockettingSettings().getSocketGemPrefixes())) {
            return im;
        }
        im.setDisplayName(beginColor + prefix + " " + name);
        return im;
    }

    private boolean containsAnyFromList(String string, List<String> list) {
        for (String s : list) {
            if (string.toUpperCase().contains(s.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    private ChatColor findColor(final String s) {
        char[] c = s.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == (char) 167 && i + 1 < c.length) {
                return ChatColor.getByChar(c[i + 1]);
            }
        }
        return null;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Entity e = event.getEntity();
        Entity d = event.getDamager();
        if (!(e instanceof LivingEntity)) {
            return;
        }
        LivingEntity lee = (LivingEntity) e;
        LivingEntity led;
        if (d instanceof LivingEntity) {
            led = (LivingEntity) d;
        } else if (d instanceof Projectile && ((Projectile) d).getShooter() instanceof LivingEntity) {
            Projectile p = (Projectile) d;
            led = (LivingEntity) p.getShooter();
            if (led == null) {
                return;
            }
        } else {
            return;
        }
        if (Objects.equals(led, lee)) {
            return;
        }
        applyEffects(led, lee);
        runCommands(led, lee);
    }

    private void runCommands(LivingEntity attacker, LivingEntity defender) {
        if (attacker == null || defender == null) {
            return;
        }
        SockettingSettings ss = mythicDrops.getSockettingSettings();
        if (attacker instanceof Player) {
            if (ss.isUseAttackerArmorEquipped()) {
                for (ItemStack attackersItem : attacker.getEquipment().getArmorContents()) {
                    if (attackersItem == null) {
                        continue;
                    }
                    List<SocketGem> attackerSocketGems = getSocketGems(attackersItem);
                    if (attackerSocketGems != null && !attackerSocketGems.isEmpty()) {
                        for (SocketGem sg : attackerSocketGems) {
                            if (sg == null) {
                                continue;
                            }
                            for (SocketCommand sc : sg.getCommands()) {
                                if (sc.getRunner() == SocketCommandRunner.CONSOLE) {
                                    String command = sc.getCommand();
                                    if (command.contains("%wielder%")) {
                                        command = command.replace("%wielder%", ((Player) attacker).getName());
                                    }
                                    if (command.contains("%target%")) {
                                        if (defender instanceof Player) {
                                            command = command.replace("%target%", ((Player) defender).getName());
                                        } else {
                                            continue;
                                        }
                                    }
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                                } else {
                                    String command = sc.getCommand();
                                    if (command.contains("%wielder%")) {
                                        command = command.replace("%wielder%", ((Player) attacker).getName());
                                    }
                                    if (command.contains("%target%")) {
                                        if (defender instanceof Player) {
                                            command = command.replace("%target%", ((Player) defender).getName());
                                        } else {
                                            continue;
                                        }
                                    }
                                    ((Player) attacker).chat("/" + command);
                                }
                            }
                        }
                    }
                }
            }
            if (ss.isUseAttackerItemInHand() && attacker.getEquipment().getItemInMainHand() != null) {
                List<SocketGem> attackerSocketGems = getSocketGems(attacker.getEquipment().getItemInMainHand());
                if (attackerSocketGems != null && !attackerSocketGems.isEmpty()) {
                    for (SocketGem sg : attackerSocketGems) {
                        if (sg == null) {
                            continue;
                        }
                        for (SocketCommand sc : sg.getCommands()) {
                            if (sc.getRunner() == SocketCommandRunner.CONSOLE) {
                                String command = sc.getCommand();
                                if (command.contains("%wielder%")) {
                                    command = command.replace("%wielder%", ((Player) attacker).getName());
                                }
                                if (command.contains("%target%")) {
                                    if (defender instanceof Player) {
                                        command = command.replace("%target%", ((Player) defender).getName());
                                    } else {
                                        continue;
                                    }
                                }
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                            } else {
                                String command = sc.getCommand();
                                if (command.contains("%wielder%")) {
                                    command = command.replace("%wielder%", ((Player) attacker).getName());
                                }
                                if (command.contains("%target%")) {
                                    if (defender instanceof Player) {
                                        command = command.replace("%target%", ((Player) defender).getName());
                                    } else {
                                        continue;
                                    }
                                }
                                ((Player) attacker).chat("/" + command);
                            }
                        }
                    }
                }
            }
        }
        if (defender instanceof Player) {
            if (ss.isUseDefenderArmorEquipped()) {
                for (ItemStack defendersItem : defender.getEquipment().getArmorContents()) {
                    if (defendersItem == null) {
                        continue;
                    }
                    List<SocketGem> defenderSocketGems = getSocketGems(defendersItem);
                    if (defenderSocketGems != null && !defenderSocketGems.isEmpty()) {
                        for (SocketGem sg : defenderSocketGems) {
                            if (sg == null) {
                                continue;
                            }
                            for (SocketCommand sc : sg.getCommands()) {
                                if (sc.getRunner() == SocketCommandRunner.CONSOLE) {
                                    String command = sc.getCommand();
                                    if (command.contains("%wielder%")) {
                                        command = command.replace("%wielder%", ((Player) defender).getName());
                                    }
                                    if (command.contains("%target%")) {
                                        if (attacker instanceof Player) {
                                            command = command.replace("%target%", ((Player) attacker).getName());
                                        } else {
                                            continue;
                                        }
                                    }
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                                } else {
                                    String command = sc.getCommand();
                                    if (command.contains("%wielder%")) {
                                        command = command.replace("%wielder%", ((Player) defender).getName());
                                    }
                                    if (command.contains("%target%")) {
                                        if (attacker instanceof Player) {
                                            command = command.replace("%target%", ((Player) attacker).getName());
                                        } else {
                                            continue;
                                        }
                                    }
                                    ((Player) defender).chat("/" + command);
                                }
                            }
                        }
                    }
                }
            }
            if (ss.isUseDefenderItemInHand() && defender.getEquipment().getItemInMainHand() != null) {
                List<SocketGem> defenderSocketGems = getSocketGems(defender.getEquipment().getItemInMainHand());
                if (defenderSocketGems != null && !defenderSocketGems.isEmpty()) {
                    for (SocketGem sg : defenderSocketGems) {
                        if (sg == null) {
                            continue;
                        }
                        for (SocketCommand sc : sg.getCommands()) {
                            if (sc.getRunner() == SocketCommandRunner.CONSOLE) {
                                String command = sc.getCommand();
                                if (command.contains("%wielder%")) {
                                    command = command.replace("%wielder%", ((Player) defender).getName());
                                }
                                if (command.contains("%target%")) {
                                    if (attacker instanceof Player) {
                                        command = command.replace("%target%", ((Player) attacker).getName());
                                    } else {
                                        continue;
                                    }
                                }
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                            } else {
                                String command = sc.getCommand();
                                if (command.contains("%wielder%")) {
                                    command = command.replace("%wielder%", ((Player) defender).getName());
                                }
                                if (command.contains("%target%")) {
                                    if (attacker instanceof Player) {
                                        command = command.replace("%target%", ((Player) attacker).getName());
                                    } else {
                                        continue;
                                    }
                                }
                                ((Player) defender).chat("/" + command);
                            }
                        }
                    }
                }
            }
        }
    }

    private void applyEffects(LivingEntity attacker, LivingEntity defender) {
        if (attacker == null || defender == null) {
            return;
        }
        SockettingSettings ss = mythicDrops.getSockettingSettings();
        // handle attacker
        if (ss.isUseAttackerArmorEquipped()) {
            for (ItemStack attackersItem : attacker.getEquipment().getArmorContents()) {
                if (attackersItem == null) {
                    continue;
                }
                List<SocketGem> attackerSocketGems = getSocketGems(attackersItem);
                if (attackerSocketGems != null && !attackerSocketGems.isEmpty()) {
                    for (SocketGem sg : attackerSocketGems) {
                        if (sg == null) {
                            continue;
                        }
                        if (sg.getGemType() != GemType.TOOL && sg.getGemType() != GemType.ANY) {
                            continue;
                        }
                        for (SocketEffect se : sg.getSocketEffects()) {
                            if (se == null) {
                                continue;
                            }
                            switch (se.getEffectTarget()) {
                                case SELF:
                                    se.apply(attacker);
                                    break;
                                case OTHER:
                                    se.apply(defender);
                                    break;
                                case AREA:
                                    for (Entity e : attacker.getNearbyEntities(se.getRadius(), se.getRadius(),
                                            se.getRadius())) {
                                        if (!(e instanceof LivingEntity)) {
                                            continue;
                                        }
                                        if (!se.isAffectsTarget() && e.equals(defender)) {
                                            continue;
                                        }
                                        se.apply((LivingEntity) e);
                                    }
                                    if (se.isAffectsWielder()) {
                                        se.apply(attacker);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        }
        if (ss.isUseAttackerItemInHand() && attacker.getEquipment().getItemInMainHand() != null) {
            List<SocketGem> attackerSocketGems = getSocketGems(attacker.getEquipment().getItemInMainHand());
            if (attackerSocketGems != null && !attackerSocketGems.isEmpty()) {
                for (SocketGem sg : attackerSocketGems) {
                    if (sg == null) {
                        continue;
                    }
                    if (sg.getGemType() != GemType.TOOL && sg.getGemType() != GemType.ANY) {
                        continue;
                    }
                    for (SocketEffect se : sg.getSocketEffects()) {
                        if (se == null) {
                            continue;
                        }
                        switch (se.getEffectTarget()) {
                            case SELF:
                                se.apply(attacker);
                                break;
                            case OTHER:
                                se.apply(defender);
                                break;
                            case AREA:
                                for (Entity e : attacker.getNearbyEntities(se.getRadius(), se.getRadius(),
                                        se.getRadius())) {
                                    if (!(e instanceof LivingEntity)) {
                                        continue;
                                    }
                                    if (!se.isAffectsTarget() && e.equals(defender)) {
                                        continue;
                                    }
                                    se.apply((LivingEntity) e);
                                }
                                if (se.isAffectsWielder()) {
                                    se.apply(attacker);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        // handle defender
        if (ss.isUseDefenderArmorEquipped()) {
            for (ItemStack defenderItem : defender.getEquipment().getArmorContents()) {
                if (defenderItem == null) {
                    continue;
                }
                List<SocketGem> defenderSocketGems = getSocketGems(defenderItem);
                for (SocketGem sg : defenderSocketGems) {
                    if (sg.getGemType() != GemType.ARMOR && sg.getGemType() != GemType.ANY) {
                        continue;
                    }
                    for (SocketEffect se : sg.getSocketEffects()) {
                        if (se == null) {
                            continue;
                        }
                        switch (se.getEffectTarget()) {
                            case SELF:
                                se.apply(defender);
                                break;
                            case OTHER:
                                se.apply(attacker);
                                break;
                            case AREA:
                                for (Entity e : defender.getNearbyEntities(se.getRadius(), se.getRadius(),
                                        se.getRadius())) {
                                    if (!(e instanceof LivingEntity)) {
                                        continue;
                                    }
                                    if (!se.isAffectsTarget() && e.equals(attacker)) {
                                        continue;
                                    }
                                    se.apply((LivingEntity) e);
                                }
                                if (se.isAffectsWielder()) {
                                    se.apply(defender);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        if (ss.isUseDefenderItemInHand() && defender.getEquipment().getItemInMainHand() != null) {
            List<SocketGem> defenderSocketGems = getSocketGems(defender.getEquipment().getItemInMainHand());
            if (defenderSocketGems != null && !defenderSocketGems.isEmpty()) {
                for (SocketGem sg : defenderSocketGems) {
                    if (sg.getGemType() != GemType.ARMOR && sg.getGemType() != GemType.ANY) {
                        continue;
                    }
                    for (SocketEffect se : sg.getSocketEffects()) {
                        if (se == null) {
                            continue;
                        }
                        switch (se.getEffectTarget()) {
                            case SELF:
                                se.apply(defender);
                                break;
                            case OTHER:
                                se.apply(attacker);
                                break;
                            case AREA:
                                for (Entity e : defender.getNearbyEntities(se.getRadius(), se.getRadius(),
                                        se.getRadius())) {
                                    if (!(e instanceof LivingEntity)) {
                                        continue;
                                    }
                                    if (!se.isAffectsTarget() && e.equals(attacker)) {
                                        continue;
                                    }
                                    se.apply((LivingEntity) e);
                                }
                                if (se.isAffectsWielder()) {
                                    se.apply(defender);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    public List<SocketGem> getSocketGems(ItemStack itemStack) {
        List<SocketGem> socketGemList = new ArrayList<SocketGem>();
        ItemMeta im;
        if (itemStack.hasItemMeta()) {
            im = itemStack.getItemMeta();
        } else {
            return socketGemList;
        }
        List<String> lore = im.getLore();
        if (lore == null) {
            return socketGemList;
        }
        for (String s : lore) {
            SocketGem sg = SocketGemUtil.getSocketGemFromName(ChatColor.stripColor(s));
            if (sg == null) {
                continue;
            }
            socketGemList.add(sg);
        }
        return socketGemList;
    }

    public int indexOfStripColor(String[] array, String string) {
        for (int i = 0; i < array.length; i++) {
            if (ChatColor.stripColor(array[i]).equalsIgnoreCase(ChatColor.stripColor(string))) {
                return i;
            }
        }
        return -1;
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
