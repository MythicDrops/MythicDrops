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
package com.tealcube.minecraft.bukkit.mythicdrops.commands;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin;
import com.tealcube.minecraft.bukkit.mythicdrops.StringExtensionsKt;
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGenerationReason;
import com.tealcube.minecraft.bukkit.mythicdrops.api.locations.Vec3;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import com.tealcube.minecraft.bukkit.mythicdrops.identification.IdentityTome;
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicCustomItem;
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory;
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.SocketItem;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.EntityUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.GsonUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemStackUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.SocketGemUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.StringListUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TierUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import kotlin.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import se.ranzdo.bukkit.methodcommand.Arg;
import se.ranzdo.bukkit.methodcommand.Command;
import se.ranzdo.bukkit.methodcommand.FlagArg;
import se.ranzdo.bukkit.methodcommand.Flags;
import se.ranzdo.bukkit.methodcommand.Wildcard;

public final class MythicDropsCommand {

  private static final Logger LOGGER =
      JulLoggerFactory.INSTANCE.getLogger(MythicDropsCommand.class);
  private MythicDrops plugin;

  public MythicDropsCommand(MythicDropsPlugin plugin) {
    this.plugin = plugin;
  }

  @Command(
      identifier = "mythicdrops debug",
      description = "Prints a bunch of debug messages",
      permissions = "mythicdrops.command.debug")
  public void debugCommand(CommandSender sender) {
    LOGGER.info("server package: " + Bukkit.getServer().getClass().getPackage().toString());
    LOGGER.info("number of tiers: " + plugin.getTierManager().get().size());
    LOGGER.info("number of custom items: " + plugin.getCustomItemManager().get().size());
    LOGGER.info(
        "config settings: "
            + GsonUtil.INSTANCE.toJson(this.plugin.getSettingsManager().getConfigSettings()));
    LOGGER.info(
        "creature spawning settings: "
            + GsonUtil.INSTANCE.toJson(
                this.plugin.getSettingsManager().getCreatureSpawningSettings()));
    sender.sendMessage(
        StringExtensionsKt.chatColorize(
            plugin.getSettingsManager().getLanguageSettings().getCommand().getDebug()));
  }

  @Command(
      identifier = "mythicdrops reload",
      description = "Reloads the configuration files",
      permissions = "mythicdrops.command.reload")
  public void reloadCommand(CommandSender sender) {
    LOGGER.info("Reloading the configuration files");
    plugin.reloadConfigurationFiles();
    plugin.reloadSettings();
    // Lord help us all
    plugin.reloadItemGroups();
    plugin.reloadTiers();
    plugin.reloadNames();
    plugin.reloadCustomItems();
    plugin.reloadRepairCosts();
    plugin.reloadSocketGems();
    plugin.reloadRelations();
    plugin.reloadSocketGemCombiners();
    LOGGER.info("Done reloading the configuration files");
    sender.sendMessage(
        StringExtensionsKt.chatColorize(
            plugin.getSettingsManager().getLanguageSettings().getCommand().getReloadConfig()));
  }

