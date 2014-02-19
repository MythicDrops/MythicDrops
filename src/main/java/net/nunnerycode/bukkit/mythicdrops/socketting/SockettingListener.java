package net.nunnerycode.bukkit.mythicdrops.socketting;

import net.nunnerycode.bukkit.libraries.ivory.utils.StringListUtils;
import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.settings.SockettingSettings;
import net.nunnerycode.bukkit.mythicdrops.api.socketting.GemType;
import net.nunnerycode.bukkit.mythicdrops.api.socketting.SocketCommandRunner;
import net.nunnerycode.bukkit.mythicdrops.api.socketting.SocketEffect;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.utils.ItemUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.SocketGemUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.TierUtil;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SockettingListener implements Listener {

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
      return;
    }
    if (event.getItem() == null) {
      return;
    }
    Player player = event.getPlayer();
    ItemStack itemInHand = event.getItem();
    String itemType = ItemUtil.getItemTypeFromMaterial(itemInHand.getType());
    if (mythicDrops.getSockettingSettings().getSocketGemMaterials()
        .contains(itemInHand.getType())) {
      event.setUseItemInHand(Event.Result.DENY);
      player.updateInventory();
    }
    if (itemType != null && ItemUtil.isArmor(itemType) && itemInHand.hasItemMeta()) {
      event.setUseItemInHand(Event.Result.DENY);
      player.updateInventory();
    }
    if (heldSocket.containsKey(player.getName())) {
      socketItem(event, player, itemInHand, itemType);
      heldSocket.remove(player.getName());
    } else {
      addHeldSocket(event, player, itemInHand);
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
    } else if (d instanceof Projectile) {
      led = ((Projectile) d).getShooter();
    } else {
      return;
    }
    applyEffects(led, lee);
    runCommands(led, lee);
  }

  public void runCommands(LivingEntity attacker, LivingEntity defender) {
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
      if (ss.isUseAttackerItemInHand() && attacker.getEquipment().getItemInHand() != null) {
        List<SocketGem> attackerSocketGems = getSocketGems(attacker.getEquipment().getItemInHand());
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
      if (ss.isUseDefenderItemInHand() && defender.getEquipment().getItemInHand() != null) {
        List<SocketGem> defenderSocketGems = getSocketGems(defender.getEquipment().getItemInHand());
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

  public void applyEffects(LivingEntity attacker, LivingEntity defender) {
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
    if (ss.isUseAttackerItemInHand() && attacker.getEquipment().getItemInHand() != null) {
      List<SocketGem> attackerSocketGems = getSocketGems(attacker.getEquipment().getItemInHand());
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
    if (ss.isUseDefenderItemInHand() && defender.getEquipment().getItemInHand() != null) {
      List<SocketGem> defenderSocketGems = getSocketGems(defender.getEquipment().getItemInHand());
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

  private void addHeldSocket(PlayerInteractEvent event, final Player player, ItemStack itemInHand) {
    if (!mythicDrops.getSockettingSettings().getSocketGemMaterials()
        .contains(itemInHand.getType())) {
      return;
    }
    if (!itemInHand.hasItemMeta()) {
      return;
    }
    ItemMeta im = itemInHand.getItemMeta();
    if (!im.hasDisplayName()) {
      return;
    }
    String
        replacedArgs =
        ChatColor.stripColor(replaceArgs(mythicDrops.getSockettingSettings().getSocketGemName(),
                                         new String[][]{{"%socketgem%", ""}}).replace('&', '\u00A7')
                                 .replace("\u00A7\u00A7", "&"));
    String type = ChatColor.stripColor(im.getDisplayName().replace(replacedArgs, ""));
    if (type == null) {
      return;
    }
    SocketGem socketGem = mythicDrops.getSockettingSettings().getSocketGemMap().get(type);
    if (socketGem == null) {
      socketGem = SocketGemUtil.getSocketGemFromName(type);
      if (socketGem == null) {
        return;
      }
    }
    player.sendMessage(
        mythicDrops.getConfigSettings().getFormattedLanguageString("command.socket-instructions",
                                                                   new String[][]{}));
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
        player.sendMessage(
            mythicDrops.getConfigSettings().getFormattedLanguageString("command.socket-cannot-use",
                                                                       new String[][]{}));
        cancelDenyRemove(event, player);
        player.updateInventory();
        return;
      }
      ItemMeta im = itemInHand.getItemMeta();
      if (!im.hasLore()) {
        player.sendMessage(
            mythicDrops.getConfigSettings().getFormattedLanguageString("command.socket-cannot-use",
                                                                       new String[][]{}));
        cancelDenyRemove(event, player);
        player.updateInventory();
        return;
      }
      List<String> lore = new ArrayList<>(im.getLore());
      String
          socketString =
          mythicDrops.getSockettingSettings().getSockettedItemString().replace('&',
                                                                               '\u00A7')
              .replace("\u00A7\u00A7", "&").replace("%tiercolor%", "");
      int index = indexOfStripColor(lore, socketString);
      if (index < 0) {
        player.sendMessage(
            mythicDrops.getConfigSettings().getFormattedLanguageString("command.socket-cannot-use",
                                                                       new String[][]{}));
        cancelDenyRemove(event, player);
        player.updateInventory();
        return;
      }
      HeldItem heldSocket1 = heldSocket.get(player.getName());
      String socketGemType = ChatColor.stripColor(heldSocket1
                                                      .getName());
      SocketGem socketGem = SocketGemUtil.getSocketGemFromName(socketGemType);
      if (socketGem == null || !socketGemTypeMatchesItemStack(socketGem, itemInHand)) {
        player.sendMessage(
            mythicDrops.getConfigSettings().getFormattedLanguageString("command.socket-cannot-use",
                                                                       new String[][]{}));
        cancelDenyRemove(event, player);
        player.updateInventory();
        return;
      }

      if (!player.getInventory().contains(heldSocket1.getItemStack())) {
        player.sendMessage(
            mythicDrops.getConfigSettings().getFormattedLanguageString("command.socket-do-not-have",
                                                                       new String[][]{}));
        cancelDenyRemove(event, player);
        player.updateInventory();
        return;
      }

      if (itemInHand.getAmount() > heldSocket1.getItemStack().getAmount()) {
        player.sendMessage(
            mythicDrops.getConfigSettings().getFormattedLanguageString("command.socket-do-not-have",
                                                                       new String[][]{}));
        cancelDenyRemove(event, player);
        player.updateInventory();
        return;
      }

      Tier tier = TierUtil.getTierFromItemStack(itemInHand);

      ChatColor cc = tier != null ? tier.getDisplayColor() : ChatColor.GOLD;

      lore.set(index, cc + socketGem.getName());

      List<String> colorCoded = StringListUtils.colorList(mythicDrops.getSockettingSettings()
                                                              .getSockettedItemLore(), '\u00A7');

      lore = StringListUtils.removeIfMatches(lore, colorCoded);

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
      player.setItemInHand(itemInHand);
      player.sendMessage(
          mythicDrops.getConfigSettings().getFormattedLanguageString("command.socket-success",
                                                                     new String[][]{}));
      cancelDenyRemove(event, player);
      event.setCancelled(false);
      player.updateInventory();
    } else {
      player.sendMessage(
          mythicDrops.getConfigSettings().getFormattedLanguageString("command.socket-cannot-use",
                                                                     new String[][]{}));
      cancelDenyRemove(event, player);
      player.updateInventory();
    }
  }

  private void cancelDenyRemove(PlayerInteractEvent event, Player player) {
    event.setCancelled(true);
    event.setUseInteractedBlock(Event.Result.DENY);
    event.setUseItemInHand(Event.Result.DENY);
    heldSocket.remove(player.getName());
  }

  public boolean socketGemTypeMatchesItemStack(SocketGem socketGem, ItemStack itemStack) {
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

  public int indexOfStripColor(List<String> list, String string) {
    String[] array = list.toArray(new String[list.size()]);
    for (int i = 0; i < array.length; i++) {
      if (ChatColor.stripColor(array[i]).equalsIgnoreCase(ChatColor.stripColor(string))) {
        return i;
      }
    }
    return -1;
  }

  public int indexOfStripColor(String[] array, String string) {
    for (int i = 0; i < array.length; i++) {
      if (ChatColor.stripColor(array[i]).equalsIgnoreCase(ChatColor.stripColor(string))) {
        return i;
      }
    }
    return -1;
  }

  public ItemMeta loreItemStack(ItemMeta im, SocketGem socketGem) {
    if (!im.hasLore()) {
      im.setLore(new ArrayList<String>());
    }
    List<String> lore = new ArrayList<String>(im.getLore());
    if (lore.containsAll(socketGem.getLore())) {
      return im;
    }
    for (String s : socketGem.getLore()) {
      lore.add(s.replace('&', '\u00A7').replace("\u00A7\u00A7", "&"));
    }
    im.setLore(lore);
    return im;
  }

  public ItemMeta enchantmentItemStack(ItemMeta itemMeta, SocketGem socketGem) {
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

  public ItemMeta suffixItemStack(ItemMeta im, SocketGem socketGem) {
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

  public ItemMeta prefixItemStack(ItemMeta im, SocketGem socketGem) {
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

  public boolean containsAnyFromList(String string, List<String> list) {
    for (String s : list) {
      if (string.toUpperCase().contains(s.toUpperCase())) {
        return true;
      }
    }
    return false;
  }

  public ChatColor findColor(final String s) {
    char[] c = s.toCharArray();
    for (int i = 0; i < c.length; i++) {
      if (c[i] == (char) 167 && i + 1 < c.length) {
        return ChatColor.getByChar(c[i + 1]);
      }
    }
    return null;
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
