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
package com.tealcube.minecraft.bukkit.mythicdrops.socketting;

import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin;
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops;
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroup;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketGem;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketGemMap;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.SocketGemUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.StringListUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TierUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class OldSockettingListener implements Listener {

  private static final Logger LOGGER =
      JulLoggerFactory.INSTANCE.getLogger(OldSockettingListener.class);

  private final Map<String, HeldItem> heldSocket = new HashMap<>();
  private MythicDropsPlugin mythicDrops;

  public OldSockettingListener(MythicDropsPlugin mythicDrops) {
    this.mythicDrops = mythicDrops;
  }

  public MythicDrops getMythicDrops() {
    return mythicDrops;
  }

  @EventHandler(priority = EventPriority.NORMAL)
  public void onRightClick(PlayerInteractEvent event) {
    if (event.getAction() != Action.RIGHT_CLICK_AIR
        && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      LOGGER.fine("event.getAction() != RIGHT_CLICK_AIR && event.getAction() != RIGHT_CLICK_BLOCK");
      return;
    }
    if (event.getHand() != EquipmentSlot.HAND) {
      LOGGER.fine("event.getHand() != EquipmentSlot.HAND");
      return;
    }
    Player player = event.getPlayer();
    if (player.getEquipment() == null) {
      LOGGER.fine("player.getEquipment() == null");
      return;
    }
    ItemStack itemInMainHand = player.getEquipment().getItemInMainHand();
    if (!player.hasPermission("mythicdrops.socket")) {
      LOGGER.fine("!player.hasPermission(\"mythicdrops.socket\")");
      return;
    }
    if (heldSocket.containsKey(player.getName())) {
      LOGGER.fine("heldSocket.containsKey(" + player.getName() + ")");
      socketItem(event, player, itemInMainHand);
      heldSocket.remove(player.getName());
      return;
    }
    LOGGER.fine("!heldSocket.containsKey(" + player.getName() + ")");
    addHeldSocket(event, player, itemInMainHand);
  }

  private void addHeldSocket(PlayerInteractEvent event, final Player player, ItemStack itemInHand) {
    if (!mythicDrops
        .getSockettingSettings()
        .getSocketGemMaterials()
        .contains(itemInHand.getType())) {
      LOGGER.fine("!socketGemMaterials.contains(itemInHand.getType())");
      return;
    }
    if (!itemInHand.hasItemMeta()) {
      LOGGER.fine("!itemInHand.hasItemMeta()");
      return;
    }
    ItemMeta im = itemInHand.getItemMeta();
    if (!im.hasDisplayName()) {
      LOGGER.fine("!im.hasDisplayName()");
      return;
    }
    String socketGemNameFormat =
        replaceArgs(
            mythicDrops.getSockettingSettings().getSocketGemName(),
            new String[][] {{"%socketgem%", ""}});
    String coloredSocketGemNameFormat =
        socketGemNameFormat.replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
    String strippedSocketGemNameFormat = ChatColor.stripColor(coloredSocketGemNameFormat);
    String strippedImDisplayName = ChatColor.stripColor(im.getDisplayName());
    String type =
        ChatColor.stripColor(strippedImDisplayName.replace(strippedSocketGemNameFormat, ""));
    if (type == null) {
      LOGGER.fine("type == null");
      return;
    }
    String socketGemNameFormatWithType =
        replaceArgs(
            mythicDrops.getSockettingSettings().getSocketGemName(),
            new String[][] {{"%socketgem%", type}});
    String coloredSocketGemNameFormatWithType =
        socketGemNameFormatWithType.replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
    String strippedSocketGemNameFormatWithType =
        ChatColor.stripColor(coloredSocketGemNameFormatWithType);
    if (!strippedSocketGemNameFormatWithType.equals(strippedImDisplayName)) {
      LOGGER.fine(
          "!strippedSocketGemNameFormatWithType.equals(strippedImDisplayName): "
              + "strippedSocketGemNameFormatWithType=\""
              + strippedSocketGemNameFormatWithType
              + "\" "
              + "strippedImDisplayName=\""
              + strippedImDisplayName
              + "\"");
      return;
    }
    SocketGem socketGem = SocketGemMap.INSTANCE.get(type);
    if (socketGem == null) {
      LOGGER.fine("socketGem == null 1");
      socketGem = SocketGemUtil.getSocketGemFromName(type);
      if (socketGem == null) {
        LOGGER.fine("socketGem == null 2");
        return;
      }
    }
    player.sendMessage(
        mythicDrops
            .getConfigSettings()
            .getFormattedLanguageString("command.socket-instructions", new String[][] {}));
    HeldItem hg = new HeldItem(socketGem.getName(), itemInHand);
    heldSocket.put(player.getName(), hg);
    Bukkit.getScheduler()
        .runTaskLaterAsynchronously(
            mythicDrops, () -> heldSocket.remove(player.getName()), 30 * 20L);
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

  private void socketItem(
      PlayerInteractEvent event, Player player, ItemStack itemInHand) {
      LOGGER.fine("ItemUtil.isArmor(itemType) || ItemUtil.isTool(itemType)");
      if (!itemInHand.hasItemMeta()) {
        LOGGER.fine("!itemInHand.hasItemMeta()");
        player.sendMessage(
            mythicDrops
                .getConfigSettings()
                .getFormattedLanguageString("command.socket-cannot-use", new String[][] {}));
        cancelDenyRemove(event, player);
        player.updateInventory();
        return;
      }
      ItemMeta im = itemInHand.getItemMeta();
      if (!im.hasLore()) {
        LOGGER.fine("!im.hasLore()");
        player.sendMessage(
            mythicDrops
                .getConfigSettings()
                .getFormattedLanguageString("command.socket-cannot-use", new String[][] {}));
        cancelDenyRemove(event, player);
        player.updateInventory();
        return;
      }
      List<String> lore = new ArrayList<>(im.getLore());
      String socketString =
          mythicDrops
              .getSockettingSettings()
              .getSockettedItemString()
              .replace('&', '\u00A7')
              .replace("\u00A7\u00A7", "&")
              .replace("%tiercolor%", "");
      int index = indexOfStripColor(lore, socketString);
      if (index < 0) {
        LOGGER.fine(String.format("index < 0: lore=[%s], socketString=\"%s\"", lore, socketString));
        player.sendMessage(
            mythicDrops
                .getConfigSettings()
                .getFormattedLanguageString("command.socket-cannot-use", new String[][] {}));
        cancelDenyRemove(event, player);
        player.updateInventory();
        return;
      }
      HeldItem heldSocket1 = heldSocket.get(player.getName());
      String socketGemType = ChatColor.stripColor(heldSocket1.getName());
      SocketGem socketGem = SocketGemUtil.getSocketGemFromName(socketGemType);
      if (socketGem == null || !socketGemTypeMatchesItemStack(socketGem, itemInHand)) {
        LOGGER.fine("socketGem == null || !socketGemTypeMatchesItemStack()");
        player.sendMessage(
            mythicDrops
                .getConfigSettings()
                .getFormattedLanguageString("command.socket-cannot-use", new String[][] {}));
        cancelDenyRemove(event, player);
        player.updateInventory();
        return;
      }

      if (!player.getInventory().contains(heldSocket1.getItemStack())) {
        LOGGER.fine("socketItem() - !player.getInventory().contains(heldSocket1.getItemStack())");
        player.sendMessage(
            mythicDrops
                .getConfigSettings()
                .getFormattedLanguageString("command.socket-do-not-have", new String[][] {}));
        cancelDenyRemove(event, player);
        player.updateInventory();
        return;
      }

      if (itemInHand.getAmount() > heldSocket1.getItemStack().getAmount()) {
        LOGGER.fine("itemInHand.getAmount() > heldSocket1.getItemStack().getAmount()");
        player.sendMessage(
            mythicDrops
                .getConfigSettings()
                .getFormattedLanguageString("command.socket-do-not-have", new String[][] {}));
        cancelDenyRemove(event, player);
        player.updateInventory();
        return;
      }

      Tier tier = TierUtil.getTierFromItemStack(itemInHand);

      ChatColor colorForSocketGemName;
      if (tier != null && mythicDrops.getSockettingSettings().isUseTierColorForSocketName()) {
        colorForSocketGemName = tier.getDisplayColor();
      } else {
        colorForSocketGemName =
            mythicDrops.getSockettingSettings().getDefaultSocketNameColorOnItems();
      }

      lore.set(index, colorForSocketGemName + socketGem.getName());

      List<String> colorCoded =
          StringListUtil.replaceArgs(
              StringListUtil.colorList(mythicDrops.getSockettingSettings().getSockettedItemLore()),
              new String[][] {{"%tiercolor%", tier != null ? tier.getDisplayColor() + "" : ""}});

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
          mythicDrops
              .getConfigSettings()
              .getFormattedLanguageString("command.socket-success", new String[][] {}));
      cancelDenyRemove(event, player);
      player.updateInventory();
  }

  private void cancelDenyRemove(PlayerInteractEvent event, Player player) {
    cancelResults(event);
    heldSocket.remove(player.getName());
  }

  private void cancelResults(PlayerInteractEvent event) {
    LOGGER.fine("cancelResults - cancelling results");
    event.setCancelled(true);
    event.setUseInteractedBlock(Event.Result.DENY);
    event.setUseItemInHand(Event.Result.DENY);
    event.getPlayer().updateInventory();
  }

  private boolean socketGemTypeMatchesItemStack(SocketGem socketGem, ItemStack itemStack) {
    List<ItemGroup> nonInverseItemGroups = socketGem.getItemGroups().stream().filter(itemGroup -> !itemGroup.isInverse()).collect(Collectors.toList());
    List<ItemGroup> inverseItemGroups = socketGem.getItemGroups().stream().filter(itemGroup -> itemGroup.isInverse()).collect(Collectors.toList());
    for (ItemGroup nonInverseItemGroup : nonInverseItemGroups) {
      if (!nonInverseItemGroup.getMaterials().contains(itemStack.getType())) {
        return false;
      }
    }
    for (ItemGroup inverseItemGroup : inverseItemGroups) {
      if (inverseItemGroup.getMaterials().contains(itemStack.getType())) {
        return false;
      }
    }
    return true;
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
    for (MythicEnchantment mythicEnchantment : socketGem.getEnchantments()) {
      int currentLevel =
          itemStackEnchantments.containsKey(mythicEnchantment.getEnchantment())
              ? itemStackEnchantments.get(mythicEnchantment.getEnchantment())
              : 0;
      currentLevel += mythicEnchantment.getRandomLevel();
      itemStackEnchantments.put(mythicEnchantment.getEnchantment(), currentLevel);
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
    List<String> socketGemSuffixes =
        SocketGemMap.INSTANCE.values().stream()
            .map(sg -> sg.getSuffix())
            .collect(Collectors.toList());
    if (mythicDrops.getSockettingSettings().isPreventMultipleChangesFromSockets()
            && ChatColor.stripColor(name).endsWith(suffix)
        || endsWithAnyFromList(ChatColor.stripColor(name), socketGemSuffixes)) {
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
    List<String> socketGemPrefixes =
        SocketGemMap.INSTANCE.values().stream()
            .map(sg -> sg.getPrefix())
            .collect(Collectors.toList());
    if (mythicDrops.getSockettingSettings().isPreventMultipleChangesFromSockets()
            && ChatColor.stripColor(name).contains(prefix)
        || startsWithAnyFromList(ChatColor.stripColor(name), socketGemPrefixes)) {
      return im;
    }
    im.setDisplayName(beginColor + prefix + " " + name);
    return im;
  }

  private boolean startsWithAnyFromList(String string, List<String> list) {
    for (String s : list) {
      if (string.toUpperCase().startsWith(s.toUpperCase())) {
        return true;
      }
    }
    return false;
  }

  private boolean endsWithAnyFromList(String string, List<String> list) {
    for (String s : list) {
      if (string.toUpperCase().endsWith(s.toUpperCase())) {
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

  public static class HeldItem {
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