  @Command(
      identifier = "mythicdrops spawn",
      description = "Spawns in MythicDrops items",
      permissions = "mythicdrops.command.spawn")
  @Flags(
      identifier = {"a", "t", "mind", "maxd"},
      description = {
        "Amount to spawn",
        "Tier to spawn",
        "Minimum durability",
        "Maximum durability"
      })
  public void spawnSubcommand(
      CommandSender sender,
      @Arg(name = "amount", def = "1") @FlagArg("a") int amount,
      @Arg(name = "tier", def = "*") @FlagArg("t") String tierName,
      @Arg(name = "mindurability", def = "1.0", verifiers = "min[0.0]|max[1.0]") @FlagArg("mind")
          double minDura,
      @Arg(name = "maxdurability", def = "1.0", verifiers = "min[0.0]|max[1.0]") @FlagArg("maxd")
          double maxDura) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin.getSettingsManager().getLanguageSettings().getCommand().getOnlyPlayers()));
      return;
    }

    Player player = (Player) sender;
    if (tierName.equalsIgnoreCase("*")
        && !player.hasPermission("mythicdrops.command.spawn.wildcard")) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin.getSettingsManager().getLanguageSettings().getCommand().getNoAccess()));
      return;
    }
    if (plugin.getTierManager().get().size() <= 0) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              StringExtensionsKt.replaceArgs(
                  plugin.getSettingsManager().getLanguageSettings().getCommand().getReloadConfig(),
                  new Pair<>("%amount%", String.valueOf(amount)))));
      return;
    }

    Tier tier = TierUtil.INSTANCE.getTier(tierName);

    if (!player.hasPermission("mythicdrops.command.spawn.wildcard")) {
      if (tier == null) {
        sender.sendMessage(
            StringExtensionsKt.chatColorize(
                plugin
                    .getSettingsManager()
                    .getLanguageSettings()
                    .getCommand()
                    .getTierDoesNotExist()));
        return;
      } else if (!player.hasPermission("mythicdrops.command.spawn." + tier.getName())) {
        sender.sendMessage(
            StringExtensionsKt.chatColorize(
                plugin.getSettingsManager().getLanguageSettings().getCommand().getNoAccess()));
        return;
      }
    }

    int amountGiven = 0;
    while (amountGiven < amount) {
      ItemStack mis =
          MythicDropsPlugin.getNewDropBuilder()
              .useDurability(false)
              .withItemGenerationReason(ItemGenerationReason.COMMAND)
              .withTier(tier)
              .build();
      if (mis != null) {
        ItemStackUtil.setDurabilityForItemStack(mis, minDura, maxDura);
        player.getInventory().addItem(mis);
        amountGiven++;
      }
    }

    player.sendMessage(
        StringExtensionsKt.chatColorize(
            StringExtensionsKt.replaceArgs(
                plugin
                    .getSettingsManager()
                    .getLanguageSettings()
                    .getCommand()
                    .getSpawnRandom()
                    .getSuccess(),
                new Pair<>("%amount%", String.valueOf(amountGiven)))));
  }

  @Command(
      identifier = "mythicdrops drop",
      description = "Drops in MythicDrops items",
      permissions = "mythicdrops.command.drop")
  @Flags(
      identifier = {"a", "t", "w", "m", "mind", "maxd"},
      description = {
        "Amount to drop",
        "Tier to drop",
        "World",
        "Minimum durability",
        "Maximum durability"
      })
  public void dropSubcommand(
      CommandSender sender,
      @Arg(name = "amount", def = "1") @FlagArg("a") int amount,
      @Arg(name = "tier", def = "*") @FlagArg("t") String tierName,
      @Arg(name = "world", def = "") @FlagArg("w") String worldName,
      @Arg(name = "x") double x,
      @Arg(name = "y") double y,
      @Arg(name = "z") double z,
      @Arg(name = "mindurability", def = "1.0", verifiers = "min[0.0]|max[1.0]") @FlagArg("mind")
          double minDura,
      @Arg(name = "maxdurability", def = "1.0", verifiers = "min[0.0]|max[1.0]") @FlagArg("maxd")
          double maxDura,
      @Arg(name = "material", def = "*") @FlagArg("m") String materialName) {
    if (!(sender instanceof Player) && "".equals(worldName)) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin.getSettingsManager().getLanguageSettings().getCommand().getOnlyPlayers()));
      return;
    }

    if (tierName.equalsIgnoreCase("*")
        && !sender.hasPermission("mythicdrops.command.spawn.wildcard")) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin.getSettingsManager().getLanguageSettings().getCommand().getNoAccess()));
      return;
    }

    String worldN = sender instanceof Player ? ((Player) sender).getWorld().getName() : worldName;

    if (plugin.getTierManager().get().size() <= 0) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              StringExtensionsKt.replaceArgs(
                  plugin
                      .getSettingsManager()
                      .getLanguageSettings()
                      .getCommand()
                      .getDropRandom()
                      .getFailure(),
                  new Pair<>("%amount%", String.valueOf(amount)))));
      return;
    }

    Tier tier = TierUtil.INSTANCE.getTier(tierName);

    if (!sender.hasPermission("mythicdrops.command.spawn.wildcard")) {
      if (tier == null) {
        sender.sendMessage(
            StringExtensionsKt.chatColorize(
                plugin
                    .getSettingsManager()
                    .getLanguageSettings()
                    .getCommand()
                    .getTierDoesNotExist()));
        return;
      } else if (!sender.hasPermission("mythicdrops.command.spawn." + tier.getName())) {
        sender.sendMessage(
            StringExtensionsKt.chatColorize(
                plugin.getSettingsManager().getLanguageSettings().getCommand().getNoAccess()));
        return;
      }
    }

    Material material;
    if (materialName.equalsIgnoreCase("*")) {
      material = ItemUtil.getRandomMaterialFromCollection(ItemUtil.getMaterialsFromTier(tier));
    } else {
      material = Material.getMaterial(materialName);
    }

    World w = Bukkit.getWorld(worldN);
    if (w == null) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin
                  .getSettingsManager()
                  .getLanguageSettings()
                  .getCommand()
                  .getWorldDoesNotExist()));
      return;
    }
    Location l = new Location(w, x, y, z);
    Entity e = EntityUtil.getEntityAtLocation(l);

    int amountGiven = 0;
    while (amountGiven < amount) {
      ItemStack mis =
          MythicDropsPlugin.getNewDropBuilder()
              .useDurability(false)
              .withItemGenerationReason(ItemGenerationReason.COMMAND)
              .withTier(tier)
              .withMaterial(material)
              .build();
      if (mis != null) {
        ItemStackUtil.setDurabilityForItemStack(mis, minDura, maxDura);
        if (e instanceof InventoryHolder) {
          ((InventoryHolder) e).getInventory().addItem(mis);
        } else if (l.getBlock().getState() instanceof InventoryHolder) {
          ((InventoryHolder) l.getBlock().getState()).getInventory().addItem(mis);
        } else {
          w.dropItem(l, mis);
        }
        amountGiven++;
      }
    }

    sender.sendMessage(
        StringExtensionsKt.chatColorize(
            StringExtensionsKt.replaceArgs(
                plugin
                    .getSettingsManager()
                    .getLanguageSettings()
                    .getCommand()
                    .getDropRandom()
                    .getSuccess(),
                new Pair<>("%amount%", String.valueOf(amountGiven)))));
  }

  @Command(
      identifier = "mythicdrops give",
      description = "Gives MythicDrops items",
      permissions = "mythicdrops.command.give")
  @Flags(
      identifier = {"a", "t", "mind", "maxd"},
      description = {
        "Amount to spawn",
        "Tier to spawn",
        "Minimum durability",
        "Maximum durability"
      })
  public void giveSubcommand(
      CommandSender sender,
      @Arg(name = "player") Player player,
      @Arg(name = "amount", def = "1") @FlagArg("a") int amount,
      @Arg(name = "tier", def = "*") @FlagArg("t") String tierName,
      @Arg(name = "mindurability", def = "1.0", verifiers = "min[0.0]|max[1.0]") @FlagArg("mind")
          double minDura,
      @Arg(name = "maxdurability", def = "1.0", verifiers = "min[0.0]|max[1.0]") @FlagArg("maxd")
          double maxDura) {
    if (tierName.equalsIgnoreCase("*")
        && !sender.hasPermission("mythicdrops.command.give.wildcard")) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin.getSettingsManager().getLanguageSettings().getCommand().getNoAccess()));
      return;
    }
    if (plugin.getTierManager().get().size() <= 0) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              StringExtensionsKt.replaceArgs(
                  plugin
                      .getSettingsManager()
                      .getLanguageSettings()
                      .getCommand()
                      .getGiveRandom()
                      .getSenderFailure(),
                  new Pair("%amount%", String.valueOf(amount)),
                  new Pair("%receiver%", player.getName()))));
      return;
    }

    Tier tier = TierUtil.INSTANCE.getTier(tierName);

    if (!sender.hasPermission("mythicdrops.command.give.wildcard")) {
      if (tier == null) {
        sender.sendMessage(
            StringExtensionsKt.chatColorize(
                plugin
                    .getSettingsManager()
                    .getLanguageSettings()
                    .getCommand()
                    .getTierDoesNotExist()));
        return;
      } else if (!sender.hasPermission("mythicdrops.command.give." + tier.getName())) {
        sender.sendMessage(
            StringExtensionsKt.chatColorize(
                plugin.getSettingsManager().getLanguageSettings().getCommand().getNoAccess()));
        return;
      }
    }

    int amountGiven = 0;
    while (amountGiven < amount) {
      ItemStack mis =
          MythicDropsPlugin.getNewDropBuilder()
              .useDurability(true)
              .withItemGenerationReason(ItemGenerationReason.COMMAND)
              .withTier(tier)
              .build();
      if (mis != null) {
        ItemStackUtil.setDurabilityForItemStack(mis, minDura, maxDura);
        player.getInventory().addItem(mis);
        amountGiven++;
      }
    }

    player.sendMessage(
        StringExtensionsKt.chatColorize(
            StringExtensionsKt.replaceArgs(
                plugin
                    .getSettingsManager()
                    .getLanguageSettings()
                    .getCommand()
                    .getGiveRandom()
                    .getReceiverSuccess(),
                new Pair<>("%amount%", String.valueOf(amountGiven)))));
    sender.sendMessage(
        StringExtensionsKt.chatColorize(
            StringExtensionsKt.replaceArgs(
                plugin
                    .getSettingsManager()
                    .getLanguageSettings()
                    .getCommand()
                    .getGiveRandom()
                    .getSenderSuccess(),
                new Pair<>("%amount%", String.valueOf(amountGiven)),
                new Pair<>("%receiver%", player.getName()))));
  }

  @Command(
      identifier = "mythicdrops customcreate",
      description = "Creates a custom item from the item in the " + "user's hand",
      permissions = "mythicdrops.command.customcreate")
  public void customCreateSubcommand(
      CommandSender sender,
      @Arg(name = "chance to spawn") double chanceToSpawn,
      @Arg(name = "chance to drop") double chanceToDrop) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin.getSettingsManager().getLanguageSettings().getCommand().getNoAccess()));
      return;
    }
    Player p = (Player) sender;
    if (p.getEquipment() == null) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin
                  .getSettingsManager()
                  .getLanguageSettings()
                  .getCommand()
                  .getCustomCreate()
                  .getFailure()));
      return;
    }
    ItemStack itemInHand = p.getEquipment().getItemInMainHand();
    if (!itemInHand.hasItemMeta()) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin
                  .getSettingsManager()
                  .getLanguageSettings()
                  .getCommand()
                  .getCustomCreate()
                  .getFailure()));
      return;
    }
    ItemMeta im = itemInHand.getItemMeta();
    if (im == null || !im.hasDisplayName() || !im.hasLore()) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin
                  .getSettingsManager()
                  .getLanguageSettings()
                  .getCommand()
                  .getCustomCreate()
                  .getFailure()));
      return;
    }
    String name;
    if (im.hasDisplayName()) {
      name = ChatColor.stripColor(im.getDisplayName()).replaceAll("\\s+", "");
    } else {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin
                  .getSettingsManager()
                  .getLanguageSettings()
                  .getCommand()
                  .getCustomCreate()
                  .getFailure()));
      return;
    }
    List<String> itemLore = new ArrayList<>();
    if (im.hasLore()) {
      itemLore = im.getLore();
    }

    List<String> lore = new ArrayList<>();
    for (String s : itemLore) {
      lore.add(s.replace('\u00A7', '&'));
    }

    Map<Enchantment, Integer> enchantments = new HashMap<>();
    if (im.hasEnchants()) {
      enchantments = im.getEnchants();
    }
    CustomItem ci = MythicCustomItem.fromItemStack(itemInHand, name, chanceToDrop, chanceToSpawn);
    plugin.getCustomItemManager().add(ci);
    sender.sendMessage(
        StringExtensionsKt.chatColorize(
            StringExtensionsKt.replaceArgs(
                plugin
                    .getSettingsManager()
                    .getLanguageSettings()
                    .getCommand()
                    .getCustomCreate()
                    .getSuccess(),
                new Pair("%name%", name))));

    plugin.getCustomItemYAML().set(name + ".display-name", ci.getDisplayName());
    plugin.getCustomItemYAML().set(name + ".lore", ci.getLore());
    plugin.getCustomItemYAML().set(name + ".weight", ci.getWeight());
    plugin
        .getCustomItemYAML()
        .set(name + ".chance-to-drop-on-monster-death", ci.getChanceToDropOnDeath());
    plugin.getCustomItemYAML().set(name + ".material-name", ci.getMaterial().name());
    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
      plugin
          .getCustomItemYAML()
          .set(name + ".enchantments." + entry.getKey().getName(), entry.getValue());
    }
    plugin.getCustomItemYAML().save();
  }

  @Command(
      identifier = "mythicdrops custom",
      description = "Gives custom MythicDrops items",
      permissions = "mythicdrops.command.custom")
  @Flags(
      identifier = {"a", "c", "mind", "maxd"},
      description = {
        "Amount to spawn",
        "Custom Item to spawn",
        "Minimum durability",
        "Maximum durability"
      })
  public void customSubcommand(
      CommandSender sender,
      @Arg(name = "player", def = "self") String playerName,
      @Arg(name = "amount", def = "1") @FlagArg("a") int amount,
      @Arg(name = "item", def = "*") @FlagArg("c") String itemName,
      @Arg(name = "mindurability", def = "1.0", verifiers = "min[0.0]|max[1.0]") @FlagArg("mind")
          double minDura,
      @Arg(name = "maxdurability", def = "1.0", verifiers = "min[0.0]|max[1.0]") @FlagArg("maxd")
          double maxDura) {
    Player player;
    if (playerName.equalsIgnoreCase("self")) {
      if (sender instanceof Player) {
        player = (Player) sender;
      } else {
        sender.sendMessage(
            StringExtensionsKt.chatColorize(
                plugin.getSettingsManager().getLanguageSettings().getCommand().getNoAccess()));
        return;
      }
    } else {
      player = Bukkit.getPlayer(playerName);
    }
    if (player == null) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin
                  .getSettingsManager()
                  .getLanguageSettings()
                  .getCommand()
                  .getPlayerDoesNotExist()));
      return;
    }
    CustomItem customItem = null;
    if (!itemName.equalsIgnoreCase("*")) {
      customItem = plugin.getCustomItemManager().getById(itemName);
      if (customItem == null) {
        sender.sendMessage(
            StringExtensionsKt.chatColorize(
                plugin
                    .getSettingsManager()
                    .getLanguageSettings()
                    .getCommand()
                    .getCustomItemDoesNotExist()));
        return;
      }
    }
    int amountGiven = 0;
    for (int i = 0; i < amount; i++) {
      try {
        ItemStack itemStack = null;
        boolean hasDurability = false;
        if (customItem == null) {
          CustomItem ci = plugin.getCustomItemManager().randomByWeight();
          if (ci != null) {
            itemStack = ci.toItemStack();
            hasDurability = ci.getHasDurability();
          }
        } else {
          itemStack = customItem.toItemStack();
          hasDurability = customItem.getHasDurability();
        }
        if (itemStack == null) {
          continue;
        }
        if (!hasDurability && itemStack.getItemMeta() instanceof Damageable) {
          ((Damageable) itemStack.getItemMeta())
              .setDamage(
                  ItemStackUtil.getDurabilityForMaterial(itemStack.getType(), minDura, maxDura));
        }
        player.getInventory().addItem(itemStack);
        amountGiven++;
      } catch (Exception ignored) {
      }
    }

    player.sendMessage(
        StringExtensionsKt.chatColorize(
            StringExtensionsKt.replaceArgs(
                plugin
                    .getSettingsManager()
                    .getLanguageSettings()
                    .getCommand()
                    .getGiveCustom()
                    .getReceiverSuccess(),
                new Pair<>("%amount%", String.valueOf(amountGiven)))));
    if (!player.equals(sender)) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              StringExtensionsKt.replaceArgs(
                  plugin
                      .getSettingsManager()
                      .getLanguageSettings()
                      .getCommand()
                      .getGiveCustom()
                      .getSenderSuccess(),
                  new Pair<>("%amount%", String.valueOf(amountGiven)),
                  new Pair<>("%receiver%", player.getName()))));
    }
  }

  @Command(
      identifier = "mythicdrops gem",
      description = "Gives MythicDrops gems",
      permissions = "mythicdrops.command.gem")
  @Flags(
      identifier = {"a", "g"},
      description = {"Amount to spawn", "Socket Gem to spawn"})
  public void gemSubcommand(
      CommandSender sender,
      @Arg(name = "player", def = "self") String playerName,
      @Arg(name = "amount", def = "1") @FlagArg("a") int amount,
      @Arg(name = "item", def = "*") @FlagArg("g") String itemName) {
    Player player;
    if (playerName.equalsIgnoreCase("self")) {
      if (sender instanceof Player) {
        player = (Player) sender;
      } else {
        sender.sendMessage(
            StringExtensionsKt.chatColorize(
                plugin.getSettingsManager().getLanguageSettings().getCommand().getNoAccess()));
        return;
      }
    } else {
      player = Bukkit.getPlayer(playerName);
    }
    if (player == null) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin
                  .getSettingsManager()
                  .getLanguageSettings()
                  .getCommand()
                  .getPlayerDoesNotExist()));
      return;
    }
    SocketGem socketGem = null;
    if (!itemName.equalsIgnoreCase("*")) {
      try {
        socketGem = SocketGemUtil.getSocketGemFromName(itemName);
      } catch (NullPointerException e) {
        sender.sendMessage(
            StringExtensionsKt.chatColorize(
                plugin
                    .getSettingsManager()
                    .getLanguageSettings()
                    .getCommand()
                    .getSocketGemDoesNotExist()));
        return;
      }
    }
    int amountGiven = 0;
    for (int i = 0; i < amount; i++) {
      try {
        ItemStack itemStack;
        if (socketGem == null) {
          itemStack =
              new SocketItem(
                  SocketGemUtil.getRandomSocketGemMaterial(),
                  SocketGemUtil.getRandomSocketGemWithChance(),
                  plugin.getSettingsManager().getSocketingSettings().getItems().getSocketGem());
        } else {
          itemStack =
              new SocketItem(
                  SocketGemUtil.getRandomSocketGemMaterial(),
                  socketGem,
                  plugin.getSettingsManager().getSocketingSettings().getItems().getSocketGem());
        }
        itemStack.setDurability((short) 0);
        player.getInventory().addItem(itemStack);
        amountGiven++;
      } catch (Exception ignored) {
        ignored.printStackTrace();
      }
    }

    player.sendMessage(
        StringExtensionsKt.chatColorize(
            StringExtensionsKt.replaceArgs(
                plugin
                    .getSettingsManager()
                    .getLanguageSettings()
                    .getCommand()
                    .getGiveGem()
                    .getReceiverSuccess(),
                new Pair<>("%amount%", String.valueOf(amountGiven)))));
    if (!player.equals(sender)) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              StringExtensionsKt.replaceArgs(
                  plugin
                      .getSettingsManager()
                      .getLanguageSettings()
                      .getCommand()
                      .getGiveGem()
                      .getSenderSuccess(),
                  new Pair<>("%amount%", String.valueOf(amountGiven)),
                  new Pair<>("%receiver%", player.getName()))));
    }
  }

  @Command(
      identifier = "mythicdrops unidentified",
      description = "Gives Unidentified Item",
      permissions = "mythicdrops.command.unidentified")
  @Flags(
      identifier = {"a"},
      description = {"Amount to spawn"})
  public void unidentifiedSubcommand(
      CommandSender sender,
      @Arg(name = "player", def = "self") String playerName,
      @Arg(name = "amount", def = "1") @FlagArg("a") int amount) {
    Player player;
    if (playerName.equalsIgnoreCase("self")) {
      if (sender instanceof Player) {
        player = (Player) sender;
      } else {
        sender.sendMessage(
            StringExtensionsKt.chatColorize(
                plugin.getSettingsManager().getLanguageSettings().getCommand().getNoAccess()));
        return;
      }
    } else {
      player = Bukkit.getPlayer(playerName);
    }
    if (player == null) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin
                  .getSettingsManager()
                  .getLanguageSettings()
                  .getCommand()
                  .getPlayerDoesNotExist()));
      return;
    }
    int amountGiven = 0;
    for (int i = 0; i < amount; i++) {
      Tier t = plugin.getTierManager().randomByWeight();
      if (t == null) {
        continue;
      }
      Collection<Material> materials = ItemUtil.getMaterialsFromTier(t);
      Material material = ItemUtil.getRandomMaterialFromCollection(materials);
      // TODO: FIX UNIDENTIFIED ITEM
      //      player
      //          .getInventory()
      //          .addItem(new UnidentifiedItem(material, plugin.getIdentifyingSettings()));
      amountGiven++;
    }
    player.sendMessage(
        StringExtensionsKt.chatColorize(
            StringExtensionsKt.replaceArgs(
                plugin
                    .getSettingsManager()
                    .getLanguageSettings()
                    .getCommand()
                    .getGiveUnidentified()
                    .getReceiverSuccess(),
                new Pair<>("%amount%", String.valueOf(amountGiven)))));
    if (!player.equals(sender)) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              StringExtensionsKt.replaceArgs(
                  plugin
                      .getSettingsManager()
                      .getLanguageSettings()
                      .getCommand()
                      .getGiveUnidentified()
                      .getSenderSuccess(),
                  new Pair<>("%amount%", String.valueOf(amountGiven)),
                  new Pair<>("%receiver%", player.getName()))));
    }
  }

  @Command(
      identifier = "mythicdrops tome",
      description = "Gives Identity Tome",
      permissions = "mythicdrops.command.tome")
  @Flags(
      identifier = {"a"},
      description = {"Amount to spawn"})
  public void tomeSubcommand(
      CommandSender sender,
      @Arg(name = "player", def = "self") String playerName,
      @Arg(name = "amount", def = "1") @FlagArg("a") int amount) {
    Player player;
    if (playerName.equalsIgnoreCase("self")) {
      if (sender instanceof Player) {
        player = (Player) sender;
      } else {
        sender.sendMessage(
            StringExtensionsKt.chatColorize(
                plugin.getSettingsManager().getLanguageSettings().getCommand().getNoAccess()));
        return;
      }
    } else {
      player = Bukkit.getPlayer(playerName);
    }
    if (player == null) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin
                  .getSettingsManager()
                  .getLanguageSettings()
                  .getCommand()
                  .getPlayerDoesNotExist()));
      return;
    }
    int amountGiven = 0;
    for (int i = 0; i < amount; i++) {
      player
          .getInventory()
          .addItem(
              new IdentityTome(
                  plugin
                      .getSettingsManager()
                      .getIdentifyingSettings()
                      .getItems()
                      .getIdentityTome()));
      amountGiven++;
    }
    player.sendMessage(
        StringExtensionsKt.chatColorize(
            StringExtensionsKt.replaceArgs(
                plugin
                    .getSettingsManager()
                    .getLanguageSettings()
                    .getCommand()
                    .getGiveTome()
                    .getReceiverSuccess(),
                new Pair<>("%amount%", String.valueOf(amountGiven)))));
    if (!player.equals(sender)) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              StringExtensionsKt.replaceArgs(
                  plugin
                      .getSettingsManager()
                      .getLanguageSettings()
                      .getCommand()
                      .getGiveTome()
                      .getSenderSuccess(),
                  new Pair<>("%amount%", String.valueOf(amountGiven)),
                  new Pair<>("%receiver%", player.getName()))));
    }
  }

  @Command(
      identifier = "mythicdrops tiers",
      description = "Lists all Tiers",
      permissions = "mythicdrops.command.tiers")
  public void tiersCommand(CommandSender sender) {
    List<String> loadedTierNames = new ArrayList<>();
    for (Tier t : plugin.getTierManager().get()) {
      loadedTierNames.add(t.getName());
    }
    String joinedTierNames = Joiner.on(", ").join(loadedTierNames);
    sender.sendMessage(
        StringExtensionsKt.chatColorize(
            StringExtensionsKt.replaceArgs(
                plugin.getSettingsManager().getLanguageSettings().getCommand().getTierList(),
                new Pair<>("%tier%", joinedTierNames))));
  }

  @Command(
      identifier = "mythicdrops modify name",
      description = "Adds a name to the item in hand",
      permissions = "mythicdrops.command.modify.name")
  public void modifyNameCommand(
      CommandSender sender, @Wildcard @Arg(name = "item name") String name) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin.getSettingsManager().getLanguageSettings().getCommand().getNoAccess()));
      return;
    }
    Player p = (Player) sender;
    ItemStack itemInHand = p.getEquipment().getItemInMainHand();
    if (itemInHand.getType() == Material.AIR) {
      p.sendMessage(
          plugin.getSettingsManager().getLanguageSettings().getCommand().getModify().getFailure());
      return;
    }
    String newName = name.replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
    ItemMeta im =
        itemInHand.hasItemMeta()
            ? itemInHand.getItemMeta()
            : Bukkit.getItemFactory().getItemMeta(itemInHand.getType());
    im.setDisplayName(newName);
    itemInHand.setItemMeta(im);
    p.sendMessage(
        plugin.getSettingsManager().getLanguageSettings().getCommand().getModify().getName());
  }

  @Command(
      identifier = "mythicdrops modify lore add",
      description = "Adds a line of lore to the item in hand",
      permissions = "mythicdrops.command.modify.lore")
  public void modifyLoreAddCommand(
      CommandSender sender, @Wildcard @Arg(name = "lore line") String line) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin.getSettingsManager().getLanguageSettings().getCommand().getNoAccess()));
      return;
    }
    Player p = (Player) sender;
    ItemStack itemInHand = p.getEquipment().getItemInMainHand();
    if (itemInHand.getType() == Material.AIR) {
      p.sendMessage(
          plugin.getSettingsManager().getLanguageSettings().getCommand().getModify().getFailure());
      return;
    }
    String newLine = line.replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
    ItemMeta im =
        itemInHand.hasItemMeta()
            ? itemInHand.getItemMeta()
            : Bukkit.getItemFactory().getItemMeta(itemInHand.getType());
    List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<String>();
    lore.add(newLine);
    im.setLore(lore);
    itemInHand.setItemMeta(im);
    p.sendMessage(
        plugin
            .getSettingsManager()
            .getLanguageSettings()
            .getCommand()
            .getModify()
            .getLore()
            .getAdd());
  }

  @Command(
      identifier = "mythicdrops modify lore remove",
      description = "Removes a line of lore to the item in hand",
      permissions = "mythicdrops.command.modify.lore")
  public void modifyLoreAddCommand(
      CommandSender sender, @Arg(name = "line to remove") int lineNumber) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin.getSettingsManager().getLanguageSettings().getCommand().getNoAccess()));
      return;
    }
    Player p = (Player) sender;
    ItemStack itemInHand = p.getEquipment().getItemInMainHand();
    if (itemInHand.getType() == Material.AIR) {
      p.sendMessage(
          plugin.getSettingsManager().getLanguageSettings().getCommand().getModify().getFailure());
      return;
    }
    ItemMeta im =
        itemInHand.hasItemMeta()
            ? itemInHand.getItemMeta()
            : Bukkit.getItemFactory().getItemMeta(itemInHand.getType());
    List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<String>();
    lore.remove(Math.max(Math.min(lineNumber - 1, lore.size()), 0));
    im.setLore(lore);
    itemInHand.setItemMeta(im);
    p.sendMessage(
        plugin
            .getSettingsManager()
            .getLanguageSettings()
            .getCommand()
            .getModify()
            .getLore()
            .getRemove());
  }

  @Command(
      identifier = "mythicdrops modify lore insert",
      description = "Adds a line of lore to the item in hand",
      permissions = "mythicdrops.command.modify.lore")
  public void modifyLoreAddCommand(
      CommandSender sender,
      @Arg(name = "index") int index,
      @Wildcard @Arg(name = "lore line") String line) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin.getSettingsManager().getLanguageSettings().getCommand().getNoAccess()));
      return;
    }
    Player p = (Player) sender;
    ItemStack itemInHand = p.getEquipment().getItemInMainHand();
    if (itemInHand.getType() == Material.AIR) {
      p.sendMessage(
          plugin.getSettingsManager().getLanguageSettings().getCommand().getModify().getFailure());
      return;
    }
    String newLine = line.replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
    ItemMeta im =
        itemInHand.hasItemMeta()
            ? itemInHand.getItemMeta()
            : Bukkit.getItemFactory().getItemMeta(itemInHand.getType());
    List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<String>();
    lore = StringListUtil.addString(lore, index, newLine, false);
    im.setLore(lore);
    itemInHand.setItemMeta(im);
    p.sendMessage(
        plugin
            .getSettingsManager()
            .getLanguageSettings()
            .getCommand()
            .getModify()
            .getLore()
            .getInsert());
  }

  @Command(
      identifier = "mythicdrops modify lore set",
      description = "Modifies a line of lore to the item in " + "hand",
      permissions = "mythicdrops.command.modify.lore")
  public void modifyLoreSetCommand(
      CommandSender sender,
      @Arg(name = "index") int index,
      @Wildcard @Arg(name = "lore line") String line) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin.getSettingsManager().getLanguageSettings().getCommand().getNoAccess()));
      return;
    }
    Player p = (Player) sender;
    ItemStack itemInHand = p.getEquipment().getItemInMainHand();
    if (itemInHand.getType() == Material.AIR) {
      p.sendMessage(
          plugin.getSettingsManager().getLanguageSettings().getCommand().getModify().getFailure());
      return;
    }
    String newLine = line.replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
    ItemMeta im =
        itemInHand.hasItemMeta()
            ? itemInHand.getItemMeta()
            : Bukkit.getItemFactory().getItemMeta(itemInHand.getType());
    List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<String>();
    if (lore.size() >= index) {
      lore.set(index, newLine);
    } else {
      lore = StringListUtil.addString(lore, index, newLine, false);
    }
    im.setLore(lore);
    itemInHand.setItemMeta(im);
    p.sendMessage(
        plugin
            .getSettingsManager()
            .getLanguageSettings()
            .getCommand()
            .getModify()
            .getLore()
            .getSet());
  }

  @Command(
      identifier = "mythicdrops modify enchantment add",
      description = "Adds an enchantment to the item in " + "hand",
      permissions = "mythicdrops.command.modify.enchantments")
  public void modifyEnchantmentAddCommand(
      CommandSender sender,
      @Arg(name = "enchantment") Enchantment enchantment,
      @Arg(name = "level", def = "1") int level) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin.getSettingsManager().getLanguageSettings().getCommand().getNoAccess()));
      return;
    }
    Player p = (Player) sender;
    ItemStack itemInHand = p.getEquipment().getItemInMainHand();
    if (itemInHand.getType() == Material.AIR) {
      p.sendMessage(
          plugin.getSettingsManager().getLanguageSettings().getCommand().getModify().getFailure());
      return;
    }
    itemInHand.addUnsafeEnchantment(enchantment, level);
    p.sendMessage(
        plugin
            .getSettingsManager()
            .getLanguageSettings()
            .getCommand()
            .getModify()
            .getEnchantment()
            .getAdd());
  }

  @Command(
      identifier = "mythicdrops modify enchantment remove",
      description = "Adds an enchantment to the item in " + "hand",
      permissions = "mythicdrops.command.modify.enchantments")
  public void modifyEnchantmentRemoveCommand(
      CommandSender sender, @Arg(name = "enchantment") Enchantment enchantment) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(
          StringExtensionsKt.chatColorize(
              plugin.getSettingsManager().getLanguageSettings().getCommand().getNoAccess()));
      return;
    }
    Player p = (Player) sender;
    ItemStack itemInHand = p.getEquipment().getItemInMainHand();
    if (itemInHand.getType() == Material.AIR) {
      p.sendMessage(
          plugin.getSettingsManager().getLanguageSettings().getCommand().getModify().getFailure());
      return;
    }
    itemInHand.removeEnchantment(enchantment);
    p.sendMessage(
        plugin
            .getSettingsManager()
            .getLanguageSettings()
            .getCommand()
            .getModify()
            .getEnchantment()
            .getRemove());
  }

  @Command(
      identifier = "mythicdrops combiners",
      description = "Lists the socket gem combiners and their locations",
      permissions = "mythicdrops.command.combiners")
  public void listCombinersCommand(CommandSender sender) {
    plugin
        .getSocketGemCombinerManager()
        .getSocketGemCombiners()
        .forEach(
            socketGemCombiner -> {
              sender.sendMessage(
                  String.format(
                      "%s => %s: %d, %d, %d",
                      socketGemCombiner.getUuid().toString(),
                      socketGemCombiner.getLocation().getWorld().getName(),
                      socketGemCombiner.getLocation().getX(),
                      socketGemCombiner.getLocation().getY(),
                      socketGemCombiner.getLocation().getZ()));
            });
  }

  @Command(
      identifier = "mythicdrops combiner add",
      description = "Adds a socket gem combiner at the block the sender is looking at",
      permissions = "mythicdrops.command.combiner.add",
      onlyPlayers = true)
  public void addCombinerCommand(Player sender) {
    List<Block> blocks = sender.getLineOfSight(Sets.newHashSet(Material.AIR), 10);
    for (Block block : blocks) {
      if (block.getType() == Material.CHEST) {
        Vec3 loc = Vec3.fromLocation(block.getLocation());
        plugin.getSocketGemCombinerManager().addSocketGemCombinerAtLocation(loc);
        plugin.saveSocketGemCombiners();
        sender.sendMessage(
            StringExtensionsKt.chatColorize(
                plugin
                    .getSettingsManager()
                    .getLanguageSettings()
                    .getCommand()
                    .getSocketGemCombinerAdd()
                    .getSuccess()));
        return;
      }
    }
    sender.sendMessage(
        StringExtensionsKt.chatColorize(
            plugin
                .getSettingsManager()
                .getLanguageSettings()
                .getCommand()
                .getSocketGemCombinerAdd()
                .getFailure()));
  }

  @Command(
      identifier = "mythicdrops combiner remove",
      description = "Removes a socket gem combiner at the block the sender is looking at",
      permissions = "mythicdrops.command.combiner.remove",
      onlyPlayers = true)
  public void removeCombinerCommand(Player sender) {
    List<Block> blocks = sender.getLineOfSight(Sets.newHashSet(Material.AIR), 10);
    for (Block block : blocks) {
      if (block.getType() == Material.CHEST) {
        Vec3 loc = Vec3.fromLocation(block.getLocation());
        if (plugin.getSocketGemCombinerManager().isSocketGemCombinerAtLocation(loc)) {
          plugin.getSocketGemCombinerManager().removeSocketGemCombinerAtLocation(loc);
          plugin.saveSocketGemCombiners();
          sender.sendMessage(
              StringExtensionsKt.chatColorize(
                  plugin
                      .getSettingsManager()
                      .getLanguageSettings()
                      .getCommand()
                      .getSocketGemCombinerRemove()
                      .getSuccess()));
          return;
        }
      }
    }
    sender.sendMessage(
        StringExtensionsKt.chatColorize(
            plugin
                .getSettingsManager()
                .getLanguageSettings()
                .getCommand()
                .getSocketGemCombinerRemove()
                .getFailure()));
  }
}
